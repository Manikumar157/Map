<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="authz"
	uri="http://www.springframework.org/security/tags"%>
<html>
<head>
<title><spring:message code="LABEL_TITLE" text="EOT Mobile" /></title>
<%-- <script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script> --%>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.5.1.min.js"></script>
<script type="text/javascript">


	function validate(){
		
		// var mnumber=document.getElementById("mobileNumber").value;
		// var businessPartnerCode=document.getElementById("businessPartnerCode").value;
	//	 var isdNum=isdCode.split(",");
	//	 var mobileNumLen=isdNum[1];
		 var numPattern='^[0-9]*$';

		
       
        /* if(document.getElementById("transactionType").value == 95 || document.getElementById("transactionType").value == 99){
    		document.transactionForm.action ="getBusinessPartnerDetails.htm";
    		document.transactionForm.submit();
        } 	 
        else{ */
        var url = "confAccountToAccount.htm";
        submitForm('transactionForm',url);
        
	
		// }
				}

/* 	$(".transactionType").change(function() {
		var transactionType = document.getElementById("transactionType").value ;

		if( transactionType == 137 ||  transactionType == 138 ){
			$(".businessdet").show();
			$(".businessCode").hide();

		}else{
			$(".businessdet").hide();
			$(".businessCode").show();
		}
				
	}); */

		/* function getval(sel)
		{
			var transactionType = document.getElementById("transactionType").value ;

			if( transactionType == 137 ||  transactionType == 138 ){
				$(".businessdet").show();
				$(".businessCode").hide();

			}else{
				$(".businessdet").hide();
				$(".businessCode").show();
			}
		} */
	 /* $(document).ready(function() {		
			var transactionType = document.getElementById("transactionType").value ;
			$("#bank_chosen").css("width","180px")	;
			
			if( transactionType == 137 ||  transactionType == 138 ){
				 	$(".businessdet").show();
					$(".businessCode").hide(); 

				}else{
					 $(".businessdet").hide();
					$(".businessCode").show(); 
				}
		}); */
</script>

</head>
<body>
	<div class="col-md-12">
		<div class="box" style="height: 350px;">
			<div class="box-header">
				<c:if test="${roleAccess eq 2}">
					<h3 class="box-title">
						<span><spring:message
								code="TITLE_WELCOME_PRINCIPAL_AGENT_TRANSACTION_SERVICE"
								text="View Principal Agent Transactions" /></span>
					</h3>
				</c:if>
				<c:if test="${roleAccess ne 2}">
					<h3 class="box-title">
						<span><spring:message
								code="TITLE_WELCOME_BUSINESS_PARTNER_TRANSACTION_SERVICE"
								text="View BusinessPartner Transactions" /></span>
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
				style="border: 1px solid; border-radius: 15px;">
				<br />
				<form:form class="form-inline"
					action="getBusinessPartnerDetails.htm" method="post"
					name="transactionForm" id="transactionForm">
					<%-- <form:form name="transactionForm" commandName="accountDetailsDTO" id="transactionForm" action="getBusinessPartnerDetails.htm" method="post"> --%>
					<jsp:include page="csrf_token.jsp" />

					<div class="row">
						<div class="col-md-12">
							<label class="col-sm-6"><spring:message
									code="LABEL_COUNTRY" text="Country:" />
								<!-- <font color="red">*</font> --></label>
							<div class="col-sm-5">
								<label class="col-sm-10"><spring:message
										code="LABEL_COUNTRY_SOUTH_SUDAN" text="SouthSudan" /></label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<label class="col-sm-6"><spring:message
									code="LABEL_FROM_ACC" text="From" /><font color="red">*</font></label>
							<div class="col-sm-5">
								<select cssClass="dropdown chosen-select" id="fromAccount" name="fromAccount"
									style="width: 200px;">
									<option value="">
										<spring:message code="LABEL_SELECT" text="--Please Select--"></spring:message>
									</option>
									<%-- <options items="${accountsList}" itemLabel="accountAlias"	itemValue="accountNumber"></options> --%>
									<c:forEach items="${accountsList}" var="accounts">
										<option value="<c:out value="${accounts.accountNumber}"/>">
											<c:out value="${accounts.accountAlias}" />
										</option>
									</c:forEach>
									<select>
										<%-- <font color="red"> <form:errors path="accountNumber" /></font> --%>
							</div>


						</div>

						<div class="col-md-12">
							<label class="col-sm-6"><spring:message
									code="LABEL_TO_ACC" text="To" /><font color="red">*</font></label>
							<div class="col-sm-5">
								<select cssClass="dropdown chosen-select" id="toAccount" name ="toAccount"
									style="width: 200px;">
									<option value="">
										<spring:message code="LABEL_SELECT" text="--Please Select--"></spring:message>
									</option>
									<%-- <options items="${accountsList}" itemLabel="accountAlias"	itemValue="accountNumber"></options> --%>
									<c:forEach items="${accountsList}" var="accounts">
										<option value="<c:out value="${accounts.accountNumber}"/>">
											<c:out value="${accounts.accountAlias}" />
										</option>
									</c:forEach>
									<select>
										<%-- <font color="red"> <form:errors path="accountNumber" /></font> --%>
							</div>


						</div>
					</div>
					<%-- <div class="row">
						<div class="col-md-12 businessCode">
							<c:if test="${roleAccess eq 2}">
								<label class="col-sm-6"><strong><spring:message
											code="LABEL_BUSINESS_PARTNER_PRINCIPAL_AGENT_CODE"
											text="Principal Agent Code" /></strong><font color="red">*</font></label>
							</c:if>
							<c:if test="${roleAccess ne 2}">
								<label class="col-sm-6"><strong><spring:message
											code="LABEL_BUSINESS_PARTNER_CODE"
											text="Business Partner Code" /></strong><font color="red">*</font></label>
							</c:if>
							<div class="col-sm-5">
								<input id="businessPartnerCode" name="businessPartnerCode"
									maxlength="10" class="form-control" />
							</div>

						</div>

						<div class="col-md-12 businessdet">
							<label class="col-sm-6 "><strong><spring:message
										code="LABEL_AGENT_MOBILE_NO" text="Agent Mobile Number" /></strong><font
								color="red">*</font></label>
							<div class="col-sm-5 ">
								<input id="mobileNumber" name="mobileNumber" maxlength="9"
									class="form-control" />
							</div>

						</div>
					</div> --%>
					<div class="box-footer">
						<input type="button" class="btn btn-primary pull-right"
							onclick="validate();"
							value="<spring:message code="LABEL_SUBMIT" text="Submit"/>" /> <br />
						<br />
					</div>
				</form:form>
			</div>
			<br />
		</div>
	</div>
</body>
</html>
