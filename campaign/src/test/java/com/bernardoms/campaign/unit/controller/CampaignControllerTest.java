package com.bernardoms.campaign.unit.controller;

import com.bernardoms.campaign.controller.CampaignController;
import com.bernardoms.campaign.model.Campaign;
import com.bernardoms.campaign.model.dto.CampaignDTO;
import com.bernardoms.campaign.model.filter.CampaignFilter;
import com.bernardoms.campaign.service.CampaignService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
public class CampaignControllerTest {

    @InjectMocks
    private CampaignController campaignController;

    private MockMvc mockMvc;

    @Mock
    private CampaignService campaignService;

    private static String URL_PATH = "/v1/campaigns";

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        mockMvc = standaloneSetup(campaignController).setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }


    @Test
    public void test_create_campaign() throws Exception {
        mapper.registerModule(new JavaTimeModule());

        var campaignDTO = CampaignDTO.builder().from(LocalDate.of(2017, 10, 1)).to(LocalDate.of(2020, 10, 3)).team_id("3").name("Campaign 4").build();

        var campaign = Campaign.builder().id(new ObjectId("507f191e810c19729de860ea")).from(LocalDate.of(2017, 10, 1)).to(LocalDate.of(2020, 10, 3)).team_id("3").name("Campaign 4").build();

        when(campaignService.saveCampaign(campaignDTO)).thenReturn(campaign);

        mockMvc.perform(
                post(URL_PATH).content(mapper.writeValueAsString(campaignDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "http://localhost/v1/campaigns/507f191e810c19729de860ea"));
    }

    @Test
    public void test_find_all_pageable_campaigns() throws Exception {

        var filter = new CampaignFilter("Campaign 4", null, null, "3", 0, null);

        var campaignDTO = CampaignDTO.builder().from(LocalDate.of(2017, 10, 1)).to(LocalDate.of(2020, 10, 3)).team_id("3").name("Campaign 4").build();

        when(campaignService.getAllCampaigns(filter)).thenReturn(new PageImpl<>(Collections.singletonList(campaignDTO)));

        mockMvc.perform(
                get(URL_PATH).param("limit", "10").param("offset", "0").param("team_id", "3").param("name","Campaign 4")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.content[0].name", is("Campaign 4")))
                .andExpect(jsonPath("$.content[0].team_id", is("3")));
    }

    @Test
    public void test_find_a_campaign_by_id() throws Exception {

        var campaignDTO = CampaignDTO.builder().from(LocalDate.of(2017, 10, 1)).to(LocalDate.of(2020, 10, 3)).team_id("3").name("Campaign 4").build();

        when(campaignService.getCampaignById(any(ObjectId.class))).thenReturn(campaignDTO);

        mockMvc.perform(
                get(URL_PATH + "/507f191e810c19729de860ea")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Campaign 4")))
                .andExpect(jsonPath("$.team_id", is("3")));
    }

    @Test
    public void test_update_campaign() throws Exception {

        mapper.registerModule(new JavaTimeModule());

        var campaignDTO = CampaignDTO.builder().from(LocalDate.of(2017, 10, 1)).to(LocalDate.of(2020, 10, 3)).team_id("1").name("Campaign updated").build();

        mockMvc.perform(
                put(URL_PATH + "/507f191e810c19729de860eb").content(mapper.writeValueAsString(campaignDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_delete_campaign() throws Exception {
        mockMvc.perform(
                delete(URL_PATH + "/507f191e810c19729de860ec"))
                .andExpect(status().isNoContent());
    }
}
