package com.voteU.election.java.utils.xml;

import javax.xml.stream.*;
import javax.xml.stream.util.StreamReaderDelegate;
import java.io.*;
import java.util.logging.*;

/**
 * The XMLParser does the low-level and has default scope on purpose because it is to be used only
 * by an instance of DutchElectionProcessor!
 */
class XMLParser extends StreamReaderDelegate {
    private static final Logger LOG = Logger.getLogger(XMLParser.class.getName());

    static {
        try {
            LogManager.getLogManager().readConfiguration(XMLParser.class.getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            System.err.println("Unable to configure logging sub-system, can't proceed.");
            e.printStackTrace();
        }
    }

    XMLParser(InputStream inputStream) {
        super(createXMLStreamReader(inputStream));
    }

    static XMLStreamReader createXMLStreamReader(InputStream input) {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLStreamReader xmlStreamReader = null;
        try {
            xmlStreamReader = inputFactory.createXMLStreamReader(input);
        } catch (XMLStreamException e) {
            LOG.severe("Cannot attach XMLStreamReader to file stream content:\n" + e);
        }
        return xmlStreamReader;
    }

    boolean tryNext() throws XMLStreamException {
        if (hasNext()) {
            next();
            return true;
        }
        return false;
    }

    boolean nextBeginTag(String tag) throws XMLStreamException {
        int fromEvent = getEventType();
        int skipCount = 0;
        while (!isStartElement() && !isEndElement() && hasNext()) {
            if (!isWhiteSpace()) {
                skipCount++;
            }
            next();
        }
        int toEvent = getEventType();
        String hit = (isStartElement() ? getLocalName() : "/");

        if (skipCount > 0 && !hit.equals(tag)) {
            LOG.finer("nextBeginTag(" + tag + "): skipped " + skipCount +
                    " from event" + fromEvent + " to event" + toEvent + " hit <" + hit + ">");
        }
        return hit.equals(tag);
    }

    String nextEndTag() throws XMLStreamException {
        while (!isEndElement() && hasNext()) {
            next();
        }
        return (isEndElement() ? getLocalName() : "/");
    }

    boolean nextEndTag(String tag) throws XMLStreamException {
        int fromEvent = getEventType();
        int skipCount = 0;
        while (!isEndElement() && hasNext()) {
            if (!isWhiteSpace()) {
                skipCount++;
            }
            next();

        }
        int toEvent = getEventType();
        String hit = (isEndElement() ? getLocalName() : "/");
        if (skipCount > 0 && !tag.equals(hit)) {
            LOG.finer("nextEndTag(" + tag + "): skipped " + skipCount +
                    " from event" + fromEvent + " to event" + toEvent + " hit </" + hit + ">");
        }
        return tag.equals(hit);
    }

    boolean findBeginTag(String tag) throws XMLStreamException {
        boolean hit = nextBeginTag(tag);
        while (!hit && hasNext()) {
            next();
            hit = nextBeginTag(tag);
        }
        return hit;
    }

    boolean findAndAcceptEndTag(String tag) throws XMLStreamException {
        boolean hit = nextEndTag(tag);
        while (!hit && hasNext()) {
            next();
            hit = nextEndTag(tag);
        }
        if (hit) {
            next();
        }
        return hit;
    }

    boolean skipMandatoryElement(String tag) throws XMLStreamException {
        if (findBeginTag(tag)) {
            findAndAcceptEndTag(tag);
            return true;
        }
        return false;
    }

    double getDoubleAttributeValue(String ns, String name, double defaultValue) {
        String value = getAttributeValue(ns, name);
        return (value != null ? Double.parseDouble(value) : defaultValue);
    }

    int getIntegerAttributeValue(String ns, String name, int defaultValue) {
        String value = getAttributeValue(ns, name);
        if (value != null) {
            if ("alle".equalsIgnoreCase(value)) {
                // Handle the special case where the ID is "alle" (everything)
                LOG.info("Attribute '" + name + "' has 'alle' (Totaaltelling). Returning a special marker value: 12345.");
                return 12345; // Use -1 or another special marker value to indicate "all contests"
            }
            try {
                return Integer.parseInt(value); // Try parsing normally if not "alle"
            } catch (NumberFormatException e) {
                // Log a warning and return the default value
                LOG.warning("Expected an integer for attribute '" + name + "' but found: " + value
                        + ". Using default value: " + defaultValue);
            }
        }
        return defaultValue; // Return default if null or parsing fails
    }

    void logStatus() {
        LOG.config("start=" + isStartElement() +
                " end=" + isEndElement() +
                " chars=" + isCharacters() +
                " whitespace=" + isWhiteSpace() +
                " name=" + getLocalName());
    }
}
