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
@Table(name = "working_customer")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WorkingCustomer {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private Integer workingRate;

	@Column
	private String workingRateComment;

	@Column
	private Integer estimatedWorkingHours;

	@Column
	private String estimatedWorkingHoursComment;

	@Column
	private Integer workingRateOT1;

	@Column
	private String workingRateOT1Comment;

	@Column
	private Integer estimatedWorkingHours1;

	@Column
	private String estimatedWorkingHours1Comment;

	@Column
	private Integer workingRateOT2;

	@Column
	private String workingRateOT2Comment;

	@Column
	private Integer estimatedWorkingHours2;

	@Column
	private String estimatedWorkingHours2Comment;

	@Column
	private Integer holidaysWorkingRate;

	@Column
	private String holidaysWorkingRateComment;

	@Column
	private Integer holidaysEstimatedWorkingHours;

	@Column
	private String holidaysEstimatedWorkingHoursComment;

	@Column
	private Integer holidaysWorkingRateOT1;

	@Column
	private String holidaysWorkingRateOT1Comment;

	@Column
	private Integer holidaysEstimatedWorkingHours1;

	@Column
	private String holidaysEstimatedWorkingHours1Comment;

	@Column
	private Integer holidaysWorkingRateOT2;

	@Column
	private String holidaysWorkingRateOT2Comment;

	@Column
	private Integer holidaysEstimatedWorkingHours2;

	@Column
	private String holidaysEstimatedWorkingHours2Comment;

	@Column
	private Integer totalSum;

//	@OneToOne(fetch = FetchType.LAZY, mappedBy = "workingCustomer")
//	private Bids bids;

	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private String createdBy;

	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;

}
