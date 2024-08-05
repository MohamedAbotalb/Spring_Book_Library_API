package com.mabotalb.library_system_api.service.implementation;

import com.mabotalb.library_system_api.entity.Patron;
import com.mabotalb.library_system_api.exception.NotFoundException;
import com.mabotalb.library_system_api.repository.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatronImplementation {
    @Autowired
    private PatronRepository patronRepository;

    private final String notFoundPatronMessage = "Patron not found with id ";

    // Get all patrons
    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }

    // Get single patron using the id
    public Optional<Patron> getPatron(long id) {
        return Optional.ofNullable(patronRepository.findById(id).orElseThrow(() -> new NotFoundException(notFoundPatronMessage + id)));
    }

    // Add new patron
    public Patron addPatron(Patron patronDetails) {
        return patronRepository.save(patronDetails);
    }

    // Update a patron using the id
    public Optional<Patron> updatePatron(Long id, Patron patronDetails) {
        return Optional.of(patronRepository.findById(id).map((patron) -> {
            patron.setName(patronDetails.getName());
            patron.setContactInformation(patronDetails.getContactInformation());
            return patronRepository.save(patron);
        })).orElseThrow(() -> new NotFoundException(notFoundPatronMessage + id));
    }

    // Delete a patron using the id
    public void deletePatron(Long id) {
        if (!patronRepository.existsById(id))
            throw new NotFoundException(notFoundPatronMessage + id);
         patronRepository.deleteById(id);
    }
}
