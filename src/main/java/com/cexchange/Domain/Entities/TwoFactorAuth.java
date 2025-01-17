package com.cexchange.Domain.Entities;

import com.cexchange.Domain.Enums.VerificationType;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class TwoFactorAuth {
    private boolean isEnabled = true;
    private VerificationType sendTo;
}
