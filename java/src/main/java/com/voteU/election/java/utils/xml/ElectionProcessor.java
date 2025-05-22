package com.voteU.election.java.utils.xml;

import com.voteU.election.java.model.Candidate;
import com.voteU.election.java.model.Affiliation;
import com.voteU.election.java.utils.PathUtils;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Logger;

/**
 * Processes the XML data files for the Dutch elections. It is completely model agnostic. This means that it
 * doesn't have any knowledge of the data model that is being used by the application.
 * All the datamodel-specific logic must be provided by a separate class that implements
 * the {@link Transformer} interface.<br>
 * At its current state it processes the files in a two-step process. First it constructs the 'kieskringen' and
 * the 'kieslijsten'. Secondly, it processes the vote counts. It behaves similar as the
 * <a href="https://www.baeldung.com/java-visitor-pattern">visitor pattern</a>.<br>
 * The full dataset consists of three types of files.
 * <ol>
 *     <li>one file per 'kieskring' containing the 'kieslijsten'</li>
 *     <li>one file per 'kieskring' containing the total votes within that 'kieskring' per candidate</li>
 *     <li>one file per municipality containing per reporting unit, the votes per candidate</li>
 * </ol>
 * <em>When processing the files only the first and third type of files are processed by this implementation at this
 * moment, but can be changed if needed!</em><br>
 * <br>
 * The data in the XML files has a more or less hierarchy structure. When a method of the transformer is called, a
 * {@link Map} containing all the information on that level, including the information at the higher levels,
 * is provided. The {@link Map} is specified as: Map&lt;String, String>. It is up to the transformer to convert any
 * numerical information from its {@link String} representation into its appropriate datatype.<br>
 * <br>
 * <em>It assumes that filenames have NOT been changed and that the content has not been altered!
 * The code assumes that there is no whitespace between the open and closing tags.</em><br>
 * <br>
 * Here is an example of how this class could be used.
 * <pre>
 *     DutchElectionTransformer creator = new DutchElectionTransformer();
 *     DutchElectionProcessor<Election> electionProcessor = new DutchElectionProcessor<>(creator);
 *     Election election = electionProcessor.processResults("TK2023", PathUtils.getResourcePath("/EML_bestanden_TK2023_HvA_UvA"));
 * </pre>
 * <br>
 * <em>You are encouraged to alter this class so it suits your needs! :-)</em>
 */
public class ElectionProcessor<E> {
    private static final Logger LOG = Logger.getLogger(ElectionProcessor.class.getName());
    private final Transformer<E> transformer;

    // Common attribute name that is use on multiple tags.
    public static final String ID = "Id";
    public static final String SHORT_CODE = "ShortCode";

    /*
     The tag names on the election level within the XML files, which are also used as keys in the maps
     when calling the methods of the transformer.
     */
    public static final String ELECTION = "Election";
    public static final String ELECTION_ID = "ElectionIdentifier";
    public static final String ELECTION_NAME = "ElectionName";
    public static final String ELECTION_CATEGORY = "ElectionCategory";
    public static final String ELECTION_DATE = "ElectionDate";
    public static final String TOTAL_VOTES = "TotalVotes";

    /*
     The tag names on the contest level within the XML files, which are also used as keys in the maps
     when calling the methods of the transformer.
     */
    public static final String CONTEST = "Contest";
    public static final String CONTEST_ID = "ContestIdentifier";
    public static final String CONTEST_NAME = "ContestName";

    /*
     The tag names on the authority level within the XML files, which are also used as keys in the maps
     when calling the methods of the transformer.
     */
    public static final String MANAGING_AUTHORITY = "ManagingAuthority";
    public static final String AUTHORITY_ID = "AuthorityIdentifier";

    /*
     The tag names on the reporting unit level within the XML files, which are also used as keys in the maps
     when calling the methods of the transformer.
     */
    public static final String REP_UNIT_VOTES = "ReportingUnitVotes";
    public static final String REP_UNIT_ID = "ReportingUnitIdentifier";
    public static final String SELECTION = "Selection";
    public static final String VALID_VOTES = "ValidVotes";
    public static final String ZIPCODE = "ZipCode"; // For convenience, is used as a key in the data-maps.

