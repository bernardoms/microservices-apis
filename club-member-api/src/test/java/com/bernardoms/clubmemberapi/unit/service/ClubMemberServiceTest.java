package com.bernardoms.clubmemberapi.unit.service;

import com.bernardoms.clubmemberapi.config.Config;
import com.bernardoms.clubmemberapi.mapper.Mapper;
import com.bernardoms.clubmemberapi.model.ClubMember;
import com.bernardoms.clubmemberapi.model.dto.CampaignDTO;
import com.bernardoms.clubmemberapi.model.dto.ClubMemberDTO;
import com.bernardoms.clubmemberapi.model.dto.TeamDTO;
import com.bernardoms.clubmemberapi.repository.ClubMemberRepository;
import com.bernardoms.clubmemberapi.service.ClubMemberService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClubMemberServiceTest {
    @Mock
    private ClubMemberRepository clubMemberRepository;

    @Mock
    private Config config;

    @Mock
    private Mapper<ClubMember, ClubMemberDTO> mapper;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ClubMemberService clubMemberService;

    @Test
    public void test_associate_club_member() {
        var campaignDTO = CampaignDTO.builder().to(LocalDate.of(2099,10,10)).from(LocalDate.of(2017,10,10)).name("campaign teste").build();

        var teamDTO = TeamDTO.builder().championship("Serie A").id("507f191e810c19729de860eb").campaigns(Collections.singletonList(campaignDTO)).name("Vasco").build();

        var clubMemberDTO = ClubMemberDTO.builder().fullName("test test2").email("test@test.com").team(TeamDTO.builder().name("vasco").build()).birthDate(LocalDate.of(1990,10,10)).build();

        when(config.getTeamEndpointAPI()).thenReturn("http://test/v1");

        when(restTemplate.getForEntity(eq("http://test/v1/teams/vasco/associate/campaigns"), eq(TeamDTO.class))).thenReturn(new ResponseEntity<>(teamDTO, HttpStatus.OK));

        ClubMemberDTO clubMemberResult = clubMemberService.associateTeamWithCampaigns(clubMemberDTO);

        assertEquals(clubMemberResult.getEmail(), "test@test.com");
        assertEquals(clubMemberResult.getFullName(), "test test2");
        assertEquals(clubMemberResult.getTeam(), teamDTO);
    }

    @Test
    public void test_create_club_member() {
        var clubMemberDTO = ClubMemberDTO.builder().fullName("test test2").email("test@test.com").birthDate(LocalDate.of(1990,10,10)).build();

        var clubMember = ClubMember.builder().fullName("test test2").email("test@test.com").teamName("Vasco").birthDate(LocalDate.of(1990,10,10)).id(new ObjectId("507f191e810c19729de860eb")).build();

        when(clubMemberRepository.findByEmail("test@test.com")).thenReturn(Optional.of(clubMember));

        clubMemberService.createClubMember(clubMemberDTO);

        verify(clubMemberRepository, never()).save(any(ClubMember.class));
    }

    @Test
    public void test_should_not_create_club_member() {
        var clubMemberDTO = ClubMemberDTO.builder().fullName("test test2").email("test@test.com").birthDate(LocalDate.of(1990,10,10)).build();

        when(mapper.mapToDocument(clubMemberDTO)).thenReturn(ClubMember.builder().fullName("test test2").email("test@test.com").teamName("Vasco").birthDate(LocalDate.of(1990,10,10)).id(new ObjectId("507f191e810c19729de860eb")).build());
        clubMemberService.createClubMember(clubMemberDTO);

        verify(clubMemberRepository, times(1)).findByEmail("test@test.com");
        verify(clubMemberRepository, times(1)).save(any(ClubMember.class));
    }
}
