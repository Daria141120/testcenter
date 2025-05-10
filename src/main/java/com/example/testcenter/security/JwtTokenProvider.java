package com.example.testcenter.security;

import com.example.testcenter.exception.CommonBackendException;
import com.example.testcenter.model.db.entity.User;
import com.example.testcenter.model.dto.auth.JwtResponse;
import com.example.testcenter.model.enums.Role;
import com.example.testcenter.security.props.JwtProperties;
import com.example.testcenter.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private SecretKey key;

    @PostConstruct
    public void init(){
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }


    public String createAccessToken(String username, Set<Role> roles){
        Map<String, Object> claims = new HashMap<>();
        List<String> rolesStrings = roles.stream().map(Enum::name).collect(Collectors.toList());
        claims.put("roles", rolesStrings);

        Date issuedDate = new Date();
        Date expiredDate =  new Date(System.currentTimeMillis() + jwtProperties.getAccessTime());
        return Jwts.builder()
                    .subject(username)
                    .issuedAt(issuedDate)
                    .expiration(expiredDate)
                    .claims(claims)
                    .signWith(key)
                    .compact();
    }

    public String createRefreshToken(String username){
        Date issuedDate = new Date();
        Date expiredRefreshDate =  new Date(System.currentTimeMillis() + jwtProperties.getRefreshTime());
        return Jwts.builder()
                .subject(username)
                .issuedAt(issuedDate)
                .expiration(expiredRefreshDate)
                .signWith(key)
                .compact();
    }


    public JwtResponse refreshUserTokens(String refreshToken){
        JwtResponse jwtResponse = new JwtResponse();
        if(!isValidToken(refreshToken)){
            throw new CommonBackendException("refresh token expired", HttpStatus.FORBIDDEN);
        }
        String username = getUsername(refreshToken);
        User user = userService.getUserByUsername(username);
        jwtResponse.setUsername(username);
        jwtResponse.setAccessToken(
                createAccessToken(user.getUsername(), user.getRoles())
        );
        jwtResponse.setRefreshToken(
                createRefreshToken(user.getUsername())
        );
        return jwtResponse;
    }

    public boolean isValidToken(String token){
        return getAllClaimsFromToken(token)
                .getExpiration()
                .after(new Date());
    }

    public String getUsername(String token){
        return getAllClaimsFromToken(token)
                .getSubject();
    }


    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    public Authentication authentication(String token){
        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return  new UsernamePasswordAuthenticationToken(
                userDetails,
                "",
                userDetails.getAuthorities()
        );
    }

}