    /*
     The tag names on the affiliation level within the XML files, which are also used as keys in the maps
     when calling the methods of the transformer.
     */
    public static final String AFFILIATION = "Affiliation";
    public static final String AFFI_ID = "AffiliationIdentifier";
    public static final String AFFI_NAME = "RegisteredName";

    /*
     The tag names on the candidate level within the XML files, which are also used as keys in the maps
     when calling the methods of the transformer.
     */
    public static final String CANDIDATE = "Candidate";
    public static final String CANDI_ID = "CandidateIdentifier";
    public static final String PERSON_NAME = "PersonName";
    public static final String NAME_LINE = "NameLine";
    public static final String INITIALS = "Initials"; // For convenience, is used as a key in the data-maps.
    public static final String FIRST_NAME = "FirstName";
    public static final String LAST_NAME_PREFIX = "NamePrefix";
    public static final String LAST_NAME = "LastName";
    public static final String GENDER = "Gender";
    //public static final String QUALIFYING_ADDRESS = "QualifyingAddress";
    //public static final String LOCALITY = "Locality";
    public static final String LOCALITY_NAME = "LocalityName";

    // Used internally
    private static final String NAME_TYPE = "NameType";

    /**
     * Creates a new instance that will use the provided transformer for transforming the data into the
     * application-specific models.
     *
     * @param transformer the {@link Transformer} that will take care of transforming the data into the
     *                    application-specific models.
     */
    public ElectionProcessor(Transformer<E> transformer) {
        this.transformer = transformer;
    }

    /**
     * Traverses all the folders within the specified folder and calls the appropriate methods of the transformer.
     * While processing the files, it will skip any file that has a different election-id than the one specified.
     * Currently, it only processes the files containing the 'kieslijsten' and the votes per reporting unit.
     * <pre>
     * NOTE: It assumes that there are <b>NO</b> whitespace characters between the tags other than within text values!
     * </pre>
     *
     * @param electionId the identifier for the of the files that should be processed, for example, <i>TK2023</i>.
     * @param folderName The name of the folder that contains the files containing the election data.
     * @throws IOException        in case something goes wrong while reading the file.
     * @throws XMLStreamException when a file has not the expected format. One example is a file that has been formatted
     *                            for better readability.
     */
    public void processResults(String electionId, String folderName) throws IOException, XMLStreamException {
        LOG.info("Loading election data from %s".formatted(folderName));
        Map<String, String> electionMap = new HashMap<>();
        electionMap.put(ELECTION_ID, electionId);
        for (Path totalVotesFile : PathUtils.findFilesToScan(folderName, "Totaaltelling_%s.eml.xml".formatted(electionId))) {
            LOG.fine("Found: %s".formatted(totalVotesFile));
            XMLParser parser = new XMLParser(new FileInputStream(totalVotesFile.toString()));
            processElection(electionMap, parser);
            processNationalLevelData(electionMap, parser);
        }
        for (Path constiFile : PathUtils.findFilesToScan(folderName, "Telling_%s_kieskring_".formatted(electionId))) {
            LOG.fine("Found: %s".formatted(constiFile));
            System.out.println(folderName + constiFile.toString());
            XMLParser parser = new XMLParser(new FileInputStream(constiFile.toString()));
            processElection(electionMap, parser);
            processConstiOrAuthoLevelData(electionMap, parser, "constituency");
        }
        for (Path authoFile : PathUtils.findFilesToScan(folderName, "Telling_%s_gemeente_".formatted(electionId))) {
            System.out.println(folderName + authoFile.toString());
            XMLParser parser = new XMLParser(new FileInputStream(authoFile.toString()));
            processElection(electionMap, parser);
            processConstiOrAuthoLevelData(electionMap, parser, "authority");
        }
        for (Path candiFile : PathUtils.findFilesToScan(folderName, "Kandidatenlijsten_%s_".formatted(electionId))) {
            LOG.fine("Found: %s".formatted(candiFile));
            XMLParser parser = new XMLParser(new FileInputStream(candiFile.toString()));
            processElection(electionMap, parser);
            processCandiLevel_ConstiData(electionMap, parser);
        }
    }

