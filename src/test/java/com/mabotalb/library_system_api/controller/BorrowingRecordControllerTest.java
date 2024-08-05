package com.mabotalb.library_system_api.controller;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import com.mabotalb.library_system_api.entity.Patron;
import com.mabotalb.library_system_api.entity.BorrowingRecord;
import com.mabotalb.library_system_api.service.BorrowingRecordService;

@WebMvcTest(BorrowingRecordController.class)
public class BorrowingRecordControllerTest {

    @Mock
    private BorrowingRecordService borrowingRecordService;

    @InjectMocks
    private BorrowingRecordController borrowingRecordController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(borrowingRecordController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testBorrowBook() throws Exception {
        Book book = new Book("9781234567890", "Test Book", "Mohamed Abotalb", 2020);
        book.setId(1L);

        Patron patron = new Patron("Ahmed Abotalb", "ahmedabotalb@example.com");
        patron.setId(1L);

        BorrowingRecord record = new BorrowingRecord(book, patron);
        record.setId(1L);

        when(borrowingRecordService.borrowBook(anyLong(), anyLong())).thenReturn(record);

        mockMvc.perform(post("/api/borrow/1/patron/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Book is borrowed successfully!"))
                .andExpect(jsonPath("$.data.book.title").value("Test Book"))
                .andExpect(jsonPath("$.data.patron.name").value("Ahmed Abotalb"));
    }

    @Test
    public void testReturnBook() throws Exception {
        Book book = new Book("9781234567890", "Test Book", "Mohamed Abotalb", 2020);
        book.setId(1L);

        Patron patron = new Patron( "Ahmed Abotalb", "ahmedabotalb@example.com");
        patron.setId(1L);

        BorrowingRecord record = new BorrowingRecord(book, patron);
        record.setId(1L);
        record.setBorrowingDate(LocalDate.now().minusDays(5));
        record.setReturnDate(LocalDate.now());

        when(borrowingRecordService.returnBook(anyLong(), anyLong())).thenReturn(record);

        mockMvc.perform(put("/api/return/1/patron/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Book is returned successfully!"))
                .andExpect(jsonPath("$.data.book.title").value("Test Book"))
                .andExpect(jsonPath("$.data.patron.name").value("Ahmed Abotalb"))
                .andExpect(jsonPath("$.data.returnDate").value(LocalDate.now().toString()));
    }
}
