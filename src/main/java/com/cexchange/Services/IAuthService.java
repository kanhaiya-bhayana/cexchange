package com.cexchange.Services;

import com.cexchange.Dtos.LoginDto;
import com.cexchange.Dtos.UserDto;
import com.cexchange.Dtos.UserResponse;

public interface IAuthService {
    UserResponse CreateUser(UserDto user);
    UserResponse LoginUser(LoginDto request);
}
