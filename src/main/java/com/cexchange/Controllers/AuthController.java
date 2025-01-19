package com.cexchange.Controllers;

import com.cexchange.Dtos.AuthResponse;
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
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDto request) {
        try {
            AuthResponse response = _authService.LoginUser(request);
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        } catch (UsernameNotFoundException ex) {
            log.error("Account not found, bad credentials: {}", ex.getMessage(), ex);
            return new ResponseEntity<>(AuthResponse.builder()
                    .message(ex.getMessage())
                    .build(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
            return new ResponseEntity<>(AuthResponse.builder()
                    .message(ex.getMessage())
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/signin/verify/{otp}/{email}")
    public ResponseEntity<AuthResponse> verifySignin(
            @PathVariable String otp,
            @PathVariable String email){
        try{
            AuthResponse response = _authService.VerifySingin(otp, email);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (UsernameNotFoundException u){
            return new ResponseEntity<>(AuthResponse.builder()
                    .message(u.getMessage())
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (Exception ex){
            return new ResponseEntity<>(AuthResponse.builder()
                    .message(ex.getMessage())
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
