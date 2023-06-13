package com.athmarine.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "extra_cost_customer")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExtraCostCustomer {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private Integer miscellaneousPortCharges;

	@Column
	private String miscellaneousPortComment;

	@Column
	private Integer miscellaneousCovidTestCharge;

	@Column
	private String miscellaneousCovidComment;

	@Column
	private Integer miscellaneousShipyardCharges;

	@Column
	private String miscellaneousShipComment;

	@Column
	private Integer miscellaneousShipyardSurcharge;

	@Column
	private String miscellaneousShipyardComment;

	@Column
	private Integer miscellaneousOtherCharge;

	@Column
	private String miscellaneousOtherComment;

	@Column
	private Integer miscellaneousTotalSum;

	@Column
	private Integer workingRate;

	@Column
	private String workingRateComment;

	@Column
	private Integer estimatedWorkingHours;

	@Column
	private String estimateWorkingHoursComment;

	@Column
	private Integer workingRateOT1;

	@Column
	private String workingRateOTComment;

	@Column
	private Integer estimatedWorkingHours1;

	@Column
	private String estimateworkingHours1Comment;

	@Column
	private Integer workingRateOT2;

	@Column
	private String workingRateOT2Comment;

	@Column
	private Integer estimatedWorkingHours2;

	@Column
	private String estimateWorkingHours2Comment;

	@Column
	private Integer holidaysWorkingRate;

	@Column
	private String holidayWorkingRateComment;

	@Column
	private Integer holidaysEstimatedWorkingHours;

	@Column
	private String holidayEstimatedWHourseComment;

	@Column
	private Integer holidaysWorkingRateOT1;

	@Column
	private String holidayWorkingRateOT1Comment;

	@Column
	private Integer holidaysEstimatedWorkingHours1;

	@Column
	private String holidaysEstimatedWHours1Comment;

	@Column
	private Integer holidaysWorkingRateOT2;

	@Column
	private String holidaysWorkRateOT2Comment;

	@Column
	private Integer holidaysEstimatedWorkingHours2;

	@Column
	private String holidaysEstWorkHours2Comment;

	@Column
	private Integer workingTotalSum;

	@Column
	private Integer travelLumpSumCost;

	@Column
	private String travelLumpSumCostComment;

	@Column
	private Integer travelAirFare;

	@Column
	private String travelAirFareComment;

	@Column
	private Integer travelTransportation;

	@Column
	private String travelTransportationComment;

	@Column
	private Integer travelEngineerRatePerHour;

	@Column
	private String travelEngRPerHourComment;

	@Column
	private Integer totalTravelTime;

	@Column
	private Integer travelTimeCost;

	@Column
	private String travelTimeCostComment;

	@Column
	private Integer travelTotalSum;

	@Column
	private Integer sparesItemOne;

	@Column
	private Integer sparesItemTwo;

	@Column
	private Integer sparesItemThree;

	@Column
	private Integer sparesItemFour;

	@Column
	private Integer sparesItemFive;

	@Column
	private Integer sparesTotalSum;

	@Column
	private String sparesComment;

	@Column
	private Integer bidId;

	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private String createdBy;

	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;

	@Column
	private String Status;

}
