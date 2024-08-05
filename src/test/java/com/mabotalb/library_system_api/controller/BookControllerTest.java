package com.mabotalb.library_system_api.controller;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.mabotalb.library_system_api.entity.Book;
import com.mabotalb.library_system_api.service.BookService;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetAllBooks() throws Exception {
        Book book1 = new Book("9781234567890", "Sample Book 1", "Mohamed Abotalb", 2020);
        Book book2 = new Book("9781234567891", "Sample Book 2", "Mohamed Abotalb", 2021);
        book1.setId(1L);
        book2.setId(2L);

        when(bookService.getAllBooks()).thenReturn(List.of(book1, book2));

        mockMvc.perform(get("/api/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Books fetched successfully!"))
                .andExpect(jsonPath("$.data[0].title").value("Sample Book 1"));
    }

    @Test
    public void testGetBookById() throws Exception {
        Book book = new Book("9781234567890", "Sample Book", "Mohamed Abotalb", 2020);
        book.setId(1L);

        when(bookService.getBook(anyLong())).thenReturn(book);

        mockMvc.perform(get("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Book fetched successfully!"))
                .andExpect(jsonPath("$.data.title").value("Sample Book"));
    }

    @Test
    public void testAddBook() throws Exception {
        Book book = new Book("9781234567890", "New Book", "Mohamed Abotalb", 2020);
        book.setId(1L);

        when(bookService.addBook(any(Book.class))).thenReturn(book);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Book added successfully!"))
                .andExpect(jsonPath("$.data.title").value("New Book"));
    }

    @Test
    public void testUpdateBookById() throws Exception {
        Book book = new Book("9781234567890", "Updated Book", "Mohamed Abotalb", 2020);
        book.setId(1L);

        when(bookService.updateBook(anyLong(), any(Book.class))).thenReturn(book);

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Book updated successfully!"))
                .andExpect(jsonPath("$.data.title").value("Updated Book"));
    }

    @Test
    public void testDeleteBookById() throws Exception {
        mockMvc.perform(delete("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Book deleted successfully!"));
    }
}
