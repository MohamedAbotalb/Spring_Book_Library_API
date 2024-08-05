package com.mabotalb.library_system_api.repository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mabotalb.library_system_api.entity.Book;
import com.mabotalb.library_system_api.entity.BorrowingRecord;
import com.mabotalb.library_system_api.entity.Patron;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class BorrowingRecordRepositoryTest {

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PatronRepository patronRepository;

    private Book book;
    private Patron patron;

    @BeforeEach
    void setUp() {
        book = new Book("9781234567890", "Test Book", "Mohamed Abotalb", 2020);
        book.setId(1L);
        book = bookRepository.save(book);

        patron = new Patron("Mohamed Abotalb", "mohamedabotalb@example.com");
        patron.setId(1L);
        patron = patronRepository.save(patron);
    }

    @Test
    void testFindByBookIdAndPatronId() {
        BorrowingRecord record = new BorrowingRecord(book, patron);
        record.setId(1L);
        borrowingRecordRepository.save(record);

        BorrowingRecord foundRecord = borrowingRecordRepository.findByBookIdAndPatronId(book.getId(), patron.getId());
        assertNotNull(foundRecord);
        assertEquals(book.getId(), foundRecord.getBook().getId());
        assertEquals(patron.getId(), foundRecord.getPatron().getId());
    }

    @Test
    void testFindByBookId() {
        BorrowingRecord record = new BorrowingRecord(book, patron);
        record.setId(1L);
        borrowingRecordRepository.save(record);

        BorrowingRecord foundRecord = borrowingRecordRepository.findByBookId(book.getId());
        assertNotNull(foundRecord);
        assertEquals(book.getId(), foundRecord.getBook().getId());
    }

    @Test
    void testFindByPatronId() {
        BorrowingRecord record = new BorrowingRecord(book, patron);
        record.setId(1L);
        borrowingRecordRepository.save(record);

        BorrowingRecord foundRecord = borrowingRecordRepository.findByPatronId(patron.getId());
        assertNotNull(foundRecord);
        assertEquals(patron.getId(), foundRecord.getPatron().getId());
    }
}

