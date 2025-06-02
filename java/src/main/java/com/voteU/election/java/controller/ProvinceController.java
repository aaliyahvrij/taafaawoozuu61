package com.voteU.election.java.controller;

import com.voteU.election.java.CompactDTO.CompactConstituency;
import com.voteU.election.java.CompactDTO.CompactProvince;
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

    @GetMapping("/compact")
    public List<CompactProvince> getCompactProvinces(@PathVariable String year) {
        return provinceService.getCompactProvinces(year);
    }

    @GetMapping("/{provinceId}/votes")
    public int getTotalVotesForProvince(@PathVariable String year, @PathVariable int provinceId) {
        return provinceService.getTotalVotesForProvince(year, provinceId);
    }

    @GetMapping("/{provinceId}/constituencies")
    public List<Constituency> getConstituenciesByProvinceId(@PathVariable String year, @PathVariable int provinceId) {
        return provinceService.getConstituenciesByProvinceId(year, provinceId);
    }

    @GetMapping("/{provinceId}/constituencies/compact")
    public List<CompactConstituency> getCompactConstituenciesByProvinceId(@PathVariable String year, @PathVariable int provinceId) {
        return provinceService.getCompactConstituenciesByProvinceId(year, provinceId);
    }

    @GetMapping("/{provinceId}/parties")
    public Map<Integer, Party> getTotalVotesPerParty(@PathVariable String year, @PathVariable int provinceId) {
        return provinceService.getTotalVotesPerParty(year, provinceId);
    }


}
