package com.bernardoms.teamsapi.integration;

import com.bernardoms.teamsapi.model.Team;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

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
        mongoTemplate.save(Team.builder().id(new ObjectId("507f191e810c19729de860ea")).name("vasco").championship("Serie A").build());
        mongoTemplate.save(Team.builder().id(new ObjectId("507f191e810c19729de860eb")).name("flamengo").championship("Serie A").build());
        mongoTemplate.save(Team.builder().id(new ObjectId("507f191e810c19729de860ec")).name("fluminense").championship("Serie A").build());
        mongoTemplate.save(Team.builder().id(new ObjectId("507f191e810c19729de860ed")).name("botafogo").championship("Serie A").build());
        alreadySaved = true;
    }
}
