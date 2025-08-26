package com.practice.keepgowing.generet;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
@Service
public class generat {

    private  final String secret="qwertyuiop";

  public   String accesstoken(String username){
    return     Jwts.builder()
                .setSubject(username)
                .signWith(SignatureAlgorithm.HS256,secret.getBytes())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+15 * 60 * 1000))
                .compact();

    }

    public   String refreshtoken(String username){
        return     Jwts.builder()
                .setSubject(username)
                .signWith(SignatureAlgorithm.HS256,secret.getBytes())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+7 * 24 * 60 * 60 * 1000))
                .compact();

    }





    public boolean verifytoken(String token){
        Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parseClaimsJws(token);


        return true;
    }


    public String extractusername(String token){
      Claims claims=Jwts.parser()
              .setSigningKey(secret.getBytes())
              .parseClaimsJws(token)
              .getBody();

      return claims.getSubject();


    }





}
