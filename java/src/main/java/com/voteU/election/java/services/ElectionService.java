package com.voteU.election.java.services;

import com.voteU.election.java.models.Election;
import com.voteU.election.java.models.PollingStation;
import com.voteU.election.java.models.Affiliation;
import com.voteU.election.java.utils.xml.ElectionReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Slf4j
@Service
public class ElectionService {
    private final ElectionReader electionReader;
    public static final LinkedHashMap<String, Election> electionList_lhMap = new LinkedHashMap<>();

    public ElectionService(ElectionReader electionReader) {
        this.electionReader = electionReader;
    }

    /**
     * Calls upon the reader to call upon the processor to retrieve
     * the electoral data of the specified election(s).
     *
     * @return true if the data has been loaded successfully, false otherwise
     */
    public boolean getElectoralData(String electionIdListString) {
        LinkedHashMap<String, Election> readerElectionList_lhMap = this.electionReader.getElectoralData(electionIdListString);
        if (readerElectionList_lhMap == null || readerElectionList_lhMap.isEmpty()) {
            log.warn("No electoral data found during getElectoralData(%s).".formatted(electionIdListString));
            return false;
        }
        electionList_lhMap.putAll(readerElectionList_lhMap);
        return true;
    }

    /**
     * Retrieves all the affiliation data of the specified election(s).
     */
    public LinkedHashMap<String, LinkedHashMap<Integer, Affiliation>> getAffiList_lhMap(String electionIdListString) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, LinkedHashMap<Integer, Affiliation>> affiList_list_lhMap = null;
        for (String electionId : electionIdList) {
            Election election = electionList_lhMap.get(electionId);
            affiList_list_lhMap.put(electionId, election.getAffiList_lhMap());
        }
        return affiList_list_lhMap;
    }

    /**
     * Retrieves all the polling station data of the specified election(s).
     */
    public LinkedHashMap<String, LinkedHashMap<String, PollingStation>> getPoStList_lhMap(String electionIdListString) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, LinkedHashMap<String, PollingStation>> poStList_list_lhMap = null;
        for (String electionId : electionIdList) {
            Election election = electionList_lhMap.get(electionId);
            poStList_list_lhMap.put(electionId, election.getPoStList_lhMap());
        }
        return poStList_list_lhMap;
    }
}
