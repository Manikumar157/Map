package com.eot.banking.service;


import com.eot.banking.dto.LimitUpdateReqDTO;
import com.eot.banking.dto.LimitUpdateRespDTO;


public interface BankRequstService {
	
	LimitUpdateRespDTO processLimitDepostiTransaction(LimitUpdateReqDTO limitUpdateReqDTO);

	LimitUpdateRespDTO fetchDetails(LimitUpdateReqDTO limitUpdateReqDTO);

}
