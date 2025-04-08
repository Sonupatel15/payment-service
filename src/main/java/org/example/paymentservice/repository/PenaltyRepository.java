package org.example.paymentservice.repository;

import org.example.paymentservice.model.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PenaltyRepository extends JpaRepository<Penalty, UUID> {
    Optional<Penalty> findByTravelId(UUID travelId);
    Optional<Penalty> findByTimingId(UUID timingId);
}