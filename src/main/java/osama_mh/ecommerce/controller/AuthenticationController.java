package osama_mh.ecommerce.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import  io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import osama_mh.ecommerce.dto.request.*;
import osama_mh.ecommerce.dto.response.JwtAuthenticationResponse;
import osama_mh.ecommerce.dto.response.MyApiResponse;
import osama_mh.ecommerce.entity.AppUser;
import osama_mh.ecommerce.exception.ApiErrorResponse;
import osama_mh.ecommerce.service.AuthenticationService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {@ExampleObject(name = "invalid phoneNumber",value = "{\n" +
                            "  \"timestamp\": \"2021-08-08T14:54:00.000+00:00\",\n" +
                            "  \"message\": \"Invalid phoneNumber\",\n" +
                            "  \"statusCode\": 400\n" +
                            "}"),
                            @ExampleObject(name = "invalid userName",value = "{\n" +
                                    "  \"timestamp\": \"2021-08-08T14:54:00.000+00:00\",\n" +
                                    "  \"message\": \"Invalid userName, must be between 3 and 20 characters long\",\n" +
                                    "  \"statusCode\": 400\n" +
                                    "}"),
                            @ExampleObject(name = "invalid password",value = "{\n" +
                                    "  \"timestamp\": \"2021-08-08T14:54:00.000+00:00\",\n" +
                                    "  \"message\": \"Invalid password, must be at least 8 characters long\",\n" +
                                    "  \"statusCode\": 400\n" +
                                    "}"),
                            @ExampleObject(name = "invalid email",value = "{\n" +
                                    "  \"timestamp\": \"2021-08-08T14:54:00.000+00:00\",\n" +
                                    "  \"message\": \"Invalid email\",\n" +
                                    "  \"statusCode\": 400\n" +
                                    "}")
                    })
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict, phoneNumber already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {@ExampleObject(name = "phoneNumber already exists",value = "{\n" +
                                    "  \"timestamp\": \"2021-08-08T14:54:00.000+00:00\",\n" +
                                    "  \"message\": \"phoneNumber already exists\",\n" +
                                    "  \"statusCode\": 409\n" +
                                    "}")
                            })
                    ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict, email  already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {@ExampleObject(name = "email already exists",value = "{\n" +
                                    "  \"timestamp\": \"2021-08-08T14:54:00.000+00:00\",\n" +
                                    "  \"message\": \"email already exists\",\n" +
                                    "  \"statusCode\": 409\n" +
                                    "}")
                            })
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "An unexpected error occurred",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {@ExampleObject(name = "Internal Server Error",value = "{\n" +
                                    "  \"timestamp\": \"2021-08-08T14:54:00.000+00:00\",\n" +
                                    "  \"message\": \"Internal Server Error\",\n" +
                                    "  \"statusCode\": 500\n" +
                                    "}")
                            })
                    )
    })
    @Operation(summary = "Sign up")
    public ResponseEntity<MyApiResponse<JwtAuthenticationResponse>> signup(@Valid @RequestBody  SignUpRequest request) {
        return ResponseEntity.ok(MyApiResponse.success(authenticationService.signup(request),"User signed up successfully"));
    }

    @PostMapping("/signin")
    @Operation(summary = "Sign in")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description ="Invalid input, object invalid",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {@ExampleObject(name = "password is required",value = "{\n" +
                                    "  \"timestamp\": \"2021-08-08T14:54:00.000+00:00\",\n" +
                                    "  \"message\": \"password is required\",\n" +
                                    "  \"statusCode\": 400\n" +
                                    "}"),
                                    @ExampleObject(name = "phoneNumber is required",value = "{\n" +
                                            "  \"timestamp\": \"2021-08-08T14:54:00.000+00:00\",\n" +
                                            "  \"message\": \"phoneNumber is required\",\n" +
                                            "  \"statusCode\": 400\n" +
                                            "}")
                            }
                    )),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized, invalid credentials",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {@ExampleObject(name = "Invalid phoneNumber or password",value = "{\n" +
                                    "  \"timestamp\": \"2021-08-08T14:54:00.000+00:00\",\n" +
                                    "  \"message\": \"Invalid email or password\",\n" +
                                    "  \"statusCode\": 401\n" +
                                    "}")
                            })
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "An unexpected error occurred",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {@ExampleObject(name = "Internal Server Error",value = "{\n" +
                                    "  \"timestamp\": \"2021-08-08T14:54:00.000+00:00\",\n" +
                                    "  \"message\": \"Internal Server Error\",\n" +
                                    "  \"statusCode\": 500\n" +
                                    "}")
                            })
                    )
    })
    public ResponseEntity<MyApiResponse<JwtAuthenticationResponse>> signin(@Valid @RequestBody SigninRequest request) {
        return ResponseEntity.ok(MyApiResponse.success(authenticationService.signin(request),"User signed in successfully"));
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot password")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input, object invalid",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {@ExampleObject(name = "email is required",value = "{\n" +
                                    "  \"timestamp\": \"2021-08-08T14:54:00.000+00:00\",\n" +
                                    "  \"message\": \"email is required\",\n" +
                                    "  \"statusCode\": 400\n" +
                                    "}"),
                                    @ExampleObject(name = "Invalid email",value = "{\n" +
                                            "  \"timestamp\": \"2021-08-08T14:54:00.000+00:00\",\n" +
                                            "  \"message\": \"Invalid email\",\n" +
                                            "  \"statusCode\": 400\n" +
                                            "}")
                            }
                    )),
            @ApiResponse(
                    responseCode = "500",
                    description = "An unexpected error occurred",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {@ExampleObject(name = "Internal Server Error",value = "{\n" +
                                    "  \"timestamp\": \"2021-08-08T14:54:00.000+00:00\",\n" +
                                    "  \"message\": \"Internal Server Error\",\n" +
                                    "  \"statusCode\": 500\n" +
                                    "}")
                            })
            )
    })
    public ResponseEntity<MyApiResponse<Boolean>> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDto request) {
        authenticationService.processForgotPassword(request.getEmail());
        return ResponseEntity.ok(MyApiResponse.success(true,"Password reset code sent successfully"));
    }

    @PostMapping("/verify-otp")
    @Operation(summary = "Verify OTP")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Otp verified successfully"),
            @ApiResponse(
                    responseCode = "500",
                    description = "An unexpected error occurred",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {@ExampleObject(name = "Internal Server Error",value = "{\n" +
                                    "  \"timestamp\": \"2021-08-08T14:54:00.000+00:00\",\n" +
                                    "  \"message\": \"Internal Server Error\",\n" +
                                    "  \"statusCode\": 500\n" +
                                    "}")
                            })
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input, object invalid",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {@ExampleObject(name = "Otp is required",value = "{\n" +
                                    "  \"timestamp\": \"2021-08-08T14:54:00.000+00:00\",\n" +
                                    "  \"message\": \"Otp is required\",\n" +
                                    "  \"statusCode\": 400\n" +
                                    "}"),
                                    @ExampleObject(name = "Email is required",value = "{\n" +
                                            "  \"timestamp\": \"2021-08-08T14:54:00.000+00:00\",\n" +
                                            "  \"message\": \"Email is required\",\n" +
                                            "  \"statusCode\": 400\n" +
                                            "}")
                            }
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found, Invalid or expired OTP",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {@ExampleObject(name = "Invalid or expired OTP",value = "{\n" +
                                    "  \"timestamp\": \"2021-08-08T14:54:00.000+00:00\",\n" +
                                    "  \"message\": \"Invalid or expired OTP.\",\n" +
                                    "  \"statusCode\": 404\n" +
                                    "}")
                            })
            )

    })
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerificationRequestDto requestDto) {
        return ResponseEntity.ok(MyApiResponse.success(authenticationService.verifyOtp(requestDto),"Otp verified successfully"));
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password")
    @ApiResponses(value = {
                  //200
            @ApiResponse(responseCode = "200", description = "Password reset successfully"),
            @ApiResponse(
                    responseCode = "500",
                    description = "An unexpected error occurred",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {@ExampleObject(name = "Internal Server Error",value = "{\n" +
                                    "  \"timestamp\": \"2021-08-08T14:54:00.000+00:00\",\n" +
                                    "  \"message\": \"Internal Server Error\",\n" +
                                    "  \"statusCode\": 500\n" +
                                    "}")
                            })
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input, object invalid",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {@ExampleObject(name = "Password must be at least 6 characters",value = "{\n" +
                                    "  \"timestamp\": \"2021-08-08T14:54:00.000+00:00\",\n" +
                                    "  \"message\": \"Password must be at least 6 characters\",\n" +
                                    "  \"statusCode\": 400\n" +
                                    "}"),
                                    @ExampleObject(name = "Password do not match",value = "{\n" +
                                            "  \"timestamp\": \"2021-08-08T14:54:00.000+00:00\",\n" +
                                            "  \"message\": \"Passwords do not match\",\n" +
                                            "  \"statusCode\": 400\n" +
                                            "}"),
                                    @ExampleObject(name = "Confirm password is required",value = "{\n" +
                                            "  \"timestamp\": \"2021-08-08T14:54:00.000+00:00\",\n" +
                                            "  \"message\": \"Confirm password is required\",\n" +
                                            "  \"statusCode\": 400\n" +
                                            "}"),
                                    @ExampleObject(name = "Email is required",value = "{\n" +
                                            "  \"timestamp\": \"2021-08-08T14:54:00.000+00:00\",\n" +
                                            "  \"message\": \"Email is required\",\n" +
                                            "  \"statusCode\": 400\n" +
                                            "}")
                            }
                    )),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized, OTP not verified",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {@ExampleObject(name = "OTP not verified",value = "{\n" +
                                    "  \"timestamp\": \"2021-08-08T14:54:00.000+00:00\",\n" +
                                    "  \"message\": \"OTP not verified\",\n" +
                                    "  \"statusCode\": 401\n" +
                                    "}"),
                                    @ExampleObject(name = "OTP expired",value = "{\n" +
                                            "  \"timestamp\": \"2021-08-08T14:54:00.000+00:00\",\n" +
                                            "  \"message\": \"OTP expired\",\n" +
                                            "  \"statusCode\": 401\n" +
                                            "}")
                            })

            ),

    })
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequestDto request) {
        return ResponseEntity.ok(MyApiResponse.success(authenticationService.resetPassword(request),"Password reset successfully"));
    }

    //delete account
    @DeleteMapping("/delete-account")
    @Operation(summary = "Delete account")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('DELIVERY_AGENT') or hasRole('ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account deleted successfully"),
            @ApiResponse(
                    responseCode = "500",
                    description = "An unexpected error occurred",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {@ExampleObject(name = "Internal Server Error",value = "{\n" +
                                    "  \"timestamp\": \"2021-08-08T14:54:00.000+00:00\",\n" +
                                    "  \"message\": \"Internal Server Error\",\n" +
                                    "  \"statusCode\": 500\n" +
                                    "}")
                            })
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {@ExampleObject(name = "Forbidden",value = "{\n" +
                                    "  \"timestamp\": \"2021-08-08T14:54:00.000+00:00\",\n" +
                                    "  \"message\": \"Forbidden\",\n" +
                                    "  \"statusCode\": 403\n" +
                                    "}")
                            })
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found, User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {@ExampleObject(name = "User not found",value = "{\n" +
                                    "  \"timestamp\": \"2021-08-08T14:54:00.000+00:00\",\n" +
                                    "  \"message\": \"User not found\",\n" +
                                    "  \"statusCode\": 404\n" +
                                    "}")
                            })
            )
    })
    public ResponseEntity<?> deleteAccount(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(MyApiResponse.success(authenticationService.deleteAccount((AppUser)userDetails),"Account deleted successfully"));
    }

}
