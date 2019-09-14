package com.okjike.data.models.dao;

import com.google.common.collect.ImmutableList;
import com.mongodb.MongoInternalException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.springframework.lang.Nullable;

import java.util.List;


public abstract class Dao<T> {
    protected MongoCollection<T> collection;
    protected MongoCollection<Document> documentCollection;

    public Dao(MongoCollection<T> collection, MongoCollection<Document> documentCollection) {
        this.collection = collection;
        this.documentCollection = documentCollection;
    }

    public List<T> findBy(Document filter, @Nullable Document sorter,
                          @Nullable Integer limit, @Nullable Integer skip) {
        FindIterable<T> findIterable = collection.find(filter);
        if (sorter != null) {
            findIterable.sort(sorter);
        }
        if (limit != null) {
            findIterable.limit(limit);
        }
        if (skip != null) {
            findIterable.skip(skip);
        }
        return ImmutableList.copyOf(findIterable);
    }

    public List<T> insert(List<T> pojos) {
        collection.insertMany(pojos);
        return pojos;
    }

    public T updateOne(Document filter, Document after, boolean upsert) {
        UpdateResult updateResult = collection
            .updateOne(filter, after, new UpdateOptions().upsert(upsert));
        if (!updateResult.wasAcknowledged()) {
            throw new MongoInternalException("Update the document meeting some problems.");
        }
        return collection.find(filter).first();
    }

    public void remove(Document filter) {
        DeleteResult deleteResult = collection.deleteMany(filter);
        if (!deleteResult.wasAcknowledged()) {
            throw new MongoInternalException("Delete the document meeting some problems.");
        }
    }
}
