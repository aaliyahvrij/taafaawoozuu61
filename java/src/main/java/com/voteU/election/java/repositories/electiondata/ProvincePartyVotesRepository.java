package com.voteU.election.java.repositories.electiondata;

import com.voteU.election.java.dto.PartyVotesDTO;
import com.voteU.election.java.entities.electiondata.ProvincePartyVotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProvincePartyVotesRepository extends JpaRepository<ProvincePartyVotes, Long> {

    // Find all votes for a given election
    List<ProvincePartyVotes> findByElectionId(String electionId);

    // Find votes for a specific party in an election
    List<ProvincePartyVotes> findByElectionIdAndPartyId(String electionId, Integer partyId);

    // Custom DTO projection
    @Query("SELECT new com.voteU.election.java.dto.PartyVotesDTO(p.partyId, p.name, ppv.votes) " +
            "FROM ProvincePartyVotes ppv " +
            "JOIN Parties p ON ppv.partyId = p.partyId AND ppv.electionId = p.electionId " +
            "WHERE ppv.electionId = :electionId AND ppv.provinceId = :provinceId")
    List<PartyVotesDTO> findProvincePartyVotes(
            @Param("electionId") String electionId,
            @Param("provinceId") Integer provinceId);

    @Query("SELECT new com.voteU.election.java.dto.PartyVotesDTO(p.partyId, p.name, ppv.votes) " +
            "FROM ProvincePartyVotes ppv " +
            "JOIN Parties p ON ppv.partyId = p.partyId AND ppv.electionId = p.electionId " +
            "WHERE ppv.electionId = :electionId AND ppv.provinceId = :provinceId " +
            "ORDER BY ppv.votes DESC")
    List<PartyVotesDTO> findProvincePartyVotesSortedByVotes(
            @Param("electionId") String electionId,
            @Param("provinceId") Integer provinceId);

    @Query("SELECT new com.voteU.election.java.dto.PartyVotesDTO(p.partyId, p.name, ppv.votes) " +
            "FROM ProvincePartyVotes ppv " +
            "JOIN Parties p ON ppv.partyId = p.partyId AND ppv.electionId = p.electionId " +
            "WHERE ppv.electionId = :electionId AND ppv.provinceId = :provinceId " +
            "ORDER BY p.name DESC")
    List<PartyVotesDTO> findProvincePartyVotesSortedByName(
            @Param("electionId") String electionId,
            @Param("provinceId") Integer provinceId);


}
