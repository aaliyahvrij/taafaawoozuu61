    package com.voteU.election.java.controller;

    import com.voteU.election.java.model.Party;
    import com.voteU.election.java.services.ElectionService;
    import org.springframework.web.bind.annotation.*;
    import java.util.Map;

    @RestController
    @RequestMapping("election")
    public class ElectionController {
        private final ElectionService electionService;

        public ElectionController(ElectionService electionService) {
            this.electionService = electionService;
        }

        @GetMapping
        public Map<Integer, Party> getParties() {
            return electionService.getParties();
        }

        @PostMapping
        public boolean readResults() {
            return electionService.readResults();
        }

    }
