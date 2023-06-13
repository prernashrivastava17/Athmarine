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
@Table(name = "spares_commission")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SparesCommission {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private Integer itemOneCommission;

	@Column
	private Integer itemTwoCommission;

	@Column
	private Integer itemThreeCommission;

	@Column
	private Integer itemFourCommission;

	@Column
	private Integer itemFiveCommission;

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
