package com.eot.banking.dao;

import java.util.List;

import com.eot.banking.dto.BillerDTO;
import com.eot.banking.dto.SenelecBillSearchDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.Page;
import com.eot.entity.Biller;
import com.eot.entity.BillerTypes;
import com.eot.entity.Country;

public interface BillerDao extends BaseDao {
   List<BillerTypes> getBillerTypes();
   Page getBillersList(BillerDTO billerDTO,Integer pageNumber, Integer bankId);
   Biller getBillerDeatils(Integer billerId);
   Biller getBillerNameByName(String name,Integer countryId);
   Biller getBillerNameByName(Integer billerId,String billerName,Integer countryId);
   Page getSenegalBills(Integer pageNumber);
   Page searchSenelecBill(SenelecBillSearchDTO senelecBillSearchDTO,Integer pageNumber) throws EOTException;
   List<SenelecBillSearchDTO> searchSenelecBillForPdf(SenelecBillSearchDTO senelecBillSearchDTO) throws EOTException;
   Page getPolicyDetailsByPolicyNo(Integer pageNumber,String policyNo,Integer recordNo)throws EOTException;
   Page getSenelecBillByPolicyNo(Integer pageNumber,String policyNo,Integer recordNo) throws EOTException;
   public List<Object> getAllPaidBills() throws EOTException;
   public double getTotalTransactionAmount() throws EOTException;
   Page getBillersList(int pageNumber, Integer bankId);
   Country loadCountry(Integer countryId);
   BillerTypes loadBillerType(Integer billerTypeId);
}
