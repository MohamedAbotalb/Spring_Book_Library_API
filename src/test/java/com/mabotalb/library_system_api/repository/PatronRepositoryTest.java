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

import com.mabotalb.library_system_api.entity.Patron;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class PatronRepositoryTest {
    @Autowired
    private PatronRepository patronRepository;

    private Patron patron;

    @BeforeEach
    void setUp() {
        patron = new Patron("Mohamed Abotalb", "mohamedabotalb@example.com");
        patron.setId(1L);
    }

    @Test
    void testSavePatron() {
        Patron savedPatron = patronRepository.save(patron);
        assertNotNull(savedPatron);
        assertEquals(patron.getName(), savedPatron.getName());
    }

    @Test
    void testFindById() {
        Patron savedPatron = patronRepository.save(patron);
        Optional<Patron> foundPatron = patronRepository.findById(savedPatron.getId());
        assertTrue(foundPatron.isPresent());
        assertEquals(patron.getName(), foundPatron.get().getName());
    }

    @Test
    void testFindAll() {
        patronRepository.save(patron);
        List<Patron> patrons = patronRepository.findAll();
        assertFalse(patrons.isEmpty());
    }

    @Test
    void testDeleteById() {
        Patron savedPatron = patronRepository.save(patron);
        patronRepository.deleteById(savedPatron.getId());
        Optional<Patron> foundPatron = patronRepository.findById(savedPatron.getId());
        assertFalse(foundPatron.isPresent());
    }

    @Test
    void testUpdateById() {
        Patron savedPatron = patronRepository.save(patron);

        savedPatron.setName("Updated Test Patron");
        savedPatron.setContactInformation("updated@example.com");

        Patron updatedPatron = patronRepository.save(savedPatron);
        Optional<Patron> foundPatron = patronRepository.findById(updatedPatron.getId());

        assertTrue(foundPatron.isPresent());
        assertEquals("Updated Test Patron", foundPatron.get().getName());
        assertEquals("updated@example.com", foundPatron.get().getContactInformation());
    }
}
