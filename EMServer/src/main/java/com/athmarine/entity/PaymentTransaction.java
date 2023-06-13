package com.athmarine.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name="payment_transaction")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentTransaction {

    @Column(unique = true, nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "system_transaction_id")
    private String system_transactionId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(updatable = false, name = "transaction_date")
    @CreationTimestamp
    private Date transactionDate;

    @Column(name = "request_id")
    private String requestId;

    @Column(name = "email")
    private String email;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "currency")
    private String currency;

    @Nationalized
    @Column(name = "currency_symbol", columnDefinition = "TEXT")
    private String currencySymbol;

    @Column(name = "payment_mode")
    private String paymentMode;

    @Column(name = "transaction_status")
    private String transactionStatus;

    @Column(name = "last_four")
    private String lastFour;

    @Column(updatable = false, name = "transaction_local_date")
    private LocalDateTime transactionLocalDate;

    @Column(name = "receipt_url")
    private String receiptUrl;

    @Column(name = "idempotency_key")
    private String idempotencyKey;

    @Column(name = "request",length = 1000)
    private String request;

    @Column(name = "response",length = 5000)
    private String response;

}