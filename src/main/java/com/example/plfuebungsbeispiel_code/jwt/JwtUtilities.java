package com.example.plfuebungsbeispiel_code.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


// Diese Klasse war gegeben und wurde nicht verändert
@Component
public class JwtUtilities
{
  // private static final String SECRET = "638CBE3A90E0303BF3808F40F95A7F02A24B4B5D029C954CF553F79E9EF1DC0384BE681C249F1223F6B55AA21DC070914834CA22C8DD98E14A872CA010091ACC";

  @Value("${token.signing.secret}")
  private String SECRET;

  private static final long VALIDITY = TimeUnit.MINUTES.toMillis(30);

  public String generateToken(String username)
  {
    Map<String, String> claims = new HashMap<>();
    claims.put("iss", "https://www.htl-kaindorf.ac.at");
    return Jwts.builder()
            .claims(claims)
            .subject(username)
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(Instant.now().plusMillis(VALIDITY)))
            .signWith(generateKey(), SignatureAlgorithm.HS256)
            // .signWith(generateKey()) // HS512
            .compact();
  }

  private SecretKey generateKey()
  {
    byte[] decodedKey = Base64.getDecoder().decode(SECRET);
    return Keys.hmacShaKeyFor(decodedKey);
  }

  public String extractUsername(String jwt)
  {
    Claims claims = getClaims(jwt);
    return claims.getSubject();
  }

  private Claims getClaims(String jwt)
  {
    return Jwts.parser()
            .verifyWith(generateKey())
            .build()
            .parseSignedClaims(jwt)
            .getPayload();
  }

  public boolean isTokenValid(String jwt)
  {
    Claims claims = getClaims(jwt);
    return claims.getExpiration().after(Date.from(Instant.now()));
  }

}
