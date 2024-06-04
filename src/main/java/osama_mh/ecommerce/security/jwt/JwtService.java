package osama_mh.ecommerce.security.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import osama_mh.ecommerce.entity.AppUser;

public interface JwtService {
    String extractUserName(String token);
    String extractUserPhoneNumber(String token);

    String generateToken(AppUser appUser);

    boolean isTokenValid(String token, AppUser appUser);
}
