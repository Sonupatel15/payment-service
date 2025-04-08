package org.example.paymentservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.paymentservice.dto.request.PenaltyRequest;
import org.example.paymentservice.dto.response.PenaltyResponse;
import org.example.paymentservice.dto.response.PenaltyStatusResponse;
import org.example.paymentservice.model.Penalty;
import org.example.paymentservice.service.PenaltyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/penalties")
@RequiredArgsConstructor
public class PenaltyController {

    private final PenaltyService penaltyService;

    @PostMapping
    public ResponseEntity<PenaltyResponse> createPenalty(@RequestBody PenaltyRequest request) {
        PenaltyResponse response = penaltyService.createPenalty(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/status")
    public ResponseEntity<PenaltyStatusResponse> getStatus(@RequestParam UUID timingId) {
        return ResponseEntity.ok(penaltyService.getPenaltyStatus(timingId));
    }
}