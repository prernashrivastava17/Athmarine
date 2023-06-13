package com.athmarine.request;

import java.util.List;

import com.athmarine.entity.BidderApprover;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorApproverModel {

	private UserModel userModel;

	private BidderApprover bidderApproverModel;

	private List<UserListResponseModel> bidderModel;

}
