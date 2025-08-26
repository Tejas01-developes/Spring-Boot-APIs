package com.practice.keepgowing.repository;

import com.practice.keepgowing.entity.userschema;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface userrepository  extends MongoRepository<userschema, ObjectId> {

    Optional<userschema> findbyusername(String username);
    Optional<userschema> findbyemail(String email);
    Optional<userschema> deletebyusername(String email);
    Optional<userschema> findbytoken(String token);
}
