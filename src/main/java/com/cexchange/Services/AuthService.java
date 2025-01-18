package com.cexchange.Services;

import com.cexchange.Config.Jwt.JwtProvider;
import com.cexchange.Domain.Entities.User;
import com.cexchange.Dtos.AuthResponse;
import com.cexchange.Dtos.LoginDto;
import com.cexchange.Dtos.UserDto;
import com.cexchange.Dtos.UserResponse;
import com.cexchange.Exceptions.UserAlreadyExistsException;
import com.cexchange.Mapper.UserMapper;
import com.cexchange.Repository.IUserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService{
    private final PasswordEncoder _passwordEncoder;
    private final IUserRepository _userRepository;
    private final AuthenticationManager _authManager;
    private final IOTPService _otpService;
    private final IEmailService _emailService;
    private String token;
    @Override
    public UserResponse CreateUser(UserDto userDto) {
        User isEmailExist = _userRepository.findByEmail(userDto.getEmail());
        if (isEmailExist != null)
            throw new UserAlreadyExistsException("An account already exist with this email: " + userDto.getEmail());
        userDto.setPassword(_passwordEncoder.encode(userDto.getPassword()));
        User user = UserMapper.mapToUser(userDto);
        if (user == null)
            throw new RuntimeException("cannot create user at the moment");

        _userRepository.save(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword()
        );

        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = JwtProvider.generateToken(auth);

        UserResponse response = UserResponse.builder()
                .id(user.getId())
                .AuthResponse(AuthResponse.builder()
                        .jwt(jwt)
                        .status(true)
                        .message("register successfully")
                        .build())
                .build();

        return response;
    }

    @Override
    public AuthResponse LoginUser(LoginDto request) throws MessagingException {
        User isExist = _userRepository.findByEmail(request.getEmail());
        if (isExist == null)
            throw new UsernameNotFoundException("Account not found, bad credentials.");

        Authentication auth = _authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String verificationOtp = _otpService.generateOTP(request.getEmail());

//        boolean isEmailSent = _emailService.SendEmail(request.getEmail(), verificationOtp);



        String jwt = JwtProvider.generateToken(auth);
        token = jwt;

        SecurityContextHolder.getContext().setAuthentication(auth);

        AuthResponse response = AuthResponse.builder()
                        .message("Verification OTP has been sent to your email. " + verificationOtp)
                .build();
        return response;
    }

    @Override
    public AuthResponse VerifySingin(String otp, String email) {
        AuthResponse response;
        if (_otpService.validateOTP(email, otp)){
            response  = AuthResponse.builder()
                    .message("Two factor authentication verified")
                    .status(true)
                    .isTwoFactorAuthEnabled(true)
                    .jwt(token)
                    .build();
            return response;
        }
        token = null;
        response = AuthResponse.builder()
                .message("Invalid OTP, Verification failed")
                .build();
        return response;
    }
}
