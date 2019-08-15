package com.wswright.words.demo.service;

import com.wswright.words.demo.model.ImportedWord;
import com.wswright.words.demo.model.TrackedThreeWord;
import com.wswright.words.demo.model.TrackedTwoWord;
import com.wswright.words.demo.model.TrackedWord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class WordImportService implements WordImport {
    private WordValidity valid;
    private WordService wordService;
    private TwoWordService twoWordService;
    private ThreeWordService threeWordService;
    private ImportedWordService importedWordService;
    private static boolean CRUNCHING_ENABLED = true;

    public WordImportService(WordValidity valid, WordService wordService, TwoWordService twoWordService, ThreeWordService threeWordService, ImportedWordService importedWordService) {
        this.valid = valid;
        this.wordService = wordService;
        this.twoWordService = twoWordService;
        this.threeWordService = threeWordService;
        this.importedWordService = importedWordService;
    }

    @Override
    public Set<ImportedWord> getWords(String message) {
        if (message == null || message.isBlank() || message.isEmpty())
            return new HashSet<>();
        LocalDateTime start = LocalDateTime.now();
        String[] words = message.split(" ");
        List<ImportedWord> importedWords = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            if(words[i] == null) continue;
            ImportedWord tmp = new ImportedWord(words[i].trim());
            if (i + 1 < words.length) tmp.after = words[i + 1];
            if (i - 1 >= 0) tmp.before = words[i - 1];
            if(!valid.isValid(tmp.before)) tmp.before = null;
            if(!valid.isValid(tmp.after)) tmp.after = null;
            if (valid.isValid(tmp)) importedWords.add(tmp);
        }
        HashSet<ImportedWord> wordSet = new HashSet<>(importedWords);
        log.info(String.format("Saving %d imported words...", wordSet.size()));
        importedWordService.saveAll(wordSet);


        int i = 0;
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int size = wordSet.size();
//        wordSet.parallelStream().parallel().forEach(word -> {
//
//            int pct = getPct(atomicInteger.getAndIncrement(), size);
//            Optional<TrackedWord> byWord = wordService.getByWord(word.word);
//            if (byWord.isPresent()) {
//                TrackedWord trackedWordToBeUpdated = byWord.get();
//                trackedWordToBeUpdated.timesSeen++;
//                wordService.saveWord(trackedWordToBeUpdated);
//                log.info(String.format("[%d%%] Saved word %s", pct, trackedWordToBeUpdated));
//            } else {
//                TrackedWord newTrackedWord = new TrackedWord(word.word);
//                wordService.saveWord(newTrackedWord);
//                log.info(String.format("[%d%%] Saved new word %s", pct, newTrackedWord));
//            }
//            createTrackedTwoWords(word.before, word.word);
//            createTrackedTwoWords(word.word, word.after);
//        });
//
//        for (ImportedWord word : wordSet) {
//            int pct = getPct(i++, size);
//            Optional<TrackedWord> byWord = wordService.getByWord(word.word);
//            if (byWord.isPresent()) {
//                TrackedWord trackedWordToBeUpdated = byWord.get();
//                trackedWordToBeUpdated.timesSeen++;
//                wordService.saveWord(trackedWordToBeUpdated);
//                log.info(String.format("[%d%%] Saved word %s", pct, trackedWordToBeUpdated));
//            } else {
//                TrackedWord newTrackedWord = new TrackedWord(word.word);
//                wordService.saveWord(newTrackedWord);
//                log.info(String.format("[%d%%] Saved new word %s", pct, newTrackedWord));
//            }
//
//            if(word.before == null || word.after == null) continue;
//            TrackedWord trackedBefore = wordService.putAndGet(word.before);
//            TrackedWord trackedWord = wordService.putAndGet(word.word);
//            TrackedWord trackedAfter = wordService.putAndGet(word.after);
//
//            createTrackedTwoWords(trackedBefore, trackedWord);
//            createTrackedTwoWords(trackedWord, trackedAfter);
//            createTrackedThreeWords(trackedBefore, trackedWord, trackedAfter);
//        }
//        LocalDateTime finish = LocalDateTime.now();
//        Duration duration = Duration.between(start, finish);
//        log.info(String.format("FINISHED PROCESSING! %d bytes - %d words - duration: %s", message.length(), wordSet.size(), duration));
        return wordSet;
    }

    @Scheduled(fixedDelay = 1000)
    public void processImportedWords() {
        int i=0, size = 100000;
        LocalDateTime start = LocalDateTime.now();
        List<ImportedWord> importedWords = importedWordService.getImportedWords(size);
        for(ImportedWord word : importedWords) {
            int pct = getPct(i++, size);
            Optional<TrackedWord> byWord = wordService.getByWord(word.word);
            if (byWord.isPresent()) {
                TrackedWord trackedWordToBeUpdated = byWord.get();
                trackedWordToBeUpdated.timesSeen++;
                wordService.saveWord(trackedWordToBeUpdated);
                log.info(String.format("[%d%%] Saved word %s", pct, trackedWordToBeUpdated));
            } else {
                TrackedWord newTrackedWord = new TrackedWord(word.word);
                wordService.saveWord(newTrackedWord);
                log.info(String.format("[%d%%] Saved new word %s", pct, newTrackedWord));
            }

            if(word.before != null && word.after != null)
                createTwoAndThreeWords(word);
        }
        importedWordService.deleteAll(importedWords);
        LocalDateTime finish = LocalDateTime.now();
        Duration duration = Duration.between(start, finish);
        long count = importedWordService.getCount();
        if(importedWords.size() > 0)
            log.info(String.format("FINISHED PROCESSING! [Words: %d] - [Duration: %s] - [Remaining: %d]", importedWords.size(), duration, count));
    }

    private void createTwoAndThreeWords(ImportedWord word) {
        TrackedWord trackedBefore = wordService.putAndGet(word.before);
        TrackedWord trackedWord = wordService.putAndGet(word.word);
        TrackedWord trackedAfter = wordService.putAndGet(word.after);

        createTrackedTwoWords(trackedBefore, trackedWord);
        createTrackedTwoWords(trackedWord, trackedAfter);
        createTrackedThreeWords(trackedBefore, trackedWord, trackedAfter);
    }

    private void createTrackedThreeWords(TrackedWord before, TrackedWord word, TrackedWord after) {
        if(before == null || word == null || after == null)
            return;
        TrackedWord trackedBefore = before;
        TrackedWord trackedWord = word;
        TrackedWord trackedAfter = after;
        if (trackedAfter != null && trackedBefore != null && trackedWord != null) {
//            if(CRUNCHING_ENABLED)
//                if (crunchWordCombo(trackedBefore, trackedAfter))
//                    return;

            threeWordService.saveThreeWords(new TrackedThreeWord(trackedBefore, trackedWord, trackedAfter));
            log.info(String.format("Created three words: %s - %s - %s", trackedBefore.word, trackedWord.word, trackedAfter.word));
            return;
        }
    }

    @Async
    @Override
    public CompletableFuture<Set<ImportedWord>> putWords(String message) {
        return CompletableFuture.completedFuture( getWords(message));
    }

    public static int getPct(int wordNum, int total) {
        return (int) ((((double) wordNum) / ((double) total)) * 100.0);
    }

    public void createTrackedTwoWords(TrackedWord before, TrackedWord after) {
        if(before == null || after == null)
            return;
        TrackedWord trackedBefore = before;
        TrackedWord trackedAfter = after;
        if (trackedAfter != null && trackedBefore != null) {
            if(CRUNCHING_ENABLED)
                if (crunchWordCombo(trackedBefore, trackedAfter))
                    return;

            twoWordService.saveTwoWords(new TrackedTwoWord(trackedBefore, trackedAfter));
            log.info(String.format("Created two words: %s - %s", trackedBefore.word, trackedAfter.word));
            return;
        }
    }

    private boolean crunchWordCombo(TrackedWord trackedBefore, TrackedWord trackedAfter) {
        List<TrackedTwoWord> byWords = twoWordService.getByWords(trackedBefore, trackedAfter);
        TrackedTwoWord master = null;
        if (byWords != null && byWords.size() > 0) {
            if (byWords.size() > 1) {
                master = shrinkTrackedTwoWords(byWords);
            }
            if (master == null) master = byWords.get(0);
            master.count++;
            twoWordService.saveTwoWords(master);
            log.info(String.format("Updated two words: %s - %s", trackedBefore.word, trackedAfter.word));
            return true;
        }
        return false;
    }

    private TrackedTwoWord shrinkTrackedTwoWords(List<TrackedTwoWord> byWords) {
        if(byWords == null || byWords.size() < 1) return null;
        if(byWords.size() == 1) return byWords.get(0);
        int sum = byWords.stream().mapToInt(c -> c.count).sum();
        TrackedTwoWord master = byWords.get(0);
        master.count = sum;
        twoWordService.saveTwoWords(master);
        int numCrunched = 0;
        for(int i=1; i< byWords.size(); i++) {
            twoWordService.deleteTwoWords(byWords.get(i));
            numCrunched++;
        }
        log.info(String.format("Crunched combo [%s][%s] from %d records to 1 record.", master.word1, master.word2, numCrunched+1));
        return master;
    }


}
