package com.okjike.data.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.Document;
import org.bson.types.ObjectId;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FundDaily {

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonProperty("id")
    private ObjectId id;

    private Long timestamp;

    private Double worth;

    // 基金id
    @JsonProperty("fundId")
    private String fundId;

    // 净值回报
    @JsonProperty("equityReturn")
    private Double equityReturn;

    // 每份回报金
    @JsonProperty("unitMoney")
    private String unitMoney;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    @JsonGetter("timestamp")
    public Long getTimestamp() {
        return this.timestamp;
    }

    @JsonSetter("x")
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @JsonGetter("worth")
    public Double getWorth() {
        return this.worth;
    }

    @JsonSetter("y")
    public void setWorth(Double worth) {
        this.worth = worth;
    }

    public Double getEquityReturn() {
        return equityReturn;
    }

    public void setEquityReturn(Double equityReturn) {
        this.equityReturn = equityReturn;
    }

    public String getUnitMoney() {
        return unitMoney;
    }

    public void setUnitMoney(String unitMoney) {
        this.unitMoney = unitMoney;
    }

    @Override
    public String toString() {
        return "FundDaily{" +
            "id=" + id +
            ", timestamp=" + timestamp +
            ", worth=" + worth +
            ", equityReturn=" + equityReturn +
            ", unitMoney='" + unitMoney + '\'' +
            '}';
    }

    public Document toDocument() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return Document.parse(objectMapper.writeValueAsString(this));
    }
}
