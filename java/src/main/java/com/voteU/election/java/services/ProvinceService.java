package com.voteU.election.java.services;

import com.voteU.election.java.model.Election;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.model.Province;
import com.voteU.election.java.reader.DutchElectionReader;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProvinceService {
    private final Map<String, Map<Integer, Province>> storedElections = new HashMap<>();
    private final DutchElectionReader electionReader;


    public ProvinceService(DutchElectionReader electionReader) {
        this.electionReader = electionReader;
    }

    public List<Province> getProvinces(String year) {

        if (!storedElections.containsKey(year)) {
            Election election = electionReader.getElection(year);
            if (election != null) {
                Map<Integer, Province> provinceMap = new HashMap<>();
                for (Province province : election.getProvinces()) {
                    provinceMap.put(province.getId(), province);
                }
                storedElections.put(year, provinceMap);
            }
        }

        Map<Integer, Province> provinceMap = storedElections.get(year);
        if (provinceMap != null) {
            return new ArrayList<>(provinceMap.values());
        }

        return new ArrayList<>();
    }

    public Map<Integer, Party> getTotalVotesPerParty(Province province) {
        Map<Integer, Party> totalVotesPerParty = new HashMap<>();
        int totalVotes = 0;

        System.out.println("==== Provincie: " + province.getName() + " ====");
        for (var constituency : province.getConstituencies()) {
            System.out.println("Kieskring ID: " + constituency.getId() + ", naam: " + constituency.getName());
        }
        for (var constituency : province.getConstituencies()) {
            for (var authority : constituency.getAuthorities().values()) {
                System.out.println("Kieskring " + constituency.getName() + " heeft " + constituency.getAuthorities().size() + " gemeentes");
                for (var party : authority.getAuthorityParties().values()) {
                    totalVotesPerParty.compute(party.getId(), (id, existingParty) -> {
                        if (existingParty == null) {
                            Party newParty = new Party(party.getId(), party.getName());
                            newParty.setVotes(party.getVotes());
                            return newParty;
                        } else {
                            existingParty.setVotes(existingParty.getVotes() + party.getVotes());
                            return existingParty;
                        }
                    });
                    totalVotes += party.getVotes();
                }
            }
        }

        for (Party party : totalVotesPerParty.values()) {
            double percentage = (totalVotes == 0) ? 0 : (party.getVotes() * 100.0 / totalVotes);
            party.setPercentage(percentage);
        }

        return totalVotesPerParty;
    }

}
