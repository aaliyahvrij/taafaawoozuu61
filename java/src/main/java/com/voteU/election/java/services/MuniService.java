package com.voteU.election.java.services;

import com.voteU.election.java.exceptions.AccessDeniedException;
import com.voteU.election.java.exceptions.ResourceAlreadyExistsException;
import com.voteU.election.java.exceptions.ResourceNotFoundException;
import com.voteU.election.java.models.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MuniService {
    private final ElectionService electionService;

    public MuniService(ElectionService electionService) {
        this.electionService = electionService;
    }

    public Map<String, Municipality> getConstiLevel_municipalitiesOf(String electionId, int constId) {
        Election election = electionService.getElectoralDataOf(electionId);
        if (election == null) {
            throw new ResourceNotFoundException("Election " + electionId + " not found");
        }
        Constituency constituency = election.getConstituencies().get(constId);
        if (constituency == null) {
            throw new ResourceNotFoundException("Constituency " + constId + " not found in election " + electionId);
        }
        return constituency.getMunicipalities();
    }

    public Map<String, Municipality> getConstiLevel_municipalitiesOf_compact(String electionId, int constId) {
        Election election = electionService.getElectoralDataOf(electionId);
        if (election == null) {
            throw new ResourceNotFoundException("Election " + electionId + " not found");
        }
        Constituency constituency = election.getConstituencies().get(constId);
        if (constituency == null) {
            throw new ResourceNotFoundException("Constituency " + constId + " not found in election " + electionId);
        }

        Map<String, Municipality> municipalities = constituency.getMunicipalities();
        Map<String, Municipality> compactMunicipalities = new HashMap<>();

        for (Municipality municipality : municipalities.values()) {
            compactMunicipalities.put(municipality.getId(), new Municipality(municipality.getId(), municipality.getName()));
        }

        return compactMunicipalities;
    }

    public Municipality getMunicipalityById(String electionId, Integer constId, String munId) {
        Map<String, Municipality> municipalities = getConstiLevel_municipalitiesOf(electionId, constId);
        Municipality municipality = municipalities.get(munId);
        if (municipality == null) {
            throw new ResourceNotFoundException("Municipality " + munId + " not found in constituency " + constId);
        }
        return municipality;
    }

    public Map<Integer, Affiliation> getMuniLevel_affiliationsOf(String electionId, Integer constituencyId, String authorityId) {
        Municipality municipality = getMunicipalityById(electionId, constituencyId, authorityId);
        if (municipality == null) {
            throw new ResourceNotFoundException("Municipality " + authorityId + " not found");
        }
        return municipality.getAffiliations();
    }

    public Affiliation getAffiliationById(String electionId, Integer constId, String munId, Integer partyId) {
        Map<Integer, Affiliation> affiliations = getMuniLevel_affiliationsOf(electionId, constId, munId);
        Affiliation affiliation = affiliations.get(partyId);
        if (affiliation == null) {
            throw new ResourceNotFoundException("Party " + partyId + " not found");
        }
        return affiliation;
    }

    public List<Candidate> getAffiLevel_candidatesOf(String electionId, Integer constId, String munId, Integer affId) {
        Affiliation affiliation = getAffiliationById(electionId, constId, munId, affId);
        if (affiliation == null) {
            throw new ResourceNotFoundException("Affiliation " + affId + " not found");
        }
        return affiliation.getCandidates();
    }
}
