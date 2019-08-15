package com.wswright.words.demo.repository;

import com.wswright.words.demo.model.TrackedThreeWord;
import com.wswright.words.demo.model.TrackedWord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThreeWordRepository extends MongoRepository<TrackedThreeWord, String> {
    Optional<TrackedThreeWord> findFirstByWord1EqualsOrderByCountDesc(TrackedWord word1);
    List<TrackedThreeWord> findTrackedThreeWordsByWord1EqualsOrderByCountDescWord2Desc(TrackedWord word1);
    List<TrackedThreeWord> findTrackedThreeWordsByWord1EqualsAndWord2EqualsOrderByCountDesc(TrackedWord word1, TrackedWord word2);
}
