<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="authz"
	uri="http://www.springframework.org/security/tags"%>
<html>
<head>

<%-- <title><spring:message code="LABEL_TITLE" text="EOT MOBILE" /></title> --%>

<%-- <link type="text/css" rel="stylesheet"
    href="<%=request.getContextPath()%>/css/calendar-system.css" />
<style>
	p.singleCol {clear:both;float:left;display:block;width:100%;margin:0px; margin-bottom:10px;}
	span.bigger {float:left;display:block;width:37%;margin:0px;padding:0px;border:0px;}
	span.data {float:left;display:block;width:62%;margin:0px;padding:0px;border:0px;}
</style> --%>
<!-- Loading Calendar JavaScript files -->
<script type="text/javascript"
    src="<%=request.getContextPath()%>/js/zapatec.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/bankManagement.js"></script>


<!-- Loading language definition file -->
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/<spring:message code="CALENDAR_LANG" />.js"></script>

<script type="text/javascript">
	var modelIdArray = new Array(), fdateArray = new Array(), tdateArray = new Array();
	var n = 0;
	
	var modelId=0

	$(document).ready( function () {
		  
		setAgrementsByTableData();
		setArraysByTableData();
		});
	
	var Alertmsg = {

		"bankName" : "<spring:message code="NotEmpty.bankDTO.bankName" text="Please Enter Valid Bank Name"/>",
		"bankNameLength" : "<spring:message code="LABEL_BANK_LENGTH" text="BankName Should not exceed More than 100 characters"/>",
		"bankNameSpace" : "<spring:message code="VALID_BANK_SPACE" text="Please Remove Unwanted Spaces in Bank Name"/>",
		"country" : "<spring:message code="NotNull.bankDTO.countryId" text="Please Select Country From  "/>",
		"currency" : "<spring:message code="NotNull.bankDTO.currencyId" text="Please Select Currency From List"/>",
		"timeZone" : "<spring:message code="NotNull.bankDTO.timezoneId" text="Please Select TimeZone From List"/>",
		"dateValid" : "<spring:message code="EMPTY_AGREEMENT_DATES" text="Please Select Valid Date's From Calender"/>",
		"dateValidFrom" : "<spring:message code="NotNull.bankDTO.agreementFromField" text="Please Select Valid From Date From Calender"/>",
		"dateValidTo" : "<spring:message code="NotNull.bankDTO.agreementToField" text="Please Select Valid To Date From Calender"/>",
		"dateSame" : "<spring:message code="SAME_AGREEMENT_DATES" text="Agreement To and Agreement From dates should not b same"/>",
		"todategreater" : "<spring:message code="SAME_BANK_AGREEMENT_TOFROMDATES" text="Agreement To date must be greater than from date" />",
		"dateValid1" : "<spring:message code="VALID_AGREEMENT_TO_DATE" text="Agreement To date should be greater than current date"/>",
		"empty" : "<spring:message code="EMPTY_RECORD" text="Please insert atleast one record"/>",
		"branchValid" : "<spring:message code="VALID_BRANCH_FILE" text="Please Select BranchFile"/>",
		"valid" : "<spring:message code="VALID_VALUES" text="Please Fill All The Values"/>",
		"priority" : "<spring:message code="VALID_PRIORITY" text="Enter Valid Priority Value Only In Digits"/>",
		"uniqueHouse" : "<spring:message code="UNIQUE_HOUSE_POOL" text="Please Select Other PoolName, Which Doesn't Exist In Table"/>",
		"uniquePriority" : "<spring:message code="UNIQUE_PRIORITY" text="Priority already exists "/>",
		"bankNameDigits" : "<spring:message code="LABEL_BANK_DIGITS" text="Please enter Bank Name with out any special characters"/>",
		"bankCode" : "<spring:message code="NotEmpty.bankDTO.bankCode" text="Please Enter  Bank Code"/>",
		"bankShortName" : "<spring:message code="NotEmpty.bankDTO.bankShortName" text="Please Enter  Bank Short Name"/>",
		"bankCodeCharacters" : "<spring:message code="LABEL_BANKCODE_DIGITS" text="Please Enter Bank Code with out  Special characters "/>",
		"bankShortNameDigits" : "<spring:message code="LABEL_BANKSHORTNAME_DIGITS" text="Please Enter  Bank Short Name with out any special characters"/>",
		"fromdategttodate" : "<spring:message code="LABEL_FROMGTTODATE" text="Agreement periods cannot overlap"/>",
		"shortNameLength" : "<spring:message code="LABEL_BANK_SHORTNAME_LENGTH" text="Please Enter Bank Short Name Maximum of 5 Characters"/>",
		"bankCodeLength" : "<spring:message code="LABEL_BANK_CODE_LENGTH" text="Please Enter Bank Code length should be  5 "/>",
		"clearinghouse" : "<spring:message code="LABEL_CLEARING_HOUSE" text="Please select clearing house "/>",
		"emptyPriority" : "<spring:message code="NotNull.bankDTO.clearingHousePriorities" text="Please Enter priority value "/>",
		"priorityLength" : "<spring:message code="LABEL_PRIORITY_LENGTH" text="Please Enter priority value maximum of one digit only"/>",
		"currentDateValid" : "<spring:message code="LABEL_CURRENTDATE_VALID" text="Please enter Agreement from Date not less than todays Date"/>",
		"govtTax" : "<spring:message code="NotNull.bankDTO.govtTax" text="Please enter Government tax"/>",
		"eotCharge" : "<spring:message code="NotNull.bankDTO.eotCharge" text="Please enter GIM service charge"/>",
		"bankCharge" : "<spring:message code="NotNull.bankDTO.bankCharge" text="Please enter Bank service charge"/>",
		"gimChargeDigit" : "<spring:message code="LABEL_GIM_SERICE_CHARGE_DIGITS" text="Please enter Gim service charge  in digits only"/>",
		"govtTaxDigits" : "<spring:message code="LABEL_GOVT_TAX_DIGITS" text="Please enter Govt Tax in digits only"/>",
		"bankChargeDigits" : "<spring:message code="LABEL_BANK_SERICE_CHARGE_DIGITS" text="Please enter Bank service charge in digits only"/>",
		"swiftCodePattern" : "<spring:message code="LABEL_SWIFT_CODE_PATTERN" text="Please enter swift code only in alpha numeric"/>",
		"swiftCodeLength" : "<spring:message code="LABEL_SWIFT_CODE_LENGTH" text="BIC/Swift Code length should be 8 characters"/>",
		"swiftBranchLength" : "<spring:message code="LABEL_SWIFT_BRANCH_LENGTH" text="Swift Branch length should be 3 characters"/>",
		"descLength" : "<spring:message code="LABEL.DESCRIPTION.LENGTH" text="Description should not exceed more than 180 characters"/>",
		"descSpecial" : "<spring:message code="DO_NOT_ENTER_SPECIAL_CHARACTER_IN_DESCRIPTION" text="Please remove the special characters in description field"/>",
		"bankCodeSpace" : "<spring:message code="VALID_BANK_CODE_SPACE" text="Please Remove White Spaces in Bank Code"/>",
		"stampFee" : "<spring:message code="VALID_STAMP_FEE" text="Please enter a valid Stamp Fee"/>",
		"stampValue" : "<spring:message code="VALID_STAMP_FEE_NOT_GREATER" text="Please enter  Stamp Fee not greater than 100"/>",
		"stampEmpty" : "<spring:message code="VALID_EMPTY_STAMP_FEE" text="Please  enter a Stamp Fee"/>",
		"fromDateFormat" : "<spring:message code="VALID_AGGREMENT_FROM_DATE_FORMAT" text="The aggrement from date format must be : dd/mm/yyyy"/>",
		"toDateFromat" : "<spring:message code="VALID_AGGREMENT_TO_DATE_FORMAT" text="The aggrement to date format must be : dd/mm/yyyy"/>",
		"validFromDay" : "<spring:message code="VALID_DAY" text="Please enter valid from date"/>",
		"validToDay" : "<spring:message code="VALID_TO_DAY" text="Please  enter  a valid to date"/>",
		"validFromMonth" : "<spring:message code="VALID_FROM_MONTH" text="Please  enter  a valid  month in from date"/>",
		"validToMonth" : "<spring:message code="VALID_TO_MONTH" text="Please  enter  a valid  month in to date"/>",
		"validfDay" : "<spring:message code="VALID_FROM_DAY" text="Please  enter  a valid  days in from date"/>",
		"validTodays" : "<spring:message code="VALID_TO_DAYS" text="Please  enter  a valid  days in to date"/>",
		"validFMonth" : "<spring:message code="VALID_FROM_MONTH" text="Please  enter  a valid  month in from date"/>",
		"validToMonth" : "<spring:message code="VALID_TO_MONTH" text="Please  enter  a valid  month in to date"/>",
		"zeroPriority" : "<spring:message code="CH_ZERO_PRIORITY" text="Priority must not be zero"/>",
		"applicationName" : "<spring:message code="VALID_APPLICATION_NAME" text="Please Enter Valid Application Commercial Name"/>",

	};

	function validateFields() {
		
		var descPattern = '^\[a-zA-ZÀ-ÿ-\' 0-9 ]*$';
		var bankName = document.getElementById("bankName").value;
		var swiftCode = document.getElementById("swiftCode").value;
		var swiftBranch = document.getElementById("swiftBranch").value;
		//var applicationName = document.getElementById("applicationName").value;
		//var channelCost=document.getElementById("channelCost").value;
		
		var country = document.getElementById("countryId").value;
		//var currency = document.getElementById("currencyId").value;
		var timezone = document.getElementById("timezoneId").value;
		var priority = document.getElementById("priority").value;
		var branch = document.getElementById("branch").value;
		var fileFormat = document.bankManageMentForm.branchFile.value
				.substring(document.bankManageMentForm.branchFile.value
						.lastIndexOf("."));

		var bankShortName = document.getElementById("bankShortName").value;
		var bankCode = document.getElementById("bankCode").value;
		var govtTax = document.getElementById("govtTax").value;
		var eotCharge = document.getElementById("eotCharge").value;
		var bankCharge = document.getElementById("bankCharge").value;
		var gimbankScharge = parseFloat(eotCharge) + parseFloat(bankCharge);
		var description = document.getElementById("description").value;
		var stampFee = document.getElementById("stampFee").value;
		
		//var eotCardBankCode = document.getElementById("eotCardBankCode").value;
		//var terminalId = document.getElementById("terminalId").value;
		//var outletNumber = document.getElementById("outletNumber").value;
		
		//var todate=document.bankManageMentForm.agreementToField.value;
		var today = new Date();
		var pattern = '^\[a-zA-ZÀ-ÿ0-9-_|/\' ]*$';
		var codePattern = '^\[0-9A-Za-z]*$';
		var spacepattern = '^\[0-9A-Za-z /\' ]*$';
		var swiftCodePattern = '^\[a-zA-Z0-9]*$';
		var bankShortNamePattern = '^\[a-zA-ZÀ-ÿ0-9\'.-_|]*$';
		var numericPattern=  /^\d{0,3}(\.\d{1,2})?$/gm;
		
		var priority = document.getElementById("priority");
		
		
		if (bankName == "") {
			alert(Alertmsg.bankName);
			return false;
		} else if (bankName.charAt(0) == " "
				|| bankName.charAt(bankName.length - 1) == " ") {
			alert(Alertmsg.bankNameSpace);
			return false;
		} else if (bankName.search(pattern) == -1) {
			alert(Alertmsg.bankNameDigits);
			return false;
		} else if (bankName.length > 100) {
			alert(Alertmsg.bankNameLength);
			return false;
		} else if (swiftCode.length != 8) {
			alert(Alertmsg.swiftCodeLength);
			return false;
		} else if (swiftBranch.length != 0 && swiftBranch.length != 3) {
			alert(Alertmsg.swiftBranchLength);
			return false;
		} else if (swiftCode.search(swiftCodePattern) == -1) {
			alert(Alertmsg.swiftCodePattern);
			return false;
		} else if (swiftBranch.search(swiftCodePattern) == -1) {
			alert(Alertmsg.swiftCodePattern);
			return false;
		} else if (bankShortName == "") {
			alert(Alertmsg.bankShortName);
			return false;
		} else if (bankShortName.charAt(0).search(codePattern) == -1
				|| (bankShortName.charAt(bankShortName.length - 1))
						.search(codePattern) == -1) {
			alert(Alertmsg.bankShortNameDigits);
			return false;
		} else if (bankShortName.search(bankShortNamePattern) == -1) {
			alert(Alertmsg.bankShortNameDigits);
			return false;
		} else if (bankShortName.length > 5) {
			alert(Alertmsg.shortNameLength);
			return false;
		} else if (bankCode == "") {
			alert(Alertmsg.bankCode);
			return false;
		} else if (bankCode.charAt(0) == " "
				|| bankCode.charAt(bankCode.length - 1) == " ") {
			alert(Alertmsg.bankCodeSpace);
			return false;
		} else if (bankCode.length<5||bankCode.length>5) {
			alert(Alertmsg.bankCodeLength);
			return false;
		} else if (!checkSpace(bankCode)) {
			alert(Alertmsg.bankCodeSpace);
			return false;
		} else if (bankCode == "00000") {
			alert("<spring:message code="INVALID_BANK_CODE" text="Please enter valid Bank Code"/>");
			return false;
		}/*  else if (eotCardBankCode.search(swiftCodePattern) == -1
				|| eotCardBankCode == "") {
			alert("<spring:message code="INVALID_GIM_CARD_BANK_CODE" text="Please enter valid m-GURUSH Card Bank Code"/>");
			return false;
		} else if (!checkAllZero(eotCardBankCode)) {
			alert("<spring:message code="INVALID_GIM_CARD_BANK_CODE" text="Please enter valid m-GURUSH Card Bank Code"/>");
			return false;
		} else if (terminalId.search(swiftCodePattern) == -1
				|| terminalId == "") {
			alert("<spring:message code="INVALID_TERMINAL_ID" text="Please enter valid Terminal ID"/>");
			return false;
		} else if (!checkAllZero(terminalId)) {
			alert("<spring:message code="INVALID_TERMINAL_ID" text="Please enter valid Terminal ID"/>");
			return false;
		} else if (outletNumber.search(swiftCodePattern) == -1
				|| outletNumber == "") {
			alert("<spring:message code="INVALID_OUTLET_NUMBER" text="Please enter valid Outlet Number"/>");
			return false;
		} else if (!checkAllZero(outletNumber)) {
			alert("<spring:message code="INVALID_OUTLET_NUMBER" text="Please enter valid Outlet Number"/>");
			return false;
		} */ else if (bankCode.search(codePattern) == -1) {
			alert(Alertmsg.bankCodeCharacters);
			return false;
		} else if (branch != "") {
			if ((fileFormat.toLowerCase() != ".csv")) {
				alert("<spring:message code="VALID_CSV_FILE" text="Please Select .csv file only"/>");
				return false;
			}
		}
		// change
		
		if (!document.bankManageMentForm.status[0].checked && !document.bankManageMentForm.status[1].checked) {
	alert('Please select Status');
	return false;
	}
		
		// change over

		if (country == "") {
			alert(Alertmsg.country);
			return false;
		} /* else if (currency == "") {
			alert(Alertmsg.currency);
			return false;
		}  */else if (timezone == "") {
			alert(Alertmsg.timeZone);
			return false;
		} else if (govtTax == "") {
			alert(Alertmsg.govtTax);
			return false;
		} else if (govtTax.charAt(0) == " "
				|| govtTax.charAt(govtTax.length - 1) == " ") {
			alert('<spring:message code=" LABEL_GOVT_TAX_SPACE" text="Please remove white space in  tax %"/>');
			return false;
		} else if (eotCharge == "") {
			alert(Alertmsg.eotCharge);
			return false;
		} else if (eotCharge.charAt(0) == " "
				|| eotCharge.charAt(eotCharge.length - 1) == " ") {
			alert('<spring:message code="LABEL_GIM_CHEARGE_SPACE" text="Please remove white space in GIM %"/>');
			return false;
		} else if (bankCharge == "") {
			alert(Alertmsg.bankCharge);
			return false;
		} else if (bankCharge.charAt(0) == " "
				|| bankCharge.charAt(bankCharge.length - 1) == " ") {
			alert('<spring:message code="LABEL_BANK_PERCENT_SPACE" text="Please remove white space in bank %"/>');
			return false;
		} else if (stampFee == "") {
			alert(Alertmsg.stampEmpty);
			return false;
		} else if (stampFee.charAt(0) == " "
				|| stampFee.charAt(stampFee.length - 1) == " ") {
			alert('<spring:message code="LABEL_STAMPFEE_SPACE" text="Please remove white space in stampfee"/>');
			return false;
		} else if (isNaN(stampFee) || parseFloat(stampFee) < 0) {
			alert(Alertmsg.stampFee);
			return false;
		} else if (stampFee.length > 6) {
			alert(Alertmsg.stampValue);
			return false;
		} else if (isNaN(govtTax) || parseFloat(govtTax) < 0) {
			alert(Alertmsg.govtTaxDigits);
			return false;
		} else if (isNaN(eotCharge)) {
			alert(Alertmsg.gimChargeDigit);
			return false;
		} else if (isNaN(bankCharge)) {
			alert(Alertmsg.bankChargeDigits);
			return false;
		} else if (govtTax > 100) {
			alert('<spring:message code=" LABEL_GOVT_TAX_PERCENT" text="Tax should be less than 100 %"/>');
			return false;
		} else if (gimbankScharge != 100) {
			alert('<spring:message code="LABEL_GIM_BANK_CHARGE" text="The sum of Bank service charge and Gim service charge should be 100%"/>');
			return false;
		} else if (description.charAt(0) == " "
				|| description.charAt(description.length - 1) == " ") {
			alert('<spring:message code="LABEL.DESCRIPTION.BLANK1" text="Please remove the white space from Description"/>');
			return false;
		} else if (description.length > 180) {
			alert(Alertmsg.descLength);
			return false;
		} else if (description.search(descPattern)==-1){
			alert(Alertmsg.descSpecial);
			return false;
		}
		else {
			
		
			if (modelIdArray.length == 0) {
				alert('<spring:message code="ALERT_BANK_AGREEMENT" text="Please select Agreement Model"/>');
				return false;
			}
			if (selectedCHouseNameArr.length == 0) {
				alert(Alertmsg.clearinghouse);
				return false;
			}
			/* if (selectedcHouse == 0) {
				alert(Alertmsg.clearinghouse);
				return false;
			} */
		
			 /*if (applicationName == "") {
				alert(Alertmsg.applicationName);
				return false;
			} else if (applicationName.charAt(0) == " "
					|| applicationName.charAt(bankName.length - 1) == " ") {
				alert(Alertmsg.applicationName);
				return false;
			} else if (applicationName.search(pattern) == -1) {
				alert(Alertmsg.applicationName);
				return false;
			}
			  if( channelCost == "" )
		        {
		    	      alert( '<spring:message code="ALERT.CHANNELCOST.NUMARICS" text="Please enter a valid channel cost"/>'); 
		    	      return false;
		        } else if(channelCost.charAt(0) == " "  || channelCost.charAt(channelCost.length-1) == " "){
		    	    	  alert('<spring:message code="ALERT.CHANNELCOST.WHITESPACES" text="Please remove white spaces"/>');
		    	    	  return false;
		    	  }
		    	  else if(!numericPattern.test( channelCost )){
		    		 // alert(numericPattern.test( channelCost ));
		    		  alert('<spring:message code="ALERT.CHANNELCOST.NUMARICS" text="Please enter a valid channel cost"/>');
		    		  return false;
		    		  }
		    	  else if((channelCost.charAt(0)== '.') ){
		         		  alert('<spring:message code="ALERT.CHANNELCOST.NUMARICS" text="Please enter a valid channel cost"/>');
		     		  return false;
		    	  } */
			     writeValues();
				writeagrementValues();
			document.bankManageMentForm.submit();

		}
	}

	function addAgreements(status, a, b, c) {
		var agreementTo;
		var agreementFrom;
		var radioValue=-1;
		if (status == 1) {
			radioValue = a;
			agreementFrom = b;
			agreementTo = c;
		} else {

			agreementTo = document.getElementById("agreementToField0").value;
			agreementFrom = document.getElementById("agreementFromField0").value;
			

			for ( var i = 0; i < document.bankManageMentForm.modelId1.length; i++) {
				if (document.bankManageMentForm.modelId1[i].checked) {
					radioValue = document.bankManageMentForm.modelId1[i].value;

					break;
				}
			}

			if (agreementFrom == "") {
				alert(Alertmsg.dateValidFrom);
				return false;
			}
			if (agreementTo == "") {
				alert(Alertmsg.dateValidTo);
				return false;
			}
			if (!validdate(agreementFrom, agreementTo)) {

				return false;

			}
			if (!currentDate(agreementFrom)) {

				alert(Alertmsg.currentDateValid);
				return false;
			}

			if (!compareFromDate(agreementFrom, agreementTo)) {
				alert(Alertmsg.todategreater);
				return false;
			}
			if (agreementFrom == agreementTo) {
				alert(Alertmsg.dateSame);
				return false;
			}

			else {

				if (n > 0) {

					for ( var r = 0; r < modelIdArray.length; r++) {
						//alert(" id "+modelIdArray[r] + " = " +radioValue)
						//if(modelIdArray[r] == radioValue){
						//alert("inside")
						//alert(" f "+stringToDate(agreementFrom) +" f "+stringToDate(fdateArray[r]))
						if ((stringToDate(agreementFrom) >= stringToDate(fdateArray[r]) && stringToDate(agreementFrom) <= stringToDate(tdateArray[r]))
								|| (stringToDate(agreementTo) >= stringToDate(fdateArray[r]) && stringToDate(agreementTo) <= stringToDate(tdateArray[r]))) {
							alert(Alertmsg.fromdategttodate);
							return false;
						}
						if (stringToDate(agreementFrom) < stringToDate(fdateArray[r])) {

							if (stringToDate(agreementTo) > stringToDate(fdateArray[r])) {
								alert(Alertmsg.fromdategttodate);
								return false;
							}
						}

						//}

					}

				}

			}

		}
		modelIdArray[modelIdArray.length] = radioValue;
		fdateArray[fdateArray.length] = agreementFrom;
		tdateArray[tdateArray.length] = agreementTo;
		
		n++;
		
		var table = document.getElementById("agGrid");
		var lastRow = table.rows.length;

		var row = table.insertRow(lastRow);

		var td1 = row.insertCell(0);
		td1.setAttribute("align", "center");
		td1.setAttribute("width", "48px;");
		td1.setAttribute("height", "15px;");
		td1.innerHTML = lastRow+1;

		var td2 = row.insertCell(1);
		td2.setAttribute("align", "center");
		td2.setAttribute("width", "150px;");

		if (radioValue == '1') {
			radioValue = "<spring:message code="LABEL_REVENUESHARING" text="Revenue Sharing" />";
		} else {
			radioValue = "<spring:message code="LABEL_ONETIMEPAYMENT" text="One Time Payment" />";
		}
		td2.innerHTML = radioValue;

		var td3 = row.insertCell(2);
		td3.setAttribute("align", "center");
		td3.setAttribute("width", "100px;");
		td3.innerHTML = agreementFrom;

		var td4 = row.insertCell(3);
		td4.setAttribute("align", "center");
		td4.setAttribute("width", "100px;");
		td4.innerHTML = agreementTo;

		var td5 = row.insertCell(4);
		td5.setAttribute("align", "center");
		td5.setAttribute("width", "100px;");
		td5.innerHTML = "<a onclick='deleteagRow();' style='cursor:pointer'>"
				+ "Delete" + "</a>";

		document.getElementById("agreementToField0").value = "";
		document.getElementById("agreementFromField0").value = "";
		document.bankManageMentForm.modelId1[0].checked = true;

	

	}
	function writeagrementValues() {
		if (modelIdArray.length == 0)
			return false;
		var t1 = "";
		var t2 = "";
		var t3 = "";
		for ( var i = 0; i < modelIdArray.length; i++) {

			if (modelIdArray[i] != undefined) {

				if (modelIdArray[i] == "<spring:message code="LABEL_REVENUE_SHARING" text="RevenueSharing" />"
						|| modelIdArray[i] == '1') {
					modelIdArray[i] = '1';

				} else {
					modelIdArray[i] = '2';
				}

				t1 = (i != (modelIdArray.length - 1)) ? t1 + modelIdArray[i]
						+ "," : t1 + modelIdArray[i];

				t2 = (i != (modelIdArray.length - 1)) ? t2 + fdateArray[i]
						+ "," : t2 + fdateArray[i];
				t3 = (i != (modelIdArray.length - 1)) ? t3 + tdateArray[i]
						+ "," : t3 + tdateArray[i];

			}
		}
		document.getElementById("agreementModelId").value = t1;
		document.getElementById("agreementFromField").value = t2;
		document.getElementById("agreementToField").value = t3;

	}

	function setAgrementsByTableData() {

		var table = document.getElementById("agGrid");
		var value;
		for ( var i = 0; i < table.rows.length; i++) {
			for ( var j = 1; j < 4; j++) {
				value = table.rows[i].cells[j].innerHTML;

				switch (j) {
				case 1:
					modelIdArray[n] = (value == "<spring:message code="LABEL_REVENUE_SHARING" text="RevenueSharing" />" ? 1
							: 2);
					 //alert(modelIdArray[n]+":length"+n)
					break;
				case 2:
					fdateArray[n] = value;
					 //alert(fdateArray[n]+":length"+n)
					break;
				case 3:
					tdateArray[n] = value;
					//alert(tdateArray[n]+":length"+n)
					break;
				}
			}
			n++;
		}

	}

	function setArraysByTableData() {
		var table = document.getElementById("tblGrid");
		var value;
		for ( var i = 0; i < table.rows.length; i++) {
			for ( var j = 1; j < 3; j++) {
				value = table.rows[i].cells[j].innerHTML;
				switch (j) {
				case 1:
					selectedCHouseNameArr[z] = value.substring(value.indexOf ('>')+1,value.lastIndexOf('<'));
					break;
				case 2:
					priorityArr[z] = value;
					break;
				}
			}
			z++;
		}
		document.getElementById("clearingHouses").value = "";
		document.getElementById("clearingHousePriorities").value = "";
		
	}

	function deleteagRow() {
		var table = document.getElementById("agGrid");
		var rows = document.getElementById('agGrid').getElementsByTagName('tbody')[0].getElementsByTagName('tr');
		for ( var s = 0; s < rows.length; s++) {
			rows[s].onclick = function() {
				var i = this.rowIndex;
				table.deleteRow(i);
				modelIdArray.splice(i, 1);
				fdateArray.splice(i, 1);
		        tdateArray.splice(i, 1);
				n--;
				if(modelId!=0)
			   {modelId--;}
				
				
			};
		}
	}

	function readAgreementValuesFromTxtBox() {
		var agreementTo = document.getElementById("agreementToField").value
				.split(",");
		var agreementFrom = document.getElementById("agreementFromField").value
				.split(",");
		var modelid = document.getElementById("agreementModelId").value
				.split(",");

		if (modelid.length != 0) {

			for ( var i = 0; i < modelid.length; i++) {
				if (agreementFrom[i] != "") {
					addAgreements(1, modelid[i], agreementFrom[i],
							agreementTo[i]);
				}
			}

		}
		document.getElementById("agreementToField").value == "";
		document.getElementById("agreementFromField").value == "";
		document.getElementById("agreementModelId").value == "";

	}

	function readCHPValuesFromTxtBox() {
		var ch = document.getElementById("clearingHouses").value.split(",");
		var pr = document.getElementById("clearingHousePriorities").value
				.split(",");
		if (ch.length != 0) {

			for ( var i = 0; i < ch.length; i++) {
				if (ch[i] != null) {
					addCHouse(1, ch[i], pr[i]);
				}
			}

		}
		document.getElementById("clearingHouses").value == "";
		document.getElementById("clearingHousePriorities").value == "";

	}

	function textCounter(field, cntfield, maxlimit) {
		if (field.value.length > maxlimit) // if too long...trim it!
			field.value = field.value.substring(0, maxlimit);
		// otherwise, update 'characters left' counter
		else
			cntfield.value = maxlimit - field.value.length;
	}

	function stringToDate(fromdt) {
		var dt1 = parseInt(fromdt.substring(0, 2), 10);
		var mon1 = parseInt(fromdt.substring(3, 5), 10) - 1;
		var yr1 = parseInt(fromdt.substring(6, 10), 10);
		return new Date(yr1, mon1, dt1);
	}
	function checkAllZero(value) {

		var count = 0;
		for ( var i = 0; i < value.length; i++) {

			if (value.charAt(i) == 0) {
				count++;
			}
		}
		if (count == value.length) {
			return false;
		} else {
			return true;
		}

	}
	
	function cancelForm(){
		document.bankManageMentForm.action="showBankManagementForm.htm";
		document.bankManageMentForm.submit();
	}
