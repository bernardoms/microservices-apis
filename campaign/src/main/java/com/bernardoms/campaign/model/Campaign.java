package com.bernardoms.campaign.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Builder
@Document(collection = "campaigns")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Campaign {
    @Id
    private ObjectId id;
    private String team_id;
    private String name;
    private LocalDate from;
    private LocalDate to;
}
