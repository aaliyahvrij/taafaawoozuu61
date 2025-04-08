package com.voteU.election.java.utils.xml;


import com.voteU.election.java.model.Election;

import java.util.Map;

/**
 * A {@link Transformer} transforms the election information which is contained in a {@link Map},
 * Map&lt;String, String>, into the models as used by the application. As part of this transformation it is also
 * responsible for making all the needed relationships between the different classes and instances.<br>
 * For example when {@link #registerCandidate(Map)} is called, it must add the candidate to the correct Affiliation(party).<br>
 * After all the data has been transformed it should be able to return an instance of a class that encapsulates all
 * the data in the application specific data classes.
 * @param <E>
 */
public interface Transformer<E> {
    /**
     * Called once per file with the information about the election. The parameter {@code electionData} (should) hold
     * the following information:
     * <ul>
     *     <li>{@link DutchElectionProcessor#ELECTION_IDENTIFIER}</li>
     *     <li>{@link DutchElectionProcessor#ELECTION_NAME}</li>
     *     <li>{@link DutchElectionProcessor#ELECTION_CATEGORY}</li>
     *     <li>{@link DutchElectionProcessor#ELECTION_DATE}</li>
     * </ul>
     *
     * @param electionData a {@code Map} containing the information as {@code String}'s.
     * @return
     */
    void registerElection(Map<String, String> electionData);

    /**
     * Called once per file with information about the contest. The {@code contestData} can hold the
     * same information as {@code electionData} in {@link #registerElection(Map)}, and the following information:
     * <ul>
     *     <li>{@link DutchElectionProcessor#CONTEST_IDENTIFIER}</li>
     *     <li>{@link DutchElectionProcessor#CONTEST_NAME}</li>
     * </ul>
     * @param contestData a {@code Map} containing information about the election and the contest.
     */
    void registerContest(Map<String, String> contestData);

    /**
     * Called multiple times per file with information about an affiliation. The {@code affiliationData} can hold the
     * same information as {@code contestData} in {@link #registerContest(Map)}, and the following information:
     * <ul>
     *     <li>{@link DutchElectionProcessor#AFFILIATION_IDENTIFIER}</li>
     *     <li>{@link DutchElectionProcessor#REGISTERED_NAME}</li>
     * </ul>
     * @param affiliationData a {@code Map} containing information about the election, contest and the affiliation.
     */
    void registerAffiliation(Map<String, String> affiliationData);

    /**
     * Called multiple times per file with information about a candidate. The {@code candidateData} can hold the
     * same information as {@code affiliationData} in {@link #registerAffiliation(Map)}, and the following information:
     * <ul>
     *     <li>{@link DutchElectionProcessor#CANDIDATE_IDENTIFIER}</li>
     *     <li>{@link DutchElectionProcessor#INITIALS}</li>
     *     <li>{@link DutchElectionProcessor#FIRST_NAME}</li>
     *     <li>{@link DutchElectionProcessor#LAST_NAME_PREFIX}</li>
     *     <li>{@link DutchElectionProcessor#LAST_NAME}</li>
     * </ul>
     * @param candidateData a {@code Map} containing information about the election, contest, affiliation and the
     *                     candidate.
     */
    void registerCandidate(Map<String, String> candidateData);

    /**
     * Called multiple times per file with information about a candidate. The {@code votesData} can hold the
     * same information as {@code candidateData} in {@link #registerCandidate(Map)}, and the following information:
     * <ul>
     *     <li>{@link DutchElectionProcessor#VALID_VOTES}</li>
     * </ul>
     * @param votesData a {@code Map} containing information about the election, contest, affiliation, candidate and
     *                  the number of votes.
     */
    void registerVotes(Map<String, String> votesData);

    /**
     * Returns an instance that encapsulates all the registered data about the election.
     * @return an instance that encapsulates all the data for an election.
     */
    E retrieve();
}
