package com.voteU.election.java.utils.xml;

import com.voteU.election.java.models.Election;
import com.voteU.election.java.utils.PathUtils;
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
     * Reads and processes all the data of all the elections.
     *
     * @return A map containing election results, organized by election year.
     */
    public Map<String, Election> getAllElectoralLevelData() {
        String[] electionIds = {"TK2021", "TK2023"};
        for (String electionId : electionIds) {
            String path = "/EML_bestanden_" + electionId;
            try {
                processor.processResults(electionId, PathUtils.getResourcePath(path));
                log.info("Processed Election {}", electionId);
            } catch (Exception e) {
                log.error("Could not process Election {}", electionId, e);
                e.printStackTrace();
            }
        }
        System.out.println("All files are processed.\n");
        return transformer.getElectionListMap();
    }

    public Election getElectoralLevelDataOf(String electionId) {
        String path = "/EML_bestanden_" + electionId;
        try {
            processor.processResults(electionId, PathUtils.getResourcePath(path));
            log.info("Processed Election {}", electionId);
        } catch (Exception e) {
            log.error("Could not process Election {}", electionId, e);
        }
        System.out.println("All files are processed.\n");
        return transformer.getElectoralLevelDataOf(electionId);
    }
}
