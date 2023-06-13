package com.athmarine.entity;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
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
@Table(name = "mst_manufacturer")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MSTManufacturer {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private String name;

	@Column(updatable = false)
	@CreationTimestamp
	private Date createdAt;

	@Column
	@UpdateTimestamp
	private Date updatedAt;

	@Column
	private int status;
	
	@Column
	private boolean isVerified;

	@ManyToMany(mappedBy = "manufacturerId", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	private List<Equipment> equipment;

	/*@OneToMany(mappedBy = "equipmentManufacturerId")
	private List<Services> service;*/

	@OneToMany(mappedBy = "id")
	private List<ServiceRequestEquipments> serviceRequestEquipments;
	
	@ManyToMany(mappedBy = "manufacturerId", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	private List<VendorServices> services;
	
	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private String createdBy;
	
	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;

	@OneToMany(cascade = CascadeType.ALL)
	private Set<ServiceCategoryEquipmentMapping> serviceCategoryEquipmentMapping;

}
