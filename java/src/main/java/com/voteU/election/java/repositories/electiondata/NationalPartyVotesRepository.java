package com.voteU.election.java.repositories.electiondata;

import com.voteU.election.java.dto.PartyVotesDTO;
import com.voteU.election.java.entities.electiondata.NationalPartyVotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NationalPartyVotesRepository extends JpaRepository<NationalPartyVotes, Long> {

    // Find all votes for a given election
    List<NationalPartyVotes> findByElectionId(String electionId);

    // Find votes for a specific party in an election
    List<NationalPartyVotes> findByElectionIdAndPartyId(String electionId, Integer partyId);

    // Custom DTO projection
    @Query("SELECT new com.voteU.election.java.dto.PartyVotesDTO(p.partyId, p.name, npv.votes) " +
            "FROM NationalPartyVotes npv " +
            "JOIN Parties p ON npv.partyId = p.partyId AND npv.electionId = p.electionId " +
            "WHERE npv.electionId = :electionId")
    List<PartyVotesDTO> findNationalPartyVotesByElectionId(@Param("electionId") String electionId);
}
