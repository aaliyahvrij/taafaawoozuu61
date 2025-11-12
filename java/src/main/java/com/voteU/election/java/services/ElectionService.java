package com.voteU.election.java.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.voteU.election.java.utils.xml.ElectionReader;
import com.voteU.election.java.models.Election;
import com.voteU.election.java.models.PollingStation;
import com.voteU.election.java.models.Affiliation;

import java.util.LinkedHashMap;

@Slf4j
@Service
public class ElectionService {
    private final ElectionReader electionReader;
    public static final LinkedHashMap<String, Election> electionListLhMap = new LinkedHashMap<>();

    public ElectionService(ElectionReader electionReader) {
        this.electionReader = electionReader;
    }

    /**
     * Calls upon the reader to call upon the processor to retrieve
     * the data of the specified election(s).
     *
     * @return true if the data has been loaded successfully, false otherwise
     */
    public boolean getElection(String electionIdListString) {
        LinkedHashMap<String, Election> readerElectionListLhMap = this.electionReader.getElection(electionIdListString);
        if (readerElectionListLhMap.isEmpty()) {
            log.warn("No data found during getElectionData(%s).".formatted(electionIdListString));
            return false;
        }
        electionListLhMap.putAll(readerElectionListLhMap);
        return true;
    }

    /**
     * Retrieves all the affiliation data of the specified election(s).
     */
    public LinkedHashMap<String, LinkedHashMap<Integer, Affiliation>> getAffiListLhMap(String electionIdListString) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, LinkedHashMap<Integer, Affiliation>> affiList_listLhMap = new LinkedHashMap<>();
        for (String electionId : electionIdList) {
            Election election = electionListLhMap.get(electionId);
            affiList_listLhMap.put(electionId, election.getAffiListLhMap());
        }
        return affiList_listLhMap;
    }

    /**
     * Retrieves all the polling station data of the specified election(s).
     */
    public LinkedHashMap<String, LinkedHashMap<String, PollingStation>> getPoStListLhMap(String electionIdListString) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, LinkedHashMap<String, PollingStation>> poStList_listLhMap = new LinkedHashMap<>();
        for (String electionId : electionIdList) {
            Election election = electionListLhMap.get(electionId);
            poStList_listLhMap.put(electionId, election.getPoStListLhMap());
        }
        return poStList_listLhMap;
    }
}
