package com.example.thanhhoa.utils;

import com.example.thanhhoa.configs.UserDetailsImpl;
import com.example.thanhhoa.dtos.UserModels.AuthorizeModel;
import com.example.thanhhoa.entities.tblAccount;
import com.example.thanhhoa.repositories.UserRepository;
import com.example.thanhhoa.services.role.RoleService;
import com.example.thanhhoa.services.user.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {
    /**
     * Đoạn JWT_SECRET này là bí mật, chỉ có phía server biết
     */
    private final String JWT_SECRET = "CapstoneThanhHoa23";
    /**
     * Thời gian có hiệu lực của chuỗi jwt
     */
    private final long JWT_EXPIRATION = 10800000L;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRepository userRepository;

//    public String generateTokenEmail(String email) {
//        Date now = new Date();
//        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
//        tblAccount user = userRepository.findByEmailAndStatus(email, Status.ACTIVE);
//        return Jwts.builder()
//                .setSubject(user.getUsername())
//                .setIssuedAt(now)
//                .setExpiration(expiryDate)
//                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
//                .claim("role", user.getRole().getRoleName())
//                .claim("id", user.getId())
//                .claim("username", user.getUsername())
//                .claim("email", user.getEmail())
//                .claim("phone", user.getPhone())
//                .claim("avatar", user.getAvatar())
//                .compact();
//    }

    public String generateToken(Authentication authentication) throws Exception {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        AuthorizeModel user = userService.getById(userPrincipal.getUserID());

        String avatarLink = "Không có avatar";

        if (user.getAvatar() != null)
            avatarLink = user.getAvatar();

        // Tạo chuỗi json web token từ id của user.
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .claim("id", user.getUserID())
                .claim("username", user.getUsername())
                .claim("fullName", user.getFullName())
                .claim("role", user.getRoleID())
                .claim("roleName", user.getRoleName())
                .claim("email", user.getEmail())
                .claim("phone", user.getPhone())
                .claim("avatarLink", avatarLink)
                .compact();
    }

    public String generateTokenForEmailOrPhone(tblAccount account) throws Exception {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

        AuthorizeModel user = userService.getById(account.getId());

        String avatarLink = "Không có avatar";

        if (user.getAvatar() != null)
            avatarLink = user.getAvatar();

        // Tạo chuỗi json web token từ id của user.
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .claim("id", user.getUserID())
                .claim("username", user.getUsername())
                .claim("fullName", user.getFullName())
                .claim("role", user.getRoleID())
                .claim("roleName", user.getRoleName())
                .claim("email", user.getEmail())
                .claim("phone", user.getPhone())
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

    public String getRoleNameFromJWT(HttpServletRequest request) {
        String authTokenHeader = request.getHeader("Authorization");
        if (authTokenHeader == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String token = authTokenHeader.substring(7);
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();

        return claims.get("roleName", String.class);
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
