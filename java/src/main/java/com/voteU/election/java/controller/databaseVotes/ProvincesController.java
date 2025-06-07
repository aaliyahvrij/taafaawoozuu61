package com.voteU.election.java.controller.databaseVotes;


import com.voteU.election.java.dto.DropdownOptionDTO;
import com.voteU.election.java.services.ProvinceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/provinces")
public class ProvincesController {
    ProvinceService provinceService;

    public ProvincesController(ProvinceService provinceService) {
        this.provinceService = provinceService;
    }

    @GetMapping("{electionId}/names")
    List<DropdownOptionDTO<Integer>> getProvinceNames(@PathVariable String electionId) {
        return provinceService.getAllProvinceNames(electionId);
    }
}
