package com.example.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;
    private String bio;

    @OneToOne
    @JoinColumn(name = "user_id")  // Foreign key to the User entity
    private User user;

    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL)
    private List<Post> posts;  // List of posts related to this user profile

    public UserProfile(String userName, String bio, User user) {
        this.userName = userName;
        this.bio = bio;
        this.user = user;
    }
}

