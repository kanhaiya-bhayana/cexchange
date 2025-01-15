package com.cexchange.Dtos;

import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
public class UserResponse {
    public UUID id;
    public boolean error;
    public String errorMessage;
    @Embedded
    private AuthResponse AuthResponse;
}
