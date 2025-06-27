package com.voteU.election.java.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;


/**
 * Represents a user entity in the system.
 * This class maps to the "user" table in the database and contains
 * information regarding users such as their username, email, name, gender, country,
 * password, and account creation timestamp.
 */
@Entity
@Table(name = "user")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "username", nullable = false, length = 45)
    private String username;

    @Column(name = "email", nullable = false, length = 45)
    private String email;

    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;

    @Lob
    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "country", nullable = false, length = 2)
    private String country;

    @Column(name = "password", nullable = false, length = 45)
    private String password;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "is_blocked", nullable = false)
    private boolean isBlocked = false;

    public User() {
    }
}

