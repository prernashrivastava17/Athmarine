package com.athmarine.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "travelCostCustomer ")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TravelCostCustomer {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private Integer lumpSumCost;

	@Column
	private String lumpSumCostComment;

	@Column
	private Integer airFare;

	@Column
	private String airFareComment;

	@Column
	private Integer transportation;

	@Column
	private String transportationComment;

	@Column
	private Integer engineerRatePerHour;

	@Column
	private String engineerRatePerHourComment;

	@Column
	private Integer totalTravelTime;

	@Column
	private String totalTravelTimeComment;

	@Column
	private Integer travelTimeCost;

	@Column
	private String travelTimeCostComment;

	@Column
	private Integer totalSum;

	@Column
	private int status;

//	@OneToOne(fetch = FetchType.LAZY, mappedBy = "travelCostCustomer")
//	private Bids bids;

	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private String createdBy;

	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;

}
