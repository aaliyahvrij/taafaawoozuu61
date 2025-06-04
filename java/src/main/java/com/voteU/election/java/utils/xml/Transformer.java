package com.voteU.election.java.utils.xml;

import com.voteU.election.java.models.Affiliation;

import java.util.*;

/**
 * A {@link Transformer} transforms the election information which is contained in a {@link HashMap},
 * HashMap&lt;String, String>, into the models as used by the application. As part of this transformation,
 * it is also responsible for making all the necessary relationships between the different classes and instances.<br>
 * For example, when {@link #registerCandiLevelData(HashMap)} is called,
 * it must add the candidate to the correct Affiliation(party).<br>
 * After all the data has been transformed, it should be able to return an instance of a class that encapsulates
 * all the data in the application-specific data classes.
 *
 * @param <E>
 */
public interface Transformer<E> {
    /**
     * Called once per file with the information about the election.
     * The parameter {@code electionMap} (should) hold the following information:
     * <ul>
     *     <li>{@link ElectionProcessor#ELECTION_ID}</li>
     *     <li>{@link ElectionProcessor#ELECTION_NAME}</li>
     *     <li>{@link ElectionProcessor#ELECTION_CATEGORY}</li>
     *     <li>{@link ElectionProcessor#ELECTION_DATE}</li>
     * </ul>
     *
     * @param electionMap a {@code HashMap} containing the information as {@code String}'s.
     */
    void registerElectoralLevelData(HashMap<String, String> electionMap);

    /**
     * Called multiple times per file with information about a candidate. The {@code nationMap} can hold the
     * same information as {@code candiMap} in {@link #registerCandiLevelData(HashMap)},
     * and the following information:
     * <ul>
     *     <li>{@link ElectionProcessor#VV_COUNT}</li>
     * </ul>
     *
     * @param nationMap a {@code HashMap} containing information about the election, constituency, affiliation,
     *                  candidate and vote count.
     */
    void registerNationalLevelData(HashMap<String, String> nationMap);

    /**
     * Called once per file with information about the constituency. The {@code constiMap} can hold the
     * same information as {@code electionMap} in {@link #registerElectoralLevelData(HashMap)},
     * and the following information:
     * <ul>
     *     <li>{@link ElectionProcessor#CONSTI_ID}</li>
     *     <li>{@link ElectionProcessor#CONSTI_NAME}</li>
     * </ul>
     *
     * @param constiMap a {@code HashMap} containing information about the election and constituency.
     */
    void registerConstiLevelData(HashMap<String, String> constiMap, List<String> affiNamesList, List<Integer> affiVotesList, Map<Integer, Map<Integer, Integer>> candiVotesMap);

    void registerMuniLevelData(HashMap<String, String> muniMap);

    void registerPollingStationLevelData(HashMap<String, String> polingStationMap, LinkedHashMap<Integer, Affiliation> pollingStationLevel_affiListMap);

    /**
     * Called multiple times per file with information about a candidate.
     * <ul>
     *     <li>{@link ElectionProcessor#CANDI_ID}</li>
     *     <li>{@link ElectionProcessor#INITIALS}</li>
     *     <li>{@link ElectionProcessor#FIRST_NAME}</li>
     *     <li>{@link ElectionProcessor#LAST_NAME_PREFIX}</li>
     *     <li>{@link ElectionProcessor#LAST_NAME}</li>
     * </ul>
     *
     * @param candiMap a {@code HashMap} containing information about the election, constituency,
     *                 affiliation and the candidate.
     */
    void registerCandiLevelData(HashMap<String, String> candiMap);

    void registerCandiLevel_constiData(HashMap<String, String> constiMap);
}
