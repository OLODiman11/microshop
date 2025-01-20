package by.olodiman11.microshop.orderservice.service;

import by.olodiman11.microshop.orderservice.event.OrderPlacedEvent;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import by.olodiman11.microshop.orderservice.dto.InventoryResponse;
import by.olodiman11.microshop.orderservice.dto.OrderLineItemsDto;
import by.olodiman11.microshop.orderservice.dto.OrderRequest;
import by.olodiman11.microshop.orderservice.model.Order;
import by.olodiman11.microshop.orderservice.model.OrderLineItems;
import by.olodiman11.microshop.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final Tracer tracer;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    @Transactional
    public String placeOrder(OrderRequest orderRequest) {
        List<String> skuCodes = orderRequest.getOrderLineItemsDtoList().stream()
            .map(OrderLineItemsDto::getSkuCode)
            .toList();

        Span inventoryServiceLookup = tracer.nextSpan().name("InventoryServiceLookup");

        try(Tracer.SpanInScope spanInScope = tracer.withSpan(inventoryServiceLookup.start())) {
            InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
                    .uri(
                            "http://inventory-service/api/inventory",
                            uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();

            boolean allInStock = Arrays.stream(inventoryResponses)
                    .allMatch(InventoryResponse::isInStock);

            if (!allInStock)
                throw new IllegalArgumentException("Not all product are in stock");

            List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList().stream()
                    .map(this::mapToOrderLineItems)
                    .toList();
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setOrderLineItemsList(orderLineItemsList);

            orderRepository.save(order);
            OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent(order.getOrderNumber());
            kafkaTemplate.send("notificationTopic", orderPlacedEvent);
            return "Order Placed Successfully";
        }
        finally {
            inventoryServiceLookup.end();
        }

    }

    private OrderLineItems mapToOrderLineItems(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        return orderLineItems;
    }
}
