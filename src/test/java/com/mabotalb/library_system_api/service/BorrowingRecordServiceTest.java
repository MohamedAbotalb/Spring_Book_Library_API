package com.mabotalb.library_system_api.service;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mabotalb.library_system_api.entity.Book;
import com.mabotalb.library_system_api.entity.Patron;
import com.mabotalb.library_system_api.entity.BorrowingRecord;
import com.mabotalb.library_system_api.exception.NotFoundException;
import com.mabotalb.library_system_api.repository.BorrowingRecordRepository;

class BorrowingRecordServiceTest {
    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @Mock
    private BookService bookService;

    @Mock
    private PatronService patronService;

    @InjectMocks
    private BorrowingRecordService borrowingRecordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBorrowBook_Success() {
        Long bookId = 1L;
        Long patronId = 1L;

        Book book = new Book("9781234567890", "Test Book", "Mohamed Abotalb", 2020);
        book.setId(bookId);

        Patron patron = new Patron("Ahmed Abotalb", "ahmedabotalb@example.com");
        patron.setId(patronId);

        when(bookService.getBook(bookId)).thenReturn(book);
        when(patronService.getPatron(patronId)).thenReturn(patron);
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenAnswer(invocation -> {
            BorrowingRecord record = invocation.getArgument(0);
            record.setId(1L);
            return record;
        });

        BorrowingRecord result = borrowingRecordService.borrowBook(bookId, patronId);

        assertNotNull(result);
        assertEquals(book, result.getBook());
        assertEquals(patron, result.getPatron());
        assertTrue(book.isBorrowed());
        verify(bookService, times(1)).updateBook(bookId, book);
        verify(borrowingRecordRepository, times(1)).save(any(BorrowingRecord.class));
    }

    @Test
    void testBorrowBook_BookAlreadyBorrowed() {
        Long bookId = 1L;
        Long patronId = 1L;

        Book book = new Book("9781234567890", "Test Book", "Mohamed Abotalb", 2020);
        book.setId(bookId);
        book.setBorrowed(true);

        when(bookService.getBook(bookId)).thenReturn(book);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            borrowingRecordService.borrowBook(bookId, patronId);
        });

        assertEquals("This book is already borrowed", exception.getMessage());
        verify(bookService, never()).updateBook(anyLong(), any(Book.class));
        verify(borrowingRecordRepository, never()).save(any(BorrowingRecord.class));
    }

    @Test
    void testReturnBook_Success() {
        Long bookId = 1L;
        Long patronId = 1L;

        Book book = new Book("9781234567890", "Test Book", "Mohamed Abotalb", 2020);
        book.setId(bookId);
        book.setBorrowed(true);

        Patron patron = new Patron("Ahmed Abotalb", "ahmedabotalb@example.com");
        patron.setId(patronId);

        BorrowingRecord record = new BorrowingRecord(book, patron);
        record.setId(1L);

        when(bookService.getBook(bookId)).thenReturn(book);
        when(patronService.getPatron(patronId)).thenReturn(patron);
        when(borrowingRecordRepository.findByBookIdAndPatronId(bookId, patronId)).thenReturn(record);
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenAnswer(invocation -> {
            BorrowingRecord updatedRecord = invocation.getArgument(0);
            updatedRecord.setReturnDate(LocalDate.now());
            return updatedRecord;
        });

        BorrowingRecord result = borrowingRecordService.returnBook(bookId, patronId);

        assertNotNull(result);
        assertEquals(LocalDate.now(), result.getReturnDate());
        assertFalse(book.isBorrowed());
        verify(bookService, times(1)).updateBook(bookId, book);
        verify(borrowingRecordRepository, times(1)).save(record);
    }

    @Test
    void testReturnBook_NotBorrowed() {
        Long bookId = 1L;
        Long patronId = 1L;

        Book book = new Book("9781234567890", "Test Book", "Mohamed Abotalb", 2020);
        book.setId(bookId);

        when(bookService.getBook(bookId)).thenReturn(book);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            borrowingRecordService.returnBook(bookId, patronId);
        });

        assertEquals("This book isn't borrowed", exception.getMessage());
        verify(bookService, never()).updateBook(anyLong(), any(Book.class));
        verify(borrowingRecordRepository, never()).save(any(BorrowingRecord.class));
    }

    @Test
    void testReturnBook_NoBorrowingRecord() {
        Long bookId = 1L;
        Long patronId = 1L;

        Book book = new Book("9781234567890", "Test Book", "Mohamed Abotalb", 2020);
        book.setId(bookId);
        book.setBorrowed(true);

        Patron patron = new Patron("Ahmed Abotalb", "ahmedabotalb@example.com");
        patron.setId(patronId);

        when(bookService.getBook(bookId)).thenReturn(book);
        when(patronService.getPatron(patronId)).thenReturn(patron);
        when(borrowingRecordRepository.findByBookIdAndPatronId(bookId, patronId)).thenReturn(null);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            borrowingRecordService.returnBook(bookId, patronId);
        });

        assertEquals("There is no borrowing record for this book and patron", exception.getMessage());
        verify(bookService, never()).updateBook(anyLong(), any(Book.class));
        verify(borrowingRecordRepository, never()).save(any(BorrowingRecord.class));
    }
}
