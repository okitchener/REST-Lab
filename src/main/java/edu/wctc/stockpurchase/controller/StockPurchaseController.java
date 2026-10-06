package edu.wctc.stockpurchase.controller;

import com.github.fge.jsonpatch.JsonPatch;
import edu.wctc.stockpurchase.entity.StockPurchase;
import edu.wctc.stockpurchase.service.StockPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/stockpurchases")
public class StockPurchaseController {

    private final StockPurchaseService stockPurchaseService;

    @Autowired
    public StockPurchaseController(StockPurchaseService stockPurchaseService) {
        this.stockPurchaseService = stockPurchaseService;
    }

    @GetMapping
    public List<StockPurchase> getAll() {
        return stockPurchaseService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockPurchase> getOne(@PathVariable int id) {
        return ResponseEntity.of(stockPurchaseService.findById(id));
    }

    @PostMapping
    public ResponseEntity<StockPurchase> create(@RequestBody StockPurchase purchase) {
        StockPurchase saved = stockPurchaseService.create(purchase);
        return ResponseEntity.created(URI.create("/api/stockpurchases/" + saved.getId())).body(saved);
    }

    // The Postman collection sends the id in the body
    @PutMapping
    public ResponseEntity<StockPurchase> replace(@RequestBody StockPurchase purchase) {
        return ResponseEntity.of(stockPurchaseService.replace(purchase.getId(), purchase));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockPurchase> replace(@PathVariable int id, @RequestBody StockPurchase purchase) {
        return ResponseEntity.of(stockPurchaseService.replace(id, purchase));
    }

    @PatchMapping(path = "/{id}", consumes = {"application/json-patch+json", "application/json"})
    public ResponseEntity<StockPurchase> patch(@PathVariable int id, @RequestBody String patchBody) throws Exception {
        JsonPatch patch = new com.fasterxml.jackson.databind.ObjectMapper().readValue(patchBody, JsonPatch.class);
        return ResponseEntity.of(stockPurchaseService.patch(id, patch));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        stockPurchaseService.delete(id);
    }
}
