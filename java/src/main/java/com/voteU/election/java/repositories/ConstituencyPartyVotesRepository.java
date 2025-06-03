package com.voteU.election.java.repositories;

import com.voteU.election.java.database.DBTables.constituencies.ConstituencyPartyVotes;
import com.voteU.election.java.database.responseDTO.PartyVotesDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConstituencyPartyVotesRepository extends JpaRepository<ConstituencyPartyVotes, Integer> {
    @Query("SELECT new com.voteU.election.java.database.responseDTO.PartyVotesDTO(p.partyId, p.name, cpv.votes) " +
            "FROM ConstituencyPartyVotes cpv " +
            "JOIN Parties p ON cpv.partyId = p.partyId AND cpv.electionId = p.electionId " +
            "WHERE cpv.electionId = :electionId AND cpv.constituencyId = :constituencyId")
    List<PartyVotesDTO> findConstituencyPartyVotesByElectionId(@Param("electionId") String electionId, @Param("constituencyId") Integer constituencyId);
}
