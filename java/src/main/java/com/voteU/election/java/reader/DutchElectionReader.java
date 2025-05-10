package com.voteU.election.java.reader;

import com.voteU.election.java.model.Constituency;
import com.voteU.election.java.model.Election;
import com.voteU.election.java.model.PollingStation;
import com.voteU.election.java.utils.PathUtils;
import com.voteU.election.java.utils.xml.DutchElectionProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
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
        Map<String, Election> elections = new HashMap<>();

        for (String electionId : electionIds) {
            String path = "/EML_bestanden_" + electionId;
            try {
                // Process election data
                Election election = electionProcessor.processResults(electionId, PathUtils.getResourcePath(path));
                    elections.put(electionId, election);
                    log.info("Processed Election " + electionId);
            } catch(Exception e){
                System.out.println("Could not process " + electionId);
                e.printStackTrace();
            }
        }
        System.out.println("All files are processed.\n");
        Map<String, Election> electionsMap = new HashMap<>();
        for (Map.Entry<String, Election> entry : elections.entrySet()) {
            String electionYear = entry.getKey();
            Election election = transformer.getElection(electionYear);
            electionsMap.put(electionYear, election);

            if (!transformer.getConstituencyMap().containsKey(electionYear)) {
                List<Constituency> constituencies = election.getConstituencies();
                transformer.addConstituencies(electionYear, constituencies);
            }

            if (!transformer.getPollingStationMap().containsKey(electionYear)) {
                List<PollingStation> pollingStations = election.getPollingStations();
                transformer.addPollingStations(electionYear, pollingStations);
            }
        }


        return electionsMap;
    }

    public Map<String, Map<Integer, Constituency>> getConstituencies() {
        return transformer.getConstituencyMap();
    }

    public Map<String, Map<String, PollingStation>> getPollingStations() {
        return transformer.getPollingStationMap();
    }

}

