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
@Document(collection = "threewords")
public class TrackedThreeWord {
    @JsonIgnore
    @Id
    ObjectId objectId;

    @DBRef
    public TrackedWord word1;
    @DBRef
    public TrackedWord word2;
    @DBRef
    public TrackedWord word3;

    public int count=1;

    public TrackedThreeWord() {
        objectId = ObjectId.get();
    }

    public TrackedThreeWord(TrackedWord word1, TrackedWord word2, TrackedWord word3) {
        objectId = ObjectId.get();
        this.word1 = word1;
        this.word2 = word2;
        this.word3 = word3;
    }

    public TrackedThreeWord(TrackedWord word1, TrackedWord word2, TrackedWord word3, int count) {
        objectId = ObjectId.get();
        this.word1 = word1;
        this.word2 = word2;
        this.word3 = word3;
        this.count = count;
    }

    public TrackedThreeWord(ObjectId objectId, TrackedWord word1, TrackedWord word2, TrackedWord word3, int count) {
        this.objectId = objectId;
        this.word1 = word1;
        this.word2 = word2;
        this.word3 = word3;
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackedThreeWord that = (TrackedThreeWord) o;
        return word1.equals(that.word1) &&
                word2.equals(that.word2) &&
                word3.equals(that.word3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word1, word2, word3);
    }

    @Override
    public String toString() {
        return "TrackedTwoWord{" +
                "objectId=" + objectId +
                ", word1=" + word1 +
                ", word2=" + word2 +
                ", word3=" + word3 +
                ", count=" + count +
                '}';
    }
}
