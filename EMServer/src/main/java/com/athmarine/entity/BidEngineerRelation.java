package com.athmarine.entity;

import java.util.Date;
import java.util.List;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "engineer_bid_relation")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BidEngineerRelation {
	
	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column
	private Integer bidId;
	
	@Column
	private Integer engineerId;

	@Column
	private boolean status;
	
	@Column
	private Date serviceOn;
	
	@Column
	private Date serviceEndOn;
	
	@Column
	private String engineerStatus;
	
	@Column
	private String serviceReport;
	
	@ManyToOne
	private ServiceRequest serviceRequest;
}
