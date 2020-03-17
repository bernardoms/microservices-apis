package com.bernardoms.campaign.unit.service;

import com.bernardoms.campaign.mapper.Mapper;
import com.bernardoms.campaign.model.Campaign;
import com.bernardoms.campaign.model.dto.CampaignDTO;
import com.bernardoms.campaign.model.filter.CampaignFilter;
import com.bernardoms.campaign.repository.CampaignRepository;
import com.bernardoms.campaign.service.CampaignQueryService;
import com.bernardoms.campaign.service.CampaignService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CampaignServiceTest {
    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private CampaignQueryService campaignQueryService;

    @Mock
    private Mapper<Campaign, CampaignDTO> mapper;

    @InjectMocks
    private CampaignService campaignService;

    @Captor
    ArgumentCaptor<List<Campaign>> campaignArgumentCaptor;

    @Test
    public void get_all_campaigns_test() {
        CampaignFilter filter = new CampaignFilter(null, null, null, "1", 0, null);


        var campaign = Campaign.builder().team_id("1").to(LocalDate.of(2099, 10 , 29))
                .from(LocalDate.of(2017, 10, 10))
                .name("campaign test1")
                .id(new ObjectId()).build();

        var campaign2 = Campaign.builder().team_id("1").to(LocalDate.of(2099, 10 , 29))
                .from(LocalDate.of(2017, 10, 11))
                .name("campaign test2")
                .id(new ObjectId()).build();

        when(campaignQueryService.getFilterQuery(filter)).thenReturn(new PageImpl<>(Arrays.asList(campaign, campaign2)));

        Page<CampaignDTO> allCampaigns = campaignService.getAllCampaigns(filter);

        assertEquals(allCampaigns.getTotalElements(), 2);

        assertEquals(allCampaigns.getTotalPages(), 1);

        assertEquals(allCampaigns.get().filter(c->c.getName().equals("campaign test1")).findFirst().get().getTeam_id(), "1");

        assertEquals(allCampaigns.get().filter(c->c.getName().equals("campaign test1")).findFirst().get().getFrom(), LocalDate.of(2017, 10, 10));

        assertEquals(allCampaigns.get().filter(c->c.getName().equals("campaign test1")).findFirst().get().getTo(), LocalDate.of(2099, 10 , 29));


        assertEquals(allCampaigns.get().filter(c->c.getName().equals("campaign test2")).findFirst().get().getFrom(), LocalDate.of(2017, 10, 11));

        assertEquals(allCampaigns.get().filter(c->c.getName().equals("campaign test2")).findFirst().get().getTo(), LocalDate.of(2099, 10 , 29));

        assertEquals(allCampaigns.get().filter(c->c.getName().equals("campaign test2")).findFirst().get().getTeam_id(), "1");
    }

    @Test
    public void test_get_campaign_by_id () throws Exception {
        var campaign = Campaign.builder().team_id("1").to(LocalDate.of(2099, 10 , 29))
                .from(LocalDate.of(2017, 10, 10))
                .name("campaign test1")
                .id(new ObjectId()).build();

        when(campaignRepository.findById(any(ObjectId.class))).thenReturn(Optional.of(campaign));

        when(mapper.mapToResp(campaign)).thenReturn(CampaignDTO.builder().name(campaign.getName()).team_id(campaign.getTeam_id()).to(campaign.getTo()).from(campaign.getFrom()).build());

        CampaignDTO campaignDTO = campaignService.getCampaignById(new ObjectId());

        verify(campaignRepository, times(1)).findById(any(ObjectId.class));

        assertEquals(campaignDTO.getTo(), LocalDate.of(2099, 10 , 29));

        assertEquals(campaignDTO.getFrom(), LocalDate.of(2017, 10, 10));

        assertEquals(campaignDTO.getTeam_id(), "1");

        assertEquals(campaignDTO.getName(), "campaign test1");
    }

    @Test
    public void test_save_campaign () throws Exception {

        var campaign = CampaignDTO.builder().team_id("1").to(LocalDate.of(2020, 10 , 3))
                .from(LocalDate.of(2017, 10, 1))
                .name("campaign test1")
                .build();


        var createdCampaign1 = Campaign.builder().id(new ObjectId())
                .from(LocalDate.of(2017, 10, 1))
                .to(LocalDate.of(2020, 10, 3))
                .team_id("1").name("Campaign 1")
                .build();

        var createdCampaign2 = Campaign.builder().id(new ObjectId())
                .from(LocalDate.of(2017, 10, 1))
                .to(LocalDate.of(2020, 10, 2))
                .team_id("2").name("Campaign 2")
                .build();


        when(campaignRepository.findAllByDateBetween(any(), any(), any())).thenReturn(Arrays.asList(createdCampaign1, createdCampaign2));

        when(mapper.mapToDocument(campaign)).thenReturn(Campaign.builder().from(campaign.getFrom()).to(campaign.getTo()).name(campaign.getName()).team_id(campaign.getTeam_id()).build());

        campaignService.saveCampaign(campaign);

        verify(campaignRepository, times(1)).saveAll(campaignArgumentCaptor.capture());

        verify(campaignRepository, times(1)).save(any(Campaign.class));

        assertEquals(campaignArgumentCaptor.getValue().stream().filter(c->c.getName().equals("Campaign 1")).findFirst().get().getTo(), LocalDate.of(2020, 10, 5));

        assertEquals(campaignArgumentCaptor.getValue().stream().filter(c->c.getName().equals("Campaign 2")).findFirst().get().getTo(), LocalDate.of(2020, 10, 4));
    }

    @Test
    public void update_campaign () throws Exception {

        var campaign = CampaignDTO.builder().team_id("1").to(LocalDate.of(2020, 10 , 3))
                .from(LocalDate.of(2017, 10, 1))
                .name("campaign test1")
                .build();


        var createdCampaign1 = Campaign.builder().id(new ObjectId())
                .from(LocalDate.of(2017, 10, 1))
                .to(LocalDate.of(2020, 10, 3))
                .team_id("1").name("Campaign 1")
                .build();

        when(campaignRepository.findById(any(ObjectId.class))).thenReturn(Optional.of(createdCampaign1));

        campaignService.updateCampaign(campaign, new ObjectId());


        verify(campaignRepository, times(1)).save(any(Campaign.class));
    }

    @Test
    public void delete_campaign () throws Exception {

        campaignService.deleteCampaign(new ObjectId());

        verify(campaignRepository, times(1)).deleteById(any(ObjectId.class));
    }
}
