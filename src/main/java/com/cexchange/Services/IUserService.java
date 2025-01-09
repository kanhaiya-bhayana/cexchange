package com.cexchange.Services;

import com.cexchange.Dtos.UserDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

public interface IUserService {
    UUID CreateUser(UserDto user);
}
