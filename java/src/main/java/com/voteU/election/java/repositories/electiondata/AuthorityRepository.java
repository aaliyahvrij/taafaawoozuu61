package com.voteU.election.java.repositories.electiondata;

import com.voteU.election.java.dto.DropdownOptionDTO;
import com.voteU.election.java.entities.electiondata.Authorities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authorities, Long> {

    @Query("SELECT new com.voteU.election.java.dto.DropdownOptionDTO(a.authorityId, a.name) " +
            "FROM Authorities a " +
            "WHERE a.electionId = :electionId AND a.constituencyId = :constituencyId")
    List<DropdownOptionDTO<String>> getAllByConstituencyId(
            @Param("electionId") String electionId,
            @Param("constituencyId") Integer constituencyId
    );

}
