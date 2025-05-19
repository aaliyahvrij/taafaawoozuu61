package com.voteU.election.java.controller;

import com.voteU.election.java.model.Constituency;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.model.Province;
import com.voteU.election.java.services.ProvinceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/election/{year}/provinces")
public class ProvinceController {

    private final ProvinceService provinceService;

    public ProvinceController(ProvinceService provinceService) {
        this.provinceService = provinceService;
    }

    @GetMapping
    public List<Province> getProvinces(@PathVariable String year) {
        return provinceService.getProvinces(year);
    }

    @GetMapping("/{provinceId}/constituencies")
    public List<Constituency> getConstituenciesByProvinceId(@PathVariable String year, @PathVariable int provinceId) {
        return provinceService.getConstituenciesByProvinceId(year, provinceId);
    }

    @GetMapping("/{provinceId}/parties")
    public Map<Integer, Party> getTotalVotesPerParty(@PathVariable String year, @PathVariable int provinceId) {
        return provinceService.getTotalVotesPerParty(year, provinceId);
    }


}
