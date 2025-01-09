package com.cexchange.Controllers;

import com.cexchange.Dtos.UserDto;
import com.cexchange.Exceptions.UserAlreadyExistsException;
import com.cexchange.Services.IUserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final IUserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UUID> createUser(@RequestBody UserDto request) {
        try {
            UUID response = userService.CreateUser(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (UserAlreadyExistsException ex) {  // Replace with actual exception
            // Log the error
//            log.error("Error while creating user: {}", ex.getMessage(), ex);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
//            log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
