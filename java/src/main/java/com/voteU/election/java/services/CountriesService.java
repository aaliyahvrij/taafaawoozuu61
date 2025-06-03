package com.voteU.election.java.services;

import com.voteU.election.java.entities.Countries;
import com.voteU.election.java.repositories.CountriesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountriesService {

    private final CountriesRepository countriesRepository;

    public CountriesService(CountriesRepository countriesRepository) {
        this.countriesRepository = countriesRepository;
    }

    public List<Countries> getAllCountries(){
        return countriesRepository.findAll();
    }

    public Optional<Countries> getCountryById(Integer id){
        return countriesRepository.findCountryById(id);
    }

    public Optional<Countries> getCountryByCode(String code){
        return countriesRepository.findCountryByCode(code);
    }

    public Optional<Countries> getCountryByName(String name) {
        return countriesRepository.findCountryByName(name);
    }

}
