package com.mabotalb.library_system_api.repository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mabotalb.library_system_api.entity.Book;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book("9781234567890", "Test Book", "Mohamed Abotalb", 2020);
        book.setId(1L);
    }

    @Test
    void testSaveBook() {
        Book savedBook = bookRepository.save(book);
        assertNotNull(savedBook);
        assertEquals(book.getTitle(), savedBook.getTitle());
    }

    @Test
    void testFindById() {
        Book savedBook = bookRepository.save(book);
        Optional<Book> foundBook = bookRepository.findById(savedBook.getId());
        assertTrue(foundBook.isPresent());
        assertEquals(book.getTitle(), foundBook.get().getTitle());
    }

    @Test
    void testFindAll() {
        bookRepository.save(book);
        List<Book> books = bookRepository.findAll();
        assertFalse(books.isEmpty());
    }

    @Test
    void testUpdateById() {
        Book savedBook = bookRepository.save(book);

        savedBook.setTitle("Updated Test Book");
        savedBook.setIsbn("9876543215878");
        savedBook.setAuthor("Updated Test Author");
        savedBook.setPublicationYear(2022);

        Book updatedBook = bookRepository.save(savedBook);
        Optional<Book> foundBook = bookRepository.findById(updatedBook.getId());

        assertTrue(foundBook.isPresent());
        assertEquals("Updated Test Book", foundBook.get().getTitle());
        assertEquals("9876543215878", foundBook.get().getIsbn());
        assertEquals("Updated Test Author", foundBook.get().getAuthor());
        assertEquals(2022, foundBook.get().getPublicationYear());
    }

    @Test
    void testDeleteById() {
        Book savedBook = bookRepository.save(book);
        bookRepository.deleteById(savedBook.getId());
        Optional<Book> foundBook = bookRepository.findById(savedBook.getId());
        assertFalse(foundBook.isPresent());
    }
}
