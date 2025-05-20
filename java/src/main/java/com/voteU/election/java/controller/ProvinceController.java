package com.voteU.election.java.controller;

import com.voteU.election.java.model.Constituency;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.model.Province;
import com.voteU.election.java.services.ProvinceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller class for managing province-related operations within the context of an election.
 * It provides endpoints for retrieving provinces, constituencies, and party vote details by province.
 */
@RestController
@RequestMapping("/api/election/{year}/provinces")
public class ProvinceController {

    /**
     * Provides access to the service responsible for handling operations
     * related to provinces. This service may include functionalities
     * such as retrieving, updating, creating, or managing province data.
     */
    private final ProvinceService provinceService;

    /**
     * Constructor for creating an instance of ProvinceController.
     *
     * @param provinceService the service layer object used to handle province-related business logic.
     */
    public ProvinceController(ProvinceService provinceService) {
        this.provinceService = provinceService;
    }

    /**
     * Retrieves a list of provinces for the specified year.
     *
     * @param year the year based on which provinces are to be retrieved
     * @return a list of Province objects representing the provinces for the given year
     */
    @GetMapping
    public List<Province> getProvinces(@PathVariable String year) {
        return provinceService.getProvinces(year);
    }

    /**
     * Retrieves a list of constituencies associated with a given province ID and year.
     *
     * @param year the year for which the constituencies are to be retrieved
     * @param provinceId the ID of the province for which constituencies are to be retrieved
     * @return a list of constituencies associated with the specified province ID and year
     */
    @GetMapping("/{provinceId}/constituencies")
    public List<Constituency> getConstituenciesByProvinceId(@PathVariable String year, @PathVariable int provinceId) {
        return provinceService.getConstituenciesByProvinceId(year, provinceId);
    }

    /**
     * Retrieves the total votes per party for a given province and year.
     *
     * @param year the election year for which the votes are to be retrieved
     * @param provinceId the unique identifier of the province
     * @return a map where the key is the party ID and the value is the corresponding party information with total votes
     */
    @GetMapping("/{provinceId}/parties")
    public Map<Integer, Party> getTotalVotesPerParty(@PathVariable String year, @PathVariable int provinceId) {
        return provinceService.getTotalVotesPerParty(year, provinceId);
    }
}