    private void processElection(Map<String, String> electionMap, XMLParser parser) throws XMLStreamException {
        if (parser.findBeginTag(MANAGING_AUTHORITY)) {
            if (parser.findBeginTag(AUTHORITY_ID)) {
                String authoId = parser.getAttributeValue(null, "Id");
                electionMap.put(AUTHORITY_ID, authoId);
                String authoName = parser.getElementText();
                electionMap.put("AuthorityName", authoName);
                parser.findAndAcceptEndTag(AUTHORITY_ID);
            }
            parser.findAndAcceptEndTag(MANAGING_AUTHORITY);
        }
        if (parser.findBeginTag(ELECTION_ID)) {
            String expectedElectionId = electionMap.get(ELECTION_ID);
            String electionId = parser.getAttributeValue(null, ID);
            if (electionMap.containsKey(ELECTION_ID) && expectedElectionId.equals(electionId)) {
                if (parser.findBeginTag(ELECTION_NAME)) {
                    String electionName = parser.getElementText();
                    electionMap.put(ELECTION_NAME, electionName);
                    parser.findAndAcceptEndTag(ELECTION_NAME);
                }
                if (parser.findBeginTag(ELECTION_CATEGORY)) {
                    String electionCategory = parser.getElementText();
                    electionMap.put(ELECTION_CATEGORY, electionCategory);
                    parser.findAndAcceptEndTag(ELECTION_CATEGORY);
                }
                if (parser.findBeginTag(ELECTION_DATE)) {
                    String electionDate = parser.getElementText();
                    electionMap.put(ELECTION_DATE, electionDate);
                    parser.findAndAcceptEndTag(ELECTION_DATE);
                }
                for (Map.Entry<String, String> electionMapPair : electionMap.entrySet()) {
                    if (electionMapPair.getValue() == null) {
                        System.err.println("Missing " + electionMapPair.getKey() + " in electionMap: " + electionMap);
                        return;
                    }
                }
                transformer.registerElection(electionMap);
                parser.findAndAcceptEndTag(ELECTION_ID);
            } else {
                LOG.warning("The %s %s does not match the expected identifier %s".formatted(ELECTION_ID, electionId, expectedElectionId));
                parser.findAndAcceptEndTag(ELECTION);
            }
        }
    }

    private void processNationalLevelData(Map<String, String> constiMap, XMLParser parser) throws XMLStreamException {
        if (parser.findBeginTag(TOTAL_VOTES)) {
            int affId = 0;
            Set<Integer> registeredAffIds = new HashSet<>();
            Set<String> registeredCandiShortCodes = new HashSet<>();
            if (parser.findBeginTag(SELECTION)) {
                while (parser.getLocalName().equals(SELECTION)) {
                    parser.nextTag();
                    switch (parser.getLocalName()) {
                        case AFFI_ID:
                            Map<String, String> affiMap = new HashMap<>(constiMap);
                            affId = parser.getIntegerAttributeValue(null, ID, 0);
                            if (registeredAffIds.contains(affId)) {
                                parser.findAndAcceptEndTag(AFFI_ID);
                                continue;
                            }
                            affiMap.put(AFFI_ID, String.valueOf(affId));
                            if (parser.findBeginTag(AFFI_NAME)) {
                                String affiName = parser.getElementText();
                                System.out.println("nationMap - Found an affiliation name: " + affiName);
                                affiMap.put(AFFI_NAME, affiName);
                                parser.findAndAcceptEndTag(AFFI_NAME);
                            }
                            parser.findAndAcceptEndTag(AFFI_ID);
                            if (parser.findBeginTag(VALID_VOTES)) {
                                int affiVotes = Integer.parseInt(parser.getElementText());
                                affiMap.put(VALID_VOTES, String.valueOf(affiVotes));
                                parser.findAndAcceptEndTag(VALID_VOTES);
                            }
                            registeredAffIds.add(affId);
                            for (Map.Entry<String, String> affiMapPair : affiMap.entrySet()) {
                                if (affiMapPair.getValue() == null) {
                                    System.err.println("National level - Missing " + affiMapPair.getKey() + " in affiMap: " + affiMap);
                                    return;
                                } else if (affiMapPair.getKey().equals(AFFI_ID) || affiMapPair.getKey().equals(VALID_VOTES)) {
                                    try {
                                        Integer.parseInt(affiMapPair.getValue());
                                    } catch (NumberFormatException e) {
                                        System.err.println("National level - Invalid " + affiMapPair.getKey() + " value '" + affiMapPair.getValue() + "' in affiMap: " + affiMap);
                                        return;
                                    }
                                }
                            }
                            transformer.registerNationalLevelData(affiMap);
                            break;
                        case CANDIDATE:
                            String candiShortCode = null;
                            if (parser.findBeginTag(CANDI_ID)) {
                                candiShortCode = parser.getAttributeValue(null, SHORT_CODE);
                                if (registeredCandiShortCodes.contains(candiShortCode)) {
                                    parser.findAndAcceptEndTag(CANDI_ID);
                                    continue;
                                }
                                parser.findAndAcceptEndTag(CANDI_ID);
                            }
                            parser.findAndAcceptEndTag(CANDIDATE);
                            if (parser.findBeginTag(VALID_VOTES)) {
                                int candiVotes = Integer.parseInt(parser.getElementText());
                                Map<String, String> candiMap = new HashMap<>(constiMap);
                                candiMap.put(SHORT_CODE, candiShortCode);
                                registeredCandiShortCodes.add(candiShortCode);
                                candiMap.put(AFFI_ID, String.valueOf(affId));
                                candiMap.put("CandiVotes", String.valueOf(candiVotes));
                                for (Map.Entry<String, String> candiMapPair : candiMap.entrySet()) {
                                    if (candiMapPair.getValue() == null) {
                                        System.err.println("National level - Missing " + candiMapPair.getKey() + " in candiMap: " + candiMap);
                                        return;
                                    } else if (candiMapPair.getKey().equals(AFFI_ID) || candiMapPair.getKey().equals(VALID_VOTES)) {
                                        try {
                                            Integer.parseInt(candiMapPair.getValue());
                                        } catch (NumberFormatException e) {
                                            System.err.println("National level - Invalid " + candiMapPair.getKey() + " value '" + candiMapPair.getValue() + "' in candiMap: " + candiMap);
                                            return;
                                        }
                                    }
                                }
                                transformer.registerNationalLevelData(candiMap);
                                parser.findAndAcceptEndTag(VALID_VOTES);
                            } else {
                                LOG.warning("Missing <ValidVotes> tag, unable to register votes for candidate %s of affiliation %d.".formatted(candiShortCode, affId));
                            }
                            break;
                        default:
                            LOG.warning("Unknown element [%s] found!".formatted(parser.getLocalName()));
                    }
                    parser.findAndAcceptEndTag(SELECTION);
                }
            }
            parser.findAndAcceptEndTag(TOTAL_VOTES);
        }
    }

