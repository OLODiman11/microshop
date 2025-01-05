package by.olodiman11.microshop.inventoryservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import by.olodiman11.microshop.inventoryservice.model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findBySkuCode(String skuCode);
}
