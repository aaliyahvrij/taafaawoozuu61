package com.voteU.election.java.reader;

import com.voteU.election.java.model.Election;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.utils.PathUtils;
import com.voteU.election.java.utils.xml.DutchElectionProcessor;
import com.voteU.election.java.utils.xml.Transformer;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * A very small demo of how the classes {@link DutchElectionProcessor} and {@link Transformer}
 * can be used to process the XML-files.
 * <br>
 * <b>Please do NOT include this code in you project!</b>
 */
public class DutchElectionReader {

    public static void main(String[] args) throws IOException, XMLStreamException {

        // We need a Transformer that has knowledge of your classes.
        DutchElectionTransformer transformer = new DutchElectionTransformer();

        // And the election processor that traverses the folders and processes the XML-files.
        DutchElectionProcessor<ArrayList<Election>> electionProcessor = new DutchElectionProcessor<>(transformer);

        // Assuming the election data is contained in {@code src/main/resource} it should be found.
        // Please note that you can also specify an absolute path to the folder!

        String[] electionYears = {"TK2021","TK2023"};
        // Lijst met categorieën (gemeente, kandidatenlijst, kieskring)
        String[] categories = {"gemeente", "kandidatenlijst"};

        // Doorloop elk jaar en elke categorie
        for (String year : electionYears) {
            for (String category : categories) {
                String folderPath = "/EML_bestanden_" + year + "/" + category;

                try {
                    System.out.println("Processing: " + folderPath);
                    ArrayList<Election> election = electionProcessor.processResults(year, PathUtils.getResourcePath(folderPath));
                    System.out.println(election);

                    System.out.println("Finished processing: " + folderPath);
                } catch (Exception e) {
                    System.err.println("Error processing " + folderPath + ": " + e.getMessage());
                }


            }
        }


        Map<Integer, Party> parties = transformer.getParties(); //HASHMAP PARTIJ OBJECTEN<ID, {PARTY}>

        for (Party party : parties.values()) {
            System.out.println(party);
        }


    }

}