    private void processConstiOrAuthoLevelData(Map<String, String> electionMap, XMLParser parser, String fileType) throws XMLStreamException {
        if (parser.findBeginTag(CONTEST)) {
            Map<String, String> constiMap = new HashMap<>(electionMap);
            if (parser.findBeginTag(CONTEST_ID)) {
                int constId = parser.getIntegerAttributeValue(null, ID, 0);
                constiMap.put(CONTEST_ID, String.valueOf(constId));
                if (parser.findBeginTag(CONTEST_NAME)) {
                    String constiName = parser.getElementText();
                    constiMap.put(CONTEST_NAME, constiName);
                    parser.findAndAcceptEndTag(CONTEST_NAME);
                }
                parser.findAndAcceptEndTag(CONTEST_ID);
            }
            if (parser.findBeginTag(TOTAL_VOTES)) {
                switch (fileType) {
                    case "constituency":
                        processConstiLevelData(constiMap, parser);
                        break;
                    case "authority":
                        processAuthority(constiMap, parser);
                        break;
                }
                parser.findAndAcceptEndTag(TOTAL_VOTES);
            }
            while (parser.nextBeginTag(REP_UNIT_VOTES)) {
                processRepUnit(constiMap, parser);
                parser.findAndAcceptEndTag(REP_UNIT_VOTES);
            }
            parser.findAndAcceptEndTag(CONTEST);
            if (!parser.findAndAcceptEndTag(CONTEST)) {
                LOG.warning("Cannot find </Contest> tag.");
            }
        } else {
            LOG.warning("Cannot find <Contest> tag.");
        }
    }

