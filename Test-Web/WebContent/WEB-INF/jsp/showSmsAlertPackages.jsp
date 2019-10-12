<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>

<html>
<head>
<style type="text/css">

.add_rule_size {
    margin-top: -17px;
    float: right;
    width: 12%;
}
div#ruleLevels {
    margin-top: 10px;
}
.box-header {
    margin-bottom: -19px;
}
</style>
<script type="text/javascript">
	function validateSearch(){
			//var subscriptionType = document.getElementById("subscription").value;
			document.viewSmsAlertPackagesForm.action="searchSmsAlertRules.htm";
			document.viewSmsAlertPackagesForm.submit();

		}
	
	function viewSMS(url,smsAlertRuleId){
		document.getElementById('smsAlertRuleId').value=smsAlertRuleId;
		submitlink(url,'viewSmsAlertPackagesForm');
	}
	
</script>
</head>
<body>
<div class="col-lg-12">
<div class="box">
	<div class="box-header">
		<h3 class="box-title">
			<span><spring:message code="LABEL_MSG_ALERT_PACKAGES" text="SMS Alert Packages"/></span></span>
		</h3>
	</div><br/>
	<form:form id="viewSmsAlertPackagesForm" name="viewSmsAlertPackagesForm" action="searchSmsAlertPackages.htm" method="post" commandName="smsAlertDTO">    
 		<input name="smsAlertRuleId" type="hidden" id="smsAlertRuleId" value="${smsrules.smsAlertRuleId}"/>
 		<jsp:include page="csrf_token.jsp"/>
			<div class="col-md-3 col-md-offset-10 add_rule_size">
				<%-- <a href="javascript:submitForm('viewSmsAlertPackagesForm','showSMSAlertForm.htm')"><b><spring:message code="LABEL_ADD_RULE" text="Add Rule"></spring:message></b>
				</a> --%>
				<input type="button" class="btn btn-primary"
							value="<spring:message code="LABEL_ADD_RULE" text="Add Clearing House"/>"
					onclick="javascript:submitForm('viewSmsAlertPackagesForm','showSMSAlertForm.htm')" />
			</div>
		
	<div class="box-body">
			<div class="row">
				<div class="col-sm-7" id="ruleLevels">
					<label class="col-sm-4" style="margin-top:4px;"><spring:message code="LABEL_SUBSCRIPTION_TYPE" text="Subscription Type"/></label> 
					<form:select path="subscriptionType" class="dropdown chosen-select">
       						<form:option value="" selected="true"><spring:message code="LABEL_SELECT" text="select:"/></form:option>
							<form:option value="1"><spring:message code="LABEL_SELECT_DAILY" text="Daily"/></form:option>
							<form:option value="2"><spring:message code="LABEL_SELECT_WEEKLY" text="Weekly"/></form:option>
							<form:option value="3"><spring:message code="LABEL_SELECT_MONTHLY" text="Monthly"/></form:option>
							<form:option value="4"><spring:message code="LABEL_SELECT_YEARLY" text="Yearly"/></form:option>
      				</form:select>
				</div>
			
			</div>
			
			<div class="box-footer" style="padding-bottom:20px;">
				<input type="button" id="submitButton" class="btn btn-primary pull-right" value="<spring:message code="LABEL_SEARCH" text="Submit"/>" onclick="validateSearch();" style="margin-right: 15px;"/>
				
			</div>
			
			</div>
</form:form>
</div>

			<div class="box">
				<div class="box-body table-responsive">
					<table id="example1" class="table table-bordered table-striped" style="text-align:center;">
						<thead>
							<tr>
								<th><spring:message code="LABEL_PACKAGE_NAME" text="Package Name" /></th>
								<th><spring:message code="LABEL_TXNTYPE" text="TxnType"/></th>
								<th><spring:message code="LABEL_SUBSCRIPTION_TYPE" text="Subscription Type"/></th> 
                                <th><spring:message code="LABEL_COST_PER_SMS" text="Cost Per SMS"/></th>
								<th><spring:message code="LABEL_COST_PER_PACKAGE" text="Cost Per Package"/></th>
								<th><spring:message code="LABEL_NUMBER_OF_SMS" text="Number Of SMS"/></th>
								<th><spring:message code="LABEL_ACTION" text="Action"/></th>
							</tr>
						</thead>
						<tbody>
						<c:set var="j" value="${ (page.currentPage - 1) * page.resultsPerPage }"></c:set>
						<c:forEach items="${page.results}" var="smsrules">
							<c:set var="j" value="${ j+1 }"></c:set>
							<tr>
								<td>
									<c:out value="${smsrules.smsAlertRuleName}"></c:out>
								</td>
								<td>
								<c:set var="lang" value="${language}"></c:set>
								<c:forEach items="${smsrules.smsalertrulestxns}" var="txnType">
									<c:forEach items="${txnType.transactionType.transactionTypesDescs}" var="transactionTypesDescs">
									 <c:if test="${transactionTypesDescs.comp_id.locale==lang}">
											<c:out 	value="${transactionTypesDescs.description}"/>
											</c:if>		 
									</c:forEach>
									<br/>
								</c:forEach>
								</td>
								<td>
								<c:forEach items="${smsrules.smsalertrulevalues}" var="smsrulval">
									<c:if test="${ smsrulval.subscriptionType == 1  }"><spring:message code="LABEL_SELECT_DAILY" text="Daily"/></c:if>
									<c:if test="${ smsrulval.subscriptionType == 2  }"><spring:message code="LABEL_SELECT_WEEKLY" text="Weekly"/></c:if>
									<c:if test="${ smsrulval.subscriptionType == 3  }"><spring:message code="LABEL_SELECT_MONTHLY" text="Monthly"/></c:if>
									<c:if test="${ smsrulval.subscriptionType == 4  }"><spring:message code="LABEL_SELECT_YEARLY" text="Yearly"/></c:if>
									<br/>
								</c:forEach>
								</td>
								<td><c:out value="${smsrules.costPerSms}"></c:out></td>
								<td>
								<c:forEach items="${smsrules.smsalertrulevalues}" var="smsrulval">
									<c:out value="${smsrulval.costPerPackage}">
									</c:out>
									<br/>
								</c:forEach>
								</td>
								<td>
									<c:forEach items="${smsrules.smsalertrulevalues}" var="smsrulval">
										<c:out value="${smsrulval.numberOfSms}">
										</c:out>
										<br/>
									</c:forEach>
								</td>
								<td>
								<%-- <a href="javascript:submitForm('viewSmsAlertPackagesForm','getSmsAlertRule.htm?smsAlertRuleId=<c:out value="${smsrules.smsAlertRuleId}"/>')"><spring:message code="LABEL_EDIT" text="Edit"/></a> --%>
								<a href="javascript:viewSMS('getSmsAlertRule.htm','<c:out value="${smsrules.smsAlertRuleId}"/>')"><spring:message code="LABEL_EDIT" text="Edit"/></a>
								</td>
							</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
</div>	
</body>
</html>