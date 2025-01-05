package by.olodiman11.microshop.orderservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void placeOrder(OrderRequest orderRequest) {
        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList().stream()
            .map(this::mapToOrderLineItems)
            .toList();
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderLineItemsList(orderLineItemsList);

        orderRepository.save(order);
    }

    private OrderLineItems mapToOrderLineItems(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        return orderLineItems;
    }
}
