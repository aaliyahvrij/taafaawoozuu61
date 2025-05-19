package com.voteU.election.java.reader;

import com.voteU.election.java.model.*;
import com.voteU.election.java.utils.xml.DutchElectionProcessor;
import com.voteU.election.java.utils.xml.Transformer;
import lombok.Getter;

import java.util.*;

/**
 * A Transformer that processes election data and organizes it into Election objects.
 */
@Getter
public class DutchElectionTransformer implements Transformer<Election> {
    private final Map<String, Election> elections = new HashMap<>();
    private static final Map<Integer, Integer> DISTRICT_TO_PROVINCE_ID = Map.ofEntries(
            Map.entry(3, 1),  // Drenthe
            Map.entry(5, 2),  // Flevoland
            Map.entry(2, 3),  // Friesland
            Map.entry(7, 4),  // Gelderland
            Map.entry(6, 4),
            Map.entry(1, 5),  // Groningen
            Map.entry(19, 6),  // Limburg
            Map.entry(18, 7),  // Noord-Brabant
            Map.entry(17, 7),
            Map.entry(10, 8),  // Noord-Holland
            Map.entry(9, 8),
            Map.entry(11, 8),
            Map.entry(4, 9),  // Overijssel
            Map.entry(8, 10), // Utrecht
            Map.entry(16, 11), // Zeeland
            Map.entry(14, 12),  // Zuid-Holland
            Map.entry(15, 12),
            Map.entry(13, 12),
            Map.entry(12, 12)
    );


    @Override
    public void registerElection(Map<String, String> electionData) {
        String electionId = electionData.get(DutchElectionProcessor.ELECTION_IDENTIFIER);
        String electionName = electionData.get(DutchElectionProcessor.ELECTION_NAME);
        String electionDate = electionData.get(DutchElectionProcessor.ELECTION_DATE);

        if (electionId == null || electionName == null || electionDate == null) {
            System.out.println("Incomplete election data: Missing ID, Name, or Date.");
            return;
        }

        // Get or create the Election object
        Election election = elections.get(electionId);
        if (election == null) {
            election = new Election(electionId, electionName, electionDate);
            elections.put(electionId, election);
        }
        registerProvinceConstituencies(election.getId());
    }

    @Override
    public void registerContest(Map<String, String> contestData) {
        // Handle contest data if needed
    }

    @Override
    public void registerAffiliation(Map<String, String> affiliationData) {
        // Optional: Handle affiliation data if required separately
    }

    @Override
    public void registerNation(Map<String, String> votesData) {
        String source = votesData.get("Source");
        boolean isTotalVotes = "TOTAL".equals(source);

        // Safely get party ID
        String partyIdStr = votesData.get(DutchElectionProcessor.AFFILIATION_IDENTIFIER);
        if (partyIdStr == null) {
            System.err.println("❌ Missing AFFILIATION_IDENTIFIER in votesData: " + votesData);
            return;
        }

        int partyId;
        try {
            partyId = Integer.parseInt(partyIdStr);
        } catch (NumberFormatException e) {
            System.err.println("❌ Invalid AFFILIATION_IDENTIFIER: '" + partyIdStr + "' in " + votesData);
            return;
        }

        String partyName = votesData.get(DutchElectionProcessor.REGISTERED_NAME);
        if (partyName == null) {
            partyName = "UNKNOWN";
        }

        // Check if the party already exists
        String electionId = votesData.get(DutchElectionProcessor.ELECTION_IDENTIFIER);
        Election election = elections.get(electionId);
        Map<Integer, Party> partyMap = election.getParties();
        Party party = partyMap.get(partyId);

        // Register party on TOTAL only, if not already registered
        if (isTotalVotes && party == null) {
            String partyVotesStr = votesData.get(DutchElectionProcessor.VALID_VOTES);
            if (partyVotesStr == null) {
                System.err.println("❌ Missing VALID_VOTES for party " + partyName + ": " + votesData);
                return;
            }

            int partyVotes;
            try {
                partyVotes = Integer.parseInt(partyVotesStr);
            } catch (NumberFormatException e) {
                System.err.println("❌ Invalid VALID_VOTES value: '" + partyVotesStr + "' in " + votesData);
                return;
            }

            // Create and register the new party
            party = new Party(partyId, partyName);
            party.setVotes(partyVotes);
            partyMap.put(partyId, party);
            // Removed duplicate logging here to prevent repeated logs during multiple calls
        }

        // Handle candidate votes
        if (votesData.containsKey("CandidateVotes")) {
            String candidateId = votesData.get(DutchElectionProcessor.CANDIDATE_IDENTIFIER);
            String candidateVotesStr = votesData.get("CandidateVotes");

            if (candidateId == null || candidateVotesStr == null) {
                System.err.println("❌ Missing candidate data in: " + votesData);
                return;
            }

            int candidateVotes;
            try {
                candidateVotes = Integer.parseInt(candidateVotesStr);
            } catch (NumberFormatException e) {
                System.err.println("❌ Invalid CandidateVotes value: '" + candidateVotesStr + "' in " + votesData);
                return;
            }

            // Check if the candidate already exists and is added to the party
            if (isTotalVotes && !party.hasCandidateShortCode(candidateId)) {
                Candidate candidate = new Candidate();
                candidate.setShortCode(candidateId);
                candidate.setVotes(candidateVotes);
                party.addCandidate(candidate);
                // Removed duplicate logging here for the candidate as well
            }
        }

        partyMap.put(partyId, party);
        election.recalculateTotalVotes();

        // Ensure only the number of registered parties is logged, not each time for each party
        // This logging happens once at the end, after all votes are processed.

    }


