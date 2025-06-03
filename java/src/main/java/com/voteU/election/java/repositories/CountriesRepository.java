package com.voteU.election.java.repositories;

import com.voteU.election.java.entities.Countries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountriesRepository extends JpaRepository<Countries, Integer> {

    Optional<Countries> findCountryById(Integer id);
    Optional<Countries> findCountryByCode(String code);
    Optional<Countries> findCountryByName(String name);
}
