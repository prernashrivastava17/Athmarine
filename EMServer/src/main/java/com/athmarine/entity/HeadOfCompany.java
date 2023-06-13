package com.athmarine.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "Head_Of_Company")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class HeadOfCompany {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private Integer headId;

	@Column
	private String status;

	@Column
	private Integer statusId;

	@CreationTimestamp
	@CreatedDate
	@Column
	private Date createdAt;
	
	@Column
	private Date serviceOn;
	
	@Column
	private Integer bidderId;
	
	@Column
	private Date poReceivedAt;

	@Column
	private String declineReason;
}
