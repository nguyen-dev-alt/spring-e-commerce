package com.app.e_commerce.services;

import com.app.e_commerce.Enum.OrderStatus;
import com.app.e_commerce.Enum.PaymentMethod;
import com.app.e_commerce.entity.*;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;

@Service
public class CartService {

    @Autowired
    private  OrderService orderService;
    @Autowired
    private MailService mailService;
    @Autowired
    private UserService userService;


    public Cart getCartFromSession(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }


    public void addItemToCart(Product product, int quantity, HttpSession session) {
        Cart cart = getCartFromSession(session);

        // Check if the item already exists in the cart
        CartItem existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            // If the product is already in the cart, update the quantity
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            // If the product is new to the cart, add it as a new CartItem
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setCart(cart);
            cart.getCartItems().add(cartItem);
        }

        // Update the total price of the cart
        updateTotalPrice(cart);

        // Save cart back into the session
        session.setAttribute("cart", cart);

    }

    public void removeItemFromCart(Long productId, HttpSession session) {
        Cart cart = getCartFromSession(session);
        cart.getCartItems().removeIf(item -> item.getProduct().getId().equals(productId));
        updateTotalPrice(cart);
        session.setAttribute("cart", cart);
    }

    public void updateCartItem(Long productId, int quantity, HttpSession session) {
        Cart cart = getCartFromSession(session);
        cart.getCartItems().forEach(item -> {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(quantity);
            }
        });
        updateTotalPrice(cart);
        session.setAttribute("cart", cart);
    }


    private void updateTotalPrice(Cart cart) {
        BigDecimal total = cart.getCartItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalPrice(total);
    }
    public void checkoutCart(String address, PaymentMethod paymentMethod, HttpSession session, Principal principal) {
        // Retrieve logged-in user details from Principal
        String username = principal.getName();  // This will return the username (usually email or username)

        // Fetch the user entity from the database using the username
        User user = userService.findByUsername(username);  // Assuming you have a userService to fetch user details

        Cart cart = getCartFromSession(session);
        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(address);
        order.setPaymentMethod(paymentMethod);
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderDate(LocalDateTime.now());

        order.setOrderStatus(OrderStatus.PENDING);  // Set status to PENDING

        // Add cart items to the order
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }

        // Save the order and send confirmation email to the user's email
        orderService.createOrder(order);

        // Send confirmation email to the user
        mailService.sendOrderConfirmationMail(user, order);

        session.removeAttribute("cart");  // Clear cart after checkout
    }

}
