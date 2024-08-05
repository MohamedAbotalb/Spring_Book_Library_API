package com.mabotalb.library_system_api.service.implementation;

import com.mabotalb.library_system_api.entity.Book;
import com.mabotalb.library_system_api.entity.BorrowingRecord;
import com.mabotalb.library_system_api.entity.Patron;
import com.mabotalb.library_system_api.repository.BookRepository;
import com.mabotalb.library_system_api.repository.BorrowingRecordRepository;
import com.mabotalb.library_system_api.repository.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class BorrowingRecordImplementation {

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PatronRepository patronRepository;

    public BorrowingRecord borrowBook(Long bookId, Long patronId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
        Patron patron = patronRepository.findById(patronId).orElseThrow(() -> new RuntimeException("Patron not found"));

        BorrowingRecord record = new BorrowingRecord();
        record.setBook(book);
        record.setPatron(patron);
        record.setBorrowingDate(LocalDate.now());

        return borrowingRecordRepository.save(record);
    }

    public BorrowingRecord returnBook(Long bookId, Long patronId) {
        BorrowingRecord record = borrowingRecordRepository.findByBookIdAndPatronId(bookId, patronId)
                .orElseThrow(() -> new RuntimeException("Borrowing record not found for this book and patron"));
        record.setReturnDate(LocalDate.now());
        return borrowingRecordRepository.save(record);
    }
}
