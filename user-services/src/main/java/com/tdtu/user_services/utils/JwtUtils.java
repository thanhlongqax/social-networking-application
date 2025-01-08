package com.tdtu.user_services.utils;

import com.tdtu.user_services.dto.ResDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${security.secret}")
    private String jwtSecret;

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserIdFromJwtToken(String token) {
        return getTokenSubject(token);
    }

    public String getTokenSubject(String token){
        token = token.split(" ")[1];
        if (validateJwtToken(token)){
            try{
                return Jwts.parserBuilder().setSigningKey(key()).build()
                        .parseClaimsJws(token).getBody().getSubject();
            }catch (ExpiredJwtException e){
                return e.getClaims().getSubject();
            }
        }
        return null;
    }

    public boolean validateJwtToken(String authToken) {
        if(authToken != null){
            try {
                Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
                return true;
            } catch (MalformedJwtException e) {
                logger.error("Invalid JWT token: {}", e.getMessage());
            } catch (ExpiredJwtException e) {
                logger.error("JWT token is expired: {}", e.getMessage());
            } catch (UnsupportedJwtException e) {
                logger.error("JWT token is unsupported: {}", e.getMessage());
            } catch (IllegalArgumentException e) {
                logger.error("JWT claims string is empty: {}", e.getMessage());
            } catch (SignatureException e) {
                logger.error("Invalid JWT signature: {}", e.getMessage());
            }
        }
        return false;
    }

    public static ResDTO<Object> generateInvalidTokenResp(){
        ResDTO<Object> response = new ResDTO<>();
        response.setCode(HttpServletResponse.SC_UNAUTHORIZED);
        response.setData(null);
        response.setMessage("You are not authenticated");

        return response;
    }
}
