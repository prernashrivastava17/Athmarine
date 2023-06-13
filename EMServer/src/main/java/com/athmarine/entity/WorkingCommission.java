package com.athmarine.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "working_commission")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WorkingCommission {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private Integer workingRateTotalCommission;

	@Column
	private Integer workingRateOT1TotalCommission;

	@Column
	private Integer workingRateOT2TotalCommission;

	@Column
	private Integer holidaysWorkingRateTotalCommission;

	@Column
	private Integer holidaysWorkingRateOT1TotalCommission;

	@Column
	private Integer holidaysWorkingRateOT2TotalCommission;

	@Column
	private int status;
    
	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private String createdBy;
	
	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;

}
