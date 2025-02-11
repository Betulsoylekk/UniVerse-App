package com.example.Service;

import com.example.Dto.request.CreateUserProfileRequest;
import com.example.Model.User;
import com.example.Model.UserProfile;
import com.example.Repository.UserProfileRepository;
import com.example.Repository.UserRepository;
import com.example.security.exceptions.UserNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    public UserProfileService(UserProfileRepository userProfileRepository, UserRepository userRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
    }
    @Transactional
    public void createOrUpdateUserProfile(CreateUserProfileRequest request) {
        // Validate user exists
        User user = validateUserExists(request.getUserId());

        // Find or create profile
        UserProfile profile = findOrCreateProfile(request.getUserId(), user);

        // Update fields
        updateProfileFields(profile, request);

        // Save changes
        userProfileRepository.save(profile);
    }

    private User validateUserExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    private UserProfile findOrCreateProfile(Long userId, User user) {
        return userProfileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    UserProfile profile = new UserProfile();
                    profile.setUser(user);
                    return profile;
                });
    }

    private void updateProfileFields(UserProfile profile, CreateUserProfileRequest request) {
        String userName = generateUniqueFullName(request.getUserName());
        profile.setUserName(userName);
        profile.setBio(request.getBio());
    }


    @Transactional
    public void handleFirstLogin(User user) {
        if (userProfileRepository.findByUserId(user.getId()).isEmpty()) {
            // Trigger async profile creation
            asyncProfileSetup(user);
        }
    }

    @Async
    public void asyncProfileSetup(User user) {
        UserProfile profile = new UserProfile();

        try {
            profile.setUser(user);
            profile.setBio("");
            profile.setUserName(generateUniqueFullName(user.getFullName()));
            userProfileRepository.save(profile);
        } catch (DataIntegrityViolationException e) {
            // Retry logic or fallback (e.g., append UUID)
            profile.setUserName(generateUniqueFullName(user.getFullName() + "-" + UUID.randomUUID()));
            userProfileRepository.save(profile);
        }
    }


    public String generateUniqueFullName(String fullName) {
        // 1. Validate and format
        String formattedName = formatName(fullName);

        // 2. Generate unique name with retry logic
        String uniqueName = formattedName;
        int attempt = 0;
        final int maxAttempts = 10;

        while (attempt < maxAttempts) {
            if (!userRepository.existsByFullName(uniqueName)) {
                return uniqueName;
            }
            uniqueName = formattedName + generateRandomSuffix();
            attempt++;
        }
        throw new IllegalStateException("Failed to generate a unique name after " + maxAttempts + " attempts");
    }

    private String formatName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty");
        }
        // Allow letters, accents, and spaces
        String cleaned = fullName.trim().replaceAll("[^\\p{L} ]", "");
        if (cleaned.isEmpty()) {
            throw new IllegalArgumentException("Invalid characters in name");
        }
        return cleaned.substring(0, 1).toUpperCase() + cleaned.substring(1).toLowerCase();
    }

    private String generateRandomSuffix() {
        return UUID.randomUUID().toString().substring(0, 8); // 8-character random suffix
    }

}
