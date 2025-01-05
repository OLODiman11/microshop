package by.olodiman11.microshop.productservice.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    String name;
    String description;
    BigDecimal price;
}
