package com.voteU.election.java.utils.xml;

import com.voteU.election.java.models.*;
import com.voteU.election.java.utils.PathUtils;

import javax.xml.stream.XMLStreamException;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
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
 * First, it constructs the 'constituencies' and the 'kieslijsten'. Secondly, it processes the valid vote count.
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
     The tag names on the electoral level within the XML files, which are also used as keys in the maps
     when calling the methods of the transformer.
     */
    public static final String ELECTION_ID = "ElectionIdentifier";
    public static final String ELECTION_NAME = "ElectionName";
    public static final String ELECTION_DATE = "ElectionDate";

    /*
     The tag names on the constituencial level within the XML files, which are also used as keys in the maps
     when calling the methods of the transformer.
     */
    public static final String CONSTI = "Contest";
    public static final String CONSTI_ID = "ContestIdentifier";
    public static final String CONSTI_NAME = "ContestName";
    public static final String TOTAL_VV_COUNT = "TotalVotes";

    /*
     The tag names on the municipal level within the XML files, which are also used as keys in the maps
     when calling the methods of the transformer.
     */
    public static final String MUNI = "ManagingAuthority";
    public static final String MUNI_ID = "AuthorityIdentifier";

    /*
     The tag names on the polling station level within the XML files, which are also used as keys in the maps
     when calling the methods of the transformer.
     */
    public static final String PO_ST = "ReportingUnitVotes";
    public static final String PO_ST_ID = "ReportingUnitIdentifier";
    public static final String SELECTION = "Selection";

    /*
     The tag names on the affiliation level within the XML files, which are also used as keys in the maps
     when calling the methods of the transformer.
     */
    public static final String AFFI = "Affiliation";
    public static final String AFFI_ID = "AffiliationIdentifier";
    public static final String AFFI_NAME = "RegisteredName";

    /*
     The tag names on the candidate level within the XML files, which are also used as keys in the maps
     when calling the methods of the transformer.
     */
    public static final String CANDI = "Candidate";
    public static final String CANDI_ID = "CandidateIdentifier";
    public static final String CANDI_SHORT_CODE = "ShortCode";
    public static final String CANDI_FULL_NAME = "PersonName";
    public static final String NAME_LINE = "NameLine";
    public static final String INITIALS = "Initials"; // For convenience, is used as a key in the maps.
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
        LinkedHashMap<String, String> electionLhMap = new LinkedHashMap<>();
        electionLhMap.put("electionId", electionId);
        for (Path nationFile : PathUtils.findFilesToScan(folderName, "Totaaltelling_%s.eml.xml".formatted(electionId))) {
            LOG.fine("Found %s - %s".formatted(folderName, nationFile));
            System.out.println(folderName + " - " + nationFile);
            XMLParser parser = new XMLParser(new FileInputStream(nationFile.toString()));
            processElectoralLevelData(electionLhMap, parser);
            processNationalLevel_affiData(electionLhMap, parser);
        }
        for (Path constiFile : PathUtils.findFilesToScan(folderName, "Telling_%s_kieskring_".formatted(electionId))) {
            LOG.fine("Found %s - %s".formatted(folderName, constiFile));
            System.out.println(folderName + " - " + constiFile);
            XMLParser parser = new XMLParser(new FileInputStream(constiFile.toString()));
            processElectoralLevelData(electionLhMap, parser);
            processConstiOrMuniLevel_affiData(electionLhMap, parser, "consti");
        }
        for (Path muniFile : PathUtils.findFilesToScan(folderName, "Telling_%s_gemeente_".formatted(electionId))) {
            LOG.fine("Found %s - %s".formatted(folderName, muniFile));
            System.out.println(folderName + " - " + muniFile);
            XMLParser parser = new XMLParser(new FileInputStream(muniFile.toString()));
            processElectoralLevelData(electionLhMap, parser);
            processConstiOrMuniLevel_affiData(electionLhMap, parser, "muni");
        }
        for (Path candiFile : PathUtils.findFilesToScan(folderName, "Kandidatenlijsten_%s_".formatted(electionId))) {
            LOG.fine("Found %s - %s".formatted(folderName, candiFile));
            System.out.println(folderName + " - " + candiFile);
            XMLParser parser = new XMLParser(new FileInputStream(candiFile.toString()));
            processElectoralLevelData(electionLhMap, parser);
            processConstiLevelData(electionLhMap, parser);
        }
    }

    private void processElectoralLevelData(LinkedHashMap<String, String> electionLhMap, XMLParser parser) throws XMLStreamException {
        if (parser.findBeginTag(MUNI)) {
            if (parser.findBeginTag(MUNI_ID)) {
                String munId = parser.getAttributeValue(null, "Id");
                electionLhMap.put("munId", munId);
                String muniName = parser.getElementText();
                electionLhMap.put("muniName", muniName);
                parser.findAndAcceptEndTag(MUNI_ID);
            }
            parser.findAndAcceptEndTag(MUNI);
        }
        if (parser.findBeginTag(ELECTION_ID)) {
            if (parser.findBeginTag(ELECTION_NAME)) {
                String electionName = parser.getElementText();
                electionLhMap.put("electionName", electionName);
                parser.findAndAcceptEndTag(ELECTION_NAME);
            }
            if (parser.findBeginTag(ELECTION_DATE)) {
                String electionDate = parser.getElementText();
                electionLhMap.put("electionDate", electionDate);
                parser.findAndAcceptEndTag(ELECTION_DATE);
            }
            for (Map.Entry<String, String> electionLhMapPair : electionLhMap.entrySet()) {
                if (electionLhMapPair.getValue() == null) {
                    System.err.println("Missing " + electionLhMapPair.getKey() + " in electionLhMap: " + electionLhMap);
                    return;
                }
            }
            this.transformer.registerElectoralLevelData(electionLhMap);
            parser.findAndAcceptEndTag(ELECTION_ID);
        }
    }

    private void processNationalLevel_affiData(LinkedHashMap<String, String> constiLhMap, XMLParser parser) throws XMLStreamException {
        if (parser.findBeginTag(TOTAL_VV_COUNT)) {
            int affId = 0;
            HashSet<Integer> processedAffIdHashSet = new HashSet<>();
            HashSet<String> processedCandiShortCodeHashSet = new HashSet<>();
            if (parser.findBeginTag(SELECTION)) {
                while (parser.getLocalName().equals(SELECTION)) {
                    parser.nextTag();
                    switch (parser.getLocalName()) {
                        case AFFI_ID:
                            LinkedHashMap<String, String> affiLhMap = new LinkedHashMap<>(constiLhMap);
                            affId = parser.getIntegerAttributeValue(null, ID, 0);
                            if (processedAffIdHashSet.contains(affId)) {
                                parser.findAndAcceptEndTag(AFFI_ID);
                                continue;
                            }
                            affiLhMap.put("affId", String.valueOf(affId));
                            if (parser.findBeginTag(AFFI_NAME)) {
                                String affiName = parser.getElementText();
                                affiLhMap.put("affiName", affiName);
                                parser.findAndAcceptEndTag(AFFI_NAME);
                            }
                            parser.findAndAcceptEndTag(AFFI_ID);
                            if (parser.findBeginTag(VV_COUNT)) {
                                int affiVVCount = Integer.parseInt(parser.getElementText());
                                affiLhMap.put("affiVVCount", String.valueOf(affiVVCount));
                                parser.findAndAcceptEndTag(VV_COUNT);
                            }
                            processedAffIdHashSet.add(affId);
                            for (Map.Entry<String, String> affiLhMapPair : affiLhMap.entrySet()) {
                                if (affiLhMapPair.getValue() == null) {
                                    System.err.println("National level - Missing " + affiLhMapPair.getKey() + " in affiLhMap: " + affiLhMap);
                                    return;
                                } else if (affiLhMapPair.getKey().equals("affId") || affiLhMapPair.getKey().equals("affiVVCount")) {
                                    try {
                                        Integer.parseInt(affiLhMapPair.getValue());
                                    } catch (NumberFormatException e) {
                                        System.err.println("National level - Invalid " + affiLhMapPair.getKey() + " value '" + affiLhMapPair.getValue() + "' in affiLhMap: " + affiLhMap);
                                        return;
                                    }
                                }
                            }
                            this.transformer.registerNationalLevel_affiData(affiLhMap);
                            break;
                        case CANDI:
                            String candiShortCode = null;
                            if (parser.findBeginTag(CANDI_ID)) {
                                candiShortCode = parser.getAttributeValue(null, CANDI_SHORT_CODE);
                                parser.findAndAcceptEndTag(CANDI_ID);
                                if (processedCandiShortCodeHashSet.contains(candiShortCode)) {
                                    continue;
                                }
                            }
                            parser.findAndAcceptEndTag(CANDI);
                            if (parser.findBeginTag(VV_COUNT)) {
                                int candiVVCount = Integer.parseInt(parser.getElementText());
                                LinkedHashMap<String, String> candiLhMap = new LinkedHashMap<>(constiLhMap);
                                candiLhMap.put("affId", String.valueOf(affId));
                                candiLhMap.put("candiShortCode", candiShortCode);
                                processedCandiShortCodeHashSet.add(candiShortCode);
                                candiLhMap.put("candiVVCount", String.valueOf(candiVVCount));
                                for (Map.Entry<String, String> candiLhMapPair : candiLhMap.entrySet()) {
                                    if (candiLhMapPair.getValue() == null) {
                                        System.err.println("National level - Missing " + candiLhMapPair.getKey() + " in candiLhMap: " + candiLhMap);
                                        return;
                                    } else if (candiLhMapPair.getKey().equals("affId") || candiLhMapPair.getKey().equals("candiVVCount")) {
                                        try {
                                            Integer.parseInt(candiLhMapPair.getValue());
                                        } catch (NumberFormatException e) {
                                            System.err.println("National level - Invalid " + candiLhMapPair.getKey() + " value '" + candiLhMapPair.getValue() + "' in candiLhMap: " + candiLhMap);
                                            return;
                                        }
                                    }
                                }
                                this.transformer.registerNationalLevel_affiData(candiLhMap);
                                parser.findAndAcceptEndTag(VV_COUNT);
                            } else {
                                LOG.warning("Missing <ValidVotes> tag. Unable to register the vvCount for candi %s of affi %d.".formatted(candiShortCode, affId));
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

    private void processConstiLevelData(LinkedHashMap<String, String> electionLhMap, XMLParser parser) throws XMLStreamException {
        if (parser.findBeginTag(CONSTI)) {
            LinkedHashMap<String, String> constiLhMap = new LinkedHashMap<>(electionLhMap);
            int constId;
            String constiName;
            if (parser.findBeginTag(CONSTI_ID)) {
                constId = parser.getIntegerAttributeValue(null, ID, 0);
                constiLhMap.put("constId", String.valueOf(constId));
                if (parser.findBeginTag(CONSTI_NAME)) {
                    constiName = parser.getElementText();
                    constiLhMap.put("constiName", constiName);
                    parser.findAndAcceptEndTag(CONSTI_NAME);
                }
                parser.findAndAcceptEndTag(CONSTI_ID);
            }
            this.transformer.registerConstiLevelData(constiLhMap);
            if (parser.findBeginTag(AFFI)) {
                while (parser.getLocalName().equals(AFFI)) {
                    processAffiLevelData(constiLhMap, parser);
                    parser.findAndAcceptEndTag(AFFI);
                }
            }
            parser.findAndAcceptEndTag(CONSTI);
            if (!parser.findAndAcceptEndTag(CONSTI)) {
                LOG.warning("Cannot find </Contest> tag.");
            }
        } else {
            LOG.warning("Cannot find <Contest> tag.");
        }
    }

    private void processConstiOrMuniLevel_affiData(LinkedHashMap<String, String> electionLhMap, XMLParser parser, String fileType) throws XMLStreamException {
        if (parser.findBeginTag(CONSTI)) {
            LinkedHashMap<String, String> constiLhMap = new LinkedHashMap<>(electionLhMap);
            if (parser.findBeginTag(CONSTI_ID)) {
                int constId = parser.getIntegerAttributeValue(null, ID, 0);
                constiLhMap.put("constId", String.valueOf(constId));
                if (parser.findBeginTag(CONSTI_NAME)) {
                    String constiName = parser.getElementText();
                    constiLhMap.put("constiName", constiName);
                    parser.findAndAcceptEndTag(CONSTI_NAME);
                }
                parser.findAndAcceptEndTag(CONSTI_ID);
            }
            if (parser.findBeginTag(TOTAL_VV_COUNT)) {
                switch (fileType) {
                    case "consti":
                        processConstiLevel_affiData(constiLhMap, parser);
                        break;
                    case "muni":
                        processMuniLevel_affiData(constiLhMap, parser);
                        break;
                }
                parser.findAndAcceptEndTag(TOTAL_VV_COUNT);
            }
            while (parser.nextBeginTag(PO_ST)) {
                processPoStLevelData(constiLhMap, parser);
                parser.findAndAcceptEndTag(PO_ST);
            }
            parser.findAndAcceptEndTag(CONSTI);
            if (!parser.findAndAcceptEndTag(CONSTI)) {
                LOG.warning("Cannot find </Contest> tag.");
            }
        } else {
            LOG.warning("Cannot find <Contest> tag.");
        }
    }

    private void processConstiLevel_affiData(LinkedHashMap<String, String> constiLhMap, XMLParser parser) throws XMLStreamException {
        if (parser.findBeginTag(CONSTI)) {
            LinkedHashMap<Integer, Affiliation> affiListLhMap = new LinkedHashMap<>();
            HashSet<Integer> processedAffiHashSet = new HashSet<>();
            LinkedHashMap<Integer, LinkedHashMap<Integer, Integer>> candiLhMap = new LinkedHashMap<>();
            HashSet<String> processedCandiHashSet = new HashSet<>();
            if (parser.findBeginTag(CONSTI_ID)) {
                int constId = parser.getIntegerAttributeValue(null, ID, 0);
                constiLhMap.put("constId", String.valueOf(constId));
                if (parser.findBeginTag(CONSTI_NAME)) {
                    String constiName = parser.getElementText().trim();
                    constiLhMap.put("constiName", constiName);
                    parser.findAndAcceptEndTag(CONSTI_NAME);
                }
                parser.findAndAcceptEndTag(CONSTI_ID);
            }
            while (parser.findBeginTag(TOTAL_VV_COUNT)) {
                int affId = -1;
                String affiName = "";
                int affiVVCount = 0;
                while (parser.findBeginTag(SELECTION)) {
                    parser.nextTag();
                    switch (parser.getLocalName()) {
                        case AFFI_ID:
                            affId = parser.getIntegerAttributeValue(null, ID, 0);
                            if (parser.findBeginTag(AFFI_NAME)) {
                                affiName = parser.getElementText().trim();
                                parser.findAndAcceptEndTag(AFFI_NAME);
                            }
                            parser.findAndAcceptEndTag(AFFI_ID);
                            if (processedAffiHashSet.contains(affId)) {
                                continue;
                            }
                            if (parser.findBeginTag(VV_COUNT)) {
                                affiVVCount = Integer.parseInt(parser.getElementText().trim());
                                candiLhMap.putIfAbsent(affId, new LinkedHashMap<>());
                                processedAffiHashSet.add(affId);
                                parser.findAndAcceptEndTag(VV_COUNT);
                            }
                            Affiliation affi = new Affiliation(affId, affiName, affiVVCount);
                            affiListLhMap.put(affId, affi);
                        case CANDI:
                            int candId = -1;
                            if (parser.findBeginTag(CANDI_ID)) {
                                candId = parser.getIntegerAttributeValue(null, ID, 0);
                                parser.findAndAcceptEndTag(CANDI_ID);
                            }
                            String candiCompKey = affId + "_" + candId;
                            if (processedCandiHashSet.contains(candiCompKey)) {
                                continue;
                            }
                            int candiVVCount = 0;
                            if (parser.findBeginTag(VV_COUNT)) {
                                candiVVCount = Integer.parseInt(parser.getElementText().trim());
                                parser.findAndAcceptEndTag(VV_COUNT);
                            }
                            candiLhMap.computeIfAbsent(affId, key -> new LinkedHashMap<>()).put(candId, candiVVCount);
                            processedCandiHashSet.add(candiCompKey);
                    }
                    parser.findAndAcceptEndTag(SELECTION);
                }
                parser.findAndAcceptEndTag(TOTAL_VV_COUNT);
            }
            this.transformer.registerConstiLevel_affiData(constiLhMap, affiListLhMap, candiLhMap);
        }
    }

    private void processMuniLevel_affiData(LinkedHashMap<String, String> constiLhMap, XMLParser parser) throws XMLStreamException {
        int affId = 0;
        HashSet<String> processedAffiHashSet = new HashSet<>();
        if (parser.findBeginTag(SELECTION)) {
            while (parser.getLocalName().equals(SELECTION)) {
                parser.nextTag();
                switch (parser.getLocalName()) {
                    case AFFI_ID:
                        LinkedHashMap<String, String> affiLhMap = new LinkedHashMap<>(constiLhMap);
                        affId = parser.getIntegerAttributeValue(null, ID, 0);
                        if (processedAffiHashSet.contains(String.valueOf(affId))) {
                            parser.findAndAcceptEndTag(AFFI_ID);
                            continue;
                        }
                        affiLhMap.put("affId", String.valueOf(affId));
                        if (parser.findBeginTag(AFFI_NAME)) {
                            String affiName = parser.getElementText();
                            affiLhMap.put("affiName", affiName);
                            parser.findAndAcceptEndTag(AFFI_NAME);
                        }
                        parser.findAndAcceptEndTag(AFFI_ID);
                        if (parser.findBeginTag(VV_COUNT)) {
                            int affiVVCount = Integer.parseInt(parser.getElementText());
                            affiLhMap.put("affiVVCount", String.valueOf(affiVVCount));
                            parser.findAndAcceptEndTag(VV_COUNT);
                        }
                        for (Map.Entry<String, String> affiLhMapPair : affiLhMap.entrySet()) {
                            if (affiLhMapPair.getValue() == null) {
                                System.err.println("Missing " + affiLhMapPair.getKey() + " in affiLhMap: " + affiLhMap);
                                return;
                            } else if (affiLhMapPair.getKey().equals("affId") || affiLhMapPair.getKey().equals("affiVVCount")) {
                                try {
                                    Integer.parseInt(affiLhMapPair.getValue());
                                } catch (NumberFormatException e) {
                                    System.err.println("Invalid " + affiLhMapPair.getKey() + " value '" + affiLhMapPair.getValue() + "' in affiLhMap: " + affiLhMap);
                                    return;
                                }
                            }
                        }
                        this.transformer.registerMuniLevel_affiData(affiLhMap);
                        break;
                    case CANDI:
                        LinkedHashMap<String, String> candiLhMap = new LinkedHashMap<>(constiLhMap);
                        int candId = 0;
                        if (parser.findBeginTag(CANDI_ID)) {
                            candId = parser.getIntegerAttributeValue(null, ID, 0);
                            parser.findAndAcceptEndTag(CANDI_ID);
                        }
                        // Form a composite key - a true unique identifier of the candidate
                        String candiCompKey = candId + "_" + affId;
                        parser.findAndAcceptEndTag(CANDI);
                        if (processedAffiHashSet.contains(candiCompKey)) {
                            continue;
                        }
                        if (parser.findBeginTag(VV_COUNT)) {
                            int candiVVCount = Integer.parseInt(parser.getElementText());
                            candiLhMap.put("candId", String.valueOf(candId));
                            candiLhMap.put("affId", String.valueOf(affId));
                            candiLhMap.put("candiVVCount", String.valueOf(candiVVCount));
                            for (Map.Entry<String, String> candiLhMapPair : candiLhMap.entrySet()) {
                                if (candiLhMapPair.getValue() == null) {
                                    System.err.println("Missing " + candiLhMapPair.getKey() + " in candiLhMap: " + candiLhMap);
                                    return;
                                } else if (candiLhMapPair.getKey().equals("candId") || candiLhMapPair.getKey().equals("candiVVCount")) {
                                    try {
                                        Integer.parseInt(candiLhMapPair.getValue());
                                    } catch (NumberFormatException e) {
                                        System.err.println("Invalid " + candiLhMapPair.getKey() + " value '" + candiLhMapPair.getValue() + "' in candiLhMap: " + candiLhMap);
                                        return;
                                    }
                                }
                            }
                            processedAffiHashSet.add(candiCompKey);
                            this.transformer.registerMuniLevel_affiData(candiLhMap);
                            parser.findAndAcceptEndTag(VV_COUNT);
                        } else {
                            LOG.warning("Missing <ValidVotes> tag. Unable to register the vvCount for candi %s of affi %d.".formatted(candId, affId));
                        }
                        break;
                    default:
                        LOG.warning("Unknown element [%s] found!".formatted(parser.getLocalName()));
                }
                parser.findAndAcceptEndTag(SELECTION);
            }
        }
    }

    private void processPoStLevelData(LinkedHashMap<String, String> constiLhMap, XMLParser parser) throws XMLStreamException {
        LinkedHashMap<String, String> poStLhMap = new LinkedHashMap<>(constiLhMap);
        String poStName = null;
        LinkedHashMap<Integer, Affiliation> affiListLhMap = new LinkedHashMap<>();
        int poStVVCount = 0;
        int affId = 0;
        int selectionIndex = 0;
        if (parser.findBeginTag(PO_ST_ID)) {
            String poStId = parser.getAttributeValue(null, ID);
            poStLhMap.put("poStId", poStId);
            poStName = parser.getElementText();
            if (poStName.contains("Stembureau")) {
                poStName = poStName.replace("Stembureau ", "");
            }
            int postCodeIndex = poStName.indexOf("(postcode:");
            if (postCodeIndex >= 0) {
                int postCodeEndIndex = poStName.indexOf(')', postCodeIndex);
                if (postCodeEndIndex > postCodeIndex) {
                    String zipCode = poStName.substring(postCodeIndex + 10, postCodeEndIndex).replace(" ", "").toUpperCase();
                    poStLhMap.put("zipCode", zipCode);
                    poStName = poStName.substring(0, postCodeIndex).trim() + poStName.substring(postCodeEndIndex + 1).trim();
                    poStLhMap.put("poStName", poStName);
                }
            } else {
                poStLhMap.put("poStName", poStName);
            }
            parser.findAndAcceptEndTag(PO_ST_ID);
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
                        LOG.warning("Missing <ValidVotes> tag. Unable to register the vvCount for affi %d within poSt %s.".formatted(affId, poStName));
                    }
                    Affiliation affi = new Affiliation(affId, affiName, affiVVCount);
                    affiListLhMap.put(affId, affi);
                    break;
                case CANDI:
                    int candId = 0;
                    if (parser.findBeginTag(CANDI_ID)) {
                        candId = parser.getIntegerAttributeValue(null, ID, 0);
                        parser.findAndAcceptEndTag(CANDI_ID);
                    }
                    parser.findAndAcceptEndTag(CANDI);
                    if (parser.findBeginTag(VV_COUNT)) {
                        int candiVVCount = Integer.parseInt(parser.getElementText());
                        Candidate candi = new Candidate(candId, candiVVCount);
                        candi.setAffId(affId);
                        affiListLhMap.get(affId).addCandi(candi);
                        parser.findAndAcceptEndTag(VV_COUNT);
                    } else {
                        LOG.warning("Missing <ValidVotes> tag. Unable to register the vvCount for candi %d of affi %d within poSt %s.".formatted(candId, affId, poStName));
                    }
                    break;
                default:
                    LOG.warning("Unknown element [%s] found!".formatted(parser.getLocalName()));
            }
            selectionIndex += 1;
            parser.findAndAcceptEndTag(SELECTION);
            if (selectionIndex == 3) {
                break;
            }
        }
        poStLhMap.put("poStVVCount", String.valueOf(poStVVCount));
        for (Map.Entry<String, String> poStLhMapPair : poStLhMap.entrySet()) {
            if (poStLhMapPair.getValue() == null) {
                System.err.println("Missing " + poStLhMapPair.getKey() + " in poStLhMap: " + poStLhMap);
                return;
            } else if (Objects.equals(poStLhMapPair.getKey(), "poStVVCount")) {
                try {
                    Integer.parseInt(poStLhMapPair.getValue());
                } catch (NumberFormatException e) {
                    System.err.println("Invalid poStVVCount value '" + poStLhMapPair.getValue() + "' in poStLhMap: " + poStLhMap);
                    return;
                }
            }
        }
        this.transformer.registerPoStLevelData(poStLhMap, affiListLhMap);
    }

    private void processAffiLevelData(LinkedHashMap<String, String> constiLhMap, XMLParser parser) throws XMLStreamException {
        LinkedHashMap<String, String> affiLhMap = new LinkedHashMap<>(constiLhMap);
        if (parser.findBeginTag(AFFI_ID)) {
            int affId = parser.getIntegerAttributeValue(null, ID, 0);
            affiLhMap.put("affId", String.valueOf(affId));
            if (parser.findBeginTag(AFFI_NAME)) {
                String affiName = parser.getElementText();
                affiLhMap.put("affiName", affiName);
                parser.findAndAcceptEndTag(AFFI_NAME);
            }
            parser.findAndAcceptEndTag(AFFI_ID);
        }
        if (parser.findBeginTag(CANDI)) {
            while (parser.getLocalName().equals(CANDI)) {
                processCandiLevelData(affiLhMap, parser);
                parser.findAndAcceptEndTag(CANDI);
            }
        }
    }

    private void processCandiLevelData(LinkedHashMap<String, String> affiLhMap, XMLParser parser) throws XMLStreamException {
        LinkedHashMap<String, String> candiLhMap = new LinkedHashMap<>(affiLhMap);
        if (parser.findBeginTag(CANDI_ID)) {
            int candId = parser.getIntegerAttributeValue(null, ID, 0);
            candiLhMap.put("candId", String.valueOf(candId));
            parser.findAndAcceptEndTag(CANDI_ID);
        }
        if (parser.findBeginTag(CANDI_FULL_NAME)) {
            if (parser.findBeginTag(NAME_LINE) && INITIALS.equals(parser.getAttributeValue("", NAME_TYPE))) {
                String initials = parser.getElementText().trim();
                candiLhMap.put("initials", initials);
                parser.findAndAcceptEndTag(NAME_LINE);
            }
            if (parser.getLocalName().equals(FIRST_NAME)) {
                String firstName = parser.getElementText().trim();
                candiLhMap.put("firstName", firstName);
                parser.findAndAcceptEndTag(FIRST_NAME);
            }
            if (parser.getLocalName().equals(LAST_NAME_PREFIX)) {
                String lastNamePrefix = parser.getElementText().trim();
                candiLhMap.put("lastNamePrefix", lastNamePrefix);
                parser.findAndAcceptEndTag(LAST_NAME_PREFIX);
            }
            if (parser.findBeginTag(LAST_NAME)) {
                String lastName = parser.getElementText().trim();
                candiLhMap.put("lastName", lastName);
                parser.findAndAcceptEndTag(LAST_NAME);
            }
            parser.findAndAcceptEndTag(CANDI_FULL_NAME);
        }
        for (Map.Entry<String, String> candiLhMapPair : candiLhMap.entrySet()) {
            if (candiLhMapPair.getValue() == null) {
                System.err.println("Missing " + candiLhMapPair.getKey() + " in candiLhMap: " + candiLhMap);
                return;
            } else {
                if (candiLhMapPair.getKey().equals("candId")) {
                    try {
                        Integer.parseInt(candiLhMapPair.getValue());
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid candId value '" + candiLhMapPair.getValue() + "' in candiLhMap: " + candiLhMap);
                        return;
                    }
                }
            }
        }
        this.transformer.registerCandiLevelData(candiLhMap);
    }
}
