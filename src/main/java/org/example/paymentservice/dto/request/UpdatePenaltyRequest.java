package org.example.paymentservice.dto.request;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePenaltyRequest {
    private UUID travelId;
    private BigDecimal penaltyAmount;
}