package com.wswright.words.demo.controller;

import com.wswright.words.demo.model.ImportedWord;
import com.wswright.words.demo.service.WordImport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@RestController
public class ImportController {
    private WordImport wordImporter;
    public ImportController(WordImport wordImporter) {
        this.wordImporter = wordImporter;
    }

    @PostMapping("/words/")
    public ResponseEntity<?> postWords(@RequestBody String body) {
        CompletableFuture<Set<ImportedWord>> setCompletableFuture = wordImporter.putWords(body);
        return ResponseEntity.ok("Processing...");
    }
}
