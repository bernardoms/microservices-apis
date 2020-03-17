package com.bernardoms.teamsapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "teams")
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Team {
    @Id
    private ObjectId id;

    private String name;
    private String championship;
}
