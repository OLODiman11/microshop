package by.olodiman11.microshop.inventoryservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import by.olodiman11.microshop.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public boolean isInStock(String skuCode) {
        return inventoryRepository.findBySkuCode(skuCode).isPresent();
    }
}
