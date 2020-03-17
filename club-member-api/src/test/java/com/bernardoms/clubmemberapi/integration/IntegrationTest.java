package com.bernardoms.clubmemberapi.integration;

import com.bernardoms.clubmemberapi.model.ClubMember;
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
        mongoTemplate.save(ClubMember.builder().id(new ObjectId("507f191e810c19729de860ea")).teamName("vasco").email("test@test.com").fullName("testing test").birthDate(LocalDate.of(1990, 10, 10)).build());
        mongoTemplate.save(ClubMember.builder().id(new ObjectId("507f191e810c19729de860eb")).teamName("flamengo").email("test3@test.com").fullName("testing test3").birthDate(LocalDate.of(1990, 10, 10)).build());
        alreadySaved = true;
    }
}
