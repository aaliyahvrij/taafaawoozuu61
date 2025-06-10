package com.voteU.election.java.ControllerTests;

import com.voteU.election.java.models.PollingStation;
import com.voteU.election.java.services.PoStService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;

@RestController
@RequestMapping("/api/{electionId}/pollingstations")
public class PoStControllerTest {
    private final PoStService poStService;

    public PoStControllerTest(PoStService poStService) {
        this.poStService = poStService;
    }

    @GetMapping
    public LinkedHashMap<String, PollingStation> getNationalLevel_pollingStationsOf(@PathVariable String electionId) {
        return this.poStService.getNationalLevel_pollingStationsOf(electionId);
    }
}

