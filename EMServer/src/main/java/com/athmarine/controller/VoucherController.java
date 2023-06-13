package com.athmarine.controller;

import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(RestMappingConstants.VoucherInterfaceUri.VOUCHER_BASE_URI)
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class VoucherController {

    @Autowired
    VoucherService voucherService;

    @GetMapping(RestMappingConstants.VoucherInterfaceUri.GET_VOUCHERS_BY_COMPANY_ID)
    public ResponseEntity<?> getVouchersByCompanyId(@RequestParam Integer companyId){

        BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(
                voucherService.getVouchersByCompanyId(companyId));

        return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
    }


    @PutMapping(RestMappingConstants.VoucherInterfaceUri.REDEEM_VOUCHER)
    public ResponseEntity<?> redeemVoucher(@RequestParam Integer voucherId, Integer invoiceId){

        BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(
                voucherService.redeemVoucher(voucherId,invoiceId));

        return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
    }

}
