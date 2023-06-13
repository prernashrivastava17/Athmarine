package com.athmarine.request;

import java.util.List;

import com.athmarine.entity.EngineerRanking;
import com.athmarine.response.EngineerDetailsResponseModel;
import com.athmarine.response.EngineerRankingResponseModel;
import com.athmarine.response.FeedbackResponseModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EngineerResponseModel {
	
	 private List<EngineerEquimentsModel> engineerEquimentsModels;
	 
	 private List<EngineerRankingResponseModel> engineerRankings;

//	private Integer id;
//
//	private String name;
//
//	List<EngineerDetailsResponseModel> engineerDetails;
//
//	private FeedbackResponseModel engineerFeedback;
//	
//	private long jobCompleted;

}
