package com.voteU.election.java.repositories.electiondata;

import com.voteU.election.java.dto.DropdownOptionDTO;
import com.voteU.election.java.entities.electiondata.Elections;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ElectionRepository extends JpaRepository<Elections, Integer> {

    @Query("SELECT new com.voteU.election.java.dto.DropdownOptionDTO(e.electionId, e.name) FROM Elections e ")
    List<DropdownOptionDTO<String>> getElectionNames();


}
