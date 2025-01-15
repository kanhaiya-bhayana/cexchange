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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final IUserRepository _userRepository;

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
