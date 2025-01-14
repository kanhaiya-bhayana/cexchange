package com.cexchange.Mapper;

import com.cexchange.Domain.Entities.User;
import com.cexchange.Domain.Enums.UserRole;
import com.cexchange.Dtos.UserDto;

public class UserMapper {
    public static User mapToUser(UserDto userDto) {
        if (userDto == null) return null;
        User user = User.builder()
                .fullName(userDto.getFullName())
                .email(userDto.getEmail())
                .role(UserRole.Customer)
                .password(userDto.getPassword())
                .build();


        User user1 = new User();
        return user;
    }
}
