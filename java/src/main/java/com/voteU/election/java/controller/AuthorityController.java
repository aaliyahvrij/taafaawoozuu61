package com.voteU.election.java.controller;
import com.voteU.election.java.model.Authority;
import com.voteU.election.java.services.AuthorityService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/authority")
public class AuthorityController {

    private final AuthorityService authorityService;

    public AuthorityController(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @GetMapping("/{year}")
    public Map<String, Authority> getAuthorityByYear(@PathVariable String year) {
        return authorityService.getAuthoritiesByYear(year);
    }


}