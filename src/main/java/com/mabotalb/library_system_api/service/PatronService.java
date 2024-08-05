package com.mabotalb.library_system_api.service;

import java.util.List;

import com.mabotalb.library_system_api.entity.Patron;

public interface PatronService {
    List<Patron> getAllPatrons();

    Patron getPatron(Long id);

    Patron addPatron(Patron patronDetails);

    Patron updatePatron(Long id, Patron patronDetails);

    void deletePatron(Long id);
}
