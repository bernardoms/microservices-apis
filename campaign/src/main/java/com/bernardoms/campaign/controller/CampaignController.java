package com.bernardoms.campaign.controller;

import com.bernardoms.campaign.model.Campaign;
import com.bernardoms.campaign.model.dto.CampaignDTO;
import com.bernardoms.campaign.model.filter.CampaignFilter;
import com.bernardoms.campaign.service.CampaignService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RequestMapping("/v1/campaigns")
@RestController
@RequiredArgsConstructor
public class CampaignController {

    @NonNull
    private final CampaignService campaignService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<CampaignDTO> getAllCampaigns(CampaignFilter campaignFilter) {
        return campaignService.getAllCampaigns(campaignFilter);
    }

    @GetMapping(value = "/{campaignId}")
    @ResponseStatus(HttpStatus.OK)
    public CampaignDTO getCampaign(@PathVariable ObjectId campaignId) throws Exception {
        return campaignService.getCampaignById(campaignId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createCampaign(@RequestBody @Validated CampaignDTO campaignDTO, UriComponentsBuilder uriComponentsBuilder) {
        var campaign = campaignService.saveCampaign(campaignDTO);

        var uriComponent = uriComponentsBuilder.path("/v1/campaigns/{id}").buildAndExpand(campaign.getId());

        return ResponseEntity.created(uriComponent.toUri()).build();
    }

    @PutMapping(value = "/{campaignId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCampaign(@RequestBody CampaignDTO campaignDTO, @PathVariable ObjectId campaignId) {
        campaignService.updateCampaign(campaignDTO, campaignId);
    }

    @DeleteMapping(value = "/{campaignId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCampaign(@PathVariable ObjectId campaignId) {
        campaignService.deleteCampaign(campaignId);
    }
}

