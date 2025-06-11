package com.voteU.election.java.controller.databaseVotes;


import com.voteU.election.java.dto.DropdownOptionDTO;
import com.voteU.election.java.services.AuthorityService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("api/authorities")
public class AuthoritiesController {

    private final AuthorityService authorityService;

    public AuthoritiesController(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }


    @GetMapping("/by-constituency")
    public List<DropdownOptionDTO<String>> getAuthoritiesByConstituencyId(@RequestParam String electionId, @RequestParam Integer constituencyId) {
        return authorityService.getAuthorityNamesByConstituencyId(electionId, constituencyId);
    }


}
