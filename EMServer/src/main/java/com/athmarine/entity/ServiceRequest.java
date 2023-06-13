package com.athmarine.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import com.athmarine.resources.ServiceTimeCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "service_request")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServiceRequest {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private String requestUID;

	@Column
	private String requestStatus;

	@ManyToOne
	@JoinColumn(name = "requester")
	private User requester;

	@ManyToOne
	private Vessel vessel;

	@ManyToOne
	private MSTCountry country;

	@ManyToOne
	private MSTPorts port;

	@Column
	private String locationType;

	@Column
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date serviceRequestOn;

	@Column
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date ETA;

	@Column
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date ETD;

	@Column
	private String preferredCurrency;

	@ManyToOne
	private MSTEquipmentCategory equipmentCategory;

	@Column
	private String equipmentName;

	@Column
	private String manufacturer;

	@Column
	private String model;

	@Column
	private String issueType;

	@Column
	private String description;

	@Column
	private int quotationTime;

	@Column
	private ServiceTimeCategory serviceTimeCategory;

	@OneToMany(mappedBy = "serviceRequest")
	private List<ServiceRequestDocuments> serviceRequestDocuments;

	@OneToMany(mappedBy = "serviceRequest")
	private List<PurchaseOrder> purchaseOrder;

	@OneToOne(mappedBy = "serviceRequest")
	private ServiceRequestInvoice serviceRequestInvoice;

	/*@ElementCollection
	private List<String> documents;*/

	/*@Column(name = "isaccepted")
	@NotNull
	private boolean isAccepted;*/

	@Column(updatable = false)
	@CreationTimestamp
	private Date createdAt;

	@Column
	@UpdateTimestamp
	private Date updatedAt;

	/*@Column
	private int status;*/

	/*@OneToMany(mappedBy = "serviceRequested")
	private List<ServiceRequestEquipments> serviceRequestEquipments;*/

	/*@ManyToOne
	private MSTState states;

	@ManyToOne
	private VendorCompany vendorCompany;

	@OneToMany(mappedBy = "serviceRequest")
	private List<Bids> bids;

	@ElementCollection
	private List<String> email;

	@Column
	private String service_required_timing;*/


	/*@Column
	@Value("${some.key:0}")
	private int vendorInterested;

	@OneToMany(mappedBy = "serviceRequest")
	private List<Feedback> feedback;*/

	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private String createdBy;

	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;

}
