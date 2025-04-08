package org.example.paymentservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.paymentservice.enums.PenaltyStatus;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "penalties", uniqueConstraints = @UniqueConstraint(columnNames = "travel_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Penalty {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "penalty_id", nullable = false, updatable = false)
    private UUID penaltyId;

    @Column(name = "travel_id", nullable = false, unique = true)
    private UUID travelId;  // Foreign key to travel_history(travel_id)

    @Column(name = "timing_id", nullable = false)
    private UUID timingId;  // Foreign key to Metro Service Timing (inter-service)

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PenaltyStatus status;

    @Column(name = "applied_at", nullable = false, updatable = false)
    private Timestamp appliedAt;

    @PrePersist
    public void onCreate() {
        this.appliedAt = new Timestamp(System.currentTimeMillis());
        if (this.status == null) {
            this.status = PenaltyStatus.NOT_PAID;
        }
    }
}