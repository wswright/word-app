package com.wswright.words.demo.service;

import com.wswright.words.demo.model.TrackedWord;
import com.wswright.words.demo.repository.WordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class WordService {
    private WordRepository wordRepo;
    private TwoWordService twoWordService;
    private Random random;
    private MongoTemplate template;

    public WordService(WordRepository wordRepo, TwoWordService twoWordService, MongoTemplate mongoTemplate) {
        this.wordRepo = wordRepo;
        this.twoWordService = twoWordService;
        random = new Random();
        this.template = mongoTemplate;
    }

    public Optional<TrackedWord> getByWord(String word) {
        Optional<TrackedWord> tmpWord = wordRepo.findById(word);
        if(tmpWord.isPresent())
            return Optional.of(tmpWord.get());
        return Optional.empty();
    }

    public void saveWord(TrackedWord trackedWord) {
        try {
            wordRepo.save(trackedWord);
//            log.info(String.format("Saved word: %s", trackedWord.word));
        } catch (Exception e) {
            log.error("Something bad happened!", e);
        }
    }

    public void saveWords(List<TrackedWord> trackedWords) {
        try {
            trackedWords.forEach(trackedWord -> {
                wordRepo.save(trackedWord);
//            log.info(String.format("Saved word: %s", trackedWord.word));
            });
        } catch (Exception e) {
            log.error("Something bad happened!", e);
        }
    }

    public TrackedWord putAndGet(String word) {
        Optional<TrackedWord> tmpWord = wordRepo.findById(word);
        if(tmpWord.isPresent())
            return tmpWord.get();
        else {
            try {
                TrackedWord trackedWord = wordRepo.save(new TrackedWord(word));
//                log.info(String.format("Saved new word: %s", word));
                return trackedWord;
            } catch (Exception e) {
                log.error(String.format("Failed to save new TrackedWord! Word: %s", word), e);
                return null;
            }
        }
    }


    public List<TrackedWord> getMostSeen(int limit) {
       return template.find(Query.query(Criteria.where("timesSeen").gt(0)).limit(limit).with(new Sort(Sort.Direction.DESC, "timesSeen")), TrackedWord.class);
//        List<TrackedWord> allByWordOrderByTimesSeenDesc = wordRepo.findAllByOrderByTimesSeenDesc();
//        return allByWordOrderByTimesSeenDesc;
    }

    public Optional<String> getNextWord(String word) {
        List<String> nextWords = getNextWords(word, 10);
        if(nextWords == null || nextWords.size() == 0)
            return Optional.empty();
        return Optional.of(nextWords.get(random.nextInt(nextWords.size())));
    }

    public List<String> getNextWords(String word, int n) {
        if(word == null || word.isBlank() || word.isEmpty())
            return new ArrayList<>();
        Optional<TrackedWord> byWord = getByWord(word);
        if(byWord.isPresent()) {
            List<String> nextWords = twoWordService.getNextWords(byWord.get(), n);
            return nextWords;
        }
        else
            return new ArrayList<>();
    }
}
