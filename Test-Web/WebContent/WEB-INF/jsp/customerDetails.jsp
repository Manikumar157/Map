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
		<title><spring:message code="LABEL_TITLE" text="EOT Mobile"></spring:message></title>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/customerServices.js"></script>
		
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
							<td width="844" align="left" valign="top">
								<table width="98%" border="0" height="200px" align="center" cellpadding="0" cellspacing="0">
									<tr height="20px">
										<td align="left" class="headding" style="font-size: 15px; font-weight: bold;">
											<spring:message code="TITLE_CUSTOMER_DETAILS" text="Customer Details "> </spring:message>
										</td>
									</tr>
									<tr height="20px">
										<td align="right"><a href="searchCustomer.htm?pageNumber=1"><b><spring:message code="LINK_SEARCH_CUSTOMERS" text="Search Customers"> </spring:message></b></a></td>
									</tr>
									<tr>
										<td colspan="2"  valign="top">
											<table width="760px" border="0" align="center" cellpadding="4" cellspacing="4"
												style="-moz-border-radius: 5px; -webkit-border-radius: 5px; border: 1px solid #a6a6a7">
												
												<tr>
													<td valign="top">
														<table border="0" width="100%" cellpadding="2" cellspacing="2">
															
															<tr>
																<td><b><spring:message code="LABEL_CUSTOMER_NAME" text="Customer Name"/></b></td>
																<td><c:out value="${customerDTO.title} ${customerDTO.firstName} ${customerDTO.middleName} ${customerDTO.lastName}" /> </td>
																<td><b><spring:message code="LABEL_MOBILE_NO" text="MobileNo.:"></spring:message></b></td>
																<td><c:out value="${customerDTO.mobileNumber}" /> </td>
															</tr>
															<tr>
																<td><b><spring:message code="LABEL_GENDER" text="Gender:"/></b></td>
																<td>
																    <c:out value="${customerDTO.gender}" /> 																			
																</td>
																<td><b><spring:message code="LABEL_DOB" text="Date Of Birth:"/></b></td>
																<td><fmt:formatDate pattern="dd-MM-yyyy" value="${customerDTO.dob}"/> </td>
															</tr>
															<tr>
																<td><b><spring:message code="LABEL_SECURE_QUEST" text="Security Question:"/></b></td>
																<td>
																<c:out value="${customerDTO.question}" />													
																</td>
																<td><b><spring:message code="LABEL_ANSWER" text="Answer:"/></b></td>
																<td>
																<c:out value="${customerDTO.answer}" />	
																</td>
															</tr>
															<tr>
																<td><b><spring:message code="LABEL_ACCTYPE" text="Type:"/></b></td>
																<td>
																<c:choose>
																	<c:when test="${customerDTO.type==0}"> <spring:message code="LABEL_CUSTOMER" text="Customer"/> </c:when> 
																	<c:otherwise><spring:message code="LABEL_SOLE_MERCHANT" text="Sole-Merchant"/></c:otherwise>  
																</c:choose>																		
																</td>
																<td><b><spring:message code="LABEL_EMAILID" text="Email Id:"/></b></td>
																<td><c:out value="${customerDTO.emailAddress}" /></td>
															</tr>
															<tr>
																<td><b><spring:message code="LABEL_QUARTER" text="Quarter:"/></b></td>
																<td>
																<c:out value="${customerDTO.quarter}" />																			
																</td>
																<td ><b><spring:message code="LABEL_ADDRESS" text="Address:"/></b></td>
																<td width="100px">
																<c:out value="${customerDTO.address}" />																			
																</td>
															</tr>
															<tr>
																<td><b><spring:message code="LABEL_CITY" text="City:"/></b></td>
																<td>
																	<c:out value="${customerDTO.city}" />																				
																</td>
																<td><b><spring:message code="LABEL_COUNTRY" text="Country :"/></b></td>
																<td>
																	<c:out value="${customerDTO.country}" />																			
																</td>
															</tr>
															<tr>
															<td colspan="4" align="center">
																<b><a href="createAccount.htm?mobileNumber=${mobileNumber}" onclick="return confirm('Do you want to create a GIM Account for this customer ?');">
																	<spring:message code="LABEL_CREATE_ACCOUNT" text="Create Account" /> 
																</a></b>
															</td>
														</tr>																									
															
														</table>
													</td>
												</tr>
											</table>
											<table><tr height="10px"><td></td></tr></table>
											<div id="data">
												<table align="center" border="0">
													<tr height="20px" >
														<td colspan="2" style="color: #ba0101;font-weight: bold;font-size: 12px;" align="center">
															<spring:message code="${message}" text=""/>
														</td>
													</tr>
												</table>
											</div>
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
