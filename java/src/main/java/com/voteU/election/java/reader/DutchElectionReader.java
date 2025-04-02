package com.voteU.election.java.reader;

import com.voteU.election.java.model.Candidate;
import com.voteU.election.java.model.Contest;
import com.voteU.election.java.model.Election;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.utils.PathUtils;
import com.voteU.election.java.utils.xml.DutchElectionProcessor;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Processes election data from XML files and provides access to the results.
 */
@Component
public class DutchElectionReader {
    private final com.voteU.election.java.reader.DutchElectionTransformer transformer;
    private final DutchElectionProcessor<Election> electionProcessor;

    public DutchElectionReader() {
        this.transformer = new com.voteU.election.java.reader.DutchElectionTransformer();
        this.electionProcessor = new DutchElectionProcessor<>(transformer);
    }

    /**
     * Reads and processes election results for multiple years.
     *
     * @return A map containing election results, organized by election year.
     */
    public Map<String, Map<Integer, Contest>> getElections() {
        String[] electionYears = {"TK2021", "TK2023"};
        Map<String, Map<Integer, Contest>> elections = new HashMap<>();

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
}
