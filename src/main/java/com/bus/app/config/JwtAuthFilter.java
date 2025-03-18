package com.bus.app.config;

import com.bus.app.util.Constants;
import com.bus.app.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    public void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer")) {
            final String token = authHeader.substring(7);
            try {
                final Claims claims = this.jwtUtil.validateToken(token);
                final String userId = claims.get("userId", String.class);
                final String role = claims.get("role", String.class);
                if (userId != null && role != null) {
                    final List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
                    final UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userId, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (JwtException e) {
                throw new ServletException(Constants.INVALID_TOKEN);
            } catch (Exception e) {
                throw new ServletException(Constants.AUTHENTICATE_ERROR);
            }
        }
        filterChain.doFilter(request, response);
    }
}
