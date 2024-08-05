package com.mabotalb.library_system_api.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@Entity
@Table(name = "borrowing_records")
public class BorrowingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @NotNull(message = "Book is required")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "patron_id")
    @NotNull(message = "Patron is required")
    private Patron patron;

    @Column(name = "borrowing_date")
    private LocalDate borrowingDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    public BorrowingRecord(Book book, Patron patron) {
        this.setBook(book);
        this.setPatron(patron);
        this.setBorrowingDate(LocalDate.now());
        this.setReturnDate(null);
    }
}
