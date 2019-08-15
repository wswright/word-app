package com.wswright.words.demo.service;

import com.wswright.words.demo.model.ImportedWord;
import com.wswright.words.demo.repository.ImportedWordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ImportedWordService {
    private ImportedWordRepository importedWordRepo;

    public ImportedWordService(ImportedWordRepository importedWordRepo) {
        this.importedWordRepo = importedWordRepo;
    }

    public long getCount() {
        return importedWordRepo.count();
    }

    public void saveAll(Iterable<ImportedWord> i) {
        try {
            importedWordRepo.saveAll(i);
        } catch (Exception e) {
            log.error("Failed to saveAll imported words!", e);
        }
    }

    public void deleteAll(Iterable<ImportedWord> i) {
        try {
            importedWordRepo.deleteAll(i);
        } catch (Exception e) {
            log.error("Failed to deleted imported words. You may get duplicate processing.", e);
        }
    }

    public List<ImportedWord> getImportedWords(int limit) {
        try {
            return importedWordRepo.findAll().stream().limit(limit).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to get imported words.", e);
            return new ArrayList<>();
        }
    }
}
