package com.order.service;

import com.order.model.Order;
import com.order.model.Ticket;
import com.order.repository.OrderRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    // URL of payment service inside Docker
    private static final String PAYMENT_SERVICE_URL = "http://payment-service:8080/api/payments";

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.restTemplate = new RestTemplate();
    }

    public Order placeOrder(Long userId, Long eventId, List<Long> seatIds, List<Double> seatPrices) {
System.out.println("oooooooooooooooooooooooooooooooooooooooooooooooooo");
        // 1️⃣ Create Order
        Order order = new Order();
        order.setUserId(userId);
        order.setEventId(eventId);
        order.setStatus("CREATED");
        order.setCreatedAt(LocalDateTime.now());

        // 2️⃣ Calculate total with 5% tax
        double total = seatPrices.stream().mapToDouble(Double::doubleValue).sum();
        total += total * 0.05;
        order.setOrderTotal(total);

        // 3️⃣ Generate tickets (attached to order)
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < seatIds.size(); i++) {
            Ticket ticket = new Ticket();
            ticket.setEventId(eventId);
            ticket.setSeatId(seatIds.get(i));
            ticket.setPricePaid(seatPrices.get(i));
            ticket.setOrder(order);
            tickets.add(ticket);
        }
        order.setTickets(tickets);

        // 4️⃣ Save order before payment (to get orderId)
        Order savedOrder = orderRepository.save(order);

        try {
            // 5️⃣ Prepare payment payload
            Map<String, Object> paymentRequest = new HashMap<>();
            paymentRequest.put("orderId", savedOrder.getOrderId());
            paymentRequest.put("amount", total);
            paymentRequest.put("method", "UPI");

            // 6️⃣ Call payment-service API
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(paymentRequest, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    PAYMENT_SERVICE_URL,
                    HttpMethod.POST,
                    entity,
                    String.class
            );
            System.out.println("➡️ Payment API Response: " + response.getStatusCode() + " | Body: " + response.getBody());
            if (response.getStatusCode().is2xxSuccessful()) {
                savedOrder.setPaymentStatus("SUCCESS");
                savedOrder.setStatus("CONFIRMED");
            } else {
                savedOrder.setPaymentStatus("FAILED");
                savedOrder.setStatus("PAYMENT_FAILED");
            }
        } catch (Exception e) {
            e.printStackTrace();
            savedOrder.setPaymentStatus("ERROR");
            savedOrder.setStatus("PAYMENT_FAILED");
        }

        // 7️⃣ Save final state
        return orderRepository.save(savedOrder);
    }

    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        order.setStatus("CANCELLED");
        order.setPaymentStatus("REFUND_INITIATED");
        return orderRepository.save(order);
    }
}
