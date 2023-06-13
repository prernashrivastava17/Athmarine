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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
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
@Table(name = "role")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Role {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	@NotNull
	private String name;

	@Column
	@NotNull
	private String value;

	@Column(updatable = false)
	@CreationTimestamp
	private Date createdAt;

	@Column
	@UpdateTimestamp
	private Date updatedAt;

	@Column
	private int status;

	@ManyToMany(cascade=CascadeType.MERGE,fetch=FetchType.LAZY)
	@JoinTable(name = "role_module", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = {
			@JoinColumn(name = "modules_id") })
	//@JsonIgnoreProperties("role")
	private List<Module> modules;
	
	@ManyToMany(mappedBy = "role")
	@JsonIgnoreProperties("role")
	private List<User> user;
	
	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private String createdBy;
	
	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;
	
	@OneToMany(mappedBy = "role")
	private List<User> listUser;


}
