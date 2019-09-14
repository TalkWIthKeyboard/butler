package com.okjike.data.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.okjike.data.models.Fund;
import com.okjike.data.models.FundDaily;

import java.util.List;

public class FundDetail {

    @JsonProperty("fund")
    private Fund fund;

    @JsonProperty("fundDaily")
    private List<FundDaily> fundDetailList;

    public FundDetail() {

    }

    public FundDetail(Fund fund, List<FundDaily> fundDetailList) {
        this.fund = fund;
        this.fundDetailList = fundDetailList;
    }
}
