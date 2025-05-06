    package com.voteU.election.java.controller;


    import com.voteU.election.java.model.Election;
    import com.voteU.election.java.model.Party;
    import com.voteU.election.java.services.ElectionService;
    import org.springframework.web.bind.annotation.*;

    import java.util.Map;

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

        @PostMapping("/{electionYear}")
        public boolean readResultsYear(@PathVariable String electionYear) {
            return electionService.readElectionYear(electionYear);
        }

        @GetMapping
        public Map<String, Election> getAllElections() {
            return electionService.getAll();
        }

        @GetMapping("/{electionYear}")
        public Election getElection(@PathVariable String electionYear) {
            return electionService.getElectionByYear(electionYear);
        }

        @GetMapping("/{electionYear}/national")
        public Map<Integer, Party> getNationalPartiesByYear(@PathVariable String electionYear){
            return electionService.getNationalPartiesByYear(electionYear);
        }





    }
