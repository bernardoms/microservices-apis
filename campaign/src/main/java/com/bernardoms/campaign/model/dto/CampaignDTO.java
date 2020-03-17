package com.bernardoms.campaign.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CampaignDTO {
    @NotBlank(message = "Name may not be blank")
    private String name;
    private LocalDate from;
    private LocalDate to;
    @NotBlank(message = "team_id")
    private String team_id;
}
