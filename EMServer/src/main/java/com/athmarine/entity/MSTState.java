package com.athmarine.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
@Table(name = "mst_state")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MSTState {

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

	@ManyToOne
	private MSTCountry country;

	@OneToMany(mappedBy = "stateId")
	private List<VendorCompany> vendorCompany;

	@OneToMany(mappedBy = "stateId")
	private List<CustomerCompany> customerCompany;

	@OneToMany(mappedBy = "states")
	private List<MSTCity> mstCity;

	/*@OneToMany(mappedBy = "states")
	private List<ServiceRequest> serviceRequest;*/

	@OneToMany(mappedBy = "stateId", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	private List<MSTPorts> mstPorts;

	/*
	 * @ManyToMany(mappedBy = "stateId", cascade = CascadeType.MERGE, fetch =
	 * FetchType.LAZY) private List<Services> services;
	 */

	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private String createdBy;

	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;

}
