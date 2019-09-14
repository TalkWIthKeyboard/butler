package com.okjike.data.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Fund {

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonProperty("id")
    private ObjectId id;

    @JsonProperty("fundId")
    private String fundId;

    @JsonProperty("name")
    private String name;

    // 原费率
    @JsonProperty("sourceRate")
    private Double sourceRate;

    // 现费率
    @JsonProperty("rate")
    private Double rate;

    // 最小申购金额
    @JsonProperty("minSubscribe")
    private Double minSubscribe;

    // 基金持仓股票代码
    @JsonProperty("stockCodes")
    private List<String> stockCodes;

    // 基金持仓债券代码
    @JsonProperty("bondCodes")
    private List<String> bondCodes;

    // 近一年的收益率
    @JsonProperty("interestRate1N")
    private Double interestRate1N;

    // 近6个月的收益率
    @JsonProperty("interestRate6M")
    private Double interestRate6M;

    // 近3个月的收益率
    @JsonProperty("interestRate3M")
    private Double interestRate3M;

    // 近1个月的收益率
    @JsonProperty("interestRate1M")
    private Double interestRate1M;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSourceRate() {
        return sourceRate;
    }

    public void setSourceRate(Double sourceRate) {
        this.sourceRate = sourceRate;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getMinSubscribe() {
        return minSubscribe;
    }

    public void setMinSubscribe(Double minSubscribe) {
        this.minSubscribe = minSubscribe;
    }

    public List<String> getStockCodes() {
        return stockCodes;
    }

    public void setStockCodes(List<String> stockCodes) {
        this.stockCodes = stockCodes;
    }

    public List<String> getBondCodes() {
        return bondCodes;
    }

    public void setBondCodes(List<String> bondCodes) {
        this.bondCodes = bondCodes;
    }

    public Double getInterestRate1N() {
        return interestRate1N;
    }

    public void setInterestRate1N(Double interestRate1N) {
        this.interestRate1N = interestRate1N;
    }

    public Double getInterestRate6M() {
        return interestRate6M;
    }

    public void setInterestRate6M(Double interestRate6M) {
        this.interestRate6M = interestRate6M;
    }

    public Double getInterestRate3M() {
        return interestRate3M;
    }

    public void setInterestRate3M(Double interestRate3M) {
        this.interestRate3M = interestRate3M;
    }

    public Double getInterestRate1M() {
        return interestRate1M;
    }

    public void setInterestRate1M(Double interestRate1M) {
        this.interestRate1M = interestRate1M;
    }

    @Override
    public String toString() {
        return "Fund{" +
            "id=" + id +
            ", fundId='" + fundId + '\'' +
            ", name='" + name + '\'' +
            ", sourceRate=" + sourceRate +
            ", rate=" + rate +
            ", minSubscribe=" + minSubscribe +
            ", stockCodes=" + stockCodes +
            ", bondCodes=" + bondCodes +
            ", interestRate1N=" + interestRate1N +
            ", interestRate6M=" + interestRate6M +
            ", interestRate3M=" + interestRate3M +
            ", interestRate1M=" + interestRate1M +
            '}';
    }

    public Document toDocument() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return Document.parse(objectMapper.writeValueAsString(this));
    }
}
