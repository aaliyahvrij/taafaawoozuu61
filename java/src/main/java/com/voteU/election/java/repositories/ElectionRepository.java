package com.voteU.election.java.repositories;

import com.voteU.election.java.model.Election;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ElectionRepository extends JpaRepository<Election, String> {
}
