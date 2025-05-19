package com.voteU.election.java.dto;

import java.util.Date;

/**
 * Represents an error response with a message and a timestamp.
 * This record is typically used to provide a standardized structure
 * for transmitting error details to clients or logging systems.
 *
 * @param message   the error message describing the issue
 * @param timeStamp the timestamp indicating when the error occurred
 */
public record ErrorResponse(String message, Date timeStamp) {
}