    public void registerConstituency(Map<String, String> constituencyData, Map<Integer, Integer> affiliationVotes, Map<Integer, Map<Integer, Integer>> candidateVotes, Map<Integer, String> affiliationNames) {
        // Step 1: Extract required info
        String electionId = constituencyData.get(DutchElectionProcessor.ELECTION_IDENTIFIER);
        int contestId = Integer.parseInt(constituencyData.get(DutchElectionProcessor.CONTEST_IDENTIFIER));
        String contestName = constituencyData.get(DutchElectionProcessor.CONTEST_NAME);


        // Step 2: Retrieve election
        Election election = elections.get(electionId);
        if (election == null) {
            System.err.println("[registerConstituency] ❌ No election found for ID: '" + electionId + "'. Aborting.");
            return;
        }

        // Step 3: Get or create constituency map
        Map<Integer, Constituency> constituencyMap = election.getConstituencies();
        if (constituencyMap == null) {
            System.out.println("[registerConstituency] ⚠️ Constituencies map was null. Initializing new map.");
            constituencyMap = new HashMap<>();
            election.setConstituencies(constituencyMap);
        } else {
            System.out.println("[registerConstituency] ✅ Found existing constituencies map with size: " + constituencyMap.size());
        }

        // Step 4: Get or create the constituency
        boolean isNew = !constituencyMap.containsKey(contestId);
        Constituency constituency = constituencyMap.computeIfAbsent(contestId, id -> new Constituency(id, contestName));

        // Step 5: Create new party map for this constituency
        Map<Integer, Party> parties = new HashMap<>();

        for (Map.Entry<Integer, String> entry : affiliationNames.entrySet()) {
            int partyId = entry.getKey();
            String partyName = entry.getValue();
            int totalVotes = affiliationVotes.getOrDefault(partyId, 0);

            Party party = new Party(partyId, partyName);
            party.setVotes(totalVotes);

            // Handle candidates
            Map<Integer, Integer> candidateVoteMap = candidateVotes.getOrDefault(partyId, new HashMap<>());
            List<Candidate> candidates = new ArrayList<>();

            for (Map.Entry<Integer, Integer> candEntry : candidateVoteMap.entrySet()) {
                int candidateId = candEntry.getKey();
                int votes = candEntry.getValue();

                Candidate candidate = new Candidate();
                candidate.setId(candidateId);
                candidate.setVotes(votes);
                candidate.setPartyId(partyId);

                candidates.add(candidate);
            }

            party.setCandidates(candidates);
            parties.put(partyId, party);
            // Update total authority votes
            int totalConstituencyVotes = parties.values().stream().mapToInt(Party::getVotes).sum();
            constituency.setVotes(totalConstituencyVotes);

        }

        // Step 6: Set the parties to the constituency and store it back
        constituency.setParties(parties);
        constituencyMap.put(contestId, constituency);
    }


