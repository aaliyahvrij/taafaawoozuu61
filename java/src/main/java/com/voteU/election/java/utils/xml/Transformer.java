package com.voteU.election.java.utils.xml;

import com.voteU.election.java.model.Affiliation;

import java.util.*;

/**
 * A {@link Transformer} transforms the election information which is contained in a {@link Map},
 * Map&lt;String, String>, into the models as used by the application. As part of this transformation, it is also
 * responsible for making all the necessary relationships between the different classes and instances.<br>
 * For example, when {@link #registerCandidate(Map)} is called, it must add the candidate to the correct Affiliation(party).<br>
 * After all the data has been transformed, it should be able to return an instance of a class that encapsulates all
 * the data in the application-specific data classes.
 *
 * @param <E>
 */
public interface Transformer<E> {
    /**
     * Called once per file with the information about the election. The parameter {@code electionData} (should) hold
     * the following information:
     * <ul>
     *     <li>{@link DutchElectionProcessor#ELECTION_ID}</li>
     *     <li>{@link DutchElectionProcessor#ELECTION_NAME}</li>
     *     <li>{@link DutchElectionProcessor#ELECTION_CATEGORY}</li>
     *     <li>{@link DutchElectionProcessor#ELECTION_DATE}</li>
     * </ul>
     *
     * @param electionMap a {@code Map} containing the information as {@code String}'s.
     */
    void registerElection(Map<String, String> electionMap);

    /**
     * Called multiple times per file with information about a candidate. The {@code nationData} can hold the
     * same information as {@code candiData} in {@link #registerCandidate(Map)}, and the following information:
     * <ul>
     *     <li>{@link DutchElectionProcessor#VALID_VOTES}</li>
     * </ul>
     *
     * @param nationLevel_affiMap a {@code Map} containing information about the election, constituency, affiliation,
     *                   candidate and the number of votes.
     */
    void registerNation(Map<String, String> nationLevel_affiMap);

    /**
     * Called once per file with information about the contest. The {@code contestData} can hold the
     * same information as {@code electionData} in {@link #registerElection(Map)}, and the following information:
     * <ul>
     *     <li>{@link DutchElectionProcessor#CONTEST_ID}</li>
     *     <li>{@link DutchElectionProcessor#CONTEST_NAME}</li>
     * </ul>
     *
     * @param constiMap a {@code Map} containing information about the election and the contest.
     */
    void registerConstituency(Map<String, String> constiMap);

    void registerAuthority(Map<String, String> authorityMap);

    void registerRepUnit(Map<String, String> repUnitMap, Map<Integer, Affiliation> repUnitMap_affiliations);

    /**
     * Called multiple times per file with information about a candidate.
     * <ul>
     *     <li>{@link DutchElectionProcessor#CANDIDATE_ID}</li>
     *     <li>{@link DutchElectionProcessor#INITIALS}</li>
     *     <li>{@link DutchElectionProcessor#FIRST_NAME}</li>
     *     <li>{@link DutchElectionProcessor#LAST_NAME_PREFIX}</li>
     *     <li>{@link DutchElectionProcessor#LAST_NAME}</li>
     * </ul>
     *
     * @param candiMap a {@code Map} containing information about the election, contest, affiliation and the
     *                  candidate.
     */
    void registerCandidate(Map<String, String> candiMap);
}
