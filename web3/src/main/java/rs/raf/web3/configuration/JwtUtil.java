package rs.raf.web3.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import rs.raf.web3.model.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "jwt big secret";

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token){
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public String generateToken(User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("email",user.getEmail());
        //claims.put("password",user.getPassword());
        claims.put("can_read",user.getPermission().getRead());
        claims.put("can_update",user.getPermission().getUpdate());
        claims.put("can_create",user.getPermission().getCreate());
        claims.put("can_delete",user.getPermission().getDelete());

        claims.put("can_search",user.getPermission().getSearch());
        claims.put("can_order",user.getPermission().getOrder());
        claims.put("can_cancel",user.getPermission().getCancel());
        claims.put("can_track",user.getPermission().getTrack());
        claims.put("can_schedule",user.getPermission().getSchedule());
        claims.put("isAdmin", user.getAdmin());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }
}
