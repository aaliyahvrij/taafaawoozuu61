package com.voteU.election.java.utils.xml;

import com.voteU.election.java.models.Affiliation;

import java.util.List;
import java.util.LinkedHashMap;

/**
 * A {@link Transformer} transforms the election information which is contained in a {@link LinkedHashMap},
 * LinkedHashMap&lt;String, String>, into the models as used by the application. As part of this transformation,
 * it is also responsible for making all the necessary relationships between the different classes and instances.<br>
 * For example, when {@link #registerCandiLevelData(LinkedHashMap)} is called,
 * it must add the candidate to the correct affiliation.<br>
 * After all the data has been transformed, it should be able to return an instance of a class
 * that encapsulates all the data in the application-specific data classes.
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
     *     <li>{@link ElectionProcessor#ELECTION_DATE}</li>
     * </ul>
     *
     * @param electionMap a {@code LinkedHashMap} containing the information as {@code String}'s.
     */
    void registerElectoralLevelData(LinkedHashMap<String, String> electionMap);

    /**
     * Called multiple times per file with information about a candidate. The {@code nationMap} can hold
     * the same information as {@code candiLhMap} in {@link #registerCandiLevelData(LinkedHashMap)},
     * and the following information:
     * <ul>
     *     <li>{@link ElectionProcessor#VV_COUNT}</li>
     * </ul>
     *
     * @param nationMap a {@code LinkedHashMap} containing information about the election,
     *                  constituency, affiliation, candidate and valid vote count.
     */
    void registerNationalLevel_affiData(LinkedHashMap<String, String> nationMap);

    /**
     * Called once per file with information about the constituency. The {@code constiLhMap} can hold
     * the same information as {@code electionMap} in {@link #registerElectoralLevelData(LinkedHashMap)},
     * and the following information:
     * <ul>
     *     <li>{@link ElectionProcessor#CONSTI_ID}</li>
     *     <li>{@link ElectionProcessor#CONSTI_NAME}</li>
     * </ul>
     *
     * @param constiLhMap a {@code LinkedHashMap} containing information about the election and constituency.
     */
    void registerConstiLevel_affiData(LinkedHashMap<String, String> constiLhMap, List<String> affiNameList, List<Integer> affiVVCountList, LinkedHashMap<Integer, LinkedHashMap<Integer, Integer>> candiLhMap);

    void registerMuniLevel_affiData(LinkedHashMap<String, String> muniLhMap);

    void registerPoStLevelData(LinkedHashMap<String, String> poStLhMap, LinkedHashMap<Integer, Affiliation> poStLevel_affiList_lhMap);

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
     * @param candiLhMap a {@code LinkedHashMap} containing information about
     *                 the election, constituency, affiliation and candidate.
     */
    void registerCandiLevelData(LinkedHashMap<String, String> candiLhMap);

    void registerCandiLevel_constiData(LinkedHashMap<String, String> constiLhMap);
}
