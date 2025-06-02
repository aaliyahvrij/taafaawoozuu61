package com.voteU.election.java.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User {

    @GeneratedValue
    @Id
    private int id;
    private String username;
    private String email;
    private String first_name;
    private String last_name;
    private String password;
    private String timestamp;

    public User() {
    }
}
