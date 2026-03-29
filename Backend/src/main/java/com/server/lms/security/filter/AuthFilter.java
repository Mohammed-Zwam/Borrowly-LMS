package com.server.lms.security.filter;

import com.server.lms.security.utils.JwtUtils;
import com.server.lms.security.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;
    private static final Set<String> PUBLIC_PATHS = Set.of("/api/auth");

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String jwtTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        String path = request.getRequestURI();

       if (PUBLIC_PATHS.stream().noneMatch(path::startsWith) && jwtTokenHeader != null && jwtTokenHeader.startsWith("Bearer ")) {
            String jwtToken = jwtTokenHeader.substring("Bearer ".length());

            if (jwtUtils.isTokenSignatureCorrect(jwtToken)) {

                String userEmail = jwtUtils.extractUserEmail(jwtToken);

                if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                    if (jwtUtils.isTokenValid(jwtToken, userDetails)) {

                        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

                        Authentication auth = new UsernamePasswordAuthenticationToken(
                                userDetails, null, authorities
                        );

                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }

}
