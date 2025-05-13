package com.voteU.election.java.reader;

import com.voteU.election.java.model.Election;
import com.voteU.election.java.utils.PathUtils;
import com.voteU.election.java.utils.xml.DutchElectionProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Processes election data from XML files and provides access to the results.
 */
@Slf4j
@Component
public class DutchElectionReader {
    private final DutchElectionTransformer transformer;
    private final DutchElectionProcessor<Election> electionProcessor;

    public DutchElectionReader() {
        this.transformer = new DutchElectionTransformer();
        this.electionProcessor = new DutchElectionProcessor<>(transformer);
    }

    /**
     * Reads and processes election results for multiple years.
     *
     * @return A map containing election results, organized by election year.
     */
    public Map<String, Election> getAll() {
        String[] electionIds = {"TK2021", "TK2023"};
        for (String electionId : electionIds) {
            String path = "/EML_bestanden_" + electionId;
            try {
                electionProcessor.processResults(electionId, PathUtils.getResourcePath(path));
                log.info("Processed Election " + electionId);
            } catch (Exception e) {
                System.out.println("Could not process Election " + electionId);
                e.printStackTrace();
            }
        }
        System.out.println("All files are processed.\n");
        return transformer.getElections();
    }
}
