package com.stocks.trading.controller;

import com.stocks.trading.models.ApiResponse;
import com.stocks.trading.models.Stocks;
import com.stocks.trading.service.StockSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockSearchController {

    @Autowired
    private StockSearchService stockSearchService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Stocks>>> searchStocks(@RequestParam String query) {
        try {
            List<Stocks> results = stockSearchService.searchStocks(query);
            return ResponseEntity.ok(ApiResponse.success(results));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to search stocks: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Stocks>>> getAllStocks() {
        try {
            List<Stocks> stocks = stockSearchService.getAllStocks();
            return ResponseEntity.ok(ApiResponse.success(stocks));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to fetch stocks: " + e.getMessage()));
        }
    }
}