package com.mabotalb.library_system_api.service;

import java.util.List;

import com.mabotalb.library_system_api.entity.Book;

public interface BookService {
    List<Book> getAllBooks();

    Book getBook(Long id);

    Book addBook(Book bookDetails);

    Book updateBook(Long id, Book bookDetails);

    void deleteBook(Long id);
}
