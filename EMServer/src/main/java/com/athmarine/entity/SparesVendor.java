package com.athmarine.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "spares_vendor")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SparesVendor {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private Integer itemOne;

	@Column
	private Integer itemTwo;

	@Column
	private Integer itemThree;

	@Column
	private Integer itemFour;

	@Column
	private Integer itemFive;

	@Column
	private Integer totalSum;

	@ManyToOne
	@JoinColumn(name = "bidder_Id")
	private User bidderId;

//	@OneToOne(fetch = FetchType.LAZY, mappedBy = "sparesVendor")
//	private Bids bids;

	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private String createdBy;

	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;

}
