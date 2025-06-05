package com.voteU.election.java.utils.xml;

import com.voteU.election.java.models.*;
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
 * At its current state, it processes the files in a two-step process.
 * First, it constructs the 'constituencies' and the 'kieslijsten'. Secondly, it processes the valid vote counts.
 * It behaves similar as the <a href="https://www.baeldung.com/java-visitor-pattern">visitor pattern</a>.<br>
 * The full dataset consists of three types of files.
 * <ol>
 *     <li>one file per constituency containing the 'kieslijsten'</li>
 *     <li>one file per constituency containing the total valid vote count within that constituency per candidate</li>
 *     <li>one file per municipality containing per polling station, the valid vote count per candidate</li>
 * </ol>
 * <em>When processing the files only the first and third type of files are processed by this implementation
 * at this moment, but can be changed if needed!</em><br>
 * <br>
 * The data in the XML files has a more or less hierarchy structure. When a method of the transformer is called,
 * a {@link LinkedHashMap} containing all the information on that level, including the information at the higher levels,
 * is provided. The {@link LinkedHashMap} is specified as: LinkedHashMap&lt;String, String>. It is up to the transformer
 * to convert any numerical information from its {@link String} representation into its appropriate datatype.<br>
 * <br>
 * <em>It assumes that filenames have NOT been changed and that the content has not been altered!
 * The code assumes that there is no whitespace between the open and closing tags.</em>
 */
public class ElectionProcessor<E> {
    private static final Logger LOG = Logger.getLogger(ElectionProcessor.class.getName());
    private final Transformer<E> transformer;

    // Common attribute name that is use on multiple tags.
    public static final String ID = "Id";
    public static final String VV_COUNT = "ValidVotes";

    /*
     The tag names on the election level within the XML files, which are also used as keys in the maps
     when calling the methods of the transformer.
     */
    public static final String ELECTION = "Election";
    public static final String ELECTION_ID = "ElectionIdentifier";
    public static final String ELECTION_NAME = "ElectionName";
    public static final String ELECTION_CATEGORY = "ElectionCategory";
    public static final String ELECTION_DATE = "ElectionDate";

    /*
     The tag names on the contest level within the XML files, which are also used as keys in the maps
     when calling the methods of the transformer.
     */
    public static final String CONSTITUENCY = "Contest";
    public static final String CONSTI_ID = "ContestIdentifier";
    public static final String CONSTI_NAME = "ContestName";
    public static final String TOTAL_VV_COUNT = "TotalVotes";

    /*
     The tag names on the municipality level within the XML files, which are also used as keys in the maps
     when calling the methods of the transformer.
     */
    public static final String MUNICIPALITY = "ManagingAuthority";
    public static final String MUNI_ID = "AuthorityIdentifier";

