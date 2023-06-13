package com.athmarine.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
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
@Table(name = "vendor_company")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VendorCompany {

	@Id
	private Integer id;

	@Column
	private String logo;

	@Column
	private String address;

	@Column
	private String city_of_registration;

	@Column
	private String country_of_registration;

	@Column
	private String email;

	@Column
	private String primaryPhone;

	@Column
	private String pincode;

	@Column
	private String faxno;

	@Column
	private boolean isSameAsAdmin;

	@Column
	private boolean isRegistered;

	@Column
	private String isEstablishedLastFiveYear;

	@Column
	private String registrationNo;

	@Column
	private String yesOrNo;

	@Column
	private Date yearOfReg;

	@Column
	private Date companyStablished;

	@Column(updatable = false)
	@CreationTimestamp
	private Date createdAt;

	@Column
	@UpdateTimestamp
	private Date updatedAt;

	@Column
	private int status;

	@ManyToOne
	@JoinColumn(name = "stateId")
	private MSTState stateId;

	private String city;

	@ManyToOne
	@JoinColumn(name = "countryId")
	private MSTCountry countryId;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "id")
	@MapsId
	private User user;

	/*@OneToMany(mappedBy = "vendorCompany")
	private List<ServiceRequest> serviceRequest;*/
	
	@OneToMany(mappedBy = "companyId")
	private List<Voucher> voucher;

	@OneToMany(mappedBy = "vendorCompany")
	private List<Feedback> feedbacks;

	@Column(name = "registrationStatus", columnDefinition = "ENUM('Address','Admin', 'Admin_verification_Email', 'Admin_verification_Phone', 'Company_Head', 'Business_Information', 'Bidder', 'Approver', 'Engineer', 'Service', 'Finance', 'Requester','Managing_Head','Completed','Payment')")
	@Enumerated(EnumType.STRING)
	private RegistrationStatus registrationStatus;

	@Column
	private boolean isRegisteredSuccessfully;

	@Column
	private String referralCode;
	
	@Column
	private String CompanyUid;

	@Column(columnDefinition = "integer default 0")
	private Integer referralUseCount;

	@Column
	private String referralCodeUsed;

	@Column(name = "termAndConditionStatus", columnDefinition = "ENUM('Rejected','Accepted') default 'Rejected'")
	@Enumerated(EnumType.STRING)
	private TermAndConditionStatus termAndConditionStatus;

	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private String createdBy;

	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;

}