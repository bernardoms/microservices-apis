package com.bernardoms.clubmemberapi.repository;

import com.bernardoms.clubmemberapi.model.ClubMember;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ClubMemberRepository extends MongoRepository<ClubMember, ObjectId> {
    Optional<ClubMember> findByEmail(String email);
}
