package com.mabotalb.library_system_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import com.mabotalb.library_system_api.response.ApiResponse;
import com.mabotalb.library_system_api.entity.BorrowingRecord;
import com.mabotalb.library_system_api.service.BorrowingRecordService;

@RestController
@RequestMapping("/api")
@Validated
public class BorrowingRecordController {
    @Autowired
    private BorrowingRecordService borrowingRecordService;

    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<ApiResponse<BorrowingRecord>> borrowBook(
            @PathVariable @Positive Long bookId,
            @PathVariable @Positive Long patronId) {
        BorrowingRecord record = borrowingRecordService.borrowBook(bookId, patronId);
        ApiResponse<BorrowingRecord> response = new ApiResponse<>("Book is borrowed successfully!", record);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/return/{bookId}/patron/{patronId}")
    public ResponseEntity<ApiResponse<BorrowingRecord>> returnBook(
            @PathVariable @Positive Long bookId,
            @PathVariable @Positive Long patronId) {
        BorrowingRecord record = borrowingRecordService.returnBook(bookId, patronId);
        ApiResponse<BorrowingRecord> response = new ApiResponse<>("Book is returned successfully!", record);
        return ResponseEntity.ok(response);
    }
}
