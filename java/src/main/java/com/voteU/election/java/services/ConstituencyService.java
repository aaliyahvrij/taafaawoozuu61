package com.voteU.election.java.services;
import com.voteU.election.java.CompactDTO.CompactConstituency;
import com.voteU.election.java.CompactDTO.CompactElection;
import com.voteU.election.java.model.Constituency;
import com.voteU.election.java.model.Election;
import com.voteU.election.java.model.Party;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ConstituencyService {

    private final ElectionService electionService;

    public ConstituencyService(ElectionService electionService) {
        this.electionService = electionService;
    }

    public Map<Integer, Constituency> getConstituenciesByYear(String year) {
        Election election = electionService.getElection(year);
        if (election == null){
            return null;
        }
        Map<Integer, Constituency> constituencies = election.getConstituencies();
        if (constituencies == null || constituencies.isEmpty()) {
            System.out.println("No constituencies found for election year: " + year);
            return null;
        }
        return constituencies;
    }

    public Map<Integer, CompactConstituency> getConstituenciesByYearCompact(String year) {
        Election election = electionService.getElection(year);
        if (election == null){
            return null;
        }
        Map<Integer, Constituency> constituencies = election.getConstituencies();
        if (constituencies == null || constituencies.isEmpty()) {
            System.out.println("No constituencies found for election year: " + year);
            return null;
        }
        Map<Integer, CompactConstituency> compactConstituencyMap = new HashMap<>();
        for (Constituency constituency : constituencies.values()) {
            CompactConstituency compactConstituency = new CompactConstituency(constituency.getId(), constituency.getName());
            compactConstituencyMap.put(compactConstituency.getId(), compactConstituency);
        }
        return compactConstituencyMap;
    }



    public Constituency getConstituencyById(String year, int constituencyId) {
        Map<Integer, Constituency> constituencies = getConstituenciesByYear(year);
        if (constituencies == null){
            return null;
        }
        return constituencies.get(constituencyId);
    }

    public Map<Integer, Party> getPartiesByConstituencyId(String year, int constituencyId) {
        Constituency constituency = getConstituencyById(year, constituencyId);
        if (constituency == null){
            return null;
        }
        return constituency.getParties();
    }
}
