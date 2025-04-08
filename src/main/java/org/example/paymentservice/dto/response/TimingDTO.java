package org.example.paymentservice.dto.response;

import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimingDTO {
    private UUID timingId;
    private UUID travelId;
    private Timestamp checkin;
    private Timestamp checkout;
}