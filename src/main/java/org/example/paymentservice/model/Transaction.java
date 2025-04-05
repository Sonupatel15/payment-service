package org.example.paymentservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.paymentservice.enums.ModeOfPayment;
import org.example.paymentservice.enums.TransactionStatus;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "transaction_id", updatable = false, nullable = false)
    private UUID transactionId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "from_station_id", nullable = false)
    private int fromStationId;

    @Column(name = "to_station_id", nullable = false)
    private int toStationId;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal distance;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal fare;

    @Enumerated(EnumType.STRING)
    @Column(name = "mode_of_payment", nullable = false)
    private ModeOfPayment modeOfPayment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @PrePersist
    protected void onCreate() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        this.createdAt = now;
        this.updatedAt = now;
        if (this.status == null) {
            this.status = TransactionStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }
}
