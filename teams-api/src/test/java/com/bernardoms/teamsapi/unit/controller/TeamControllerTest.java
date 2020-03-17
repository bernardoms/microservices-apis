package com.bernardoms.teamsapi.unit.controller;

import com.bernardoms.teamsapi.config.Config;
import com.bernardoms.teamsapi.controller.TeamController;
import com.bernardoms.teamsapi.exception.TeamNotFoundException;
import com.bernardoms.teamsapi.model.dto.CampaignDTO;
import com.bernardoms.teamsapi.model.dto.TeamDTO;
import com.bernardoms.teamsapi.model.filter.TeamFilter;
import com.bernardoms.teamsapi.service.TeamService;
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
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
public class TeamControllerTest {

    @InjectMocks
    private TeamController teamController;

    private MockMvc mockMvc;

    @Mock
    private TeamService teamService;

    private static String URL_PATH = "/v1/teams";

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        mockMvc = standaloneSetup(teamController).setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }


    @Test
    public void test_create_campaign() throws Exception {
        mapper.registerModule(new JavaTimeModule());

        var teamDTO = TeamDTO.builder().name("Portuguesa").championship("Serie B").build();

        when(teamService.saveTeam(teamDTO)).thenReturn(new ObjectId("507f191e810c19729de860ea"));

        mockMvc.perform(
                post(URL_PATH).content(mapper.writeValueAsString(teamDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "http://localhost/v1/teams/507f191e810c19729de860ea"));
    }

    @Test
    public void test_find_all_pageable_campaigns() throws Exception {

        var filter = new TeamFilter("Portuguesa", "Serie B", null, 3);

        var teamDTO = TeamDTO.builder().name("Portuguesa").championship("Serie B").build();

        when(teamService.getAll(filter)).thenReturn(new PageImpl<>(Collections.singletonList(teamDTO)));

        mockMvc.perform(
                get(URL_PATH).param("limit", "3").param("name", "Portuguesa").param("championship","Serie B")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.content[0].name", is("Portuguesa")))
                .andExpect(jsonPath("$.content[0].championship", is("Serie B")));
    }

    @Test
    public void test_find_a_campaign_by_id() throws Exception {

        var teamDTO = TeamDTO.builder().name("Portuguesa").championship("Serie B").build();

        when(teamService.getTeamById(any(ObjectId.class))).thenReturn(teamDTO);

        mockMvc.perform(
                get(URL_PATH + "/507f191e810c19729de860ea")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Portuguesa")))
                .andExpect(jsonPath("$.championship", is("Serie B")));
    }

    @Test
    public void test_update_campaign() throws Exception {

        mapper.registerModule(new JavaTimeModule());

        var teamDTO = TeamDTO.builder().name("Portuguesa").championship("Serie B").build();

        mockMvc.perform(
                put(URL_PATH + "/507f191e810c19729de860eb").content(mapper.writeValueAsString(teamDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_delete_campaign() throws Exception {
        mockMvc.perform(
                delete(URL_PATH + "/507f191e810c19729de860ec"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_associate_team_with_campaigns() throws Exception {
        var campaign1 = CampaignDTO.builder().from(LocalDate.of(2017,10,10)).to(LocalDate.of(2020, 10, 10)).name("Campaign test1").team_id("1").build();
        var campaign2 = CampaignDTO.builder().from(LocalDate.of(2017,10,10)).to(LocalDate.of(2020, 10, 11)).name("Campaign test2").team_id("1").build();


        var teamDTO = TeamDTO.builder().name("Portuguesa").championship("Serie B").campaigns(Arrays.asList(campaign1, campaign2)).build();

        when(teamService.getTeamWithCampaignAssociate(any(String.class))).thenReturn(teamDTO);

        mockMvc.perform(
                get(URL_PATH + "/507f191e810c19729de860ea/associate/campaigns"))
                .andExpect(jsonPath("$.name", is("Portuguesa")))
                .andExpect(jsonPath("$.championship", is("Serie B")))
                .andExpect(jsonPath("$.campaigns[0].name", is("Campaign test1")))
                .andExpect(jsonPath("$.campaigns[1].name", is("Campaign test2")));
    }
}
