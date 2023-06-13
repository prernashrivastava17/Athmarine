package com.athmarine.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "finance")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Finance {

	@Column
	@Id
	private Integer id;

	@Column
	@CreationTimestamp
	private Date createdAt;

	@Column
	@UpdateTimestamp
	private Date updatedAt;

	@Column
	private int status;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "id")
	@MapsId
	private User user;

	@Column
	private String currency;

	@Column
	private String type;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "finance_turnover", joinColumns = { @JoinColumn(name = "finance_id") }, inverseJoinColumns = {
			@JoinColumn(name = "turnover_id") })
	private List<Turnover> turnover;
	
	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private String createdBy;
	
	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;


}
