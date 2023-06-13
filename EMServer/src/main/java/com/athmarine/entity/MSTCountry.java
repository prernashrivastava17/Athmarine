package com.athmarine.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mst_country")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MSTCountry {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	@NotNull
	private String name;

	@Column
	private String phoneCode;

	@Column
	private String shortName;

	@Column
	private String currency;

	@Column(updatable = false)
	@CreationTimestamp
	private Date createdAt;

	@Column
	@UpdateTimestamp
	private Date updatedAt;

	@Column
	private int status;

	@OneToMany(mappedBy = "country")
	private List<MSTState> state;

	@OneToMany(mappedBy = "country")
	private List<MSTCity> country;

	@OneToMany(mappedBy = "country")
	private List<MSTPorts> mSTPorts;

	@OneToMany(mappedBy = "countryId")
	private List<VendorCompany> vendorCompany;

	@OneToMany(mappedBy = "countryId")
	private List<CustomerCompany> customerCompany;
	
	@OneToMany(mappedBy = "country")
	private List<ServiceRequest> serviceRequest;
	
	@OneToMany(mappedBy = "countryIds")
	private List<VendorServices> vendorService;
	
	@Column
	private String flag;

	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private String createdBy;
	
	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;


}
