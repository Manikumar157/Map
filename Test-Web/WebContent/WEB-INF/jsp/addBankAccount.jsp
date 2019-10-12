<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>

<head>
<script type="text/javascript"> 

  	function validate(){
  		
  		var accountNumber = document.getElementById("customerBankAccountNumber").value;
  		var accAlias = document.getElementById("accountAlias").value;
  		var branch = document.getElementById("branchId").value;
  		var numpattern='^[0-9]*$'; 
  		var namepattern='^[A-Za-zÀ-ÿ ]*$';
  		
  		if(accountNumber==""){
            alert("<spring:message code="VALIDATION_ACCOUNTNUMBER_EMPTY" text="Please enter Account number"/>");
            return false;
        }else if(accountNumber.length > 11){
            alert("<spring:message code="VALIDATION_ACCOUNTNUMBER_LENGTH" text="Account number should not exceed more than 255 characters"/>");
            return false;
        }else if(accountNumber.search(numpattern)==-1){
            alert("<spring:message code="VALIDATION_ACCOUNTALIAS_DIGITS" text="Please enter Account number only in digits"/>");
            return false;
        }else if(accAlias==""){
            alert("<spring:message code="VALIDATION_ACCOUNTALIAS_EMPTY" text="Please enter account alias"/>");
            return false;
        }else if(accAlias.charAt(0) == " " || accAlias.charAt(accAlias.length-1) == " "){
            alert("<spring:message code="VALIDATION_ACCOUNTALIAS_SPACE" text="Please Remove Unwanted Spaces in account alias"/>");
            return false;
        }else if(accAlias.search(namepattern)==-1){
            alert("<spring:message code="VALIDATION_ACCOUNTALIAS_DIGITS" text="Please enter Account alias with out any special characters"/>");
            return false;
        }else if(accAlias.length > 16){
            alert("<spring:message code="VALIDATION_ACCOUNTALIAS_LENGTH" text="Account alias should not exceed more than 16 characters"/>");
            return false;
        }else if(branch==""){
            alert("<spring:message code="VALIDATION_BRANCH_EMPTY" text="Please select branch"/>");
            return false;
        }       
  		else{
  			document.getElementById('branchId').disabled = false;
  		  	document.getElementById('customerBankAccountNumber').disabled = false;
  		  document.addBankAccountForm.action="saveCustomerBankAccount.htm";
  		  document.addBankAccountForm.submit();
        }
    }
  	
function cancelForm(){
document.addBankAccountForm.action="addBankAccount.htm";
document.addBankAccountForm.submit();
}

function disableSelect(){
  	document.getElementById('branchId').disabled = true;
  	document.getElementById('customerBankAccountNumber').disabled = true;
}
</script>

<title><spring:message code="LABEL_TITLE" text="GIM MOBILE" /></title>

</head>

