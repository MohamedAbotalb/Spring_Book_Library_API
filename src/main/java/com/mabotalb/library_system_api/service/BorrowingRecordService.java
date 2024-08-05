package com.mabotalb.library_system_api.service;

import com.mabotalb.library_system_api.entity.BorrowingRecord;

public interface BorrowingRecordService {
    BorrowingRecord borrowBook(Long bookId, Long patronId);

    BorrowingRecord returnBook(Long bookId, Long patronId);
}
