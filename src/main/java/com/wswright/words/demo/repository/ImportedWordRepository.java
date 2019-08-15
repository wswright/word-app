package com.wswright.words.demo.repository;

import com.wswright.words.demo.model.ImportedWord;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportedWordRepository extends MongoRepository<ImportedWord, ObjectId> {
}
