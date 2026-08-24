package com.app.ecom.service;

import com.app.ecom.config.enums.OrderStatus;
import com.app.ecom.dto.orders.OrderItemDTO;
import com.app.ecom.dto.orders.OrderResponse;
import com.app.ecom.model.CartItem;
import com.app.ecom.model.Order;
import com.app.ecom.model.OrderItem;
import com.app.ecom.model.User;
import com.app.ecom.repository.OrderRepository;
import com.app.ecom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final UserRepository userRepository;


    public Optional<OrderResponse> placeOrder(String userId) {
        //validate the cart items
        List<CartItem> cartItems = cartService.getCart(userId);
        if(cartItems.isEmpty()){
            return Optional.empty();
        }
        //validate the user
        Optional<User> userOp = userRepository.findById(Integer.valueOf(userId));
        if (userOp.isEmpty()){
            return Optional.empty();
        }
        User user = userOp.orElse(null);

        //Calculate the total price
        BigDecimal totalPrice = cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //Create Order
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotal(totalPrice);

        List<OrderItem> orderItems = cartItems.stream().map(getCartItemOrderItemFunction(order)).toList();

        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        //Clear the cart if order placed
        cartService.clearCart(userId);

        return Optional.of(mapToOrderResponse(savedOrder));
    }

    private static Function<CartItem, OrderItem> getCartItemOrderItemFunction(Order order) {
        return item ->
                new OrderItem(
                        null,
                        item.getProduct(),
                        item.getQuantity(),
                        item.getPrice(),
                        order
                );
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTotal(),
                order.getStatus(),
                order.getItems().stream()
                        .map(orderItem -> new OrderItemDTO(
                                orderItem.getId(),
                                orderItem.getProduct().getId(),
                                orderItem.getQuantity(),
                                orderItem.getPrice(),
                                orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity()))
                        )
                ).toList(),
                order.getCreatedAt()
        );
    }
}
