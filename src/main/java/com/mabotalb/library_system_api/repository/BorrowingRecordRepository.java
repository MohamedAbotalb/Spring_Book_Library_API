package com.mabotalb.library_system_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mabotalb.library_system_api.entity.BorrowingRecord;

public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {
    @Query(value = "SELECT * FROM borrowing_records br " +
                   "WHERE br.book_id =:bookId AND br.patron_id =:patronId AND br.return_date IS NULL", nativeQuery = true)
    BorrowingRecord findByBookIdAndPatronId(Long bookId, Long patronId);

    @Query(value = "SELECT * FROM borrowing_records br " +
                   "WHERE br.book_id =:bookId AND br.return_date IS NULL", nativeQuery = true)
    BorrowingRecord findByBookId(Long bookId);

    @Query(value = "SELECT * FROM borrowing_records br " +
                   "WHERE br.patron_id =:patronId AND br.return_date IS NULL", nativeQuery = true)
    BorrowingRecord findByPatronId(Long patronId);
}
