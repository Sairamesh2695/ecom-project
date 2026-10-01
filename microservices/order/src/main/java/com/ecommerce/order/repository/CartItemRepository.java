package com.ecommerce.order.repository;

import com.ecommerce.order.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    CartItem findByUserAndProductId(Long userId, Long prodId);
    List<CartItem> findByUserId(Long userId);
    void deleteByUserId(Long userId);
    void deleteByUserAndProductId(Long userId, Long prodId);
}
