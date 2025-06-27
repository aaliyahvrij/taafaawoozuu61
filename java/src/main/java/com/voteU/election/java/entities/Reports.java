package com.voteU.election.java.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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
}
