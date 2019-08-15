package com.wswright.words.demo.repository;

import com.wswright.words.demo.model.TrackedTwoWord;
import com.wswright.words.demo.model.TrackedWord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TwoWordRepository extends MongoRepository<TrackedTwoWord, String> {
    Optional<TrackedTwoWord> findFirstByWord1EqualsOrderByCountDesc(TrackedWord word1);
    List<TrackedTwoWord> findTrackedTwoWordsByWord1EqualsOrderByCountDescWord2Desc(TrackedWord word1);
    List<TrackedTwoWord> findTrackedTwoWordsByWord1EqualsAndWord2EqualsOrderByCountDesc(TrackedWord word1, TrackedWord word2);
}
