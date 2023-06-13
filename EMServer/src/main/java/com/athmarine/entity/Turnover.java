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
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "turnover")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Turnover {

	@Column(unique = true, nullable = false)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	@NotNull
	private String turnover;

	@Column
	@NotNull
	private String docname;

	@Column
	private String docUpload;

	@Column
	private String year;
	
	@Column
	private String currency;

	@Column
	@CreationTimestamp
	private Date createdAt;

	@Column
	@UpdateTimestamp
	private Date updatedAt;

	@Column
	private int status;

	@ManyToMany(mappedBy = "turnover", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	private List<Finance> finance;
	
	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private String createdBy;
	
	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;


}