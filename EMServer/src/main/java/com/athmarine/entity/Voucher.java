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

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "voucher")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Voucher {

	@Column(unique = true, nullable = false)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "company_id")
	private VendorCompany companyId;

	@Column(name = "voucher_number")
	private String voucherNumber;

	@Column(name = "voucher_amount")
	private Integer voucherAmount;
	
	@Column
	private boolean isRegistration;

	@Column
	@CreationTimestamp
	private Date createdAt;

	@Column
	@UpdateTimestamp
	private Date updatedAt;

	@Column
	private int status;

	@Column
	private Boolean redeemed;

}
