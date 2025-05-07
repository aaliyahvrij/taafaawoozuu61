package com.voteU.election.java.controller;
import com.voteU.election.java.model.Authority;
import com.voteU.election.java.services.AuthorityService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/election/{year}/authorities")
public class AuthorityController {

    private final AuthorityService authorityService;

    public AuthorityController(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @GetMapping
    public Map<String, Authority> getAuthoritiesByYear(@PathVariable String year) {
        return authorityService.getAuthoritiesByYear(year);
    }

    @GetMapping("/{authorityId}")
    public Authority getAuthorityById(@PathVariable String year, @PathVariable String authorityId) {
        return authorityService.getAuthorityById(year, authorityId);
    }

}