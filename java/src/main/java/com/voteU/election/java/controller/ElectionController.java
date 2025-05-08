    package com.voteU.election.java.controller;
    import com.voteU.election.java.model.Authority;
    import com.voteU.election.java.model.Election;
    import com.voteU.election.java.model.Party;
    import com.voteU.election.java.services.AuthorityService;
    import com.voteU.election.java.services.ElectionService;
    import org.springframework.web.bind.annotation.*;
    import java.util.Map;

    @RestController
    @RequestMapping("/api/election")
    public class ElectionController {
        private final ElectionService electionService;
        private final AuthorityService authorityService;

        public ElectionController(ElectionService electionService, AuthorityService authorityService) {
            this.electionService = electionService;
            this.authorityService = authorityService;
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

        @GetMapping("/{electionId}/parties")
        public Map<Integer, Party> getAllPartiesByElection(@PathVariable String electionId){
            return electionService.getAllPartiesByElection(electionId);
        }

        @GetMapping("/{electionId}/authorities")
        public Map<String, Authority> getAllAuthoritiesByElection(@PathVariable String electionId) {
            return authorityService.getAllAuthoritiesByElection(electionId);
        }
    }
