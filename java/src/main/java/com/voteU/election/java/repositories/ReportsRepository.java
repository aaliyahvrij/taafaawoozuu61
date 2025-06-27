package com.voteU.election.java.repositories;

import com.voteU.election.java.entities.Reports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportsRepository extends JpaRepository<Reports, Integer> {
    
}
