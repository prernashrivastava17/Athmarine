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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "module")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Module {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private String parentPermission;

	@Column
	private String permission;

	@Column
	@NotNull(message = "addRight Cannot Be Null")
	private boolean addRight;

	@Column
	@NotNull
	private boolean viewRight;

	@Column
	@NotNull
	private boolean modifyRight;

	@Column(name = "created_at", updatable = false)
	@CreationTimestamp
	private Date createdAt;

	@Column(name = "updated_at")
	@UpdateTimestamp
	private Date updatedAt;

	@Column
	private int status;

	@ManyToMany(mappedBy = "modules",cascade=CascadeType.MERGE,fetch=FetchType.EAGER)
	@JsonIgnoreProperties("modules")
	private List<Role> role;
 
	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private String createdBy;
	
	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;



}
