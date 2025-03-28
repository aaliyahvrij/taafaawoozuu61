package com.voteU.election.java.reader;

import com.voteU.election.java.model.Election;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.utils.PathUtils;
import com.voteU.election.java.utils.xml.DutchElectionProcessor;
import com.voteU.election.java.utils.xml.Transformer;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.Map;

/**
 * A very small demo of how the classes {@link DutchElectionProcessor} and {@link Transformer}
 * can be used to process the XML-files.
 * <br>
 * <b>Please do NOT include this code in you project!</b>
 */
public class DutchElectionReader {

    public static void main(String[] args) throws IOException, XMLStreamException {
        System.out.println("Processing files...");

        // We need a Transformer that has knowledge of your classes.
        DutchElectionTransformer transformer = new DutchElectionTransformer();

        // And the election processor that traverses the folders and processes the XML-files.
        DutchElectionProcessor<Election> electionProcessor = new DutchElectionProcessor<>(transformer);

        // Assuming the election data is contained in {@code src/main/resource} it should be found.
        // Please note that you can also specify an absolute path to the folder!

        String resourceName = "/verkiezingsdata/2021/Kandidatenlijst_2021/EML_bestanden_TK2021_deel_1/Kandidatenlijsten_TK2021_Amsterdam.eml.xml";
        Election election = electionProcessor.processResults("TK2021", PathUtils.getResourcePath(resourceName));

        System.out.println("All files are processed.\n");
        // Just print the 'results'
        //System.out.println(election);

        Map<Integer, Party> parties = transformer.getParties(); //HASHMAP PARTIJ OBJECTEN<ID, {PARTY}>

        for (Party party : parties.values()) {
            System.out.println(party);
        }


    }

}
