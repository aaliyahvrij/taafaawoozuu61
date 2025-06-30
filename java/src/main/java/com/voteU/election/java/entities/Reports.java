package com.voteU.election.java.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Represents a report entity in the system.
 * This class maps to the "reports" table in the database and is used for handling
 * reports submitted by users about other users or posts. It contains information
 * about the reporter, the reported user or post, the reason for the report,
 * and the timestamp for when the report was created.
 *
 * The relationships include:
 * - A many-to-one association with the User entity for the reporter.
 * - A many-to-one association with the User entity for the reported user.
 * - A many-to-one association with the Posts entity for the reported post.
 */
@Entity
@Getter
@Setter
@Table(name = "reports")
public class Reports {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "reporter_user_id", referencedColumnName = "id")
    private User reporter;

    @ManyToOne
    @JoinColumn(name = "reported_user_id", referencedColumnName = "id")
    private User reported;

    private String reason;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Posts post;
}
