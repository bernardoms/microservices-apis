package com.bernardoms.clubmemberapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "clubmembers")
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClubMember {
    @Id
    private ObjectId id;
    private LocalDate birthDate;
    private String fullName;
    private String email;
    private String teamName;
}
