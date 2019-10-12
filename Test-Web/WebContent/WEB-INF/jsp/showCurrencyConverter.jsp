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

function validateShowCurrencyConverterForm() {
	var baseCurrencyId =document.getElementById("baseCurrencyId").value;
	var counterCurrencyId = document.getElementById("counterCurrencyId").value;
	var conversionRate= document.getElementById("conversionRate").value;   
	var numericPattern=  /^\d{0,3}(\.\d{1,2})?$/gm;
	
	if(baseCurrencyId == ""){
		alert("<spring:message code='ALERT_CURRENCY_CONVERTOR' text='Please select a Base currency'/>");
		return false;
	}

	if( counterCurrencyId == "" )
    {
		alert("<spring:message code='ALERT__COUNTER_CURRENCY' text='Please select counter currency'/>"); 
	      return false;
    } 
	  	if( conversionRate == "" )
	    {
		alert("<spring:message code='ALERT_CONVERSION_RATE' text='Please enter conversion rate'/>"); 
		      return false;
	    } 
		else if(conversionRate.charAt(0) == " "  || conversionRate.charAt(conversionRate.length-1) == " "){
	    	alert("<spring:message code='ALERT_BUYING_RATE_WHITE_SPACES' text='Please remove white spaces'/>");
		    	  return false;
		  }
	    else if(conversionRate.search(numericPattern)==-1){		
	    
	    	alert("<spring:message code='ALERT_NUMBER_VALIDATION_CONVERSION_RATE' text='Please add valid conversion rate'/>");
			  return false;
			  }
		  else if((conversionRate.charAt(0)== '.') ){
			  alert("<spring:message code='ALERT_NUMBER_VALIDATION_CONVERTION_RATE' text='Please add valid conversion rate'/>");
	 		  return false;
		  }
	
	document.showCurrencyConverterForm.action = "addCurrencyConverter.htm";
	document.showCurrencyConverterForm.submit();	
		
}
</script>
</head>
 <body>
	<div class="col-md-12">
		<div class="box">
			<div class="box-header">
				<h3 class="box-title">
					<span><spring:message code="TITLE_CURRENCY_CONVERTOR" text="Currency Convertor" /></span>
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
				<form:form name="showCurrencyConverterForm" id="showCurrencyConverterForm" action="showCurrencyConverterForm.htm"
						method="post" commandName="currencyConvertorDTO" class="form-inline">
						<jsp:include page="csrf_token.jsp"/>
						<authz:authorize ifAnyGranted="ROLE_viewCurrencyConverterAdminActivityAdmin">
						<br/>
					<div class="row">
						<div class="col-md-12">
							<label class="col-sm-5"> <spring:message
									code="LABEL_BASE_CURRENCY" text="Base Currency" /><font
								color="red">*</font></label>
							<form:select path="baseCurrencyId" cssClass="dropdown chosen-select">
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
							<label class="col-sm-5"> <spring:message
									code="LABEL_COUNTER_CURRENCY" text="Counter Currency" /><font
								color="red">*</font></label>
							<form:select path="counterCurrencyId" cssClass="dropdown chosen-select">
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
							<label class="col-sm-5"> <spring:message
									code="LABEL_CONVERSION_RATE" text="Conversion Rate" /><font
								color="red">*</font></label>
							<form:input path="conversionRate" cssClass="form-control"
								maxlength="6" />
							<FONT color="red">
							<form:errors path="conversionRate" /></FONT>
						</div>
					</div>
					
					
						 <!-- /.box-body -->

						<div class="box-footer">
							
							<input type="button" id="submitButton"
								value="<spring:message code="LABEL_SUBMIT" text="Submit"/>"
								onclick="validateShowCurrencyConverterForm();" class="btn btn-primary pull-right" ></input>
							
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
								<th style="text-align: center;"><spring:message code="LABEL_BASE_CURRENCY"
										text="Base Currency" /></th>
								<th style="text-align: center;"><spring:message code="LABEL_COUNTER_CURRENCY" text="Counter Currency"/></th>
								<th style="text-align: center;"><spring:message code="LABEL_CONVERSION_RATE"
										text="Conversion Rate" /></th>
							</tr>
						</thead>
						<tbody>
							<%
								int i = 0;
							%>
						 	 <c:forEach items="${page.results}" var="ConversionRate">
								<tr>
									<td><c:out value="${ConversionRate.currencyByBaseCurrencyId.currencyName}" /></td>
									<td><c:out value="${ConversionRate.currencyByCounterCurrencyId.currencyName}" /></td>
									<td><c:out value="${ConversionRate.conversionRate}" /></td>
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
