package com.bernardoms.teamsapi.mapper;

import com.bernardoms.teamsapi.model.Team;
import com.bernardoms.teamsapi.model.dto.TeamDTO;
import org.springframework.stereotype.Component;

@Component
public class MapperTeam implements Mapper<Team, TeamDTO> {

    @Override
    public TeamDTO mapToResp(Team mappedDocument) {
        return TeamDTO.builder()
                .name(mappedDocument.getName().toLowerCase())
                .championship(mappedDocument.getChampionship())
                .id(mappedDocument.getId().toString())
                .build();
    }

    @Override
    public Team mapToDocument(TeamDTO mappedResponse) {
        return Team.builder()
                .name(mappedResponse.getName().toLowerCase())
                .championship(mappedResponse.getChampionship())
                .build();
    }
}
