package com.voteU.election.java.utils.xml;

import com.voteU.election.java.utils.PathUtils;
import javax.xml.stream.XMLStreamException;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Logger;

/**
 * Processes the XML data files for the Dutch elections. It is completely model agnostic. This means that it
 * doesn't have any knowledge of the data model that is being used by the application. All the datamodel specific
 * logic must be provided by a separate class that implements the {@link Transformer} interface.<br>
 * At its current state it processes the files in a two-step process. First it constructs the 'kieskringen' and
 * the 'kieslijsten', secondly it processes the vote counts. It behaves similar as the
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
 * Here is an example on how this class could be used.
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
     The tag names on the election level within the XML files which are also used as keys in the maps when calling
     the methods of the transformer.
     */
    public static final String ELECTION = "Election";
    public static final String ELECTION_IDENTIFIER = "ElectionIdentifier";
    public static final String ELECTION_NAME = "ElectionName";
    public static final String ELECTION_CATEGORY = "ElectionCategory";
    public static final String ELECTION_DATE = "ElectionDate";
    public static final String TOTAL_VOTES = "TotalVotes";

    /*
     The tag names on the contest level within the XML files which are also used as keys in the maps when calling
     the methods of the transformer.
     */
    public static final String CONTEST = "Contest";
    public static final String CONTEST_IDENTIFIER = "ContestIdentifier";
    public static final String CONTEST_NAME = "ContestName";

    /*
     The tag names on the affiliation level within the XML files which are also used as keys in the maps when calling
     the methods of the transformer.
     */
    public static final String AFFILIATION = "Affiliation";
    public static final String AFFILIATION_IDENTIFIER = "AffiliationIdentifier";
    public static final String REGISTERED_NAME = "RegisteredName";

    /*
     The tag names on the candidate level within the XML files which are also used as keys in the maps when calling
     the methods of the transformer.
     */
    public static final String CANDIDATE = "Candidate";
    public static final String CANDIDATE_IDENTIFIER = "CandidateIdentifier";
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

    /*
     The tag names on the reporting unit level within the XML files which are also used as keys in the maps when calling
     the methods of the transformer.
     */
    public static final String REPORTING_UNIT_IDENTIFIER = "ReportingUnitIdentifier";
    public static final String REPORTING_UNIT_NAME = "ReportingStationName";
    public static final String SELECTION = "Selection";
    public static final String REPORTING_UNIT_VOTES = "ReportingUnitVotes";
    public static final String VALID_VOTES = "ValidVotes";
    public static final String ZIPCODE = "ZipCode"; // For convenience, is used as a key in the data-maps.

    // Used internally
    private static final String NAME_TYPE = "NameType";
    private static final String INVALID_NAME = "INVALID";
    private static final String NO_ZIPCODE = "";

    /**
     * Creates a new instance that will use the provided transformer for transforming the data into the
     * application specific models.
     * @param transformer the {@link Transformer} that will take care of transforming the data into the application
     *                    specific models.
     */
    public DutchElectionProcessor(Transformer<E> transformer) {
        this.transformer = transformer;
    }

    /**
     * Traverses all the folders within the specified folder and calls the appropriate methods of the transformer.
     * While processing the files it will skip any file that has a different election-id than the one specified.
     * Currently, it only processes the files containing the 'kieslijsten' and the votes per reporting unit.
     * <pre>
     * NOTE: It assumes that there are <b>NO</b> whitespace characters between the tags other than within text values!
     * </pre>
     * @param electionId the identifier for the of the files that should be processed, for example <i>TK2023</i>.
     * @param folderName The name of the folder that contains the files containing the election data.
     * @return returns the result as defined by the {@link Transformer#retrieve()} method.
     * @throws IOException in case something goes wrong while reading the file.
     * @throws XMLStreamException when a file has not the expected format. One example is a file that has been formatted
     * for better readability.
     */
    public E processResults(String electionId, String folderName) throws IOException, XMLStreamException {
        // Een map om gegevens per jaar op te slaan
        Map<String, Map<String, String>> electionDataMap = new HashMap<>();

        LOG.info("=== Processing election: %s ===".formatted(electionId));
        LOG.info("Loading election data from %s".formatted(folderName));

        Map<String, String> electionData = new HashMap<>();
        electionData.put(ELECTION_IDENTIFIER, electionId);

        List<Path> files = PathUtils.findFilesToScan(folderName, "Kandidatenlijsten_%s_".formatted(electionId));
        for (Path electionFile : files) {
            LOG.fine("Found: %s".formatted(electionFile));
            XMLParser parser = new XMLParser(new FileInputStream(electionFile.toString()));
            processElection(electionData, parser);
            processContest(electionData, parser);
        }

        for(Path totalVotesFile : PathUtils.findFilesToScan(folderName, "Totaaltelling_%s.eml.xml".formatted(electionId)) ){
            LOG.fine("Found: %s".formatted(totalVotesFile));
            XMLParser parser = new XMLParser(new FileInputStream(totalVotesFile.toString()));
            processElection(electionData, parser);
            processNationalData(electionData, parser);
        }


        for (Path votesPerReportingStationFile : PathUtils.findFilesToScan(folderName, "Telling_%s_gemeente".formatted(electionId))) {
            LOG.fine("Found: %s".formatted(votesPerReportingStationFile));
            XMLParser parser = new XMLParser(new FileInputStream(votesPerReportingStationFile.toString()));
            //processElection(electionData, parser);
            //processVotes(electionData, parser, "gemeente");
        }

        for(Path constituencyFile : PathUtils.findFilesToScan(folderName, "Telling_%s_kieskring_".formatted(electionId))) {
            LOG.fine("Found: %s".formatted(constituencyFile));
            XMLParser parser = new XMLParser(new FileInputStream(constituencyFile.toString()));
            processElection(electionData, parser);
            processVotes(electionData, parser, "kieskring");

        }


        return transformer.retrieve();
    }

    private void processElection(Map<String, String> electionData, XMLParser parser) throws XMLStreamException {
        if (parser.findBeginTag(ELECTION_IDENTIFIER)) {
            String expectedElectionId = electionData.get(ELECTION_IDENTIFIER);

            String electionId = parser.getAttributeValue(null, ID);
            if (electionData.containsKey(ELECTION_IDENTIFIER) && expectedElectionId.equals(electionId)) {
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

                parser.findAndAcceptEndTag(ELECTION_IDENTIFIER);
            } else {
                LOG.warning("The %s %s does not match the expected identifier %s".formatted(ELECTION_IDENTIFIER, electionId, expectedElectionId));
                parser.findAndAcceptEndTag(ELECTION);
            }
        }
    }

    private void processContest(Map<String, String> electionData, XMLParser parser) throws XMLStreamException {
        if (parser.findBeginTag(CONTEST)) {
            int id = 0;
            String name = null;
            if (parser.findBeginTag(CONTEST_IDENTIFIER)) {
                id = parser.getIntegerAttributeValue(null, ID, 0);
                if (parser.findBeginTag(CONTEST_NAME)) {
                    name = parser.getElementText();
                    parser.findAndAcceptEndTag(CONTEST_NAME);
                }
                parser.findAndAcceptEndTag(CONTEST_IDENTIFIER);
            }

            Map<String, String> contestData = new HashMap<>(electionData);
            contestData.put(CONTEST_IDENTIFIER, String.valueOf(id));
            contestData.put(CONTEST_NAME, name);

            transformer.registerContest(contestData);

            parser.findBeginTag(AFFILIATION);
            while (parser.getLocalName().equals(AFFILIATION)) {
                processAffiliation(contestData, parser);
            }

            if (!parser.findAndAcceptEndTag(CONTEST)) {
                LOG.warning("Can't find %s closing tag.".formatted(CONTEST));
            }
        } else {
            LOG.warning("Can't find %s opening tag.".formatted(CONTEST));
        }
    }

    private void processAffiliation(Map<String, String> contestData, XMLParser parser) throws XMLStreamException {
        if (parser.findBeginTag(AFFILIATION)) {
            int id = 0;
            String name = INVALID_NAME;
            if (parser.findBeginTag(AFFILIATION_IDENTIFIER)) {
                id = parser.getIntegerAttributeValue(null, ID, 0);
                if (parser.findBeginTag(REGISTERED_NAME)) {
                    name = parser.getElementText();
                }
                parser.findAndAcceptEndTag(REGISTERED_NAME);
                parser.findAndAcceptEndTag(AFFILIATION_IDENTIFIER);
            }

            Map<String, String> affiliationData = new HashMap<>(contestData);
            affiliationData.put(AFFILIATION_IDENTIFIER, String.valueOf(id));
            affiliationData.put(REGISTERED_NAME, name);

            transformer.registerAffiliation(affiliationData);

            parser.findBeginTag(CANDIDATE);
            while (parser.getLocalName().equals(CANDIDATE)) {
                processCandidate(affiliationData, parser);
            }

            parser.findAndAcceptEndTag(AFFILIATION);
        }
    }

    private void processCandidate(Map<String, String> affiliationData, XMLParser parser) throws XMLStreamException {
        int id = 0;
        String initials = null;
        String firstName = null;
        String lastNamePrefix = null;
        String lastName = null;
        String gender = null;
        String locality = null;

        parser.nextBeginTag(CANDIDATE);
        if (parser.findBeginTag(CANDIDATE_IDENTIFIER)) {
            id = parser.getIntegerAttributeValue(null, ID, 0);
        }
        if (parser.findBeginTag(PERSON_NAME)) {
            if (parser.findBeginTag(NAME_LINE) && INITIALS.equals(parser.getAttributeValue("", NAME_TYPE))) {
                initials = parser.getElementText().trim();
                parser.findAndAcceptEndTag(NAME_LINE);
            }
            if (parser.getLocalName().equals(FIRST_NAME)) {
                firstName = parser.getElementText().trim();
                parser.findAndAcceptEndTag(FIRST_NAME);
            }
            if (parser.getLocalName().equals(LAST_NAME_PREFIX)) {
                if (parser.findBeginTag(LAST_NAME_PREFIX)) {
                    lastNamePrefix = parser.getElementText().trim();
                    parser.findAndAcceptEndTag(LAST_NAME_PREFIX);
                }
            }
            if (parser.findBeginTag(LAST_NAME)) {
                lastName = parser.getElementText().trim();
                parser.findAndAcceptEndTag(LAST_NAME);
            }
            parser.findAndAcceptEndTag(PERSON_NAME);
        }

        if (parser.findBeginTag(GENDER)) {
            gender = parser.getElementText().trim();
            parser.findAndAcceptEndTag(GENDER);
        }

        if (parser.findBeginTag(QUALIFYING_ADDRESS)) {
            if (parser.findBeginTag(LOCALITY)) {
                if (parser.findBeginTag(LOCALITY_NAME)) {
                    locality = parser.getElementText().trim();
                    parser.findAndAcceptEndTag(LOCALITY_NAME);
                }
                parser.findAndAcceptEndTag(LOCALITY);
            }
            parser.findAndAcceptEndTag(QUALIFYING_ADDRESS);
        }


        parser.findAndAcceptEndTag(CANDIDATE);

        Map<String, String> candidateData = new HashMap<>(affiliationData);
        candidateData.put(CANDIDATE_IDENTIFIER, String.valueOf(id));
        if (initials != null) {
            candidateData.put(INITIALS, (initials));
        }
        if (firstName != null) {
            candidateData.put(FIRST_NAME, firstName);
        }
        if (lastName != null) {
            candidateData.put(LAST_NAME_PREFIX, lastNamePrefix);
        }
        if (lastName != null) {
            candidateData.put(LAST_NAME, lastName);
        }
        if (gender != null) {
            candidateData.put(GENDER, gender);
        }
        if (locality != null) {
            candidateData.put(LOCALITY_NAME, locality);
        }

        transformer.registerCandidate(candidateData);
    }

    private void processVotes(Map<String, String> electionData, XMLParser parser, String fileType ) throws XMLStreamException {
        if (parser.findBeginTag(CONTEST)) {

            int contestId = 0;
            if (parser.findBeginTag(CONTEST_IDENTIFIER)) {
                contestId = parser.getIntegerAttributeValue(null, ID, 0);
                parser.findAndAcceptEndTag(CONTEST_IDENTIFIER);
            }

            Map<String, String> contestData = new HashMap<>(electionData);
            contestData.put(CONTEST_IDENTIFIER, String.valueOf(contestId));

            if(fileType.equals("kieskring")) {
                processConstituency(contestData, parser);
            }

            while (parser.findBeginTag(REPORTING_UNIT_VOTES)) {
                processReportingUnit(contestData, parser);
            }

            parser.findAndAcceptEndTag(CONTEST);
        }
    }
    private void processNationalData(Map<String, String> contestData, XMLParser parser) throws XMLStreamException {
        if (parser.findBeginTag(TOTAL_VOTES)){
            int affiliationId = 0;
            String name = INVALID_NAME;
            int affiliationVotes = 0;

            Set<Integer> registeredAffiliationIds = new HashSet<>();
            Set<String> registeredCandidateIds = new HashSet<>();

            while (parser.findBeginTag(SELECTION)) {
                parser.next();
                switch (parser.getLocalName()) {
                    case AFFILIATION_IDENTIFIER:
                        Map<String, String> afTotalVotesData = new HashMap<>(contestData);
                        affiliationId = parser.getIntegerAttributeValue(null, ID, 0);
                        if (registeredAffiliationIds.contains(affiliationId)) {
                            parser.findAndAcceptEndTag(AFFILIATION_IDENTIFIER);
                            continue;
                        }

                        if (parser.findBeginTag(REGISTERED_NAME)) {
                            name = parser.getElementText();
                            parser.findAndAcceptEndTag(REGISTERED_NAME);
                        }
                        parser.findAndAcceptEndTag(AFFILIATION_IDENTIFIER);
                        if (parser.findBeginTag(VALID_VOTES)) {
                            affiliationVotes = Integer.parseInt(parser.getElementText());
                            parser.findAndAcceptEndTag(VALID_VOTES);
                        }
                        afTotalVotesData.put(AFFILIATION_IDENTIFIER, String.valueOf(affiliationId));
                        afTotalVotesData.put(REGISTERED_NAME, name);
                        afTotalVotesData.put(VALID_VOTES, String.valueOf(affiliationVotes));
                        afTotalVotesData.put("Source", "TOTAL"); // ✅ important!
                        transformer.registerNationalVotes(afTotalVotesData);
                        registeredAffiliationIds.add(affiliationId);
                        break;
                    case CANDIDATE:
                        Map<String, String> caTotalVotesData = new HashMap<>(contestData);
                        String candidateId = null;
                        if (parser.findBeginTag(CANDIDATE_IDENTIFIER)) {
                            candidateId = parser.getAttributeValue(null,SHORT_CODE);
                        }

                        parser.findAndAcceptEndTag(CANDIDATE);
                        // If this candidate has already been registered, skip it
                        if (registeredCandidateIds.contains(candidateId)) {
                            parser.findAndAcceptEndTag(VALID_VOTES);
                            continue;
                        }

                        if (parser.findBeginTag(VALID_VOTES)) {
                            int candidateVoteCount = Integer.parseInt(parser.getElementText());
                            parser.findAndAcceptEndTag(VALID_VOTES);

                            caTotalVotesData.put(CANDIDATE_IDENTIFIER, candidateId);
                            caTotalVotesData.put("CandidateVotes", String.valueOf(candidateVoteCount));
                            caTotalVotesData.put(AFFILIATION_IDENTIFIER, String.valueOf(affiliationId));
                            caTotalVotesData.put("Source", "TOTAL"); // ✅ important!
                            transformer.registerNationalVotes(caTotalVotesData);
                            //System.out.println(caTotalVotesData.get("CandidateVotes"));
                            registeredCandidateIds.add(candidateId);

                        } else {
                            LOG.warning("Missing %s tag, unable to register votes for candidate %s of affiliation %d.".formatted(VALID_VOTES, candidateId, affiliationId));
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



    private void processReportingUnit(Map<String, String> contestData, XMLParser parser) throws XMLStreamException {
        if (parser.findBeginTag(REPORTING_UNIT_VOTES)) {
            String reportingUnitId = null;
            String reportingUnitName = null;
            String zipCode = NO_ZIPCODE;

            if (parser.findBeginTag(REPORTING_UNIT_IDENTIFIER)) {
                reportingUnitId = parser.getAttributeValue(null, ID);
                reportingUnitName = parser.getElementText();

                parser.findAndAcceptEndTag(REPORTING_UNIT_IDENTIFIER);
                int postCodeIndex = reportingUnitName.indexOf("(postcode:");
                if (postCodeIndex >= 0) {
                    int postCodeEndIndex = reportingUnitName.indexOf(')', postCodeIndex);
                    if (postCodeEndIndex > postCodeIndex) {
                        zipCode = reportingUnitName.substring(postCodeIndex + 10, postCodeEndIndex).replace(" ", "").toUpperCase();
                        reportingUnitName = reportingUnitName.substring(0, postCodeIndex).trim() + reportingUnitName.substring(postCodeEndIndex + 1).trim();
                    }
                }
            }

            Map<String, String> reportingUnitData = new HashMap<>(contestData);
            reportingUnitData.put(REPORTING_UNIT_IDENTIFIER, reportingUnitId);
            reportingUnitData.put(REPORTING_UNIT_NAME, reportingUnitName);
            reportingUnitData.put(ZIPCODE, zipCode);

            int affiliationId = 0;
            while (parser.getLocalName().equals(SELECTION)) {
                parser.next();
                switch (parser.getLocalName()) {
                    case AFFILIATION_IDENTIFIER:
                        affiliationId = parser.getIntegerAttributeValue(null, ID, 0);
                        parser.findAndAcceptEndTag(AFFILIATION_IDENTIFIER);
                        // Skipping the total ValidVotes for this affiliation
                        if (parser.findBeginTag(VALID_VOTES)) {
                            parser.findAndAcceptEndTag(VALID_VOTES);
                        }
                        reportingUnitData.put(AFFILIATION_IDENTIFIER, String.valueOf(affiliationId));
                        break;
                    case CANDIDATE:
                        int candidateId = 0;
                        if (parser.findBeginTag(CANDIDATE_IDENTIFIER)) {
                            candidateId = parser.getIntegerAttributeValue(null, ID, 0);
                        }

                        parser.findAndAcceptEndTag(CANDIDATE);
                        if (parser.findBeginTag(VALID_VOTES)) {
                            int voteCount = Integer.parseInt(parser.getElementText());
                            parser.findAndAcceptEndTag(VALID_VOTES);

                            reportingUnitData.put(CANDIDATE_IDENTIFIER, String.valueOf(candidateId));
                            reportingUnitData.put(VALID_VOTES, String.valueOf(voteCount));

                            transformer.registerVotes(reportingUnitData);
                        } else {
                            LOG.warning("Missing %s tag, unable to register votes for candidate %d of affiliation %d within reporting unit %s.".formatted(VALID_VOTES, candidateId, affiliationId, reportingUnitName));
                        }
                        break;
                    default:
                        LOG.warning("Unknown element [%s] found!".formatted(parser.getLocalName()));
                }

                parser.findAndAcceptEndTag(SELECTION);
            }

            parser.findAndAcceptEndTag(REPORTING_UNIT_VOTES);
        }
    }

    private void processConstituency(Map<String, String> contestData, XMLParser parser) throws XMLStreamException {



        Map<Integer, Integer> affiliationVotes = new HashMap<>();
        Map<Integer, Integer> candidateVotes = new HashMap<>();
        Map<String, String> constituencyData = new HashMap<>(contestData);

        if (parser.findBeginTag(CONTEST)) {
            int id = 0;
            String constituencyName = null;
            if(parser.findBeginTag(CONTEST_IDENTIFIER)){
                id = parser.getIntegerAttributeValue(null, ID, 0);
                if(parser.findBeginTag(CONTEST_NAME)){
                    constituencyName = parser.getElementText().trim();
                    parser.findAndAcceptEndTag(CONTEST_NAME);
                }
                parser.findAndAcceptEndTag(CONTEST_IDENTIFIER);
            }

            constituencyData.put(CONTEST_IDENTIFIER, String.valueOf(id));
            constituencyData.put(CONTEST_NAME, constituencyName);

            if (parser.findBeginTag(VALID_VOTES)) {
                while (parser.findBeginTag(SELECTION)) {
                    String affiliationName = null;
                    int affiliationId = 0;
                    int candidateId = 0;
                    int totalVotes = 0;

                    boolean isAffiliation = parser.findBeginTag(AFFILIATION_IDENTIFIER);
                    boolean isCandidate = !isAffiliation && parser.findBeginTag(CANDIDATE);

                    if (isAffiliation) {
                        affiliationId = parser.getIntegerAttributeValue(null, ID, 0);

                        if (parser.findBeginTag(REGISTERED_NAME)) {
                            affiliationName = parser.getElementText().trim();
                            parser.findAndAcceptEndTag(REGISTERED_NAME);
                        }

                        parser.findAndAcceptEndTag(AFFILIATION_IDENTIFIER);
                    } else if (isCandidate) {
                        if (parser.findBeginTag(CANDIDATE_IDENTIFIER)) {
                            candidateId = parser.getIntegerAttributeValue(null, ID, 0);
                            parser.findAndAcceptEndTag(CANDIDATE_IDENTIFIER);
                        }
                        parser.findAndAcceptEndTag(CANDIDATE);
                    }

                    constituencyData.put(AFFILIATION_IDENTIFIER, String.valueOf(affiliationId));
                    constituencyData.put(REGISTERED_NAME, affiliationName);
                    constituencyData.put(CANDIDATE_IDENTIFIER, String.valueOf(candidateId));


                    if (parser.findBeginTag(VALID_VOTES)) {
                        totalVotes = Integer.parseInt(parser.getElementText().trim());
                        parser.findAndAcceptEndTag(VALID_VOTES);
                    }

                    parser.findAndAcceptEndTag(SELECTION);

                    if (affiliationId != -1) {
                        affiliationVotes.put(affiliationId, totalVotes);

                    } else if (candidateId != -1) {
                        candidateVotes.put(candidateId, totalVotes);

                    }

                }


                parser.findAndAcceptEndTag(VALID_VOTES);

            }
            transformer.registerConstituency(constituencyData, affiliationVotes, candidateVotes );


        }

    }

}



