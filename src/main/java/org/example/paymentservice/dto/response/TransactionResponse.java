package org.example.paymentservice.dto.response;

import org.example.paymentservice.enums.ModeOfPayment;
import org.example.paymentservice.enums.TransactionStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {

    private UUID transactionId;

    private UUID userId;

    private Long fromStationId;

    private Long toStationId;

    private BigDecimal distance;

    private BigDecimal fare;

    private ModeOfPayment modeOfPayment;

    private TransactionStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}