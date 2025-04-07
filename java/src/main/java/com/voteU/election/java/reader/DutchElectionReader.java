package com.voteU.election.java.reader;

import com.voteU.election.java.model.Contest;
import com.voteU.election.java.model.Election;
import com.voteU.election.java.utils.PathUtils;
import com.voteU.election.java.utils.xml.DutchElectionProcessor;
import com.voteU.election.java.dto.ContestSummaryDTO;  // Ensure this DTO is imported
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * Processes election data from XML files and provides access to the results.
 */
@Component
public class DutchElectionReader {
    private final DutchElectionTransformer transformer;
    private final DutchElectionProcessor<Election> electionProcessor;

    // Stores contests for both years.
    Map<String, Map<Integer, Contest>> elections = new HashMap<>();

    public DutchElectionReader() {
        this.transformer = new DutchElectionTransformer();
        this.electionProcessor = new DutchElectionProcessor<>(transformer);
    }

    /**
     * Reads and processes election results for multiple years.
     *
     * @return A map containing election results, organized by election year.
     */
    public Map<String, Map<Integer, Contest>> getAll() {
        String[] electionYears = {"TK2021", "TK2023"};

        for (String electionYear : electionYears) {
            String path = "/EML_bestanden_" + electionYear;
            try {
                // Process election data
                electionProcessor.processResults(electionYear, PathUtils.getResourcePath(path));
                Map<Integer, Contest> contests = transformer.getContests(electionYear);

                if (contests != null && !contests.isEmpty()) {
                    elections.put(electionYear, contests);
                }
            } catch (IOException | XMLStreamException e) {
                System.err.println("Error processing election data for " + electionYear);
                e.printStackTrace();
            }
        }

        return elections;
    }

    /**
     * Fetches the summary for a specific election year (contest name and party count).
     *
     * @param electionId The year of the election (e.g., "TK2021", "TK2023").
     * @return A list of ContestSummaryDTO objects with contest name and party count.
     */
    public List<ContestSummaryDTO> getElectionSummary(String electionId) {
        List<ContestSummaryDTO> contestSummaries = new ArrayList<>();

        Map<Integer, Contest> electionContests = elections.get(electionId);
        if (electionContests != null) {
            for (Contest contest : electionContests.values()) {
                ContestSummaryDTO contestSummary = new ContestSummaryDTO(contest.getName(), contest.getParties().size());
                contestSummaries.add(contestSummary);
            }
        }

        return contestSummaries;
    }
}
