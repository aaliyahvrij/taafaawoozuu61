package com.voteU.election.java.services.electiondata.memory;

import com.voteU.election.java.dtoCompact.CompactConstituency;
import com.voteU.election.java.dtoCompact.CompactProvince;
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

    /**
     * A reference to the ElectionService, which manages the handling and retrieval
     * of election-related data. This service is used to access election details,
     * including retrieving elections, their associated data, and any processing logic
     * required to support province-related operations.
     */
    private final ElectionService electionService;
    /**
     * Repository interface for performing database operations related to Provinces.
     * Provides methods for retrieving province data specific to elections.
     * Used to abstract and encapsulate interaction with the Province-related persistence layer.
     */
    private final ProvinceRepository provinceRepository;

    /**
     * Constructs a ProvinceService instance, used to manage province-related operations
     * associated with elections. This service utilizes an ElectionService to fetch
     * election-specific data and a ProvinceRepository to perform repository-based queries
     * related to provinces.
     *
     * @param electionService    the service responsible for managing election-related operations
     * @param provinceRepository the repository for performing database operations on provinces
     */
    public ProvinceService(ElectionService electionService, ProvinceRepository provinceRepository) {
        this.electionService = electionService;
        this.provinceRepository = provinceRepository;
    }

    /**
     * Retrieves a list of provinces for the specified election year.
     *
     * @param year the election year for which the provinces are to be retrieved
     * @return a list of provinces for the specified election year, or an empty list if the election does not exist
     */
    public List<Province> getProvinces(String year) {
        Election election = electionService.getElection(year);
        if (election == null) return new ArrayList<>();
        return election.getProvinces();
    }

    /**
     * Retrieves a list of compact representations of provinces for the specified election year.
     *
     * @param year The election year for which the compact provinces are to be retrieved.
     * @return A list of CompactProvince objects representing the provinces for the given election year.
     * If no election exists for the specified year, an empty list is returned.
     */
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

    /**
     * Retrieves a list of constituencies associated with a specific province ID
     * for a given election year.
     *
     * @param year the year of the election for which constituencies are to be retrieved
     * @param provinceId the unique identifier of the province
     * @return a list of constituencies for the specified province ID in the given year.
     *         If the election or province cannot be found, an empty list is returned.
     */
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

    /**
     * Retrieves a list of compact constituencies for a specified province in a given election year.
     *
     * @param year the election year as a string
     * @param provinceId the unique identifier of the province
     * @return a list of CompactConstituency objects for the specified province, or an empty list if the election or province is not found
     */
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


    /**
     * Calculates the total votes received by each party within a given province and computes the
     * percentage of total votes for each party relative to all votes cast in the province.
     *
     * @param province the province containing constituencies, authorities, and associated parties whose votes
     *                 are to be aggregated and analyzed.
     * @return a map where the key is the party ID and the value is the corresponding Party object
     *         containing the total votes and percentage of votes for that party.
     */
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

    /**
     * Retrieves a map containing the total votes per party for a specified province in a given election year.
     *
     * @param year the election year for which the data is being fetched
     * @param provinceId the unique identifier of the province within the election
     * @return a map where the key is the party ID and the value is the associated Party object, or an empty map if the election or province does not exist
     */
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

    /**
     * Retrieves a list of all province names associated with a given election year.
     *
     * @param year the election year for which the province names should be fetched
     * @return a list of DropdownOptionDTO objects containing the province names and their IDs
     */
    public List<DropdownOptionDTO<Integer>> getAllProvinceNames(String year) {
        return provinceRepository.getProvincesByElectionId(year);
    }


    /**
     * Calculates the total number of votes for a given province in a specific election year.
     *
     * @param year the year of the election in which the votes need to be counted
     * @param provinceId the unique identifier of the province for which the votes are to be totaled
     * @return the total votes for the specified province and election year, or 0 if the election or province is not found
     */
    public int getTotalVotesForProvince(String year, int provinceId) {
        Election election = electionService.getElection(year);
        if (election == null) return 0;

        Province province = election.getProvinces().stream()
                .filter(p -> p.getId() == provinceId)
                .findFirst()
                .orElse(null);

        if (province == null) return 0;

        int totalVotes = 0;

        for (var constituency : province.getConstituencies()) {
            for (var authority : constituency.getAuthorities().values()) {
                for (var party : authority.getParties().values()) {
                    totalVotes += party.getVotes();
                }
            }
        }

        return totalVotes;
    }


}
