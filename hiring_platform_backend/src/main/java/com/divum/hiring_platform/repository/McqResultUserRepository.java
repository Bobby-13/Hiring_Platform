package com.divum.hiring_platform.repository;

import com.divum.hiring_platform.entity.MCQResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface McqResultUserRepository extends MongoRepository<MCQResult, String> {

    Optional<MCQResult> findById(String resultId);

}