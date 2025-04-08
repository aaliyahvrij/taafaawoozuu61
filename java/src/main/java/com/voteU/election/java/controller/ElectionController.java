    package com.voteU.election.java.controller;

    import com.voteU.election.java.model.Contest;
    import com.voteU.election.java.model.Election;
    import com.voteU.election.java.model.Party;
    import com.voteU.election.java.services.ElectionService;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;
    import java.util.Map;

    @CrossOrigin(origins = "http://localhost:5173")
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

        @PostMapping("/{electionId}/parties")
        public boolean readResultsYear(@PathVariable String electionId) {
            return electionService.readElectionYear(electionId);
        }


        @GetMapping("/{electionId}/parties")
        public Election getElection(@PathVariable String electionId) {
            return electionService.getElection(electionId);
        }

        @GetMapping
        public Map<String, Election> getParties() {

            return electionService.getAll();
        }



    }
