package com.app.ecom.dto.product;

import com.app.ecom.config.enums.ProductCategory;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer units;
    private String category;
    private String imageUrl;
    private Boolean isActive;
}
