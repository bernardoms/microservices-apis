package com.bernardoms.campaign.service;

import com.bernardoms.campaign.model.Campaign;
import com.bernardoms.campaign.model.filter.CampaignFilter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CampaignQueryService {

    @NonNull private final MongoOperations mongoOperations;

    public Page<Campaign> getFilterQuery(CampaignFilter campaignFilter) {
        Query query = new Query();

        Optional.ofNullable(campaignFilter.getName()).ifPresent(c->query.addCriteria(Criteria.where("name").is(campaignFilter.getName())));

        Optional.ofNullable(campaignFilter.getTeam_id()).ifPresent(c->query.addCriteria(Criteria.where("team_id").is(campaignFilter.getTeam_id())));

        Optional.ofNullable(campaignFilter.getFrom()).ifPresent(c->query.addCriteria(Criteria.where("from").gte(campaignFilter.getFrom())));

        Optional.ofNullable(campaignFilter.getTo()).ifPresent(c->query.addCriteria(Criteria.where("to").gt(campaignFilter.getTo())));

        var pageable = PageRequest.of(campaignFilter.getOffset(), campaignFilter.getLimit(), Sort.unsorted());

        long count = mongoOperations.count(query, Campaign.class);

        List<Campaign> allCampaigns = mongoOperations.find(query.with(pageable), Campaign.class);

        return new PageImpl<>(allCampaigns, pageable, count);
    }
}
