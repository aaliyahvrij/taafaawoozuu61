package com.voteU.election.java.services;

import com.voteU.election.java.model.Constituency;
import com.voteU.election.java.model.Election;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.model.Province;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service class responsible for managing operations related to provinces within the election system.
 * Provides functionality for retrieving province data, constituencies within a province,
 * and aggregate voting information for parties within a province.
 */
@Service
public class ProvinceService {

    private final ElectionService electionService;

    public ProvinceService(ElectionService electionService) {
        this.electionService = electionService;
    }

    public List<Province> getProvinces(String year) {
        Election election = electionService.getElection(year);
        if (election == null) return new ArrayList<>();
        return election.getProvinces();
    }

    public List<Constituency> getConstituenciesByProvinceId(String year, int provinceId) {
        Election election = electionService.getElection(year);
        if (election == null) return new ArrayList<>();

        Province province = election.getProvinces().stream()
                .filter(p -> p.getId() == provinceId)
                .findFirst()
                .orElse(null);

        if (province == null) return new ArrayList<>();

        return province.getConstituencies();
    }

    public Map<Integer, Party> getTotalVotesPerParty(Province province) {
        Map<Integer, Party> totalVotesPerParty = new HashMap<>();
        int totalVotes = 0;

        for (var constituency : province.getConstituencies()) {
            for (var authority : constituency.getAuthorities().values()) {
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

    public Map<Integer, Party> getTotalVotesPerParty(String year, int provinceId) {
        Election election = electionService.getElection(year);
        if (election == null) return Map.of();

        Province province = election.getProvinces().stream()
                .filter(p -> p.getId() == provinceId)
                .findFirst()
                .orElse(null);

        if (province == null) return Map.of();

        return getTotalVotesPerParty(province);
    }
}
