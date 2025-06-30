package com.voteU.election.java.repositories;

import com.voteU.election.java.entities.Reports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing database operations related to the Reports entity.
 * Extends JpaRepository to provide basic CRUD operations.
 */
@Repository
public interface ReportsRepository extends JpaRepository<Reports, Integer> {
    
}
