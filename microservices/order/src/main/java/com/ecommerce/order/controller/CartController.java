package com.ecommerce.order.controller;

import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.dto.CartItemResponse;
import com.ecommerce.order.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<String> addToCart(@RequestHeader("X-User-ID") String id, @RequestBody CartItemRequest cartRequest){
        boolean added = cartService.addToCart(id,cartRequest);
        return added ? ResponseEntity.status(HttpStatus.CREATED).body("Added to cart")
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product out of stock or User not found or Product not found");
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<String> deleteFromCart(@RequestHeader("X-User-ID") String userId, @PathVariable Long productId){
        boolean deleted = cartService.deleteFromCart(userId, productId);
        return deleted ? ResponseEntity.status(HttpStatus.OK).body("Deleted from cart")
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product out of stock or User not found or Product not found");
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> showCart(@RequestHeader("X-User-ID") String userId){
        return ResponseEntity.ok(cartService.showCart(userId));
    }
}
