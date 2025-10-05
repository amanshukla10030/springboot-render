package com.happyghumakkads.service;

import com.happyghumakkads.entity.Booking;
import com.happyghumakkads.entity.Package;
import com.happyghumakkads.entity.User;
import com.happyghumakkads.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EmailService emailService;

    public Booking createBooking(User user, Package packageEntity, LocalDate travelDate,
                                 Integer numberOfTravelers, String specialRequests) {
        BigDecimal totalAmount = packageEntity.getPrice().multiply(new BigDecimal(numberOfTravelers));
        String bookingReference = generateBookingReference();

        Booking booking = new Booking(bookingReference, user, packageEntity, travelDate,
                numberOfTravelers, totalAmount);
        booking.setSpecialRequests(specialRequests);

        Booking saved = bookingRepository.save(booking);

        try {
            emailService.sendBookingConfirmationEmail(saved);
            emailService.sendAdminBookingNotification(saved);
        } catch (Exception e) {
            System.err.println("Email sending failed: " + e.getMessage());
        }
        return saved;
    }

    public Optional<Booking> findById(Long id) { return bookingRepository.findById(id); }

    public Optional<Booking> findByBookingReference(String ref) { return bookingRepository.findByBookingReference(ref); }

    public List<Booking> getBookingsByUser(User user) { return bookingRepository.findByUser(user); }

    public List<Booking> getBookingsByUserId(Long userId) { return bookingRepository.findByUserIdOrderByCreatedAtDesc(userId); }

    public List<Booking> getAllBookings() { return bookingRepository.findAll(); }

    public List<Booking> getBookingsByStatus(Booking.BookingStatus status) { return bookingRepository.findByStatus(status); }

    public List<Booking> getBookingsByPaymentStatus(Booking.PaymentStatus ps) { return bookingRepository.findByPaymentStatus(ps); }

    public Booking update(Booking booking) { return bookingRepository.save(booking); }

    public Booking markPaymentPaid(String razorpayOrderId, String razorpayPaymentId) {
        Optional<Booking> bookingOpt = bookingRepository.findByRazorpayOrderId(razorpayOrderId);
        if (bookingOpt.isEmpty()) return null;
        Booking booking = bookingOpt.get();
        booking.setPaymentStatus(Booking.PaymentStatus.PAID);
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        booking.setRazorpayPaymentId(razorpayPaymentId);
        bookingRepository.save(booking);
        try {
            emailService.sendPaymentConfirmationEmail(booking);
        } catch (Exception e) {
            System.err.println("Payment email failed: " + e.getMessage());
        }
        return booking;
    }

    // Compatibility method used by PaymentController
    public Booking updatePaymentStatus(String razorpayOrderId, Booking.PaymentStatus paymentStatus, String razorpayPaymentId) {
        Optional<Booking> bookingOpt = bookingRepository.findByRazorpayOrderId(razorpayOrderId);
        if (bookingOpt.isEmpty()) return null;
        Booking booking = bookingOpt.get();
        booking.setPaymentStatus(paymentStatus);
        if (paymentStatus == Booking.PaymentStatus.PAID) {
            booking.setStatus(Booking.BookingStatus.CONFIRMED);
            booking.setRazorpayPaymentId(razorpayPaymentId);
            bookingRepository.save(booking);
            try {
                emailService.sendPaymentConfirmationEmail(booking);
            } catch (Exception e) {
                System.err.println("Payment email failed: " + e.getMessage());
            }
        } else {
            bookingRepository.save(booking);
        }
        return booking;
    }

    public void cancelBooking(Long id) {
        bookingRepository.findById(id).ifPresent(b -> {
            b.setStatus(Booking.BookingStatus.CANCELLED);
            bookingRepository.save(b);
            try { emailService.sendBookingCancellationEmail(b); } catch (Exception ignored) {}
        });
    }

    public List<Booking> getBetween(LocalDateTime start, LocalDateTime end) { return bookingRepository.findBookingsBetweenDates(start, end); }

    public long getConfirmedCount() { return bookingRepository.countConfirmedBookings(); }

    public Double getTotalRevenue() { return bookingRepository.getTotalRevenue(); }

    public void attachRazorpayOrder(String bookingRef, String orderId) {
        bookingRepository.findByBookingReference(bookingRef).ifPresent(b -> {
            b.setRazorpayOrderId(orderId);
            bookingRepository.save(b);
        });
    }

    private String generateBookingReference() {
        return "HG" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}
