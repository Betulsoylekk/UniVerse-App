package com.example.security;


import com.example.Model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Grant authority only if the user is verified
        if (user.getIsEmailVerified()==1) {
            return Collections.singletonList(new SimpleGrantedAuthority("VERIFIED"));
        }
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return user.getHashedPassword(); // From your User entity
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // Or username, depending on your setup
    }

    // Other UserDetails methods (adjust based on your needs)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true; // Or tie this to a separate "active" flag if needed
    }

    // Optional: Add a method to access the full User entity
    public User getUser() {
        return user;
    }
}
