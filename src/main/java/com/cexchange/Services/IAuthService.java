package com.cexchange.Services;

import com.cexchange.Domain.Entities.TwoFactorOTP;
import com.cexchange.Domain.Entities.User;
import com.cexchange.Dtos.LoginDto;
import com.cexchange.Dtos.UserDto;
import com.cexchange.Dtos.UserResponse;

import java.util.UUID;

public interface IAuthService {
    UserResponse CreateUser(UserDto user);
    UserResponse LoginUser(LoginDto request);
}
