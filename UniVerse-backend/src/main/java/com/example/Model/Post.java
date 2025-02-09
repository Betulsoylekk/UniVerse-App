package com.example.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String topic;

    @Column(nullable = false)
    private String content;  // Example content for the post



    @ManyToOne
    @JoinColumn(name = "user_profile_id")  // Foreign key to UserProfile
    private UserProfile userProfile;  // Reference to the UserProfile

    public Post(String topic, String content) {
        this.topic = topic;
        this.content = content;
    }
}

