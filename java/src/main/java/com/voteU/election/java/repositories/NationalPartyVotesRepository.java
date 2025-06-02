package com.voteU.election.java.repositories;

import com.voteU.election.java.database.responseDTO.PartyVotesDTO;
import com.voteU.election.java.database.DBTables.national.NationalPartyVotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NationalPartyVotesRepository extends JpaRepository<NationalPartyVotes, NationalPartyVotes.NationalPartyVotesId>{

    // Find all votes for a given election
    List<NationalPartyVotes> findByElectionId(String electionId);

    // Find votes for a specific party in an election
    List<NationalPartyVotes> findByElectionIdAndPartyId(String electionId, String partyId);

    @Query("SELECT new com.voteU.election.java.database.responseDTO.PartyVotesDTO(p.key.id, p.name, npv.votes) " +
            "FROM NationalPartyVotes npv JOIN npv.party p " +
            "WHERE npv.election.id = :electionId")
    List<PartyVotesDTO> findPartyVotesByElection(@Param("electionId") String electionId);
}
