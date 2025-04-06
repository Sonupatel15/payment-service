//package org.example.paymentservice.service;
//
//import lombok.RequiredArgsConstructor;
//import org.example.paymentservice.dto.request.TransactionRequest;
//import org.example.paymentservice.dto.response.MetroCardResponse;
//import org.example.paymentservice.dto.response.TransactionResponse;
//import org.example.paymentservice.enums.ModeOfPayment;
//import org.example.paymentservice.enums.TransactionStatus;
//import org.example.paymentservice.exception.*;
//import org.example.paymentservice.model.Transaction;
//import org.example.paymentservice.repository.TransactionRepository;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.client.RestTemplate;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class TransactionService {
//
//    private final RestTemplate restTemplate;
//    private final TransactionRepository transactionRepository;
//
//    @Value("${user.service.url}")
//    private String userServiceUrl;
//
//    @Value("${metro.service.url}")
//    private String metroServiceUrl;
//
//    private static final BigDecimal PER_KM_FARE = BigDecimal.valueOf(5.0);
//
//    @Transactional
//    public TransactionResponse createTransaction(TransactionRequest request) {
//        // 1. Validate user
//        validateUser(request.getUserId());
//
//        // 2. Validate route & fetch distance
//        BigDecimal distance = fetchDistance(request.getFromStationId(), request.getToStationId());
//
//        // 3. Calculate base fare
//        BigDecimal baseFare = distance.multiply(PER_KM_FARE)
//                .setScale(2, RoundingMode.HALF_UP); // round to 2 decimals
//
//        // 4. Handle CARD payment
//        if (request.getModeOfPayment() == ModeOfPayment.CARD) {
//            MetroCardResponse card = fetchMetroCard(request.getUserId());
//
//            if (!card.isActive()) {
//                throw new MetroCardNotFoundException("Metro card is not active for user: " + request.getUserId());
//            }
//
//            BigDecimal cardBalance = card.getBalance();
//            if (cardBalance.compareTo(baseFare) < 0) {
//                throw new InsufficientBalanceException("Insufficient balance on metro card for user: " + request.getUserId());
//            }
//
//            // Deduct full fare first
//            deductFareFromMetroCard(card.getCardId(), baseFare);
//
//            // Apply 10% discount AFTER deduction
//            baseFare = baseFare.multiply(BigDecimal.valueOf(0.9))
//                    .setScale(2, RoundingMode.HALF_UP);
//        }
//
//        // 5. Save transaction
//        Transaction transaction = saveTransaction(request, distance, baseFare);
//
//        // 6. Return response
//        return mapToTransactionResponse(transaction);
//    }
//
//    private void validateUser(UUID userId) {
//        String userUrl = userServiceUrl + "/api/users/" + userId;
//        try {
//            restTemplate.getForObject(userUrl, Object.class);
//        } catch (Exception e) {
//            throw new UserNotFoundException("User not found with ID: " + userId);
//        }
//    }
//
//    private BigDecimal fetchDistance(int fromStationId, int toStationId) {
//        String url = metroServiceUrl + "/api/fare-chart/distance?fromStationId=" + fromStationId +
//                "&toStationId=" + toStationId;
//        try {
//            Double distance = restTemplate.getForObject(url, Double.class);
//            if (distance == null) {
//                throw new RouteNotFoundException("Route distance is null.");
//            }
//            return BigDecimal.valueOf(distance).setScale(2, RoundingMode.HALF_UP);
//        } catch (Exception e) {
//            throw new RouteNotFoundException("Route not found between stations " + fromStationId + " and " + toStationId);
//        }
//    }
//
//    private MetroCardResponse fetchMetroCard(UUID userId) {
//        String url = userServiceUrl + "/api/metro-cards/user/" + userId;
//        try {
//            return restTemplate.getForObject(url, MetroCardResponse.class);
//        } catch (Exception e) {
//            throw new MetroCardNotFoundException("Metro card not found for user ID: " + userId);
//        }
//    }
//
//    private void deductFareFromMetroCard(UUID cardId, BigDecimal fare) {
//        String url = userServiceUrl + "/api/metro-cards/deduct?cardId=" + cardId + "&amount=" + fare;
//        try {
//            restTemplate.put(url, null);
//        } catch (Exception e) {
//            throw new MetroCardUpdateException("Failed to deduct fare from metro card");
//        }
//    }
//
//    private Transaction saveTransaction(TransactionRequest request, BigDecimal distance, BigDecimal fare) {
//        Transaction transaction = Transaction.builder()
//                .userId(request.getUserId())
//                .fromStationId(request.getFromStationId())
//                .toStationId(request.getToStationId())
//                .distance(distance)
//                .fare(fare)
//                .modeOfPayment(request.getModeOfPayment())
//                .status(TransactionStatus.SUCCESS)
//                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
//                .updatedAt(Timestamp.valueOf(LocalDateTime.now()))
//                .build();
//        return transactionRepository.save(transaction);
//    }
//
//    private TransactionResponse mapToTransactionResponse(Transaction transaction) {
//        return TransactionResponse.builder()
//                .transactionId(transaction.getTransactionId())
//                .userId(transaction.getUserId())
//                .fromStationId(transaction.getFromStationId())
//                .toStationId(transaction.getToStationId())
//                .distance(transaction.getDistance())
//                .fare(transaction.getFare())
//                .modeOfPayment(transaction.getModeOfPayment())
//                .status(transaction.getStatus())
//                .createdAt(transaction.getCreatedAt().toLocalDateTime())
//                .updatedAt(transaction.getUpdatedAt().toLocalDateTime())
//                .build();
//    }
//}


package org.example.paymentservice.service;

