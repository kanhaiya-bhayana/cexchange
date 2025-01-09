package com.cexchange.Services;

import com.cexchange.Domain.Entities.User;
import com.cexchange.Dtos.UserDto;
import com.cexchange.Mapper.UserMapper;
import com.cexchange.Repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final IUserRepository _userRepository;
    @Override
    public UUID CreateUser(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto);
        if (user == null)
            throw new RuntimeException("cannot create user at the moment");

        _userRepository.save(user);
        return user.getId();
    }
}