    @Override
    public void registerAuthority(Map<String, String> authorityData) {
        String electionId = authorityData.get(DutchElectionProcessor.ELECTION_IDENTIFIER);
        String contestIdStr = authorityData.get(DutchElectionProcessor.CONTEST_IDENTIFIER);
        String authorityId = authorityData.get(DutchElectionProcessor.AUTHORITY_IDENTIFIER);
        String partyIdStr = authorityData.get(DutchElectionProcessor.AFFILIATION_IDENTIFIER);
        String partyName = authorityData.getOrDefault(DutchElectionProcessor.REGISTERED_NAME, "UNKNOWN");
        String authorityName = authorityData.get(DutchElectionProcessor.AUTHORITY_NAME);
        boolean isTotalVotes = "GEMEENTE".equals(authorityData.get("Source"));

        // Validate basic data
        if (electionId == null || contestIdStr == null || authorityId == null || partyIdStr == null) return;

        int contestId, partyId;
        try {
            contestId = Integer.parseInt(contestIdStr);
            partyId = Integer.parseInt(partyIdStr);
        } catch (NumberFormatException e) {
            return;
        }

        // Retrieve election and constituency
        Election election = elections.get(electionId);
        if (election == null) return;

        Constituency constituency = election.getConstituencies().get(contestId);
        if (constituency == null) {
            System.err.println("[registerAuthority] ❌ Constituency with ID " + contestId + " not found in election " + electionId);
            return;
        }

        // Get or create authority in constituency
        Map<String, Authority> authorityMap = constituency.getAuthorities();
        Authority authorityInMap = authorityMap.computeIfAbsent(authorityId, id -> {
            Authority authority = new Authority(id);
            authority.setName(authorityName);
            authority.setConstituencyId(contestId);
            return authority;
        });

        // Register party votes under authority
        Map<Integer, Party> partyMap = authorityInMap.getAuthorityParties();
        Party party = partyMap.get(partyId);

        if (isTotalVotes && party == null) {
            String votesStr = authorityData.get(DutchElectionProcessor.VALID_VOTES);
            if (votesStr == null) return;

            try {
                int votes = Integer.parseInt(votesStr);
                party = new Party(partyId, partyName);
                party.setVotes(votes);
                partyMap.put(partyId, party);

                // Update total authority votes
                int totalVotes = partyMap.values().stream().mapToInt(Party::getVotes).sum();
                authorityInMap.setVotes(totalVotes);

            } catch (NumberFormatException ignored) {}
        }

        // Register candidate votes (if applicable)
        if (authorityData.containsKey("CandidateVotes") && party != null && isTotalVotes) {
            try {
                int candidateId = Integer.parseInt(authorityData.get(DutchElectionProcessor.CANDIDATE_IDENTIFIER));
                int candidateVotes = Integer.parseInt(authorityData.get("CandidateVotes"));

                if (!party.hasCandidateId(candidateId)) {
                    Candidate candidate = new Candidate();
                    candidate.setId(candidateId);
                    candidate.setVotes(candidateVotes);
                    candidate.setPartyId(partyId);
                    party.addCandidate(candidate);
                }
            } catch (NumberFormatException | NullPointerException ignored) {}
        }
    }

