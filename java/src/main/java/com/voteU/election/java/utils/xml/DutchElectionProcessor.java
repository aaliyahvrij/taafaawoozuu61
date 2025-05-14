package com.voteU.election.java.utils.xml;

import com.voteU.election.java.model.Party;
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
     The tag names on the election level within the XML files, which are also used as keys in the maps when calling
     the methods of the transformer.
     */
    public static final String ELECTION = "Election";
    public static final String ELECTION_ID = "ElectionIdentifier";
    public static final String ELECTION_NAME = "ElectionName";
    public static final String ELECTION_CATEGORY = "ElectionCategory";
    public static final String ELECTION_DATE = "ElectionDate";
    public static final String TOTAL_VOTES = "TotalVotes";

    /*
     The tag names on the contest level within the XML files, which are also used as keys in the maps when calling
     the methods of the transformer.
     */
    public static final String CONTEST = "Contest";
    public static final String CONTEST_ID = "ContestIdentifier";
    public static final String CONTEST_NAME = "ContestName";

    /*
     The tag names on the authority level within the XML files, which are also used as keys in the maps when calling
     the methods of the transformer.
     */
    public static final String MANAGING_AUTHORITY = "ManagingAuthority";
    public static final String AUTHORITY_ID = "AuthorityIdentifier";
    public static final String AUTHORITY_NAME = "AuthorityName";

    /*
     The tag names on the reporting unit level within the XML files, which are also used as keys in the maps when calling
     the methods of the transformer.
     */
    public static final String REP_UNIT_ID = "ReportingUnitIdentifier";
    public static final String SELECTION = "Selection";
    public static final String REP_UNIT_VOTES = "ReportingUnitVotes";
    public static final String VALID_VOTES = "ValidVotes";
    public static final String ZIPCODE = "ZipCode"; // For convenience, is used as a key in the data-maps.

    /*
     The tag names on the affiliation level within the XML files, which are also used as keys in the maps when calling
     the methods of the transformer.
     */
    public static final String AFFILIATION = "Affiliation";
    public static final String AFFILIATION_ID = "AffiliationIdentifier";
    public static final String REGISTERED_NAME = "RegisteredName";

    /*
     The tag names on the candidate level within the XML files, which are also used as keys in the maps when calling
     the methods of the transformer.
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
    private static final String INVALID_NAME = "INVALID";
    private static final String NO_ZIPCODE = "";

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
        Map<String, String> electionData = new HashMap<>();
        electionData.put(ELECTION_ID, electionId);
        for (Path totalVotesFile : PathUtils.findFilesToScan(folderName, "Totaaltelling_%s.eml.xml".formatted(electionId))) {
            LOG.fine("Found: %s".formatted(totalVotesFile));
            XMLParser parser = new XMLParser(new FileInputStream(totalVotesFile.toString()));
            processElection(electionData, parser);
            processNation(electionData, parser);
        }
        for (Path constiFile : PathUtils.findFilesToScan(folderName, "Telling_%s_kieskring_".formatted(electionId))) {
            LOG.fine("Found: %s".formatted(constiFile));
            System.out.println(folderName + constiFile.toString());
            XMLParser parser = new XMLParser(new FileInputStream(constiFile.toString()));
            processElection(electionData, parser);
            processVotes(electionData, parser, "constituency");
        }
        for (Path authorityFile : PathUtils.findFilesToScan(folderName, "Telling_%s_gemeente_".formatted(electionId))) {
            System.out.println(folderName + authorityFile.toString());
            XMLParser parser = new XMLParser(new FileInputStream(authorityFile.toString()));
            processElection(electionData, parser);
            processVotes(electionData, parser, "authority");
        }
        for (Path candiFile : PathUtils.findFilesToScan(folderName, "Kandidatenlijsten_%s_".formatted(electionId))) {
            LOG.fine("Found: %s".formatted(candiFile));
            XMLParser parser = new XMLParser(new FileInputStream(candiFile.toString()));
            processElection(electionData, parser);
            processConstituency(electionData, parser);
        }
    }

    private void processElection(Map<String, String> electionData, XMLParser parser) throws XMLStreamException {
        if (parser.findBeginTag(MANAGING_AUTHORITY)) {
            if (parser.findBeginTag(AUTHORITY_ID)) {
                String authorityId = parser.getAttributeValue(null, "Id");
                electionData.put(AUTHORITY_ID, authorityId);
                String authorityName = parser.getElementText();
                electionData.put(AUTHORITY_NAME, authorityName);
                parser.findAndAcceptEndTag(AUTHORITY_ID);
            }
            parser.findAndAcceptEndTag(MANAGING_AUTHORITY);
        }
        if (parser.findBeginTag(ELECTION_ID)) {
            String expectedElectionId = electionData.get(ELECTION_ID);
            String electionId = parser.getAttributeValue(null, ID);
            if (electionData.containsKey(ELECTION_ID) && expectedElectionId.equals(electionId)) {
                if (parser.findBeginTag(ELECTION_NAME)) {
                    String electionName = parser.getElementText();
                    parser.findAndAcceptEndTag(ELECTION_NAME);
                    electionData.put(ELECTION_NAME, electionName);
                }
                if (parser.findBeginTag(ELECTION_CATEGORY)) {
                    String electionCategory = parser.getElementText();
                    parser.findAndAcceptEndTag(ELECTION_CATEGORY);
                    electionData.put(ELECTION_CATEGORY, electionCategory);
                }
                if (parser.findBeginTag(ELECTION_DATE)) {
                    String electionDate = parser.getElementText();
                    parser.findAndAcceptEndTag(ELECTION_DATE);
                    electionData.put(ELECTION_DATE, electionDate);
                }
                transformer.registerElection(electionData);
                parser.findAndAcceptEndTag(ELECTION_ID);
            } else {
                LOG.warning("The %s %s does not match the expected identifier %s".formatted(ELECTION_ID, electionId, expectedElectionId));
                parser.findAndAcceptEndTag(ELECTION);
            }
        }
    }

    private void processVotes(Map<String, String> electionData, XMLParser parser, String fileType) throws XMLStreamException {
        if (parser.findBeginTag(CONTEST)) {
            Map<String, String> constiData = new HashMap<>(electionData);
            if (parser.findBeginTag(CONTEST_ID)) {
                int constId = parser.getIntegerAttributeValue(null, ID, 0);
                constiData.put(CONTEST_ID, String.valueOf(constId));
                if (parser.findBeginTag(CONTEST_NAME)) {
                    String constiName = parser.getElementText();
                    constiData.put(CONTEST_NAME, constiName);
                    parser.findAndAcceptEndTag(CONTEST_NAME);
                }
                parser.findAndAcceptEndTag(CONTEST_ID);
            }
            if (parser.findBeginTag(TOTAL_VOTES)) {
                switch (fileType) {
                    case "constituency":
                        //System.out.println("Processing constituency votes");
                        //processConstituency(constiData, parser);
                        break;
                    case "authority":
                        //System.out.println("Processing authority votes");
                        processAuthority(constiData, parser);
                        break;
                }
                parser.findAndAcceptEndTag(TOTAL_VOTES);
            }
            while (parser.nextBeginTag(REP_UNIT_VOTES)) {
                processRepUnit(constiData, parser);
                parser.findAndAcceptEndTag(REP_UNIT_VOTES);
            }
            parser.findAndAcceptEndTag(CONTEST);
        }
    }

    private void processNation(Map<String, String> constiData, XMLParser parser) throws XMLStreamException {
        if (parser.findBeginTag(TOTAL_VOTES)) {
            //System.out.println("processNation - parser at " + parser.getLocalName());
            int affId = 0;
            Set<Integer> registeredAffIds = new HashSet<>();
            Set<String> registeredCandIds = new HashSet<>();
            while (parser.getLocalName().equals(SELECTION)) {
                parser.nextTag();
                switch (parser.getLocalName()) {
                    case AFFILIATION_ID:
                        //System.out.println("processNation - Found an affiliation identifier.");
                        Map<String, String> affiNationData = new HashMap<>(constiData);
                        affId = parser.getIntegerAttributeValue(null, ID, 0);
                        affiNationData.put(AFFILIATION_ID, String.valueOf(affId));
                        if (registeredAffIds.contains(affId)) {
                            parser.findAndAcceptEndTag(AFFILIATION_ID);
                            continue;
                        }
                        if (parser.findBeginTag(REGISTERED_NAME)) {
                            String affiName = parser.getElementText();
                            affiNationData.put(REGISTERED_NAME, affiName);
                            parser.findAndAcceptEndTag(REGISTERED_NAME);
                        }
                        parser.findAndAcceptEndTag(AFFILIATION_ID);
                        if (parser.findBeginTag(VALID_VOTES)) {
                            int affiVotes = Integer.parseInt(parser.getElementText());
                            affiNationData.put(VALID_VOTES, String.valueOf(affiVotes));
                            parser.findAndAcceptEndTag(VALID_VOTES);
                        }
                        //System.out.println(affiNationData);
                        registeredAffIds.add(affId);
                        transformer.registerNation(affiNationData);
                        break;
                    case CANDIDATE:
                        String candId = null;
                        if (parser.findBeginTag(CANDIDATE_ID)) {
                            candId = parser.getAttributeValue(null, SHORT_CODE);
                        }
                        parser.findAndAcceptEndTag(CANDIDATE);

                        // If this candidate has already been registered, skip it
                        if (registeredCandIds.contains(candId)) {
                            parser.findAndAcceptEndTag(VALID_VOTES);
                            continue;
                        }
                        if (parser.findBeginTag(VALID_VOTES)) {
                            int candiVotes = Integer.parseInt(parser.getElementText());
                            Map<String, String> candiTotalVotesData = new HashMap<>(constiData);
                            candiTotalVotesData.put(CANDIDATE_ID, candId);
                            registeredCandIds.add(candId);
                            candiTotalVotesData.put("CandiVotes", String.valueOf(candiVotes));
                            //System.out.println(candiTotalVotesData.get("CandiVotes"));
                            candiTotalVotesData.put(AFFILIATION_ID, String.valueOf(affId));
                            candiTotalVotesData.put("Source", "TOTAL");
                            transformer.registerNation(candiTotalVotesData);
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
            parser.findAndAcceptEndTag(TOTAL_VOTES);
        }
    }

    private void processConstituency(Map<String, String> electionData, XMLParser parser) throws XMLStreamException {
        if (parser.findBeginTag(CONTEST)) {
            Map<String, String> constiData = new HashMap<>(electionData);
            if (parser.findBeginTag(CONTEST_ID)) {
                int constId = parser.getIntegerAttributeValue(null, ID, 0);
                constiData.put(CONTEST_ID, String.valueOf(constId));
                if (parser.findBeginTag(CONTEST_NAME)) {
                    String constiName = parser.getElementText();
                    constiData.put(CONTEST_NAME, constiName);
                    parser.findAndAcceptEndTag(CONTEST_NAME);
                }
                parser.findAndAcceptEndTag(CONTEST_ID);
            }
            transformer.registerConstituency(constiData);
            if (parser.findBeginTag(AFFILIATION)) {
                while (parser.getLocalName().equals(AFFILIATION)) {
                    processAffiliation(constiData, parser);
                    parser.findAndAcceptEndTag(AFFILIATION);
                }
            }
            if (!parser.findAndAcceptEndTag(CONTEST)) {
                LOG.warning("Can't find %s closing tag.".formatted(CONTEST));
            }
        } else {
            LOG.warning("Can't find %s opening tag.".formatted(CONTEST));
        }
    }

    private void processAuthority(Map<String, String> constiData, XMLParser parser) throws XMLStreamException {
        //System.out.println("processAuthority - parser at " + parser.getLocalName());
        int affId = 0;
        Set<String> registeredCandiAffiliations = new HashSet<>();
        while (parser.getLocalName().equals(SELECTION)) {
            parser.nextTag();
            switch (parser.getLocalName()) {
                case AFFILIATION_ID:
                    //System.out.println("processAuthority - Found an affiliation identifier.");
                    Map<String, String> affiTotalVotesData = new HashMap<>(constiData);
                    affId = parser.getIntegerAttributeValue(null, ID, 0);
                    affiTotalVotesData.put(AFFILIATION_ID, String.valueOf(affId));

                    // Avoid processing the same affiliation multiple times
                    if (affiTotalVotesData.containsKey(String.valueOf(affId))) {
                        parser.findAndAcceptEndTag(AFFILIATION_ID);
                        continue;
                    }
                    if (parser.findBeginTag(REGISTERED_NAME)) {
                        String affiName = parser.getElementText();
                        affiTotalVotesData.put(REGISTERED_NAME, affiName);
                        parser.findAndAcceptEndTag(REGISTERED_NAME);
                    }
                    parser.findAndAcceptEndTag(AFFILIATION_ID);
                    if (parser.findBeginTag(VALID_VOTES)) {
                        int affiVotes = Integer.parseInt(parser.getElementText());
                        affiTotalVotesData.put(VALID_VOTES, String.valueOf(affiVotes));
                        parser.findAndAcceptEndTag(VALID_VOTES);
                    }
                    affiTotalVotesData.put("Source", "AUTHORITY");
                    transformer.registerAuthority(affiTotalVotesData);
                    break;
                case CANDIDATE:
                    Map<String, String> candiTotalVotesData = new HashMap<>(constiData);
                    int candId = 0;
                    if (parser.findBeginTag(CANDIDATE_ID)) {
                        //System.out.println("Found a candidate identifier.");
                        candId = parser.getIntegerAttributeValue(null, ID, 0);
                    }

                    // Form a composite key using both candId and affId
                    String candiAffiKey = candId + "_" + affId;
                    parser.findAndAcceptEndTag(CANDIDATE);

                    // If this candidate has already been registered, skip it
                    if (registeredCandiAffiliations.contains(candiAffiKey)) {
                        parser.findAndAcceptEndTag(VALID_VOTES);
                        continue;
                    }
                    if (parser.findBeginTag(VALID_VOTES)) {
                        int candiVotes = Integer.parseInt(parser.getElementText());
                        candiTotalVotesData.put(CANDIDATE_ID, String.valueOf(candId));
                        candiTotalVotesData.put("CandiVotes", String.valueOf(candiVotes));
                        candiTotalVotesData.put(AFFILIATION_ID, String.valueOf(affId));
                        candiTotalVotesData.put("Source", "AUTHORITY");
                        registeredCandiAffiliations.add(candiAffiKey);
                        transformer.registerAuthority(candiTotalVotesData);
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

    private void processRepUnit(Map<String, String> constiData, XMLParser parser) throws XMLStreamException {
        if (parser.findBeginTag(REP_UNIT_VOTES)) {
            Map<String, String> repUnitData = new HashMap<>(constiData);
            String repUnitName = null;
            List<Party> repUnitAffiliations = new ArrayList<>();
            int repUnitVotes = 0;
            if (parser.findBeginTag(REP_UNIT_ID)) {
                String repUnitId = parser.getAttributeValue(null, ID);
                repUnitData.put(REP_UNIT_ID, repUnitId);
                repUnitName = parser.getElementText();
                int postCodeIndex = repUnitName.indexOf("(postcode:");
                if (postCodeIndex >= 0) {
                    int postCodeEndIndex = repUnitName.indexOf(')', postCodeIndex);
                    if (postCodeEndIndex > postCodeIndex) {
                        String zipCode = repUnitName.substring(postCodeIndex + 10, postCodeEndIndex).replace(" ", "").toUpperCase();
                        repUnitData.put(ZIPCODE, zipCode);
                        repUnitName = repUnitName.substring(0, postCodeIndex).trim() + repUnitName.substring(postCodeEndIndex + 1).trim();
                        repUnitData.put("RepUnitName", repUnitName);
                    } else {
                        repUnitData.put("RepUnitName", repUnitName);
                    }
                }
                parser.findAndAcceptEndTag(REP_UNIT_ID);
            }
            while (parser.getLocalName().equals(SELECTION)) {
                parser.nextTag();
                int affId = 0;
                switch (parser.getLocalName()) {
                    case AFFILIATION_ID:
                        Party affiliation;
                        String affiName = "";
                        int affiVotes = 0;
                        affId = parser.getIntegerAttributeValue(null, ID, 0);
                        if (parser.findBeginTag(REGISTERED_NAME)) {
                            affiName = parser.getElementText();
                            parser.findAndAcceptEndTag(REGISTERED_NAME);
                        }
                        parser.findAndAcceptEndTag(AFFILIATION_ID);
                        if (parser.findBeginTag(VALID_VOTES)) {
                            affiVotes = Integer.parseInt(parser.getElementText());
                            repUnitVotes = repUnitVotes + affiVotes;
                            parser.findAndAcceptEndTag(VALID_VOTES);
                        } else {
                            LOG.warning("Missing %s tag, unable to register votes for affiliation %d within reporting unit %s.".formatted(VALID_VOTES, affId, repUnitName));
                        }
                        affiliation = new Party(affId, affiName, affiVotes);
                        repUnitAffiliations.add(affiliation);
                        break;
                    case CANDIDATE:
                        int candId = 0;
                        if (parser.findBeginTag(CANDIDATE_ID)) {
                            candId = parser.getIntegerAttributeValue(null, ID, 0);
                            repUnitData.put(CANDIDATE_ID, String.valueOf(candId));
                        }
                        parser.findAndAcceptEndTag(CANDIDATE);
                        if (parser.findBeginTag(VALID_VOTES)) {
                            int candiVotes = Integer.parseInt(parser.getElementText());
                            repUnitData.put("CandiRepUnitVotes", String.valueOf(candiVotes));
                            parser.findAndAcceptEndTag(VALID_VOTES);
                        } else {
                            LOG.warning("Missing %s tag, unable to register votes for candidate %d of affiliation %d within reporting unit %s.".formatted(VALID_VOTES, candId, affId, repUnitName));
                        }
                        break;
                    default:
                        LOG.warning("Unknown element [%s] found!".formatted(parser.getLocalName()));
                }
                parser.findAndAcceptEndTag(SELECTION);
            }
            repUnitData.put("RepUnitVotes", String.valueOf(repUnitVotes));
            transformer.registerRepUnit(repUnitData, repUnitAffiliations);
            parser.findAndAcceptEndTag(REP_UNIT_VOTES);
        }
    }

    private void processAffiliation(Map<String, String> constiData, XMLParser parser) throws XMLStreamException {
        Map<String, String> affiData = new HashMap<>(constiData);
        if (parser.findBeginTag(AFFILIATION_ID)) {
            int affId = parser.getIntegerAttributeValue(null, ID, 0);
            affiData.put(AFFILIATION_ID, String.valueOf(affId));
            if (parser.findBeginTag(REGISTERED_NAME)) {
                String affiName = parser.getElementText();
                affiData.put(REGISTERED_NAME, affiName);
                parser.findAndAcceptEndTag(REGISTERED_NAME);
            }
            parser.findAndAcceptEndTag(AFFILIATION_ID);
        }
        if (parser.findBeginTag(CANDIDATE)) {
            while (parser.getLocalName().equals(CANDIDATE)) {
                processCandidate(affiData, parser);
                parser.findAndAcceptEndTag(CANDIDATE);
            }
        }
    }

    private void processCandidate(Map<String, String> affiData, XMLParser parser) throws XMLStreamException {
        Map<String, String> candiData = new HashMap<>(affiData);
        if (parser.findBeginTag(CANDIDATE_ID)) {
            int candId = parser.getIntegerAttributeValue(null, ID, 0);
            candiData.put(CANDIDATE_ID, String.valueOf(candId));
            parser.findAndAcceptEndTag(CANDIDATE_ID);
        }
        if (parser.findBeginTag(PERSON_NAME)) {
            if (parser.findBeginTag(NAME_LINE) && INITIALS.equals(parser.getAttributeValue("", NAME_TYPE))) {
                String initials = parser.getElementText().trim();
                candiData.put(INITIALS, initials);
                parser.findAndAcceptEndTag(NAME_LINE);
            }
            if (parser.getLocalName().equals(FIRST_NAME)) {
                String firstName = parser.getElementText().trim();
                candiData.put(FIRST_NAME, firstName);
                parser.findAndAcceptEndTag(FIRST_NAME);
            }
            if (parser.getLocalName().equals(LAST_NAME_PREFIX)) {
                String lastNamePrefix = parser.getElementText().trim();
                candiData.put(LAST_NAME_PREFIX, lastNamePrefix);
                parser.findAndAcceptEndTag(LAST_NAME_PREFIX);
            }
            if (parser.findBeginTag(LAST_NAME)) {
                String lastName = parser.getElementText().trim();
                candiData.put(LAST_NAME, lastName);
                parser.findAndAcceptEndTag(LAST_NAME);
            }
            parser.findAndAcceptEndTag(PERSON_NAME);
        }
        transformer.registerCandidate(candiData);
    }
}
