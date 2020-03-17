package com.bernardoms.clubmemberapi.unit.controller;

import com.bernardoms.clubmemberapi.controller.ClubMemberController;
import com.bernardoms.clubmemberapi.model.dto.CampaignDTO;
import com.bernardoms.clubmemberapi.model.dto.ClubMemberDTO;
import com.bernardoms.clubmemberapi.model.dto.TeamDTO;
import com.bernardoms.clubmemberapi.service.ClubMemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
public class ClubMemberControllerTest {
    private static String URL_PATH = "/v1/clubmembers";

    private ObjectMapper mapper = new ObjectMapper();

    private MockMvc mockMvc;

    @InjectMocks
    private ClubMemberController clubMemberController;

    @Mock
    private ClubMemberService clubMemberService;
    @BeforeEach
    public void setUp() {
        mockMvc = standaloneSetup(clubMemberController).build();
    }


    @Test
    public void test_create_club_member() throws Exception {
        mapper.registerModule(new JavaTimeModule());

        var campaignDTO = CampaignDTO.builder().to(LocalDate.of(2099,10,10)).from(LocalDate.of(2017,10,10)).name("campaign teste").build();

        var teamDTO = TeamDTO.builder().championship("Serie A").campaigns(Collections.singletonList(campaignDTO)).name("Vasco").build();

        var clubMember = ClubMemberDTO.builder().fullName("test test2").email("test@test.com").birthDate(LocalDate.of(1990,10,10)).team(teamDTO).build();

        when(clubMemberService.associateTeamWithCampaigns(any(ClubMemberDTO.class))).thenReturn(clubMember);

        mockMvc.perform(
                post(URL_PATH).content(mapper.writeValueAsString(clubMember)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName", is("test test2")))
                .andExpect(jsonPath("$.email", is("test@test.com")))
                .andExpect(jsonPath("$.team.name", is("Vasco")))
                .andExpect(jsonPath("$.team.championship", is("Serie A")))
                .andExpect(jsonPath("$.team.campaigns[0].name", is("campaign teste")));
    }
}
