package com.mabotalb.library_system_api.service.implementation;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.mabotalb.library_system_api.entity.Patron;
import com.mabotalb.library_system_api.entity.BorrowingRecord;
import com.mabotalb.library_system_api.service.PatronService;
import com.mabotalb.library_system_api.exception.NotFoundException;
import com.mabotalb.library_system_api.repository.PatronRepository;
import com.mabotalb.library_system_api.repository.BorrowingRecordRepository;

@Service
public class PatronImplementation implements PatronService {
    @Autowired
    private PatronRepository patronRepository;

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    private static final String notFoundPatronMessage = "Patron not found with id ";

    // Get all patrons
    @Override
    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }

    // Get single patron using the id
    @Override
    public Patron getPatron(Long id) {
        return patronRepository.findById(id).orElseThrow(() -> new NotFoundException(notFoundPatronMessage + id));
    }

    // Add new patron
    @Transactional
    @Override
    public Patron addPatron(Patron patronDetails) {
        return patronRepository.save(patronDetails);
    }

    // Update a patron using the id
    @Transactional
    @Override
    public Patron updatePatron(Long id, Patron patronDetails) {
        return patronRepository.findById(id).map((patron) -> {
            patron.setName(patronDetails.getName());
            patron.setContactInformation(patronDetails.getContactInformation());
            return patronRepository.save(patron);
        }).orElseThrow(() -> new NotFoundException(notFoundPatronMessage + id));
    }

    // Delete a patron using the id
    @Transactional
    @Override
    public void deletePatron(Long id) {
        BorrowingRecord activeRecord = borrowingRecordRepository.findByPatronId(id);
        if (activeRecord != null)
            throw new RuntimeException("This patron can't be deleted as it's already in a borrowing record");

        if (!patronRepository.existsById(id))
            throw new NotFoundException(notFoundPatronMessage + id);

        patronRepository.deleteById(id);
    }
}
