package com.voteU.election.java.controller;

import com.voteU.election.java.model.Party;
import com.voteU.election.java.model.Province;
import com.voteU.election.java.services.ProvinceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/provinces")
public class ProvinceController {

    private final ProvinceService provinceService;

    public ProvinceController(ProvinceService provinceService) {
        this.provinceService = provinceService;
    }

    @GetMapping("/{year}")
    public List<Province> getProvincesByYear(@PathVariable String year) {
        return provinceService.getProvinces(year);
    }

    @GetMapping("/api/province/{year}/{id}/results")
    public ResponseEntity<List<Party>> getProvinceResults(@PathVariable String year, @PathVariable int id) {
        List<Province> provinces = provinceService.getProvinces(year);

        Province province = provinces.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);

        if (province == null) {
            return ResponseEntity.notFound().build();
        }

        Map<Integer, Party> result = provinceService.getTotalVotesPerParty(province);
        return ResponseEntity.ok(new ArrayList<>(result.values()));
    }
}