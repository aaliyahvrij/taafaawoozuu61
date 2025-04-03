package com.voteU.election.java.reader;

import com.voteU.election.java.model.Election;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.utils.PathUtils;
import com.voteU.election.java.utils.xml.DutchElectionProcessor;
import com.voteU.election.java.utils.xml.Transformer;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.Map;

/**
 * A very small demo of how the classes {@link DutchElectionProcessor} and {@link Transformer}
 * can be used to process the XML-files.
 * <br>
 * <b>Please do NOT include this code in you project!</b>
 */
@Component
public class DutchElectionReader {
    private final  DutchElectionTransformer transformer;
    private final DutchElectionProcessor<Election> electionProcessor;

    public DutchElectionReader() {
        this.transformer = new DutchElectionTransformer();
        this.electionProcessor = new DutchElectionProcessor<>(transformer);
    }

    public boolean readElections() {
        try {
            // Dynamically generate the path

            String resourceName = "/EML_bestanden_TK2021/Kandidatenlijst/Kandidatenlijsten_TK2021_Amsterdam.eml.xml";

            // Process results
            Election election = electionProcessor.processResults("TK2021", PathUtils.getResourcePath(resourceName));

            System.out.println("All files processed for folder: " + resourceName);
            return true;
        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map<Integer, Party> getAll(){
        return transformer.getParties();
    }

}
