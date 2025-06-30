package com.voteU.election.java.controller;

import com.voteU.election.java.CompactDTO.CompactConstituency;
import com.voteU.election.java.CompactDTO.CompactProvince;
import com.voteU.election.java.model.Constituency;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.model.Province;
import com.voteU.election.java.services.ProvinceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller class responsible for handling HTTP requests related to provinces
 * in the context of elections for a specified year. The endpoints within this
 * controller allow clients to retrieve information about provinces, their
 * constituencies, and associated voting data.
 */
@RestController
@RequestMapping("/api/election/{year}/provinces")
public class ProvinceController {

    /**
     * A service component responsible for handling operations
     * and business logic related to provinces.
     * This includes interactions with data layers
     * and processing province-specific information.
     */
    private final ProvinceService provinceService;

    /**
     * Constructor for the ProvinceController class.
     *
     * @param provinceService the service instance used for managing province-related operations
     */
    public ProvinceController(ProvinceService provinceService) {
        this.provinceService = provinceService;
    }

    /**
     * Retrieves a list of provinces for the specified year.
     *
     * @param year the year for which to retrieve the list of provinces
     * @return a list of Province objects for the specified year
     */
    @GetMapping
    public List<Province> getProvinces(@PathVariable String year) {
        return provinceService.getProvinces(year);
    }

    /**
     * Retrieves a list of compact provinces for the specified year.
     *
     * @param year the year for which the compact provinces are to be retrieved
     * @return a list of CompactProvince objects representing the compact provinces for the specified year
     */
    @GetMapping("/compact")
    public List<CompactProvince> getCompactProvinces(@PathVariable String year) {
        return provinceService.getCompactProvinces(year);
    }

    /**
     * Retrieves the total number of votes for a specific province in a given year.
     *
     * @param year the year for which the votes are being retrieved
     * @param provinceId the unique identifier of the province
     * @return the total number of votes for the specified province and year
     */
    @GetMapping("/{provinceId}/votes")
    public int getTotalVotesForProvince(@PathVariable String year, @PathVariable int provinceId) {
        return provinceService.getTotalVotesForProvince(year, provinceId);
    }

    /**
     * Retrieves a list of constituencies for a given province ID and year.
     *
     * @param year the year for which the constituencies are to be retrieved
     * @param provinceId the identifier of the province whose constituencies are to be retrieved
     * @return a list of constituencies associated with the specified province ID and year
     */
    @GetMapping("/{provinceId}/constituencies")
    public List<Constituency> getConstituenciesByProvinceId(@PathVariable String year, @PathVariable int provinceId) {
        return provinceService.getConstituenciesByProvinceId(year, provinceId);
    }

    /**
     * Retrieves a list of compact constituencies based on the provided province ID and year.
     *
     * @param year The year for which the constituencies need to be retrieved.
     * @param provinceId The ID of the province for which the constituencies are to be fetched.
     * @return A list of compact constituencies associated with the given province ID and year.
     */
    @GetMapping("/{provinceId}/constituencies/compact")
    public List<CompactConstituency> getCompactConstituenciesByProvinceId(@PathVariable String year, @PathVariable int provinceId) {
        return provinceService.getCompactConstituenciesByProvinceId(year, provinceId);
    }

    /**
     * Retrieves the total votes per political party for a given province and year.
     *
     * @param year the year for which the voting data is to be retrieved
     * @param provinceId the unique identifier of the province
     * @return a map with the party ID as the key and the Party object containing vote details as the value
     */
    @GetMapping("/{provinceId}/parties")
    public Map<Integer, Party> getTotalVotesPerParty(@PathVariable String year, @PathVariable int provinceId) {
        return provinceService.getTotalVotesPerParty(year, provinceId);
    }


}
