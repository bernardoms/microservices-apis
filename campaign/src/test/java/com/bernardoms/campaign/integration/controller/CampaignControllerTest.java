package com.bernardoms.campaign.integration.controller;

import com.bernardoms.campaign.integration.IntegrationTest;
import com.bernardoms.campaign.model.dto.CampaignDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class CampaignControllerTest extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private static String URL_PATH = "/v1/campaigns";


    @Test
    public void test_create_campaign() throws Exception {
        var campaignDTO = CampaignDTO.builder().from(LocalDate.of(2017, 10, 1)).to(LocalDate.of(2020, 10, 3)).team_id("3").name("Campaign 4").build();

        mockMvc.perform(
                post(URL_PATH).content(mapper.writeValueAsString(campaignDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andExpect(header().exists("Location"));
    }

    @Test
    public void test_find_all_pageable_campaigns_with_all_filters() throws Exception {

        mockMvc.perform(
                get(URL_PATH).param("limit", "10").param("offset", "0").param("team_id", "1").param("name","Campaign 1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.content[0].name", is("Campaign 1")))
                .andExpect(jsonPath("$.content[0].from", is("2017-10-01")))
                .andExpect(jsonPath("$.content[0].to", is("2020-10-03")))
                .andExpect(jsonPath("$.content[0].team_id", is("1")));
    }

    @Test
    public void test_find_all_pageable_campaigns_with_no_filters() throws Exception {

        mockMvc.perform(
                get(URL_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name", is("Campaign 1")))
                .andExpect(jsonPath("$.content[0].from", is("2017-10-01")))
                .andExpect(jsonPath("$.content[0].to", is("2020-10-05")))
                .andExpect(jsonPath("$.content[0].team_id", is("1")))
                .andExpect(jsonPath("$.content[1].name", is("Campaign 2")))
                .andExpect(jsonPath("$.content[1].from", is("2017-10-01")))
                .andExpect(jsonPath("$.content[1].to", is("2020-10-04")))
                .andExpect(jsonPath("$.content[1].team_id", is("2")));
    }

    @Test
    public void test_find_a_campaign_by_id() throws Exception {

        mockMvc.perform(
                get(URL_PATH + "/507f191e810c19729de860ea")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Campaign 1")))
                .andExpect(jsonPath("$.from", is("2017-10-01")))
                .andExpect(jsonPath("$.to", is("2020-10-05")))
                .andExpect(jsonPath("$.team_id", is("1")));
    }

    @Test
    public void test_update_campaign() throws Exception {

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
