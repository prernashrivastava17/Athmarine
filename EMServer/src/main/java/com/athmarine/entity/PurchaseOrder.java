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
@Table(name = "purchase_order")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PurchaseOrder {

    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /*@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="service_request_id", referencedColumnName = "id")
    private ServiceRequest serviceRequest;*/

    @ManyToOne
    @JoinColumn(name = "service_request_id")
    private ServiceRequest serviceRequest;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="bid_id", referencedColumnName = "id")
    private Bids bid;

    @Column
    private String poUID;

    @Column
    private String poStatus;

    @Column
    private String customerPoNo;

    @Column
    private String contactPersonName;

    @Column
    private String currency;

    @Column
    private Boolean status;

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

    @OneToOne(mappedBy = "purchaseOrder")
    private ServiceRequestInvoice serviceRequestInvoice;
}
