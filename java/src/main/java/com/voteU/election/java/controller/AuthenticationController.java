package com.voteU.election.java.controller;

import com.voteU.election.java.dto.JwtResponse;
import com.voteU.election.java.dto.LoginDTO;
import com.voteU.election.java.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Authenticate user", description = "Authenticate user by checking and unhashing the login credentials")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User logged in successfully", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = JwtResponse.class),
                    examples = @ExampleObject(
                            name = "JWT Token Example",
                            value = """
                {
                  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                }
            """))),
            @ApiResponse(responseCode = "404", description = "User not found.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                {
                  "timestamp": "2025-06-09T12:34:56",
                  "status": 404,
                  "error": "Not Found",
                  "message": "User not found",
                  "path": "/api/users/id/99"
                }
            """))),
            @ApiResponse(responseCode = "403", description = "forbidden", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                {
                  "timestamp": "2025-06-09T12:34:56",
                  "status": 401,
                  "error": "Forbidden",
                  "message": "Invalid or missing authentication token",
                  "path": "/api/users/id/1"
                }
            """)))
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginDTO loginDTO) {
        JwtResponse jwtResponse = authenticationService.authenticate(loginDTO);
        return ResponseEntity.ok(jwtResponse);
    }

}
