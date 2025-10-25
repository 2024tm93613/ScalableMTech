package com.order.controller;

import com.order.model.Order;
import com.order.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Accept JSON with seatIds and seatPrices as lists
    @PostMapping("/place")
    public Order placeOrder(@RequestBody Map<String, Object> orderData) {
        Long userId = Long.valueOf(orderData.get("userId").toString());
        Long eventId = Long.valueOf(orderData.get("eventId").toString());

        // Convert JSON arrays to Java Lists
        List<Integer> seatIdsInt = (List<Integer>) orderData.get("seatIds");
        List<Double> seatPrices = (List<Double>) orderData.get("seatPrices");

        // Convert seatIds to Long
        List<Long> seatIds = seatIdsInt.stream()
                .map(Long::valueOf)
                .toList();

        return orderService.placeOrder(userId, eventId, seatIds, seatPrices);
    }

    @GetMapping("/{orderId}")
    public Order getOrder(@PathVariable Long orderId) {
        return orderService.getOrder(orderId);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PutMapping("/{orderId}/cancel")
    public Order cancelOrder(@PathVariable Long orderId) {
        return orderService.cancelOrder(orderId);
    }

}
