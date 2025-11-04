package com.order.external;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceClientImpl implements PaymentServiceClient {

    private final RestTemplate restTemplate;

    public PaymentServiceClientImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public boolean processPayment(Long orderId, double amount, String method) {
        try {
            String paymentUrl = "http://payment-service:8080/api/payments";

            Map<String, Object> body = new HashMap<>();
            body.put("orderId", orderId);
            body.put("amount", amount);
            body.put("method", method);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(paymentUrl, request, String.class);

            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            System.err.println("❌ Payment API call failed: " + e.getMessage());
            return false;
        }
    }
}
