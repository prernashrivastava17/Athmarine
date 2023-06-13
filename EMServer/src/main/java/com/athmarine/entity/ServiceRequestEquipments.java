package com.athmarine.entity;

import java.util.Date;

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

@Entity
@Table(name = "serviceRequest_equipments")
public class ServiceRequestEquipments {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "model")
	private String model;

	@Column
	private Date createdAt;

	@Column
	private Date updatedAt;

	@Column
	private int status;

	@ManyToOne
	@JoinColumn(name = "brand")
	private MSTManufacturer mSTManufacturer;

	@ManyToOne
	private ServiceRequest serviceRequested;

	@ManyToOne
	@JoinColumn(name = "equipmentId")
	private Equipment equipment;

	@ManyToOne
	@JoinColumn(name = "categoryId")
	private MSTEquipmentCategory mSTEquipmentCategory;

	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private String createdBy;

	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;

}
