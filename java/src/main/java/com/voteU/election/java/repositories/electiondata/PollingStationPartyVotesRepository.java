package com.voteU.election.java.repositories.electiondata;

import com.voteU.election.java.dto.PartyVotesDTO;
import com.voteU.election.java.entities.electiondata.PollingStationPartyVotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PollingStationPartyVotesRepository extends JpaRepository<PollingStationPartyVotes, Long> {

    @Query("SELECT new com.voteU.election.java.dto.PartyVotesDTO(p.partyId, p.name, ppv.votes) " +
            "FROM PollingStationPartyVotes ppv " +
            "JOIN Parties p ON ppv.partyId = p.partyId AND ppv.electionId = p.electionId " +
            "WHERE ppv.electionId = :electionId AND ppv.pollingStationId = :pollingStationId ")
    List<PartyVotesDTO> findPollingStationPartyVotesByElectionAndPollingStationId(
            @Param("electionId") String electionId,
            @Param("pollingStationId") String pollingStationId);
}
