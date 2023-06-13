package com.athmarine.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "invoice")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Invoice {

	@Column(unique = true, nullable = false)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private String invoiceNumber;

	@Column(name = "amount")
	private Double amount;

	@Column
	private Integer totalEngineer;

	@Column(name = "currency")
	private String currency;

	@Nationalized
	@Column(name = "currency_symbol", columnDefinition = "TEXT")
	private String currencySymbol;

	@Column(columnDefinition = "bit default 0")
	private boolean isPaymentStatus;

	@Column(updatable = false)
	@CreationTimestamp
	private Date createdAt;

	@Column
	@UpdateTimestamp
	private Date updatedAt;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

}
