package org.example.paymentservice.dto.request;

import org.example.paymentservice.enums.ModeOfPayment;
import org.example.paymentservice.enums.TransactionStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequest {
    private UUID userId;

    private Long fromStationId;

    private Long toStationId;

    private ModeOfPayment modeOfPayment;

    // Optional: default to PENDING if not provided
    private TransactionStatus status;
}