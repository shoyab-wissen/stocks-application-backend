package com.stocks.registration.services;

import com.stocks.registration.models.User;
import com.stocks.registration.models.UserDTO;
import com.stocks.registration.models.UserVerif;
import com.stocks.registration.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender emailSender;

    public int sendMessage(User user) {
        String subject = "Code for forgot password";
        int code = (int) (Math.random() * 1000000);
        String body = "Your code is " + code;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(subject);
        message.setText(body);
        message.setTo(user.getEmail());
        try {
            emailSender.send(message);
            return code;
        } catch (Exception e) {
            return -1;
        }
    }

    public User authenticateAndGetUser(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // In production, use proper password hashing
            if (user.getPassword().equals(password)) {
                // Set user as active when they log in
                user.setActive(true);
                return userRepository.save(user);
            }
        }
        return null;
    }

    // Add logout method
    public boolean logoutUser(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setActive(false);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    // Registration logic
    public User registerUser(User user) {
        // In production, add checks for existing email, password hashing, etc.
        Optional<User> users = userRepository.findByEmail(user.getEmail());
        if (users.isPresent()) {
            return null;
        }
        if (!userRepository.findByPanCard(user.getPanCard()).isEmpty()) {
            return null;
        }
        return userRepository.save(user);
    }

    // Get All Users
    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    // Get User by ID
    public UserDTO getUserById(Long id) {
        Optional<User> userOpt = userRepository.findById(id.intValue());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            UserDTO userDTO = new UserDTO();
            userDTO.setName(user.getName());
            userDTO.setEmail(user.getEmail());
            userDTO.setActive(user.isActive());
            userDTO.setBalance(user.getBalance());
            userDTO.setDateOfBirth(user.getDateOfBirth());
            userDTO.setPanCard(user.getPanCard());
            userDTO.setTransactionCount(user.getTransactionCount());
            return userDTO;
        }
        return null;
    }

    // Delete User
    public void deleteUser(Long id) {
        userRepository.deleteById(id.intValue());
    }

    // Login (simple check by email/password)
    public boolean authenticate(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        return userOpt.isPresent() && userOpt.get().getPassword().equals(password);
    }

    // Forgot Password: only works if email + dateOfBirth match
    public boolean forgotPassword(String email, LocalDate dob, String newPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            int code = sendMessage(user);
            if (code == -1) {
                return false;
            }
            return true;
        } else {
            return false;
        }
        // Optional<User> userOpt = userRepository.findByEmailAndDateOfBirth(email, dob);
        // if (userOpt.isPresent()) {
        //     User user = userOpt.get();
        //     user.setPassword(newPassword);
        //     userRepository.save(user);
        //     return true;
        // }
        // return false;
    }

    // Change Password: must match old password
    public boolean changePassword(String email, String oldPassword, String newPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(oldPassword)) {
                user.setPassword(newPassword);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }
}