    private void processConstiLevelData(Map<String, String> receivedConstiMap, XMLParser parser) throws XMLStreamException {
        Map<String, String> constiMap = new HashMap<>(receivedConstiMap);
        Map<String, String> affiNamesMap = new HashMap<>();
        Map<String, Integer> affiVotesMap = new HashMap<>();
        Set<Integer> processedAffiliations = new HashSet<>();
        Set<String> processedCandiAffiliations = new HashSet<>();
        Map<Integer, Map<Integer, Integer>> candiVotesMap = new HashMap<>();
        if (parser.findBeginTag(CONTEST)) {
            int constId;
            String constiName;
            if (parser.findBeginTag(CONTEST_ID)) {
                constId = parser.getIntegerAttributeValue(null, ID, 0);
                constiMap.put(CONTEST_ID, String.valueOf(constId));
                if (parser.findBeginTag(CONTEST_NAME)) {
                    constiName = parser.getElementText().trim();
                    constiMap.put(CONTEST_NAME, constiName);
                    parser.findAndAcceptEndTag(CONTEST_NAME);
                }
                parser.findAndAcceptEndTag(CONTEST_ID);
            }
            while (parser.findBeginTag(TOTAL_VOTES)) {
                int currentAffId = -1;
                String currentAffiName;
                while (parser.findBeginTag(SELECTION)) {
                    parser.nextTag();
                    if (parser.getLocalName().equals(AFFI_ID)) {
                        currentAffId = parser.getIntegerAttributeValue(null, ID, 0);
                        if (parser.findBeginTag(AFFI_NAME)) {
                            currentAffiName = parser.getElementText().trim();
                            affiNamesMap.put(AFFI_NAME, currentAffiName);
                            parser.findAndAcceptEndTag(AFFI_NAME);
                        }
                        parser.findAndAcceptEndTag(AFFI_ID);
                        if (!processedAffiliations.contains(currentAffId)) {
                            int affiVotes;
                            if (parser.findBeginTag(VALID_VOTES)) {
                                affiVotes = Integer.parseInt(parser.getElementText().trim());
                                affiVotesMap.put(VALID_VOTES, affiVotes);
                                candiVotesMap.putIfAbsent(currentAffId, new HashMap<>());
                                processedAffiliations.add(currentAffId);
                                parser.findAndAcceptEndTag(VALID_VOTES);
                            }
                        } else {
                            parser.findBeginTag(VALID_VOTES);
                            parser.findAndAcceptEndTag(VALID_VOTES);
                            continue;
                        }

                    } else if (parser.getLocalName().equals(CANDIDATE)) {
                        int candId = -1;
                        if (parser.findBeginTag(CANDI_ID)) {
                            candId = parser.getIntegerAttributeValue(null, ID, 0);
                            parser.findAndAcceptEndTag(CANDI_ID);
                        }
                        String candiCompKey = currentAffId + "_" + candId;
                        if (processedCandiAffiliations.contains(candiCompKey)) {
                            parser.findBeginTag(VALID_VOTES);
                            parser.findAndAcceptEndTag(VALID_VOTES);
                            //continue;
                        } else {
                            int candiVotes = 0;
                            if (parser.findBeginTag(VALID_VOTES)) {
                                candiVotes = Integer.parseInt(parser.getElementText().trim());
                                parser.findAndAcceptEndTag(VALID_VOTES);
                            }
                            candiVotesMap.computeIfAbsent(currentAffId, k -> new HashMap<>()).put(candId, candiVotes);
                            processedCandiAffiliations.add(candiCompKey);
                        }
                    }
                    parser.findAndAcceptEndTag(SELECTION);
                }
                parser.findAndAcceptEndTag(TOTAL_VOTES);
            }
            transformer.registerConstiLevelData(constiMap, affiNamesMap, affiVotesMap, candiVotesMap);
        }
    }

