package com.divum.hiring_platform.util;

import com.divum.hiring_platform.entity.EmployeeSession;
import com.divum.hiring_platform.entity.UserSession;
import com.divum.hiring_platform.repository.service.EmployeeSessionRepositoryService;
import com.divum.hiring_platform.repository.service.UserSessionRepositoryService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserSessionRepositoryService userSessionRepositoryService;
    private final EmployeeSessionRepositoryService employeeSessionRepositoryService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String jwt = bearerToken.substring(7);
            if (jwt.isEmpty()) {
                log.error("Empty JWT found in Authorization header");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token is empty");
                return;
            }

            try {
                String jit = jwtUtil.extractUsername(jwt);
                Object role = jwtUtil.extractRoleFromToken(jwt);
                if (jit != null && role != null) {
                    role = role.toString().replace("[", "").replace("]", "");
                    if (role.equals("user")) {
                        handleUserAuth(jit, jwt, role);
                    } else if (role.equals("employee") || role.equals("admin")) {
                        handleEmployeeAuth(jit, jwt, role);
                    }
                }
            } catch (Exception e) {
                log.error("Error processing JWT token: {}", e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }


    private void handleEmployeeAuth(String jit, String jwt, Object role) {
        EmployeeSession session = employeeSessionRepositoryService.findByUniqueId(jit);
        if (session == null) {
            return;
        }
        Authentication authentication = getAuthentication(jit, jwt, role);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void handleUserAuth(String jit, String jwt, Object role) {
        UserSession session = userSessionRepositoryService.findByUniqueId(jit);
        if (session == null) {
            return;
        }
        Authentication authentication = getAuthentication(jit, jwt, role);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Authentication getAuthentication(String jit, String jwt, Object role) {
        Set<GrantedAuthority> authoritySet = new HashSet<>();
        authoritySet.add(new SimpleGrantedAuthority(role.toString()));
        return !jwtUtil.isTokenExpired(jwt) ? new SimpleUserAuthentication(true, jit, jwt, authoritySet) : null;
    }
}
