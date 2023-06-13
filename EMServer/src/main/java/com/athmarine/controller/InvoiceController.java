package com.athmarine.controller;

import com.athmarine.request.InvoiceDetailsModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(RestMappingConstants.InvoiceInterfaceUri.INVOICE_BASE_URI)
public class InvoiceController {

    @Autowired
    InvoiceService invoiceService;

    @PostMapping(RestMappingConstants.InvoiceInterfaceUri.INVOICE_SAVE_URI)
    public ResponseEntity<?> saveInvoice(@RequestBody InvoiceDetailsModel invoiceDetailsModel){

        BaseApiResponse baseApiResponse = ResponseBuilder
                .getSuccessResponse(invoiceService.saveToInvoiceEntity(invoiceDetailsModel));
        return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
    }

    @GetMapping(RestMappingConstants.InvoiceInterfaceUri.GET_INVOICE)
    public ResponseEntity<?> getInvoiceById(@RequestParam Integer invoiceId){

        BaseApiResponse baseApiResponse = ResponseBuilder
                .getSuccessResponse(invoiceService.getInvoice(invoiceId));
        return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

    }

}
