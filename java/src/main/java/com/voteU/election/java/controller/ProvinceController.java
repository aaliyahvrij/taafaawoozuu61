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
 * REST controller for handling operations related to provinces within a specific election year.
 * It provides endpoints to retrieve full and compact province data, constituencies by province,
 * and total votes per party in a province.
 */
@RestController
@RequestMapping("/api/election/{year}/provinces")
public class ProvinceController {

    private final ProvinceService provinceService;

    /**
     * Constructor for ProvinceController.
     *
     * @param provinceService The service that handles province-related logic.
     */
    public ProvinceController(ProvinceService provinceService) {
        this.provinceService = provinceService;
    }

    /**
     * Retrieves all provinces for a specific election year.
     *
     * @param year The election year (e.g. "2021", "2023").
     * @return A list of all provinces in the given year.
     */
    @GetMapping
    public List<Province> getProvinces(@PathVariable String year) {
        return provinceService.getProvinces(year);
    }
    /**
     * Retrieves a simplified list of provinces (compact format) for a given election year.
     *
     * @param year The election year.
     * @return A list of provinces with only essential information (e.g. ID and name).
     */
    @GetMapping("/compact")
    public List<CompactProvince> getCompactProvinces(@PathVariable String year) {
        return provinceService.getCompactProvinces(year);
    }

    /**
     * Retrieves all constituencies within a specific province and election year.
     *
     * @param year       The election year.
     * @param provinceId The ID of the province.
     * @return A list of constituencies belonging to the given province.
     */
    @GetMapping("/{provinceId}/constituencies")
    public List<Constituency> getConstituenciesByProvinceId(@PathVariable String year, @PathVariable int provinceId) {
        return provinceService.getConstituenciesByProvinceId(year, provinceId);
    }

    /**
     * Retrieves a compact list of constituencies (ID and name only) for a specific province and election year.
     *
     * @param year       The election year.
     * @param provinceId The ID of the province.
     * @return A list of compact constituencies belonging to the given province.
     */
    @GetMapping("/{provinceId}/constituencies/compact")
    public List<CompactConstituency> getCompactConstituenciesByProvinceId(@PathVariable String year, @PathVariable int provinceId) {
        return provinceService.getCompactConstituenciesByProvinceId(year, provinceId);
    }

    /**
     * Retrieves the total number of votes per party within a given province and election year.
     *
     * @param year       The election year.
     * @param provinceId The ID of the province.
     * @return A map where the key is the party ID and the value is the corresponding {@link Party} object with vote totals.
     */
    @GetMapping("/{provinceId}/parties")
    public Map<Integer, Party> getTotalVotesPerParty(@PathVariable String year, @PathVariable int provinceId) {
        return provinceService.getTotalVotesPerParty(year, provinceId);
    }


}
