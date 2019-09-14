package com.okjike.data.models;

public final class Constants {
    public static final String FUND_DETAIL_URL_FORMAT = "http://fund.eastmoney.com/pingzhongdata/%s.js?v=%d";

    public static final String FUND_NAME_URL = "http://fund.eastmoney.com/js/fundcode_search.js";

    public static final String FUND_COMPANY_NAME_URL = "http://fund.eastmoney.com/js/jjjz_gs.js?dt=%d";

    public static final Integer REQUEST_TIMEOUT = 3_000;

    public static final String FUND_NAME_REGEX = "fS_name = \"(.*?)\"";

    public static final String FUND_SOURCE_RATE_REGEX = "fund_sourceRate=\"(.*?)\"";

    public static final String FUND_RATE_REGEX = "fund_Rate=\"(.*?)\"";

    public static final String FUND_MIN_SUBSCRIBE = "fund_minsg=\"(.*?)\"";

    public static final String FUND_STOCK_CODES = "stockCodes=\\[(.*?)\\]";

    public static final String FUND_BOND_CODES = "zqCodes=\"(.*?)\"";

    public static final String FUND_1N_INTEREST_RATE = "syl_1n=\"(.*?)\"";

    public static final String FUND_6M_INTEREST_RATE = "syl_6y=\"(.*?)\"";

    public static final String FUND_3M_INTEREST_RATE = "syl_3y=\"(.*?)\"";

    public static final String FUND_1M_INTEREST_RATE = "syl_1y=\"(.*?)\"";

    public static final String FUND_WORTH_TREND = "Data_netWorthTrend = (.*?);";

    public static final String REDIS_FUND_CACHE_KEY_FORMAT = "azkaban:fund-cache:%s";

    public static final String REDIS_FUND_DAILY_CACHE_KEY_FORMAT = "azkaban:fund-daily-cache";
}
