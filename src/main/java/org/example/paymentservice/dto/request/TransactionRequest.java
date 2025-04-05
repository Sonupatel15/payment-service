package org.example.paymentservice.dto.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
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

    private int fromStationId;

    private int toStationId;

    private ModeOfPayment modeOfPayment;

    // Optional: default to PENDING if not provided
    @JsonSetter(nulls = Nulls.SKIP)
    private TransactionStatus status = TransactionStatus.PENDING;
}