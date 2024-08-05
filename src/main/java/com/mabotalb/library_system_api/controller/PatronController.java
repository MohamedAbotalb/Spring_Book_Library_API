package com.mabotalb.library_system_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.Positive;
import jakarta.validation.Valid;

import com.mabotalb.library_system_api.entity.Patron;
import com.mabotalb.library_system_api.service.PatronService;
import com.mabotalb.library_system_api.response.ApiResponse;

@RestController
@RequestMapping("/api/patrons")
@Validated
public class PatronController {
    @Autowired
    private PatronService patronService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Patron>>> getAllPatrons() {
        List<Patron> patrons = patronService.getAllPatrons();
        ApiResponse<List<Patron>> response = new ApiResponse<>("Patrons fetched successfully!", patrons);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Patron>> getPatronById(@PathVariable @Positive Long id) {
        Patron patron = patronService.getPatron(id);
        ApiResponse<Patron> response = new ApiResponse<>("Patron fetched successfully!", patron);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Patron>> AddPatron(@Valid @RequestBody Patron patronDetails) {
        Patron savedPatron = patronService.addPatron(patronDetails);
        ApiResponse<Patron> response = new ApiResponse<>("Patron added successfully!", savedPatron);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Patron>> updatePatronById(
            @PathVariable @Positive Long id,
            @Valid @RequestBody Patron patronDetails) {
        Patron updatedPatron = patronService.updatePatron(id, patronDetails);
        ApiResponse<Patron> response = new ApiResponse<>("Patron updated successfully!", updatedPatron);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatronById(@PathVariable @Positive Long id) {
        patronService.deletePatron(id);
        return ResponseEntity.ok("Patron deleted successfully!");
    }
}
