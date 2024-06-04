package osama_mh.ecommerce.service.impl;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import osama_mh.ecommerce.dto.request.OtpVerificationRequestDto;
import osama_mh.ecommerce.dto.request.ResetPasswordRequestDto;
import osama_mh.ecommerce.dto.request.SignUpRequest;
import osama_mh.ecommerce.dto.request.SigninRequest;
import osama_mh.ecommerce.dto.response.JwtAuthenticationResponse;
import osama_mh.ecommerce.entity.AppUser;
import osama_mh.ecommerce.entity.PasswordResetToken;
import osama_mh.ecommerce.enums.UserRole;
import osama_mh.ecommerce.exception.CustomRuntimeException;
import osama_mh.ecommerce.repository.AppUserRepository;
import osama_mh.ecommerce.repository.PasswordResetTokenRepository;
import osama_mh.ecommerce.security.jwt.JwtService;
import osama_mh.ecommerce.service.AuthenticationService;
import osama_mh.ecommerce.utils.mailSender.EmailSender;
import osama_mh.ecommerce.utils.sms.SmsUtils;

import java.time.Instant;
import java.util.Optional;

import static org.hibernate.internal.util.ExceptionHelper.getRootCause;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailSender mailSender;
    private final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        try {
            AppUser user = constructNewAppUser(request);
            userRepository.save(user);
            String jwt = jwtService.generateToken(user);
            return JwtAuthenticationResponse.builder().token(jwt).build();
        } catch (DataIntegrityViolationException e) {
            logger.error("phoneNumber/email already exists", e);
            String rootMsg = getRootCause(e).getMessage();
            if (rootMsg != null && rootMsg.contains("email")) {
                throw new CustomRuntimeException("email already exists", HttpStatus.CONFLICT);
            }
            throw new CustomRuntimeException("phoneNumber already exists", HttpStatus.CONFLICT);
        }
    }

    @Override
    public JwtAuthenticationResponse signin(SigninRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getPhoneNumber(), request.getPassword()));
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid phoneNumber or password. ");
        }

        AppUser user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                    .orElseThrow(() -> new BadCredentialsException("Invalid phoneNumber or password."));

        String jwt =  jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public boolean processForgotPassword(String email) {
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        PasswordResetToken passwordResetToken = saveOrUpdateNewResetPasswordToken(user);
        sendMail(passwordResetToken,user);
        return false;
    }

    @Override
    public boolean verifyOtp(OtpVerificationRequestDto request) {
        PasswordResetToken token = passwordResetTokenRepository.findByToken(request.getOtp()).orElseThrow(
                () -> new CustomRuntimeException("Invalid or expired OTP.", HttpStatus.NOT_FOUND));
        if(token==null || !token.getUser().getEmail().equals(request.getEmail())){
            throw new CustomRuntimeException("Invalid or expired OTP.", HttpStatus.NOT_FOUND);
        }
        passwordResetTokenRepository.setIsVerified(token.getId());
        return true;
    }

    @Override
    public boolean resetPassword(ResetPasswordRequestDto requestDto){
        if(requestDto.passwordMismatch()){
            throw new CustomRuntimeException("Passwords do not match", HttpStatus.BAD_REQUEST);
        }
        PasswordResetToken token = passwordResetTokenRepository.findByUserEmail(requestDto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if(token.getExpiration().isBefore(Instant.now())){
            throw new CustomRuntimeException("OTP expired", HttpStatus.UNAUTHORIZED);
        }
        if(!token.getIsVerified()){
            throw new CustomRuntimeException("OTP not verified", HttpStatus.UNAUTHORIZED);
        }

        AppUser user = token.getUser();
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        userRepository.save(user);
        return true;
    }

    @Override
    @Transactional
    public Boolean deleteAccount(AppUser user){
       try{
           userRepository.deleteUserById(user.getId());
           return true;
       }catch (DataIntegrityViolationException e){
           throw new CustomRuntimeException("Error occurred while deleting account, "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
         }
    }

    private AppUser constructNewAppUser(SignUpRequest request){
       return AppUser.builder()
                .username(request.getUserName())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(UserRole.CUSTOMER)
                .build();
    }

    private PasswordResetToken saveOrUpdateNewResetPasswordToken(AppUser user) {
        PasswordResetToken passwordResetToken = constructNewRestPassword(user);
        Optional<PasswordResetToken> alreadyExistingToken = passwordResetTokenRepository.existsByUserId(user.getId());
        if(alreadyExistingToken.isPresent()){
            passwordResetTokenRepository.updateToken(passwordResetToken.getToken(),alreadyExistingToken.get().getId(), passwordResetToken.getExpiration());
        }else{
            passwordResetTokenRepository.save(passwordResetToken);
        }
        return passwordResetToken;
    }

    private PasswordResetToken constructNewRestPassword(AppUser user) {
        String otp = SmsUtils.generateOtp();
        return PasswordResetToken.builder()
                .user(user)
                .token(otp)
                .expiration(Instant.now().plusSeconds(60*5))
                .build();
    }
    private void sendMail(PasswordResetToken passwordResetToken, AppUser user) {
        String message = "Use this OTP to reset password: " + passwordResetToken.getToken();
        try{
        mailSender.sendEmail(user.getEmail(), "Reset Password", message);
        }catch (Exception e){
            System.out.println("Error occurred while sending email"+e.getMessage());
            throw new CustomRuntimeException("Error occurred while sending email, "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
       // smsSender.sendSms(passwordResetToken.getUser().getPhoneNumber(), message);
    }
}