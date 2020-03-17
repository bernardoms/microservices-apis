package com.bernardoms.campaign.unit.service;

import com.bernardoms.campaign.model.Campaign;
import com.bernardoms.campaign.model.filter.CampaignFilter;
import com.bernardoms.campaign.service.CampaignQueryService;
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

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CampaignQueryServiceTest {
    @Mock
    private MongoOperations mongoOperations;

    @InjectMocks
    private CampaignQueryService campaignQueryService;

    @Captor
    private ArgumentCaptor<Query> queryArgumentCaptor;

    @Test
    public void test_filter_by_team_id() {
        var filter = new CampaignFilter(null, null, null, "3", 0, null);

        var campaign = Campaign.builder().team_id("1").to(LocalDate.of(2099, 10 , 29))
                .from(LocalDate.of(2017, 10, 10))
                .name("campaign test1")
                .id(new ObjectId()).build();

        when(mongoOperations.find(any(), any())).thenReturn(Collections.singletonList(campaign));

        campaignQueryService.getFilterQuery(filter);

        verify(mongoOperations, times(1)).find(queryArgumentCaptor.capture(), any());

        assertEquals(queryArgumentCaptor.getValue().toString(), "Query: { \"team_id\" : \"3\"}, Fields: {}, Sort: {}");
    }

    @Test
    public void test_filter_by_everything() {
        var filter = new CampaignFilter("Campaign Test", LocalDate.of(2017, 10, 1), LocalDate.of(2020, 10, 21), "3", 0, null);

        var campaign = Campaign.builder().team_id("1").to(LocalDate.of(2099, 10 , 29))
                .from(LocalDate.of(2017, 10, 10))
                .name("campaign test1")
                .id(new ObjectId()).build();

        when(mongoOperations.find(any(), any())).thenReturn(Collections.singletonList(campaign));

        campaignQueryService.getFilterQuery(filter);

        verify(mongoOperations, times(1)).find(queryArgumentCaptor.capture(), any());

        assertEquals(queryArgumentCaptor.getValue().toString(), "Query: { \"name\" : \"Campaign Test\", \"team_id\" : \"3\", \"from\" : { \"$gte\" : { \"$java\" : 2017-10-01 } }, \"to\" : { \"$gt\" : { \"$java\" : 2020-10-21 } } }, Fields: {}, Sort: {}");
    }
}
