package com.happyghumakkads.service;

import com.happyghumakkads.entity.User;
import com.happyghumakkads.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EmailService emailService;
    
    public User saveUser(User user) {
        // In a real application, you would hash the password here
        User savedUser = userRepository.save(user);
        
        // Send welcome email
        try {
            emailService.sendWelcomeEmail(savedUser);
        } catch (Exception e) {
            // Log the error but don't fail the registration
            System.err.println("Failed to send welcome email: " + e.getMessage());
        }
        
        return savedUser;
    }
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public Optional<User> authenticateUser(String email, String password) {
        // In a real application, you would hash the password and compare
        return userRepository.findByEmailAndPassword(email, password);
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public User updateUser(User user) {
        return userRepository.save(user);
    }
    
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    public long getCustomerCount() {
        return userRepository.countCustomers();
    }
}
