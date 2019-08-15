package com.wswright.words.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Getter
@Setter
@Document(collection = "twowords")
public class TrackedTwoWord {
    @JsonIgnore
    @Id
    ObjectId objectId;

    @DBRef
    public TrackedWord word1;
    @DBRef
    public TrackedWord word2;

    public int count=1;

    public TrackedTwoWord() {
        objectId = ObjectId.get();
    }

    public TrackedTwoWord(TrackedWord word1, TrackedWord word2) {
        objectId = ObjectId.get();
        this.word1 = word1;
        this.word2 = word2;
    }

    public TrackedTwoWord(TrackedWord word1, TrackedWord word2, int count) {
        objectId = ObjectId.get();
        this.word1 = word1;
        this.word2 = word2;
        this.count = count;
    }

    public TrackedTwoWord(ObjectId objectId, TrackedWord word1, TrackedWord word2, int count) {
        this.objectId = objectId;
        this.word1 = word1;
        this.word2 = word2;
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackedTwoWord that = (TrackedTwoWord) o;
        return word1.equals(that.word1) &&
                word2.equals(that.word2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word1, word2);
    }

    @Override
    public String toString() {
        return "TrackedTwoWord{" +
                "objectId=" + objectId +
                ", word1=" + word1 +
                ", word2=" + word2 +
                ", count=" + count +
                '}';
    }
}
