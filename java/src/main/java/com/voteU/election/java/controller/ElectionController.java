    package com.voteU.election.java.controller;


    import com.voteU.election.java.model.Election;
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

        @PostMapping("/{electionYear}/parties")
        public boolean readResultsYear(@PathVariable String electionYear) {
            return electionService.readElectionYear(electionYear);
        }


        @GetMapping("/{electionYear}/parties")
        public Election getElection(@PathVariable String electionYear) {
            return electionService.getElectionByYear(electionYear);
        }

        @GetMapping
        public Map<String, Election> getParties() {

            return electionService.getAll();
        }



    }
