package com.voteU.election.java.repositories.electiondata;

import com.voteU.election.java.dto.DropdownOptionDTO;
import com.voteU.election.java.entities.electiondata.Constituencies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConstituencyRepository extends JpaRepository<Constituencies, Integer> {

    @Query("SELECT new com.voteU.election.java.dto.DropdownOptionDTO(c.constituencyId, c.name) FROM Constituencies c where c.electionId = :electionId")
    List<DropdownOptionDTO<Integer>> getAllByElectionId(@Param("electionId") String electionId);

    @Query("SELECT new com.voteU.election.java.dto.DropdownOptionDTO(c.constituencyId, c.name) " +
            "FROM Constituencies c " +
            "WHERE c.electionId = :electionId AND c.provinceId = :provinceId")
    List<DropdownOptionDTO<Integer>> getAllByProvinceId(
            @Param("electionId") String electionId,
            @Param("provinceId") Integer provinceId
    );


}
