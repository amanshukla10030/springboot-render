package com.happyghumakkads.controller;

import com.happyghumakkads.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping
public class PagesController {

    @Autowired
    private EmailService emailService;

    // Contact
    @GetMapping("/contact")
    public String contact(Model model) {
        return "pages/contact";
    }

    @PostMapping("/contact")
    public String submitContact(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String travelType,
            @RequestParam String message,
            Model model) {
        if (name == null || name.isBlank() || email == null || email.isBlank() || message == null || message.isBlank()) {
            model.addAttribute("error", "Please provide your name, email, and a message.");
            return "pages/contact";
        }
        try { 
            emailService.sendContactMessage(name, email, phone, travelType, message); 
        } catch (Exception e) {
            model.addAttribute("error", "Failed to send your message. Please try again later.");
            return "pages/contact";
        }
        model.addAttribute("success", "Thank you! We will get back to you soon.");
        return "pages/contact";
    }

    // About
    @GetMapping("/about")
    public String about(Model model) {
        return "pages/about";
    }

    // Careers
    @GetMapping("/careers")
    public String careers(Model model) {
        return "pages/careers";
    }

    // International Tour
    @GetMapping("/international-tour")
    public String international(Model model) {
        return "pages/international";
    }

    // Industrial Tour
    @GetMapping("/industrial-tour")
    public String industrial(Model model) {
        return "pages/industrial";
    }

    // Students Group Tour
    @GetMapping("/students-group-tour")
    public String students(Model model) {
        return "pages/students";
    }

    // Domestic Tour
    @GetMapping("/domestic-tour")
    public String domestic(Model model) {
        return "pages/domestic";
    }

    // Privacy Policy
    @GetMapping("/privacy-policy")
    public String privacyPolicy(Model model) {
        return "privacy-policy";
    }

    // Terms of Service
    @GetMapping("/terms-of-service")
    public String termsOfService(Model model) {
        return "terms-of-service";
    }

    // Refund Policy
    @GetMapping("/refund-policy")
    public String refundPolicy(Model model) {
        return "refund-policy";
    }
}
