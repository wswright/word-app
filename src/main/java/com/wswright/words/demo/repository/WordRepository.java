package com.wswright.words.demo.repository;

import com.wswright.words.demo.model.TrackedWord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WordRepository extends MongoRepository<TrackedWord, String> {
    List<TrackedWord> findAllByOrderByTimesSeenDesc();
}
