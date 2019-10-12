/* Copyright EOT 2018. All rights reserved.
*
* This software is the confidential and proprietary information
* of EOT. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EOT.
*
* Id: CommissionSplitsController.java
*
* Date Author Changes
* May 28, 2019 Sudhanshu Created
*/
package com.eot.banking.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;

import com.eot.banking.dto.CommisionSplitsDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.service.CommissionService;
import com.eot.banking.service.TransactionService;
import com.eot.banking.utils.JSONAdaptor;

/**
 * The Class CommissionSplitsController.
 */
@Controller
public class CommissionSplitsController extends PageViewController {
	
	@Autowired
	CommissionService commissionService;
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private LocaleResolver localeResolver;

	/**
	 * Commission split form.
	 *
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @param commisionSplitsDTO
	 *            the commision splits DTO
	 * @param response
	 *            the response
	 * @return the string
	 */
	@RequestMapping("/commissionSplitForm.htm")
	public String commissionSplitForm(HttpServletRequest request, Map<String, Object> model,
		CommisionSplitsDTO commisionSplitsDTO, HttpServletResponse response) {
		try {
			List<CommisionSplitsDTO> commisionSplits = commissionService.loadCommissionSplits();
			JSONAdaptor adaptor =  new JSONAdaptor();
			String commissionJson = adaptor.toJSON(commisionSplits);
			commisionSplitsDTO.setCommissionSplitList(commisionSplits);
			//commisionSplitsDTO.setCommisionPct(0f);
			//model.put("transTypeList", transactionService.getTransactionType(localeResolver.resolveLocale(request).toString().substring(0, 2)));
			model.put("commisionSplitsDTO", commisionSplitsDTO);
			model.put("commissionJson", commissionJson);
		} catch (EOTException e) {
			e.printStackTrace();
		}
		return "commisionSplits";
	}
	
	@RequestMapping("/setCommissionSplits.htm")
	public String saveCommissionSplits(@Valid CommisionSplitsDTO commisionSplitsDTO, HttpServletRequest request, Map<String, Object> model,
			HttpServletResponse response) {
		
		String commissionSplitJson =  "{ \"commissionSplitList\":"+ commisionSplitsDTO.getCommdata()+"}";
		System.out.println(commissionSplitJson);
		JSONAdaptor adaptor =  new JSONAdaptor();
		commisionSplitsDTO = (CommisionSplitsDTO)adaptor.fromJSON(commissionSplitJson, CommisionSplitsDTO.class);
		try {
			List<CommisionSplitsDTO> oldCommisionSplits = commissionService.loadCommissionSplits();
			if(CollectionUtils.isNotEmpty(oldCommisionSplits)){
				commissionService.deleteOldCommission();
				commissionService.createCommissionSplits(commisionSplitsDTO);
				model.put("message", "COMMISION_SPLIT_UPDATED_SUCCESSFULLY");
			}else{
				commissionService.createCommissionSplits(commisionSplitsDTO);
				model.put("message", "COMMISION_SPLIT_CREATED_SUCCESSFULLY");
			}
			List<CommisionSplitsDTO> commisionSplits = commissionService.loadCommissionSplits();
			commisionSplitsDTO.setCommissionSplitList(commisionSplits);
			String commissionJson = adaptor.toJSON(commisionSplits);
			model.put("commissionJson", commissionJson);
		} catch (EOTException e) {
			e.printStackTrace();
		}
		model.put("commisionSplitsDTO", commisionSplitsDTO);
		/*try {
			model.put("transTypeList", transactionService.getTransactionType(localeResolver.resolveLocale(request).toString().substring(0, 2)));
		} catch (EOTException e) {
			e.printStackTrace();
		}*/
		return "commisionSplits";
	}

}
