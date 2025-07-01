package com.voteU.election.java.controller.database;


import com.voteU.election.java.dto.DropdownOptionDTO;
import com.voteU.election.java.services.ConstituencyService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("api/constituencies")
public class ConstituenciesController {

    private final ConstituencyService constituencyService;

    public ConstituenciesController(ConstituencyService constituencyService) {
        this.constituencyService = constituencyService;
    }

    @GetMapping("/all")
    public List<DropdownOptionDTO<Integer>> getConstituencies(@RequestParam String electionId) {
        return constituencyService.getAllConstituencyNames(electionId);
    }

    @GetMapping()
    public List<DropdownOptionDTO<Integer>> getConstituenciesByProvinceId(@RequestParam String electionId, @RequestParam Integer provinceId) {
        return constituencyService.getAllConstituencyNamesByProvince(electionId, provinceId);
    }


}
