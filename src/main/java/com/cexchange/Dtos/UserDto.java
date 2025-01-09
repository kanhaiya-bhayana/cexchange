package com.cexchange.Dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
    private String fullName;
    private String email;
    private String password;
}
