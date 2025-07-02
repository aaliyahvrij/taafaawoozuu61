package com.voteU.election.java.controller.database;

import com.voteU.election.java.entities.electiondata.PollingStations;
import com.voteU.election.java.services.PollingStationsService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/pollingstations")
public class PollingStationsController {
    private final PollingStationsService pollingStationsService;

    public PollingStationsController(PollingStationsService pollingStationsService) {
        this.pollingStationsService = pollingStationsService;
    }


    @GetMapping("/search")
    public List<PollingStations> getPollingStationsByZipcode(
            @RequestParam String zipcode,
            @RequestParam(defaultValue = "TK2021") String electionId
            ) {
        return pollingStationsService.getPollingStationsByZipCode(zipcode, electionId);
    }

    @GetMapping
    public Page<PollingStations> getPollingStations(
            @RequestParam(defaultValue = "TK2021") String electionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size
    ) {
        return pollingStationsService.getPollingStations(electionId, page, size);
    }

}
