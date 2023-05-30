package com.example.thanhhoa.utils;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

@Component
@Slf4j
public class JwtUtil {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    /**
     * Đoạn JWT_SECRET này là bí mật, chỉ có phía server biết
     */
    private final String JWT_SECRET = "provip123";
    /**
     * Thời gian có hiệu lực của chuỗi jwt
     */
    private final long JWT_EXPIRATION = 10800000L;

    public String generateTokenEmail(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
        User user = userRepository.findUserByEmail(email);
        UserProfileModel userProfile = userProfileService.findByUser(user);
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .claim("role", user.getRole().getName())
                .claim("id", user.getId())
                .claim("avatar", userProfile.getAvatar())
                .claim("username", user.getUsername())
                .claim("email", user.getEmail())
                .claim("phone", user.getPhone())
                .compact();
    }

    public String generateTokenV2(Authentication authentication) throws Exception {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        UserReadDTO userDTO = userService.getDTOById(userPrincipal.getUserID());

        String avatarLink = "Không có avatar";

        if (userDTO.getFile() != null)
            avatarLink = userDTO.getFile().getFileLink();

        // Tạo chuỗi json web token từ id của user.
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .claim("id", userDTO.getUserId())
                .claim("username", userDTO.getUsername())
                .claim("fullName", userDTO.getFullName())
                .claim("role", userPrincipal.getAuthorities())
                .claim("email", userDTO.getEmail())
                .claim("phone", userDTO.getPhone())
                .claim("avatarLink", avatarLink)
                .compact();
    }

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();

        return claims.get("id", Long.class);
    }

    public String getUserNameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();

        return claims.get("username", String.class);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

    public String getAndValidateJwt(String bearerToken) throws Exception {
        String error = "";
        String jwt = "";

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            jwt = bearerToken.substring(7);

            try {
                Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(jwt);
            } catch (MalformedJwtException ex) {
                error += "Invalid JWT token. ";
            } catch (ExpiredJwtException ex) {
                error += "Expired JWT token. ";
            } catch (UnsupportedJwtException ex) {
                error += "Unsupported JWT token. ";
            } catch (IllegalArgumentException ex) {
                error += "JWT claims string is empty. ";
            }
        } else {
            error += "JWT is empty. ";
        }

        if (!error.trim().isEmpty())
            throw new IllegalArgumentException(error);

        return jwt;
    }

}
