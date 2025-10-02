package com.happyghumakkads.controller;

import com.happyghumakkads.dto.BookingRequest;
import com.happyghumakkads.entity.Booking;
import com.happyghumakkads.entity.Package;
import com.happyghumakkads.entity.User;
import com.happyghumakkads.service.BookingService;
import com.happyghumakkads.service.PackageService;
import com.happyghumakkads.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private PackageService packageService;

    @Autowired
    private UserService userService;

    @GetMapping("/new/{packageId}")
    public String bookingForm(@PathVariable Long packageId, Model model) {
        Package pkg = packageService.findById(packageId).orElse(null);
        if (pkg == null) return "redirect:/packages";
        BookingRequest req = new BookingRequest();
        req.setPackageId(packageId);
        req.setTravelDate(LocalDate.now().plusDays(7));
        req.setTravelers(1);
        // Initialize guest booking fields
        req.setFullName("");
        req.setEmail("");
        req.setPhone("");
        model.addAttribute("pkg", pkg);
        model.addAttribute("req", req);
        return "booking/form";
    }

    @PostMapping
    public String createBooking(@Valid @ModelAttribute("req") BookingRequest req,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            packageService.findById(req.getPackageId()).ifPresent(pkg -> model.addAttribute("pkg", pkg));
            return "booking/form";
        }
        Package pkg = packageService.findById(req.getPackageId()).orElse(null);
        if (pkg == null) {
            model.addAttribute("error", "Package not found");
            packageService.findById(req.getPackageId()).ifPresent(p -> model.addAttribute("pkg", p));
            return "booking/form";
        }

        // Find or create user for guest booking
        User user = userService.findByEmail(req.getEmail()).orElse(null);
        if (user == null) {
            // Create new user for guest booking
            // Split full name into first and last name
            String[] nameParts = req.getFullName().trim().split("\\s+", 2);
            String firstName = nameParts[0];
            String lastName = nameParts.length > 1 ? nameParts[1] : "User"; // Use "User" for single name

            user = new User(firstName, lastName, req.getEmail(), req.getPhone(), "guest-booking");
            user = userService.saveUser(user);
        }

        Booking booking = bookingService.createBooking(user, pkg, req.getTravelDate(), req.getTravelers(), req.getSpecialRequests());
        return "redirect:/payment/checkout?ref=" + booking.getBookingReference();
    }

    @GetMapping("/{ref}")
    public String bookingDetail(@PathVariable String ref, Model model) {
        Booking booking = bookingService.findByBookingReference(ref).orElse(null);
        if (booking == null) return "redirect:/home";
        model.addAttribute("booking", booking);
        return "booking/detail";
    }
}
