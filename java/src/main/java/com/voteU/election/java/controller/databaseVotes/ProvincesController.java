package com.voteU.election.java.controller.databaseVotes;

import com.voteU.election.java.dto.DropdownOptionDTO;
import com.voteU.election.java.services.ProvinceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/provinces")
public class ProvincesController {

    private final ProvinceService provinceService;

    public ProvincesController(ProvinceService provinceService) {
        this.provinceService = provinceService;
    }

    @GetMapping()
    public List<DropdownOptionDTO<Integer>> getProvinces(@RequestParam String electionId) {
        return provinceService.getAllProvinceNames(electionId);
    }
}
