package com.mabotalb.library_system_api.service.implementation;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.mabotalb.library_system_api.entity.Book;
import com.mabotalb.library_system_api.entity.Patron;
import com.mabotalb.library_system_api.entity.BorrowingRecord;
import com.mabotalb.library_system_api.service.BookService;
import com.mabotalb.library_system_api.service.PatronService;
import com.mabotalb.library_system_api.service.BorrowingRecordService;
import com.mabotalb.library_system_api.exception.NotFoundException;
import com.mabotalb.library_system_api.repository.BorrowingRecordRepository;

@Service
public class BorrowingRecordImplementation implements BorrowingRecordService {
    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private PatronService patronService;

    // Make a borrow request for a book with a specific patron using bookId and patronId
    @Transactional
    @Override
    public BorrowingRecord borrowBook(Long bookId, Long patronId) {
        Book book = bookService.getBook(bookId);
        Patron patron = patronService.getPatron(patronId);

        if (book.isBorrowed())
            throw new RuntimeException("This book is already borrowed");
        book.setBorrowed(true);
        bookService.updateBook(bookId, book);

        BorrowingRecord record = new BorrowingRecord(book, patron);
        return borrowingRecordRepository.save(record);
    }

    // Make a return request for a book with a specific patron using bookId and patronId
    @Transactional
    @Override
    public BorrowingRecord returnBook(Long bookId, Long patronId) {
        Book book = bookService.getBook(bookId);
        Patron patron = patronService.getPatron(patronId);
        BorrowingRecord record = borrowingRecordRepository.findByBookIdAndPatronId(bookId, patronId);
        if (record == null) {
            throw new NotFoundException("There is no borrowing record for this book and patron");
        }
        if (book.isBorrowed() && record.getReturnDate() == null) {
            book.setBorrowed(false);
            bookService.updateBook(bookId, book);
            record.setReturnDate(LocalDate.now());
            return borrowingRecordRepository.save(record);
        } else
            throw new RuntimeException("This book isn't borrowed");
    }
}
