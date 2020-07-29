package com.myhome.security.jwt.impl;

import com.myhome.security.jwt.AppJwt;
import com.myhome.security.jwt.AppJwtEncoderDecoder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.ZoneId;
import java.util.Date;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("default")
public class SecretJwtEncoderDecoder implements AppJwtEncoderDecoder {

  @Override public AppJwt decode(String encodedJwt, String secret) {
    Claims claims = Jwts.parser()
        .setSigningKey(secret)
        .parseClaimsJws(encodedJwt)
        .getBody();
    String userId = claims.getSubject();
    Date expiration = claims.getExpiration();
    return AppJwt.builder()
        .userId(userId)
        .expiration(expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
        .build();
  }

  @Override public String encode(AppJwt jwt, String secret) {
    Date expiration = Date.from(jwt.getExpiration().atZone(ZoneId.systemDefault()).toInstant());

    return Jwts.builder()
        .setSubject(jwt.getUserId())
        .setExpiration(expiration)
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact();
  }
}
