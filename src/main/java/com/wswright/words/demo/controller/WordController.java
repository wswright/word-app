package com.wswright.words.demo.controller;

import com.wswright.words.demo.model.TrackedWord;
import com.wswright.words.demo.service.WordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@RestController
public class WordController {
    private WordService wordService;

    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    @GetMapping("/words/mostseen")
    public ResponseEntity<?> getMostSeen() {
        List<TrackedWord> mostSeen = wordService.getMostSeen(100);
        return ResponseEntity.ok(mostSeen);
    }

    @GetMapping("/words/after/{word}/")
    public ResponseEntity<?> getNextWord(@PathVariable String word) {
        if(word == null || word.length() == 0)
            return ResponseEntity.noContent().build();
        Optional<String> nextWord = wordService.getNextWord(word);
        if(nextWord.isPresent())
            return ResponseEntity.ok(nextWord.get());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/words/num/{word}/{count}/")
    public ResponseEntity<?> getWords(@PathVariable String word, @PathVariable int count) {
        String words = "";
        String currentWord = word;
        for(int i=0; i<count && i < 100; i++) {
            Optional<String> nextWord = wordService.getNextWord(currentWord);
            if(nextWord.isEmpty())
                return ResponseEntity.ok(words);
            String s = nextWord.get();
            words += " " + s;
            currentWord = s;
        }
        return ResponseEntity.ok(words);
    }
}
