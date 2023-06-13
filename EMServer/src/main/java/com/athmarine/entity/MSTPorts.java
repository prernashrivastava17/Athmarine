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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
@Table(name = "mst_ports")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MSTPorts {

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

	@ManyToMany(mappedBy = "portId", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	private List<VendorServices> services;

	@OneToMany(mappedBy = "port")
	private List<ServiceRequest> serviiceRequest;

	/*
	 * @OneToMany(mappedBy="id") private List<CustomerCompany> customerCompany;
	 */

	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinTable(name = "mstports_state", joinColumns = { @JoinColumn(name = "mstport_id") }, inverseJoinColumns = {
			@JoinColumn(name = "stateId") })
	@JsonIgnoreProperties("mst_ports")
	private MSTState stateId;

	@ManyToOne
	private MSTCountry country;
	
	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private String createdBy;
	
	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;


}
