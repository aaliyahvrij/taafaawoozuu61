package com.voteU.election.java.services;

import com.voteU.election.java.entities.Countries;
import com.voteU.election.java.repositories.CountriesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * Service class for managing Countries entities. This service provides methods
 * to perform operations such as retrieving, searching, and accessing country data
 * through interactions with the CountriesRepository.
 */
@Service
public class CountriesService {

    private final CountriesRepository countriesRepository;

    public CountriesService(CountriesRepository countriesRepository) {
        this.countriesRepository = countriesRepository;
    }

    /**
     * Retrieves a list of all countries from the database.
     *
     * @return a list of Countries objects representing all countries available in the system.
     */
    public List<Countries> getAllCountries(){
        return countriesRepository.findAll();
    }

    /**
     * Retrieves a country entity based on the provided unique identifier.
     *
     * @param id the unique identifier of the country to retrieve
     * @return an Optional containing the Countries entity if found, or an empty Optional if no country exists with the specified id
     */
    public Optional<Countries> getCountryById(Integer id){
        return countriesRepository.findCountryById(id);
    }

    /**
     * Retrieves a country from the database based on the provided country code.
     *
     * @param code the unique code of the country to retrieve
     * @return an Optional containing the Countries entity if found, or an empty Optional if no country exists with the specified code
     */
    public Optional<Countries> getCountryByCode(String code){
        return countriesRepository.findCountryByCode(code);
    }

    /**
     * Retrieves a country entity from the database based on the provided name.
     *
     * @param name the name of the country to retrieve
     * @return an Optional containing the Countries entity if found, or an empty Optional if no country exists with the specified name
     */
    public Optional<Countries> getCountryByName(String name) {
        return countriesRepository.findCountryByName(name);
    }

}
