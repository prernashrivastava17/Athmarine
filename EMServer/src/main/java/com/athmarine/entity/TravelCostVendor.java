package com.athmarine.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "travel_cost_vendor")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TravelCostVendor {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

//	@Column
//	private Integer lumpSumCost;
//
//	@Column
//	private Integer airFare;
//
//	@Column
//	private Integer transportation;
//
//	@Column
//	private Integer engineerRatePerHour;
//
//	@Column
//	private Integer totalTravelTime;
//
//	@Column
//	private Integer travelTimeCost;
//
//	@Column
//	private Integer totalSum;
	

//	@OneToOne(fetch = FetchType.LAZY, mappedBy = "travelCostVendor")
//	private Bids bids;

//	@Column
//	private int status;
	
	@Column
	private String inline;
	
	
	@ManyToOne
	private Bids bids;

	@Column
	private Integer proposedAmount;
	
	@Column
	private Integer amountYouGet;
	
	@Column
	private Integer extraExpenses;

	@Column
	private Integer extraExpensesYouGet;

	@Column
	private String extraExpenseApprovalStatus;
	
	@Column
	private Integer approvedAmount;
	
	@Column
	private String comments;
	@Column
	private Integer fareValue;
	
	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private String createdBy;
	
	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;
//	
//	@ManyToOne
//	@JoinColumn(name = "bidder_Id")
//	private User bidderId;

}
