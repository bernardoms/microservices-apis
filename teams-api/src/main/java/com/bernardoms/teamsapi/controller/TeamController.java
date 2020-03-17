package com.bernardoms.teamsapi.controller;

import com.bernardoms.teamsapi.exception.TeamAlreadyExistsException;
import com.bernardoms.teamsapi.model.dto.TeamDTO;
import com.bernardoms.teamsapi.model.filter.TeamFilter;
import com.bernardoms.teamsapi.service.TeamService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/teams")
public class TeamController {
    @NonNull private final TeamService teamService;

    @PostMapping
    public ResponseEntity<String> createTeam(@RequestBody TeamDTO team, UriComponentsBuilder uriComponentsBuilder) throws TeamAlreadyExistsException {
        ObjectId teamId = teamService.saveTeam(team);

        var uri =  uriComponentsBuilder.path("/v1/teams/{id}").buildAndExpand(teamId);

        return ResponseEntity.created(uri.toUri()).build();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<TeamDTO> getAllTeams(TeamFilter teamFilter) {
        return teamService.getAll(teamFilter);
    }

    @GetMapping(value = "{teamId}")
    @ResponseStatus(HttpStatus.OK)
    public TeamDTO getTeamById(@PathVariable ObjectId teamId) throws Exception {
        return teamService.getTeamById(teamId);
    }

    @DeleteMapping(value = "{teamId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeam(@PathVariable ObjectId teamId){
        teamService.deleteTeam(teamId);
    }

    @PutMapping(value = "{teamId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTeam(@PathVariable ObjectId teamId, TeamDTO teamDTO) {
        teamService.updateTeam(teamId, teamDTO);
    }

    @GetMapping(value = "{teamName}/associate/campaigns")
    @ResponseStatus(HttpStatus.OK)
    public TeamDTO getTeamWithAllAssociatesCampaigns(@PathVariable String teamName) throws Exception {
        return teamService.getTeamWithCampaignAssociate(teamName);
    }
}
