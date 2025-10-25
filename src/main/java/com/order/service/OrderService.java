package com.order.service;

import com.order.model.Order;
import com.order.model.Ticket;
import com.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order placeOrder(Long userId, Long eventId, List<Long> seatIds, List<Double> seatPrices) {

        // 1. Create order
        Order order = new Order();
        order.setUserId(userId);
        order.setEventId(eventId);
        order.setStatus("CONFIRMED");
        order.setPaymentStatus("SUCCESS"); // Mock for now
        order.setCreatedAt(LocalDateTime.now());

        // 2. Compute total
        double total = seatPrices.stream().mapToDouble(Double::doubleValue).sum();
        total += total * 0.05; // 5% tax
        order.setOrderTotal(total);

        // 3. Generate tickets
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < seatIds.size(); i++) {
            Ticket ticket = new Ticket();
            ticket.setEventId(eventId);
            ticket.setSeatId(seatIds.get(i));
            ticket.setPricePaid(seatPrices.get(i));
            ticket.setOrder(order); // Important
            tickets.add(ticket);
        }
        order.setTickets(tickets);

        // 4. Save order (tickets saved automatically via cascade)
        return orderRepository.save(order);
    }

    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    // ✅ GET ALL ORDERS
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // ✅ CANCEL ORDER
    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        order.setStatus("CANCELLED");
        order.setPaymentStatus("REFUND_INITIATED");

        return orderRepository.save(order);
    }
}
