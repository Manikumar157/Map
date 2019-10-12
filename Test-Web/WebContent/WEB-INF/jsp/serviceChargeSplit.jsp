<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>

<script type="text/javascript">

function submitForm(rowCount,colCount){
	var rowCount=parseInt(rowCount);
	var colCount=parseInt(colCount)+1;
	var arr=document.getElementsByTagName('input');

	var val=0;
	for(var i=0;i<rowCount;i++){
		var sum=0.0;
		for(var j=0;j<colCount;j++){
			sum+=parseInt(arr[val].value);
			val++;
		}
		if(sum!=100.0){
			alert(arr[--val].id+' <spring:message code="ERROR_8006" text="Sum of service charge % has to be 100 for all transaction types."/>');
			return false;
		}
	}
	document.bankManageMentForm.action="saveServiceChargeSplit.htm";
	document.bankManageMentForm.submit();
}


function cancelForm(){
    document.bankManageMentForm.action="showBankManagementForm.htm";
    document.bankManageMentForm.submit();
}
</script>
<title><spring:message code="LABEL_TITLE" text="EOT MOBILE" /></title>

<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/css/calendar-system.css" />

</head>

<body>
<table width="1003" border="0" align="center" cellpadding="0"
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
						<td align="left" class="headding" colspan="2"
							style="font-size: 15px; font-weight: bold;"><spring:message
							code="LABEL_TITLE_BANKSCSPLITS" text="Service Charge Percentages" />
						- <c:out value="${bank.bankName }" /><br />
						</td>
					</tr>
					<tr height="20px">
						<td align="left" style="color: #ba0101"><strong><spring:message
							code="${message}" text="" /></strong></td>
					</tr>
					<tr>
						<td>
						<form name="bankManageMentForm" action="saveServiceChargeSplit.htm"
							method="post">
						<table border="0"
							style="-moz-border-radius: 5px; -webkit-border-radius: 5px; border: 1px solid #a6a6a7"
							align="center" width="100%" cellpadding="4" cellspacing="4">
							<tr>
								<td>
									<table width="100%" cellpadding="0" cellspacing="0"><tr><td align="right">
										<b><a href="editBankForm.htm?bankId=${bank.bankId}">
											<spring:message code="LABEL_VIEW_BANK" text="View Bank"></spring:message>
										</a></b>
										</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>
									<table align="center" width="100%" cellpadding="4" cellspacing="4">
												
										<tr>
											<td></td>
											<c:set var="colCount" value="0"></c:set>
											<c:forEach items="${stakeHolderList}" var="sth" varStatus="ccnt">
												<c:set var="colCount" value="${ccnt.count}"></c:set>
												<td align="center" class="headding"
													style="font-size: 10px; font-weight: bold;"><c:out
													value="${sth.stakeholderOrganization}"></c:out></td>
											</c:forEach>
											<td align="center" class="headding"
													style="font-size: 10px; font-weight: bold;"><c:out
													value="${bank.bankName}"></c:out></td>
										</tr>
										<c:set var="rowCount" value="0"></c:set>
										<c:forEach items="${txnTypes}" var="txn" varStatus="rcnt">
											<c:set var="rowCount" value="${rcnt.count}"></c:set>
											<tr>
												<td><c:out value="${txn.description}" /></td>
												<c:forEach items="${stakeHolderList}" var="sth" varStatus="cnt">
													<c:set var="key"
														value="${sth.account.accountNumber}-${txn.transactionType}"></c:set>
													<td align="center" ><input style="text-align: right"
														class="small_text_feild" name="<c:out value="${key}"/>"
														value="<c:out value="${serviceChargeMap[key]}"/>" type="text" id="${txn.description}"/>
													</td>
												</c:forEach>
												<td>
												<c:set var="bankKey"
														value="${bank.account.accountNumber}-${txn.transactionType}"></c:set>
												<input style="text-align: right"
														class="small_text_feild" name="<c:out value="${bankKey}"/>"
														value="${serviceChargeMap[bankKey]}" type="text" id="${txn.description}"/>
												</td>
											</tr>
										</c:forEach>
										
								</table>
								</td>
							</tr>
								<tr>
											<td colspan="3"><input type="hidden" name="bankId"
												value="${bank.bankId}" /></td>
											
										</tr>
							<tr>
								<td>
									<table width="100%" cellpadding="0" cellspacing="0"><tr><td align="right">
										<input type="button" onclick="submitForm('<c:out value="${rowCount}"/>','<c:out value="${colCount}"/>');" value="<spring:message code="BUTTON_SUBMIT" text="Submit" />"></input>
										<input type="button" value="<spring:message code="LABEL_CANCEL" text="Cancel"/>" onclick="cancelForm();" />
										</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						</form>
						</td>
					</tr>
				</table>
				</td>
		</table>
		</td>
	</tr>
	
</table>
</td>
</tr>
<br>

<tr>
	<td><jsp:include page="footer.jsp" /></td>
</tr>
</table>
</body>
</html>