package com.bernardoms.teamsapi.unit.service;

import com.bernardoms.teamsapi.model.Team;
import com.bernardoms.teamsapi.model.filter.TeamFilter;
import com.bernardoms.teamsapi.service.TeamQueryFilterService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamQueryFilterServiceTest {
    @Mock
    private MongoOperations mongoOperations;

    @InjectMocks
    private TeamQueryFilterService teamQueryFilterService;

    @Captor
    private ArgumentCaptor<Query> queryArgumentCaptor;

    @Test
    public void test_filter_by_team_name() {
        var filter = new TeamFilter("Portuguesa", null, null, 3);

        var team = Team.builder().name("Portuguesa").championship("Serie B").id(new ObjectId()).build();

        when(mongoOperations.find(any(), any())).thenReturn(Collections.singletonList(team));

        teamQueryFilterService.getFilterQuery(filter);

        verify(mongoOperations, times(1)).find(queryArgumentCaptor.capture(), any());

        assertEquals(queryArgumentCaptor.getValue().toString(), "Query: { \"name\" : \"Portuguesa\"}, Fields: {}, Sort: {}");
    }

    @Test
    public void test_filter_by_everything() {
        var filter = new TeamFilter("Portuguesa", "Serie B", null, 3);

        var team = Team.builder().name("Portuguesa").championship("Serie B").id(new ObjectId()).build();

        when(mongoOperations.find(any(), any())).thenReturn(Collections.singletonList(team));

        teamQueryFilterService.getFilterQuery(filter);

        verify(mongoOperations, times(1)).find(queryArgumentCaptor.capture(), any());

        assertEquals(queryArgumentCaptor.getValue().toString(), "Query: { \"name\" : \"Portuguesa\", \"championship\" : \"Serie B\"}, Fields: {}, Sort: {}");
    }
}
