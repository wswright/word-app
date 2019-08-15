package com.wswright.words.demo.service;

import com.wswright.words.demo.model.ImportedWord;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface WordImport {
    Set<ImportedWord> getWords(String message);
    CompletableFuture<Set<ImportedWord>> putWords(String message);
    void processImportedWords();
}
