package org.example.paymentservice.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class MetroCardResponse {

    private UUID cardId;
    private UUID userId;
    private BigDecimal balance;
    private boolean active;

}