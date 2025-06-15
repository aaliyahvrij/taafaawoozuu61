package com.voteU.election.java.HttpTest;

import com.voteU.election.java.controller.ProvinceController;
import com.voteU.election.java.model.Province;
import com.voteU.election.java.services.ProvinceService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProvinceController.class)
class ProvinceHttpTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ProvinceService provinceService;

    @Test
    void testGetProvincesReturns200() throws Exception {
        List<Province> provinces = List.of(new Province(1, "Test Province"));
        when(provinceService.getProvinces("2023")).thenReturn(provinces);

        mockMvc.perform(get("/provinces/2023"))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$[0].name").value("Test Province"));
    }
}
