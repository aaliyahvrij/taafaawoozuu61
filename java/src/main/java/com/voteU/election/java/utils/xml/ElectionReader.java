package com.voteU.election.java.utils.xml;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.voteU.election.java.models.Election;
import com.voteU.election.java.utils.PathUtils;

import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * Calls to the processor to retrieve the specified election data from the XML files.
 */
@Slf4j
@Component
public class ElectionReader {
    private final ElectionProcessor<Election> processor;
    private final ElectionTransformer transformer;
    private final String[] theEntireElectionIdList = {"TK2021", "TK2023"};

    public ElectionReader() {
        this.transformer = new ElectionTransformer();
        this.processor = new ElectionProcessor<>(this.transformer);
    }

    /**
     * Reads and processes the data of the specified election(s).
     *
     * @return A map containing election results, organized by election year.
     */
    public LinkedHashMap<String, Election> getElection(String electionIdListString) {
        String[] electionIdList;
        if (Objects.equals(electionIdListString, "all")) {
            electionIdList = theEntireElectionIdList;
        } else {
            electionIdList = electionIdListString.split("-");
        }
        for (String electionId : electionIdList) {
            String filePath = "/EML_bestanden_" + electionId;
            try {
                this.processor.processResults(electionId, PathUtils.getResourcePath(filePath));
                log.info("Processed Election {}", electionId);
            } catch (Exception e) {
                log.error("Could not process Election {}", electionId, e);
            }
        }
        System.out.println("All files are processed.\n");
        return this.transformer.getElectionListLhMap();
    }
}
