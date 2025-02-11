package com.example.scheduler;

import com.example.Model.User;
import com.example.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class UserCleanupScheduler {

    private final UserRepository userRepository;

    @Autowired
    public UserCleanupScheduler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")  // Runs every day at midnight
    public void cleanUpUnverifiedUsers() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusDays(7);  // 7 days grace period
        List<User> unverifiedUsers = userRepository.findUnverifiedUsers(cutoffTime);  // Custom query
        for (User user : unverifiedUsers) {
            userRepository.delete(user);  // Or mark as inactive
        }
    }
}
