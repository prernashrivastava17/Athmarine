package com.athmarine.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "experience")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Experience {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column
	private String experience;
	
	@ManyToOne
	@JoinColumn(name = "enggequip")
	private EngineerEquiments enggequip;
	
	@ManyToOne
	@JoinColumn(name = "equipment")
	private Equipment equipment;
	
	@ManyToOne
	@JoinColumn(name = "vendor_services")
	private VendorServices vendor_services;
	
	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private String createdBy;
	
	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;


}
