package com.example.Model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String hashedPassword;

    @Column
    private String verificationToken;
    @Column
    private String collegeName;

    @Column(nullable=false)
    private LocalDateTime createdAt;

    @Column(nullable=false)
    private Integer isEmailVerified;

    @Column(nullable = true)
    private String lastVerificationEmailSent;

    @ManyToMany
    @JoinTable(
            name = "user_followers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private Set<User> followers;  // Users who follow this user

    @ManyToMany(mappedBy = "followers")
    private Set<User> following;  // Users this user is following

    public User(String email, String hashedPassword, String fullName,String collegeName) {
        this.email=email;
        this.hashedPassword=hashedPassword;
        this.fullName=fullName;
        this.collegeName=collegeName;
    }
}


