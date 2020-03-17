package com.bernardoms.teamsapi.repository;

import com.bernardoms.teamsapi.model.Team;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TeamRepository extends MongoRepository<Team, ObjectId> {
    Optional<Team> findByName(String name);
}
