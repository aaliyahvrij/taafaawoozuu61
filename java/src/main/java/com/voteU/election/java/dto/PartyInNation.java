package com.voteU.election.java.dto;

import com.voteU.election.java.dto.CandidateInNation;
import com.voteU.election.java.model.Candidate;

import java.util.List;

public class PartyInNation {
    private int id;
    private String name;
    private int votes;
    private List<CandidateInNation> candidates;
}