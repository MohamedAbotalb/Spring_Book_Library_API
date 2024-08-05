package com.mabotalb.library_system_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.Positive;
import jakarta.validation.Valid;

import com.mabotalb.library_system_api.entity.Book;
import com.mabotalb.library_system_api.service.BookService;
import com.mabotalb.library_system_api.response.ApiResponse;

@RestController
@RequestMapping("/api/books")
@Validated
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Book>>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        ApiResponse<List<Book>> response = new ApiResponse<>("Books fetched successfully!", books);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Book>> getBookById(@PathVariable @Positive Long id) {
        Book book = bookService.getBook(id);
        ApiResponse<Book> response = new ApiResponse<>("Book fetched successfully!", book);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Book>> AddBook(@Valid @RequestBody Book bookDetails) {
        Book savedBook = bookService.addBook(bookDetails);
        ApiResponse<Book> response = new ApiResponse<>("Book added successfully!", savedBook);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Book>> updateBookById(
            @PathVariable @Positive Long id,
            @Valid @RequestBody Book bookDetails) {
        Book updatedBook = bookService.updateBook(id, bookDetails);
        ApiResponse<Book> response = new ApiResponse<>("Book updated successfully!", updatedBook);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBookById(@PathVariable @Positive Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok("Book deleted successfully!");
    }
}
