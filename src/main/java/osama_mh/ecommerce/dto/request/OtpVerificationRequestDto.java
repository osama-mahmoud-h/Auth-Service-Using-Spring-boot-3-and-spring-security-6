package osama_mh.ecommerce.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OtpVerificationRequestDto {
    @NotBlank(message = "Otp is required")
    private String otp;

    @NotBlank(message = "Email is required")
    private String email;
}
