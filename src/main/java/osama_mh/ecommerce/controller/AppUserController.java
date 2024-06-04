package osama_mh.ecommerce.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import osama_mh.ecommerce.dto.response.MyApiResponse;
import osama_mh.ecommerce.dto.response.UserResponseDto;
import osama_mh.ecommerce.entity.AppUser;
import osama_mh.ecommerce.exception.ApiErrorResponse;
import osama_mh.ecommerce.mapper.UserMapper;

@RestController
@RequestMapping("/api/v1/users")
public class AppUserController {

    @GetMapping("/current")
    @Operation(summary = "Get current authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
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
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {@ExampleObject(name = "Forbidden", value = "{\n" +
                                    "  \"timestamp\": \"2021-08-08T14:54:00.000+00:00\",\n" +
                                    "  \"message\": \"Forbidden\",\n" +
                                    "  \"statusCode\": 403\n" +
                                    "}")
                            }
                    )
            ),
    })
    public ResponseEntity<MyApiResponse<UserResponseDto>> getUsers(@AuthenticationPrincipal UserDetails currentUser) {
        AppUser appUser = (AppUser) currentUser;
        return ResponseEntity.ok().body(
                MyApiResponse
                        .success(UserMapper.mapCurrentAuthedUserToUserResponseDto(appUser), "User retrieved successfully")
        );
    }
    }