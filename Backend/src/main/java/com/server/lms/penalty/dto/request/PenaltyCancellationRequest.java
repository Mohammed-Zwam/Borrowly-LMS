package com.server.lms.penalty.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class PenaltyCancellationRequest {
    @NotNull(message = "Reason is required")
    @Length(max = 500)
    private String reason;
}
