package com.mabotalb.library_system_api.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mabotalb.library_system_api.entity.Book;
import com.mabotalb.library_system_api.entity.BorrowingRecord;
import com.mabotalb.library_system_api.exception.NotFoundException;
import com.mabotalb.library_system_api.repository.BookRepository;
import com.mabotalb.library_system_api.repository.BorrowingRecordRepository;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBooks() {
        Book book1 = new Book("9781234567890", "Test Book 1", "Mohamed Abotalb", 2020);
        book1.setId(1L);

        Book book2 = new Book( "9781234567891", "Test Book 2", "Mohamed Abotalb", 2021);
        book2.setId(2L);

        List<Book> books = Arrays.asList(book1, book2);
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.getAllBooks();

        assertEquals(2, result.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testGetBook_Success() {
        Book book = new Book("9781234567890", "Test Book", "Mohamed Abotalb", 2020);
        book.setId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book result = bookService.getBook(1L);

        assertNotNull(result);
        assertEquals("Test Book", result.getTitle());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void testGetBook_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            bookService.getBook(1L);
        });

        assertEquals("Book not found with id 1", exception.getMessage());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void testAddBook() {
        Book book = new Book("9781234567890", "Test Book", "Mohamed Abotalb", 2020);
        book.setId(1L);

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = bookService.addBook(book);

        assertNotNull(result);
        assertEquals("Test Book", result.getTitle());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testUpdateBook_Success() {
        Book existingBook = new Book("9781234567890", "Old Title", "Mohamed Abotalb", 2020);
        existingBook.setId(1L);

        Book updatedBookDetails = new Book("9781234567891", "New Title", "Mohamed Abotalb", 2020);
        updatedBookDetails.setId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBookDetails);

        Book result = bookService.updateBook(1L, updatedBookDetails);

        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(existingBook);
    }

    @Test
    void testUpdateBook_NotFound() {
        Book updatedBookDetails = new Book("9781234567890", "New Title", "Mohamed Abotalb", 2020);
        updatedBookDetails.setId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            bookService.updateBook(1L, updatedBookDetails);
        });

        assertEquals("Book not found with id 1", exception.getMessage());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteBook_Success() {
        when(borrowingRecordRepository.findByBookId(1L)).thenReturn(null);
        when(bookRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> {
            bookService.deleteBook(1L);
        });

        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteBook_InBorrowingRecord() {
        BorrowingRecord activeRecord = new BorrowingRecord();
        when(borrowingRecordRepository.findByBookId(1L)).thenReturn(activeRecord);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookService.deleteBook(1L);
        });

        assertEquals("This book can't be deleted as it's already in a borrowing record", exception.getMessage());
        verify(bookRepository, times(0)).deleteById(1L);
    }

    @Test
    void testDeleteBook_NotFound() {
        when(borrowingRecordRepository.findByBookId(1L)).thenReturn(null);
        when(bookRepository.existsById(1L)).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            bookService.deleteBook(1L);
        });

        assertEquals("Book not found with id 1", exception.getMessage());
        verify(bookRepository, times(0)).deleteById(1L);
    }
}
