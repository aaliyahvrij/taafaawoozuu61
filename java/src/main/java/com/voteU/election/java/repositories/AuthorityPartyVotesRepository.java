package com.voteU.election.java.repositories;

import com.voteU.election.java.database.responseDTO.PartyVotesDTO;
import com.voteU.election.java.database.DBTables.authorities.AuthorityPartyVotes;
import com.voteU.election.java.database.DBTables.authorities.AuthorityPartyVotesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthorityPartyVotesRepository extends JpaRepository<AuthorityPartyVotes, AuthorityPartyVotesId> {

    List<AuthorityPartyVotes> findByElection_Id(String electionId);

    List<AuthorityPartyVotes> findByAuthority_IdAndElection_Id(String authorityId, String electionId);

    List<AuthorityPartyVotes> findByAuthority_IdAndElection_IdAndParty_Id(String authorityId, String electionId, int partyId);

    @Query("""
    SELECT new com.voteU.election.java.CompactDTO.PartyVotesDTO(party.id, party.name, apv.votes)
    FROM AuthorityPartyVotes apv
    JOIN apv.party party
    JOIN apv.authority authority
    JOIN apv.election election
    WHERE authority.id = :authorityId AND election.id = :electionId
""")
    List<PartyVotesDTO> findPartyVotesByAuthorityExplicit(@Param("authorityId") String authorityId,
                                                          @Param("electionId") String electionId);
}
