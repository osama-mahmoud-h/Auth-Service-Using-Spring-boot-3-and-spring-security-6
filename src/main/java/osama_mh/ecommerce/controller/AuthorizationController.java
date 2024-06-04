package osama_mh.ecommerce.controller;


import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/roles")
public class AuthorizationController {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationController.class);

    @GetMapping("/isAdmin")
    //has role admin
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Check if the user is an admin")
    public String isAdmin(){
        return "You are an admin";
    }

    @GetMapping("/isCustomer")
    @Operation(summary = "Check if the user is a customer")
    @PreAuthorize("hasRole('CUSTOMER')")
    public String isCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Authentication: {}, Authorities: {}", authentication.getName(), authentication.getAuthorities());
        return "Access Granted for CUSTOMER";
    }

    @GetMapping("/isDelivery")
    @Operation(summary = "Check if the user is a delivery")
    @PreAuthorize("hasRole('DELIVERY')")
    public String isDelivery(){
        return "You are a delivery";
    }

}