    /*
     The tag names on the polling station level within the XML files, which are also used as keys in the maps
     when calling the methods of the transformer.
     */
    public static final String POLLING_STATION = "ReportingUnitVotes";
    public static final String POLLING_STATION_ID = "ReportingUnitIdentifier";
    public static final String SELECTION = "Selection";
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
    public static final String CANDI_SHORT_CODE = "ShortCode";
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
     * Currently, it only processes the files containing the 'kieslijsten' and the votes per polling station.
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
        LinkedHashMap<String, String> electionMap = new LinkedHashMap<>();
        electionMap.put(ELECTION_ID, electionId);
        for (Path totalVotesFile : PathUtils.findFilesToScan(folderName, "Totaaltelling_%s.eml.xml".formatted(electionId))) {
            LOG.fine("Found: %s".formatted(totalVotesFile));
            XMLParser parser = new XMLParser(new FileInputStream(totalVotesFile.toString()));
            processElectoralLevelData(electionMap, parser);
            processNationalLevelData(electionMap, parser);
        }
        for (Path constiFile : PathUtils.findFilesToScan(folderName, "Telling_%s_kieskring_".formatted(electionId))) {
            LOG.fine("Found: %s".formatted(constiFile));
            System.out.println(folderName + constiFile.toString());
            XMLParser parser = new XMLParser(new FileInputStream(constiFile.toString()));
            processElectoralLevelData(electionMap, parser);
            processConstiOrMuniLevelData(electionMap, parser, "constituency");
        }
        for (Path muniFile : PathUtils.findFilesToScan(folderName, "Telling_%s_gemeente_".formatted(electionId))) {
            System.out.println(folderName + muniFile.toString());
            XMLParser parser = new XMLParser(new FileInputStream(muniFile.toString()));
            processElectoralLevelData(electionMap, parser);
            processConstiOrMuniLevelData(electionMap, parser, "municipality");
        }
        for (Path candiFile : PathUtils.findFilesToScan(folderName, "Kandidatenlijsten_%s_".formatted(electionId))) {
            LOG.fine("Found: %s".formatted(candiFile));
            XMLParser parser = new XMLParser(new FileInputStream(candiFile.toString()));
            processElectoralLevelData(electionMap, parser);
            processCandiLevel_ConstiData(electionMap, parser);
        }
    }

    private void processElectoralLevelData(LinkedHashMap<String, String> electionMap, XMLParser parser) throws XMLStreamException {
        if (parser.findBeginTag(MUNICIPALITY)) {
            if (parser.findBeginTag(MUNI_ID)) {
                String munId = parser.getAttributeValue(null, "Id");
                electionMap.put(MUNI_ID, munId);
                String muniName = parser.getElementText();
                electionMap.put("muniName", muniName);
                parser.findAndAcceptEndTag(MUNI_ID);
            }
            parser.findAndAcceptEndTag(MUNICIPALITY);
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
                this.transformer.registerElectoralLevelData(electionMap);
                parser.findAndAcceptEndTag(ELECTION_ID);
            } else {
                LOG.warning("The %s %s does not match the expected identifier %s".formatted(ELECTION_ID, electionId, expectedElectionId));
                parser.findAndAcceptEndTag(ELECTION);
            }
        }
    }

    private void processNationalLevelData(LinkedHashMap<String, String> constiMap, XMLParser parser) throws XMLStreamException {
        if (parser.findBeginTag(TOTAL_VV_COUNT)) {
            int affId = 0;
            HashSet<Integer> registeredAffIds = new HashSet<>();
            HashSet<String> registeredCandiShortCodes = new HashSet<>();
            if (parser.findBeginTag(SELECTION)) {
                while (parser.getLocalName().equals(SELECTION)) {
                    parser.nextTag();
                    switch (parser.getLocalName()) {
                        case AFFI_ID:
                            LinkedHashMap<String, String> affiMap = new LinkedHashMap<>(constiMap);
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
                            if (parser.findBeginTag(VV_COUNT)) {
                                int affiVVCount = Integer.parseInt(parser.getElementText());
                                affiMap.put(VV_COUNT, String.valueOf(affiVVCount));
                                parser.findAndAcceptEndTag(VV_COUNT);
                            }
                            registeredAffIds.add(affId);
                            for (Map.Entry<String, String> affiMapPair : affiMap.entrySet()) {
                                if (affiMapPair.getValue() == null) {
                                    System.err.println("National level - Missing " + affiMapPair.getKey() + " in affiMap: " + affiMap);
                                    return;
                                } else if (affiMapPair.getKey().equals(AFFI_ID) || affiMapPair.getKey().equals(VV_COUNT)) {
                                    try {
                                        Integer.parseInt(affiMapPair.getValue());
                                    } catch (NumberFormatException e) {
                                        System.err.println("National level - Invalid " + affiMapPair.getKey() + " value '" + affiMapPair.getValue() + "' in affiMap: " + affiMap);
                                        return;
                                    }
                                }
                            }
                            this.transformer.registerNationalLevelData(affiMap);
                            break;
                        case CANDIDATE:
                            String candiShortCode = null;
                            if (parser.findBeginTag(CANDI_ID)) {
                                candiShortCode = parser.getAttributeValue(null, CANDI_SHORT_CODE);
                                if (registeredCandiShortCodes.contains(candiShortCode)) {
                                    parser.findAndAcceptEndTag(CANDI_ID);
                                    continue;
                                }
                                parser.findAndAcceptEndTag(CANDI_ID);
                            }
                            parser.findAndAcceptEndTag(CANDIDATE);
                            if (parser.findBeginTag(VV_COUNT)) {
                                int candiVVCount = Integer.parseInt(parser.getElementText());
                                LinkedHashMap<String, String> candiMap = new LinkedHashMap<>(constiMap);
                                candiMap.put(CANDI_SHORT_CODE, candiShortCode);
                                registeredCandiShortCodes.add(candiShortCode);
                                candiMap.put(AFFI_ID, String.valueOf(affId));
                                candiMap.put("candiVVCount", String.valueOf(candiVVCount));
                                for (Map.Entry<String, String> candiMapPair : candiMap.entrySet()) {
                                    if (candiMapPair.getValue() == null) {
                                        System.err.println("National level - Missing " + candiMapPair.getKey() + " in candiMap: " + candiMap);
                                        return;
                                    } else if (candiMapPair.getKey().equals(AFFI_ID) || candiMapPair.getKey().equals(VV_COUNT)) {
                                        try {
                                            Integer.parseInt(candiMapPair.getValue());
                                        } catch (NumberFormatException e) {
                                            System.err.println("National level - Invalid " + candiMapPair.getKey() + " value '" + candiMapPair.getValue() + "' in candiMap: " + candiMap);
                                            return;
                                        }
                                    }
                                }
                                this.transformer.registerNationalLevelData(candiMap);
                                parser.findAndAcceptEndTag(VV_COUNT);
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
            parser.findAndAcceptEndTag(TOTAL_VV_COUNT);
        }
    }

    private void processConstiOrMuniLevelData(LinkedHashMap<String, String> electionMap, XMLParser parser, String fileType) throws XMLStreamException {
        if (parser.findBeginTag(CONSTITUENCY)) {
            LinkedHashMap<String, String> constiMap = new LinkedHashMap<>(electionMap);
            if (parser.findBeginTag(CONSTI_ID)) {
                int constId = parser.getIntegerAttributeValue(null, ID, 0);
                constiMap.put(CONSTI_ID, String.valueOf(constId));
                if (parser.findBeginTag(CONSTI_NAME)) {
                    String constiName = parser.getElementText();
                    constiMap.put(CONSTI_NAME, constiName);
                    parser.findAndAcceptEndTag(CONSTI_NAME);
                }
                parser.findAndAcceptEndTag(CONSTI_ID);
            }
            if (parser.findBeginTag(TOTAL_VV_COUNT)) {
                switch (fileType) {
                    case "constituency":
                        processConstiLevelData(constiMap, parser);
                        break;
                    case "municipality":
                        processMuniLevelData(constiMap, parser);
                        break;
                }
                parser.findAndAcceptEndTag(TOTAL_VV_COUNT);
            }
            while (parser.nextBeginTag(POLLING_STATION)) {
                processPollingStationLevelData(constiMap, parser);
                parser.findAndAcceptEndTag(POLLING_STATION);
            }
            parser.findAndAcceptEndTag(CONSTITUENCY);
            if (!parser.findAndAcceptEndTag(CONSTITUENCY)) {
                LOG.warning("Cannot find </Contest> tag.");
            }
        } else {
            LOG.warning("Cannot find <Contest> tag.");
        }
    }

    private void processConstiLevelData(LinkedHashMap<String, String> constiMap, XMLParser parser) throws XMLStreamException {
        List<String> affiNamesList = new ArrayList<>();
        List<Integer> affiVotesList = new ArrayList<>();
        HashSet<Integer> processedAffiliations = new HashSet<>();
        HashSet<String> processedCandiAffiliations = new HashSet<>();
        LinkedHashMap<Integer, LinkedHashMap<Integer, Integer>> candiVotesMap = new LinkedHashMap<>();
        if (parser.findBeginTag(CONSTITUENCY)) {
            int constId;
            String constiName;
            if (parser.findBeginTag(CONSTI_ID)) {
                constId = parser.getIntegerAttributeValue(null, ID, 0);
                constiMap.put(CONSTI_ID, String.valueOf(constId));
                if (parser.findBeginTag(CONSTI_NAME)) {
                    constiName = parser.getElementText().trim();
                    constiMap.put(CONSTI_NAME, constiName);
                    parser.findAndAcceptEndTag(CONSTI_NAME);
                }
                parser.findAndAcceptEndTag(CONSTI_ID);
            }
            while (parser.findBeginTag(TOTAL_VV_COUNT)) {
                int currentAffId = -1;
                String currentAffiName;
                while (parser.findBeginTag(SELECTION)) {
                    parser.nextTag();
                    if (parser.getLocalName().equals(AFFI_ID)) {
                        currentAffId = parser.getIntegerAttributeValue(null, ID, 0);
                        if (parser.findBeginTag(AFFI_NAME)) {
                            currentAffiName = parser.getElementText().trim();
                            affiNamesList.add(currentAffiName);
                            parser.findAndAcceptEndTag(AFFI_NAME);
                        }
                        parser.findAndAcceptEndTag(AFFI_ID);
                        if (!processedAffiliations.contains(currentAffId)) {
                            int affiVVCount;
                            if (parser.findBeginTag(VV_COUNT)) {
                                affiVVCount = Integer.parseInt(parser.getElementText().trim());
                                affiVotesList.add(affiVVCount);
                                candiVotesMap.putIfAbsent(currentAffId, new LinkedHashMap<>());
                                processedAffiliations.add(currentAffId);
                                parser.findAndAcceptEndTag(VV_COUNT);
                            }
                        } else {
                            parser.findBeginTag(VV_COUNT);
                            parser.findAndAcceptEndTag(VV_COUNT);
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
                            parser.findBeginTag(VV_COUNT);
                            parser.findAndAcceptEndTag(VV_COUNT);
                            //continue;
                        } else {
                            int candiVVCount = 0;
                            if (parser.findBeginTag(VV_COUNT)) {
                                candiVVCount = Integer.parseInt(parser.getElementText().trim());
                                parser.findAndAcceptEndTag(VV_COUNT);
                            }
                            candiVotesMap.computeIfAbsent(currentAffId, k -> new LinkedHashMap<>()).put(candId, candiVVCount);
                            processedCandiAffiliations.add(candiCompKey);
                        }
                    }
                    parser.findAndAcceptEndTag(SELECTION);
                }
                parser.findAndAcceptEndTag(TOTAL_VV_COUNT);
            }
            this.transformer.registerConstiLevelData(constiMap, affiNamesList, affiVotesList, candiVotesMap);
        }
    }

    private void processMuniLevelData(LinkedHashMap<String, String> constiMap, XMLParser parser) throws XMLStreamException {
        if (parser.findBeginTag(SELECTION)) {
            int affId = 0;
            HashSet<String> registeredCandiAffiliations = new HashSet<>();
            while (parser.getLocalName().equals(SELECTION)) {
                parser.nextTag();
                switch (parser.getLocalName()) {
                    case AFFI_ID:
                        LinkedHashMap<String, String> affiMap = new LinkedHashMap<>(constiMap);
                        affId = parser.getIntegerAttributeValue(null, ID, 0);
                        if (registeredCandiAffiliations.contains(String.valueOf(affId))) {
                            parser.findAndAcceptEndTag(AFFI_ID);
                            continue;
                        }
                        affiMap.put(AFFI_ID, String.valueOf(affId));
                        if (parser.findBeginTag(AFFI_NAME)) {
                            String affiName = parser.getElementText();
                            affiMap.put(AFFI_NAME, affiName);
                            parser.findAndAcceptEndTag(AFFI_NAME);
                        }
                        parser.findAndAcceptEndTag(AFFI_ID);
                        if (parser.findBeginTag(VV_COUNT)) {
                            int affiVVCount = Integer.parseInt(parser.getElementText());
                            affiMap.put(VV_COUNT, String.valueOf(affiVVCount));
                            parser.findAndAcceptEndTag(VV_COUNT);
                        }
                        for (Map.Entry<String, String> affiMapPair : affiMap.entrySet()) {
                            if (affiMapPair.getValue() == null) {
                                System.err.println("Missing " + affiMapPair.getKey() + " in affiMap: " + affiMap);
                                return;
                            } else if (affiMapPair.getKey().equals(VV_COUNT)) {
                                try {
                                    Integer.parseInt(affiMapPair.getValue());
                                } catch (NumberFormatException e) {
                                    System.err.println("Invalid " + affiMapPair.getKey() + " value '" + affiMapPair.getValue() + "' in affiMap: " + affiMap);
                                    return;
                                }
                            }
                        }
                        this.transformer.registerMuniLevelData(affiMap);
                        break;
                    case CANDIDATE:
                        LinkedHashMap<String, String> candiMap = new LinkedHashMap<>(constiMap);
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
                        if (parser.findBeginTag(VV_COUNT)) {
                            int candiVVCount = Integer.parseInt(parser.getElementText());
                            candiMap.put(CANDI_ID, String.valueOf(candId));
                            candiMap.put(AFFI_ID, String.valueOf(affId));
                            candiMap.put("candiVVCount", String.valueOf(candiVVCount));
                            for (Map.Entry<String, String> candiMapPair : candiMap.entrySet()) {
                                if (candiMapPair.getValue() == null) {
                                    System.err.println("Missing " + candiMapPair.getKey() + " in candiMap: " + candiMap);
                                    return;
                                } else if (candiMapPair.getKey().equals(CANDI_ID) || candiMapPair.getKey().equals("candiVVCount")) {
                                    try {
                                        Integer.parseInt(candiMapPair.getValue());
                                    } catch (NumberFormatException e) {
                                        System.err.println("Invalid " + candiMapPair.getKey() + " value '" + candiMapPair.getValue() + "' in candiMap: " + candiMap);
                                        return;
                                    }
                                }
                            }
                            registeredCandiAffiliations.add(candiCompKey);
                            this.transformer.registerMuniLevelData(candiMap);
                            parser.findAndAcceptEndTag(VV_COUNT);
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

    private void processPollingStationLevelData(LinkedHashMap<String, String> constiMap, XMLParser parser) throws XMLStreamException {
        LinkedHashMap<String, String> pollingStationMap = new LinkedHashMap<>(constiMap);
        String pollingStationName = null;
        LinkedHashMap<Integer, Affiliation> pollingStationLevel_affiListMap = new LinkedHashMap<>();
        int pollingStationVVCount = 0;
        Affiliation affiliation;
        int affId = 0;
        int selectionIndex = 0;
        if (parser.findBeginTag(POLLING_STATION_ID)) {
            String pollingStationId = parser.getAttributeValue(null, ID);
            pollingStationMap.put(POLLING_STATION_ID, pollingStationId);
            pollingStationName = parser.getElementText();
            System.out.println("before - pollingStationName - " + pollingStationName);
            if (pollingStationName.contains("Stembureau")) {
                pollingStationName = pollingStationName.replace("Stembureau ", "");
                System.out.println("after - pollingStationName - " + pollingStationName);
            }
            int postCodeIndex = pollingStationName.indexOf("(postcode:");
            if (postCodeIndex >= 0) {
                int postCodeEndIndex = pollingStationName.indexOf(')', postCodeIndex);
                if (postCodeEndIndex > postCodeIndex) {
                    String zipCode = pollingStationName.substring(postCodeIndex + 10, postCodeEndIndex).replace(" ", "").toUpperCase();
                    pollingStationMap.put(ZIPCODE, zipCode);
                    pollingStationName = pollingStationName.substring(0, postCodeIndex).trim() + pollingStationName.substring(postCodeEndIndex + 1).trim();
                    pollingStationMap.put("pollingStationName", pollingStationName);
                }
            } else {
                pollingStationMap.put("pollingStationName", pollingStationName);
            }
            parser.findAndAcceptEndTag(POLLING_STATION_ID);
        }
        while (parser.getLocalName().equals(SELECTION)) {
            parser.nextTag();
            switch (parser.getLocalName()) {
                case AFFI_ID:
                    affId = parser.getIntegerAttributeValue(null, ID, 0);
                    String affiName = "";
                    int affiVVCount = 0;
                    if (parser.findBeginTag(AFFI_NAME)) {
                        affiName = parser.getElementText();
                        parser.findAndAcceptEndTag(AFFI_NAME);
                    }
                    parser.findAndAcceptEndTag(AFFI_ID);
                    if (parser.findBeginTag(VV_COUNT)) {
                        affiVVCount = Integer.parseInt(parser.getElementText());
                        pollingStationVVCount = pollingStationVVCount + affiVVCount;
                        parser.findAndAcceptEndTag(VV_COUNT);
                    } else {
                        LOG.warning("Missing <ValidVotes> tag, unable to register votes for affiliation %d within polling station %s.".formatted(affId, pollingStationName));
                    }
                    affiliation = new Affiliation(affId, affiName, affiVVCount);
                    pollingStationLevel_affiListMap.put(affId, affiliation);
                    break;
                case CANDIDATE:
                    int candId = 0;
                    if (parser.findBeginTag(CANDI_ID)) {
                        candId = parser.getIntegerAttributeValue(null, ID, 0);
                        parser.findAndAcceptEndTag(CANDI_ID);
                    }
                    parser.findAndAcceptEndTag(CANDIDATE);
                    if (parser.findBeginTag(VV_COUNT)) {
                        int candiVVCount = Integer.parseInt(parser.getElementText());
                        Candidate candidate = new Candidate(candId, candiVVCount);
                        candidate.setAffId(affId);
                        pollingStationLevel_affiListMap.get(affId).addCandidate(candidate);
                        parser.findAndAcceptEndTag(VV_COUNT);
                    } else {
                        LOG.warning("Missing <ValidVotes> tag, unable to register votes for candidate %d of affiliation %d within polling station %s.".formatted(candId, affId, pollingStationName));
                    }
                    break;
                default:
                    LOG.warning("Unknown element [%s] found!".formatted(parser.getLocalName()));
            }
            selectionIndex = selectionIndex + 1;
            parser.findAndAcceptEndTag(SELECTION);
            if (selectionIndex == 3) break;
        }
        pollingStationMap.put("pollingStationVVCount", String.valueOf(pollingStationVVCount));
        for (Map.Entry<String, String> pollingStationMapPair : pollingStationMap.entrySet()) {
            if (pollingStationMapPair.getValue() == null) {
                System.err.println("Missing " + pollingStationMapPair.getKey() + " in pollingStationMap: " + pollingStationMap);
                return;
            } else if (Objects.equals(pollingStationMapPair.getKey(), POLLING_STATION)) {
                try {
                    Integer.parseInt(pollingStationMapPair.getValue());
                } catch (NumberFormatException e) {
                    System.err.println("Invalid " + pollingStationMapPair.getKey() + " value '" + pollingStationMapPair.getValue() + "' in pollingStationMap: " + pollingStationMap);
                    return;
                }
            }
        }
        this.transformer.registerPollingStationLevelData(pollingStationMap, pollingStationLevel_affiListMap);
    }

    private void processAffiLevelData(LinkedHashMap<String, String> constiMap, XMLParser parser) throws XMLStreamException {
        LinkedHashMap<String, String> affiMap = new LinkedHashMap<>(constiMap);
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
                processCandiLevelData(affiMap, parser);
                parser.findAndAcceptEndTag(CANDIDATE);
            }
        }
    }

    private void processCandiLevelData(LinkedHashMap<String, String> affiMap, XMLParser parser) throws XMLStreamException {
        LinkedHashMap<String, String> candiMap = new LinkedHashMap<>(affiMap);
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
        this.transformer.registerCandiLevelData(candiMap);
    }

    private void processCandiLevel_ConstiData(LinkedHashMap<String, String> electionMap, XMLParser parser) throws XMLStreamException {
        if (parser.findBeginTag(CONSTITUENCY)) {
            LinkedHashMap<String, String> constiMap = new LinkedHashMap<>(electionMap);
            int constId;
            String constiName;
            if (parser.findBeginTag(CONSTI_ID)) {
                constId = parser.getIntegerAttributeValue(null, ID, 0);
                constiMap.put(CONSTI_ID, String.valueOf(constId));
                if (parser.findBeginTag(CONSTI_NAME)) {
                    constiName = parser.getElementText();
                    constiMap.put(CONSTI_NAME, constiName);
                    parser.findAndAcceptEndTag(CONSTI_NAME);
                }
                parser.findAndAcceptEndTag(CONSTI_ID);
            }
            this.transformer.registerCandiLevel_constiData(constiMap);
            if (parser.findBeginTag(AFFILIATION)) {
                while (parser.getLocalName().equals(AFFILIATION)) {
                    processAffiLevelData(constiMap, parser);
                    parser.findAndAcceptEndTag(AFFILIATION);
                }
            }
            parser.findAndAcceptEndTag(CONSTITUENCY);
            if (!parser.findAndAcceptEndTag(CONSTITUENCY)) {
                LOG.warning("Cannot find </Contest> tag.");
            }
        } else {
            LOG.warning("Cannot find <Contest> tag.");
        }
    }
}
