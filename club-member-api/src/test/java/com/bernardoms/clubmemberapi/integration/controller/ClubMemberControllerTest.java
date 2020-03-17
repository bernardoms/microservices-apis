package com.bernardoms.clubmemberapi.integration.controller;

import com.bernardoms.clubmemberapi.config.Config;
import com.bernardoms.clubmemberapi.integration.IntegrationTest;
import com.bernardoms.clubmemberapi.model.dto.ClubMemberDTO;
import com.bernardoms.clubmemberapi.model.dto.TeamDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class ClubMemberControllerTest extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private static String URL_PATH = "/v1/clubmembers";

    @Test
    public void test_create_new_club_member() throws Exception {

        var clubMemberDTO = ClubMemberDTO.builder().email("test2@test.com").birthDate(LocalDate.of(1990, 10, 10)).fullName("testing testeee").team(TeamDTO.builder().name("Vasco").build()).build();

        mockMvc.perform(
                post(URL_PATH).content(mapper.writeValueAsString(clubMemberDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.birthDate", is("1990-10-10")))
                .andExpect(jsonPath("$.fullName", is("testing testeee")))
                .andExpect(jsonPath("$.email", is("test2@test.com")))
                .andExpect(jsonPath("$.team.name", is("Vasco")))
                .andExpect(jsonPath("$.team.championship", is("Serie A")))
                .andExpect(jsonPath("$.team.campaigns[0].name", is("Campaign 1")))
                .andExpect(jsonPath("$.team.campaigns[0].from", is("2017-01-10")))
                .andExpect(jsonPath("$.team.campaigns[0].to", is("2020-10-02")))
                .andExpect(jsonPath("$.team.campaigns[1].name", is("Campaign 2")))
                .andExpect(jsonPath("$.team.campaigns[1].from", is("2017-01-10")))
                .andExpect(jsonPath("$.team.campaigns[1].to", is("2020-10-01")));
    }

    @Test
    public void test_create_with_existing_club_member() throws Exception {
        var clubMemberDTO = ClubMemberDTO.builder().email("test@test.com").birthDate(LocalDate.of(1990, 10, 10)).team(TeamDTO.builder().name("Vasco").build()).fullName("testing test").build();

        mockMvc.perform(
                post(URL_PATH).content(mapper.writeValueAsString(clubMemberDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.birthDate", is("1990-10-10")))
                .andExpect(jsonPath("$.fullName", is("testing test")))
                .andExpect(jsonPath("$.email", is("test@test.com")))
                .andExpect(jsonPath("$.team.name", is("Vasco")))
                .andExpect(jsonPath("$.team.championship", is("Serie A")))
                .andExpect(jsonPath("$.team.campaigns[0].name", is("Campaign 1")))
                .andExpect(jsonPath("$.team.campaigns[0].from", is("2017-01-10")))
                .andExpect(jsonPath("$.team.campaigns[0].to", is("2020-10-02")))
                .andExpect(jsonPath("$.team.campaigns[1].name", is("Campaign 2")))
                .andExpect(jsonPath("$.team.campaigns[1].from", is("2017-01-10")))
                .andExpect(jsonPath("$.team.campaigns[1].to", is("2020-10-01")));
    }
}

