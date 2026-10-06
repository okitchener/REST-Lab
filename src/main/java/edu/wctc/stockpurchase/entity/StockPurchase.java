package edu.wctc.stockpurchase.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name="stock_purchase")
public class StockPurchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="purchase_id")
    private int id;

    @Column(name="symbol")
    private String symbol;

    @Column(name="price_per_share")
    private Double pricePerShare;

    @Column(name="shares")
    private Double shares;

    @Column(name="purchase_date")
    private LocalDateTime purchaseDate;
}
