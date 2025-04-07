package com.voteU.election.java.services;

import com.voteU.election.java.dto.ContestSummaryDTO;
import com.voteU.election.java.model.Contest;
import com.voteU.election.java.reader.DutchElectionReader;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ElectionService {
    private final DutchElectionReader electionReader;
    private final Map<String, Map<Integer, Contest>> storedElections = new HashMap<>();
    private final Map<String, List<ContestSummaryDTO>> contestSummariesByYear = new HashMap<>(); // ✅ Store summaries per year


    public ElectionService(DutchElectionReader electionReader) {
        this.electionReader = electionReader;
    }


    public boolean readElections() {
        Map<String, Map<Integer, Contest>> elections = electionReader.getAll();
        if (elections.isEmpty()) {
            return false;
        }
        storedElections.putAll(elections);
        return true;
    }

    public boolean readElectionYear(String electionId) {
        if (contestSummariesByYear.containsKey(electionId)) {
            return true; // ✅ If already fetched, don't refetch
        }

        Map<String, Map<Integer, Contest>> elections = storedElections;
        if (elections.isEmpty()) {
            return false;
        }

        Map<Integer, Contest> electionContests = elections.get(electionId);
        if (electionContests != null) {
            List<ContestSummaryDTO> contestSummaries = new ArrayList<>();
            for (Contest contest : electionContests.values()) {
                contestSummaries.add(new ContestSummaryDTO(contest.getName(), contest.getParties().size()));
            }
            contestSummariesByYear.put(electionId, contestSummaries); // ✅ Store summaries per year
        }
        return true;
    }
    /**
     * Retrieves the stored election data (GET equivalent).
     *
     * @return A map containing election results grouped by year.
     */
    public Map<String, Map<Integer, Contest>> getAll() {
        return storedElections;
    }

    public List<ContestSummaryDTO> getElection(String electionId) {
            return contestSummariesByYear.get(electionId);
    }


}
