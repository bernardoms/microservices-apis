package com.bernardoms.campaign.mapper;

import com.bernardoms.campaign.model.Campaign;
import com.bernardoms.campaign.model.dto.CampaignDTO;
import org.springframework.stereotype.Component;

@Component
public class MapperCampaign implements Mapper<Campaign, CampaignDTO> {

    @Override
    public CampaignDTO mapToResp(Campaign mappedDocument) {
        return CampaignDTO.builder()
                .from(mappedDocument.getFrom())
                .to(mappedDocument.getTo())
                .team_id(mappedDocument.getTeam_id())
                .name(mappedDocument.getName())
                .build();
    }

    @Override
    public Campaign mapToDocument(CampaignDTO mappedResponse) {
        return Campaign.builder()
                .from(mappedResponse.getFrom())
                .to(mappedResponse.getTo())
                .name(mappedResponse.getName())
                .team_id(mappedResponse.getTeam_id())
                .build();
    }
}
