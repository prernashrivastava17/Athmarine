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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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
@Table(name = "user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

	@Column(unique = true, nullable = false)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private String address;

	@Column
	@NotNull
	private String name;

	@Column
	@NotNull
	private String email;

	private String type;

	@Column
//	@GeneratedValue(generator = "uuid2")
//	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private String uid;

	@Column
	@NotNull
	private String primaryPhone;

	@Column
	private String currency;

	@Column
	private String phoneCode;

	@Column
	private String dob;

	// @Builder.Default
	@Column(columnDefinition = "varchar(255) default 'UploadDocument/Avtar20665B1626353485030.png'")
	private String imageUrl;

	@Column
	private String secondaryPhone;

	@Column
	private String designation;

	@Column
	private String password;

	@Column
	private String sameASUser;

	@Column
	private boolean userAccess;

	@Column
	@NotNull
	private boolean isPhoneVerified;

	@Column
	private boolean isEmailVerified;

	@Column(columnDefinition = "bit default 0")
	private boolean isEmailVerifiedTerm;

	@Column
	@Builder.Default
	private boolean isOnWhatsapp = false;

	@Column(updatable = false)
	@CreationTimestamp
	private Date createdAt;

	@Column
	@UpdateTimestamp
	private Date updatedAt;

	@Column
	private int status;

	@ManyToOne
	@JoinColumn(name = "companyId")
	private User companyId;

	@ManyToOne
	@JoinColumn(name = "approverId")
	private User approverId;

	@OneToMany(mappedBy = "companyId")
	private List<User> listUser;

	@ManyToMany
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	@JsonIgnoreProperties("user")
	private List<Role> role;

	@OneToOne(mappedBy = "userVendorBidder")
	private BidderApprover bidderApprover;

	@OneToMany(mappedBy = "id")
	private List<BidderApprover> bidderApprovers;

	@OneToMany(mappedBy = "companyId")
	private List<VendorServices> companyService;

	@OneToOne(mappedBy = "user")
	private VendorCompany vendorList;

	@OneToMany(mappedBy = "id")
	private List<Finance> finance;

	@OneToMany(mappedBy = "engineer")
	private List<Feedback> feedback;

	@OneToOne(mappedBy = "userCustomer")
	private CustomerCompany customerCompany;

	@OneToOne(mappedBy = "userCustomerApprover")
	private CustomerApprover customerApprover;

	@OneToMany(mappedBy = "id")
	private List<CustomerApprover> customerApprovers;

	@OneToMany(mappedBy = "requester")
	private List<ServiceRequest> serviceRequest;

//	@ManyToMany(mappedBy = "vendorEngineer", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
//	private List<ServiceRequest> serviceRequestEngineer;

	/*@ManyToMany(mappedBy = "vendorEngineer", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	private List<Bids> bidsEngineers;*/

	@OneToMany(mappedBy = "id")
	private List<VendorApproversPriceRange> aprovers;

	@OneToMany(mappedBy = "id")
	private List<VendorApproversPriceRange> aproverfeedback;

	@OneToMany(mappedBy = "userId")
	private List<Bids> bids;

//	@OneToMany(mappedBy = "bidderId")
//	private List<TravelCostVendor> travelCostVendor;

//	@OneToMany(mappedBy = "bidderId")
//	private List<MiscellaneousVendor> miscellaneousVendor;

//	@OneToMany(mappedBy = "bidderId")
//	private List<SparesVendor> sparesVendor;
//
//	@OneToMany(mappedBy = "bidderId")
//	private List<WorkingVendor> workingVendor;

//	@OneToMany(mappedBy = "approverId")
//	private List<Bids> bidsApprover;

	@Column
	private String biddingLimit;

	@Column
	private String availableOn;

	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private String createdBy;

	@Column(name ="individual_no")
	private String individualNo;

	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;

	@OneToOne(mappedBy = "userCharges")
	private EngineerCharges engineerChages;

	@Column(name = "update_data", length = 10000)
	private String updateData;

}