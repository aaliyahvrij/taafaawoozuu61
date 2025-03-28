package com.voteU.election.java;

import com.voteU.election.java.services.ElectionService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

@SpringBootApplication
public class JavaApplication {

	public static void main(String[] args) throws XMLStreamException, IOException {
		SpringApplication.run(JavaApplication.class, args);
	}

}
