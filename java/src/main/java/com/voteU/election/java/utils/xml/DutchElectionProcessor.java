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
public class DutchElectionProcessor<E> {
    private static final Logger LOG = Logger.getLogger(DutchElectionProcessor.class.getName());
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
    public static final String AFFILIATION_ID = "AffiliationIdentifier";
    public static final String AFFILIATION_NAME = "RegisteredName";

    /*
     The tag names on the candidate level within the XML files, which are also used as keys in the maps
     when calling the methods of the transformer.
     */
    public static final String CANDIDATE = "Candidate";
    public static final String CANDIDATE_ID = "CandidateIdentifier";
    public static final String PERSON_NAME = "PersonName";
    public static final String NAME_LINE = "NameLine";
    public static final String INITIALS = "Initials"; // For convenience, is used as a key in the data-maps.
    public static final String FIRST_NAME = "FirstName";
    public static final String LAST_NAME_PREFIX = "NamePrefix";
    public static final String LAST_NAME = "LastName";
    public static final String GENDER = "Gender";
    public static final String QUALIFYING_ADDRESS = "QualifyingAddress";
    public static final String LOCALITY = "Locality";
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
    public DutchElectionProcessor(Transformer<E> transformer) {
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
            processNationalLevelTotalVotes(electionMap, parser);
        }
        for (Path constiFile : PathUtils.findFilesToScan(folderName, "Telling_%s_kieskring_".formatted(electionId))) {
            LOG.fine("Found: %s".formatted(constiFile));
            System.out.println(folderName + constiFile.toString());
            XMLParser parser = new XMLParser(new FileInputStream(constiFile.toString()));
            processElection(electionMap, parser);
            processConstiOrAuthorityLevel_TotalVotes(electionMap, parser, "constituency");
        }
        for (Path authorityFile : PathUtils.findFilesToScan(folderName, "Telling_%s_gemeente_".formatted(electionId))) {
            System.out.println(folderName + authorityFile.toString());
            XMLParser parser = new XMLParser(new FileInputStream(authorityFile.toString()));
            processElection(electionMap, parser);
            processConstiOrAuthorityLevel_TotalVotes(electionMap, parser, "authority");
        }
        for (Path candiFile : PathUtils.findFilesToScan(folderName, "Kandidatenlijsten_%s_".formatted(electionId))) {
            LOG.fine("Found: %s".formatted(candiFile));
            XMLParser parser = new XMLParser(new FileInputStream(candiFile.toString()));
            processElection(electionMap, parser);
            processConstituency(electionMap, parser);
        }
    }

    private void processElection(Map<String, String> electionMap, XMLParser parser) throws XMLStreamException {
        if (parser.findBeginTag(MANAGING_AUTHORITY)) {
            if (parser.findBeginTag(AUTHORITY_ID)) {
                String authorityId = parser.getAttributeValue(null, "Id");
                electionMap.put(AUTHORITY_ID, authorityId);
                String authorityName = parser.getElementText();
                electionMap.put("AuthorityName", authorityName);
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
                transformer.registerElection(electionMap);
                parser.findAndAcceptEndTag(ELECTION_ID);
            } else {
                LOG.warning("The %s %s does not match the expected identifier %s".formatted(ELECTION_ID, electionId, expectedElectionId));
                parser.findAndAcceptEndTag(ELECTION);
            }
        }
    }

    private void processConstiOrAuthorityLevel_TotalVotes(Map<String, String> electionMap, XMLParser parser, String fileType) throws XMLStreamException {
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
                        processConstituency(constiMap, parser);
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
        }
    }

    private void processNationalLevelTotalVotes(Map<String, String> constiMap, XMLParser parser) throws XMLStreamException {
        if (parser.findBeginTag(TOTAL_VOTES)) {
            int affId = 0;
            Set<Integer> registeredAffIds = new HashSet<>();
            Set<String> registeredCandIds = new HashSet<>();
            if (parser.findBeginTag(SELECTION)) {
                while (parser.getLocalName().equals(SELECTION)) {
                    parser.nextTag();
                    switch (parser.getLocalName()) {
                        case AFFILIATION_ID:
                            //System.out.println("processNation - Found an affiliation identifier.");
                            Map<String, String> nationalLevel_affiMap = new HashMap<>(constiMap);
                            affId = parser.getIntegerAttributeValue(null, ID, 0);
                            // Avoid processing the same affiliation multiple times
                            if (registeredAffIds.contains(affId)) {
                                parser.findAndAcceptEndTag(AFFILIATION_ID);
                                continue;
                            }
                            nationalLevel_affiMap.put(AFFILIATION_ID, String.valueOf(affId));
                            if (parser.findBeginTag(AFFILIATION_NAME)) {
                                String affiName = parser.getElementText();
                                System.out.println("nationMap - Found an affiliation name: " + affiName);
                                nationalLevel_affiMap.put(AFFILIATION_NAME, affiName);
                                parser.findAndAcceptEndTag(AFFILIATION_NAME);
                            }
                            parser.findAndAcceptEndTag(AFFILIATION_ID);
                            if (parser.findBeginTag(VALID_VOTES)) {
                                int affiVotes = Integer.parseInt(parser.getElementText());
                                nationalLevel_affiMap.put(VALID_VOTES, String.valueOf(affiVotes));
                                parser.findAndAcceptEndTag(VALID_VOTES);
                            }
                            registeredAffIds.add(affId);
                            transformer.registerNationalLevelTotalVotes(nationalLevel_affiMap);
                            break;
                        case CANDIDATE:
                            String candId = null;
                            if (parser.findBeginTag(CANDIDATE_ID)) {
                                candId = parser.getAttributeValue(null, SHORT_CODE);
                                // If this candidate has already been registered, skip it
                                if (registeredCandIds.contains(candId)) {
                                    parser.findAndAcceptEndTag(CANDIDATE_ID);
                                    continue;
                                }
                                parser.findAndAcceptEndTag(CANDIDATE_ID);
                            }
                            parser.findAndAcceptEndTag(CANDIDATE);
                            if (parser.findBeginTag(VALID_VOTES)) {
                                int candiVotes = Integer.parseInt(parser.getElementText());
                                Map<String, String> nationalLevel_candiMap = new HashMap<>(constiMap);
                                nationalLevel_candiMap.put(CANDIDATE_ID, candId);
                                registeredCandIds.add(candId);
                                nationalLevel_candiMap.put("CandiVotes", String.valueOf(candiVotes));
                                nationalLevel_candiMap.put(AFFILIATION_ID, String.valueOf(affId));
                                nationalLevel_candiMap.put("Source", "TOTAL");
                                transformer.registerNationalLevelTotalVotes(nationalLevel_candiMap);
                                parser.findAndAcceptEndTag(VALID_VOTES);
                            } else {
                                LOG.warning("Missing %s tag, unable to register votes for candidate %s of affiliation %d.".formatted(VALID_VOTES, candId, affId));
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

    private void processConstituency(Map<String, String> electionMap, XMLParser parser) throws XMLStreamException {
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
            transformer.registerConstituency(constiMap);
            if (parser.findBeginTag(AFFILIATION)) {
                while (parser.getLocalName().equals(AFFILIATION)) {
                    processAffiliation(constiMap, parser);
                    parser.findAndAcceptEndTag(AFFILIATION);
                }
            }
            parser.findAndAcceptEndTag(CONTEST);
            if (!parser.findAndAcceptEndTag(CONTEST)) {
                LOG.warning("Can't find %s closing tag.".formatted(CONTEST));
            }
        } else {
            LOG.warning("Can't find %s opening tag.".formatted(CONTEST));
        }
    }

    private void processAuthority(Map<String, String> constiMap, XMLParser parser) throws XMLStreamException {
        if (parser.findBeginTag(SELECTION)) {
            int affId = 0;
            Set<String> registeredCandiAffiliations = new HashSet<>();
            while (parser.getLocalName().equals(SELECTION)) {
                parser.nextTag();
                switch (parser.getLocalName()) {
                    case AFFILIATION_ID:
                        //System.out.println("processAuthority - Found an affiliation identifier.");
                        Map<String, String> affiVotesMap = new HashMap<>(constiMap);
                        affId = parser.getIntegerAttributeValue(null, ID, 0);
                        // Avoid processing the same affiliation multiple times
                        if (registeredCandiAffiliations.contains(String.valueOf(affId))) {
                            parser.findAndAcceptEndTag(AFFILIATION_ID);
                            continue;
                        }
                        affiVotesMap.put(AFFILIATION_ID, String.valueOf(affId));
                        if (parser.findBeginTag(AFFILIATION_NAME)) {
                            String affiName = parser.getElementText();
                            affiVotesMap.put(AFFILIATION_NAME, affiName);
                            parser.findAndAcceptEndTag(AFFILIATION_NAME);
                        }
                        parser.findAndAcceptEndTag(AFFILIATION_ID);
                        if (parser.findBeginTag(VALID_VOTES)) {
                            int affiVotes = Integer.parseInt(parser.getElementText());
                            affiVotesMap.put(VALID_VOTES, String.valueOf(affiVotes));
                            parser.findAndAcceptEndTag(VALID_VOTES);
                        }
                        affiVotesMap.put("Source", "AUTHORITY");
                        transformer.registerAuthority(affiVotesMap);
                        break;
                    case CANDIDATE:
                        Map<String, String> candiVotesMap = new HashMap<>(constiMap);
                        int candId = 0;
                        if (parser.findBeginTag(CANDIDATE_ID)) {
                            //System.out.println("Found a candidate identifier.");
                            candId = parser.getIntegerAttributeValue(null, ID, 0);
                            parser.findAndAcceptEndTag(CANDIDATE_ID);
                        }
                        // Form a composite key from candId and affId
                        String candiAffiKey = candId + "_" + affId;
                        // If this candidate has already been registered, skip it
                        if (registeredCandiAffiliations.contains(candiAffiKey)) {
                            parser.findAndAcceptEndTag(CANDIDATE);
                            continue;
                        }
                        parser.findAndAcceptEndTag(CANDIDATE);
                        if (parser.findBeginTag(VALID_VOTES)) {
                            int candiVotes = Integer.parseInt(parser.getElementText());
                            candiVotesMap.put(CANDIDATE_ID, String.valueOf(candId));
                            candiVotesMap.put(AFFILIATION_ID, String.valueOf(affId));
                            candiVotesMap.put("CandiVotes", String.valueOf(candiVotes));
                            candiVotesMap.put("Source", "AUTHORITY");
                            registeredCandiAffiliations.add(candiAffiKey);
                            transformer.registerAuthority(candiVotesMap);
                            parser.findAndAcceptEndTag(VALID_VOTES);
                        } else {
                            LOG.warning("Missing %s tag, unable to register votes for candidate %s of affiliation %d.".formatted(VALID_VOTES, candId, affId));
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
        Map<Integer, Affiliation> repUnitAffiliations = new HashMap<>();
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
                case AFFILIATION_ID:
                    affId = parser.getIntegerAttributeValue(null, ID, 0);
                    String affiName = "";
                    int affiVotes = 0;
                    if (parser.findBeginTag(AFFILIATION_NAME)) {
                        affiName = parser.getElementText();
                        parser.findAndAcceptEndTag(AFFILIATION_NAME);
                    }
                    parser.findAndAcceptEndTag(AFFILIATION_ID);
                    if (parser.findBeginTag(VALID_VOTES)) {
                        affiVotes = Integer.parseInt(parser.getElementText());
                        repUnitVotes = repUnitVotes + affiVotes;
                        parser.findAndAcceptEndTag(VALID_VOTES);
                    } else {
                        LOG.warning("Missing %s tag, unable to register votes for affiliation %d within reporting unit %s.".formatted(VALID_VOTES, affId, repUnitName));
                    }
                    affiliation = new Affiliation(affId, affiName, affiVotes);
                    repUnitAffiliations.put(affId, affiliation);
                    break;
                case CANDIDATE:
                    int candId = 0;
                    if (parser.findBeginTag(CANDIDATE_ID)) {
                        candId = parser.getIntegerAttributeValue(null, ID, 0);
                        parser.findAndAcceptEndTag(CANDIDATE_ID);
                    }
                    parser.findAndAcceptEndTag(CANDIDATE);
                    if (parser.findBeginTag(VALID_VOTES)) {
                        int candiVotes = Integer.parseInt(parser.getElementText());
                        Candidate candidate = new Candidate();
                        candidate.setId(candId);
                        candidate.setAffId(affId);
                        candidate.setVotes(candiVotes);
                        repUnitAffiliations.get(affId).addCandidate(candidate);
                        parser.findAndAcceptEndTag(VALID_VOTES);
                    } else {
                        LOG.warning("Missing %s tag, unable to register votes for candidate %d of affiliation %d within reporting unit %s.".formatted(VALID_VOTES, candId, affId, repUnitName));
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
        transformer.registerRepUnit(repUnitMap, repUnitAffiliations);
    }

    private void processAffiliation(Map<String, String> constiMap, XMLParser parser) throws XMLStreamException {
        Map<String, String> affiMap = new HashMap<>(constiMap);
        if (parser.findBeginTag(AFFILIATION_ID)) {
            int affId = parser.getIntegerAttributeValue(null, ID, 0);
            affiMap.put(AFFILIATION_ID, String.valueOf(affId));
            if (parser.findBeginTag(AFFILIATION_NAME)) {
                String affiName = parser.getElementText();
                affiMap.put(AFFILIATION_NAME, affiName);
                parser.findAndAcceptEndTag(AFFILIATION_NAME);
            }
            parser.findAndAcceptEndTag(AFFILIATION_ID);
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
        if (parser.findBeginTag(CANDIDATE_ID)) {
            int candId = parser.getIntegerAttributeValue(null, ID, 0);
            candiMap.put(CANDIDATE_ID, String.valueOf(candId));
            parser.findAndAcceptEndTag(CANDIDATE_ID);
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
        transformer.registerCandidate(candiMap);
    }
}
