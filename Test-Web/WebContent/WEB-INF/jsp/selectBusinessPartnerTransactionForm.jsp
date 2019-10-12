<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<html>
<head>
<title><spring:message code="LABEL_TITLE" text="EOT Mobile" /></title>
<%-- <script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script> --%>
	<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.5.1.min.js"></script>
<script type="text/javascript">


	function validate(){
		
		 var mnumber=document.getElementById("mobileNumber").value;
		 var businessPartnerCode=document.getElementById("businessPartnerCode").value;
	//	 var isdNum=isdCode.split(",");
	//	 var mobileNumLen=isdNum[1];
		 var numPattern='^[0-9]*$';
			  	/* if(document.getElementById('isdCode').value!=''){
				 	mobilepattern='^\[0-9]{'+mobileNumLen+'}$'; 
				} */
		 if (document.getElementById('transactionType').value  ==""){
				alert("<spring:message code="VALIDATION_TRASACTION_TYPE" text="Please select Transaction Type" />");
				return false;
		 }
		 else if(document.getElementById("transactionType").value != 137 && document.getElementById("transactionType").value != 138){	
		 if ( businessPartnerCode== "" ) {
				alert("<spring:message code="VALIDATION_BUSINESS_PARTNER_CODE" text="Please enter Business Partner Code" />"); 
				return false;
		}/* else if (mnumber ==""){
				alert("<spring:message code="VALIDATION_MOBILE_DIGITS" text="Please enter Mobile Number" />");
		} */else if(businessPartnerCode.charAt(0) == " " || businessPartnerCode.charAt(businessPartnerCode.length-1) == " "){  
		      alert("<spring:message code='VALIDATION_BUSINESS_PARTNER_CODE_SPACE' text='Please remove  space in Business Partner Code'/>");
		      return false;
		 }else if (businessPartnerCode.search(numPattern)==-1){
				alert("<spring:message code="VALIDATION_BUSINESS_PARTNER_CODE_NUMBER" text="Please enter Business Partner Code only in digits" />");
		}/* else if(document.getElementById('isdCode').value!='' && mnumber.search(mobilepattern)==-1){
                 alert("<spring:message code="VALIDATION_MOBILE_LENGTH" text="Please enter valid Mobile Number of Length"/>"+ " "+mobileNumLen);
                 document.transactionForm.mobileNumber.focus();
         }*/ 
		}else if(document.getElementById("transactionType").value == 137 || document.getElementById("transactionType").value == 138){
			if ( mnumber== "" ) {
				alert("<spring:message code="VALIDATION_MOBILENUM" text="Please enter mobile number" />"); 
				return false;
		}
        } 
        if(document.getElementById("transactionType").value == 95 || document.getElementById("transactionType").value == 99){
        //	var mobileNumber=isdNum[0].concat(mnumber);
    	//	document.transactionForm.action ="getBusinessPartnerDetails.htm?businessPartnerCode="+businessPartnerCode;
    		document.transactionForm.action ="getBusinessPartnerDetails.htm";
    		document.transactionForm.submit();
        } 	 
        else{
		/* var mobileNumber=isdNum[0].concat(mnumber);
		document.transactionForm.action ="getAccountDetails.htm?mobileNumber="+mobileNumber;
		document.transactionForm.submit(); */
		
	//	var mobileNumber=isdNum[0].concat(mnumber);
    //    document.getElementById('mobileNumber').value=mobileNumber;
        var url = "getBusinessPartnerDetails.htm";
        submitForm('transactionForm',url);
        
	}
	}

	$(".transactionType").change(function() {
		var transactionType = document.getElementById("transactionType").value ;

		if( transactionType == 137 ||  transactionType == 138 ){
			$(".businessdet").show();
			$(".businessCode").hide();

		}else{
			$(".businessdet").hide();
			$(".businessCode").show();
		}
				
	});

		function getval(sel)
		{
			var transactionType = document.getElementById("transactionType").value ;

			if( transactionType == 137 ||  transactionType == 138 ){
				$(".businessdet").show();
				$(".businessCode").hide();

			}else{
				$(".businessdet").hide();
				$(".businessCode").show();
			}
		}
	 $(document).ready(function() {		
			var transactionType = document.getElementById("transactionType").value ;
			$("#bank_chosen").css("width","180px")	;
			
			if( transactionType == 137 ||  transactionType == 138 ){
				 	$(".businessdet").show();
					$(".businessCode").hide(); 

				}else{
					 $(".businessdet").hide();
					$(".businessCode").show(); 
				}
		});
