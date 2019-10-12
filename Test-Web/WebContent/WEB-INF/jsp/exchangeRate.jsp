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
<script type="text/javascript">

function validate() {
	var buyingRate =document.getElementById("buyingRate").value;
	var sellingRate = document.getElementById("sellingRate").value;
	var currencyId= document.getElementById("currencyId").value;   
	//var pattern  =/^\d{1,3}\.\d{1,2}$/;
	var numericPattern=  /^\d{0,6}(\.\d{1,2})?$/gm;
	
	if(currencyId == ""){
		alert("<spring:message code='ALERT_CURRENCY' text='Please select a currency'/>");
		return false;
	}

	
	if( sellingRate < buyingRate)
    {
		alert("Selling rate should be equal to or greater than buying rate"); 
	      return false;
    } 
	if( sellingRate == "" )
    {
		alert("<spring:message code='ALERT_SELLING_RATE' text='Please enter selling rate'/>"); 
	      return false;
    }
	if( sellingRate.charAt(0)== " "||  sellingRate.charAt(sellingRate.length-1) == " ")
    {
		alert("<spring:message code='ALERT_SELLING_RATE_WHITE_SPACES' text='Please remove white spaces'/>"); 
	      return false;
    } 
    else if(sellingRate.search(numericPattern)==-1 || (sellingRate.charAt(0)== '.') ) {
		  alert("<spring:message code='ALERT_NUMBER_VALIDATION' text='Please add valid selling rate'/>");
		  return false;
		  }
	  	if( buyingRate == "" )
	    {
		alert("<spring:message code='ALERT_BUYING_RATE' text='Please enter buying rate'/>"); 
		      return false;
	    } 
		else if(buyingRate.charAt(0) == " "  || buyingRate.charAt(buyingRate.length-1) == " "){
	    	alert("<spring:message code='ALERT_BUYING_RATE_WHITE_SPACES' text='Please remove white spaces'/>");
		    	  return false;
		  }
	    else if(buyingRate.search(numericPattern)==-1){		
	    
	    	alert("<spring:message code='ALERT_NUMBER_VALIDATION_BUYING_RATE' text='Please add valid buying rate'/>");
			  return false;
			  }
		  else if((buyingRate.charAt(0)== '.') ){
			  alert("<spring:message code='ALERT_NUMBER_VALIDATION_BUYING_RATE' text='Please add valid buying rate'/>");
	 		  return false;
		  }
	
	document.showExchangeRateForm.action = "addExchangeRate.htm";
	document.showExchangeRateForm.submit();	
		
}
</script>
</head>
 <body>
	<div class="col-md-12">
		<div class="box">
			<div class="box-header">
				<h3 class="box-title">
					<span><spring:message code="TITLE_EXCHANGE_RATE" text="Exchange Rate" /></span>
				</h3>
			</div>
			<br />
			<div class="col-sm-5 col-sm-offset-5" style="color: #ba0101; font-weight: bold; font-size: 12px;">
					<spring:message code="${message}" text=""/>
			</div>
			<!-- /.box-header -->
			<div class="box-body" style="height:300px;">
			<div class="col-sm-3"></div>
				<!-- form start -->
				<div class="col-sm-6" style="border: 1px solid;border-radius: 15px;">
				<form:form name="showExchangeRateForm" id="showExchangeRateForm" action="showExchangeRateForm.htm"
						method="post" commandName="exchangeRateDTO" class="form-inline">
						<jsp:include page="csrf_token.jsp"/>
						<authz:authorize ifAnyGranted="ROLE_viewExchangeRateAdminActivityAdmin">
						<br/>
					<div class="row">
						<div class="col-md-12">
							<label class="col-sm-5"> <spring:message
									code="LABEL_CURRENCY" text="Currency" /><font
								color="red">*</font></label>
							<form:select path="currencyId" cssClass="dropdown chosen-select">
								<form:option value="">
									<spring:message code="LABEL_SELECT" text="--Please Select--"></spring:message>
								</form:option>
								<form:options items="${currencyList}" itemLabel="currencyName"
									itemValue="currencyId" />

							</form:select>
						</div>
					</div>
					<br/>
						<div class="row">
						<div class="col-md-12">
							<label class="col-sm-5"><spring:message
									code="LABEL_SELLING_RATE" text="Selling Rate"></spring:message><font
								color="red">*</font></label>
							<form:input path="sellingRate" cssClass="form-control"
								maxlength="6" />
							<FONT color="red">
							<form:errors path="sellingRate" /></FONT>
						</div>
					</div>
					<br/>
					<div class="row">
							<div class="col-md-12">
							<label class="col-sm-5"> <spring:message
									code="LABEL_BUYING_RATE" text="Buying Rate" /><font
								color="red">*</font></label>
							<form:input path="buyingRate" cssClass="form-control"
								maxlength="6" />
							<FONT color="red">
							<form:errors path="buyingRate" /></FONT>
						</div>
					</div>
					
					
						 <!-- /.box-body -->

						<div class="box-footer">
							
							<input type="button" id="submitButton"
								value="<spring:message code="LABEL_SUBMIT" text="Submit"/>"
								onclick="validate();" class="btn btn-primary pull-right" ></input>
							
						</div><br>
						</authz:authorize>
				</form:form>
				</div>
			</div>
			</div>
			<div class="box">
			<div class="box-body">
				<div class="box-body table-responsive">
					<table id="example1" class="table table-bordered table-striped"
						style="text-align: center;">
						<thead>
							<tr>
								<th style="text-align: center;"><spring:message code="LABEL_CURRENCY"
										text="Currency" /></th>
								<th style="text-align: center;"><spring:message code="LABEL_SELLING_RATE" text="Selling Rate"/></th>
								<th style="text-align: center;"><spring:message code="LABEL_BUYING_RATE"
										text="Buying Rate" /></th>
							</tr>
						</thead>
						<tbody>
							<%
								int i = 0;
							%>
						 	<c:forEach items="${page.results}" var="exchangeRate">
								<tr>
									<td><c:out value="${exchangeRate.currency.currencyName}" /></td>
									<td><c:out value="${exchangeRate.sellingRate}" /></td>
									<td><c:out value="${exchangeRate.buyingRate}" /></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
			<!-- /.box-body -->
		</div>
		<!-- /.box -->
	</div>
	</body>
</html>
