package com.stocks.trading.controller;

import com.stocks.trading.dto.TradeRequest;
import com.stocks.trading.dto.TradeResponse;
import com.stocks.trading.service.TradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trade")
@CrossOrigin(origins = "*")
public class TradingController {

    @Autowired
    private TradingService tradingService;

    @PostMapping("/buy")
    public ResponseEntity<TradeResponse> buyStocks(@RequestBody TradeRequest request) {
        try {
            TradeResponse response = tradingService.buyStocks(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(TradeResponse.builder()
                            .status("FAILED")
                            .message(e.getMessage())
                            .build());
        }
    }

    @PostMapping("/sell")
    public ResponseEntity<TradeResponse> sellStocks(@RequestBody TradeRequest request) {
        try {
            TradeResponse response = tradingService.sellStocks(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(TradeResponse.builder()
                            .status("FAILED")
                            .message(e.getMessage())
                            .build());
        }
    }

    @GetMapping("/price/{symbol}")
    public ResponseEntity<Double> getStockPrice(@PathVariable String symbol) {
        double price = tradingService.getCurrentStockPrice(symbol);
        return ResponseEntity.ok(price);
    }
}