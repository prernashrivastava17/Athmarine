package com.athmarine.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "approver_bid_relation")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApproverBidRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer approverId;

    private Integer bidId;

    private String bidStatus;

    private String declineReason;
}
