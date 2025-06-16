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
 * REST controller that handles API requests related to provinces within a given election year.
 * Provides endpoints for retrieving provinces, constituencies, and aggregated vote data.
 */
@RestController
@RequestMapping("/api/election/{year}/provinces")
public class ProvinceController {

    private final ProvinceService provinceService;

    /**
     * Constructs the controller with required services.
     *
     * @param provinceService the service that handles province-related operations
     */
    public ProvinceController(ProvinceService provinceService) {
        this.provinceService = provinceService;
    }

    /**
     * Retrieves all provinces for a specific election year.
     *
     * @param year the election year
     * @return list of full {@link Province} objects
     */
    @GetMapping
    public List<Province> getProvinces(@PathVariable String year) {
        return provinceService.getProvinces(year);
    }

    /**
     * Retrieves a compact list of provinces (ID and name only) for a specific election year.
     *
     * @param year the election year
     * @return list of {@link CompactProvince} DTOs
     */
    @GetMapping("/compact")
    public List<CompactProvince> getCompactProvinces(@PathVariable String year) {
        return provinceService.getCompactProvinces(year);
    }

    /**
     * Retrieves the total number of votes cast in a specific province during a specific election year.
     *
     * @param year       the election year
     * @param provinceId the ID of the province
     * @return total number of votes
     */
    @GetMapping("/{provinceId}/votes")
    public int getTotalVotesForProvince(@PathVariable String year, @PathVariable int provinceId) {
        return provinceService.getTotalVotesForProvince(year, provinceId);
    }

    /**
     * Retrieves all constituencies within a specific province for a given election year.
     *
     * @param year       the election year
     * @param provinceId the ID of the province
     * @return list of full {@link Constituency} objects
     */
    @GetMapping("/{provinceId}/constituencies")
    public List<Constituency> getConstituenciesByProvinceId(@PathVariable String year, @PathVariable int provinceId) {
        return provinceService.getConstituenciesByProvinceId(year, provinceId);
    }

    /**
     * Retrieves a compact list of constituencies (ID and name only) for a specific province and election year.
     *
     * @param year       the election year
     * @param provinceId the ID of the province
     * @return list of {@link CompactConstituency} DTOs
     */
    @GetMapping("/{provinceId}/constituencies/compact")
    public List<CompactConstituency> getCompactConstituenciesByProvinceId(@PathVariable String year, @PathVariable int provinceId) {
        return provinceService.getCompactConstituenciesByProvinceId(year, provinceId);
    }

    /**
     * Retrieves total votes per party in a specific province during a specific election year.
     *
     * @param year       the election year
     * @param provinceId the ID of the province
     * @return a map of party IDs to {@link Party} objects with vote counts and percentages
     */
    @GetMapping("/{provinceId}/parties")
    public Map<Integer, Party> getTotalVotesPerParty(@PathVariable String year, @PathVariable int provinceId) {
        return provinceService.getTotalVotesPerParty(year, provinceId);
    }


}
