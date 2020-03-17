package com.bernardoms.teamsapi.model.filter;

import com.bernardoms.teamsapi.model.dto.TeamDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
public class TeamFilter extends TeamDTO {
   private Integer offset;
   private Integer limit;

   public TeamFilter(String name, String championship, Integer offset, Integer limit) {
      super(name, championship);
      this.limit = Objects.isNull(limit) ? 100 : limit;
      this.offset = Objects.isNull(offset) ? 0 : offset;
   }
}