    @Override
    public void registerPollingStation(Map<String, String> reportingUnitData) {
        String electionId = reportingUnitData.get(DutchElectionProcessor.ELECTION_IDENTIFIER);
        String contestIdStr = reportingUnitData.get(DutchElectionProcessor.CONTEST_IDENTIFIER);
        String authorityId = reportingUnitData.get(DutchElectionProcessor.AUTHORITY_IDENTIFIER);
        String pollingStationId = reportingUnitData.get(DutchElectionProcessor.REPORTING_UNIT_IDENTIFIER);
        String partyIdStr = reportingUnitData.get(DutchElectionProcessor.AFFILIATION_IDENTIFIER);
        String partyName = reportingUnitData.getOrDefault(DutchElectionProcessor.REGISTERED_NAME, "UNKNOWN");
        String pollingStationName = reportingUnitData.get(DutchElectionProcessor.REPORTING_UNIT_NAME);
        String zipcode = reportingUnitData.get(DutchElectionProcessor.ZIPCODE);

        // Validate basic data
        if (electionId == null || contestIdStr == null || authorityId == null || pollingStationId == null || partyIdStr == null) return;

        int contestId, partyId;
        try {
            contestId = Integer.parseInt(contestIdStr);
            partyId = Integer.parseInt(partyIdStr);
        } catch (NumberFormatException e) {
            return;
        }

        // Retrieve election and constituency
        Election election = elections.get(electionId);
        if (election == null) return;

        Constituency constituency = election.getConstituencies().get(contestId);
        if (constituency == null) {
            System.err.println("[registerPollingStation] ❌ Constituency with ID " + contestId + " not found in election " + electionId);
            return;
        }

        // Get the authority inside the constituency
        Map<String, Authority> authorityMap = constituency.getAuthorities();
        Authority authority = authorityMap.get(authorityId);
        if (authority == null) {
            System.err.println("[registerPollingStation] ❌ Authority with ID " + authorityId + " not found in constituency " + contestId);
            return;
        }

        // Get or create polling station inside authority
        Map<String, PollingStation> pollingStationMap = authority.getPollingStations();
        PollingStation pollingStation = pollingStationMap.computeIfAbsent(pollingStationId, id -> {
            PollingStation ps = new PollingStation(pollingStationId,pollingStationName, zipcode);
            ps.setAuthorityId(authorityId);
            return ps;
        });

        // Register party votes under polling station
        Map<Integer, Party> partyMap = pollingStation.getParties();
        Party party = partyMap.get(partyId);

        if (party == null) {
            String votesStr = reportingUnitData.get("AffiliationVotes");
            if (votesStr == null) return;

            try {
                int votes = Integer.parseInt(votesStr);
                party = new Party(partyId, partyName);
                party.setVotes(votes);
                partyMap.put(partyId, party);

                // Update total polling station votes
                int totalVotes = partyMap.values().stream().mapToInt(Party::getVotes).sum();
                pollingStation.setVotes(totalVotes);

            } catch (NumberFormatException ignored) {}
        }

        // Register candidate votes (if applicable)
        if (reportingUnitData.containsKey("CandidateVotes") && party != null) {
            try {
                int candidateId = Integer.parseInt(reportingUnitData.get(DutchElectionProcessor.CANDIDATE_IDENTIFIER));
                int candidateVotes = Integer.parseInt(reportingUnitData.get("CandidateVotes"));

                if (!party.hasCandidateId(candidateId)) {
                    Candidate candidate = new Candidate();
                    candidate.setId(candidateId);
                    candidate.setVotes(candidateVotes);
                    candidate.setPartyId(partyId);
                    party.addCandidate(candidate);
                }
            } catch (NumberFormatException | NullPointerException ignored) {}
        }
    }


    @Override
    public void registerCandidate(Map<String, String> candidateData) {
        String caIdStr = candidateData.get(DutchElectionProcessor.CANDIDATE_IDENTIFIER);
        String caFirstName = candidateData.get(DutchElectionProcessor.FIRST_NAME);
        String caLastName = candidateData.get(DutchElectionProcessor.LAST_NAME);
        String localityName = candidateData.get(DutchElectionProcessor.LOCALITY_NAME);
        String gender = candidateData.get(DutchElectionProcessor.GENDER);
        String electionId = candidateData.get(DutchElectionProcessor.ELECTION_IDENTIFIER);
        String contestIdStr = candidateData.get(DutchElectionProcessor.CONTEST_IDENTIFIER);
        String affIdStr = candidateData.get(DutchElectionProcessor.AFFILIATION_IDENTIFIER);

        if (caIdStr != null && caLastName != null && electionId != null && contestIdStr != null && affIdStr != null) {

            int caId = Integer.parseInt(caIdStr);
            int affId = Integer.parseInt(affIdStr);
            int contestId = Integer.parseInt(contestIdStr);
            Election election = elections.get(electionId);

            if (election != null) {
                Map<Integer, Constituency> constituencies = election.getConstituencies();
                Constituency constituency = constituencies.get(contestId);

                if (constituency != null) {
                    // Update or insert candidate in Constituency-level Party
                    Map<Integer, Party> parties = constituency.getParties();
                    populateCandidate(caFirstName, caLastName, localityName, gender, caId, affId, parties);

                    // Update or insert candidate in each Authority-level Party
                    Map<String, Authority> authorities = constituency.getAuthorities();
                    for (Authority authority : authorities.values()) {
                        Map<Integer, Party> partyMap = authority.getAuthorityParties();
                        populateCandidate(caFirstName, caLastName, localityName, gender, caId, affId, partyMap);

                        Map<String, PollingStation> pollingStations = authority.getPollingStations();
                        for (PollingStation pollingStation : pollingStations.values()) {
                            Map<Integer, Party> pollingStationParties = pollingStation.getParties();
                            populateCandidate(caFirstName, caLastName, localityName, gender, caId, affId, pollingStationParties);
                        }
                    }
                }
            }
        }
    }