    private void processAuthority(Map<String, String> constiMap, XMLParser parser) throws XMLStreamException {
        if (parser.findBeginTag(SELECTION)) {
            int affId = 0;
            Set<String> registeredCandiAffiliations = new HashSet<>();
            while (parser.getLocalName().equals(SELECTION)) {
                parser.nextTag();
                switch (parser.getLocalName()) {
                    case AFFI_ID:
                        Map<String, String> affiVotesMap = new HashMap<>(constiMap);
                        affId = parser.getIntegerAttributeValue(null, ID, 0);
                        if (registeredCandiAffiliations.contains(String.valueOf(affId))) {
                            parser.findAndAcceptEndTag(AFFI_ID);
                            continue;
                        }
                        affiVotesMap.put(AFFI_ID, String.valueOf(affId));
                        if (parser.findBeginTag(AFFI_NAME)) {
                            String affiName = parser.getElementText();
                            affiVotesMap.put(AFFI_NAME, affiName);
                            parser.findAndAcceptEndTag(AFFI_NAME);
                        }
                        parser.findAndAcceptEndTag(AFFI_ID);
                        if (parser.findBeginTag(VALID_VOTES)) {
                            int affiVotes = Integer.parseInt(parser.getElementText());
                            affiVotesMap.put(VALID_VOTES, String.valueOf(affiVotes));
                            parser.findAndAcceptEndTag(VALID_VOTES);
                        }
                        for (Map.Entry<String, String> affiVotesMapPair : affiVotesMap.entrySet()) {
                            if (affiVotesMapPair.getValue() == null) {
                                System.err.println("Missing " + affiVotesMapPair.getKey() + " in affiVotesMap: " + affiVotesMap);
                                return;
                            } else if (affiVotesMapPair.getKey().equals(VALID_VOTES)) {
                                try {
                                    Integer.parseInt(affiVotesMapPair.getValue());
                                } catch (NumberFormatException e) {
                                    System.err.println("Invalid " + affiVotesMapPair.getKey() + " value '" + affiVotesMapPair.getValue() + "' in affiVotesMap: " + affiVotesMap);
                                    return;
                                }
                            }
                        }
                        transformer.registerAuthority(affiVotesMap);
                        break;
                    case CANDIDATE:
                        Map<String, String> candiVotesMap = new HashMap<>(constiMap);
                        int candId = 0;
                        if (parser.findBeginTag(CANDI_ID)) {
                            candId = parser.getIntegerAttributeValue(null, ID, 0);
                            parser.findAndAcceptEndTag(CANDI_ID);
                        }
                        // Form a composite key - a true unique identifier of the candidate
                        String candiCompKey = candId + "_" + affId;
                        if (registeredCandiAffiliations.contains(candiCompKey)) {
                            parser.findAndAcceptEndTag(CANDIDATE);
                            continue;
                        }
                        parser.findAndAcceptEndTag(CANDIDATE);
                        if (parser.findBeginTag(VALID_VOTES)) {
                            int candiVotes = Integer.parseInt(parser.getElementText());
                            candiVotesMap.put(CANDI_ID, String.valueOf(candId));
                            candiVotesMap.put(AFFI_ID, String.valueOf(affId));
                            candiVotesMap.put("CandiVotes", String.valueOf(candiVotes));
                            for (Map.Entry<String, String> candiVotesMapPair : candiVotesMap.entrySet()) {
                                if (candiVotesMapPair.getValue() == null) {
                                    System.err.println("Missing " + candiVotesMapPair.getKey() + " in candiVotesMap: " + candiVotesMap);
                                    return;
                                } else if (candiVotesMapPair.getKey().equals(CANDI_ID) || candiVotesMapPair.getKey().equals("CandidateVotes")) {
                                    try {
                                        Integer.parseInt(candiVotesMapPair.getValue());
                                    } catch (NumberFormatException e) {
                                        System.err.println("Invalid " + candiVotesMapPair.getKey() + " value '" + candiVotesMapPair.getValue() + "' in candiVotesMap: " + candiVotesMap);
                                        return;
                                    }
                                }
                            }
                            registeredCandiAffiliations.add(candiCompKey);
                            transformer.registerAuthority(candiVotesMap);
                            parser.findAndAcceptEndTag(VALID_VOTES);
                        } else {
                            LOG.warning("Missing <ValidVotes> tag, unable to register votes for candidate %s of affiliation %d.".formatted(candId, affId));
                        }
                        break;
                    default:
                        LOG.warning("Unknown element [%s] found!".formatted(parser.getLocalName()));
                }
                parser.findAndAcceptEndTag(SELECTION);
            }
        }
    }

