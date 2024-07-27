package org.cihan.elibrarian.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationFilterJwt extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Her istekte bu filtre cagirilir jwt kontrolü yapılır.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        if (isAuthApiRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authTokenHeader = request.getHeader("Authorization");
        if (authTokenHeader == null || !authTokenHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = extractJwtToken(authTokenHeader);
        String userName = jwtService.extractUsername(jwtToken);

        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = loadUserDetailsByUsername(userName);
            if (isJwtTokenValid(jwtToken, userDetails)) {
                authenticateUser(userDetails, request);
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isAuthApiRequest(HttpServletRequest request) {
        return request.getServletPath().contains("/api/v1/auth");
    }

    private String extractJwtToken(String authTokenHeader) {
        return authTokenHeader.substring(7);
    }

    private UserDetails loadUserDetailsByUsername(String userName) {
        return this.userDetailsService.loadUserByUsername(userName);
    }

    private boolean isJwtTokenValid(String jwtToken, UserDetails userDetails) {
        return jwtService.isTokenValid(jwtToken, userDetails);
    }

    private void authenticateUser(UserDetails userDetails, ServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
