package com.cexchange.Mapper;

import com.cexchange.Domain.Entities.User;
import com.cexchange.Dtos.UserDto;

public class UserMapper {
    public static User mapToUser(UserDto userDto) {
        if (userDto == null) return null;
//        User.builder().build();
        return new User();//.builder()
//                .fullName(userDto.getFullName())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .build();
    }
}
