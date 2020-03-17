package com.bernardoms.teamsapi.integration.controller;

import com.bernardoms.teamsapi.integration.IntegrationTest;
import com.bernardoms.teamsapi.model.Team;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class TeamControllerTest extends IntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private static String URL_PATH = "/v1/teams";

    @Test
    public void test_create_new_team() throws Exception {
        var teamDTO = Team.builder().name("Portuguesa").championship("Serie B").build();

        mockMvc.perform(
                post(URL_PATH).content(mapper.writeValueAsString(teamDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andExpect(header().exists("Location"));
    }

    @Test
    public void test_create_new_team_already_exists() throws Exception {
        var teamDTO = Team.builder().name("vasco").championship("Serie B").build();

        mockMvc.perform(
                post(URL_PATH).content(mapper.writeValueAsString(teamDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void test_find_all_pageable_teams_with_all_filters() throws Exception {

        mockMvc.perform(
                get(URL_PATH).param("limit", "10").param("offset", "0").param("name", "vasco").param("championship","Serie A")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.content[0].name", is("vasco")))
                .andExpect(jsonPath("$.content[0].championship", is("Serie A")));
    }

    @Test
    public void test_find_all_pageable_teams_with_no_filters() throws Exception {

        mockMvc.perform(
                get(URL_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name", is("vasco")))
                .andExpect(jsonPath("$.content[0].championship", is("Serie A")))
                .andExpect(jsonPath("$.content[1].name", is("flamengo")))
                .andExpect(jsonPath("$.content[1].championship", is("Serie A")))
                .andExpect(jsonPath("$.content[2].name", is("fluminense")))
                .andExpect(jsonPath("$.content[2].championship", is("Serie A")))
                .andExpect(jsonPath("$.content[3].name", is("botafogo")))
                .andExpect(jsonPath("$.content[3].championship", is("Serie A")));
    }

    @Test
    public void test_find_a_team_by_id() throws Exception {

        mockMvc.perform(
                get(URL_PATH + "/507f191e810c19729de860ea")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("vasco")))
                .andExpect(jsonPath("$.championship", is("Serie A")));
    }

    @Test
    public void test_find_a_team_by_id_not_found() throws Exception {

        mockMvc.perform(
                get(URL_PATH + "/507f191e810c19729de860aa")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void test_update_campaign() throws Exception {

        var campaignDTO = Team.builder().name("Fluminense").championship("Serie B").build();

        mockMvc.perform(
                put(URL_PATH + "/507f191e810c19729de860ec").content(mapper.writeValueAsString(campaignDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_delete_campaign() throws Exception {
        mockMvc.perform(
                delete(URL_PATH + "/507f191e810c19729de860ed"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_associate_teams_to_campaigns() throws Exception {

        mockMvc.perform(
                get(URL_PATH + "/vasco/associate/campaigns").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("vasco")))
                .andExpect(jsonPath("$.championship", is("Serie A")))
                .andExpect(jsonPath("$.campaigns[0].name", is("Campaign 1")))
                .andExpect(jsonPath("$.campaigns[0].from", is("2017-01-10")))
                .andExpect(jsonPath("$.campaigns[0].to", is("2020-10-02")))
                .andExpect(jsonPath("$.campaigns[1].name", is("Campaign 2")))
                .andExpect(jsonPath("$.campaigns[1].from", is("2017-01-10")))
                .andExpect(jsonPath("$.campaigns[1].to", is("2020-10-01")));
    }

    @Test
    public void test_associate_teams_to_campaigns_not_found() throws Exception {

        mockMvc.perform(
                get(URL_PATH + "/507f191e810c19729de860aa/associate/campaigns").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
