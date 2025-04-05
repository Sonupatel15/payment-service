package org.example.paymentservice.controller;

import jakarta.validation.Valid;
import org.example.paymentservice.dto.request.TransactionRequest;
import org.example.paymentservice.dto.response.TransactionResponse;
import org.example.paymentservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody TransactionRequest request) {
        TransactionResponse response = transactionService.createTransaction(request);
        return ResponseEntity.ok(response);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<TransactionResponse> getTransaction(@PathVariable UUID id) {
//        return ResponseEntity.ok(transactionService.getTransaction(id));
//    }
//
//    @GetMapping
//    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
//        return ResponseEntity.ok(transactionService.getAllTransactions());
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<TransactionResponse> updateTransaction(
//            @PathVariable UUID id, @RequestBody TransactionRequest request) {
//        return ResponseEntity.ok(transactionService.updateTransaction(id, request));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteTransaction(@PathVariable UUID id) {
//        transactionService.deleteTransaction(id);
//        return ResponseEntity.noContent().build();
//    }
}