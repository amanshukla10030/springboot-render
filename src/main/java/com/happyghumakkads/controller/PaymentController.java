package com.happyghumakkads.controller;

import com.happyghumakkads.entity.Booking;
import com.happyghumakkads.service.BookingService;
import com.happyghumakkads.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private BookingService bookingService;

    @Autowired
    private PaymentService paymentService;

    @Value("${razorpay.key-id}")
    private String razorpayKeyId;

    @Value("${razorpay.key-secret}")
    private String razorpayKeySecret;

    @GetMapping("/checkout")
    public String checkout(@RequestParam("ref") String bookingRef, Model model) {
        logger.info("Checkout initiated for booking reference: {}", bookingRef);
        Booking booking = bookingService.findByBookingReference(bookingRef).orElse(null);
        if (booking == null) {
            logger.error("Booking not found: {}", bookingRef);
            return "redirect:/home";
        }
        try {
            int amountPaise = booking.getTotalAmount().multiply(new BigDecimal(100)).intValue();
            logger.info("Creating Razorpay order for amount: {} paise", amountPaise);
            Order order = paymentService.createOrder(amountPaise, bookingRef);
            String orderId = order.get("id");
            bookingService.attachRazorpayOrder(bookingRef, orderId);
            logger.info("Razorpay order created successfully: {}", orderId);

            model.addAttribute("booking", booking);
            model.addAttribute("orderId", orderId);
            model.addAttribute("amountPaise", amountPaise);
            model.addAttribute("keyId", razorpayKeyId);
        } catch (Exception e) {
            logger.error("Failed to initialize payment for booking: {}", bookingRef, e);
            model.addAttribute("error", "Failed to initialize payment: " + e.getMessage());
            return "payment/error";
        }
        return "payment/checkout";
    }

    @PostMapping("/success")
    public String paymentSuccess(@RequestParam("razorpay_payment_id") String paymentId,
                                 @RequestParam("razorpay_order_id") String orderId,
                                 @RequestParam("razorpay_signature") String signature,
                                 Model model) {
        logger.info("Payment success callback received for order: {}", orderId);
        
        try {
            // Verify signature for security
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", orderId);
            options.put("razorpay_payment_id", paymentId);
            options.put("razorpay_signature", signature);
            
            boolean isValidSignature = Utils.verifyPaymentSignature(options, razorpayKeySecret);
            
            if (!isValidSignature) {
                logger.error("Invalid signature for order: {}", orderId);
                model.addAttribute("error", "Payment verification failed. Invalid signature.");
                model.addAttribute("orderId", orderId);
                return "payment/failed";
            }
            
            logger.info("Signature verified successfully for order: {}", orderId);
            Booking booking = bookingService.updatePaymentStatus(orderId, Booking.PaymentStatus.PAID, paymentId);
            
            if (booking == null) {
                logger.error("Booking not found for order: {}", orderId);
                model.addAttribute("error", "Booking not found for this order.");
                model.addAttribute("orderId", orderId);
                return "payment/failed";
            }
            
            logger.info("Payment completed successfully for booking: {}", booking.getBookingReference());
            model.addAttribute("booking", booking);
            return "payment/success";
            
        } catch (Exception e) {
            logger.error("Error processing payment success for order: {}", orderId, e);
            model.addAttribute("error", "Error processing payment: " + e.getMessage());
            model.addAttribute("orderId", orderId);
            return "payment/failed";
        }
    }

    @PostMapping("/failed")
    public String paymentFailed(@RequestParam(value = "razorpay_order_id", required = false) String orderId,
                                @RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "error_code", required = false) String errorCode,
                                Model model) {
        logger.warn("Payment failed for order: {} - Error: {} (Code: {})", orderId, error, errorCode);
        
        // Update booking payment status to FAILED if order ID exists
        if (orderId != null && !orderId.isEmpty()) {
            try {
                bookingService.updatePaymentStatus(orderId, Booking.PaymentStatus.FAILED, null);
            } catch (Exception e) {
                logger.error("Error updating payment status to FAILED for order: {}", orderId, e);
            }
        }
        
        model.addAttribute("orderId", orderId);
        model.addAttribute("error", error != null ? error : "Payment was cancelled or failed");
        model.addAttribute("errorCode", errorCode);
        return "payment/failed";
    }

    @PostMapping("/create-custom-order")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createCustomOrder(@RequestBody Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Double amount = ((Number) payload.get("amount")).doubleValue();
            
            if (amount == null || amount < 1) {
                response.put("error", "Invalid amount. Minimum ₹1 required.");
                return ResponseEntity.badRequest().body(response);
            }
            
            int amountPaise = (int) (amount * 100);
            String receipt = "CUSTOM_" + System.currentTimeMillis();
            
            logger.info("Creating custom payment order for amount: ₹{} ({} paise)", amount, amountPaise);
            
            Order order = paymentService.createOrder(amountPaise, receipt);
            String orderId = order.get("id");
            
            logger.info("Custom payment order created successfully: {}", orderId);
            
            response.put("orderId", orderId);
            response.put("amountPaise", amountPaise);
            response.put("keyId", razorpayKeyId);
            response.put("customerName", "Guest");
            response.put("customerEmail", "");
            response.put("customerPhone", "");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error creating custom payment order", e);
            response.put("error", "Failed to create payment order: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/custom-success")
    public String customPaymentSuccess(@RequestParam("razorpay_payment_id") String paymentId,
                                      @RequestParam("razorpay_order_id") String orderId,
                                      @RequestParam("razorpay_signature") String signature,
                                      @RequestParam("paidAmount") BigDecimal paidAmount,
                                      Model model) {
        logger.info("Custom payment success callback - Order: {}, Amount: ₹{}", orderId, paidAmount);
        
        try {
            // Verify signature
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", orderId);
            options.put("razorpay_payment_id", paymentId);
            options.put("razorpay_signature", signature);
            
            boolean isValidSignature = Utils.verifyPaymentSignature(options, razorpayKeySecret);
            
            if (!isValidSignature) {
                logger.error("Invalid signature for custom payment order: {}", orderId);
                model.addAttribute("error", "Payment verification failed. Invalid signature.");
                return "payment/custom-payment-result";
            }
            
            logger.info("Custom payment verified successfully - Payment ID: {}, Amount: ₹{}", paymentId, paidAmount);
            
            model.addAttribute("success", true);
            model.addAttribute("paymentId", paymentId);
            model.addAttribute("orderId", orderId);
            model.addAttribute("amount", paidAmount);
            model.addAttribute("message", "Payment of ₹" + paidAmount + " received successfully!");
            
            return "payment/custom-payment-result";
            
        } catch (Exception e) {
            logger.error("Error processing custom payment success", e);
            model.addAttribute("success", false);
            model.addAttribute("error", "Error processing payment: " + e.getMessage());
            return "payment/custom-payment-result";
        }
    }
}
