package com.okjike.data.models.dao;

import com.mongodb.MongoClient;
import com.okjike.data.models.AzkabanConfig;
import com.okjike.data.models.Fund;
import com.okjike.data.modules.MongoModule;
import com.okjike.data.modules.MongoModule.Azkaban;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class FundDao extends Dao<Fund> {
    public FundDao(AzkabanConfig config, @Azkaban MongoClient mongoClient) {
        super(MongoModule
                .providesMongoCollection(mongoClient, config.azkabanDatabase,
                    config.fundCollection, Fund.class),
            MongoModule
                .providesMongoCollection(mongoClient, config.azkabanDatabase,
                    config.fundCollection));
    }

    public Fund upsertByFundId(String fundId, Document after) {
        return updateOne(new Document("fundId", fundId), new Document("$set", after), true);
    }

    public Optional<Fund> findByFundId(String fundId) {
        List<Fund> funds = findBy(new Document("fundId", fundId), null, 1, 0);
        return Optional.ofNullable(funds.get(0));
    }
}
