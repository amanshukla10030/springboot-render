package com.happyghumakkads.repository;

import com.happyghumakkads.entity.Booking;
import com.happyghumakkads.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    List<Booking> findByUser(User user);
    
    Optional<Booking> findByBookingReference(String bookingReference);
    
    List<Booking> findByStatus(Booking.BookingStatus status);
    
    List<Booking> findByPaymentStatus(Booking.PaymentStatus paymentStatus);
    
    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId ORDER BY b.createdAt DESC")
    List<Booking> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);
    
    @Query("SELECT b FROM Booking b WHERE b.createdAt BETWEEN :startDate AND :endDate")
    List<Booking> findBookingsBetweenDates(@Param("startDate") LocalDateTime startDate, 
                                          @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.status = 'CONFIRMED'")
    long countConfirmedBookings();
    
    @Query("SELECT SUM(b.totalAmount) FROM Booking b WHERE b.paymentStatus = 'PAID'")
    Double getTotalRevenue();
    
    Optional<Booking> findByRazorpayOrderId(String razorpayOrderId);
}
