package com.voteU.election.java.dto;

import lombok.Getter;
import lombok.Setter;


/**
 * LoginDTO represents a data transfer object used for user authentication requests.
 * It contains the basic credentials required for user login.
 *
 * This object is typically used in the login process to encapsulate the username
 * and password provided by the user.
 */
@Getter
@Setter
public class LoginDTO {
    private String username;
    private String password;
}
