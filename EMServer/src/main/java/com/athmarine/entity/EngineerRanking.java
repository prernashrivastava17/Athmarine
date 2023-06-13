package com.athmarine.entity;

import java.util.Date;
import java.util.List;

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


@Entity
@Table(name = "engineer_ranking")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EngineerRanking {
	
	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(unique = true)
	
	private Integer engineerId;
	
	@Column
	private Integer companyId;
	
	@Column
	private String ranking;
	
	private String color;
	

}
