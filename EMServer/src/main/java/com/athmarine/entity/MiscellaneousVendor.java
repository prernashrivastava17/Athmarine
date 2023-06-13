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
@Table(name = "miscellaneous_vendor")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MiscellaneousVendor {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	
	@ManyToOne
	private Bids bids;

//	@Column
//	private Integer portCharges;
//
//	@Column
//	private Integer covidTestCharge;
//
//	@Column
//	private Integer shipyardCharges;
//
//	@Column
//	private Integer shipyardSurcharge;
//
//	@Column
//	private Integer otherCharge;
//
//	@Column
//	private Integer totalSum;

//	@OneToOne(fetch = FetchType.LAZY, mappedBy = "miscellaneousVendor")
//	private Bids bids;

//	@ManyToOne
//	@JoinColumn(name = "bidder_Id")
//	private User bidderId;

	
	@Column
	private String inline;

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

}
