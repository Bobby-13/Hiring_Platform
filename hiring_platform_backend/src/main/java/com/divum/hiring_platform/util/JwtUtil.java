package com.divum.hiring_platform.util;

import com.divum.hiring_platform.dto.EmployeeResponseDTO;
import com.divum.hiring_platform.entity.EmployeeAvailability;
import com.divum.hiring_platform.util.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.employee-availability.expiration-time}")
    private Long employeeAvailabilityTokenExpiryTime;

    @Value("${jwt.password-reset}")
    private Long passwordResetTokenExpiryTime;

    public String createEmployeeAvailabilityToken(EmployeeAvailability employeeAvailability, String decision) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("employee_id", employeeAvailability.getEmployeeAndContest().getEmployee().getEmployeeId());
        claims.put("contest_id", employeeAvailability.getEmployeeAndContest().getContest().getContestId());
        claims.put("decision", decision);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject("Divum")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + employeeAvailabilityTokenExpiryTime))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public EmployeeResponseDTO extractEmployeeResponse(String token) {
        Claims claims = extractAllClaims(token);
        String contestId = (String) claims.get("contest_id");
        Number employeeId = (Number) claims.get("employee_id");
        String decision = (String) claims.get("decision");
        return EmployeeResponseDTO.builder()
                .employeeId(employeeId.longValue())
                .contestId(contestId)
                .decision(decision)
                .build();
    }


    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateForgotPasswordToken(Long employeeId) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("employeeId", employeeId);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject("Divum")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + passwordResetTokenExpiryTime))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

    }
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    public Integer extractEmployeeId(String token) {
        Claims claims = extractAllClaims(token);
        return (Integer) claims.get("employeeId");
    }

    public String generateToken(String uniqeId, LocalDateTime endTime, Role role){

        Map<String, Object> claims = new HashMap<>();

        claims.put("role", role.toString().toLowerCase());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(uniqeId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }

    public String generateTokenForLogin(UserDetails userDetails, String uniqueId) {
        HashMap<String, Object> claims = new HashMap<>();

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        List<String> authoritiesString = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        claims.put("role", authoritiesString);
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(uniqueId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 24 * 60 * 60))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Object extractRoleFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(getSignInKey())
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role");
    }
}
