package com.okjike.data.models.dao;

import com.mongodb.MongoClient;
import com.okjike.data.models.AzkabanConfig;
import com.okjike.data.models.FundDaily;
import com.okjike.data.modules.MongoModule;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FundDailyDao extends Dao<FundDaily> {
    public FundDailyDao(AzkabanConfig config, @MongoModule.Azkaban MongoClient mongoClient) {
        super(MongoModule
                .providesMongoCollection(mongoClient, config.azkabanDatabase,
                    config.fundDailyCollection, FundDaily.class),
            MongoModule
                .providesMongoCollection(mongoClient, config.azkabanDatabase,
                    config.fundDailyCollection));
    }

    public List<FundDaily> getByFundId(String fundId, Integer limit) {
        return findBy(new Document("fundId", fundId), new Document("timestamp", -1), limit, 0);
    }
}
