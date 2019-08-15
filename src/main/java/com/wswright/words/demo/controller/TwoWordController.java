package com.wswright.words.demo.controller;

import com.wswright.words.demo.model.TrackedTwoWord;
import com.wswright.words.demo.service.TwoWordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TwoWordController {
    private TwoWordService twoWordService;

    public TwoWordController(TwoWordService twoWordService) {
        this.twoWordService = twoWordService;
    }

    @GetMapping("/twowords/{word}/")
    public ResponseEntity<?> getWords(@PathVariable String word) {
        List<TrackedTwoWord> words = twoWordService.getWords(word);
        return ResponseEntity.ok(words);
    }

//    @GetMapping("/twowords/{word1}/{word2}")
}
