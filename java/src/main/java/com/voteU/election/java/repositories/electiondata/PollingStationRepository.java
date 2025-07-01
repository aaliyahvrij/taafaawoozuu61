package com.voteU.election.java.repositories.electiondata;

import com.voteU.election.java.entities.electiondata.PollingStations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PollingStationRepository extends JpaRepository<PollingStations, String> {

     List<PollingStations> findByZipcode(@Param("zipcode") String zipcode);

     Page<PollingStations> findAll(Pageable pageable);
}
