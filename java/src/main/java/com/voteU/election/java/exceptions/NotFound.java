package com.voteU.election.java.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * A custom exception that represents a "Not Found" condition (HTTP 404 status).
 * This exception is intended to be thrown when a requested resource is not found.
 * It extends the {@link RuntimeException} class and is annotated with
 * {@link ResponseStatus} to map it to the HTTP status code 404 (Not Found).
 *
 * This exception can carry a detailed error message provided during instantiation.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFound extends RuntimeException {
  public NotFound(String message) {
    super(message);
  }
}
