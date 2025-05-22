package com.voteU.election.java.reader;

import com.voteU.election.java.model.Election;
import com.voteU.election.java.utils.PathUtils;
import com.voteU.election.java.utils.xml.ElectionProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Processes election data from XML files and provides access to the results.
 */
@Slf4j
@Component
public class ElectionReader {
    private final ElectionProcessor<Election> processor;
    private final ElectionTransformer transformer;

    public ElectionReader() {
        this.transformer = new ElectionTransformer();
        this.processor = new ElectionProcessor<>(transformer);
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
                processor.processResults(electionId, PathUtils.getResourcePath(path));
                log.info("Processed Election " + electionId);
            } catch (Exception e) {
                System.out.println("Could not process Election " + electionId);
                e.printStackTrace();
            }
        }
        System.out.println("All files are processed.\n");
        return transformer.getElectionListDataMap();
    }

    public Election getElection(String electionId) {
        String path = "/EML_bestanden_" + electionId;
        try {
            processor.processResults(electionId, PathUtils.getResourcePath(path));
            log.info("Processed Election {}", electionId);
        } catch (Exception e) {
            log.error("Could not process {}", electionId, e);
        }
        System.out.println("All files are processed.\n");
        return transformer.getElection(electionId);
    }
}
