package com.athmarine.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "feedback")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Feedback {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(columnDefinition = "TEXT")
	private String comment;

	@Column
	@NotNull
	private int rating;

	@Column
	private String type;

	@Column
	private Date createdAt;

	@Column
	private Date updatedAt;

	@Column
	private int status;

	@ManyToOne
	@JoinColumn(name = "engineerId")
	private User engineer;

	@ManyToOne
	@JoinColumn(name = "serviceRequestId")
	private ServiceRequest serviceRequest;

	@ManyToOne
	@JoinColumn(name = "feedbackBy")
	private User userfeedback;

	@ManyToOne
	@JoinColumn(name = "vendorCompany")
	private VendorCompany vendorCompany;
	
	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private String createdBy;
	
	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;


}
