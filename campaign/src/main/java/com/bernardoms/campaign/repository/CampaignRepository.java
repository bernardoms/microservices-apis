package com.bernardoms.campaign.repository;

import com.bernardoms.campaign.model.Campaign;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface CampaignRepository extends MongoRepository<Campaign, ObjectId> {
    @Query("{ 'from' : { $gte: ?0, $lte: ?1}, $and: [{'to' : { $gte: ?0, $lte: ?1 }}, {'to' :{ $gte : ?2 }}]}  ")
    List<Campaign> findAllByDateBetween(LocalDate from, LocalDate to, LocalDate now);
}
