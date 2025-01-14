package com.cexchange.Services;

import com.cexchange.Dtos.UserDto;
import com.cexchange.Dtos.UserResponse;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.UUID;

public interface IUserService extends UserDetailsService {
    UserResponse CreateUser(UserDto user);
}
