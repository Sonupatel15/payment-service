package org.example.paymentservice.dto.response;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class TimingResponse {
    private UUID timingId;
    private UUID travelId;
    private Timestamp checkin;
    private Timestamp checkout;
}
