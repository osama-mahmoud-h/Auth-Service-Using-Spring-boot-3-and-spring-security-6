package osama_mh.ecommerce.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import osama_mh.ecommerce.entity.AppUser;

public interface AppUserService {
    UserDetailsService userDetailsService();

}
