package com.eot.banking.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.eot.banking.dto.TxnSummaryDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.Page;

public interface TxnSummaryService {
	public Map<String, List> getTxnSummaryMasterData(String language) throws EOTException;
	public Map<String, Page> getTxnSummaryData(int pageNumber,String requestPage,TxnSummaryDTO txnSummaryDTO,HttpServletRequest request) throws EOTException;
}
