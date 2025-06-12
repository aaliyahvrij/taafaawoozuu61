package com.voteU.election.java.services;

import com.voteU.election.java.CompactDTO.CompactConstituency;
import com.voteU.election.java.CompactDTO.CompactProvince;
import com.voteU.election.java.dto.DropdownOptionDTO;
import com.voteU.election.java.model.Constituency;
import com.voteU.election.java.model.Election;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.model.Province;
import com.voteU.election.java.repositories.electiondata.ProvinceRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProvinceService {

    private final ElectionService electionService;
    private final ProvinceRepository provinceRepository;

    public ProvinceService(ElectionService electionService, ProvinceRepository provinceRepository) {
        this.electionService = electionService;
        this.provinceRepository = provinceRepository;
    }

    public List<Province> getProvinces(String year) {
        Election election = electionService.getElection(year);
        if (election == null) return new ArrayList<>();
        return election.getProvinces();
    }

    public List<CompactProvince> getCompactProvinces(String year) {
        Election election = electionService.getElection(year);
        if (election == null) return new ArrayList<>();
        List<Province> provinceMap = election.getProvinces();
        List<CompactProvince> compactProvinces = new ArrayList<>();
        for (Province province : provinceMap) {
            compactProvinces.add(new CompactProvince(province.getId(), province.getName()));
        }
        return compactProvinces;
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

    public List<CompactConstituency> getCompactConstituenciesByProvinceId(String year, int provinceId) {
        Election election = electionService.getElection(year);
        if (election == null) return new ArrayList<>();

        Province province = election.getProvinces().stream()
                .filter(p -> p.getId() == provinceId)
                .findFirst()
                .orElse(null);

        if (province == null) return new ArrayList<>();

        List<CompactConstituency> compactConstituencies = new ArrayList<>();
        for (Constituency constituency : province.getConstituencies()) {
            compactConstituencies.add(new CompactConstituency(constituency.getId(), constituency.getName()));
        }

        return compactConstituencies;
    }


    public Map<Integer, Party> getTotalVotesPerParty(Province province) {
        Map<Integer, Party> totalVotesPerParty = new HashMap<>();
        int totalVotes = 0;

        for (var constituency : province.getConstituencies()) {
            for (var authority : constituency.getAuthorities().values()) {
                for (var party : authority.getParties().values()) {
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

    public List<DropdownOptionDTO<Integer>> getAllProvinceNames(String year) {
        return provinceRepository.getProvincesByElectionId(year);
    }

    public Map<Integer, Party> getParties(String year,  int provinceId) {
        Election election = electionService.getElection(year);
        List<Province> provinces = election.getProvinces();
        for (Province province : provinces) {
            if (province.getId() == provinceId){
                return province.getParties();
            }
        }
        return null;
    }

    public Map<String, Object> getSummary(String year, int provinceId) {
        Election election = electionService.getElection(year);
        List<Province> provinces = election.getProvinces();
        for (Province province : provinces) {
            if (province.getId() == provinceId) {
                return province.getSummary();
            }
        }
        return null;
    }


}
