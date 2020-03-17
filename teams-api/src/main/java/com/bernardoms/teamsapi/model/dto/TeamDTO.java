package com.bernardoms.teamsapi.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeamDTO {
    @NotBlank(message = "name can't be empty")
    private String name;
    @NotBlank(message = "championship can't be empty")
    private String championship;
    private String id;
    private List<CampaignDTO> campaigns;

    public TeamDTO(String name, String championship) {
        this.championship = championship;
        this.name = name;
    }
}
