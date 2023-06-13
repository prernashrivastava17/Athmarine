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
@Table(name = "equipment")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Equipment {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private String name;

	@Column
	private String code;

	@Column
	private String isVerified;

	@Column
	private String experience;

	@Column(updatable = false)
	@CreationTimestamp
	private Date createdAt;

	@Column
	@UpdateTimestamp
	private Date updatedAt;

	@Column
	private Integer status;

	@ManyToOne
	private MSTEquipmentCategory category;

	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinTable(name = "equipment_manufacturer", joinColumns = { @JoinColumn(name = "equipment_id") }, inverseJoinColumns = {
			@JoinColumn(name = "manufacturerId") })
	private List<MSTManufacturer> manufacturerId;
	
	@ManyToMany(mappedBy = "equipmentId", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	private List<VendorServices> demoServices;

	@ManyToMany(mappedBy = "equimentId", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	private List<EngineerEquiments> engineerEquiments;

	// @ManyToMany(mappedBy = "equipmentId2", cascade = CascadeType.MERGE, fetch =
	// FetchType.LAZY)
	// private List<EngineerEquiments2> engineerEquiments2;

	@OneToMany(mappedBy = "id")
	private List<ServiceRequestEquipments> serviceRequestEquipments;
	
	@OneToMany(mappedBy = "equipment")
	private List<Experience> experiences;
	
	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private String createdBy;
	
	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;

	@OneToMany(cascade = CascadeType.ALL)
	private Set<ServiceCategoryEquipmentMapping> serviceCategoryEquipmentMapping;

}
