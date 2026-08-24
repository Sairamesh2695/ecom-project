package com.app.ecom.dto.cart;

import com.app.ecom.model.Product;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemResponse {
    private Long id;
    private Product product;
    private Integer quantity;
    private BigDecimal price;
}
