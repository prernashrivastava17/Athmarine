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

/*@Entity
@Table(name = "miscellaneous_median")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor*/
public class MiscellaneousMedian {

	/*@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private Integer portChargesMedian;

	@Column
	private Integer covidTestChargeMedian;

	@Column
	private Integer shipyardChargesMedian;

	@Column
	private Integer shipyardSurchargeMedian;

	@Column
	private Integer otherChargeMedian;

	@Column
	private Integer totalSum;

	@Column
	private Integer bidId;

	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private String createdBy;

	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;*/

}
