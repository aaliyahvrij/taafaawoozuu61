package com.voteU.election.java.utils.xml;

import com.voteU.election.java.models.*;
import com.voteU.election.java.utils.PathUtils;

import javax.xml.stream.XMLStreamException;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.Objects;
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

    // Tag (attribute) names in multiple levels within the XML files
    public static final String VV_COUNT = "ValidVotes";
    public static final String ID = "Id";

    /*
     The tag names on the election level within the XML files, which are also used as keys in the maps
     when calling the methods of the transformer.
     */
    public static final String ELECTION = "Election";
    public static final String ELECTION_ID = "ElectionIdentifier";
    public static final String ELECTION_NAME = "ElectionName";
    public static final String ELECTION_DATE = "ElectionDate";

    /*
     The tag names on the constituency level within the XML files, which are also used as keys in the maps
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
    public static final String CANDI_FULL_NAME = "PersonName";
    public static final String NAME_LINE = "NameLine";
    public static final String INITIALS = "Initials"; // For convenience, is used as a key in the data-maps.
    public static final String FIRST_NAME = "FirstName";
    public static final String LAST_NAME_PREFIX = "NamePrefix";
    public static final String LAST_NAME = "LastName";

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
     * Currently, it only processes the files containing the 'kieslijsten' and valid vote count per polling station.
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
        electionMap.put("electionId", electionId);
        for (Path nationFile : PathUtils.findFilesToScan(folderName, "Totaaltelling_%s.eml.xml".formatted(electionId))) {
            LOG.fine("Found %s - %s".formatted(folderName, nationFile));
            System.out.println(folderName + " - " + nationFile);
            XMLParser parser = new XMLParser(new FileInputStream(nationFile.toString()));
            processElectoralLevelData(electionMap, parser);
            processNationalLevel_affiData(electionMap, parser);
        }
        for (Path constiFile : PathUtils.findFilesToScan(folderName, "Telling_%s_kieskring_".formatted(electionId))) {
            LOG.fine("Found %s - %s".formatted(folderName, constiFile));
            System.out.println(folderName + " - " + constiFile);
            XMLParser parser = new XMLParser(new FileInputStream(constiFile.toString()));
            processElectoralLevelData(electionMap, parser);
            processConstiOrMuniLevel_affiData(electionMap, parser, "consti");
        }
        for (Path muniFile : PathUtils.findFilesToScan(folderName, "Telling_%s_gemeente_".formatted(electionId))) {
            LOG.fine("Found %s - %s".formatted(folderName, muniFile));
            System.out.println(folderName + " - " + muniFile);
            XMLParser parser = new XMLParser(new FileInputStream(muniFile.toString()));
            processElectoralLevelData(electionMap, parser);
            processConstiOrMuniLevel_affiData(electionMap, parser, "muni");
        }
        for (Path candiFile : PathUtils.findFilesToScan(folderName, "Kandidatenlijsten_%s_".formatted(electionId))) {
            LOG.fine("Found %s - %s".formatted(folderName, candiFile));
            System.out.println(folderName + " - " + candiFile);
            XMLParser parser = new XMLParser(new FileInputStream(candiFile.toString()));
            processElectoralLevelData(electionMap, parser);
            processCandiLevel_ConstiData(electionMap, parser);
        }
    }

    private void processElectoralLevelData(LinkedHashMap<String, String> electionMap, XMLParser parser) throws XMLStreamException {
        if (parser.findBeginTag(MUNICIPALITY)) {
            if (parser.findBeginTag(MUNI_ID)) {
                String munId = parser.getAttributeValue(null, "Id");
                electionMap.put("munId", munId);
                String muniName = parser.getElementText();
                electionMap.put("muniName", muniName);
                parser.findAndAcceptEndTag(MUNI_ID);
            }
            parser.findAndAcceptEndTag(MUNICIPALITY);
        }
        if (parser.findBeginTag(ELECTION_ID)) {
            if (parser.findBeginTag(ELECTION_NAME)) {
                String electionName = parser.getElementText();
                electionMap.put("electionName", electionName);
                parser.findAndAcceptEndTag(ELECTION_NAME);
            }
            if (parser.findBeginTag(ELECTION_DATE)) {
                String electionDate = parser.getElementText();
                electionMap.put("electionDate", electionDate);
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
        }
    }

    private void processNationalLevel_affiData(LinkedHashMap<String, String> constiMap, XMLParser parser) throws XMLStreamException {
        if (parser.findBeginTag(TOTAL_VV_COUNT)) {
            int affId = 0;
            HashSet<Integer> processedAffIds = new HashSet<>();
            HashSet<String> processedCandiShortCodes = new HashSet<>();
            if (parser.findBeginTag(SELECTION)) {
                while (parser.getLocalName().equals(SELECTION)) {
                    parser.nextTag();
                    switch (parser.getLocalName()) {
                        case AFFI_ID:
                            LinkedHashMap<String, String> affiMap = new LinkedHashMap<>(constiMap);
                            affId = parser.getIntegerAttributeValue(null, ID, 0);
                            if (processedAffIds.contains(affId)) {
                                parser.findAndAcceptEndTag(AFFI_ID);
                                continue;
                            }
                            affiMap.put("affId", String.valueOf(affId));
                            if (parser.findBeginTag(AFFI_NAME)) {
                                String affiName = parser.getElementText();
                                affiMap.put("affiName", affiName);
                                parser.findAndAcceptEndTag(AFFI_NAME);
                            }
                            parser.findAndAcceptEndTag(AFFI_ID);
                            if (parser.findBeginTag(VV_COUNT)) {
                                int affiVVCount = Integer.parseInt(parser.getElementText());
                                affiMap.put("affiVVCount", String.valueOf(affiVVCount));
                                parser.findAndAcceptEndTag(VV_COUNT);
                            }
                            processedAffIds.add(affId);
                            for (Map.Entry<String, String> affiMapPair : affiMap.entrySet()) {
                                if (affiMapPair.getValue() == null) {
                                    System.err.println("National level - Missing " + affiMapPair.getKey() + " in affiMap: " + affiMap);
                                    return;
                                } else if (affiMapPair.getKey().equals("affId") || affiMapPair.getKey().equals("affiVVCount")) {
                                    try {
                                        Integer.parseInt(affiMapPair.getValue());
                                    } catch (NumberFormatException e) {
                                        System.err.println("National level - Invalid " + affiMapPair.getKey() + " value '" + affiMapPair.getValue() + "' in affiMap: " + affiMap);
                                        return;
                                    }
                                }
                            }
                            this.transformer.registerNationalLevel_affiData(affiMap);
                            break;
                        case CANDIDATE:
                            String candiShortCode = null;
                            if (parser.findBeginTag(CANDI_ID)) {
                                candiShortCode = parser.getAttributeValue(null, CANDI_SHORT_CODE);
                                parser.findAndAcceptEndTag(CANDI_ID);
                                if (processedCandiShortCodes.contains(candiShortCode)) {
                                    continue;
                                }
                            }
                            parser.findAndAcceptEndTag(CANDIDATE);
                            if (parser.findBeginTag(VV_COUNT)) {
                                int candiVVCount = Integer.parseInt(parser.getElementText());
                                LinkedHashMap<String, String> candiMap = new LinkedHashMap<>(constiMap);
                                candiMap.put("affId", String.valueOf(affId));
                                candiMap.put("candiShortCode", candiShortCode);
                                processedCandiShortCodes.add(candiShortCode);
                                candiMap.put("candiVVCount", String.valueOf(candiVVCount));
                                for (Map.Entry<String, String> candiMapPair : candiMap.entrySet()) {
                                    if (candiMapPair.getValue() == null) {
                                        System.err.println("National level - Missing " + candiMapPair.getKey() + " in candiMap: " + candiMap);
                                        return;
                                    } else if (candiMapPair.getKey().equals("affId") || candiMapPair.getKey().equals("candiVVCount")) {
                                        try {
                                            Integer.parseInt(candiMapPair.getValue());
                                        } catch (NumberFormatException e) {
                                            System.err.println("National level - Invalid " + candiMapPair.getKey() + " value '" + candiMapPair.getValue() + "' in candiMap: " + candiMap);
                                            return;
                                        }
                                    }
                                }
                                this.transformer.registerNationalLevel_affiData(candiMap);
                                parser.findAndAcceptEndTag(VV_COUNT);
                            } else {
                                LOG.warning("Missing <ValidVotes> tag. Unable to register the valid vote count for candidate %s of affiliation %d.".formatted(candiShortCode, affId));
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

    private void processConstiOrMuniLevel_affiData(LinkedHashMap<String, String> electionMap, XMLParser parser, String fileType) throws XMLStreamException {
        if (parser.findBeginTag(CONSTITUENCY)) {
            LinkedHashMap<String, String> constiMap = new LinkedHashMap<>(electionMap);
            if (parser.findBeginTag(CONSTI_ID)) {
                int constId = parser.getIntegerAttributeValue(null, ID, 0);
                constiMap.put("constId", String.valueOf(constId));
                if (parser.findBeginTag(CONSTI_NAME)) {
                    String constiName = parser.getElementText();
                    constiMap.put("constiName", constiName);
                    parser.findAndAcceptEndTag(CONSTI_NAME);
                }
                parser.findAndAcceptEndTag(CONSTI_ID);
            }
            if (parser.findBeginTag(TOTAL_VV_COUNT)) {
                switch (fileType) {
                    case "consti":
                        processConstiLevel_affiData(constiMap, parser);
                        break;
                    case "muni":
                        processMuniLevel_affiData(constiMap, parser);
                        break;
                }
                parser.findAndAcceptEndTag(TOTAL_VV_COUNT);
            }
            while (parser.nextBeginTag(POLLING_STATION)) {
                processPoStLevelData(constiMap, parser);
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

    private void processConstiLevel_affiData(LinkedHashMap<String, String> constiMap, XMLParser parser) throws XMLStreamException {
        if (parser.findBeginTag(CONSTITUENCY)) {
            List<String> affiNameList = new ArrayList<>();
            List<Integer> affiVVCountList = new ArrayList<>();
            HashSet<Integer> processedAffiliations = new HashSet<>();
            HashSet<String> processedCandidates = new HashSet<>();
            LinkedHashMap<Integer, LinkedHashMap<Integer, Integer>> candiMap = new LinkedHashMap<>();
            if (parser.findBeginTag(CONSTI_ID)) {
                int constId = parser.getIntegerAttributeValue(null, ID, 0);
                constiMap.put("constId", String.valueOf(constId));
                if (parser.findBeginTag(CONSTI_NAME)) {
                    String constiName = parser.getElementText().trim();
                    constiMap.put("constiName", constiName);
                    parser.findAndAcceptEndTag(CONSTI_NAME);
                }
                parser.findAndAcceptEndTag(CONSTI_ID);
            }
            while (parser.findBeginTag(TOTAL_VV_COUNT)) {
                int affId = -1;
                String affiName;
                while (parser.findBeginTag(SELECTION)) {
                    parser.nextTag();
                    switch (parser.getLocalName()) {
                        case AFFI_ID:
                            affId = parser.getIntegerAttributeValue(null, ID, 0);
                            if (parser.findBeginTag(AFFI_NAME)) {
                                affiName = parser.getElementText().trim();
                                affiNameList.add(affiName);
                                parser.findAndAcceptEndTag(AFFI_NAME);
                            }
                            parser.findAndAcceptEndTag(AFFI_ID);
                            if (processedAffiliations.contains(affId)) {
                                continue;
                            }
                            if (parser.findBeginTag(VV_COUNT)) {
                                int affiVVCount = Integer.parseInt(parser.getElementText().trim());
                                affiVVCountList.add(affiVVCount);
                                candiMap.putIfAbsent(affId, new LinkedHashMap<>());
                                processedAffiliations.add(affId);
                                parser.findAndAcceptEndTag(VV_COUNT);
                            }
                        case CANDIDATE:
                            int candId = -1;
                            if (parser.findBeginTag(CANDI_ID)) {
                                candId = parser.getIntegerAttributeValue(null, ID, 0);
                                parser.findAndAcceptEndTag(CANDI_ID);
                            }
                            String candiCompKey = affId + "_" + candId;
                            if (processedCandidates.contains(candiCompKey)) {
                                continue;
                            }
                            int candiVVCount = 0;
                            if (parser.findBeginTag(VV_COUNT)) {
                                candiVVCount = Integer.parseInt(parser.getElementText().trim());
                                parser.findAndAcceptEndTag(VV_COUNT);
                            }
                            candiMap.computeIfAbsent(affId, key -> new LinkedHashMap<>()).put(candId, candiVVCount);
                            processedCandidates.add(candiCompKey);
                    }
                    parser.findAndAcceptEndTag(SELECTION);
                }
                parser.findAndAcceptEndTag(TOTAL_VV_COUNT);
            }
            this.transformer.registerConstiLevel_affiData(constiMap, affiNameList, affiVVCountList, candiMap);
        }
    }

    private void processMuniLevel_affiData(LinkedHashMap<String, String> constiMap, XMLParser parser) throws XMLStreamException {
        if (parser.findBeginTag(SELECTION)) {
            int affId = 0;
            HashSet<String> processedAffiliations = new HashSet<>();
            while (parser.getLocalName().equals(SELECTION)) {
                parser.nextTag();
                switch (parser.getLocalName()) {
                    case AFFI_ID:
                        LinkedHashMap<String, String> affiMap = new LinkedHashMap<>(constiMap);
                        affId = parser.getIntegerAttributeValue(null, ID, 0);
                        if (processedAffiliations.contains(String.valueOf(affId))) {
                            parser.findAndAcceptEndTag(AFFI_ID);
                            continue;
                        }
                        affiMap.put("affId", String.valueOf(affId));
                        if (parser.findBeginTag(AFFI_NAME)) {
                            String affiName = parser.getElementText();
                            affiMap.put("affiName", affiName);
                            parser.findAndAcceptEndTag(AFFI_NAME);
                        }
                        parser.findAndAcceptEndTag(AFFI_ID);
                        if (parser.findBeginTag(VV_COUNT)) {
                            int affiVVCount = Integer.parseInt(parser.getElementText());
                            affiMap.put("affiVVCount", String.valueOf(affiVVCount));
                            parser.findAndAcceptEndTag(VV_COUNT);
                        }
                        for (Map.Entry<String, String> affiMapPair : affiMap.entrySet()) {
                            if (affiMapPair.getValue() == null) {
                                System.err.println("Missing " + affiMapPair.getKey() + " in affiMap: " + affiMap);
                                return;
                            } else if (affiMapPair.getKey().equals("affId") || affiMapPair.getKey().equals("affiVVCount")) {
                                try {
                                    Integer.parseInt(affiMapPair.getValue());
                                } catch (NumberFormatException e) {
                                    System.err.println("Invalid " + affiMapPair.getKey() + " value '" + affiMapPair.getValue() + "' in affiMap: " + affiMap);
                                    return;
                                }
                            }
                        }
                        this.transformer.registerMuniLevel_affiData(affiMap);
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
                        parser.findAndAcceptEndTag(CANDIDATE);
                        if (processedAffiliations.contains(candiCompKey)) {
                            continue;
                        }
                        if (parser.findBeginTag(VV_COUNT)) {
                            int candiVVCount = Integer.parseInt(parser.getElementText());
                            candiMap.put("candId", String.valueOf(candId));
                            candiMap.put("affId", String.valueOf(affId));
                            candiMap.put("candiVVCount", String.valueOf(candiVVCount));
                            for (Map.Entry<String, String> candiMapPair : candiMap.entrySet()) {
                                if (candiMapPair.getValue() == null) {
                                    System.err.println("Missing " + candiMapPair.getKey() + " in candiMap: " + candiMap);
                                    return;
                                } else if (candiMapPair.getKey().equals("candId") || candiMapPair.getKey().equals("candiVVCount")) {
                                    try {
                                        Integer.parseInt(candiMapPair.getValue());
                                    } catch (NumberFormatException e) {
                                        System.err.println("Invalid " + candiMapPair.getKey() + " value '" + candiMapPair.getValue() + "' in candiMap: " + candiMap);
                                        return;
                                    }
                                }
                            }
                            processedAffiliations.add(candiCompKey);
                            this.transformer.registerMuniLevel_affiData(candiMap);
                            parser.findAndAcceptEndTag(VV_COUNT);
                        } else {
                            LOG.warning("Missing <ValidVotes> tag. Unable to register the valid vote count for candidate %s of affiliation %d.".formatted(candId, affId));
                        }
                        break;
                    default:
                        LOG.warning("Unknown element [%s] found!".formatted(parser.getLocalName()));
                }
                parser.findAndAcceptEndTag(SELECTION);
            }
        }
    }

    private void processPoStLevelData(LinkedHashMap<String, String> constiMap, XMLParser parser) throws XMLStreamException {
        LinkedHashMap<String, String> poStMap = new LinkedHashMap<>(constiMap);
        String poStName = null;
        LinkedHashMap<Integer, Affiliation> poStLevel_affiListMap = new LinkedHashMap<>();
        int poStVVCount = 0;
        Affiliation affiliation;
        int affId = 0;
        int selectionIndex = 0;
        if (parser.findBeginTag(POLLING_STATION_ID)) {
            String poStId = parser.getAttributeValue(null, ID);
            poStMap.put("poStId", poStId);
            poStName = parser.getElementText();
            if (poStName.contains("Stembureau")) {
                poStName = poStName.replace("Stembureau ", "");
            }
            int postCodeIndex = poStName.indexOf("(postcode:");
            if (postCodeIndex >= 0) {
                int postCodeEndIndex = poStName.indexOf(')', postCodeIndex);
                if (postCodeEndIndex > postCodeIndex) {
                    String zipCode = poStName.substring(postCodeIndex + 10, postCodeEndIndex).replace(" ", "").toUpperCase();
                    poStMap.put("zipCode", zipCode);
                    poStName = poStName.substring(0, postCodeIndex).trim() + poStName.substring(postCodeEndIndex + 1).trim();
                    poStMap.put("poStName", poStName);
                }
            } else {
                poStMap.put("poStName", poStName);
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
                        poStVVCount += affiVVCount;
                        parser.findAndAcceptEndTag(VV_COUNT);
                    } else {
                        LOG.warning("Missing <ValidVotes> tag. Unable to register the valid vote count for affiliation %d within polling station %s.".formatted(affId, poStName));
                    }
                    affiliation = new Affiliation(affId, affiName, affiVVCount);
                    poStLevel_affiListMap.put(affId, affiliation);
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
                        poStLevel_affiListMap.get(affId).addCandidate(candidate);
                        parser.findAndAcceptEndTag(VV_COUNT);
                    } else {
                        LOG.warning("Missing <ValidVotes> tag. Unable to register the valid vote count for candidate %d of affiliation %d within polling station %s.".formatted(candId, affId, poStName));
                    }
                    break;
                default:
                    LOG.warning("Unknown element [%s] found!".formatted(parser.getLocalName()));
            }
            selectionIndex += 1;
            parser.findAndAcceptEndTag(SELECTION);
            if (selectionIndex == 3) break;
        }
        poStMap.put("poStVVCount", String.valueOf(poStVVCount));
        for (Map.Entry<String, String> poStMapPair : poStMap.entrySet()) {
            if (poStMapPair.getValue() == null) {
                System.err.println("Missing " + poStMapPair.getKey() + " in poStMap: " + poStMap);
                return;
            } else if (Objects.equals(poStMapPair.getKey(), "poStVVCount")) {
                try {
                    Integer.parseInt(poStMapPair.getValue());
                } catch (NumberFormatException e) {
                    System.err.println("Invalid poStVVCount value '" + poStMapPair.getValue() + "' in poStMap: " + poStMap);
                    return;
                }
            }
        }
        this.transformer.registerPoStLevelData(poStMap, poStLevel_affiListMap);
    }

    private void processAffiLevelData(LinkedHashMap<String, String> constiMap, XMLParser parser) throws XMLStreamException {
        LinkedHashMap<String, String> affiMap = new LinkedHashMap<>(constiMap);
        if (parser.findBeginTag(AFFI_ID)) {
            int affId = parser.getIntegerAttributeValue(null, ID, 0);
            affiMap.put("affId", String.valueOf(affId));
            if (parser.findBeginTag(AFFI_NAME)) {
                String affiName = parser.getElementText();
                affiMap.put("affiName", affiName);
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
            candiMap.put("candId", String.valueOf(candId));
            parser.findAndAcceptEndTag(CANDI_ID);
        }
        if (parser.findBeginTag(CANDI_FULL_NAME)) {
            if (parser.findBeginTag(NAME_LINE) && INITIALS.equals(parser.getAttributeValue("", NAME_TYPE))) {
                String initials = parser.getElementText().trim();
                candiMap.put("initials", initials);
                parser.findAndAcceptEndTag(NAME_LINE);
            }
            if (parser.getLocalName().equals(FIRST_NAME)) {
                String firstName = parser.getElementText().trim();
                candiMap.put("firstName", firstName);
                parser.findAndAcceptEndTag(FIRST_NAME);
            }
            if (parser.getLocalName().equals(LAST_NAME_PREFIX)) {
                String lastNamePrefix = parser.getElementText().trim();
                candiMap.put("lastNamePrefix", lastNamePrefix);
                parser.findAndAcceptEndTag(LAST_NAME_PREFIX);
            }
            if (parser.findBeginTag(LAST_NAME)) {
                String lastName = parser.getElementText().trim();
                candiMap.put("lastName", lastName);
                parser.findAndAcceptEndTag(LAST_NAME);
            }
            parser.findAndAcceptEndTag(CANDI_FULL_NAME);
        }
        for (Map.Entry<String, String> candiMapPair : candiMap.entrySet()) {
            if (candiMapPair.getValue() == null) {
                System.err.println("Missing " + candiMapPair.getKey() + " in candiMap: " + candiMap);
                return;
            } else {
                if (candiMapPair.getKey().equals("candId")) {
                    try {
                        Integer.parseInt(candiMapPair.getValue());
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid candId value '" + candiMapPair.getValue() + "' in candiMap: " + candiMap);
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
                constiMap.put("constId", String.valueOf(constId));
                if (parser.findBeginTag(CONSTI_NAME)) {
                    constiName = parser.getElementText();
                    constiMap.put("constiName", constiName);
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
