package com.voteU.election.java.exceptions;

/**
 * NotFound is a custom exception class used to indicate that a requested resource
 * or entity was not found.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
