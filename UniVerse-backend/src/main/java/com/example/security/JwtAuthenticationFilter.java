package com.example.security;


import com.example.error.ErrorResponse;
import com.example.error.enums.ErrorType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // Skip JWT filter for /auth/** paths
        if (request.getServletPath().startsWith("/auth/")) {
            filterChain.doFilter(request, response); // Let the request pass through (no JWT authentication)
            return; // Skip JWT authentication
        }


        String token = extractToken(request);
        if (token != null) {
            try {
                jwtUtil.validateToken(token);


                String username = jwtUtil.extractUsername(token);

                CustomUserDetails userDetails =
                        (CustomUserDetails) userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities() // Includes "VERIFIED" if applicable
                        );
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (ExpiredJwtException e) {
                sendErrorResponse(response, ErrorType.TOKEN_EXPIRED);
            } catch (JwtException | IllegalArgumentException e) {
                sendErrorResponse(response, ErrorType.INVALID_TOKEN);
            }
        }
        filterChain.doFilter(request, response);
    }





    private ErrorResponse sendErrorResponse(HttpServletResponse response, ErrorType errorType) throws IOException {
        response.setStatus(errorType.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .type(errorType)
                .build();

        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
        return errorResponse;

    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return (header != null && header.startsWith("Bearer ")) ? header.substring(7) : null;
    }
}