    private void populateCandidate(String caFirstName, String caLastName, String localityName, String gender, int caId, int affId, Map<Integer, Party> parties) {
        Party party = parties.get(affId);

        if (party != null) {
            List<Candidate> candidates = party.getCandidates();
            Candidate existingCandidate = null;

            for (Candidate candidate : candidates) {
                if (candidate.getId() == caId && candidate.getPartyId() == affId) {
                    existingCandidate = candidate;
                    break;
                }
            }

            if (existingCandidate != null) {
                existingCandidate.setFirstName(caFirstName);
                existingCandidate.setLastName(caLastName);
                existingCandidate.setGender(gender);
                existingCandidate.setLocalityName(localityName);
            } else {
                Candidate newCandidate = new Candidate();
                newCandidate.setId(caId);
                newCandidate.setFirstName(caFirstName);
                newCandidate.setLastName(caLastName);
                party.addCandidate(newCandidate);
            }
        }
    }

    public void registerProvinceConstituencies(String electionId) {
        Election election = elections.get(electionId);
        if (election == null) {
            System.err.println("[registerProvinceConstituencies] ❌ No election found for ID: '" + electionId + "'.");
            return;
        }

        Map<Integer, Constituency> constituencyMap = election.getConstituencies();
        if (constituencyMap == null || constituencyMap.isEmpty()) {
            System.out.println("[registerProvinceConstituencies] ⚠️ No constituencies found for election: " + electionId);
            return;
        }

        // Maak een nieuwe lijst voor de provinces, zodat we zeker een verse kopie hebben
        List<Province> updatedProvinces = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            Province province = new Province(i, getProvinceName(i));
            updatedProvinces.add(province);
        }

        for (Map.Entry<Integer, Integer> entry : DISTRICT_TO_PROVINCE_ID.entrySet()) {
            int constituencyId = entry.getKey();
            int provinceId = entry.getValue();

            Constituency constituency = constituencyMap.get(constituencyId);
            if (constituency == null) {
                System.out.println("[registerProvinceConstituencies] ⚠️ Constituency not found for ID: " + constituencyId);
                continue;
            }

            Province matchedProvince = updatedProvinces.stream()
                    .filter(p -> p.getId() == provinceId)
                    .findFirst()
                    .orElse(null);

            if (matchedProvince != null) {
                matchedProvince.getConstituencies().add(constituency);
                System.out.println("[registerProvinceConstituencies] ✅ Linked constituency " + constituencyId + " to province " + matchedProvince.getName());
            } else {
                System.err.println("[registerProvinceConstituencies] ❌ Province not found for ID: " + provinceId);
            }
        }

        // Zet de nieuwe, gekoppelde provinces in de election
        election.setProvinces(updatedProvinces);
    }

    private String getProvinceName(int id) {
        return switch (id) {
            case 1 -> "Drenthe";
            case 2 -> "Flevoland";
            case 3 -> "Friesland";
            case 4 -> "Gelderland";
            case 5 -> "Groningen";
            case 6 -> "Limburg";
            case 7 -> "Noord-Brabant";
            case 8 -> "Noord-Holland";
            case 9 -> "Overijssel";
            case 10 -> "Utrecht";
            case 11 -> "Zeeland";
            case 12 -> "Zuid-Holland";
            default -> "Onbekend";
        };
    }




    /**
     * This method is not needed since we now track elections by year.
     */
    @Override
    public Election retrieve() {
        return null; // Return null or logic to retrieve a specific election
    }

    /**
     * Get election data for a specific year.
     */
    public Election getElection(String year) {
        return elections.get(year);
    }

}
