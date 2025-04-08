package org.example.paymentservice.dto.response;

import lombok.*;
import org.example.paymentservice.enums.PenaltyStatus;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PenaltyResponse {
    private UUID penaltyId;
    private UUID travelId;
    private UUID timingId;
    private BigDecimal amount;
    private PenaltyStatus status;
    private Timestamp appliedAt;
}