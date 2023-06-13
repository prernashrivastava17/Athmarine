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
@Table(name = "travelCommission ")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TravelCommission {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private Integer lumpSumCommission;

	@Column
	private Integer airFareCommission;
	
	@Column
	private Integer transportationCommission;
	
	@Column
	private Integer travelTimeCostCommission;
	
	@Column
	private Integer totalSum;
	
	@Column
	private int status;
	
	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private String createdBy;
	
	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;


}
