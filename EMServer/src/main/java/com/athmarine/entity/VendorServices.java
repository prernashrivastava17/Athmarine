package com.athmarine.entity;


import java.sql.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "VendorService")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorServices {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "created_at", updatable = false)
	@CreationTimestamp
	private Date createdAt;

	@Column(name = "updated_at")
	@UpdateTimestamp
	private Date updatedAt;

	@Column
	private int status;
	
	@Column(columnDefinition = "bit default 0")
	private boolean vendorServiceKey;


	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinTable(name = "VendorService_equipment", joinColumns = { @JoinColumn(name = "service_id") }, inverseJoinColumns = {
			@JoinColumn(name = "equipmentId") })
	private List<Equipment> equipmentId;



	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinTable(name = "VendorService_port", joinColumns = { @JoinColumn(name = "service_id") }, inverseJoinColumns = {
			@JoinColumn(name = "portId") })
	@JsonIgnoreProperties("VendorService")
	private List<MSTPorts> portId;

	@ManyToMany
	@JoinTable(name = "VendorService_category", joinColumns = {
			@JoinColumn(name = "service_id") }, inverseJoinColumns = { @JoinColumn(name = "categoryId") })
	@JsonIgnoreProperties("VendorService")
	private List<MSTEquipmentCategory> categoryId;

	@ManyToMany
	@JoinTable(name = "VendorService_manufacturer", joinColumns = {
			@JoinColumn(name = "service_id") }, inverseJoinColumns = { @JoinColumn(name = "manufacturerId") })
	@JsonIgnoreProperties("VendorService")
	private List<MSTManufacturer> manufacturerId;

	@OneToMany(mappedBy = "vendor_services")
	private List<Experience> experience;

	@ManyToOne
	@JoinColumn(name = "companyId")
	private User companyId;

	@ManyToOne
	@JoinColumn(name = "country_Id")
	private MSTCountry countryIds;

	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private String createdBy;

	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;

	@OneToMany(cascade = CascadeType.ALL)
	private Set<ServiceCategoryEquipmentMapping> serviceCategoryEquipmentMapping;

}