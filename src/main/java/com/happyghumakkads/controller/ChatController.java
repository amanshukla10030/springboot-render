package com.happyghumakkads.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> chat(@RequestBody Map<String, Object> payload) {
        String message = String.valueOf(payload.getOrDefault("message", "")).toLowerCase();
        String reply;
        if (message.contains("package") || message.contains("tour")) {
            reply = "You can browse our latest packages at /packages. Tell me your destination and dates, and I can suggest options.";
        } else if (message.contains("book")) {
            reply = "To book, open a package and click Book Now. If you'd like, share travelers and dates here and I can guide you.";
        } else if (message.contains("contact") || message.contains("phone") || message.contains("email")) {
            reply = "You can reach us at +91 84471 33338 or contact@happyghumakkads.com. I can also submit a contact request for you.";
        } else if (message.contains("price") || message.contains("cost")) {
            reply = "Package prices are shown per person. Open a package to see details; custom quotes are also available.";
        } else {
            reply = "Hi! I'm your travel assistant. Ask me about packages, bookings, prices, or how to contact our team.";
        }

        Map<String, Object> res = new HashMap<>();
        res.put("reply", reply);
        res.put("timestamp", LocalDateTime.now().toString());
        return res;
    }
}
