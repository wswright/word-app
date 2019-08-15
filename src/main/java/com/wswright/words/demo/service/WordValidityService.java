package com.wswright.words.demo.service;

import com.wswright.words.demo.model.ImportedWord;
import org.springframework.stereotype.Service;

@Service
public class WordValidityService implements WordValidity {
    public String[] baddies = {"!","@","#","$","%","^", "&", "*", "(", ")", "_", "-", "+", "=", "{", "}", "[", "]", "\"", "\\", "/", "?", ".", ",", "\r", "\n", "\t", "“"};

    @Override
    public boolean isValid(ImportedWord word) {
        String w = word.word;
        if(w == null) return false;
        if(w.isEmpty()) return false;
        if(w.isBlank()) return false;
        if(w.length() > 20) return false;
        for(String bad : baddies) {
            if(w.contains(bad)) return false;
        }

        return true;
    }

    @Override
    public boolean isValid(String word) {
        return isValid(new ImportedWord(word));
    }
}
