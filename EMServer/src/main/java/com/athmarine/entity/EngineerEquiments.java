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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "engineer_equiments")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EngineerEquiments {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private String certified;

	@Column(updatable = false)
	@CreationTimestamp
	private Date createdAt;

	@Column
	@UpdateTimestamp
	private Date updatedAt;

	@Column
	private String yesOrNo;

	@Column
	private Integer status;

	@OneToMany(mappedBy = "enggequip")
	private List<EngineerCertificates> engineerCertificates;

	@OneToMany(mappedBy = "enggequip")
	private List<Experience> experience;

	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinTable(name = "ENGINEER_EQUIPMENTID", joinColumns = @JoinColumn(name = "engineer_id"), inverseJoinColumns = @JoinColumn(name = "equipment_Id"))
	private List<Equipment> equimentId;

	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinTable(name = "ENGINEER_CATEGORYID", joinColumns = @JoinColumn(name = "engineer_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
	private List<MSTEquipmentCategory> categoryId;

	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinTable(name = "ENGINEER_MAKEID", joinColumns = @JoinColumn(name = "engineer_id"), inverseJoinColumns = @JoinColumn(name = "make_id"))
	private List<Make> makeId;

	@ManyToOne
	@JoinColumn(name = "engineerId")
	private User engineerId;
	
	private String engineersId;
	
	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private String createdBy;
	
	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;


}