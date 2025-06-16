package com.voteU.election.java.repositories.electiondata;

import com.voteU.election.java.dto.PartyVotesDTO;
import com.voteU.election.java.entities.electiondata.AuthorityPartyVotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthorityPartyVotesRepository extends JpaRepository<AuthorityPartyVotes, Long> {

    // Find all votes for a given election
    List<AuthorityPartyVotes> findByElectionId(String electionId);

    // Find votes for a specific party in an election
    List<AuthorityPartyVotes> findByElectionIdAndPartyId(String electionId, Integer partyId);

    // Custom DTO projection
    @Query("SELECT new com.voteU.election.java.dto.PartyVotesDTO(p.partyId, p.name, apv.votes) " +
            "FROM AuthorityPartyVotes apv " +
            "JOIN Parties p ON apv.partyId = p.partyId AND apv.electionId = p.electionId " +
            "WHERE apv.electionId = :electionId AND apv.authorityId= :authorityId")
    List<PartyVotesDTO> findAuthorityPartyVotes(
            @Param("electionId") String electionId,
            @Param("authorityId") String authorityId);

    @Query("SELECT new com.voteU.election.java.dto.PartyVotesDTO(p.partyId, p.name, apv.votes) " +
            "FROM AuthorityPartyVotes apv " +
            "JOIN Parties p ON apv.partyId = p.partyId AND apv.electionId = p.electionId " +
            "WHERE apv.electionId = :electionId AND apv.authorityId = :authorityId " +
            "ORDER BY apv.votes DESC")
    List<PartyVotesDTO> AuthorityPartyVotesSortedByVotes(
            @Param("electionId") String electionId,
            @Param("authorityId") String authorityId);

    @Query("SELECT new com.voteU.election.java.dto.PartyVotesDTO(p.partyId, p.name, apv.votes) " +
            "FROM AuthorityPartyVotes apv " +
            "JOIN Parties p ON apv.partyId = p.partyId AND apv.electionId = p.electionId " +
            "WHERE apv.electionId = :electionId AND apv.authorityId = :authorityId " +
            "ORDER BY p.name ASC")
    List<PartyVotesDTO> AuthorityPartyVotesSortedByName(
            @Param("electionId") String electionId,
            @Param("authorityId") String authorityId);


}
