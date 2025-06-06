package com.voteU.election.java.repositories.electiondata;

import com.voteU.election.java.dto.DropdownOptionDTO;
import com.voteU.election.java.entities.electiondata.Provinces;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProvinceRepository extends JpaRepository<Provinces, Integer> {

    @Query("SELECT new com.voteU.election.java.dto.DropdownOptionDTO(c.provinceId, c.name) FROM Provinces c where c.electionId = :electionId")
    List<DropdownOptionDTO<Integer>> getProvincesByElectionId(@Param("electionId") String electionId);
}