    private void processRepUnit(Map<String, String> constiMap, XMLParser parser) throws XMLStreamException {
        Map<String, String> repUnitMap = new HashMap<>(constiMap);
        String repUnitName = null;
        Map<Integer, Affiliation> repUnitAffiliationsMap = new HashMap<>();
        int repUnitVotes = 0;
        Affiliation affiliation;
        int affId = 0;
        int selectionIndex = 0;
        if (parser.findBeginTag(REP_UNIT_ID)) {
            String repUnitId = parser.getAttributeValue(null, ID);
            repUnitMap.put(REP_UNIT_ID, repUnitId);
            repUnitName = parser.getElementText();
            int postCodeIndex = repUnitName.indexOf("(postcode:");
            if (postCodeIndex >= 0) {
                int postCodeEndIndex = repUnitName.indexOf(')', postCodeIndex);
                if (postCodeEndIndex > postCodeIndex) {
                    String zipCode = repUnitName.substring(postCodeIndex + 10, postCodeEndIndex).replace(" ", "").toUpperCase();
                    repUnitMap.put(ZIPCODE, zipCode);
                    repUnitName = repUnitName.substring(0, postCodeIndex).trim() + repUnitName.substring(postCodeEndIndex + 1).trim();
                    repUnitMap.put("RepUnitName", repUnitName);
                }
            } else {
                repUnitMap.put("RepUnitName", repUnitName);
            }
            parser.findAndAcceptEndTag(REP_UNIT_ID);
        }
        while (parser.getLocalName().equals(SELECTION)) {
            parser.nextTag();
            switch (parser.getLocalName()) {
                case AFFI_ID:
                    affId = parser.getIntegerAttributeValue(null, ID, 0);
                    String affiName = "";
                    int affiVotes = 0;
                    if (parser.findBeginTag(AFFI_NAME)) {
                        affiName = parser.getElementText();
                        parser.findAndAcceptEndTag(AFFI_NAME);
                    }
                    parser.findAndAcceptEndTag(AFFI_ID);
                    if (parser.findBeginTag(VALID_VOTES)) {
                        affiVotes = Integer.parseInt(parser.getElementText());
                        repUnitVotes = repUnitVotes + affiVotes;
                        parser.findAndAcceptEndTag(VALID_VOTES);
                    } else {
                        LOG.warning("Missing <ValidVotes> tag, unable to register votes for affiliation %d within reporting unit %s.".formatted(affId, repUnitName));
                    }
                    affiliation = new Affiliation(affId, affiName, affiVotes);
                    repUnitAffiliationsMap.put(affId, affiliation);
                    break;
                case CANDIDATE:
                    int candId = 0;
                    if (parser.findBeginTag(CANDI_ID)) {
                        candId = parser.getIntegerAttributeValue(null, ID, 0);
                        parser.findAndAcceptEndTag(CANDI_ID);
                    }
                    parser.findAndAcceptEndTag(CANDIDATE);
                    if (parser.findBeginTag(VALID_VOTES)) {
                        int candiVotes = Integer.parseInt(parser.getElementText());
                        Candidate candidate = new Candidate(candId, candiVotes);
                        candidate.setAffId(affId);
                        repUnitAffiliationsMap.get(affId).addCandidate(candidate);
                        parser.findAndAcceptEndTag(VALID_VOTES);
                    } else {
                        LOG.warning("Missing <ValidVotes> tag, unable to register votes for candidate %d of affiliation %d within reporting unit %s.".formatted(candId, affId, repUnitName));
                    }
                    break;
                default:
                    LOG.warning("Unknown element [%s] found!".formatted(parser.getLocalName()));
            }
            selectionIndex = selectionIndex + 1;
            parser.findAndAcceptEndTag(SELECTION);
            if (selectionIndex == 3) break;
        }
        repUnitMap.put("RepUnitVotes", String.valueOf(repUnitVotes));
        for (Map.Entry<String, String> repUnitMapPair : repUnitMap.entrySet()) {
            if (repUnitMapPair.getValue() == null) {
                System.err.println("Missing " + repUnitMapPair.getKey() + " in repUnitMap: " + repUnitMap);
                return;
            } else if (Objects.equals(repUnitMapPair.getKey(), REP_UNIT_VOTES)) {
                try {
                    Integer.parseInt(repUnitMapPair.getValue());
                } catch (NumberFormatException e) {
                    System.err.println("Invalid " + repUnitMapPair.getKey() + " value '" + repUnitMapPair.getValue() + "' in repUnitMap: " + repUnitMap);
                    return;
                }
            }
        }
        transformer.registerRepUnit(repUnitMap, repUnitAffiliationsMap);
    }

