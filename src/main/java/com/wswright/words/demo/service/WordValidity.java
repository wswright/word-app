package com.wswright.words.demo.service;

import com.wswright.words.demo.model.ImportedWord;

public interface WordValidity {
    boolean isValid(ImportedWord word);
    boolean isValid(String word);
}
