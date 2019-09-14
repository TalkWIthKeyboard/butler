package com.okjike.data.modules;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.ReadPreference;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.okjike.data.models.AzkabanConfig;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.mapstruct.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
public class MongoModule {
    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Azkaban {

    }

    @Azkaban
    @Bean
    MongoClient providesMainMongoClient(AzkabanConfig conf) {
        return constructMongoClient(conf.azkabanMongo);
    }

    private MongoClient constructMongoClient(String mongoUrl) {
        return new MongoClient(new MongoClientURI(
            mongoUrl,
            MongoClientOptions.builder()
                .socketTimeout(500)
                .connectTimeout(500)
                .readPreference(ReadPreference.secondaryPreferred())
                .serverSelectionTimeout(200))
        );
    }

    public static <T> MongoCollection<T> providesMongoCollection(MongoClient mongoClient,
                                                                 String database, String collection, Class<T> clazz) {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoDatabase mongoDatabase = mongoClient.getDatabase(database)
            .withCodecRegistry(pojoCodecRegistry);
        return mongoDatabase.getCollection(collection, clazz);
    }

    public static MongoCollection<Document> providesMongoCollection(MongoClient mongoClient,
                                                                    String database, String collection) {
        return mongoClient.getDatabase(database).getCollection(collection);
    }
}
