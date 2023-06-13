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
@Table(name = "working_vendor")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkingVendor {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	private Bids bids;

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
	private Integer approvedAmount;

	@Column
	private String extraExpenseApprovalStatus;
	
	@Column
	private String comments;
	
	@Column
	private Integer fareValue; 

//	@Column
//	private Integer workingRate;
//
//	@Column
//	private Integer estimatedWorkingHours;
//
//	@Column
//	private Integer workingRateOT1;
//
//	@Column
//	private Integer estimatedWorkingHours1;
//
//	@Column
//	private Integer workingRateOT2;
//
//	@Column
//	private Integer estimatedWorkingHours2;
//
//	@Column
//	private Integer holidaysWorkingRate;
//
//	@Column
//	private Integer holidaysEstimatedWorkingHours;
//
//	@Column
//	private Integer holidaysWorkingRateOT1;
//
//	@Column
//	private Integer holidaysEstimatedWorkingHours1;
//
//	@Column
//	private Integer holidaysWorkingRateOT2;
//
//	@Column
//	private Integer holidaysEstimatedWorkingHours2;
//
//	@Column
//	private Integer totalSum;

//	@OneToOne(fetch = FetchType.LAZY, mappedBy = "workingVendor")
//	private Bids bids;

	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private String createdBy;

	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;

	

}