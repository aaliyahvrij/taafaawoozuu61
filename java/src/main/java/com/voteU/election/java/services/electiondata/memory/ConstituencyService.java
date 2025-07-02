package com.voteU.election.java.services.electiondata.memory;
import com.voteU.election.java.dtoCompact.CompactConstituency;
import com.voteU.election.java.model.Constituency;
import com.voteU.election.java.model.Election;
import com.voteU.election.java.model.Party;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Service class responsible for handling operations related to constituencies.
 * <p>
 * It fetches constituency data from the election service and provides methods
 * to access detailed and compact views of constituencies and their related parties.
 * </p>
 */
@Service
public class ConstituencyService {

    private final ElectionService electionService;

    /**
     * Constructor that injects the ElectionService dependency.
     *
     * @param electionService the election service to retrieve election data
     */
    public ConstituencyService(ElectionService electionService) {
        this.electionService = electionService;
    }

    /**
     * Retrieves all constituencies for a given election year.
     *
     * @param year the election year as a string
     * @return a map of constituency IDs to Constituency objects, or null if not found
     */
    public Map<Integer, Constituency> getConstituenciesByYear(String year) {
        Election election = electionService.getElection(year);
        if (election == null){
            return null;
        }
        Map<Integer, Constituency> constituencies = election.getConstituencies();
        if (constituencies == null || constituencies.isEmpty()) {
            System.out.println("No constituencies found for election year: " + year);
            return null;
        }
        return constituencies;
    }

    /**
     * Retrieves a compact representation of all constituencies for a given election year.
     *
     * @param year the election year as a string
     * @return a map of constituency IDs to CompactConstituency DTOs, or null if not found
     */
    public Map<Integer, CompactConstituency> getConstituenciesByYearCompact(String year) {
        Election election = electionService.getElection(year);
        if (election == null){
            return null;
        }
        Map<Integer, Constituency> constituencies = election.getConstituencies();
        if (constituencies == null || constituencies.isEmpty()) {
            System.out.println("No constituencies found for election year: " + year);
            return null;
        }
        Map<Integer, CompactConstituency> compactConstituencyMap = new HashMap<>();
        for (Constituency constituency : constituencies.values()) {
            CompactConstituency compactConstituency = new CompactConstituency(constituency.getId(), constituency.getName());
            compactConstituencyMap.put(compactConstituency.getId(), compactConstituency);
        }
        return compactConstituencyMap;
    }

    /**
     * Retrieves a specific constituency by its ID for a given election year.
     *
     * @param year the election year as a string
     * @param constituencyId the ID of the constituency
     * @return the Constituency object, or null if not found
     */
    public Constituency getConstituencyById(String year, int constituencyId) {
        Map<Integer, Constituency> constituencies = getConstituenciesByYear(year);
        if (constituencies == null){
            return null;
        }
        return constituencies.get(constituencyId);
    }

    /**
     * Retrieves all parties associated with a specific constituency for a given election year.
     *
     * @param year the election year as a string
     * @param constituencyId the ID of the constituency
     * @return a map of party IDs to Party objects, or null if not found
     */
    public Map<Integer, Party> getPartiesByConstituencyId(String year, int constituencyId) {
        Constituency constituency = getConstituencyById(year, constituencyId);
        if (constituency == null){
            return null;
        }
        return constituency.getParties();
    }
}
