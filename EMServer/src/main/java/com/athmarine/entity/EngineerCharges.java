package com.athmarine.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "engineer_charges")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EngineerCharges {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column
	private Integer basePrice;
	
	@Column
	private Integer extraPrice;
	
	@Column
	private Integer extraAbove;
	
	@Column
	private Integer maxCharge;
	
	@Column
	private Integer baseEngeerCount;
	
	@Column
	private Integer extraEngeerCount;
	
	@Column
	private Integer extraAboveEngeerCount;
	
	@Column
	private Integer maxChargeEngeerCount;

	@OneToOne
	@JoinColumn(name = "companyId")
	private User userCharges;
	
	@Column(updatable = false)
	@CreationTimestamp
	private Date createdAt;

	@Column
	@UpdateTimestamp
	private Date updatedAt;
	
	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private String createdBy;
	
	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;

}
