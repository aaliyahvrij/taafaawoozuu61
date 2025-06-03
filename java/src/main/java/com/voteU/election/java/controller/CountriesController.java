package com.voteU.election.java.controller;

import com.voteU.election.java.entities.Countries;
import com.voteU.election.java.exceptions.NotFound;
import com.voteU.election.java.services.CountriesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
public class CountriesController {

    private final CountriesService countriesService;

    public CountriesController(CountriesService countriesService) {
        this.countriesService = countriesService;
    }

    @GetMapping
    public ResponseEntity<List<Countries>> getAllCountries() {
        return ResponseEntity.ok(countriesService.getAllCountries());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Countries> getCountriesById(@PathVariable Integer id) {
        return countriesService.getCountryById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFound("Country not found with id " + id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Countries> getCountriesByCode(@PathVariable String code) {
        return countriesService.getCountryByCode(code)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFound("Country not found with code " + code));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Countries> getCountriesByName(@PathVariable String name) {
        return countriesService.getCountryByName(name)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFound("Country not found with name " + name));
    }
}