</script>

</head>
<body>
<div class="col-md-12">
		<div class="box"  style="height:350px;">
			<div class="box-header">
				<c:if test="${roleAccess eq 2}">
				<h3 class="box-title">
					<span><spring:message code="TITLE_WELCOME_PRINCIPAL_AGENT_TRANSACTION_SERVICE" text="View Principal Agent Transactions" /></span>
				</h3>
				</c:if>
				<c:if test="${roleAccess ne 2}">
				<h3 class="box-title">
					<span><spring:message code="TITLE_WELCOME_BUSINESS_PARTNER_TRANSACTION_SERVICE" text="View BusinessPartner Transactions" /></span>
				</h3>
				</c:if>
			</div>
				<div class="col-sm-6 col-sm-offset-4">
					<div style="color: #ba0101; font-weight: bold; font-size: 12px;">
						<spring:message code="${message}" text="" />
					</div>
				</div>
			<!-- /.box-header -->
			<div class="col-sm-6 col-sm-offset-3"
				style="border: 1px solid; border-radius: 15px;"><br />
				<%-- <div class="pull-right">
					<a href="uploadTransactionDetails.htm"><b><spring:message
								code="LABEL_UPLOAD_BULK_TXN" text="Upload Transaction Details" /></b></a>
				</div><br /> --%>
				<!-- form start -->
				<form:form class="form-inline"
					action="getBusinessPartnerDetails.htm" method="post" name="transactionForm" id="transactionForm">
					<jsp:include page="csrf_token.jsp"/>
					
					<!--  <input type="hidden" id="businessPartnerCode" name="businessPartnerCode"/>
					  <input type="hidden" id="mobileNumber" name="mobileNumber"/> -->
					  
					 <div class="row">
						<%--  <form:hidden path="" value="${isdCode},${mobileNumLength}" id="isdCode" /> 
						 <c:set var="isdCode" value="${isdCode},${mobileNumLength}"/>
						 <input type="hidden" id="isdCode" value="${isdCode},${mobileNumLength}"/> --%>
						<div class="col-md-12">
							<label class="col-sm-6"><spring:message
									code="LABEL_COUNTRY" text="Country:" /><!-- <font color="red">*</font> --></label>
									<div class="col-sm-5">
									<label class="col-sm-10"><spring:message
									code="LABEL_COUNTRY_SOUTH_SUDAN" text="SouthSudan" /></label>
					<%-- 		 commenting to make a default country as SouthSudan,
									by vineeth on 12-11-2018		
							<select class="dropdown chosen-select" id="isdCode" name="isdCode">
								<option value="">
									<spring:message code="LABEL_SELECT" text="--Please Select--" />
								</option>
								<c:set var="lang" value="${language}"></c:set>
								<c:forEach items="${countryList}" var="country">
									<c:forEach items="${country.countryNames}" var="cNames">
										<c:if test="${cNames.comp_id.languageCode==lang }">
											<option
												value="${country.isdCode},${country.mobileNumberLength}">${cNames.countryName}</option>
										</c:if>
									</c:forEach>
								</c:forEach>
							</select> --%>
						</div>
						</div>
					</div> 
					<div class="row">
						<div class="col-md-12">
							<label class="col-sm-6"><spring:message code="LABEL_TXN_TYPE"
									text="Txn Type" /><font color="red">*</font></label> 
									<div class="col-sm-5">
									<select  onchange="getval(this);"
								name="transactionType" id="transactionType" class="dropdown chosen-select transactionType">
								<option value="">
									<spring:message code="LABEL_SELECT" text="Please select" />
								</option>
								<authz:authorize ifAnyGranted="ROLE_bankteller,ROLE_bankadmin,ROLE_businesspartnerL1,ROLE_businesspartnerL2">
								<authz:authorize ifAnyGranted="ROLE_bankteller,ROLE_businesspartnerL1,ROLE_businesspartnerL2">
								<option value="30">
									<spring:message code="LABEL_TITLE_BALANCE"
										text="Balance Enquiry" />
								</option>
								<%-- <authz:authorize ifAnyGranted="ROLE_bankteller">
									<option value="95"><spring:message
										code="LABEL_DEPOSIT" text="Deposit" /></option>
										<form:option value="24"> <spring:message code="LABEL_BUSINESS_PARTNER_L1" text="Business Partner L1"/></form:option>
													
								</authz:authorize> --%>
									<option value="35"><spring:message
										code="LABEL_STATEMENT" text="Ministatement" /></option>
									<option value="98"><spring:message
										code="LABEL_TXN_STMT" text="Txn Statement" /></option>
									<authz:authorize ifAnyGranted="ROLE_businesspartnerL2">
									<option value="137"><spring:message
										code="LABEL_CASH_IN" text="Cash In" /></option>
									<option value="138"><spring:message
										code="LABEL_CASH_OUT" text="Cash Out" /></option>
									</authz:authorize>
									</authz:authorize>
									<authz:authorize ifAnyGranted="ROLE_bankadmin,ROLE_businesspartnerL1">
									<option value="143"><spring:message
										code="LABEL_TRANSFER_EMONEY" text="Transfer eMoney" /></option>
									</authz:authorize>
								<%-- <authz:authorize ifAnyGranted="ROLE_bankteller">
									<option value="99"><spring:message
										code="LABEL_WITHDRAWAL" text="Withdrawal" /></option>
								</authz:authorize> --%>
								</authz:authorize>
							</select>
							</div>
						</div>
					</div>
						<div class="row">
						<div class="col-md-12 businessCode">
							<c:if test="${roleAccess eq 2}">
							<label class="col-sm-6"><strong><spring:message
										code="LABEL_BUSINESS_PARTNER_PRINCIPAL_AGENT_CODE" text="Principal Agent Code" /></strong><font
								color="red">*</font></label> 
								</c:if>
								<c:if test="${roleAccess ne 2}">
								<label class="col-sm-6"><strong><spring:message
										code="LABEL_BUSINESS_PARTNER_CODE" text="Business Partner Code" /></strong><font
								color="red">*</font></label> 
								</c:if>
								<div class="col-sm-5">
								<input id="businessPartnerCode"
								name="businessPartnerCode" maxlength="10" class="form-control" />
						</div>
												
					</div>

						<div class="col-md-12 businessdet">
							<label class="col-sm-6 "><strong><spring:message
										code="LABEL_AGENT_MOBILE_NO" text="Agent Mobile Number" /></strong><font
								color="red">*</font></label> 
								<div class="col-sm-5 ">
								<input id="mobileNumber"
								name="mobileNumber" maxlength="9" class="form-control" />
								</div>
						
					</div>
					</div>
					<!-- /.box-body -->

					<div class="box-footer">
						<input type="button" class="btn btn-primary pull-right"
							onclick="validate();"
							value="<spring:message code="LABEL_SUBMIT" text="Submit"/>" /> <br />
						<br />
					</div>
				<%-- </form> --%>
