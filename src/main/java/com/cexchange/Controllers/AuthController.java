package com.cexchange.Controllers;

import com.cexchange.Dtos.LoginDto;
import com.cexchange.Dtos.UserDto;
import com.cexchange.Dtos.UserResponse;
import com.cexchange.Exceptions.UserAlreadyExistsException;
import com.cexchange.Services.IAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AuthController {
    private final IAuthService _authService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserDto request) {
            try {
            UserResponse response = _authService.CreateUser(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (UserAlreadyExistsException ex) {
            log.error("Error while creating user: {}", ex.getMessage(), ex);
            return new ResponseEntity<>(UserResponse.builder()
                    .error(true)
                    .errorMessage(ex.getMessage())
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
            return new ResponseEntity<>(UserResponse.builder()
                    .error(true)
                    .errorMessage(ex.getMessage())
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<UserResponse> login(@RequestBody LoginDto request) {
        try {
            UserResponse response = _authService.LoginUser(request);
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        } catch (UsernameNotFoundException ex) {
            log.error("Account not found, bad credentials: {}", ex.getMessage(), ex);
            return new ResponseEntity<>(UserResponse.builder()
                    .error(true)
                    .errorMessage(ex.getMessage())
                    .build(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
            return new ResponseEntity<>(UserResponse.builder()
                    .error(true)
                    .errorMessage(ex.getMessage())
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/signin/verify{opt}")
    public ResponseEntity<?> verifySignin(
            @PathVariable String otp,
            @RequestParam String email){

    }
}
