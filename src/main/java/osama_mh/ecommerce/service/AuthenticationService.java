package osama_mh.ecommerce.service;


import osama_mh.ecommerce.dto.request.OtpVerificationRequestDto;
import osama_mh.ecommerce.dto.request.ResetPasswordRequestDto;
import osama_mh.ecommerce.dto.request.SignUpRequest;
import osama_mh.ecommerce.dto.request.SigninRequest;
import osama_mh.ecommerce.dto.response.JwtAuthenticationResponse;
import osama_mh.ecommerce.entity.AppUser;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);

    boolean processForgotPassword(String email);

    boolean verifyOtp(OtpVerificationRequestDto request);

    boolean resetPassword(ResetPasswordRequestDto requestDto);

    //delete account.
    Boolean deleteAccount(AppUser user);
}