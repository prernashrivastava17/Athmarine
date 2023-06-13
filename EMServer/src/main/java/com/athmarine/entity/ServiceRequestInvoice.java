package com.athmarine.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "service_request_invoice")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServiceRequestInvoice {

    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String invoiceUID;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="service_request_id", referencedColumnName = "id")
    private ServiceRequest serviceRequest;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="bid_id", referencedColumnName = "id")
    private Bids bid;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="purchase_order_id", referencedColumnName = "id")
    private PurchaseOrder purchaseOrder;

    @Column
    private String invoiceStatus;

    @Column
    private String customer_id;

    @Column
    private String currency;

    @Column
    private String voucherNumber;

    @Column
    private Integer voucherDiscount;

    @Column
    private Integer totalCost;

    @Column(updatable = false)
    @CreationTimestamp
    private Date createdAt;

    @Column
    @UpdateTimestamp
    private Date updatedAt;

    @Column(name = "created_by", updatable = false)
    @CreatedBy
    private String createdBy;

    @Column(name = "updated_by")
    @LastModifiedBy
    private String updatedBy;
}
