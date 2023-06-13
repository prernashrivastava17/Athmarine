package com.athmarine.entity;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mst_equipment_category")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MSTEquipmentCategory {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private String name;

	@Column
	private String shortName;

	@Column(updatable = false)
	@CreationTimestamp
	private Date createdAt;

	@Column
	@UpdateTimestamp
	private Date updatedAt;

	@Column
	private boolean isVerified;

	@Column
	private int status;

	@OneToMany(mappedBy = "category")
	private List<Equipment> equipment;

	/*
	 * @OneToMany(mappedBy = "equipmentCategory") private List<Services> service;
	 */

	@ManyToMany(mappedBy = "categoryId", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	private List<EngineerEquiments> engineerEquiments;

//	@ManyToMany(mappedBy = "categoryId2", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	// private List<EngineerEquiments2> engineerEquiments2;

	@OneToMany(mappedBy = "id")
	private List<ServiceRequestEquipments> serviceRequestEquipments;
	
	@ManyToMany(mappedBy = "categoryId", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	private List<VendorServices> services;
	
	@OneToMany(mappedBy = "equipmentCategory")
	private List<ServiceRequest> serviceRequest;
	
	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private String createdBy;
	
	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;

	@OneToMany(cascade = CascadeType.ALL)
	private Set<ServiceCategoryEquipmentMapping> serviceCategoryEquipmentMapping;

}
