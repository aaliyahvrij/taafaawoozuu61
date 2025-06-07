package com.voteU.election.java.controller;

import com.voteU.election.java.entities.Countries;
import com.voteU.election.java.exceptions.ResourceNotFoundException;
import com.voteU.election.java.services.CountriesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * CountriesController is a REST controller that provides endpoints for managing and
 * retrieving information about countries. This includes operations to fetch all countries
 * or fetch a specific country based on its ID, code, or name.
 */
@RestController
@RequestMapping("/api/countries")
public class CountriesController {

    private final CountriesService countriesService;

    public CountriesController(CountriesService countriesService) {
        this.countriesService = countriesService;
    }

    /**
     * Retrieves a list of all countries.
     *
     * @return a ResponseEntity containing a list of all Countries objects.
     */
    @GetMapping
    public ResponseEntity<List<Countries>> getAllCountries() {
        return ResponseEntity.ok(countriesService.getAllCountries());
    }

    /**
     * Retrieves the details of a specific country based on its unique identifier (ID).
     *
     * @param id the unique identifier of the country to retrieve
     * @return a ResponseEntity containing the country details if found
     * @throws ResourceNotFoundException if no country is found with the specified ID
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<Countries> getCountriesById(@PathVariable Integer id) {
        return countriesService.getCountryById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with id " + id));
    }

    /**
     * Retrieves a country based on its unique code from the database.
     *
     * @param code the country code used to identify the country, must be a valid two-character string
     * @return a ResponseEntity containing the country if found
     * @throws ResourceNotFoundException if no country is found with the specified code
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<Countries> getCountriesByCode(@PathVariable String code) {
        return countriesService.getCountryByCode(code)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with code " + code));
    }

    /**
     * Retrieves a country by its name.
     *
     * @param name the name of the country to retrieve
     * @return a ResponseEntity containing the country details if found
     * @throws ResourceNotFoundException if no country with the specified name is found
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<Countries> getCountriesByName(@PathVariable String name) {
        return countriesService.getCountryByName(name)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with name " + name));
    }
}
