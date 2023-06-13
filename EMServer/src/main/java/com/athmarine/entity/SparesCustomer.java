package com.athmarine.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "spares_customer")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SparesCustomer {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private Integer itemOne;

	@Column
	private String itemOneComment;

	@Column
	private Integer itemTwo;

	@Column
	private String itemTwoComment;

	@Column
	private Integer itemThree;

	@Column
	private String itemThreeComment;

	@Column
	private Integer itemFour;

	@Column
	private String itemFourComment;

	@Column
	private Integer itemFive;

	@Column
	private String itemFiveComment;

	@Column
	private Integer totalSum;

//	@OneToOne(fetch = FetchType.LAZY, mappedBy = "sparesCustomer")
//	private Bids bids;

	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private String createdBy;

	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;

}
