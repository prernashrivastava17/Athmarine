package com.athmarine.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "phone_subscription")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneSubscription {
	
	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column
	private int status;
	
	@Column
	@NotNull
	private String phoneNum;
	
	@Column(updatable = false)
	@CreationTimestamp
	private Date createdAt;

	@Column
	@UpdateTimestamp
	private Date updatedAt;


}
