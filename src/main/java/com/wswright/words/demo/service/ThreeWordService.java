package com.wswright.words.demo.service;

import com.wswright.words.demo.model.TrackedThreeWord;
import com.wswright.words.demo.model.TrackedTwoWord;
import com.wswright.words.demo.model.TrackedWord;
import com.wswright.words.demo.repository.ThreeWordRepository;
import com.wswright.words.demo.repository.TwoWordRepository;
import com.wswright.words.demo.repository.WordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ThreeWordService {
    private WordRepository wordRepo;
    private ThreeWordRepository threeWordRepo;

    public ThreeWordService(WordRepository wordRepo, ThreeWordRepository threeWordRepo) {
        this.wordRepo = wordRepo;
        this.threeWordRepo = threeWordRepo;
    }

    public void saveThreeWords(TrackedThreeWord word) {
        try {
            threeWordRepo.save(word);
        } catch (Exception e) {
            log.error(String.format("Failed to save ThreeWords: %s", word), e);
        }
    }

    public List<TrackedThreeWord> getByWords(TrackedWord word1, TrackedWord word2, TrackedWord word3) {
        if(word1 == null || word1.word == null || word2 == null || word2.word == null || word3 == null || word3.word == null) return new ArrayList<>();
        List<TrackedThreeWord> words = null;
        try {
            words = threeWordRepo.findTrackedThreeWordsByWord1EqualsAndWord2EqualsOrderByCountDesc(word1, word2);
        } catch (Exception e) {
            log.error(String.format("Failed to retrieve TrackedThreeWord for words: [%s], [%s], [%s]. Message: %s", word1.word, word2.word, word3.word, e.getMessage()), e);
        }
        return words;
    }

    public void deleteTwoWords(TrackedThreeWord word) {
//        log.info(String.format("Deleting ThreeWord: %s", word));
        try {
            threeWordRepo.delete(word);
        } catch (Exception e) {
            log.error("Failed to delete ThreeWord! This likely happened during a crunch.", e);
        }
    }

    public Optional<String> getNext(TrackedWord trackedWord) {
        Optional<TrackedThreeWord> firstByWord1EqualsOrderByCountDesc = threeWordRepo.findFirstByWord1EqualsOrderByCountDesc(trackedWord);
        if(firstByWord1EqualsOrderByCountDesc.isPresent()) {
            TrackedThreeWord threeWord = firstByWord1EqualsOrderByCountDesc.get();
            return Optional.of(threeWord.word2.word);
        } else
            return Optional.empty();
    }

    public List<String> getNextWords(TrackedWord trackedWord, int count) {
        List<TrackedThreeWord> collect = threeWordRepo.findTrackedThreeWordsByWord1EqualsOrderByCountDescWord2Desc(trackedWord).stream().limit(count).collect(Collectors.toList());
        return collect.stream().map(ttw -> ttw.word2.word).collect(Collectors.toList());
    }

    public List<TrackedThreeWord> getWords(String word) {
        Optional<TrackedWord> byId = wordRepo.findById(word);
        if(byId.isEmpty())
            return new ArrayList<>();
        else {
            List<TrackedThreeWord> wordsOrderedByCountDescWord2Desc = threeWordRepo.findTrackedThreeWordsByWord1EqualsOrderByCountDescWord2Desc(byId.get()).stream().limit(100).collect(Collectors.toList());
            return wordsOrderedByCountDescWord2Desc;
        }
    }

}
