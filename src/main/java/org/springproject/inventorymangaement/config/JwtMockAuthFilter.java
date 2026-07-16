package org.springproject.inventorymangaement.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtMockAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer mock-jwt-token-for-")) {
            String token = authHeader.substring(26); // length of "Bearer mock-jwt-token-for-"
            String[] parts = token.split("-");
            if (parts.length > 0) {
                String role = parts[0]; // e.g. "Admin", "Manager", "Floor_Staff"

                // Convert to uppercase Spring authority format, e.g., ROLE_ADMIN or ROLE_FLOOR_STAFF
                String springRole = "ROLE_" + role.toUpperCase();

                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(springRole);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        role, null, Collections.singletonList(authority));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
