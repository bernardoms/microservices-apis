package com.bernardoms.campaign.service;

import com.bernardoms.campaign.exception.CampaignNotFoundException;
import com.bernardoms.campaign.mapper.Mapper;
import com.bernardoms.campaign.model.Campaign;
import com.bernardoms.campaign.model.dto.CampaignDTO;
import com.bernardoms.campaign.model.filter.CampaignFilter;
import com.bernardoms.campaign.repository.CampaignRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CampaignService {
    @NonNull
    private final CampaignRepository campaignRepository;
    @NonNull
    private final CampaignQueryService campaignQueryService;
    @NonNull
    private final Mapper<Campaign, CampaignDTO> mapper;

    public Page<CampaignDTO> getAllCampaigns(CampaignFilter campaignDTO) {

        campaignDTO.setTo(LocalDate.now());

        var campaigns = campaignQueryService.getFilterQuery(campaignDTO);

        return campaigns.map(c -> new CampaignDTO(c.getName(), c.getFrom(), c.getTo(), c.getTeam_id()));
    }

    public CampaignDTO getCampaignById(ObjectId campaignId) throws Exception {
        var campaign = campaignRepository.findById(campaignId).orElseThrow(() -> new CampaignNotFoundException("Campaign with id : " + campaignId + "not found"));

        return  mapper.mapToResp(campaign);
    }

    public Campaign saveCampaign(CampaignDTO campaignDTO) {
        var newCampaign = mapper.mapToDocument(campaignDTO);

        var foundCampaigns = campaignRepository.findAllByDateBetween(campaignDTO.getFrom(), campaignDTO.getTo(), LocalDate.now());

        foundCampaigns.sort(Comparator.comparing(Campaign::getTo));

        if (!foundCampaigns.isEmpty()) {
            handleEqualsCampaignDate(foundCampaigns, newCampaign);
            campaignRepository.saveAll(foundCampaigns);
        }

        return campaignRepository.save(newCampaign);
    }

    public void updateCampaign(CampaignDTO campaignDTO, ObjectId id) {

        var optionalCampaign = campaignRepository.findById(id);

        if (optionalCampaign.isPresent()) {
            optionalCampaign.get().setTo(campaignDTO.getTo());
            optionalCampaign.get().setFrom(campaignDTO.getFrom());
            optionalCampaign.get().setName(campaignDTO.getName());
            optionalCampaign.get().setTeam_id(campaignDTO.getTeam_id());
            campaignRepository.save(optionalCampaign.get());
        }
    }

    public void deleteCampaign(ObjectId campaignId) {
        campaignRepository.deleteById(campaignId);
    }

    private void handleEqualsCampaignDate(List<Campaign> campaigns, Campaign campaign) {
        campaigns.forEach(c -> {
            c.setTo(c.getTo().plusDays(1));
            while (checkEqualsCampaignDate(campaigns, campaign, c)) {
                c.setTo(c.getTo().plusDays(1));
            }
        });
    }

    private boolean checkEqualsCampaignDate(List<Campaign> campaigns, Campaign newCampaign, Campaign sumCampaign) {
        return campaigns.stream().filter(c -> !c.getId().equals(sumCampaign.getId())).anyMatch(c -> c.getTo().equals(sumCampaign.getTo())) || sumCampaign.getTo().equals(newCampaign.getTo());
    }
}
