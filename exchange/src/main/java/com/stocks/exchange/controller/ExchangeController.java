package com.stocks.exchange.controller;

import com.stocks.exchange.models.Stocks;
import com.stocks.exchange.service.StocksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin( origins = "*" )
@RequestMapping("/api/exchange")
public class ExchangeController {

    @Autowired
    private StocksService stocksService;

    private boolean isAdmin(String username, String password) {
        return "admin".equals(username) && "admin".equals(password);
    }

    // Add stock
    @PostMapping("/add")
    public ResponseEntity<?> addStock(
            @RequestParam String username,
            @RequestParam String password,
            @RequestBody Stocks stock) {

        if (!isAdmin(username, password)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        Stocks created = stocksService.addStock(stock);
        return ResponseEntity.ok(created); // Return the newly created stock in the response body
    }

    // Update stock
    @PutMapping("/update")
    public ResponseEntity<?> updateStock(
            @RequestParam String username,
            @RequestParam String password,
            @RequestBody Stocks stock) {

        if (!isAdmin(username, password)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        Stocks updated = stocksService.updateStock(stock);
        return ResponseEntity.ok(updated); // Return the updated stock in the response body
    }

    // Delete stock
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStock(
            @RequestParam String username,
            @RequestParam String password,
            @PathVariable int id) {

        if (!isAdmin(username, password)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        stocksService.deleteStock(id);
        return ResponseEntity.ok("Stock deleted successfully (id=" + id + ")");
    }

    // View stocks
    @GetMapping("/view")
    public ResponseEntity<?> viewStocks(
            @RequestParam String username,
            @RequestParam String password) {

        if (!isAdmin(username, password)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        List<Stocks> all = stocksService.getAllStocks();
        return ResponseEntity.ok(all);
    }
}
