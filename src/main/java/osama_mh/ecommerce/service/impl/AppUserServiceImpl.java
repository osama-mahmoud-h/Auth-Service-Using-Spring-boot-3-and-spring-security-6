package osama_mh.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import osama_mh.ecommerce.entity.AppUser;
import osama_mh.ecommerce.repository.AppUserRepository;
import osama_mh.ecommerce.service.AppUserService;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository userRepository;
        @Override
        public UserDetailsService userDetailsService() {
            return new UserDetailsService() {
                @Override
                public AppUser loadUserByUsername(String phoneNumber)  {
                    return userRepository.findByPhoneNumber(phoneNumber)
                            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                }
            };
        }

}
