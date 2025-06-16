package com.voteU.election.java.reader;

import com.voteU.election.java.model.*;
import com.voteU.election.java.utils.xml.DutchElectionProcessor;
import com.voteU.election.java.utils.xml.Transformer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * The DutchElectionTransformer class is responsible for transforming and registering various election-related data
 * into application-specific models. It performs operations such as associating candidates with parties,
 * converting raw data into structured models, and building all necessary relationships between the entities such as
 * elections, parties, constituencies, and candidates.
 *
 * This class also maintains an internal representation of elections data and handles mappings necessary for data processing.
 * It extends multiple parent classes and overrides their methods to achieve functionality specific to Dutch elections.
 */
@Slf4j
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
            Map.entry(12, 12),
            Map.entry(20, 13) // Bonaire, Caribisch Nederland
    );


    @Override
    public void registerElection(Map<String, String> electionData) {
        String electionId = electionData.get(DutchElectionProcessor.ELECTION_IDENTIFIER);
        String electionName = electionData.get(DutchElectionProcessor.ELECTION_NAME);
        String electionDate = electionData.get(DutchElectionProcessor.ELECTION_DATE);

        if (electionId == null || electionName == null || electionDate == null) {
            log.error("Incomplete election data: Missing ID, Name, or Date.");
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

    public void registerConstituency(Map<String, String> constituencyData, Map<Integer, Integer> affiliationVotes, Map<Integer, Map<Integer, Integer>> candidateVotes, Map<Integer, String> affiliationNames) {
        // Step 1: Extract required info
        String electionId = constituencyData.get(DutchElectionProcessor.ELECTION_IDENTIFIER);
        int contestId = Integer.parseInt(constituencyData.get(DutchElectionProcessor.CONTEST_IDENTIFIER));
        String contestName = constituencyData.get(DutchElectionProcessor.CONTEST_NAME);


        // Step 2: Retrieve election
        Election election = elections.get(electionId);
        if (election == null) {
            log.error("[registerConstituency] ❌ No election found for ID: '" + electionId + "'. Aborting.");
            return;
        }

        // Step 3: Get or create constituency map
        Map<Integer, Constituency> constituencyMap = election.getConstituencies();
        if (constituencyMap == null) {
            log.warn("[registerConstituency] ⚠️ Constituencies map was null. Initializing new map.");
            constituencyMap = new HashMap<>();
            election.setConstituencies(constituencyMap);
        } else {
            log.info("[registerConstituency] ✅ Found existing constituencies map with size: " + constituencyMap.size());
        }


        Constituency constituency = constituencyMap.computeIfAbsent(contestId, id -> new Constituency(id, contestName));
        constituency.setElectionId(electionId);
        // Step 4: Get or create the constituency
        int provinceId = DISTRICT_TO_PROVINCE_ID.get(contestId);
        constituency.setProvinceId(provinceId);

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
    public void registerNation(Map<String, String> votesData) {
        String electionId = votesData.get(DutchElectionProcessor.ELECTION_IDENTIFIER);
        Election election = elections.get(electionId);
        if (election == null) {
            log.error("❌ No election found for ID: '" + electionId + "'");
            return;
        }

        Map<Integer, Constituency> constituencies = election.getConstituencies();
        if (constituencies == null || constituencies.isEmpty()) {
            log.error("❌ No constituencies found for election ID: '" + electionId + "'");
            return;
        }

        // Accumulate national party votes
        Map<Integer, Integer> accumulatedVotes = new HashMap<>();
        Map<Integer, String> partyNames = new HashMap<>();

        // Map: partyId -> Map<candidateId, accumulatedVotes>
        Map<Integer, Map<Integer, Candidate>> accumulatedCandidates = new HashMap<>();

        for (Constituency constituency : constituencies.values()) {
            Map<Integer, Party> constituencyParties = constituency.getParties();
            if (constituencyParties == null) continue;

            for (Party party : constituencyParties.values()) {
                int partyId = party.getId();
                int votes = party.getVotes();
                String name = party.getName();

                accumulatedVotes.put(partyId, accumulatedVotes.getOrDefault(partyId, 0) + votes);
                partyNames.putIfAbsent(partyId, name);

                // Accumulate candidates for this party
                List<Candidate> candidates = party.getCandidates();
                if (candidates != null) {
                    Map<Integer, Candidate> partyCandidates = accumulatedCandidates.computeIfAbsent(partyId, k -> new HashMap<>());

                    for (Candidate candidate : candidates) {
                        int candidateId = candidate.getId();
                        int candidateVotes = candidate.getVotes();

                        Candidate accumulatedCandidate = partyCandidates.get(candidateId);
                        if (accumulatedCandidate == null) {
                            // Create new candidate with zero votes first, copy id and maybe name if needed
                            accumulatedCandidate = new Candidate();
                            accumulatedCandidate.setId(candidateId);
                            accumulatedCandidate.setPartyId(partyId);
                            accumulatedCandidate.setVotes(0);
                            // Optionally set candidate name if you have it
                            partyCandidates.put(candidateId, accumulatedCandidate);
                        }

                        // Accumulate votes
                        accumulatedCandidate.setVotes(accumulatedCandidate.getVotes() + candidateVotes);
                    }
                }
            }
        }

        // Build national party map with candidates
        Map<Integer, Party> nationalParties = new HashMap<>();
        int totalVotes = 0;
        for (Map.Entry<Integer, Integer> entry : accumulatedVotes.entrySet()) {
            int partyId = entry.getKey();
            int votes = entry.getValue();
            String name = partyNames.get(partyId);

            Party party = new Party(partyId, name);
            party.setVotes(votes);

            // Attach accumulated candidates list
            Map<Integer, Candidate> candidatesMap = accumulatedCandidates.get(partyId);
            if (candidatesMap != null) {
                party.setCandidates(new ArrayList<>(candidatesMap.values()));
            } else {
                party.setCandidates(new ArrayList<>());
            }

            nationalParties.put(partyId, party);
            totalVotes += votes;
        }

        // Update election
        election.setParties(nationalParties);
        election.setVotes(totalVotes);
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
            log.error("[registerAuthority] ❌ Constituency with ID " + contestId + " not found in election " + electionId);
            return;
        }

        // Get or create authority in constituency
        Map<String, Authority> authorityMap = constituency.getAuthorities();
        Authority authorityInMap = authorityMap.computeIfAbsent(authorityId, id -> {
            Authority authority = new Authority(id);
            authority.setName(authorityName);
            authority.setConstituencyId(contestId);
            authority.setElectionId(electionId);
            return authority;
        });

        // Register party votes under authority
        Map<Integer, Party> partyMap = authorityInMap.getParties();
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
            log.error("[registerPollingStation] ❌ Constituency with ID " + contestId + " not found in election " + electionId);
            return;
        }

        // Get the authority inside the constituency
        Map<String, Authority> authorityMap = constituency.getAuthorities();
        Authority authority = authorityMap.get(authorityId);
        if (authority == null) {
            log.error("[registerPollingStation] ❌ Authority with ID " + authorityId + " not found in constituency " + contestId);
            return;
        }

        // Get or create polling station inside authority
        Map<String, PollingStation> pollingStationMap = authority.getPollingStations();
        PollingStation pollingStation = pollingStationMap.computeIfAbsent(pollingStationId, id -> {
            PollingStation ps = new PollingStation(pollingStationId,pollingStationName, zipcode);
            ps.setAuthorityId(authorityId);
            ps.setElectionId(electionId);
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
                    candidate.setElectionId(electionId);
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
                Map<Integer, Party> electionParties = election.getParties();
                populateCandidate(caFirstName, caLastName, localityName, gender, caId, affId, electionParties, electionId);

                Map<Integer, Constituency> constituencies = election.getConstituencies();
                Constituency constituency = constituencies.get(contestId);

                if (constituency != null) {
                    // Update or insert candidate in Constituency-level Party
                    Map<Integer, Party> parties = constituency.getParties();
                    populateCandidate(caFirstName, caLastName, localityName, gender, caId, affId, parties, electionId);

                    // Update or insert candidate in each Authority-level Party
                    Map<String, Authority> authorities = constituency.getAuthorities();
                    for (Authority authority : authorities.values()) {
                        Map<Integer, Party> partyMap = authority.getParties();
                        populateCandidate(caFirstName, caLastName, localityName, gender, caId, affId, partyMap, electionId);

                        Map<String, PollingStation> pollingStations = authority.getPollingStations();
                        for (PollingStation pollingStation : pollingStations.values()) {
                            Map<Integer, Party> pollingStationParties = pollingStation.getParties();
                            populateCandidate(caFirstName, caLastName, localityName, gender, caId, affId, pollingStationParties, electionId);
                        }
                    }
                }
            }
        }
    }

    private void populateCandidate(String caFirstName, String caLastName, String localityName, String gender, int caId, int affId, Map<Integer, Party> parties, String electionId) {
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
                existingCandidate.setElectionId(electionId);
            } else {
                Candidate newCandidate = new Candidate();
                newCandidate.setId(caId);
                newCandidate.setFirstName(caFirstName);
                newCandidate.setLastName(caLastName);
                newCandidate.setGender(gender);
                newCandidate.setLocalityName(localityName);
                newCandidate.setPartyId(affId);
                newCandidate.setElectionId(electionId);

                party.addCandidate(newCandidate);
            }
        }
    }

    public void registerProvinceConstituencies(String electionId) {
        Election election = elections.get(electionId);
        if (election == null) {
            log.error("[registerProvinceConstituencies] ❌ No election found for ID: '" + electionId + "'.");
            return;
        }

        Map<Integer, Constituency> constituencyMap = election.getConstituencies();
        if (constituencyMap == null || constituencyMap.isEmpty()) {
            return;
        }

        // Maak een nieuwe lijst voor de provinces, zodat we zeker een verse kopie hebben
        List<Province> updatedProvinces = new ArrayList<>();
        for (int i = 1; i <= 13; i++) {
            Province province = new Province(i, getProvinceName(i));
            updatedProvinces.add(province);
        }

        for (Map.Entry<Integer, Integer> entry : DISTRICT_TO_PROVINCE_ID.entrySet()) {
            int constituencyId = entry.getKey();
            int provinceId = entry.getValue();

            Constituency constituency = constituencyMap.get(constituencyId);
            if (constituency == null) {
                continue;
            }

            Province matchedProvince = updatedProvinces.stream()
                    .filter(p -> p.getId() == provinceId)
                    .findFirst()
                    .orElse(null);

            if (matchedProvince != null) {
                matchedProvince.getConstituencies().add(constituency);
            } else {
                log.error("[registerProvinceConstituencies] ❌ Province not found for ID: " + provinceId);
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
            case 13 -> "Caribisch-Nederland";
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
