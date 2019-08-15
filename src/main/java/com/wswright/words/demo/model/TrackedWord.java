package com.wswright.words.demo.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Objects;


@Getter
@Setter
@ToString
@Document(collection = "words")
public class TrackedWord {
    @Id
    @Field("word")
    public String word;
    @Field("timesSeen")
    public int timesSeen;


    public TrackedWord() {
    }

    public TrackedWord(String word) {
        this.word = word;
        this.timesSeen = 1;
    }

    public TrackedWord(String word, int timesSeen) {
        this.word = word;
        this.timesSeen = timesSeen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackedWord that = (TrackedWord) o;
        return word.equals(that.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word);
    }
}
