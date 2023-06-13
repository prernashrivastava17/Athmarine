package com.athmarine.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer_company")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerCompany {

	@Id
	private Integer id;

	@Column
	private String logo;

	@Column
	@NotNull
	private String address;

	@Column
	private String city;

	@Column
	private String pincode;

	@Column
	private String faxno;

	@Column(name = "is_admin_registered")
	@NotNull
	private boolean isAdminRegistered;

	@Column(name = "is_registered")
	private boolean isRegistered;

	@Column
	private String registrationNo;

	@Column
	private Date yearOfReg;
	
	@Column
	private String city_of_registration;

	@Column
	private String country_of_registration;
	
	@Column
	private String companyUid;

	@Column(updatable = false)
	@CreationTimestamp
	private Date createdAt;

	@Column
	@UpdateTimestamp
	private Date updatedAt;

	@Column
	private Integer status;
	
	@Column
	private String referralCode;

	@Column(columnDefinition = "integer default 0")
	private Integer referralUseCount;

	@Column
	private String referralCodeUsed;

	/*@ManyToOne
	@JoinColumn(name = "stateId")
	private MSTCountry mstCountry;*/
	
	@ManyToOne
	@JoinColumn(name = "stateId")
	private MSTState stateId;
	

	private String cityCustomer;
	
	@ManyToOne
	@JoinColumn(name = "countryId")
	private MSTCountry countryId;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "id")
	@MapsId
	private User userCustomer;
	
//	@OneToMany(mappedBy="requestorComapny")
//	private List<ServiceRequest> serviceRequest;
//	

	@Column(name = "registrationStatus", columnDefinition = "ENUM('Address','Admin', 'Admin_verification_Email', 'Admin_verification_Phone', 'Company_Head', 'Business_Information', 'Bidder', 'Approver', 'Engineer', 'Service', 'Finance', 'Requester','Managing_Head','Completed')")
	@Enumerated(EnumType.STRING)
	private RegistrationStatus registrationStatus;
	
	@Column
	private boolean isRegisteredSuccessfully;
	
//	@OneToMany(mappedBy = "customerCompany")
//	private List<Vessel> vessel;
	
	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private String createdBy;
	
	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;


}