</form:form>
			</div><br />

			<!-- /.box-body -->
		</div>
		<!-- /.box -->
	</div>

<%--<table width="1003" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td><jsp:include page="top.jsp" /></td>
	</tr>
	<tr>
		<td>
		<table width="1003" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="159" valign="top">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td><jsp:include page="left.jsp" /></td>
					</tr>
				</table>
				</td>
				<td width="844" align="left" valign="top">
				<table width="98%" border="0" height="400px" align="center"
					cellpadding="0" cellspacing="0">
					<tr height="20px">
						<td width="20%" align="left" valign="top" class="headding"
							style="font-size: 15px; font-weight: bold;"><spring:message
							code="TITLE_WELCOME_TRANSACTION_SERVICE"
							text="Transaction Service" /></td>
					</tr>
					<tr height="50px">
						<td align="center" style="color: #ba0101"><strong><spring:message
							code="${message}" text="" /></strong></td>
					</tr>
					<tr valign="top">
						<td valign="top">
						<form id="selectTransactionForm" action="getAccountDetails.htm"
							method="post" name="transactionForm">
						<table border="0" width="45%"
							style="-moz-border-radius: 5px; -webkit-border-radius: 5px; border: 1px solid #a6a6a7"
							align="center" width="100%" cellpadding="8" cellspacing="8"
							valign="top">
							<tr>

								<td align="left"><strong><spring:message code="LABEL_COUNTRY"
									text="Country:" /><font color="red">*</font></td>
								<td align="left"><select class="dropdown" id="isdCode"
									name="isdCode">
									<option value=""><spring:message code="LABEL_SELECT"
										text="--Please Select--" /> <c:set var="lang"
										value="${language}"></c:set> <c:forEach items="${countryList}"
										var="country">
										<c:forEach items="${country.countryNames}" var="cNames">
											<c:if test="${cNames.comp_id.languageCode==lang }">
												<option value="${country.isdCode},${country.mobileNumberLength}">${cNames.countryName}</option>
											</c:if>
										</c:forEach>
									</c:forEach>
								</select></td>
							</tr>
							<tr>
								<td align="left"><strong><spring:message
									code="LABEL_MOBILE_NO" text="Mobile Number" /></strong><font color="red">*</font></td>
								<td align="left"><input id="mobileNumber"
									name="mobileNumber" maxlength="15" class="text_feild" /></td>
							</tr>
							<tr>
								<td align="left"><span style="color: #FF0000;">&nbsp;</span><strong><spring:message
									code="LABEL_TXN" text="Txn Type" /><font color="red">*</font></td>
								<td align="left"><select name="transactionType" id="transactionType"
									class="dropdown">
									<option value=""><spring:message code="LABEL_SELECT"
										text="Please select" /></option>
									<option value="30"><spring:message
										code="LABEL_TITLE_BALANCE" text="Balance Enquiry" /></option>
										 <authz:authorize ifAnyGranted="ROLE_bankteller">
									<option value="95"><spring:message
										code="LABEL_DEPOSIT" text="Deposit" /></option>
										</authz:authorize>
									<option value="35"><spring:message
										code="LABEL_STATEMENT" text="Ministatement" /></option>
									<option value="98"><spring:message
										code="LABEL_TXN_STMT" text="Txn Statement" /></option>
										 <authz:authorize ifAnyGranted="ROLE_bankteller">
									<option value="99"><spring:message
										code="LABEL_WITHDRAWAL" text="Withdrawal" /></option>
										</authz:authorize>
									
								</select></td>
							</tr>
							<tr height="20px">
								<td colspan="2" align="right"><input type="button"
									onclick="return validate();"
									value="<spring:message code="LABEL_SUBMIT" text="Submit"/>" /></td>
							</tr>
						</table>
						</form>
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		<div id="mobileDiv" style="visibility: hidden" />
		</td>
	</tr>
	<tr>
		<td><jsp:include page="footer.jsp" /></td>
	</tr>
</table> --%>

</body>
</html>
