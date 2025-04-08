package org.example.paymentservice.dto.response;

import lombok.*;
import org.example.paymentservice.enums.PenaltyStatus;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PenaltyDTO {
    private UUID travelId;
    private UUID timingId;
    private BigDecimal amount;
    private PenaltyStatus status;
}