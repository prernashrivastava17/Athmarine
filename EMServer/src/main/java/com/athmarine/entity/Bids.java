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

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import io.micrometer.core.annotation.Counted;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.PackagePrivate;

@Entity
@Table(name = "bids")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Bids {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column
	private Integer companyId;

	@Column(updatable = false)
	@CreationTimestamp
	private Date createdAt;

	@Column
	@UpdateTimestamp
	private Date updatedAt;

	@Column
	private boolean status;
	
	@Column
	private String bidUID;

	@ManyToOne
	@JoinColumn(name = "bidderId")
	private User userId;

	@ManyToOne
	private ServiceRequest serviceRequest;

	@Column
	private String bidsStatus;

	@Column
	private String additionalCostStatus;
	
	@Column
	private Integer totalProposedAmount;
	
	@Column
	private Integer totalAmountGet;
	
	@Column
	private Integer totalTax;

	@Column
	private Integer totalAddCost;

	@Column
	private Integer totalAddCostGet;

	@Column
	private Integer totalFareValue;

	@Column
	@OneToMany( mappedBy = "bids" )	
	private List<MiscellaneousVendor> miscellaneousVendor;

	/*@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinTable(name = "bidsEngineers", joinColumns = { @JoinColumn(name = "bids_id") }, inverseJoinColumns = {
			@JoinColumn(name = "engineer_id") })
	private List<User> vendorEngineer;*/

	@Column
	@OneToMany( mappedBy = "bids" )
	private List<TravelCostVendor> travelCostVendor;

	@Column
	@OneToMany( mappedBy = "bids" )
	private List<WorkingVendor> workingVendor;
	
	@Column
	private int expectedHrsNeeded;
	
	@Column
	private int noOfEngineer;
	
	@Column
	private int warranty;

	@OneToOne(mappedBy = "bid")
	private PurchaseOrder purchaseOrder;

	@OneToOne(mappedBy = "bid")
	private ServiceRequestInvoice serviceRequestInvoice;

	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private String createdBy;

	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;
}