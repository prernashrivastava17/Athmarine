package com.athmarine.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vessel")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Vessel {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private String shipname;

	@Column
	private String MMSI;

	@Column
	private Integer IMO;

	@Column
	private Integer shipId;

	@Column
	private String callSign;

	@Column
	private String typeName;

	@Column
	private Integer DWT;

	@Column
	private String flag;

	@Column
	private String country;

	@Column
	private String yearOfBuilt;

	@Column
	private String MT_URL;
	
	@OneToMany(mappedBy = "vessel")
	private List<ServiceRequest> serviceRequest;
	
	@Column
	private int status;
	
//	@ManyToOne
//	private CustomerCompany customerCompany;
	
	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private String createdBy;
	
	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;


}
