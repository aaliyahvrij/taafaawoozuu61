package com.voteU.election.java;

import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.voteU.election.java.reader.DutchElectionReader;
import com.voteU.election.java.services.ElectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.GsonBuilderUtils;

import javax.crypto.spec.PSource;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;

@Slf4j
@SpringBootApplication
public class JavaApplication {

	public static void main(String[] args) throws XMLStreamException, IOException {
		SpringApplication.run(JavaApplication.class, args);

	}

	@Bean
	public Hibernate6Module hibernate6Module() {
		return new Hibernate6Module();
	}


}
