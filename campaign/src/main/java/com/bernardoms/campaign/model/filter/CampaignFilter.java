package com.bernardoms.campaign.model.filter;

import com.bernardoms.campaign.model.dto.CampaignDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
public class CampaignFilter extends CampaignDTO {
    private Integer offset;
    private Integer limit;

    public CampaignFilter(String name, LocalDate from, LocalDate to, String team_id, Integer offset, Integer limit) {
        super(name, from, to, team_id);
        this.limit = Objects.isNull(this.limit)  ? 100 : limit;
        this.offset = Objects.isNull(this.offset) ? 0 : offset;
    }
}
