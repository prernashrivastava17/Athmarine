package com.athmarine.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import org.hibernate.validator.constraints.UniqueElements;

@Entity
@Table(name = "bidder_request_relation")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BidderRequestRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer requestId;

    private Integer bidderId;

    private String requestStatus;
    
    private Integer BidId;

}
