package com.okjike.data.controllers;

import com.okjike.data.models.request.FundDetail;
import com.okjike.data.services.FundService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/fund")
public class FundHttpController {

    private FundService fundService;

    public FundHttpController(FundService fundService) {
        this.fundService = fundService;
    }

    @GetMapping("/detail")
    public Mono<FundDetail> getFund(@RequestParam String fundId,
                                    @RequestParam(defaultValue = "20") Integer dailyLimit) throws Exception {
        return Mono.just(fundService.getFundDetail(fundId, dailyLimit));
    }
}
