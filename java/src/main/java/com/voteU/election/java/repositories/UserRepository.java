package com.voteU.election.java.repositories;

import com.voteU.election.java.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

/**
 * This interface manages database operations for the User entity.
 * It extends JpaRepository to provide standard CRUD methods and defines
 * additional query methods specific to User.
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserById(Integer id);

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByUsernameAndPassword(String username, String password);

    Optional<User> findUserByEmail(String email);

    void deleteById(Integer id);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}

