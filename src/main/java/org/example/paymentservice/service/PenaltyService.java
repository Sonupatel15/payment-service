package org.example.paymentservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.paymentservice.dto.request.PenaltyRequest;
import org.example.paymentservice.dto.request.UpdatePenaltyRequest;
import org.example.paymentservice.dto.response.*;
import org.example.paymentservice.enums.PenaltyStatus;
import org.example.paymentservice.model.Penalty;
import org.example.paymentservice.repository.PenaltyRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class PenaltyService {
//
//    private final PenaltyRepository penaltyRepository;
//    private final RestTemplate restTemplate;
//
//    @Value("${metro.service.url}")
//    private String metroServiceUrl;
//
//    @Value("${user.service.url}")
//    private String userServiceUrl;
//
//    public Penalty createPenalty(UUID timingId) {
//        // Step 1: Get Timing from Metro Service
//        ResponseEntity<TimingResponse> response = restTemplate.getForEntity(
//                metroServiceUrl + "/timings/" + timingId,
//                TimingResponse.class
//        );
//
//        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
//            throw new RuntimeException("Failed to fetch timing from Metro Service");
//        }
//
//        TimingResponse timing = response.getBody();
//
//        if (timing.getCheckin() == null || timing.getCheckout() == null) {
//            throw new RuntimeException("Incomplete checkin/checkout data");
//        }
//
//        // Step 2: Calculate time difference
//        long minutes = Duration.between(timing.getCheckin().toInstant(), timing.getCheckout().toInstant()).toMinutes();
//        if (minutes <= 90) {
//            return null; // No penalty
//        }
//
//        // Step 3: Add penalty
//        BigDecimal amount = BigDecimal.valueOf(20); // Example amount
//        Penalty penalty = Penalty.builder()
//                .timingId(timingId)
//                .travelId(timing.getTravelId())
//                .amount(amount)
//                .status(PenaltyStatus.NOT_PAID)
//                .build();
//
//        penalty = penaltyRepository.save(penalty);
//
//        // Step 4: Update penalty in User Service
//        UpdatePenaltyRequest update = new UpdatePenaltyRequest(timing.getTravelId(), amount);
//        restTemplate.postForEntity(userServiceUrl + "/travel-histories/update-penalty", update, Void.class);
//
//        return penalty;
//    }
//
//    public PenaltyStatusResponse getPenaltyStatus(UUID timingId) {
//        Penalty penalty = penaltyRepository.findByTimingId(timingId)
//                .orElseThrow(() -> new RuntimeException("No penalty found for timingId"));
//
//        return new PenaltyStatusResponse(timingId, penalty.getStatus().name());
//    }
//}


@Service
@RequiredArgsConstructor
public class PenaltyService {

    private final PenaltyRepository penaltyRepository;
    private final RestTemplate restTemplate;

    @Value("${user.service.url}")
    private String userServiceUrl;
    @Value("${metro.service.url}")
    private String metroServiceUrl;

    @Transactional
    public PenaltyResponse createPenalty(PenaltyRequest request) {
        // 1. Fetch Base Fare from User Service
        BigDecimal baseFare = fetchBaseFare(request.getTravelId());

        // 2. Calculate Penalty (50% of base fare)
        BigDecimal penaltyAmount = baseFare.multiply(new BigDecimal("0.5"));

        // 3. Create Penalty Record
        Penalty penalty = Penalty.builder()
                .travelId(request.getTravelId())
                .timingId(request.getTimingId())
                .amount(penaltyAmount)
                .status(PenaltyStatus.NOT_PAID)
                .build();

        penaltyRepository.save(penalty);

        // 4. Update User Service (TravelHistory)
        UpdatePenaltyRequest updateRequest = new UpdatePenaltyRequest(
                request.getTravelId(),
                penaltyAmount
        );

        restTemplate.postForEntity(
                userServiceUrl + "/api/travel-history/update-penalty",
                updateRequest,
                Void.class
        );

        // 5. Return Response
        return PenaltyResponse.builder()
                .penaltyId(penalty.getPenaltyId())
                .travelId(penalty.getTravelId())
                .timingId(penalty.getTimingId())
                .amount(penalty.getAmount())
                .status(penalty.getStatus())
                .appliedAt(penalty.getAppliedAt())
                .build();
    }

    // Helper: Fetch Base Fare from User Service
    private BigDecimal fetchBaseFare(UUID travelId) {
        ResponseEntity<BigDecimal> response = restTemplate.getForEntity(
                userServiceUrl + "/api/travel-history/" + travelId + "/base-fare",
                BigDecimal.class
        );
        return response.getBody();
    }

    public PenaltyStatusResponse getPenaltyStatus(UUID timingId) {
        Penalty penalty = penaltyRepository.findByTimingId(timingId)
                .orElseThrow(() -> new RuntimeException("No penalty found"));
        return new PenaltyStatusResponse(timingId, penalty.getStatus().name());
    }
}

