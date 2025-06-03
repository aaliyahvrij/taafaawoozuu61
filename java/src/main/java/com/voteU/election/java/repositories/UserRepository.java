package com.voteU.election.java.repositories;

import com.voteU.election.java.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserById(Integer id);

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);

    void deleteById(Integer id);
}