</script>

<script type="text/javascript">
var actionButton = document.getElementById("actionButton").value;
if(actionButton == 'Update '){
setArraysByTableData();
setAgrementsByTableData();
check();
check1();
}else if(actionButton == 'Add'){
	readAgreementValuesFromTxtBox();
	readCHPValuesFromTxtBox();
	check();
	check1();
}
//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting -->
function bankDetail(url,bankId){
	document.getElementById('bankId').value=bankId;
	submitlink(url,'bankManageMentForm');
}
//@end
</script>
</head>
<body onload="check(),check1()">
	<form:form name="bankManageMentForm" id="bankManageMentForm" class="form-inline"
		action="saveBank.htm" method="post" commandName="bankDTO"
		enctype="multipart/form-data" autocomplete="off">
		<jsp:include page="csrf_token.jsp"/>
		<div class="row">
		<form:hidden path="countryId" value="1" id="countryId" />
			<div class="col-lg-12">
				<div class="box">
					<div class="box-header">
						<h3 class="box-title">
							<span><spring:message code="LABEL_BANK_MNMT" text="Banks" /></span>
						</h3>
					</div>
					<br />
					<div class="col-sm-5 col-sm-offset-4" style="color: #ba0101; font-weight: bold; font-size: 12px;">
						<spring:message code="${message}" text="" />
					</div>
					<div class="col-md-3 col-md-offset-9" id="bankTable">
					<c:if test="${(bankDTO.bankId ne null) }">
					<!-- //@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting --> 
					<%-- <a href="javascript:submitForm('bankManageMentForm','addCard.htm?bankId=<c:out value="${bankDTO.bankId}"/>')"><b><spring:message
								code="LABEL_ADD_CARD" text="Add Card" /></b></a> |    
     				<a href="javascript:submitForm('bankManageMentForm','searchBranch.htm?bankId=<c:out value="${bankDTO.bankId}"/>')"><b><spring:message
								code="LABEL_VIEW_BRANCH" text="View Branches" /></b></a> --%>
							<a href="javascript:bankDetail('addCard.htm','<c:out value="${bankDTO.bankId}"/>')"><b><spring:message
								code="LABEL_ADD_CARD" text="Add Card" /></b></a> |    
     				<a href="javascript:bankDetail('searchBranch.htm','<c:out value="${bankDTO.bankId}"/>')"><b><spring:message
								code="LABEL_VIEW_BRANCH" text="View Branches" /></b></a>
								<!-- @End -->
					</c:if>
					</div>
					<div class="box-body">
						<div class="row">
							
							<div class="col-sm-6">
								<label class="col-sm-4"><spring:message
										code="LABEL_BANKNAME" text="Bank Name:" /><font color="red">*</font></label>
								<form:hidden path="bankId" />

								<form:input path="bankName" cssClass="form-control"
									maxlength="50" />
								<FONT color="red"><form:errors path="bankName" /></FONT>
							</div>
							<div class="col-sm-6">
								<label class="col-sm-4"><spring:message
										code="LABEL_BANKSHORTNAME" text="Bank Short Name" /><font
									color="red">*</font></label>
								<form:input path="bankShortName" cssClass="form-control"
									maxlength="5" />
								<FONT color="red"><form:errors path="bankShortName" /></FONT>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<label class="col-sm-4"><spring:message
										code="LABEL_SWIFT_CODE" text="BIC/SWIFT Code" /><font
									color="red">*</font></label>
								<form:input path="swiftCode" cssClass="form-control"
									maxlength="8" />
								<FONT color="red"><form:errors path="swiftCode" /></FONT>
							</div>
							<div class="col-sm-6">
								<label class="col-sm-4"><spring:message
										code="LABEL_SWIFT_BRANCH" text="SWIFT Branch" /></label>
								<form:input path="swiftBranch" cssClass="form-control"
									maxlength="3" />
								<FONT color="red"><form:errors path="swiftBranch" /></FONT>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<label class="col-sm-4"><spring:message
										code="LABEL_BANKCODE" text="Bank Code" /><font color="red">*</font></label>
								<form:input path="bankCode" cssClass="form-control"
									maxlength="5" />
								<FONT color="red"><form:errors path="bankCode" /></FONT>
							</div>
							<div class="col-sm-6">
								<label class="col-sm-4"><spring:message
										code="LABEL_STATUS" text="Status" /><font color="red">*</font></label>
								<form:radiobutton path="status" value="1" label="Active"></form:radiobutton>
								&nbsp; &nbsp;
								<form:radiobutton path="status" value="0" label="Inactive"></form:radiobutton>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<label class="col-sm-4"><spring:message
										code="LABEL_COUNTRY" text="Country:" /><!-- <font color="red">*</font> -->
										</label>
								<label class="col-sm-4"><spring:message
									code="LABEL_COUNTRY_SOUTH_SUDAN" text="South Sudan" /></label>
								
							<%-- 	commenting to make a default country as SouthSudan,
									by vineeth on 13-11-2018
								<select class="dropdown chosen-select" id="countryId" name="countryId">
									<option value="">
										<spring:message code="LABEL_SELECT" text="--Please Select--" />
										<c:set var="lang" value="${language}"></c:set>
										<c:forEach items="${masterData.countryList}" var="country">
											<c:forEach items="${country.countryNames}" var="cNames">
												<c:if test="${cNames.comp_id.languageCode==lang }">
													<option value="<c:out value="${country.countryId}"/>"
														<c:if test="${country.countryId eq bankDTO.countryId}" >selected=true</c:if>>
														<c:out value="${cNames.countryName}" />
													</option>
												</c:if>
											</c:forEach>
										</c:forEach>
								</select> <FONT color="red"><form:errors path="countryId" /></FONT> --%>
							</div>
							<div class="col-sm-6">
								<label class="col-sm-4"><spring:message
										code="LABEL_CURRENCY" text="Currency:" />
									</label>	
									<%-- 	<font color="red">*</font></label>
								<form:select path="currencyId" cssClass="dropdown chosen-select">
									<form:option value="">
										<spring:message code="LABEL_SELECT" text="--Please Select--" />
									</form:option>
									
									<form:options items="${masterData.currencyList}"
										itemLabel="currencyName" itemValue="currencyId"></form:options>
								</form:select>
								<FONT color="red"><form:errors path="currencyId" /></FONT> --%>
								<label class="col-sm-4"><spring:message
										code="LABEL_COUNTRY_SOUTH_SUDANESE_POUND" text="South Sudanese Pound" />
									</label>	
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<label class="col-sm-4"><spring:message
										code="LABEL_TIMEZONE" text="Time Zone:" /><font color="red">*</font></label>
								<form:select path="timezoneId" cssClass="dropdown chosen-select">
									<form:option value="">
										<spring:message code="LABEL_SELECT" text="--Please Select--" />
									</form:option>
									<form:options items="${masterData.timeZoneList}"
										itemLabel="timeZoneDesc" itemValue="timeZoneId"></form:options>
								</form:select>
								<FONT color="red"><form:errors path="timezoneId" /></FONT>
							</div>
							<div class="col-sm-6">
								<label class="col-sm-4"><form:label for="branchFile"
										path="branchFile">
										<spring:message code="LABEL_UPLOAD_BRANCH"
											text="Branch: (Only .csv file)" />
									</form:label></label>
								<form:input path="branchFile" type="file" id="branch" />
								<a href="javascript:submitForm('uploadForm','fileUploadInstructions.htm?templeteId=<c:out value="${bankDTO.templeteId}"/>');" ><spring:message code="LABEL_DOWNLOAD_FILE_INST"
											text="(Download instructions)" /></a>
							</div>
						</div>
						<div class="row">
							
							<div class="col-sm-6">
								<label class="col-sm-4"><spring:message
										code="LABEL_ID_REQUIRED" text="ID Proof Required" /><font
									color="red">*</font></label>
								<form:radiobutton path="kycFlag" value="1" label="Yes"></form:radiobutton>
								&nbsp; &nbsp;
								<form:radiobutton path="kycFlag" value="0" label="No"></form:radiobutton>
							</div>
							
							
							<div class="col-sm-6">
								<authz:authorize ifAnyGranted="ROLE_admin">
									<label class="col-sm-4"><spring:message
											code="LABEL_BANKGROUP" text="BankGroup" /></label>

									<form:select path="bankGroupId" cssClass="dropdown chosen-select">
										<form:option value="">
											<spring:message code="LABEL_SELECT" text="--Please Select--" />
										</form:option>
										<form:options items="${masterData.bankGroupList}"
											itemLabel="bankGroupName" itemValue="bankGroupId"></form:options>

									</form:select>
									<FONT color="red"><form:errors path="bankGroupId" /></FONT>
								</authz:authorize>
								<br />
								<br /><%--  <label class="col-sm-4"><spring:message
										code="LABEL_GIM_CARD_BANK_CODE" text="GIM Card Bank Code" /><font
									color="red">*</font></label>
								<form:input path="eotCardBankCode" cssClass="form-control"
									maxlength="6" />
								<FONT color="red"><form:errors path="eotCardBankCode" /></FONT> --%>
							</div>
						</div>
						<%-- <div class="row">
							<div class="col-sm-6">
								<label class="col-sm-4"><spring:message
										code="LABEL_TERMINAL_ID" text="Terminal ID" /><font
									color="red">*</font></label>
								<form:input path="terminalId" cssClass="form-control"
									maxlength="8" />
								<FONT><form:errors path="terminalId" /></FONT>
							</div>
							<div class="col-sm-6">
								<label class="col-sm-4"><spring:message
										code="LABEL_OUTLET_NUMBER" text="Outlet Number" /><font
									color="red">*</font></label>
								<form:input path="outletNumber" cssClass="form-control"
									maxlength="15" />
								<FONT color="red"><form:errors path="outletNumber" /></FONT>
							</div>
						</div> --%>
						<div class="row">
							
							<%-- <div class="col-sm-6">
								<label class="col-sm-4"><spring:message
										code="LABEL_COLLABORATION_TYPE" text="Collaboration Type"></spring:message><font
									color="red">*</font></label>
								<form:select path="collaborationLogo" cssClass="dropdown chosen-select">
									<form:option value="GIM">
										<spring:message code="LABEL_GIMK" text="GIM"></spring:message>
									</form:option>
									<form:option value="BANK">
										<spring:message code="LABEL_BANK" text="Bank"></spring:message>
									</form:option>
									<form:option value="COMBO">
										<spring:message code="LABEL_COMBO" text="Bank & GIM"></spring:message>
									</form:option>
								</form:select>
								<font color="RED"><form:errors path="collaborationLogo"></form:errors></font>
							</div> --%>
						</div>
						<%-- <div class="row">
							<div class="col-sm-6">
								<label class="col-sm-4"><spring:message
										code="LABEL_COMMERCIAL_NAME" text="Commercial Name" /> <font
									color="red">*</font></label>
								<form:input path="applicationName" cssClass="form-control"
									maxlength="20" />
								<FONT color="red"><form:errors path="applicationName" /></FONT>
							</div>
							<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
										code="LABEL_CHANNEL_COST" text="Channel Cost" /> <font
									color="red">*</font></label>
								<form:input path="channelCost" cssClass="form-control"
									maxlength="6" />
								<FONT color="red"><form:errors path="channelCost" /></FONT>
							</div>
						</div> --%>
						<div class="row">
						<div class="col-sm-12">
							<div class="control-group col-md-2" style="margin-top: 2.3%;">
								<label class="control-label" for="name"><spring:message
										code="LABEL_SERVICE_CHARGE_SPLIT" text="Service Charge Split" /><font
									color="red">*</font></label>
							</div>
							<div class="control-group col-md-2">
								<label class="control-label" for="name"><spring:message
										code="LABEL_STAMP_FEE" text="Stamp Fee" /></label>
								<div class="controls">
									<form:input path="stampFee" cssClass="form-control"
										style="width:100%;" maxlength="6" />
									<FONT color="red"><form:errors path="stampFee" /></FONT>
								</div>
							</div>
							<div class="control-group col-md-2">
								<label class="control-label" for="name"><spring:message
										code="LABEL_TAX" text="Tax" /> %</label>
								<div class="controls">
									<form:input path="govtTax" cssClass="form-control"
										style="width:100%;" maxlength="6" />
									<FONT color="red"><form:errors path="govtTax" /></FONT>
								</div>
							</div>
							<div class="control-group col-md-2">
								<label class="control-label" for="name"><spring:message
										code="LABEL_GIM" text="GIM" /> %</label>
								<div class="controls">
									<form:input path="eotCharge" cssClass="form-control"
										style="width:100%;" maxlength="6" />
									<FONT color="red"><form:errors path="eotCharge" /></FONT>
								</div>
							</div>
							<div class="control-group col-md-2">
								<label class="control-label" for="name"><spring:message
										code="LABEL_ENTITY_SHARE" text="m-GURUSH" /> %</label>
								<div class="controls">
									<form:input path="bankCharge" cssClass="form-control"
										style="width:100%;" maxlength="6" />
									<FONT color="red"><form:errors path="bankCharge" /></FONT>
								</div>
							</div>
						</div>
						<br>
						</div>
						
						<div class="row">
							<div class="col-sm-6">
								<label class="col-sm-4"><spring:message
										code="LABEL_BANKDESC" text="Description" /></label>
								<form:textarea path="description" rows="4" cols="19" style="resize: none !important;"
									onKeyDown="textCounter(document.bankManageMentForm.description,180,180)"
									onKeyUp="textCounter(document.bankManageMentForm.description,180,180)" />
								<FONT color="red"><form:errors path="description" /></FONT>
							</div>
						
						</div>
					</div>
				</div>
				<div class="box">
					<div class="box-body">
						<div class="table-responsive">
							<table style="text-align: center; width: 100%">
								<tr>
									<form:hidden path="agreementModelId" />
									<form:hidden path="agreementFromField" />
									<form:hidden path="agreementToField" />
									<td style="font-weight: bold">Agreements</td>
									<td><input type="radio" id="modelId1" name="modelId1"
										value="1" checked />
									<spring:message code="LABEL_REVENUESHARING"
											text="Revenue Sharing" />&nbsp; &nbsp; <input type="radio"
										id="modelId1" name="modelId1" value="2" /> <spring:message
											code="LABEL_ONETIMEPAYMENT" text="One Time Payment" /></td>
									<td style="font-weight: bold"><spring:message
											code="LABEL_AGREEMENT_FROM" text="Agreement From" /><font
										color="red">*</font> <input type="text"
										id="agreementFromField0" class="form-control datepicker"></input>
										<FONT color="red"><form:errors
												path="agreementFromField" /></FONT>
									<td style="font-weight: bold"><spring:message
											code="LABEL_AGREEMENT_TO" text="To" /><font color="red">*</font>
										<input type="text" id="agreementToField0"
										class="form-control datepicker"></input> <FONT color="red"><form:errors
												path="agreementToField" /></FONT>
									<td style="font-weight: bold"><input type="button"
										value="<spring:message code="LABEL_ADD" text="Add"/>"
										onclick="addAgreements(0,'','','');"></td>
								</tr>
							</table>
							<br>
							 <table width="100%" class = "table table-bordered table-hover"
                                                                        style="-moz-border-radius: 5px; -webkit-border-radius: 5px; border: 1px solid #a6a6a7"
                                                                        cellpadding="0" cellspacing="0" 
                                                                        align="center">
                                                                            <tr class="tableheading">
                                                                                <th align="center" width="50px"><spring:message 
                                                                                    code="LABEL_SL_NO" text="SL.NO." /></th>
                                                                                <th align="center"  width="150px"><spring:message
                                                                             code="LABEL_AGREEMENTS" text="Agreements" /></th>
                                                                                <th align="center" width="100px" ><spring:message
                                                                                    code="LABEL_AGREEMENT_FDATE" text="From Date" /></th>
                                                                                <th align="center" width="100px"><spring:message
                                                                                    code="LABEL_AGREEMENT_TDATE" text="To Date" /></th>
                                                                                <th align="center" width="100px"><spring:message
                                                                                    code="LABEL_ACTION" text="Action" /></th>
                                                                                    
                                                                            </tr>
                                                                            <tr>
                                                                          <td colspan="5">
                                                                            <table id="agGrid" class = "table table-bordered table-hover"  width="100%" border="0" 
                                                                                cellspacing="0" cellpadding="0" align="left">
                                                                                <tbody>
                                                                                <c:set var="id" value="1" scope="page"></c:set>                                        
                                   
                                                                                <c:set var="id" value="1" scope="page"></c:set>                                                    
                                                                     
                                                                                        <c:forEach items="${bankDTO.agreements}"     var="ag">
                                                   <tr>
                                                                                    <td align="center" width="48px"><c:out
                                                                                                        value="${id}" /></td>
                                                                                                        <c:if test="${ag.agreementType==1}">
                                                                                                     <c:set var="model"><spring:message code="LABEL_REVENUESHARING" text="Revenue Sharing" /></c:set>
                                                                                                        </c:if>
                                                                                            <c:if test="${ag.agreementType==2}">
                                                                                            <c:set var="model"><spring:message code="LABEL_ONETIMEPAYMENT" text="One Time Payment" /> </c:set>
                                                                                                </c:if>
                                                                                          <td  align="center" width="152px"><c:out value="${model}" /></td>
                                                                                                     <td  align="left" width="100px" ><fmt:formatDate pattern="dd/MM/yyyy"  value="${ag.agreementFrom}"/>
                                                                                                         </td>
                                                                                                    <td  align="center" width="100px"><fmt:formatDate pattern="dd/MM/yyyy"  value="${ag.agreementTo}" />
                                                                                                    </td>
                                                                                                    
                                                                                                    <td align="center" width="100px" ><a
                                                                                                        onclick='deleteagRow();' style='cursor: pointer'><spring:message
                                                                                                        code="LABEL_DELETE" text="DELETE" /></a></td>
                                                                                                </tr>
                                                                                            <c:set var="id" value="${id+1}" scope="page"></c:set>
                                                                                                                                                                
                                                                                                
                                                                                        </c:forEach>
                                                                                
                                                                                </tbody>
                                                                                
                                                                            </table>
                                                                          </td>
                                                                        </tr>
                                                                    </table>
						</div>
					</div>
				</div>
				<div class="box">
					<div class="box-body">
						<div class="table-responsive">
							<table style="text-align: center; width: 100%">
								<tr style="font-weight: bold">
									<td colspan="2" style="text-align: right"><spring:message
											code="LABEL_SELECT_CLEARANCE_HOUSE"
											text="Select Clearance House:" /> <font color="red">*</font>
										<form:hidden path="clearingHouses" /> <form:hidden
											path="clearingHousePriorities" /> 
											<select class="dropdown" id="chouses" >
											<option value="0">
												<spring:message code="LABEL_SELECT" text="--Please Select--" />
											</option>
											<c:forEach items="${masterData.clearingHouseList}" var="ch1">
												<option value="<c:out value="${ch1.clearingPoolId}"/>"
													label="<c:out value="${ch1.clearingPoolName}"/>">
													<c:out value="${ch1.clearingPoolName}" />
												</option>
											</c:forEach>
									</select> <FONT color="red"><form:errors path="clearingHouses" /></FONT>
									</td>
									<td><spring:message code="LABEL_PRIORITY" text="Priority" /><font
										color="red">*</font> &nbsp; &nbsp; <input type="text" size="2"
										id="priority" class="form-control"></input> <FONT color="red">
											<form:errors path="clearingHousePriorities" />
									</FONT></td>
									<td><input type="button"
										value="<spring:message code="LABEL_ADD" text="Add"/>"
										onclick="addCHouse(0,'','');"></td>
								</tr>
							</table>
							<br>
						<table border="0" width="100%" >
                                                            <tr>
                                                                <td colspan="5">
                                                                <div style="height: auto; overflow: auto;">
                                                                <table width="100%"
                                                                    style="-moz-border-radius: 5px; -webkit-border-radius: 5px; border: 1px solid #a6a6a7"
                                                                    cellpadding="0" cellspacing="0" border="0" width="80%"
                                                                    align="center" class="table table-bordered table-hover">
                                                                    <tr class="tableheading">
                                                                        <th align="center"  width="50px"><spring:message
                                                                            code="LABEL_SL_NO" text="SL.NO" /></th>
                                                                        <th align="center"  width="278px"><spring:message
                                                                            code="LABEL_CHPOOL_NAME" text="Clearing House Pool Name" /></th>
                                                                        <th align="center"  width="100px"><spring:message
                                                                            code="LABEL_PRIORITY" text="Priority" /></th>
                                                                        <th align="center"  width="70px"><spring:message
                                                                            code="LABEL_ACTION" text="Action" /></th>
                                                                    </tr>
            
                                                                    <tr>
                                                                        <td colspan="4">
                                                                        <table id="tblGrid" width="100%" border="0"
                                                                            cellspacing="0" cellpadding="0" class="table table-bordered table-hover">
                                                                            <tbody>
                                                                                <c:set var="id" value="1" scope="page"></c:set>
                                                                                <c:forEach items="${bankDTO.chPool}" var="cpId">
                                                                                    <c:forEach items="${masterData.clearingHouseList}"
                                                                                        var="ch">
                                                                                        <c:if test="${cpId.clearingPoolId == ch.clearingPoolId}">
                                                                                            <tr>
                                                                                                <td align="center" width="48px"><c:out
                                                                                                    value="${id}" /></td>
                                                                                                <td align="center" width="280px"><c:out
                                                                                                    value="${ch.clearingPoolName}" />
                                                                                                <div style="display: none;"><c:out
                                                                                                    value="${ch.clearingPoolId}" /></div>
                                                                                                </td>
                                                                                                <td align="center" width="100px"><c:out
                                                                                                    value="${cpId.poolPriority}" /></td>
                                                                                                <td align="center" width="70px"><a
                                                                                                    onclick='deleteRow();' style='cursor: pointer'><spring:message
                                                                                                    code="LABEL_DELETE" text="DELETE" /></a></td>
                                                                                            </tr>
                                                                                        </c:if>
                                                                                    </c:forEach>
                                                                                    <c:set var="id" value="${id+1}" scope="page"></c:set>
                                                                                </c:forEach>
                                                                            </tbody>
                                                                        </table>
                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                                </div>
                                                                </td>
                                                            </tr>
                                                            
                                                    
                                                    <c:if test="${fn:length(bankDTO.branchCodeLength)>0}">
                                                         <tr> <td align="center" colspan="7"><FONT color="RED"> <spring:message code="LABEL_BRANCH_CODE_LENGTH"
                                                                    text="Branch Code Length  should be 5 characters ." /></FONT><td></tr>
                                                        <c:forEach items="${bankDTO.branchCodeLength}" var="error">
                                                            <tr>
                                                                <td align="center" colspan="7"><FONT color="RED"><spring:message
                                                                    code="LABEL_BRANCH_FILE_ERROR_ROW_NO" text="Row Number" />
                                                                <c:out value="${error}">
                                                                </c:out> </FONT></td>
                                                            </tr>
                                                        </c:forEach>
                                                    </c:if>
                                                    <c:if test="${fn:length(bankDTO.duplicateBranch)>0 }" >
                                                    <tr> <td align="center" colspan="7"><FONT color="RED"><spring:message code="LABEL_BRANCH_LOCATION_ALREADY_EXIST"
                                                                    text="Branch Location is already exist ." /></FONT><td></tr>
                                                    <c:forEach items="${bankDTO.duplicateBranch}" var="error">
                                                            <tr>
                                                                <td align="center" colspan="7"><FONT color="RED"><spring:message
                                                                    code="LABEL_BRANCH_FILE_ERROR_ROW_NO" text="Row Number" />
                                                                <c:out value="${error}">
                                                                </c:out> </FONT></td>
                                                            </tr>
                                                        </c:forEach>
                                                        </c:if>
                                                        <%-- 
                                                        this code is commented to resolved bug id 5842: bidyut <06-08-2018>
                                                        <c:if test="${fn:length(bankDTO.duplicateBranchCode)>0 }" >
                                                    <tr> <td align="center" colspan="7"><FONT color="RED"><spring:message code="LABEL_BRANCH_CODE_ALREADY_EXIST"
                                                                    text="Branch Code is already exist ." /></FONT><td></tr>
                                                    <c:forEach items="${bankDTO.duplicateBranchCode}" var="error">
                                                            <tr>
                                                                <td align="center" colspan="7"><FONT color="RED"><spring:message
                                                                    code="LABEL_BRANCH_FILE_ERROR_ROW_NO" text="Row Number" />
                                                                <c:out value="${error}">
                                                                </c:out> </FONT></td>
                                                            </tr>
                                                        </c:forEach>
                                                        </c:if> --%>
                                                         <c:if test="${fn:length(bankDTO.invalidBranchCode)>0 }" >
                                                          <tr> <td align="center" colspan="7"><FONT color="RED"><spring:message code="LABEL_INVALID_BRANCH_CODE"
                                                                    text="Brnach Code Contain Invalid Values in csv file(Please enter alphanumeric only)" /></FONT><td></tr>
                                               			     <c:forEach items="${bankDTO.invalidBranchCode}" var="error">
                                                            <tr>
                                                                <td align="center" colspan="7"><FONT color="RED"><spring:message
                                                                    code="LABEL_BRANCH_FILE_ERROR_ROW_NO" text="Row Number" />
                                                                <c:out value="${error}">
                                                                </c:out> </FONT></td>
                                                            </tr>
                                                        </c:forEach>
                                                        </c:if>
                                                         <c:if test="${fn:length(bankDTO.csvduplicateBranchCode)>0}" >
                                                           <tr> <td align="center" colspan="7"><FONT color="RED"> <spring:message code="LABEL_CSV_DUPLICATE_BRANCH_CODE"
                                                                    text="Branch Code is duplicated in csv File" /></FONT><td></tr>
                                                                  
                                                    <c:forEach items="${bankDTO.csvduplicateBranchCode}" var="error">
                                                            <tr>
                                                                <td align="center" colspan="7"><FONT color="RED"><spring:message
                                                                    code="LABEL_BRANCH_FILE_ERROR_ROW_NO" text="Row Number" />
                                                                <c:out value="${error}">
                                                                </c:out> </FONT></td>
                                                            </tr>
                                                        </c:forEach>
                                                        </c:if>
                                                         <c:if test="${fn:length(bankDTO.csvInvalidCity)>0}" >
                                                           <tr> <td align="center" colspan="7"><FONT color="RED"> <spring:message code="LABEL_CSV_INVALID_CITY"
                                                                    text="Branch  File Contain Invalid State Name(s)" /></FONT><td></tr>
                                                                  
                                                    <c:forEach items="${bankDTO.csvInvalidCity}" var="error">
                                                            <tr>
                                                                <td align="center" colspan="7"><FONT color="RED"><spring:message
                                                                    code="LABEL_BRANCH_FILE_ERROR_ROW_NO" text="Row Number" />
                                                                <c:out value="${error}">
                                                                </c:out> </FONT></td>
                                                            </tr>
                                                        </c:forEach>
                                                        </c:if>
                                                         <c:if test="${fn:length(bankDTO.branchErrorList)>0}" >
                                                           <tr> <td align="center" colspan="7"><FONT color="RED"> <spring:message code="LABEL_CSV_INVALID_QUARTER"
                                                                    text="Branch  File Contain Invalid City/County Name(s)" /></FONT><td></tr>
                                                                  
                                                    <c:forEach items="${bankDTO.branchErrorList}" var="error">
                                                            <tr>
                                                                <td align="center" colspan="7"><FONT color="RED"><spring:message
                                                                    code="LABEL_BRANCH_FILE_ERROR_ROW_NO" text="Row Number" />
                                                                <c:out value="${error}">
                                                                </c:out> </FONT></td>
                                                            </tr>
                                                        </c:forEach>
                                                        </c:if>
                                                        </table>
                                                        <c:choose>
                                                    	
                                                        <c:when test="${(bankDTO.bankId eq null && message==null) }">
                                                            <script> window.onload=function(){check();check1();};</script>                                                            
                                                            <c:set var="buttonName" value="LABEL_ADD" scope="page" />
                                                        </c:when>
                                                         <c:when test="${(bankDTO.bankId eq null && message!=null) }">
                                                            <script> window.onload=function(){readAgreementValuesFromTxtBox();readCHPValuesFromTxtBox();check();check1();};</script>                                                            
                                                            <c:set var="buttonName" value="LABEL_ADD" scope="page" />
                                                        </c:when>
                                                        <c:when test="${(bankDTO.bankId ne null && message!=null) }">
                                                            <script> window.onload=function(){readAgreementValuesFromTxtBox();readCHPValuesFromTxtBox();check();check1();};</script>                                                            
                                                            <c:set var="buttonName" value="LABEL_UPDATE" scope="page" />
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:set var="buttonName" value="LABEL_UPDATE" scope="page" />
                                                            <script>window.onload=function(){setArraysByTableData();setAgrementsByTableData();check();check1();};</script>
                                                        </c:otherwise>
                                                    </c:choose>
						</div>
					</div>
				</div>
				<div class="col-md-3 col-md-offset-10">
				<input type="button" class="btn btn-primary" id="actionButton"
					value="<spring:message code="${ buttonName }" text="${ buttonName }"/>"
					onclick="validateFields();" />
				<div class="space"></div>
				<input type="button" class="btn btn-primary"
					value="<spring:message code="LABEL_CANCEL" text="Cancel"/>"
					onclick="cancelForm()" />
				</div>
			</div>
		</div>
</form:form>
<form:form name="uploadForm" id="uploadForm" commandName="bankDTO"></form:form>
</body>

</html>
