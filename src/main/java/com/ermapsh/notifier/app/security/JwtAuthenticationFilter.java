
package com.ermapsh.notifier.app.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserContext userContext;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        log.info("Incoming request: {}", request.getRequestURI());

        final String authorizationHeader =
                request.getHeader("Authorization");

        // No access token provided
        if (authorizationHeader == null ||
                !authorizationHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = authorizationHeader
                .substring("Bearer ".length())
                .trim();

        // Empty token
        if (jwtToken.isBlank()) {
            sendUnauthorized(response, "Access token is required");
            return;
        }

        try {

            Claims claims = jwtUtil.verifyAccessToken(jwtToken);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                String userId = jwtUtil.extractUserId(claims);

                var auth = new UsernamePasswordAuthenticationToken(
                        claims.getSubject(),
                        null,
                        List.of()
                );

                SecurityContextHolder.getContext().setAuthentication(auth);

                userContext.setUserId(UUID.fromString(userId));
            }

            filterChain.doFilter(request, response);

        } catch (JwtException | IllegalArgumentException exception) {

            log.warn(
                    "Invalid access token for request {}: {}",
                    request.getRequestURI(),
                    exception.getClass().getSimpleName()
            );

            SecurityContextHolder.clearContext();

            sendUnauthorized(response, "Invalid or expired access token");
        }
    }

    private void sendUnauthorized(
            HttpServletResponse response,
            String message
    ) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(
                "{\"message\":\"" + message + "\"}"
        );
    }
}
