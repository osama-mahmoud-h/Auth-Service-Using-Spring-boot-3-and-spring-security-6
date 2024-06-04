package osama_mh.ecommerce.mapper;


import org.springframework.security.core.userdetails.UserDetails;
import osama_mh.ecommerce.dto.response.UserResponseDto;
import osama_mh.ecommerce.entity.AppUser;

public class UserMapper {

    public static UserResponseDto mapCurrentAuthedUserToUserResponseDto(AppUser appUser) {
        return UserResponseDto.builder()
                .id(appUser.getId())
                .userName(appUser.getUsername())
                .email(appUser.getEmail())
                .phoneNumber(appUser.getPhoneNumber())
                .role(appUser.getRole())
                .build();
    }
}