    private void processAffiliation(Map<String, String> constiMap, XMLParser parser) throws XMLStreamException {
        Map<String, String> affiMap = new HashMap<>(constiMap);
        if (parser.findBeginTag(AFFI_ID)) {
            int affId = parser.getIntegerAttributeValue(null, ID, 0);
            affiMap.put(AFFI_ID, String.valueOf(affId));
            if (parser.findBeginTag(AFFI_NAME)) {
                String affiName = parser.getElementText();
                affiMap.put(AFFI_NAME, affiName);
                parser.findAndAcceptEndTag(AFFI_NAME);
            }
            parser.findAndAcceptEndTag(AFFI_ID);
        }
        if (parser.findBeginTag(CANDIDATE)) {
            while (parser.getLocalName().equals(CANDIDATE)) {
                processCandidate(affiMap, parser);
                parser.findAndAcceptEndTag(CANDIDATE);
            }
        }
    }

    private void processCandidate(Map<String, String> affiMap, XMLParser parser) throws XMLStreamException {
        Map<String, String> candiMap = new HashMap<>(affiMap);
        if (parser.findBeginTag(CANDI_ID)) {
            int candId = parser.getIntegerAttributeValue(null, ID, 0);
            candiMap.put(CANDI_ID, String.valueOf(candId));
            parser.findAndAcceptEndTag(CANDI_ID);
        }
        if (parser.findBeginTag(PERSON_NAME)) {
            if (parser.findBeginTag(NAME_LINE) && INITIALS.equals(parser.getAttributeValue("", NAME_TYPE))) {
                String initials = parser.getElementText().trim();
                candiMap.put(INITIALS, initials);
                parser.findAndAcceptEndTag(NAME_LINE);
            }
            if (parser.getLocalName().equals(FIRST_NAME)) {
                String firstName = parser.getElementText().trim();
                candiMap.put(FIRST_NAME, firstName);
                parser.findAndAcceptEndTag(FIRST_NAME);
            }
            if (parser.getLocalName().equals(LAST_NAME_PREFIX)) {
                String lastNamePrefix = parser.getElementText().trim();
                candiMap.put(LAST_NAME_PREFIX, lastNamePrefix);
                parser.findAndAcceptEndTag(LAST_NAME_PREFIX);
            }
            if (parser.findBeginTag(LAST_NAME)) {
                String lastName = parser.getElementText().trim();
                candiMap.put(LAST_NAME, lastName);
                parser.findAndAcceptEndTag(LAST_NAME);
            }
            parser.findAndAcceptEndTag(PERSON_NAME);
        }
        for (Map.Entry<String, String> candiMapPair : candiMap.entrySet()) {
            if (candiMapPair.getValue() == null) {
                System.err.println("Missing " + candiMapPair.getKey() + " in candiMap: " + candiMap);
                return;
            } else {
                if (candiMapPair.getKey().equals(CANDI_ID)) {
                    try {
                        Integer.parseInt(candiMapPair.getValue());
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid " + candiMapPair.getKey() + " value '" + candiMapPair.getValue() + "' in candiMap: " + candiMap);
                        return;
                    }
                }
            }
        }
        transformer.registerCandidate(candiMap);
    }

    private void processCandiLevel_ConstiData(Map<String, String> electionMap, XMLParser parser) throws XMLStreamException {
        if (parser.findBeginTag(CONTEST)) {
            Map<String, String> constiMap = new HashMap<>(electionMap);
            int constId;
            String constiName;
            if (parser.findBeginTag(CONTEST_ID)) {
                constId = parser.getIntegerAttributeValue(null, ID, 0);
                constiMap.put(CONTEST_ID, String.valueOf(constId));
                if (parser.findBeginTag(CONTEST_NAME)) {
                    constiName = parser.getElementText();
                    constiMap.put(CONTEST_NAME, constiName);
                    parser.findAndAcceptEndTag(CONTEST_NAME);
                }
                parser.findAndAcceptEndTag(CONTEST_ID);
            }
            transformer.registerCandiLevel_ConstiData(constiMap);
            if (parser.findBeginTag(AFFILIATION)) {
                while (parser.getLocalName().equals(AFFILIATION)) {
                    processAffiliation(constiMap, parser);
                    parser.findAndAcceptEndTag(AFFILIATION);
                }
            }
            parser.findAndAcceptEndTag(CONTEST);
            if (!parser.findAndAcceptEndTag(CONTEST)) {
                LOG.warning("Cannot find </Contest> tag.");
            }
        } else {
            LOG.warning("Cannot find <Contest> tag.");
        }
    }
}
