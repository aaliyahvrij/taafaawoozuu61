package com.voteU.election.java;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

@Slf4j
@SpringBootApplication
public class JavaApplication {
    public static void main(String[] args) throws XMLStreamException, IOException {
        SpringApplication.run(JavaApplication.class, args);
    }
}