import lombok.RequiredArgsConstructor;
import org.example.paymentservice.dto.request.TransactionRequest;
import org.example.paymentservice.dto.request.TravelHistoryRequest;
import org.example.paymentservice.dto.response.MetroCardResponse;
import org.example.paymentservice.dto.response.TransactionResponse;
import org.example.paymentservice.enums.ModeOfPayment;
import org.example.paymentservice.enums.TransactionStatus;
import org.example.paymentservice.enums.TravelStatus;
import org.example.paymentservice.exception.*;
import org.example.paymentservice.model.Transaction;
import org.example.paymentservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final RestTemplate restTemplate;
    private final TransactionRepository transactionRepository;

    @Value("${user.service.url}")
    private String userServiceUrl;

    @Value("${metro.service.url}")
    private String metroServiceUrl;

    private static final BigDecimal PER_KM_FARE = BigDecimal.valueOf(5.0);

    @Transactional
    public TransactionResponse createTransaction(TransactionRequest request) {
        // 1. Validate user
        validateUser(request.getUserId());

        // 2. Validate route & fetch distance
        BigDecimal distance = fetchDistance(request.getFromStationId(), request.getToStationId());

        // 3. Calculate base fare
        BigDecimal baseFare = distance.multiply(PER_KM_FARE)
                .setScale(2, RoundingMode.HALF_UP);

        // 4. Handle CARD payment
        if (request.getModeOfPayment() == ModeOfPayment.CARD) {
            MetroCardResponse card = fetchMetroCard(request.getUserId());

            if (!card.isActive()) {
                throw new MetroCardNotFoundException("Metro card is not active for user: " + request.getUserId());
            }

            if (card.getBalance().compareTo(baseFare) < 0) {
                throw new InsufficientBalanceException("Insufficient balance on metro card for user: " + request.getUserId());
            }

            // Deduct full fare
            deductFareFromMetroCard(card.getCardId(), baseFare);

            // Apply 10% discount
            baseFare = baseFare.multiply(BigDecimal.valueOf(0.9)).setScale(2, RoundingMode.HALF_UP);
        }

        // 5. Save transaction
        Transaction transaction = saveTransaction(request, distance, baseFare);

        // 6. Notify User Service (create travel history) if SUCCESS
        if (transaction.getStatus() == TransactionStatus.SUCCESS) {
            notifyUserService(transaction);
        }

        // 7. Return response
        return mapToTransactionResponse(transaction);
    }

    private void notifyUserService(Transaction transaction) {
        String url = userServiceUrl + "/api/travel-history";

        TravelHistoryRequest request = TravelHistoryRequest.builder()
                .transactionId(transaction.getTransactionId())
                .userId(transaction.getUserId())
                .fromStation((int) transaction.getFromStationId())
                .toStation((int) transaction.getToStationId())
                .distance(transaction.getDistance())
                .baseFare(transaction.getFare())
                .penalty(BigDecimal.ZERO)
                .totalFare(transaction.getFare())
                .status(TravelStatus.NOT_STARTED)
                .build();

        try {
            HttpEntity<TravelHistoryRequest> entity = new HttpEntity<>(request);
            restTemplate.postForObject(url, entity, Void.class);
        } catch (Exception e) {
            // You can log or retry here if you want
            throw new RuntimeException("Failed to notify User Service: " + e.getMessage());
        }
    }

    private void validateUser(UUID userId) {
        String userUrl = userServiceUrl + "/api/users/" + userId;
        try {
            restTemplate.getForObject(userUrl, Object.class);
        } catch (Exception e) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
    }

    private BigDecimal fetchDistance(int fromStationId, int toStationId) {
        String url = metroServiceUrl + "/api/fare-chart/distance?fromStationId=" + fromStationId +
                "&toStationId=" + toStationId;
        try {
            Double distance = restTemplate.getForObject(url, Double.class);
            if (distance == null) {
                throw new RouteNotFoundException("Route distance is null.");
            }
            return BigDecimal.valueOf(distance).setScale(2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            throw new RouteNotFoundException("Route not found between stations " + fromStationId + " and " + toStationId);
        }
    }

    private MetroCardResponse fetchMetroCard(UUID userId) {
        String url = userServiceUrl + "/api/metro-cards/user/" + userId;
        try {
            return restTemplate.getForObject(url, MetroCardResponse.class);
        } catch (Exception e) {
            throw new MetroCardNotFoundException("Metro card not found for user ID: " + userId);
        }
    }

    private void deductFareFromMetroCard(UUID cardId, BigDecimal fare) {
        String url = userServiceUrl + "/api/metro-cards/deduct?cardId=" + cardId + "&amount=" + fare;
        try {
            restTemplate.put(url, null);
        } catch (Exception e) {
            throw new MetroCardUpdateException("Failed to deduct fare from metro card");
        }
    }

    private Transaction saveTransaction(TransactionRequest request, BigDecimal distance, BigDecimal fare) {
        Transaction transaction = Transaction.builder()
                .userId(request.getUserId())
                .fromStationId(request.getFromStationId())
                .toStationId(request.getToStationId())
                .distance(distance)
                .fare(fare)
                .modeOfPayment(request.getModeOfPayment())
                .status(TransactionStatus.SUCCESS)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .updatedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        return transactionRepository.save(transaction);
    }

    private TransactionResponse mapToTransactionResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .transactionId(transaction.getTransactionId())
                .userId(transaction.getUserId())
                .fromStationId(transaction.getFromStationId())
                .toStationId(transaction.getToStationId())
                .distance(transaction.getDistance())
                .fare(transaction.getFare())
                .modeOfPayment(transaction.getModeOfPayment())
                .status(transaction.getStatus())
                .createdAt(transaction.getCreatedAt().toLocalDateTime())
                .updatedAt(transaction.getUpdatedAt().toLocalDateTime())
                .build();
    }
}