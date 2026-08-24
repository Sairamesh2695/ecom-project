package com.app.ecom.service;

import com.app.ecom.dto.cart.CartItemRequest;
import com.app.ecom.dto.cart.CartItemResponse;
import com.app.ecom.model.CartItem;
import com.app.ecom.model.Product;
import com.app.ecom.model.User;
import com.app.ecom.repository.CartItemRepository;
import com.app.ecom.repository.ProductRepository;
import com.app.ecom.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public boolean addToCart(String id, CartItemRequest cartRequest) {
        Optional<Product> product = productRepository.findById(cartRequest.getProductId());
        Product prod = product.orElse(null);
        if(prod == null) return false;
        if(prod.getUnits() < cartRequest.getUnits()){
            return false;
        }

        Optional<User> userOp = userRepository.findById(Integer.valueOf(id));
        User user = userOp.orElse(null);
        if(user == null) return false;
        CartItem existingCartItem = cartItemRepository.findByUserAndProduct(user,prod);

        if(existingCartItem != null){
            //If existing cart item is present, we can update the quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartRequest.getUnits());
            existingCartItem.setPrice(prod.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            cartItemRepository.save(existingCartItem);
        }else{
            //Create a new cart item
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(prod);
            cartItem.setQuantity(cartRequest.getUnits());
            cartItem.setPrice(prod.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            cartItemRepository.save(cartItem);
        }
        return true;
    }

    public boolean deleteFromCart(String userId, Long productId) {
        Optional<Product> productOp = productRepository.findById(productId);
        Product product = productOp.orElse(null);
        if(product == null) return false;

        Optional<User> userOp = userRepository.findById(Integer.valueOf(userId));
        User user = userOp.orElse(null);
        if(user == null) return false;

        CartItem existing = cartItemRepository.findByUserAndProduct(user,product);

        if(existing == null){
            return false;
        }

        if(existing.getQuantity() > 1){
            existing.setQuantity(existing.getQuantity() - 1);
            existing.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existing.getQuantity())));
            cartItemRepository.save(existing);
        }else{
            cartItemRepository.delete(existing);
        }
        return true;
    }

    public List<CartItemResponse> showCart(String userId) {
        return userRepository.findById(Integer.valueOf(userId))
                .map(cartItemRepository::findByUser)
                .orElseGet(List::of)
                .stream()
                .map(this::mapToCartItemResponse)
                .collect(Collectors.toList());
    }

    public List<CartItem> getCart(String userId) {
        return userRepository.findById(Integer.valueOf(userId))
                .map(cartItemRepository::findByUser)
                .orElseGet(List::of);
    }

    public CartItemResponse mapToCartItemResponse(CartItem cartItem){
        CartItemResponse cartItemResponse = new CartItemResponse();

        cartItemResponse.setId(cartItem.getId());
        cartItemResponse.setProduct(cartItem.getProduct());
        cartItemResponse.setQuantity(cartItem.getQuantity());
        cartItemResponse.setPrice(cartItem.getPrice());

        return cartItemResponse;
    }

    @Transactional
    public void clearCart(String userId){
        cartItemRepository.deleteByUserId(userId);
    }
}
