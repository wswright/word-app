package com.wswright.words.demo.service;

import com.wswright.words.demo.model.TrackedTwoWord;
import com.wswright.words.demo.model.TrackedWord;
import com.wswright.words.demo.repository.TwoWordRepository;
import com.wswright.words.demo.repository.WordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TwoWordService {
    private WordRepository wordRepo;
    private TwoWordRepository twoWordRepo;
    private MongoTemplate template;

    public TwoWordService(WordRepository wordRepo, TwoWordRepository twoWordRepo, MongoTemplate template) {
        this.wordRepo = wordRepo;
        this.twoWordRepo = twoWordRepo;
        this.template = template;
    }

    public void saveTwoWords(TrackedTwoWord word) {
        try {
            twoWordRepo.save(word);
        } catch (Exception e) {
            log.error(String.format("Failed to save TwoWords: %s", word), e);
        }
    }

    public List<TrackedTwoWord> getByWords(TrackedWord word1, TrackedWord word2) {
        if(word1 == null || word1.word == null || word2 == null || word2.word == null) return new ArrayList<>();
        List<TrackedTwoWord> words = null;
        try {
            words = twoWordRepo.findTrackedTwoWordsByWord1EqualsAndWord2EqualsOrderByCountDesc(word1, word2);
        } catch (Exception e) {
            log.error(String.format("Failed to retrieve TrackedTwoWords for words: [%s] and [%s]. Message: %s", word1.word, word2.word, e.getMessage()), e);
        }
        return words;
    }

    public void deleteTwoWords(TrackedTwoWord word) {
//        log.info(String.format("Deleting TwoWord: %s", word));
        try {
            twoWordRepo.delete(word);
        } catch (Exception e) {
            log.error("Failed to delete TwoWord! This likely happened during a crunch.", e);
        }
    }

    public Optional<String> getNext(TrackedWord trackedWord) {
        Optional<TrackedTwoWord> firstByWord1EqualsOrderByCountDesc = twoWordRepo.findFirstByWord1EqualsOrderByCountDesc(trackedWord);
        if(firstByWord1EqualsOrderByCountDesc.isPresent()) {
            TrackedTwoWord twoWord = firstByWord1EqualsOrderByCountDesc.get();
            return Optional.of(twoWord.word2.word);
        } else
            return Optional.empty();
    }

    public List<String> getNextWords(TrackedWord trackedWord, int count) {
        return template.find(Query.query(Criteria.where("word1").is(trackedWord)).limit(count), TrackedTwoWord.class).stream().map(w -> w.word2.word).collect(Collectors.toList());

//        List<TrackedTwoWord> collect = twoWordRepo.findTrackedTwoWordsByWord1EqualsOrderByCountDescWord2Desc(trackedWord).stream().limit(count).collect(Collectors.toList());
//        return collect.stream().map(ttw -> ttw.word2.word).collect(Collectors.toList());
    }

    public List<TrackedTwoWord> getWords(String word) {
        Optional<TrackedWord> byId = wordRepo.findById(word);
        if(byId.isEmpty())
            return new ArrayList<>();
        else {
            List<TrackedTwoWord> wordsOrderedByCountDescWord2Desc = twoWordRepo.findTrackedTwoWordsByWord1EqualsOrderByCountDescWord2Desc(byId.get()).stream().limit(100).collect(Collectors.toList());
            return wordsOrderedByCountDescWord2Desc;
        }
    }

}
