package osama_mh.ecommerce.security.jwt.impl;


import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import osama_mh.ecommerce.entity.AppUser;
import osama_mh.ecommerce.exception.CustomRuntimeException;
import osama_mh.ecommerce.security.jwt.JwtService;
import osama_mh.ecommerce.service.AppUserService;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final AppUserService userService;
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            if (jwt != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                validateAndAuthenticate(jwt, request);
            }
        } catch (Exception ex) {
            log.error("JWT Authentication failed: {}", ex.getMessage(), ex);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed: " + ex.getMessage());
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.isNotEmpty(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void validateAndAuthenticate(String jwt, HttpServletRequest request) {
        String userPhoneNumber = jwtService.extractUserName(jwt);
        if (StringUtils.isNotEmpty(userPhoneNumber)) {
            AppUser appUser = (AppUser) userService.userDetailsService().loadUserByUsername(userPhoneNumber);
            if (jwtService.isTokenValid(jwt, appUser)) {
                log.info("JWT token is valid for user: {}", userPhoneNumber);
                authenticateUser(appUser, request);
            } else {
                log.warn("Invalid JWT token for user: {}", userPhoneNumber);
                throw new SecurityException("Invalid JWT token.");
            }
        }
    }

    private void authenticateUser(AppUser appUser, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                appUser, null, appUser.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authenticationToken);
        SecurityContextHolder.setContext(context);
    }

}