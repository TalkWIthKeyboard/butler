package com.okjike.data.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.okjike.data.exception.MissingFundDetailException;
import com.okjike.data.models.Constants;
import com.okjike.data.models.Fund;
import com.okjike.data.models.FundDaily;
import com.okjike.data.models.dao.FundDailyDao;
import com.okjike.data.models.dao.FundDao;
import com.okjike.data.models.request.FundDetail;
import com.okjike.data.modules.TemplateModule;
import org.springframework.http.*;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class FundService {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final long HAS_NO_CURRENT_UPDATE_TIME = -1;

    private RestTemplate restTemplate;
    private RetryTemplate retryTemplate;
    private HttpHeaders getHeader;
    private FundDao fundDao;
    private FundDailyDao fundDailyDao;
    private JedisPool jedisPool;

    public FundService(RestTemplate restTemplate, RetryTemplate retryTemplate, @TemplateModule.AllTypeGet HttpHeaders getHeader,
                       FundDao fundDao, FundDailyDao fundDailyDao, JedisPool jedisPool) {
        this.restTemplate = restTemplate;
        this.retryTemplate = retryTemplate;
        this.getHeader = getHeader;
        this.fundDao = fundDao;
        this.fundDailyDao = fundDailyDao;
        this.jedisPool = jedisPool;
    }

    public FundDetail getFundDetail(String fundId, Integer fundDailyLimit) throws Exception {
        try (Jedis jedis = jedisPool.getResource()) {
            List<String> hmResponse = jedis.hmget(Constants.REDIS_FUND_DAILY_CACHE_KEY_FORMAT, fundId);
            long todayStartTime = getStartAtDateTimestamp();

            if (Strings.isNullOrEmpty(hmResponse.get(0))) {
                return getFundFromExternal(fundId, todayStartTime, HAS_NO_CURRENT_UPDATE_TIME, fundDailyLimit);
            } else {
                long currentUpdateTime = Long.parseLong(hmResponse.get(0));
                if (todayStartTime > currentUpdateTime) {
                    return getFundFromExternal(fundId, todayStartTime, currentUpdateTime, fundDailyLimit);
                } else {
                    Fund fund = fundDao.findByFundId(fundId).get();
                    List<FundDaily> fundDailies = fundDailyDao.getByFundId(fundId, fundDailyLimit);
                    return new FundDetail(fund, fundDailies);
                }
            }
        }
    }

    private FundDetail getFundFromExternal(String fundId, long todayStartTime, long currentUpdateTime, Integer fundDailyLimit) throws Exception {
        long now = System.currentTimeMillis();
        String responseString = requestFundDetailString(fundId, now);
        Fund fund = new Fund();
        fund.setFundId(fundId);
        fund.setName(getRegexMatcherFirstResult(responseString, Constants.FUND_NAME_REGEX).get());
        fund.setSourceRate(getRegexMatcherFirstResult(responseString, Constants.FUND_SOURCE_RATE_REGEX)
            .map(Double::valueOf).get());
        fund.setRate(getRegexMatcherFirstResult(responseString, Constants.FUND_RATE_REGEX)
            .map(Double::valueOf).get());
        fund.setMinSubscribe(getRegexMatcherFirstResult(responseString, Constants.FUND_MIN_SUBSCRIBE)
            .map(Double::valueOf).get());
        fund.setStockCodes(getRegexMatcherFirstResult(responseString, Constants.FUND_STOCK_CODES)
            .map(this::arrayString2Array).get());
        fund.setBondCodes(getRegexMatcherFirstResult(responseString, Constants.FUND_BOND_CODES)
            .map(this::arrayString2Array).get());
        fund.setInterestRate1N(getRegexMatcherFirstResult(responseString, Constants.FUND_1N_INTEREST_RATE)
            .map(Double::valueOf).get());
        fund.setInterestRate6M(getRegexMatcherFirstResult(responseString, Constants.FUND_6M_INTEREST_RATE)
            .map(Double::valueOf).get());
        fund.setInterestRate3M(getRegexMatcherFirstResult(responseString, Constants.FUND_3M_INTEREST_RATE)
            .map(Double::valueOf).get());
        fund.setInterestRate1M(getRegexMatcherFirstResult(responseString, Constants.FUND_1M_INTEREST_RATE)
            .map(Double::valueOf).get());

        Fund savedFund = fundDao.upsertByFundId(fundId, fund.toDocument());
        List<FundDaily> worthTrendItems = parserList(responseString, Constants.FUND_WORTH_TREND, FundDaily[].class);
        worthTrendItems.forEach(it -> it.setFundId(fundId));

        // 只对最新的数据进行保存
        if (currentUpdateTime > 0) {
            worthTrendItems = worthTrendItems
                .stream()
                .filter(it -> it.getTimestamp() > currentUpdateTime)
                .collect(Collectors.toList());
        }
        List<FundDaily> fundDailyAfterSaved = fundDailyDao.insert(worthTrendItems);
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hmset(Constants.REDIS_FUND_DAILY_CACHE_KEY_FORMAT, ImmutableMap.of(fundId, String.valueOf(todayStartTime)));
        }
        return new FundDetail(savedFund, fundDailyAfterSaved.stream()
            .sorted((td1, td2) -> td2.getTimestamp().compareTo(td1.getTimestamp()))
            .limit(fundDailyLimit)
            .collect(Collectors.toList()));
    }

    private String requestFundDetailString(String fundId, long now) throws MissingFundDetailException {
        String url = String.format(Constants.FUND_DETAIL_URL_FORMAT, fundId, now);
        return retryTemplate.execute(context -> {
            ResponseEntity<String> responseEntity = restTemplate
                .exchange(url, HttpMethod.GET, new HttpEntity<>(getHeader), String.class);
            if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                throw new MissingFundDetailException("Request fund detail meets some problems, code: " + responseEntity.getStatusCode());
            } else {
                return responseEntity.getBody();
            }
        });
    }

    private Optional<String> getRegexMatcherFirstResult(String context, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(context);
        if (matcher.find()) {
            return Optional.of(matcher.group(1));
        }
        return Optional.empty();
    }

    private <T> List<T> parserList(String contenxt, String regex, Class<T[]> clazz) throws IOException {
        String targetString = getRegexMatcherFirstResult(contenxt, regex).get();
        return ImmutableList.copyOf(objectMapper.readValue(targetString, clazz));
    }

    // target context: 1. "6000311","0001572","0004252","3001302","0020202"
    // 2. 0196111,1280352
    private List<String> arrayString2Array(String context) {
        String replacedContext = context.replace("\"", "");
        return ImmutableList.copyOf(replacedContext.split(","));
    }

    private long getStartAtDateTimestamp() {
        Instant instant = Instant.now();
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, zoneId);
        ZonedDateTime zdtStart = zdt.toLocalDate().atStartOfDay(zoneId);
        return Timestamp.from(zdtStart.toInstant()).getTime();
    }
}
