package com.bernardoms.teamsapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CampaignPageableDTO {
    private int totalPages;
    private boolean last;
    private List<CampaignDTO> content;
}
