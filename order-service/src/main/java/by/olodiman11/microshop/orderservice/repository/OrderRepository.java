package by.olodiman11.microshop.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import by.olodiman11.microshop.orderservice.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
