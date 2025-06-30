package com.voteU.election.java.controller;
import com.voteU.election.java.CompactDTO.CompactConstituency;
import com.voteU.election.java.CompactDTO.CompactElection;
import com.voteU.election.java.model.Constituency;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.services.ConstituencyService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * REST controller responsible for handling requests related to constituencies during a specific election year.
 * Provides endpoints to retrieve full and compact constituency data, retrieve specific constituencies by ID,
 * and get the parties associated with a given constituency.
 * <p>
 * Base URL: /api/election/{year}/constituencies
 * </p>
 *
 */
@RestController
@RequestMapping("/api/election/{year}/constituencies")
public class ConstituencyController {

    private final ConstituencyService constituencyService;

    /**
     * Constructor for ConstituencyController.
     *
     * @param constituencyService the service layer handling constituency data
     */
    public ConstituencyController(ConstituencyService constituencyService) {
        this.constituencyService = constituencyService;
    }

    /**
     * Retrieves all constituencies for a given election year.
     *
     * @param year the election year
     * @return a map of constituency IDs to full Constituency objects
     */
    @GetMapping
    public Map<Integer, Constituency> getConstituenciesByYear(@PathVariable String year){
        return constituencyService.getConstituenciesByYear(year);
    }

    /**
     * Retrieves compact representations of all constituencies for a given election year.
     *
     * @param year the election year
     * @return a map of constituency IDs to CompactConstituency DTOs
     */
    @GetMapping("/compact")
    public Map<Integer, CompactConstituency> getConstituenciesByYearCompact(@PathVariable String year){
        return constituencyService.getConstituenciesByYearCompact(year);
    }

    /**
     * Retrieves a specific constituency by its ID for a given election year.
     *
     * @param year the election year
     * @param constituencyId the ID of the constituency
     * @return the Constituency object
     */
    @GetMapping("/{constituencyId}")
    public Constituency getConstituencyById(@PathVariable String year, @PathVariable Integer constituencyId){
        return constituencyService.getConstituencyById(year, constituencyId);
    }

    /**
     * Retrieves all parties associated with a specific constituency for a given election year.
     *
     * @param year the election year
     * @param constituencyId the ID of the constituency
     * @return a map of party IDs to Party objects
     */
    @GetMapping("/{constituencyId}/parties")
    public Map<Integer, Party> getPartiesByConstituencyId(@PathVariable String year, @PathVariable Integer constituencyId){
        return constituencyService.getPartiesByConstituencyId(year, constituencyId);
    }

}

