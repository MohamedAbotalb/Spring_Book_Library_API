package com.mabotalb.library_system_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mabotalb.library_system_api.entity.Patron;

@Repository
public interface PatronRepository extends JpaRepository<Patron, Long> {}
