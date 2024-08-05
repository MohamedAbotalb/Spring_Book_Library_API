package com.mabotalb.library_system_api.service.implementation;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mabotalb.library_system_api.entity.Book;
import com.mabotalb.library_system_api.entity.BorrowingRecord;
import com.mabotalb.library_system_api.service.BookService;
import com.mabotalb.library_system_api.exception.NotFoundException;
import com.mabotalb.library_system_api.repository.BookRepository;
import com.mabotalb.library_system_api.repository.BorrowingRecordRepository;

@Service
public class BookImplementation implements BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    private static final String notFoundBookMessage = "Book not found with id ";

    // Get all books
    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Get a single book using the id
    @Override
    public Book getBook(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new NotFoundException(notFoundBookMessage + id));
    }

    // Add a new book
    @Transactional
    @Override
    public Book addBook(Book bookDetails) {
        return bookRepository.save(bookDetails);
    }

    // Update a single book using the id
    @Transactional
    @Override
    public Book updateBook(Long id, Book bookDetails) {
        return bookRepository.findById(id).map((book) -> {
            book.setTitle(bookDetails.getTitle());
            book.setIsbn(bookDetails.getIsbn());
            book.setAuthor(bookDetails.getAuthor());
            book.setPublicationYear(bookDetails.getPublicationYear());
            return bookRepository.save(book);
        }).orElseThrow(() -> new NotFoundException(notFoundBookMessage + id));
    }

    // Delete a book using the id
    @Transactional
    @Override
    public void deleteBook(Long id) {
        BorrowingRecord activeRecord = borrowingRecordRepository.findByBookId(id);
        if (activeRecord != null)
            throw new RuntimeException("This book can't be deleted as it's already in a borrowing record");

        if (!bookRepository.existsById(id))
            throw new NotFoundException(notFoundBookMessage + id);

        bookRepository.deleteById(id);
    }
}
