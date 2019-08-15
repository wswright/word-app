package com.wswright.words.demo.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document(collection = "importedwords")
public class ImportedWord {
    @Id
    public ObjectId id;
    public String word;
    public String before;
    public String after;

    public ImportedWord() {
        id = ObjectId.get();
    }

    public ImportedWord(String word) {
        this.id = ObjectId.get();
        this.word = word;
        this.before = null;
        this.after = null;
    }

    public ImportedWord(ObjectId id, String word, String before, String after) {
        this.id = id;
        this.word = word;
        this.before = before;
        this.after = after;
    }

    public ImportedWord(String word, String before, String after) {
        this.id = ObjectId.get();
        this.word = word;
        this.before = before;
        this.after = after;
    }
}
