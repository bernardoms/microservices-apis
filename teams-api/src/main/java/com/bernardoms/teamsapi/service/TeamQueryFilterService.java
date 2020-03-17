package com.bernardoms.teamsapi.service;

import com.bernardoms.teamsapi.model.Team;
import com.bernardoms.teamsapi.model.dto.TeamDTO;
import com.bernardoms.teamsapi.model.filter.TeamFilter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamQueryFilterService {
    @NonNull
    private final MongoOperations mongoOperations;

    public Page<Team> getFilterQuery(TeamFilter teamFilter) {
        Query query = new Query();

        Optional.ofNullable(teamFilter.getName()).ifPresent(c->query.addCriteria(Criteria.where("name").is(teamFilter.getName())));

        Optional.ofNullable(teamFilter.getChampionship()).ifPresent(c->query.addCriteria(Criteria.where("championship").is(teamFilter.getChampionship())));

        var pageable = PageRequest.of(teamFilter.getOffset(), teamFilter.getLimit(), Sort.unsorted());

        long count = mongoOperations.count(query, Team.class);

        List<Team> allTeams = mongoOperations.find(query.with(pageable), Team.class);

        return new PageImpl<>(allTeams, pageable, count);
    }
}
