package com.voteU.election.java.dto;

import java.util.Date;

public record ErrorResponse(String message, Date timeStamp) {
}
