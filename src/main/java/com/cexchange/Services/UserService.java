package com.cexchange.Services;

import com.cexchange.Config.Jwt.JwtProvider;
import com.cexchange.Domain.Entities.User;
import com.cexchange.Dtos.AuthResponse;
import com.cexchange.Dtos.UserDto;
import com.cexchange.Dtos.UserResponse;
import com.cexchange.Exceptions.UserAlreadyExistsException;
import com.cexchange.Mapper.UserMapper;
import com.cexchange.Repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final IUserRepository _userRepository;
    @Override
    public UserResponse CreateUser(UserDto userDto) {
        User isEmailExist = _userRepository.findByEmail(userDto.getEmail());
        if (isEmailExist != null)
                throw new UserAlreadyExistsException("An account already exist with this email: " + userDto.getEmail());

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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = _userRepository.findByEmail(username);
        if (user == null)
            throw new UsernameNotFoundException(username);
        List<GrantedAuthority> authorityList = new ArrayList<>();

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), authorityList
        );
    }
}
