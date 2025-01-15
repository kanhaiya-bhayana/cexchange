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
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService{
    private final PasswordEncoder _passwordEncoder;
    private final IUserRepository _userRepository;
    private final AuthenticationManager _authManager;
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
    public UserResponse LoginUser(LoginDto request) {
        User isExist = _userRepository.findByEmail(request.getEmail());
        if (isExist == null)
            throw new UsernameNotFoundException("Account not found, bad credentials.");

        Authentication auth = _authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String jwt = JwtProvider.generateToken(auth);

        SecurityContextHolder.getContext().setAuthentication(auth);

        UserResponse response = UserResponse.builder()
                .id(isExist.getId())
                .AuthResponse(AuthResponse.builder()
                        .jwt(jwt)
                        .status(true)
                        .message("login successfully")
                        .build())
                .build();

        return response;
    }
}
