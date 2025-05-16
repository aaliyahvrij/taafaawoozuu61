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
    
        @Override
        public void registerElection(Map<String, String> electionData) {
            String electionId = electionData.get(DutchElectionProcessor.ELECTION_ID);
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
            // System.out.println(electionData);
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
            String partyIdStr = votesData.get(DutchElectionProcessor.AFFILIATION_ID);
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
            String electionId = votesData.get(DutchElectionProcessor.ELECTION_ID);
            Election election = elections.get(electionId);
            Map<Integer, Party> partyMap = election.getAffilations();
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
                String candidateId = votesData.get(DutchElectionProcessor.CANDIDATE_ID);
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
    
            // Ensure only the number of registered parties is logged, not each time for each party
            // This logging happens once at the end, after all votes are processed.
    
        }


        public void registerConstituency(
                Map<String, String> constituencyData,
                Map<Integer, Integer> affiliationVotes,
                Map<Integer, Map<Integer, Integer>> candidateVotes,
                Map<Integer, String> affiliationNames
        ) {
            // Step 1: Extract required info
            String electionId = constituencyData.get(DutchElectionProcessor.ELECTION_ID);
            int contestId = Integer.parseInt(constituencyData.get(DutchElectionProcessor.CONTEST_ID));
            String contestName = constituencyData.get(DutchElectionProcessor.CONTEST_NAME);

            System.out.println("[registerConstituency] ➤ Starting for contestId: " + contestId + ", contestName: " + contestName);
            System.out.println("[registerConstituency] ➤ Election ID: " + electionId);

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
            System.out.println("[registerConstituency] ➕ Constituency " + (isNew ? "created" : "updated") + " for contestId: " + contestId);

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
                    candidate.setAffId(partyId);

                    candidates.add(candidate);
                }

                party.setCandidates(candidates);
                parties.put(partyId, party);
                System.out.println("[registerConstituency]   ↳ Registered partyId " + partyId + " with " + totalVotes + " votes and " + candidates.size() + " candidates.");
            }

            // Step 6: Set the parties to the constituency and store it back
            constituency.setParties(parties);
            constituencyMap.put(contestId, constituency);

            System.out.println("[registerConstituency] ✅ Constituency " + contestId + " registered with " + parties.size() + " parties.");
            System.out.println("[registerConstituency] 📊 Total constituencies in election '" + electionId + "': " + constituencyMap.size());
        }


        @Override
        public void registerAuthority(Map<String, String> authorityData) {
            String electionId = authorityData.get(DutchElectionProcessor.ELECTION_ID);
            String contestIdStr = authorityData.get(DutchElectionProcessor.CONTEST_ID);
            String authorityId = authorityData.get(DutchElectionProcessor.AUTHORITY_ID);
            String partyIdStr = authorityData.get(DutchElectionProcessor.AFFILIATION_ID);
            String partyName = authorityData.getOrDefault(DutchElectionProcessor.REGISTERED_NAME, "UNKNOWN");
            String authorityName = authorityData.get(DutchElectionProcessor.AUTHORITY_NAME);
            boolean isTotalVotes = "GEMEENTE".equals(authorityData.get("Source"));
            if (electionId == null || contestIdStr == null || authorityId == null || partyIdStr == null)
                return;
    
            int contestId, partyId;
            try {
                contestId = Integer.parseInt(contestIdStr);
                partyId = Integer.parseInt(partyIdStr);
            } catch (NumberFormatException e) {
                return;
            }
    
            Election election = elections.get(electionId);
            if (election == null) return;
    
    
            Map<String, Authority> authorityMap = election.getAuthorities();
            Authority authorityInMap = authorityMap.computeIfAbsent(authorityId, id -> {
                Authority authority = new Authority(id);
                authority.setName(authorityName);
                authority.setConstituencyId(contestId);
                return authority;
            });
    
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
                    int totalVotes = 0;
                    for (Party p : partyMap.values()) {
                        totalVotes += p.getVotes();
                    }
                    authorityInMap.setVotes(totalVotes);

                } catch (NumberFormatException ignored) {
                }
            }
            if (authorityData.containsKey("CandidateVotes") && party != null && isTotalVotes) {
                try {
                    int candidateId = Integer.parseInt(authorityData.get(DutchElectionProcessor.CANDIDATE_ID));
                    int candidateVotes = Integer.parseInt(authorityData.get("CandidateVotes"));
    
                    if (!party.hasCandidateId(candidateId)) {
                        Candidate candidate = new Candidate();
                        candidate.setId(candidateId);
                        candidate.setVotes(candidateVotes);
                        candidate.setAffId(partyId);
                        party.addCandidate(candidate);
                    }
                } catch (NumberFormatException | NullPointerException ignored) {
                }
            }
            Constituency constituency = election.getConstituencies().get(contestId);
            if (constituency != null) {
                Map<String, Authority> authorities = constituency.getAuthorities();
                authorities.put(authorityId, authorityInMap);
            }
        }
    
    
    
        @Override
        public void registerCandidate(Map<String, String> candidateData) {
            String caIdStr = candidateData.get(DutchElectionProcessor.CANDIDATE_ID);
            String caFirstName = candidateData.get(DutchElectionProcessor.FIRST_NAME);
            String caLastName = candidateData.get(DutchElectionProcessor.LAST_NAME);
            String localityName = candidateData.get(DutchElectionProcessor.LOCALITY_NAME);
            String gender = candidateData.get(DutchElectionProcessor.GENDER);
            String electionId = candidateData.get(DutchElectionProcessor.ELECTION_ID);
            String contestIdStr = candidateData.get(DutchElectionProcessor.CONTEST_ID);
            String affIdStr = candidateData.get(DutchElectionProcessor.AFFILIATION_ID);
    
            if (caIdStr != null && caLastName != null
                    && electionId != null && contestIdStr != null && affIdStr != null) {
    
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
                    if (candidate.getId() == caId && candidate.getAffId() == affId) {
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
