package com.voteU.election.java.repositories.electiondata;

import com.voteU.election.java.dto.PartyVotesDTO;
import com.voteU.election.java.entities.electiondata.ConstituencyPartyVotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConstituencyPartyVotesRepository extends JpaRepository<ConstituencyPartyVotes, Long> {

    // Find all votes for a given election
    List<ConstituencyPartyVotes> findByElectionId(String electionId);

    // Find votes for a specific party in an election
    List<ConstituencyPartyVotes> findByElectionIdAndPartyId(String electionId, Integer partyId);

    // Custom DTO projection
    @Query("SELECT new com.voteU.election.java.dto.PartyVotesDTO(p.partyId, p.name, cpv.votes) " +
            "FROM ConstituencyPartyVotes cpv " +
            "JOIN Parties p ON cpv.partyId = p.partyId AND cpv.electionId = p.electionId " +
            "WHERE cpv.electionId = :electionId AND cpv.constituencyId = :constituencyId")
    List<PartyVotesDTO> findConstituencyPartyVotes(
            @Param("electionId") String electionId,
            @Param("constituencyId") Integer constituencyId);

    @Query("SELECT new com.voteU.election.java.dto.PartyVotesDTO(p.partyId, p.name, cpv.votes) " +
            "FROM ConstituencyPartyVotes cpv " +
            "JOIN Parties p ON cpv.partyId = p.partyId AND cpv.electionId = p.electionId " +
            "WHERE cpv.electionId = :electionId AND cpv.constituencyId = :constituencyId " +
            "ORDER BY cpv.votes DESC")
    List<PartyVotesDTO> findConstituencyPartyVotesSortedByVotes(
            @Param("electionId") String electionId,
            @Param("constituencyId") Integer constituencyId);

    @Query("SELECT new com.voteU.election.java.dto.PartyVotesDTO(p.partyId, p.name, cpv.votes) " +
            "FROM ConstituencyPartyVotes cpv " +
            "JOIN Parties p ON cpv.partyId = p.partyId AND cpv.electionId = p.electionId " +
            "WHERE cpv.electionId = :electionId AND cpv.constituencyId = :constituencyId " +
            "ORDER BY p.name ASC")
    List<PartyVotesDTO> findConstituencyPartyVotesSortedByName(
            @Param("electionId") String electionId,
            @Param("constituencyId") Integer constituencyId);





    List<ConstituencyPartyVotes> constituencyId(int constituencyId);
}
