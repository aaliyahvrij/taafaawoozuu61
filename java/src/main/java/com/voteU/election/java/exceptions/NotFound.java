package com.voteU.election.java.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * NotFound is a custom exception class used to indicate that a requested resource
 * or entity was not found.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFound extends RuntimeException {
    public NotFound(String message) {
        super(message);
    }
}

