package edu.wctc.stockpurchase.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonpatch.JsonPatch;
import edu.wctc.stockpurchase.entity.StockPurchase;
import edu.wctc.stockpurchase.repo.StockPurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StockPurchaseService {
    private final StockPurchaseRepository stockPurchaseRepository;
    private final ObjectMapper patchMapper = new ObjectMapper();

    @Autowired
    public StockPurchaseService(StockPurchaseRepository stockPurchaseRepository) {
        this.stockPurchaseRepository = stockPurchaseRepository;
    }

    public List<StockPurchase> findAll() {
        return (List<StockPurchase>) stockPurchaseRepository.findAll();
    }

    public Optional<StockPurchase> findById(int id) {
        return stockPurchaseRepository.findById(id);
    }

    public StockPurchase create(StockPurchase purchase) {
        purchase.setId(0);
        return stockPurchaseRepository.save(purchase);
    }

    public Optional<StockPurchase> replace(int id, StockPurchase purchase) {
        if (!stockPurchaseRepository.existsById(id)) {
            return Optional.empty();
        }
        purchase.setId(id);
        return Optional.of(stockPurchaseRepository.save(purchase));
    }

    public void delete(int id) {
        if (stockPurchaseRepository.existsById(id)) {
            stockPurchaseRepository.deleteById(id);
        }
    }

    public Optional<StockPurchase> patch(int id, JsonPatch patch) throws Exception {
        Optional<StockPurchase> existing = stockPurchaseRepository.findById(id);
        if (existing.isEmpty()) {
            return Optional.empty();
        }
        StockPurchase sp = existing.get();

        ObjectNode node = patchMapper.createObjectNode();
        node.put("id", sp.getId());
        node.put("symbol", sp.getSymbol());
        if (sp.getPricePerShare() != null) node.put("pricePerShare", sp.getPricePerShare());
        if (sp.getShares() != null) node.put("shares", sp.getShares());
        if (sp.getPurchaseDate() != null) node.put("purchaseDate", sp.getPurchaseDate().toString());

        JsonNode patched = patch.apply(node);

        sp.setSymbol(text(patched, "symbol"));
        sp.setPricePerShare(number(patched, "pricePerShare"));
        sp.setShares(number(patched, "shares"));
        String date = text(patched, "purchaseDate");
        sp.setPurchaseDate(date == null ? null : LocalDateTime.parse(date));
        return Optional.of(stockPurchaseRepository.save(sp));
    }

    private static String text(JsonNode n, String field) {
        JsonNode v = n.get(field);
        return v == null || v.isNull() ? null : v.asText();
    }

    private static Double number(JsonNode n, String field) {
        JsonNode v = n.get(field);
        return v == null || v.isNull() ? null : v.asDouble();
    }
}
