package osama_mh.ecommerce.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "userName is required")
    @Size(min = 3, max = 25, message = "Invalid userName, must be between 3 and 20 characters long")
    private String userName;

    @NotBlank(message = "phoneNumber is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phoneNumber")
    private String phoneNumber;


    @Email(message = "Invalid email")
    @NotBlank(message = "email is required")
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 8, message = "Invalid password, must be at least 8 characters long")
    private String password;
}