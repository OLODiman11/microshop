package by.olodiman11.microshop.orderservice.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "t_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String orderNumber;

    @OneToMany(
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        mappedBy = "order",
        orphanRemoval = true
    )
    private List<OrderLineItems> orderLineItemsList;

    public Order setOrderLineItemsList(List<OrderLineItems> orderLineItemsList) {
        this.orderLineItemsList = orderLineItemsList;
        orderLineItemsList.stream().forEach(item -> item.setOrder(this));
        return this;
    }

    public Order addOrderLineItems(OrderLineItems orderLineItems) {
        orderLineItemsList.add(orderLineItems);
        orderLineItems.setOrder(this);
        return this;
    }

    public Order removeOrderLineItems(OrderLineItems orderLineItems) {
        orderLineItemsList.remove(orderLineItems);
        orderLineItems.setOrder(null);
        return this;
    }
}
