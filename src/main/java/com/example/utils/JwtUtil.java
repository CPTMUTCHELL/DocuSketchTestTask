package com.example.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.dto.Token;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
@Component
@RequiredArgsConstructor
public class JwtUtil {
    @Value(("${jwt.secret}"))
    private  String secret;
    @Value(("${jwt.access.expire}"))
    private  String accessExpire;
    @Value(("${jwt.refresh.expire}"))
    private  String refreshExpire;
    private final UserService userService;
    public Token generateJwt(Authentication authResult){
        var username =  authResult.getName();
        var algorithm = Algorithm.HMAC256(secret.getBytes());
        var accessToken = JWT.create().withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + Long.parseLong(accessExpire)))
                .withClaim("roles", userService.loadUserByUsername(username).getAuthorities()
                        .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        var refreshToken = JWT.create().withSubject(username)
                .withClaim("roles",new ArrayList<>(List.of("ROLE_REFRESH")))
                .withExpiresAt(new Date(System.currentTimeMillis() + Long.parseLong(refreshExpire)))
                .sign(algorithm);


        return new Token(accessToken,refreshToken);
    }

}
