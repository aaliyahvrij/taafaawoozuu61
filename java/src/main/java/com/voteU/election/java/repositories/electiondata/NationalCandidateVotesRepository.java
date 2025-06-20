package com.voteU.election.java.repositories.electiondata;

import com.voteU.election.java.dto.CandidateVotesDTO;
import com.voteU.election.java.entities.electiondata.NationalCandidateVotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NationalCandidateVotesRepository extends JpaRepository<NationalCandidateVotes, Long> {

    @Query("SELECT new com.voteU.election.java.dto.CandidateVotesDTO(c.firstName, c.lastName, ncv.votes) " +
            "FROM NationalCandidateVotes ncv " +
            "JOIN Candidates c ON ncv.candidateId = c.candidateId AND ncv.partyId = c.partyId AND ncv.electionId = c.electionId " +
            "WHERE ncv.electionId = :electionId AND ncv.partyId = :partyId")
    List<CandidateVotesDTO> findAllCandidateVotesByParty(
            @Param("electionId") String electionId,
            @Param("partyId") Integer partyId
    );


}
