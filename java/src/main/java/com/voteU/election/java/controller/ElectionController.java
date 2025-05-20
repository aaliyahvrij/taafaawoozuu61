    package com.voteU.election.java.controller;
    import com.voteU.election.java.CompactDTO.CompactElection;
    import com.voteU.election.java.model.Election;
    import com.voteU.election.java.model.Party;
    import com.voteU.election.java.services.ElectionService;
    import org.springframework.web.bind.annotation.*;
    import java.util.Map;

    /**
     * ElectionController is a REST controller that provides endpoints for managing and retrieving election-related data.
     * It interacts with the ElectionService to perform operations such as reading election results, retrieving election
     * details, and fetching all related parties for an election.
     *
     * The controller supports CRUD-like operations for elections and exposes different endpoints to serve various
     * requests related to elections and their associated entities.
     *
     * Mappings:
     * - `POST /api/election`: Reads all election results.
     * - `POST /api/election/{electionId}`: Reads results for a specific election.
     * - `GET /api/election`: Retrieves all elections.
     * - `GET /api/election/{electionId}`: Retrieves details of a specific election by ID.
     * - `GET /api/election/{electionId}/compact`: Fetches compact information for a specific election ID.
     * - `GET /api/election/{electionId}/parties`: Retrieves all parties associated with a specific election ID.
     */
    @RestController
    @RequestMapping("/api/election")
    public class ElectionController {
        private final ElectionService electionService;

        public ElectionController(ElectionService electionService) {
            this.electionService = electionService;
        }

        @PostMapping
        public boolean readResults() {
            return electionService.readElections();
        }

        @PostMapping("/{electionId}")
        public boolean readResultsElection(@PathVariable String electionId) {
            return electionService.readElection(electionId);
        }

        @GetMapping
        public Map<String, Election> getAllElections() {
            return electionService.getAll();
        }

        @GetMapping("/{electionId}")
        public Election getElection(@PathVariable String electionId) {
            return electionService.getElection(electionId);
        }

        @GetMapping("{electionId}/compact")
        public CompactElection getCompactElection(@PathVariable String electionId) {
            return electionService.getCompactElection(electionId);
        }

        @GetMapping("/{electionId}/parties")
        public Map<Integer, Party> getAllPartiesByElection(@PathVariable String electionId){
            return electionService.getAllPartiesByElection(electionId);
        }

    }
