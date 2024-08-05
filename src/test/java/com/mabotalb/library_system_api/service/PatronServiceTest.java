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

import com.mabotalb.library_system_api.entity.Patron;
import com.mabotalb.library_system_api.entity.BorrowingRecord;
import com.mabotalb.library_system_api.exception.NotFoundException;
import com.mabotalb.library_system_api.repository.PatronRepository;
import com.mabotalb.library_system_api.repository.BorrowingRecordRepository;

class PatronServiceTest {

    @Mock
    private PatronRepository patronRepository;

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @InjectMocks
    private PatronService patronService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPatrons() {
        Patron patron1 = new Patron( "Mohamed Abotalb", "mohamedabotalb@example.com");
        patron1.setId(1L);

        Patron patron2 = new Patron("Ahmed Abotalb", "ahmedabotalb@example.com");
        patron2.setId(2L);

        List<Patron> patrons = Arrays.asList(patron1, patron2);
        when(patronRepository.findAll()).thenReturn(patrons);

        List<Patron> result = patronService.getAllPatrons();

        assertEquals(2, result.size());
        verify(patronRepository, times(1)).findAll();
    }

    @Test
    void testGetPatron_Success() {
        Patron patron = new Patron("Mohamed Abotalb", "mohamedabotalb@example.com");
        patron.setId(1L);

        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));

        Patron result = patronService.getPatron(1L);

        assertNotNull(result);
        assertEquals("Mohamed Abotalb", result.getName());
        verify(patronRepository, times(1)).findById(1L);
    }

    @Test
    void testGetPatron_NotFound() {
        when(patronRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            patronService.getPatron(1L);
        });

        assertEquals("Patron not found with id 1", exception.getMessage());
        verify(patronRepository, times(1)).findById(1L);
    }

    @Test
    void testAddPatron() {
        Patron patron = new Patron("Mohamed Abotalb", "mohamedabotalb@example.com");
        patron.setId(1L);

        when(patronRepository.save(any(Patron.class))).thenReturn(patron);

        Patron result = patronService.addPatron(patron);

        assertNotNull(result);
        assertEquals("Mohamed Abotalb", result.getName());
        verify(patronRepository, times(1)).save(patron);
    }

    @Test
    void testUpdatePatron_Success() {
        Patron existingPatron = new Patron("Old Name", "old.email@example.com");
        existingPatron.setId(1L);

        Patron updatedPatronDetails = new Patron("New Name", "new.email@example.com");
        updatedPatronDetails.setId(1L);

        when(patronRepository.findById(1L)).thenReturn(Optional.of(existingPatron));
        when(patronRepository.save(any(Patron.class))).thenReturn(updatedPatronDetails);

        Patron result = patronService.updatePatron(1L, updatedPatronDetails);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
        verify(patronRepository, times(1)).findById(1L);
        verify(patronRepository, times(1)).save(existingPatron);
    }

    @Test
    void testUpdatePatron_NotFound() {
        Patron updatedPatronDetails = new Patron("New Name", "new.email@example.com");
        updatedPatronDetails.setId(1L);

        when(patronRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            patronService.updatePatron(1L, updatedPatronDetails);
        });

        assertEquals("Patron not found with id 1", exception.getMessage());
        verify(patronRepository, times(1)).findById(1L);
    }

    @Test
    void testDeletePatron_Success() {
        when(borrowingRecordRepository.findByPatronId(1L)).thenReturn(null);
        when(patronRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> {
            patronService.deletePatron(1L);
        });

        verify(patronRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeletePatron_InBorrowingRecord() {
        BorrowingRecord activeRecord = new BorrowingRecord();
        when(borrowingRecordRepository.findByPatronId(1L)).thenReturn(activeRecord);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            patronService.deletePatron(1L);
        });

        assertEquals("This patron can't be deleted as it's already in a borrowing record", exception.getMessage());
        verify(patronRepository, times(0)).deleteById(1L);
    }

    @Test
    void testDeletePatron_NotFound() {
        when(borrowingRecordRepository.findByPatronId(1L)).thenReturn(null);
        when(patronRepository.existsById(1L)).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            patronService.deletePatron(1L);
        });

        assertEquals("Patron not found with id 1", exception.getMessage());
        verify(patronRepository, times(0)).deleteById(1L);
    }
}