<style>
.view_cus {
    text-align: right;
}
.add_bank {
    padding: 20px;
}
</style>
<body>
<div class="col-md-12">
		<div class="box" style="height:430px;">
			<div class="box-header">
			<h3 class="box-title">
					<span><spring:message code="LABEL_ADD_BANK_ACCOUNT" text="Add Bank Account" /></span>
				</h3>
			</div>
			<br />
			<div class="col-sm-6 col-sm-offset-3 table_border">
						<div style="color: #ba0101; font-weight: bold; font-size: 12px;">
							<spring:message code="${message}" text="" />
						</div>
					<form:form name="addBankAccountForm" action="saveCustomerBankAccount.htm" method="post" commandName="bankAccountDTO">
						
						<div class="add_bank">
					<!-- form start -->
						<div class="col-md-12 view_cus">
										<label>
										 <a href="viewCustomer.htm?customerId=<c:out value="${bankAccountDTO.customerId}"/>"><spring:message code="LABEL_VIEW_CUSTOMER" text="View Customer"/> </a>
									    </label>
									</div>
								<div class="col-md-12">
									<label class="col-sm-6">
									<spring:message code="LABEL_BANK_ACC_NUMBER" text="Account Number" /><font color="red">*</font>
									</label>
									<div class="col-md-6"><form:input path="customerBankAccountNumber" id="customerBankAccountNumber"
										cssClass="text_feild form-control" maxlength="11"/><FONT color="red"><form:errors
										path="customerBankAccountNumber" /></FONT></div>
							</div>
								<div class="col-md-12">
							<label class="col-sm-6"><spring:message code="LABEL_BANK_ACC_ALIAS"
										text="Account alias" /><font color="red">*</font></label>
									<div class="col-md-6"><form:input path="accountAlias" id="accountAlias"
										cssClass="text_feild form-control" maxlength="30"/><FONT color="red"><form:errors
										path="accountAlias" /></FONT></div>
								</div>
								<div class="col-md-12">
									<label class="col-sm-6"><spring:message code="LABEL_BRANCH" text="Branch" /><font color="red">*</font></label>
									<div class="col-md-6">
										<form:select id="branchId" cssClass="dropdown" path="branchId">
																	<form:option value="">
															<spring:message code="LABEL_SELECT" text="--Please Select--"></spring:message>
															<form:options items="${branchList}" itemValue="branchId" itemLabel="location"></form:options>
														</form:option>
										</form:select>
										<font color="RED"><form:errors path="branchId" cssClass="" /></font>
										<form:hidden path="accountHolderName" />
										<form:hidden path="referenceType" />
									</div>
								</div>
                                 <form:hidden path="slNo" />
                                 <form:hidden path="customerId" />
								<div class="col-md-12">
							<label class="col-sm-6">
									<spring:message code="LABEL_STATUS" text="Status" />
							</label>
									<div class="col-md-6">
									<form:radiobutton path="status" value="2" ></form:radiobutton><spring:message code="LABEL_ACTIVE" text="Active" />
									<form:radiobutton path="status" value="3"></form:radiobutton><spring:message code="LABEL_INACTIVE" text="In Active" />
									</div>
							</div>

								
									<c:choose>
										<c:when test="${(bankAccountDTO.slNo eq null) }">
											<c:set var="buttonName" value="LABEL_ADD" scope="page" />
										</c:when>
										<c:otherwise>
										<script>disableSelect();</script>
											<c:set var="buttonName" value="LABEL_UPDATE" scope="page" />
										</c:otherwise>
									</c:choose>
									<div class="col-sm-6 col-sm-offset-9">
									<input type="button"
										id="submitButton" class="btn btn-primary"
										value="<spring:message code="${ buttonName }" text="${ buttonName }"/>"
										onclick="validate();" /> 
										<c:if test="${bankAccountDTO.slNo ne null}">
									<input type="button" value="<spring:message code="LABEL_CANCEL" text="Cancel"/>" onclick="cancelForm();" />
									</c:if>
									</div></div>
									</form:form>
									</div></div>
								<div class="box">
				<div class="box-body table-responsive">
					<table id="example1" class="table table-bordered table-striped" style="text-align:center;">
												<tr class="tableheading">
													<th align="left" width="15%"><spring:message code="LABEL_BANK_ACC_NUMBER" text="Account Number" /></th>
													<th align="center" width="15%"><spring:message code="LABEL_ACCOUNT_ALIAS"  text="Account Alias"/></th>
													<th align="center" width="15%"><spring:message code="LABEL_STATUS"  text="Status"/></th>
													<th align="center" width="15%"><spring:message code="LABEL_EDIT"  text="Edit"/></th>
												</tr>
												<%int i=0; %>
												
												<c:forEach items="${page.results}" var="custBankAcc">
													
													<tr height="23px" class="" <% if(i%2!=0)%> bgColor="#d2d3f1" <% i++; %>>
														<td align="left"><c:out value="${custBankAcc.bankAccountNumber}" /></td>	
														<td align="center"><c:out value="${custBankAcc.alias}" /></td>
														
														<c:if test="${custBankAcc.status==2}">
														<c:set var="status" ><spring:message code="LABEL_ACTIVE" text="Active"/></c:set>
													</c:if>
													<c:if test="${custBankAcc.status==3}">
														<c:set var="status" ><spring:message code="LABEL_DEACTIVATE" text="Inactive"/></c:set>
													</c:if>
													<td align="center"><c:out
														value="${status}" /></td>
														 <td align="center">
															<a href="editCustomerBankAccount.htm?slNo=<c:out value="${custBankAcc.slno}"/>&referenceId=<c:out value="${custBankAcc.referenceId}"/>">
																<spring:message	code="LABEL_EDIT" text="Edit" />
															</a>
														</td>
													</tr>
												</c:forEach>
												<tr  bgcolor="#30314f" style="color:white;">
										 			<c:if test="${page.totalPages>1}">
										            <td colspan="7" align="right" height="25px">
										            <a	<c:if test="${page.currentPage!='1'}">href="<c:out value="${page.requestPage}"/>?customerId=<c:out value="${bankAccountDTO.customerId}" />&pageNumber=<c:out value="1" />" </c:if> style="color:white;"><c:out value="[ First " /></a>
													<a	<c:if test="${page.currentPage!='1'}">href="<c:out value="${page.requestPage}"/>?customerId=<c:out value="${bankAccountDTO.customerId}" />&pageNumber=<c:out value="${page.currentPage-1}" />" </c:if> style="color:white;"><c:out value=" / Prev ]"/></a> 
													<c:forEach var="i" begin="${page.startPage}" end="${page.endPage}" step="1">
													<c:if test="${page.currentPage!=i}"> <a href="<c:out value="${page.requestPage}"/>?&customerId=<c:out value="${bankAccountDTO.customerId}" />&pageNumber=<c:out value="${i}" />" style="color:white;"><c:out value="${i}" /></a></c:if>
													<c:if test="${page.currentPage==i}"><b><c:out value="${i}" /></b></c:if>
													</c:forEach> 
													<a <c:if test="${page.currentPage<page.totalPages}">href="<c:out value="${page.requestPage}"/>?customerId=<c:out value="${bankAccountDTO.customerId}" />&pageNumber=<c:out value="${page.currentPage+1}" />"</c:if> style="color:white;"><c:out value="[ Next / " /></a> 
													<a <c:if test="${page.totalPages!=page.currentPage}">href="<c:out value="${page.requestPage}"/>?customerId=<c:out value="${bankAccountDTO.customerId}" />&pageNumber=<c:out value="${page.totalPages}" />"</c:if> style="color:white;"><c:out value="Last ]" /></a>
										            </td>
										            </c:if>
										 		</tr>
											</table>
										</div>
										</div>
		</div></div>
</body>
</html>
