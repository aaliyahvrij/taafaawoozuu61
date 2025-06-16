package com.voteU.election.java.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * Represents a post in the application, which contains information such as a title, description,
 * body content, creation timestamp, and relationships with a user and comments.
 */
@Entity
@Table(name = "posts")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Posts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "title", nullable = false, length = 45)
    private String title;

    @Column(name = "description", nullable = false, length = 45)
    private String description;

    @Lob
    @Column(name = "body", nullable = false)
    private String body;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties("posts")  // To avoid back reference in User entity
    private User user;

    @OneToMany(mappedBy = "postsId")
    @JsonIgnore  // Or use @JsonManagedReference/@JsonBackReference pair
    private Set<Comments> comments = new LinkedHashSet<>();

}
