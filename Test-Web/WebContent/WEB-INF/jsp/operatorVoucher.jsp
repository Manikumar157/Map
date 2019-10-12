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
<title><spring:message code="LABEL_TITLE" text="EOT MOBILE" /></title>

<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/css/calendar-system.css" />
</head>
<body>
	<table width="1003" border="0" align="center" cellpadding="0" cellspacing="0">
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
						<td width="844"  align="left" valign="top">
							<table width="100%" border="0" height="400px" align="center" cellpadding="0" cellspacing="0">
								<tr height="20px">
									<td align="left" class="headding" width="40%"
										style="font-size: 15px; font-weight: bold;"><c:out
										value="${operatorName.operatorName}"></c:out>-<spring:message
										code="TITLE_VOUCHER" text="View Voucher Details" /></td>
									<td align="right" class="headding"
										style="font-size: 10px; font-weight: bold;"><a
										href="editOperator.htm?operatorId=${denominationDTO.operatorId}"><spring:message
										code="LINK_VIEW_OPERATOR" text="View Operators" /></a></td>
								</tr>
								<tr height="20px">
									<td colspan="2"></td>
								</tr>
								<tr>
									<td valign="top" colspan="2">
									<table width="90%" align="center"
										style="-moz-border-radius: 5px; -webkit-border-radius: 5px; border: 1px solid #a6a6a7"
										cellpadding="0" cellspacing="0" border="0">
										<tr class="tableheading">
											<th align="center"><spring:message
												code="LABEL_VOUCHERSERIALNUMBER" text="VoucherSerialNumber" /></th>
											<th align="center"><spring:message
												code="LABEL_VOUCHERNUMBER" text="VoucherNumber" /></th>
											<th align="center"><spring:message
												code="LABEL_STATUS" text="Status" /></th>
											<th align="center"><spring:message
												code="LABEL_OPERATOR" text="Operator" /></th>
											<th align="center"><spring:message
												code="LABEL_DENOMINATION" text="Denomination" /></th>
										</tr>
										<%int i=0; %>
										<c:forEach items="${vouchers}" var="vouchers">
											<tr height="23px" class="" <% if(i%2!=0)%> bgColor="#d2d3f1" <% i++; %>>
												<td align="center" ><c:out
													value="${vouchers.voucherSerialNumber}" /></td>
												<td align="center" ><c:out
													value="${vouchers.voucherNumber}" /></td>
												<c:if test="${vouchers.active==1}">
													<c:set var="status" value="Active"></c:set>
												</c:if>
												<c:if test="${vouchers.active==0}">
													<c:set var="status" value="Inactive"></c:set>
												</c:if>
												<td align="center" ><c:out value="${status}" /></td>
												<td align="center" ><c:out
													value="${vouchers.operator.operatorName}" /></td>
												<td align="center" ><c:out
													value="${vouchers.operatorDenomination.denomination}" /></td>
											</tr>
										</c:forEach>
									</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td><jsp:include page="footer.jsp" /></td>
		</tr>
	</table>
</body>
</html>
