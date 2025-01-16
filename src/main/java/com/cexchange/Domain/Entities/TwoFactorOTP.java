package com.cexchange.Domain.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class TwoFactorOTP {
    @Id
    private UUID id;
    private String otp;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToOne
    private User user;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String jwt;
}
