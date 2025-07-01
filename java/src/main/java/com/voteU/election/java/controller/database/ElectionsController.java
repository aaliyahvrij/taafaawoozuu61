package com.voteU.election.java.controller.database;


import com.voteU.election.java.dto.DropdownOptionDTO;

import com.voteU.election.java.services.ElectionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("api/elections")
public class ElectionsController {

   private final ElectionService electionService;

   public ElectionsController(ElectionService electionService) {
       this.electionService = electionService;
   }

    @GetMapping
    public List<DropdownOptionDTO<String>> getElectionNames() {
       return electionService.getElectionNames();
    }

}
