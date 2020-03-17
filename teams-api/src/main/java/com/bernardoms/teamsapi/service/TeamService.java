package com.bernardoms.teamsapi.service;

import com.bernardoms.teamsapi.config.Config;
import com.bernardoms.teamsapi.exception.TeamAlreadyExistsException;
import com.bernardoms.teamsapi.exception.TeamNotFoundException;
import com.bernardoms.teamsapi.mapper.Mapper;
import com.bernardoms.teamsapi.model.Team;
import com.bernardoms.teamsapi.model.dto.CampaignPageableDTO;
import com.bernardoms.teamsapi.model.dto.TeamDTO;
import com.bernardoms.teamsapi.model.filter.TeamFilter;
import com.bernardoms.teamsapi.repository.TeamRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamService {
    @NonNull
    private final TeamRepository teamRepository;

    @NonNull
    private final TeamQueryFilterService teamQueryFilterService;

    @NonNull
    private final RestTemplate restTemplate;

    @NonNull
    private Config config;

    @NonNull
    private Mapper<Team, TeamDTO> mapper;

    public ObjectId saveTeam(TeamDTO teamDTO) throws TeamAlreadyExistsException {
        var team = mapper.mapToDocument(teamDTO);

        Optional<Team> optionalTeam = teamRepository.findByName(teamDTO.getName());

        if(optionalTeam.isPresent()) {
            throw  new TeamAlreadyExistsException("Team with name : " + teamDTO.getName() + "already exists");
        }

        return teamRepository.save(team).getId();
    }

    public void deleteTeam(ObjectId id) {
        teamRepository.deleteById(id);
    }

    public void updateTeam(ObjectId id, TeamDTO teamDTO) {
        var optionalTeam = teamRepository.findById(id);

        if (optionalTeam.isPresent()) {
            optionalTeam.get().setChampionship(teamDTO.getChampionship());
            optionalTeam.get().setName(teamDTO.getName());
            teamRepository.save(optionalTeam.get());
        }
    }

    public Page<TeamDTO> getAll(TeamFilter teamFilter) {
        var filterQuery = teamQueryFilterService.getFilterQuery(teamFilter);

        return filterQuery.map(f -> new TeamDTO(f.getName(), f.getChampionship()));

    }

    public TeamDTO getTeamById(ObjectId id) throws Exception {
        var team = teamRepository.findById(id).orElseThrow(() -> new TeamNotFoundException("Team with id : " + id + " not found!"));

        return mapper.mapToResp(team);
    }

    @HystrixCommand(fallbackMethod = "getTeamWithCampaignAssociateFallback")
    public TeamDTO getTeamWithCampaignAssociate(String teamName) throws Exception {

        var optionalTeam = teamRepository.findByName(teamName);

        var team = optionalTeam.orElseThrow(() -> new TeamNotFoundException("Team with name :" + teamName + " not exists"));

        boolean lastPage = false;

        int offset = 0;

        var builder = UriComponentsBuilder.fromHttpUrl(config.getCampaignEndpoint() + "/campaigns");

        builder.queryParam("team_id", team.getId());
        builder.queryParam("offset", 0);
        builder.queryParam("limit", config.getSizePage());

        var teamDTO = mapper.mapToResp(team);

        teamDTO.setCampaigns(new ArrayList<>());

        while (!lastPage) {
            var campaignResponse = restTemplate.getForEntity(builder.toUriString(), CampaignPageableDTO.class);

            offset += config.getSizePage();

            builder.replaceQueryParam("offset", offset);

            if (Objects.isNull(campaignResponse.getBody())) {
                break;
            }

            teamDTO.getCampaigns().addAll(campaignResponse.getBody().getContent());

            lastPage = campaignResponse.getBody().isLast();
        }
        return teamDTO;
    }

    public TeamDTO getTeamWithCampaignAssociateFallback(String teamName) throws Exception {
        var team = teamRepository.findByName(teamName).orElseThrow(() -> new TeamNotFoundException("Team with Id :" + teamName + " not exists"));
        return mapper.mapToResp(team);
    }
}
