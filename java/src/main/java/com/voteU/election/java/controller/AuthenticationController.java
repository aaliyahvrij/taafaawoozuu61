package com.voteU.election.java.controller;

import com.voteU.election.java.dto.JwtResponse;
import com.voteU.election.java.dto.LoginDTO;
import com.voteU.election.java.services.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginDTO loginDTO) {
        JwtResponse jwtResponse = authenticationService.authenticate(loginDTO);
        return ResponseEntity.ok(jwtResponse);
    }

}
