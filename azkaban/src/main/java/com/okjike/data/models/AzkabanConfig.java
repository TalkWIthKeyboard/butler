package com.okjike.data.models;

import com.okjike.kejiva.config.annotation.Config;
import com.okjike.kejiva.config.annotation.Field;

@Config
public class AzkabanConfig {

    @Field(key = "is_debug")
    public Boolean isDebug;

    @Field(key = "azkaban_mongo")
    public String azkabanMongo;

    @Field(key = "azkaban_database")
    public String azkabanDatabase;

    @Field(key = "fund_collection")
    public String fundCollection;

    @Field(key = "fund_daily_collection")
    public String fundDailyCollection;

    @Field(key = "azkaban_redis_host")
    public String azkabanRedisHost;

    @Field(key = "azkaban_redis_port")
    public Integer azkabanRedisPort;
}
