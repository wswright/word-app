package com.wswright.words.demo.service;

import com.wswright.words.demo.model.TrackedWord;
import com.wswright.words.demo.repository.TwoWordRepository;
import com.wswright.words.demo.repository.WordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TwoWordCrunchService {
    private WordService wordService;
    private WordRepository wordRepo;
    private TwoWordService twoWordService;
    private TwoWordRepository twoWordRepository;


    public TwoWordCrunchService(WordService wordService, WordRepository wordRepo, TwoWordService twoWordService, TwoWordRepository twoWordRepository) {
        this.wordService = wordService;
        this.wordRepo = wordRepo;
        this.twoWordService = twoWordService;
        this.twoWordRepository = twoWordRepository;
    }


    public void crunch() {
        log.info(">>>]CRUNCHER[<<<: Running...");
        List<TrackedWord> all = wordRepo.findAll();
        for(TrackedWord word : all) {
            if(canBeCrunched(word))
                crunchWord(word);
        }
    }


    private boolean canBeCrunched(TrackedWord word) {
//        wordRepo.
        return false;
    }

    private void crunchWord(TrackedWord word) {
        log.info(String.format(">>>]CRUNCHER[<<<: Crunching '%s'...", word.word));
    }
}
