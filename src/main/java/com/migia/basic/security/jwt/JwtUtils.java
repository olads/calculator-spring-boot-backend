package com.migia.basic.security.jwt;

import com.migia.basic.models.User;
import io.jsonwebtoken.*;
import org.hibernate.criterion.NullExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;


@Component
public class JwtUtils {

    private final static Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${calculator.app.jwtCookieName}")
    private String jwtSecret;

    @Value("${calculator.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${calculator.app.jwtCookieName}")
    private String name;

    @Value("Authorization")
    private String authenticationHeaderName;

    public String getJwtFromRequest(HttpServletRequest request) {
        try {
            String jwt = request.getHeader(authenticationHeaderName);
            jwt = jwt.replace(name,"");

            return jwt;
        }
        catch (NullPointerException e){
            return null;
        }


    }
    public String generateJwtCookie(User user) {
        String jwt = name + generateTokenFromUsername(user.getUsername());
       // ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).path("/app").maxAge(24 * 60 * 60).httpOnly(true).build();
        return jwt;
    }
    public String getCleanJwt() {
        String jwt = name+"";
        return jwt;
    }
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
         //   logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
          //  logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
         //   logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
          //  logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
           // logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
}
