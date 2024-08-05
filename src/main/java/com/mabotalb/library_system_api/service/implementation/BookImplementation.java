package com.mabotalb.library_system_api.service.implementation;

import com.mabotalb.library_system_api.entity.Book;
import com.mabotalb.library_system_api.exception.NotFoundException;
import com.mabotalb.library_system_api.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookImplementation {
    @Autowired
    private BookRepository bookRepository;

    private final String notFoundBookMessage = "Book not found with id ";

    // Get all books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Get a single book using the id
    public Optional<Book> getBook(Long id) {
        return Optional.ofNullable(bookRepository.findById(id).orElseThrow(() -> new NotFoundException(notFoundBookMessage + id)));
    }

    // Add a new book
    public Book addBook(Book bookDetails) {
        return bookRepository.save(bookDetails);
    }

    // Update a single book using the id
    public Optional<Book> updateBook(Long id, Book bookDetails) {
        return Optional.ofNullable(bookRepository.findById(id).map((book) -> {
            book.setTitle(bookDetails.getTitle());
            book.setIsbn(bookDetails.getIsbn());
            book.setAuthor(bookDetails.getAuthor());
            book.setPublicationYear(bookDetails.getPublicationYear());
            return bookRepository.save(book);
        }).orElseThrow(() -> new NotFoundException(notFoundBookMessage + id)));
    }

    // Delete a book using the id
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id))
            throw new NotFoundException(notFoundBookMessage + id);
        bookRepository.deleteById(id);
    }
}
