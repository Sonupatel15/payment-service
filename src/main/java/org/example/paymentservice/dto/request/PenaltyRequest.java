package org.example.paymentservice.dto.request;

import lombok.*;
import org.example.paymentservice.enums.PenaltyStatus;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PenaltyRequest {
    private UUID travelId;
    private UUID timingId;
}