package com.example.Repository;

import com.example.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByVerificationToken(String token);

    Optional<User> findByEmail(String email);

    boolean existsByFullName(String fullName);


    @Query("SELECT u FROM User u WHERE u.isEmailVerified = 0 AND u.createdAt < :cutoffTime")
    List<User> findUnverifiedUsers(@Param("cutoffTime") LocalDateTime cutoffTime);
}






