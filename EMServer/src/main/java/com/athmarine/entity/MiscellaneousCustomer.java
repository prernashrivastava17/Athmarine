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
@Table(name = "miscellaneous_customer")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MiscellaneousCustomer {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private Integer portCharges;

	@Column
	private String portChargesComment;

	@Column
	private Integer covidTestCharge;

	@Column
	private String covidTestChargeComment;

	@Column
	private Integer shipyardCharges;

	@Column
	private String shipyardChargesComment;

	@Column
	private Integer shipyardSurcharge;

	@Column
	private String ShipyardSurchargeComment;

	@Column
	private Integer otherCharge;

	@Column
	private String otherChargeComment;

	@Column
	private Integer totalSum;

	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private String createdBy;

	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;

}
