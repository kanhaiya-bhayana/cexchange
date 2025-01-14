package com.cexchange.Controllers;

import com.cexchange.Dtos.UserDto;
import com.cexchange.Dtos.UserResponse;
import com.cexchange.Exceptions.UserAlreadyExistsException;
import com.cexchange.Services.IUserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AuthController {
    private final IUserService userService;
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserDto request) {
        try {
            UUID response = userService.CreateUser(request);
            return new ResponseEntity<>(new UserResponse(response), HttpStatus.CREATED);
        } catch (UserAlreadyExistsException ex) {
            log.error("Error while creating user: {}", ex.getMessage(), ex);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<?> getTest(){
        return new ResponseEntity<>("Hello", HttpStatus.ACCEPTED);
    }
}
