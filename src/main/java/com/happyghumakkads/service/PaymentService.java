package com.happyghumakkads.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Value("${razorpay.key-id}")
    private String keyId;

    @Value("${razorpay.key-secret}")
    private String keySecret;

    public Order createOrder(int amountInPaise, String receipt) throws Exception {
        logger.info("Creating Razorpay order - Amount: {} paise, Receipt: {}", amountInPaise, receipt);
        
        // Validate credentials are configured
        if (keyId == null || keyId.isEmpty() || keyId.equals("your_key_id")) {
            logger.error("Razorpay Key ID is not properly configured");
            throw new IllegalStateException("Razorpay Key ID is not configured. Please check application.yml");
        }
        
        if (keySecret == null || keySecret.isEmpty() || keySecret.equals("your_key_secret")) {
            logger.error("Razorpay Key Secret is not properly configured");
            throw new IllegalStateException("Razorpay Key Secret is not configured. Please check application.yml");
        }
        
        try {
            RazorpayClient client = new RazorpayClient(keyId, keySecret);
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amountInPaise); // amount in paise
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", receipt);
            orderRequest.put("payment_capture", 1); // Auto capture payment
            
            Order order = client.orders.create(orderRequest);
            String orderId = (String) order.get("id");
            logger.info("Razorpay order created successfully - Order ID: {}", orderId);
            return order;
            
        } catch (RazorpayException e) {
            logger.error("Razorpay API error while creating order: {}", e.getMessage(), e);
            throw new Exception("Failed to create Razorpay order: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while creating Razorpay order", e);
            throw new Exception("Unexpected error creating payment order: " + e.getMessage(), e);
        }
    }
}
