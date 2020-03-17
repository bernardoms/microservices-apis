package com.bernardoms.teamsapi.unit.service;

import com.bernardoms.teamsapi.config.Config;
import com.bernardoms.teamsapi.exception.TeamAlreadyExistsException;
import com.bernardoms.teamsapi.exception.TeamNotFoundException;
import com.bernardoms.teamsapi.mapper.Mapper;
import com.bernardoms.teamsapi.model.Team;
import com.bernardoms.teamsapi.model.dto.CampaignDTO;
import com.bernardoms.teamsapi.model.dto.CampaignPageableDTO;
import com.bernardoms.teamsapi.model.dto.TeamDTO;
import com.bernardoms.teamsapi.model.filter.TeamFilter;
import com.bernardoms.teamsapi.repository.TeamRepository;
import com.bernardoms.teamsapi.service.TeamQueryFilterService;
import com.bernardoms.teamsapi.service.TeamService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {
    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamQueryFilterService teamQueryFilterService;

    @Mock
    private Mapper<Team, TeamDTO> mapper;

    @InjectMocks
    private TeamService teamService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Config config;

    @Captor
    private ArgumentCaptor<Team> teamArgumentCaptor;

    @Test
    public void get_all_teams_test() {
        var filter = new TeamFilter(null,"Serie A", null, 3);


        var team1 = Team.builder().name("Vasco").championship("Serie A").build();

        var team2 = Team.builder().name("Flamengo").championship("Serie A").build();

        when(teamQueryFilterService.getFilterQuery(filter)).thenReturn(new PageImpl<>(Arrays.asList(team1, team2)));

        Page<TeamDTO> allTeams = teamService.getAll(filter);

        assertEquals(allTeams.getTotalElements(), 2);

        assertEquals(allTeams.getTotalPages(), 1);

        assertEquals(allTeams.get().filter(c->c.getName().equals("Vasco")).findFirst().get().getChampionship(), "Serie A");

        assertEquals(allTeams.get().filter(c->c.getName().equals("Flamengo")).findFirst().get().getChampionship(), "Serie A");
    }

    @Test
    public void test_get_team_by_id () throws Exception {
        var team = Team.builder().name("Portuguesa").championship("Serie B").build();

        when(teamRepository.findById(any(ObjectId.class))).thenReturn(Optional.of(team));

        when(mapper.mapToResp(team)).thenReturn(TeamDTO.builder().name("Portuguesa").championship("Serie B").build());

        TeamDTO teamDT = teamService.getTeamById(new ObjectId());

        verify(teamRepository, times(1)).findById(any(ObjectId.class));

        assertEquals(teamDT.getName(), "Portuguesa");

        assertEquals(teamDT.getChampionship(), "Serie B");
    }

    @Test
    public void test_save_team () throws TeamAlreadyExistsException {
        var team = Team.builder().name("Portuguesa").championship("Serie B").id(new ObjectId()).build();
        var teamDTO = TeamDTO.builder().name("Portuguesa").championship("Serie B").build();

        when(mapper.mapToDocument((any()))).thenReturn(team);

        when(teamRepository.save(any(Team.class))).thenReturn(team);

        teamService.saveTeam(teamDTO);

        verify(teamRepository, times(1)).save(any(Team.class));
    }

    @Test
    public void update_team () {

        var team = Team.builder().name("Portuguesa").championship("Serie B").build();

        var teamDTO = TeamDTO.builder().name("Portuguesa").championship("Serie A").build();

        when(teamRepository.findById(any(ObjectId.class))).thenReturn(Optional.of(team));

        teamService.updateTeam(new ObjectId(), teamDTO);

        verify(teamRepository, times(1)).save(teamArgumentCaptor.capture());

        assertEquals(teamArgumentCaptor.getValue().getChampionship(), "Serie A");
    }

    @Test
    public void delete_team () throws Exception {
        teamService.deleteTeam(new ObjectId());

        verify(teamRepository, times(1)).deleteById(any(ObjectId.class));
    }

    @Test
    public  void associate_team_with_campaigns() throws Exception {
        var campaignDTO = CampaignDTO.builder().to(LocalDate.of(2099,10,10)).from(LocalDate.of(2017,10,10)).name("campaign teste").build();
        var campaignDTO2 = CampaignDTO.builder().to(LocalDate.of(2099,10,10)).from(LocalDate.of(2017,10,11)).name("campaign teste2").build();

        var campaignPageable1 = new CampaignPageableDTO(2,false, Collections.singletonList(campaignDTO));

        var campaignPageable2 = new CampaignPageableDTO(2,true, Collections.singletonList(campaignDTO2));

        var team = Team.builder().name("Portuguesa").championship("Serie B").id(new ObjectId("507f191e810c19729de860ea")).build();

        var teamDTO = TeamDTO.builder().name("Portuguesa").championship("Serie B").build();

        when(config.getCampaignEndpoint()).thenReturn("http://test.com/v1/");

        when(mapper.mapToResp(team)).thenReturn(teamDTO);

        when(config.getSizePage()).thenReturn(1);

        when(teamRepository.findByName("vasco")).thenReturn(Optional.of(team));

        when(restTemplate.getForEntity("http://test.com/v1/campaigns?team_id=507f191e810c19729de860ea&offset=0&limit=1", CampaignPageableDTO.class)).thenReturn(new ResponseEntity<>(campaignPageable1, HttpStatus.OK));

        when(restTemplate.getForEntity("http://test.com/v1/campaigns?team_id=507f191e810c19729de860ea&limit=1&offset=1", CampaignPageableDTO.class)).thenReturn(new ResponseEntity<>(campaignPageable2, HttpStatus.OK));

        var teamWithCampaignAssociate = teamService.getTeamWithCampaignAssociate("vasco");

        assertEquals(teamWithCampaignAssociate.getChampionship(), "Serie B");

        assertEquals(teamWithCampaignAssociate.getName(), "Portuguesa");

        assertEquals(teamWithCampaignAssociate.getCampaigns().size(), 2);
    }

    @Test
    public  void associate_team_with_campaigns_no_team_found() throws Exception {
        TeamNotFoundException exception = assertThrows(
                TeamNotFoundException.class,
                () -> teamService.getTeamWithCampaignAssociate("vasco"));
        assertEquals(exception.getMessage(), "Team with name :vasco not exists");
    }
}
