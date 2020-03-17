package com.bernardoms.campaign.integration;

import com.bernardoms.campaign.model.Campaign;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDate;

@SpringBootTest
public abstract class IntegrationTest {


    private static boolean alreadySaved = false;

    @Autowired
    MongoTemplate mongoTemplate;


    @BeforeEach
    public void setUp() {

        if (alreadySaved) {
            return;
        }
        mongoTemplate.save(Campaign.builder().id(new ObjectId("507f191e810c19729de860ea")).from(LocalDate.of(2017, 10, 1)).to(LocalDate.of(2020, 10, 3)).team_id("1").name("Campaign 1").build());
        mongoTemplate.save(Campaign.builder().id(new ObjectId("507f191e810c19729de860eb")).from(LocalDate.of(2017, 10, 1)).to(LocalDate.of(2020, 10, 2)).team_id("2").name("Campaign 2").build());
        mongoTemplate.save(Campaign.builder().id(new ObjectId("507f191e810c19729de860ec")).from(LocalDate.of(2017, 10, 1)).to(LocalDate.of(2017, 10, 3)).team_id("2").name("Campaign 2").build());
        alreadySaved = true;
    }
}
