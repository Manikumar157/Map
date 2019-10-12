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
<!-- <script type='text/javascript' src='js/jquery-1.5.1.min.js'></script> -->
<%-- 		<script type="text/javascript" src="<%=request.getContextPath()%>/js/customerServices.js"></script> --%>
<script type="text/javascript">
		
		var Alertmsg={"ALERT_BLOCK_APP":"<spring:message code='ALERT_BLOCK_APP' text='Do you want to block the application &#63;'/>",
				"ALERT_UNBLOCK_APP":"<spring:message code='ALERT_UNBLOCK_APP' text='Do you want to unblock the application &#63;'/>",
				 "ALERT_ACTIVATE_CUSTOMER":"<spring:message code='ALERT_ACTIVATE_CUSTOMER' text='Do you want to activate the customer &#63;'/>",
				 "ALERT_DEACTIVATE_CUSTOMER":"<spring:message code='ALERT_DEACTIVATE_CUSTOMER' text='Do you want to de-activate the customer &#63;'/>",
				 "ALERT_RESET_CUST_PIN" : "<spring:message code='ALERT_RESET_CUST_PIN' text='Do you want to reset the customer PIN &#63;'/>",
				 "ALERT_RESET_CUST_TXN_PIN" : "<spring:message code='ALERT_RESET_CUST_TXN_PIN' text='Do you want to reset the customer Transaction PIN &#63;'/>",
				 "ALERT_RESCHEDULE_SMS" : "<spring:message code='ALERT_RESCHEDULE_SMS' text='Do you want to re-send the SMS to the customer &#63;'/>",
				 "ALERT_REINIT_REQUEST" : "<spring:message code='ALERT_REINIT_REQUEST' text='Do you want to re-initiate the request &#63;'/>",
				 "ALERT_REINSTALL_APP" : "<spring:message code='ALERT_REINSTALL_APP' text='Do you want to re-install the application &#63; (This will disable the existing application.)'/>"
				 };	
			 
			 setTimeout("showHideDiv()", 600000);// 10 minutes
			 
			 function showHideDiv() {
			        var link = document.getElementById('link');
			        link.style.display = "none";
			}
		
		</script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/customerServices.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/common.js"></script>

<style type="text/css">
<!--
.style1 {
	color: #FFFFFF;
	font-weight: bold;
}

-->
th {
	text-align: center;
}

textarea:active {
	border-color: black;
}
</style>
<style>
td {
	text-align: center;
}
</style>
</head>
<body>
	<div class="row">
		<div class="col-lg-12">
			<div class="box">
				<div class="box-header">
					<h3 class="box-title">
						<c:choose>
							<c:when test="${customerDTO.type==3}">
								<span> <spring:message
										code="TITLE_AGENT_SOLE_MERCHANT_DETAILS"
										text="Agent Sole Merchant Details" />
								</span>
							</c:when>
							<c:when test="${customerDTO.type==1}">
								<spring:message code="TITLE_AGENTS_DETAILS" text="Agent Details" />
							</c:when>
							<c:when test="${customerDTO.type==2}">
								<spring:message code="TITLE_SOLE_MERCHANT_DETAILS"
									text="Sole-Merchant Details" />
							</c:when>
						</c:choose>
					</h3>

				</div>
				<div class="col-sm-5 col-sm-offset-4">
					<div class="col-sm-12"
						style="color: #ba0101; margin: auto; font-weight: bold; font-size: 12px;">
						<c:if test="${message ne null}">
							<c:choose>
								<c:when test="${customerDTO.type==3}">
									<span> <spring:message code="LABEL_AGENT_SOLE_MERCHANT"
											text="Agent Sole Merchant" />
									</span>
								</c:when>
								<c:when test="${customerDTO.type==1}">
									<spring:message code="LABEL_AGENT" text="Agent" />
								</c:when>
								<c:when test="${customerDTO.type==2}">
									<spring:message code="LABEL_SOLE_MERCHANT" text="Merchant" />
								</c:when>
							</c:choose>
							<spring:message code="${message}" text="" />
						</c:if>
					</div>
				</div>
				<form:form id="viewAgentForm">
					<div id="csrfToken">
						<jsp:include page="csrf_token.jsp" />
					</div>
					<div class="col-sm-5 col-sm-offset-9">
						<a
							href="javascript:submitForm('viewAgentForm','searchAgentPage.htm')"><strong>
								<%-- type=<c:out value="${customerDTO.type}" /> --%> <c:choose>
									<c:when test="${customerDTO.type==3}">
										<strong style="margin-left: 50px;"><spring:message
												code="LINK_SEARCH_AGENT_SOLE_MERCHANTS"
												text="Search Agent Sole Merchants" /></strong>
									</c:when>
									<c:when test="${customerDTO.type==1}">
										<strong style="margin-left: 100px;"><spring:message
												code="LINK_SEARCH_AGENTS" text="Search Agents" /></strong>
									</c:when>
									<c:when test="${customerDTO.type==2}">
										<strong style="margin-left: 90px;"><spring:message
												code="LINK_SEARCH_SOLE_MERCHANTS" text="Search Merchants" /></strong>
									</c:when>
								</c:choose>
						</strong></a> &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
					</div>
					<br />
					<br />
					<div class="box-body">
						<c:if test="${customerDTO.type==1 || customerDTO.type==2}">
							<div class="row">
								<div class="col-sm-6">
									<label class="col-sm-5"><spring:message
											code="LABEL_BUSNIESS_NAME" text="Business Name:"></spring:message></label>
									<div class="col-sm-5" style="margin-top: 4px;">
										<c:out value="${customerDTO.businessName} " />
									</div>
								</div>
								<c:if test="${customerDTO.type==2}">
								<div class="col-sm-6">
									<label class="col-sm-5"><spring:message
											code="LABEL_MERCHANT_CODE" text=" Merchant Code"></spring:message></label>
									<div class="col-sm-5" style="margin-top: 4px;">
										<c:out value="${customerDTO.agentCode} " />
									</div>
								</div>
								</c:if>
							</div>
						</c:if>
						<div class="row">
							<div class="col-sm-6">
								<label class="col-sm-5" style="margin-top: 4px;"><spring:message
										code="LABEL_CUSTOMER_TITLE" text="Title" /></label>
								<div class="col-sm-5" style="margin-top: 4px;">
									<c:out value="${customerDTO.title} " />
								</div>
							</div>
							<div class="col-sm-6">
								<label class="col-sm-5" style="margin-top: 4px;"><spring:message
										code="LABEL_CUSTOMER_FNAME" text="First Name:"></spring:message></label>
								<div class="col-sm-5" style="margin-top: 4px;">
									<c:out value="${customerDTO.firstName} " />
								</div>
							</div>
						</div>


						<div class="row">
							<div class="col-sm-6">
								<label class="col-sm-5" style="margin-top: 4px;"><spring:message
										code="LABEL_CUSTOMER_MNAME" text="Middle Name" /></label>
								<div class="col-sm-5" style="margin-top: 4px;">
									<c:out value="${customerDTO.middleName} " />
								</div>
							</div>
							<div class="col-sm-6">
								<label class="col-sm-5" style="margin-top: 4px;"><spring:message
										code="LABEL_CUSTOMER_LNAME" text="Last Name.:"></spring:message></label>
								<div class="col-sm-5" style="margin-top: 4px;">
									<c:out value="${customerDTO.lastName}" />
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<label class="col-sm-5" style="margin-top: 4px;"><spring:message
										code="LABEL_MOBILE_NO" text="MobileNo.:"></spring:message></label>
								<div class="col-sm-5" style="margin-top: 4px;">
									<c:out value="${customerDTO.mobileNumber}" />
								</div>
							</div>


							<div class="col-sm-6">
								<label class="col-sm-5"><spring:message
										code="LABEL_GENDER" text="Gender:" /></label>
								<c:if
									test="${customerDTO.gender eq 'male' || customerDTO.gender eq 'Male'}">
									<c:set var="gender">
										<spring:message code="LABEL_MALE" text="Male" />
									</c:set>
								</c:if>
								<c:if
									test="${customerDTO.gender eq 'female' || customerDTO.gender eq 'Female'}">
									<c:set var="gender">
										<spring:message code="LABEL_FEMALE" text="Female" />
									</c:set>
								</c:if>
								<div class="col-sm-5">
									<c:out value="${gender}" />
								</div>
							</div>

						</div>


						<!--@  Author name <vinod joshi>, Date<7/20/2018>, purpose of change < modified full jsp page because view page and edit page must have in same order > ,
				@  Start-->

						<div class="row">

							<div class="col-sm-6">
								<label class="col-sm-5"><spring:message code="LABEL_DOB"
										text="Date Of Birth:" /></label>
								<div class="col-sm-5">
									<fmt:formatDate pattern="dd-MM-yyyy" value="${customerDTO.dob}" />
								</div>
							</div>
							<%-- <div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_SECURE_QUEST" text="Security Question:"/></label> 
					<div class="col-sm-5"><c:out value="${customerDTO.selectedQuestion}" /></div> 
				</div> --%>
							<div class="col-sm-6">
								<label class="col-sm-5" style="margin-top: 4px;"><spring:message
										code="LABEL_PLACE_OF_BIRTH" text="Place Of Birth:" /></label>
								<div class="col-sm-5" style="margin-top: 4px;">
									<c:out value="${customerDTO.placeOfBirth}" />
								</div>
							</div>
						</div>

						<div class="row">

							<div class="col-sm-6">
								<label class="col-sm-5"><spring:message
										code="LABEL_LANGUAGE" text="Language:" /></label>
								<div class="col-sm-5">
									<c:out value="${customerDTO.language}" />
								</div>
							</div>

							<div class="col-sm-6">
								<label class="col-sm-5"><spring:message
										code="LABEL_PROFILE" text="Profile" /></label>
								<div class="col-sm-5">
									<c:out value="${customerDTO.profileName}" />
								</div>
							</div>

						</div>

						<!-- 			<div class="row"> -->

						<%-- <div class="col-sm-6">
								<label class="col-sm-5"><spring:message
										code="LABEL_SECURE_QUEST" text="Security Question:" /></label>
								<div class="col-sm-5">
									<c:out value="${customerDTO.selectedQuestion}" />
								</div>
							</div>
							<div class="col-sm-6">
								<label class="col-sm-5"><spring:message
										code="LABEL_ANSWER" text="Answer:" /></label>
								<div class="col-sm-5">
									<c:out value="${customerDTO.answer}" />
								</div>
							</div>
 --%>
						<%-- <div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_ACCTYPE" text="Type:"/></label> 
					<div class="col-sm-5" style="margin-top:4px;">
					<c:choose>
						<c:when test="${customerDTO.type==0}"> <spring:message code="LABEL_CUSTOMER" text="Customer"/>
						 </c:when> 
						 <c:when test="${customerDTO.type==1}"> <spring:message code="LABEL_MERCHANT" text="Merchant"/>
						 </c:when> 
						 <c:when test="${customerDTO.type==2}"> <spring:message code="LABEL_SOLE_MERCHANT" text="Sole-Merchant"/>
						 </c:when> 
					</c:choose>
					</div>
				</div> --%>

						<!-- 	</div> -->

						<div class="row">

							<%-- <div class="col-sm-6">
								<label class="col-sm-5" style="margin-top: 4px;"><spring:message
										code="LABEL_ACCTYPE" text="Type:" /></label>
								<div class="col-sm-5" style="margin-top: 4px;">
									<c:choose>
										<c:when test="${customerDTO.type==3}">
											<spring:message code="LABEL_AGENT_SOLE_MERCHANT"
												text="Agent Sole Merchant" />
										</c:when>
										<c:when test="${customerDTO.type==1}">
											<spring:message code="LABEL_AGENT" text="Agent" />
										</c:when>
										<c:when test="${customerDTO.type==2}">
											<spring:message code="LABEL_SOLE_MERCHANT"
												text="Sole-Merchant" />
										</c:when>
									</c:choose>
								</div>
							</div> --%>

							<div class="col-sm-6">
								<label class="col-sm-5" style="margin-top: 4px;"><spring:message
										code="LABEL_EMAILID" text="Email Id:" /></label>
								<div class="col-sm-5" style="margin-top: 4px;">
									<c:out value="${customerDTO.emailAddress}" />
								</div>
							</div>

							<div class="col-sm-6">
								<label class="col-sm-5"><spring:message
										code="LABEL_PROFESSION" text="Profession :" /></label>
								<div class="col-sm-5">
									<c:choose>
										<c:when test="${customerDTO.profession eq 'Doctor'}">
											<spring:message code="LABEL_DOCTOR" text="Doctor"></spring:message>
										</c:when>
										<c:when test="${customerDTO.profession eq 'Employee'}">
											<spring:message code="LABEL_EMPLOYEE" text="Employee"></spring:message>
										</c:when>
										<c:when test="${customerDTO.profession eq 'Farmer'}">
											<spring:message code="LABEL_FARMER" text="Farmer"></spring:message>
										</c:when>
										<c:when test="${customerDTO.profession eq 'Gendarme'}">
											<spring:message code="LABEL_GENDARME" text="Gendarme"></spring:message>
										</c:when>
										<c:when
											test="${customerDTO.profession eq 'Health Staff Magistrate'}">
											<spring:message code="LABEL_HEALTH_STAFF_MAGISTRATE"
												text="Health Staff Magistrate"></spring:message>
										</c:when>
										<c:when test="${customerDTO.profession eq 'Journalist'}">
											<spring:message code="LABEL_JOURNALIST" text="Journalist"></spring:message>
										</c:when>
										<c:when test="${customerDTO.profession eq 'Magistrate'}">
											<spring:message code="LABEL_MAGISTRATE" text="Magistrate"></spring:message>
										</c:when>
										<c:when
											test="${customerDTO.profession eq 'Managing Director'}">
											<spring:message code="LABEL_MANAGING_DIRECTOR"
												text="Managing Director"></spring:message>
										</c:when>
										<c:when test="${customerDTO.profession eq 'Merchant'}">
											<spring:message code="LABEL_MERCHANT" text="Merchant"></spring:message>
										</c:when>
										<c:when test="${customerDTO.profession eq 'Military'}">
											<spring:message code="LABEL_MILITARY" text="Military"></spring:message>
										</c:when>
										<c:when test="${customerDTO.profession eq 'Officer'}">
											<spring:message code="LABEL_OFFICER" text="Officer"></spring:message>
										</c:when>
										<c:when test="${customerDTO.profession eq 'Pharmacist'}">
											<spring:message code="LABEL_PHARMACIST" text="Pharmacist"></spring:message>
										</c:when>
										<c:when test="${customerDTO.profession eq 'Policeman'}">
											<spring:message code="LABEL_POLICE" text="Police"></spring:message>
										</c:when>
										<c:when test="${customerDTO.profession eq 'Retired'}">
											<spring:message code="LABEL_RETIRED" text="Retired"></spring:message>
										</c:when>
										<c:when test="${customerDTO.profession eq 'Sole Merchant'}">
											<spring:message code="LABEL_SOLE_MERCHANT" text="Merchant"></spring:message>
										</c:when>
										<c:when test="${customerDTO.profession eq 'Student'}">
											<spring:message code="LABEL_STUDENT" text="Student"></spring:message>
										</c:when>
										<c:when
											test="${customerDTO.profession eq 'Student(University)'}">
											<spring:message code="LABEL_STUDENT_UNIVERSITY"
												text="Student(University)"></spring:message>
										</c:when>
										<c:when test="${customerDTO.profession eq 'Teacher'}">
											<spring:message code="LABEL_TEACHER" text="Teacher"></spring:message>
										</c:when>
										<c:when test="${customerDTO.profession eq 'Unemplyed'}">
											<spring:message code="LABEL_UNEMPLOYED" text="Unemplyed"></spring:message>
										</c:when>
										<c:when test="${customerDTO.profession eq 'Worker'}">
											<spring:message code="LABEL_WORKER" text="Worker"></spring:message>
										</c:when>
										<c:when test="${customerDTO.profession eq 'Unemplyed'}">
											<spring:message code="LABEL_UNEMPLOYED" text="Unemplyed"></spring:message>
										</c:when>
										<c:when
											test="${customerDTO.others ne null && customerDTO.profession eq 'Others'}">
											<c:out value="${customerDTO.others}" />
										</c:when>
										<c:otherwise>
											<c:out value="${customerDTO.profession}" />
										</c:otherwise>
									</c:choose>
								</div>
							</div>



							<%-- <div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_CITY" text="City:"/></label>
					<div class="col-sm-5"><c:out value="${customerDTO.cityName}" /></div> 
					</div> --%>

						</div>

						<div class="row">


							<div class="col-sm-6">
								<label class="col-sm-5" style="margin-top: 4px;"><spring:message
										code="LABEL_COUNTRY" text="Country :" /></label>
								<div class="col-sm-5" style="margin-top: 4px;">
									<spring:message code="LABEL_COUNTRY_SOUTH_SUDAN"
										text="SouthSudan" />
								</div>
							</div>

							<%-- commenting to make a default country as SouthSudan,
							by vineeth on 12-11-2018
								<div class="col-sm-5" style="margin-top: 4px;">
									<c:set var="lang" value="${language}"></c:set>
									<c:forEach items="${customerDTO.customerCountry.countryNames}"
										var="cname">
										<c:if test="${cname.comp_id.languageCode==lang }">
											<c:out value="${cname.countryName}" />
										</c:if>
									</c:forEach>
								</div>
							</div> --%>

							<div class="col-sm-6">
								<label class="col-sm-5"><spring:message
										code="LABEL_CITY" text="City:" /></label>
								<div class="col-sm-5">
									<c:out value="${customerDTO.cityName}" />
								</div>
							</div>
						</div>

						<div class="row">
							<%-- <div class="col-sm-6">
								<label class="col-sm-5"><spring:message
										code="LABEL_ADDRESS" text="Address:" /></label>

								<textarea class="col-sm-6" rows="3" cols="50"
									readonly="readonly"
									style="overflow: hidden; resize: none; border: none; outline: none;"><c:out value="${customerDTO.address}" /></textarea>								
							</div> --%>

							<div class="col-sm-6">
								<label class="col-sm-5" style="margin-top: 4px;"><spring:message
										code="LABEL_ADDRESS" text="Address:" /></label>
								<div class="col-sm-5" style="margin-top: 4px;">
									<c:out value="${customerDTO.address} " />
								</div>
							</div>

							<div class="col-sm-6">
								<label class="col-sm-5" style="margin-top: 4px;"><spring:message
										code="LABEL_QUARTER" text="Quarter:" /></label>
								<div class="col-sm-5" style="margin-top: 4px;">
									<c:out value="${customerDTO.quarterName}" />
								</div>
							</div>

						</div>

						<div class="row">

							<%-- <div class="col-sm-6">
								<label class="col-sm-5"><spring:message
										code="LABEL_BANK_CUSTOMER_ID" text="Bank Customer ID:" /></label>
								<div class="col-sm-5">
									<c:out value="${customerDTO.bankCustomerId}" />
								</div>
							</div> --%>

							<%-- <div class="col-md-6">
								<label class="col-sm-5"><spring:message
										code="LABEL_COMMISSION" text="Commission"></spring:message></label>
								<form:input path="" maxlength="20" cssClass="form-control"></form:input>
								<div class="col-sm-5">
									<c:out value="${customerDTO.commission}" />
								</div>
								<!-- path="commission" -->
							</div> --%>
							<div class="col-md-6">
								<label class="col-sm-5"><spring:message
										code="LABEL_ONBOARDED_BY" text="OnBoarded By" /></label>
								<div class="col-sm-5">
									<%-- <c:if test="${customerDTO.onBordedBy eq 'self' || customerDTO.onBordedBy eq 'Self' || customerDTO.onBordedBy eq 'SELF'}">												    
                         <c:set var="onBordedBy" ><spring:message code="LABEL_SELF" text="Self" /></c:set>
                    </c:if> --%>
									<c:choose>
										<c:when
											test="${customerDTO.onBordedBy eq 'self' || customerDTO.onBordedBy eq 'Self' || customerDTO.onBordedBy eq 'SELF'}">
											<c:set var="onBordedBy">
												<spring:message code="LABEL_SELF" text="Self" />
											</c:set>
										</c:when>
										<c:otherwise>
											<c:set var="onBordedBy" value="${customerDTO.onBordedBy}"></c:set>
										</c:otherwise>
									</c:choose>
									<c:out value="${onBordedBy}" />
								</div>
							</div>

							<div class="col-sm-6">
								<label class="col-sm-5" style="margin-top: 4px;"><spring:message
										code="LABEL_REGISTEREDDATE" text="Registered Date"></spring:message></label>
								<div class="col-sm-5" style="margin-top: 4px;">
									<fmt:formatDate value="${customerDTO.createdDate}"
										pattern="dd/MM/yyyy" />
								</div>
							</div>
							<c:if test="${ customerDTO.customerKycStatus eq 21}">
								<div class="col-sm-6">
									<label class="col-sm-5" style="margin-top: 4px;"><spring:message
											code="LABEL_REJECTION_REASON" text="Reason For Rejection:" /></label>
									<div class="col-sm-5" style="margin-top: 4px;">
										<c:out value="${customerDTO.reasonForRejection} " />
									</div>
								</div>
							</c:if>
							<c:if test="${ customerDTO.appStatus == 80 }">

								<div class="col-sm-6">
									<label class="col-sm-5" style="margin-top: 4px;"><spring:message
											code="LABEL_BLOCK_REASON" text="Block Reason:" /></label>
									<div class="col-sm-5" style="margin-top: 4px;">
										<c:out value="${customerDTO.reasonForBlock} " />
									</div>

								</div>
							</c:if>
							<c:if test="${ customerDTO.customerStatus == 40 }">
								<div class="col-sm-6">
									<label class="col-sm-5" style="margin-top: 4px;"><spring:message
											code="LABEL_DEACTIVATE_REASON" text="Reason for DeActivate:" /></label>
									<div class="col-sm-5" style="margin-top: 4px;">
										<c:out value="${customerDTO.reasonForDeActivate} " />
									</div>
								</div>
						</c:if>
						</div>

						<div class="row">


							<div class="col-sm-6">
								<label class="col-sm-5"><spring:message
										code="LABEL_IDTYPE" text="ID Type" /></label>
								<div class="col-sm-5">
									<c:choose>
										<c:when test="${customerDTO.idType eq 'PassPort'}">
											<spring:message code="LABEL_PASSPORT" text="PassPort"></spring:message>
										</c:when>
										<c:when test="${customerDTO.idType eq 'National Identity'}">
											<spring:message code="LABEL_NATIONAL_IDENTITY"
												text="National Identity"></spring:message>
										</c:when>
										<c:when test="${customerDTO.idType eq 'Driving License'}">
											<spring:message code="LABEL_DRIVING_LISENCE"
												text="Driving License"></spring:message>
										</c:when>
										<c:when test="${customerDTO.idType eq 'Identity Proof'}">
											<spring:message code="LABEL_IDENTITY_PROOF"
												text="Identity Proof"></spring:message>
										</c:when>
										<c:when test="${customerDTO.idType eq 'Consular Card'}">
											<spring:message code="LABEL_CONSULAR_CARD"
												text="Consular Card"></spring:message>
										</c:when>
										<c:otherwise>
											<c:out value="${customerDTO.idType}" />
										</c:otherwise>
									</c:choose>
								</div>
							</div>

							<%-- <div class="col-md-6">
								<label class="col-sm-5"><spring:message
										code="LABEL_COMMISSION" text="Commission(%)"></spring:message></label>
								<form:input path="" maxlength="20" cssClass="form-control"></form:input>
								<div class="col-sm-5">
									<c:out value="${customerDTO.commission}" />
								</div>
								<!-- path="commission" -->
								</div> --%>

						</div>
						<div class="row">

							<div class="col-sm-6">
								<label class="col-sm-5" style="margin-top: 4px;"><spring:message
										code="LABEL_ID_NUMBER" text="IDNumber :" /></label>
								<div class="col-sm-5" style="margin-top: 4px;">
									<c:out value="${customerDTO.idNumber}" />
								</div>

							</div>
							<div class="col-sm-6">
								<label class="col-sm-5" style="margin-top: 4px;"><spring:message
										code="LABEL_PLACE_OF_ISSUE" text="Place Of Issue:"></spring:message></label>
								<div class="col-sm-5" style="margin-top: 4px;">
									<c:out value="${customerDTO.placeOfIssue}" />
								</div>
							</div>
						</div>
						<div class="row">

							<div class="col-sm-6">
								<label class="col-sm-5"><spring:message
										code="LABEL_ISSUE_DATE" text="Issue Date" /></label>
								<div class="col-sm-5">
									<fmt:formatDate pattern="dd-MM-yyyy"
										value="${customerDTO.issueDate}" />
								</div>
							</div>

							<div class="col-sm-6">
								<label class="col-sm-5" style="margin-top: 4px;"><spring:message
										code="LABEL_EXPIRY_DATE" text="Expiry Date" /></label>
								<div class="col-sm-5" style="margin-top: 4px;">
									<fmt:formatDate pattern="dd-MM-yyyy"
										value="${customerDTO.expiryDate}" />
								</div>
							</div>

						</div>

						<div class="row">

							<div class="col-sm-6">
								<label class="col-sm-5"><spring:message
										code="LABEL_ID_PROOF" text="ID Proof" /></label>
								<div class="col-sm-5">
									<!--@  Author name <vinod joshi>, Date<6/21/2018>, purpose of change < wanted quarter name > ,
					    @  Start-->
									<a
										onclick="return openNewWindow('<%=request.getContextPath()%>/getPhoto.htm?type=idproof&customerId=<c:out value="${customerDTO.customerId}" />')">
										<img
										src="<%=request.getContextPath()%>/getPhoto.htm?type=idproof&customerId=<c:out value="${customerDTO.customerId}" />"
										onerror="this.src='<%=request.getContextPath()%>/images/default_photo.png';"
										alt="<spring:message code="LABEL_ID_PROOF_NOT_FOUND" text="ID Proof not found" />"
										width="50" height="50" />
									</a>
								</div>
							</div>
							<div class="col-sm-6">
								<label class="col-sm-5"><spring:message
										code="LABEL_SIGNATURE" text="Signature" /></label>
								<div class="col-sm-5">
									<a
										onclick="return openNewWindow('<%=request.getContextPath()%>/getPhoto.htm?type=signature&customerId=<c:out value="${customerDTO.customerId}" />')">
										<img
										src="<%=request.getContextPath()%>/getPhoto.htm?type=signature&customerId=<c:out value="${customerDTO.customerId}" />"
										onerror="this.src='<%=request.getContextPath()%>/images/default_photo.png';"
										alt="<spring:message code="LABEL_SIGNATURE_NOT_FOUND" text="Signature not found" />"
										width="50" height="50" />
									</a>
								</div>
							</div>
						</div>
						<!--  added by bidyut for profile and address proof -->
						<div class="row">
							<div class="col-sm-6">
								<label class="col-sm-5"><spring:message
										code="LABEL_PROFILE_PHOTO" text="Profile Photo" /></label>
								<div class="col-sm-5">
									<a
										onclick="return openNewWindow('<%=request.getContextPath()%>/getPhoto.htm?type=profilePhoto&customerId=<c:out value="${customerDTO.customerId}" />')">
										<img
										src="<%=request.getContextPath()%>/getPhoto.htm?type=profilePhoto&customerId=<c:out value="${customerDTO.customerId}" />"
										onerror="this.src='<%=request.getContextPath()%>/images/default_photo.png';"
										alt="<spring:message code="LABEL_PROFILE_PHOTO_NOT_FOUND" text="Profile photo not found" />"
										width="50" height="50" />
									</a>
								</div>
							</div>
							<div class="col-sm-6">
								<label class="col-sm-5"><spring:message
										code="LABEL_ADDRESS_PROOF" text="Address Proof" /></label>
								<div class="col-sm-5">
									<a
										onclick="return openNewWindow('<%=request.getContextPath()%>/getPhoto.htm?type=addressProof&customerId=<c:out value="${customerDTO.customerId}" />')">
										<img
										src="<%=request.getContextPath()%>/getPhoto.htm?type=addressProof&customerId=<c:out value="${customerDTO.customerId}" />"
										onerror="this.src='<%=request.getContextPath()%>/images/default_photo.png';"
										alt="<spring:message code="LABEL_ADDRESS_PROOF_NOT_FOUND" text="Address not found" />"
										width="50" height="50" />
									</a>
								</div>
							</div>
						</div>
						<!--@ End -->
						<!--  added by vineeth for Registered Date -->
						<!--@ End -->
					</div>
					<div class="row">
						<div class="col-sm-11" style="margin-left: 78px" id="link">
							<%-- <authz:authorize ifAnyGranted="ROLE_editCustomerAdminActivityAdmin" > --%>
							<authz:authorize
								ifAnyGranted="ROLE_bankteller,ROLE_branchmanager, ROLE_bankadmin, ROLE_mGurush,ROLE_businesspartnerL1,ROLE_businesspartnerL2,ROLE_businesspartnerL3">
								<c:forEach var="acc" items="${customerDTO.accountList }" end="0">
									<c:set var="bankId" value="${ acc.bank.bankId }"></c:set>
									<c:set var="accountNumber" value="${ acc.accountNumber }"></c:set>
								</c:forEach>
								<b><a
									href="javascript:editCustomer('editAgent.htm','${customerDTO.customerId}')"><spring:message
											code="LINK_EDIT_PROFILE" text="Edit Profile" /></a></b> |	 	  						
			<input type="hidden" id="custiId" name="custiId" value="" />
								<input type="hidden" id="customerId" name="customerId" />
								<input type="hidden" id="appStatus" name="appStatus" />
							</authz:authorize>

							<authz:authorize
								ifAnyGranted="ROLE_bankteller,ROLE_branchmanager,ROLE_relationshipmanager,ROLE_admin,ROLE_supportbank,ROLE_personalrelationexec, ROLE_bankadmin, ROLE_mGurush,ROLE_businesspartnerL1,ROLE_businesspartnerL2,ROLE_businesspartnerL3">
								<authz:authorize
									ifAnyGranted="ROLE_bankteller,ROLE_branchmanager,ROLE_relationshipmanager,ROLE_admin,ROLE_supportbank,ROLE_personalrelationexec">
									<b><a href="#" id="view_reqs"
										onclick="viewRequests('${customerDTO.customerId}', '1')"><spring:message
												code="LABEL_EXCEPTIONS" text="Exceptions"></spring:message></a></b> | 
			<b><a href="#" id="view_sms"
										onclick="viewSMS('${customerDTO.isdCode}${customerDTO.mobileNumber}', '1')"><spring:message
												code="LABEL_SMS" text="SMS"></spring:message></a></b> |
								</authz:authorize>
								<b><a href="#" id="view_txns"
									onclick="viewTransactions('${customerDTO.customerId}', '1')"><spring:message
											code="LABEL_VIEW_TRANSACTIONS" text="Transactions"></spring:message></a></b> | 
		
			</authz:authorize>
							<authz:authorize
								ifAnyGranted="ROLE_superadmin,ROLE_bankadmin,ROLE_mGurush,ROLE_businesspartnerL1,ROLE_businesspartnerL2,ROLE_businesspartnerL3">
								<b><a href="#" id="view_reqs"
									onclick="viewRequests('${customerDTO.customerId}', '1')"><spring:message
											code="LABEL_EXCEPTIONS" text="Exceptions"></spring:message></a></b> | 										
			<b><a href="#" id="view_sms"
									onclick="viewSMS('${customerDTO.isdCode}${customerDTO.mobileNumber}', '1')"><spring:message
											code="LABEL_SMS" text="SMS"></spring:message></a></b> |
							</authz:authorize>
							<%-- 	<authz:authorize ifAnyGranted="ROLE_superadmin">
								<c:choose>
									<c:when
										test="${ customerDTO.customerStatus == 10 ||customerDTO.customerStatus == 15}">

										<c:if test="${ customerDTO.appStatus != 80 }">
											<b><a href="#" id="reinstall_app"
												onclick="reinstallApp('${customerDTO.customerId}')"><spring:message
														code="LABEL_REINSTALL_APPLICATION"
														text="Re-install Application"></spring:message></a></b> |

										</c:if>

									</c:when>

									<c:when test="${ customerDTO.customerStatus == 20 }">

										<c:if
											test="${ customerDTO.appStatus == 40 || customerDTO.appStatus != 80}">
											<b><a href="#" id="reinstall_app"
												onclick="reinstallApp('${customerDTO.customerId}')"><spring:message
														code="LABEL_REINSTALL_APPLICATION"
														text="Re-install Application"></spring:message></a></b> |
										</c:if>

									</c:when>

								</c:choose>
							</authz:authorize> --%>
							<authz:authorize
								ifAnyGranted="ROLE_bankteller,ROLE_superadmin,ROLE_branchmanager,ROLE_relationshipmanager,ROLE_admin,ROLE_gimsupportlead,ROLE_gimsupportexec,ROLE_supportbank,ROLE_personalrelationexec,ROLE_bankadmin,ROLE_mGurush,ROLE_businesspartnerL1,ROLE_businesspartnerL2,ROLE_businesspartnerL3">
								<c:choose>
									<c:when
										test="${ customerDTO.customerStatus == 10 ||customerDTO.customerStatus == 15 ||customerDTO.customerStatus == 40 ||customerDTO.customerStatus == 20}">

										<c:if test="${ customerDTO.appStatus != 80 }">
											<b><a href="#" id="reinstall_app"
												onclick="reinstallApp('${customerDTO.customerId}')"><spring:message
														code="LABEL_REINSTALL_APPLICATION"
														text="Re-install Application"></spring:message></a></b> |
			<!--Author<Anu kalam azad> Date<22/06/2018> reset pin should display in customer page, start -->

											<b><a href="#" id="reset_pin"
												onclick="resetPIN('${customerDTO.customerId}')"><spring:message
														code="LABEL_RESET_PIN" text="Reset Pin"></spring:message></a></b> | 
			<!-- End -->



											<%-- <b><a href="#" id="reset_txn_pin"
													onclick="resetTxnPIN('${customerDTO.customerId}')"><spring:message
															code="LABEL_RESET_TXN1_PIN" text="Reset Transaction Pin"></spring:message></a>
												</b> |  --%>


										</c:if>
										<b> <c:if test="${ customerDTO.appStatus != 80 }">
												<c:set var="newStatus" value="80"></c:set>
												<c:set var="link" value="BLOCK_APPLICATION"></c:set>
											</c:if> <c:if test="${ customerDTO.appStatus == 80 }">
												<c:set var="newStatus" value="40"></c:set>
												<c:set var="link" value="UNBLOCK_APPLICATION"></c:set>
											</c:if> <%-- <a
											href="javascript:submitForm('viewAgentForm','changeApplicationStatus.htm?customerId=${customerDTO.customerId}&status=${newStatus}')"
											id="change_app_status"
											onclick="return changeApplicationStatus(${ customerDTO.appStatus });">
												<spring:message code="${ link }" text="Change Status"></spring:message>
										</a> --%> <a
											<%-- href="javascript:submitForm('viewAgentForm','changeApplicationStatus.htm?customerId=${customerDTO.customerId}&status=${newStatus}')" --%>
											id="change_app_status"
											style="cursor: pointer;"
											onclick="return changeBlockApplication(${customerDTO.customerId},${newStatus},${ customerDTO.appStatus },'viewAgentForm');">
												<spring:message code="${ link }" text="Change Status"></spring:message>
										</a>

										</b> | 
								 	<b> <c:if test="${ customerDTO.customerStatus != 40 }">
												<c:set var="newStatus" value="40"></c:set>
												<c:if test="${customerDTO.type==1}">
													<c:set var="link" value="DEACTIVATE_AGENT"></c:set>
												</c:if>
												<c:if test="${customerDTO.type==2}">
													<c:set var="link" value="DEACTIVATE_MARCHANT"></c:set>
												</c:if>
											</c:if> <c:if test="${ customerDTO.customerStatus == 40 }">
												<c:set var="newStatus" value="20"></c:set>
												<%-- <c:set var="link" value="ACTIVATE_MERCHANT"></c:set> --%>
												<c:if test="${customerDTO.type==1}">
													<c:set var="link" value="ACTIVATE_AGENT"></c:set>
												</c:if>
												<c:if test="${customerDTO.type==2}">
													<c:set var="link" value="ACTIVATE_MERCHANT"></c:set>
												</c:if>
											</c:if> <%-- <a href="javascript:submitForm('viewAgentForm','changeCustomerStatus.htm?customerId=${customerDTO.customerId}&status=${newStatus}')"
											id="change_customer_status"
											onclick="return changeCustomerStatus(${ customerDTO.appStatus });">
												<spring:message code="${ link }" text="Change Status"></spring:message>
										</a>  --%> <a
										<%-- 	href="javascript:submitForm('viewAgentForm','changeCustomerStatus.htm?customerId=${customerDTO.customerId}&status=${newStatus}')" --%>
											id="change_customer_status"
											style="cursor: pointer;"
											onclick="return changeDeActivateApplication(${customerDTO.customerId},${newStatus},${ customerDTO.customerStatus },'viewAgentForm');">
												<spring:message code="${ link }" text="Change Status"></spring:message>
										</a> <%-- <a
											href="javascript:submitForm('viewAgentForm','changeCustomerStatus.htm?customerId=${customerDTO.customerId}&status=${newStatus}')"
											id="change_customer_status"
											onclick="return changeAgentStatus(${customerDTO.customerId},${newStatus},${ customerDTO.appStatus });">
												<spring:message code="${ link }" text="Change Status"></spring:message>
										</a> --%>

										</b> |

									</c:when>

									<c:when test="${ customerDTO.customerStatus == 20 }">

										<c:if
											test="${ customerDTO.appStatus == 40 || customerDTO.appStatus != 80}">
											<b><a href="#" id="reinstall_app"
												onclick="reinstallApp('${customerDTO.customerId}')"><spring:message
														code="LABEL_REINSTALL_APPLICATION"
														text="Re-install Application"></spring:message></a></b> |
				<b><a href="#" id="reset_pin"
												onclick="resetPIN('${customerDTO.customerId}')"><spring:message
														code="LABEL_RESET_PIN" text="Reset Pin"></spring:message></a></b> | 
														
														 
				<%-- <b><a href="#" id="reset_txn_pin"
												onclick="resetTxnPIN('${customerDTO.customerId}')"><spring:message
														code="LABEL_RESET_TXN1_PIN" text="Reset Transaction Pin"></spring:message></a></b> |  --%>
										</c:if>

										<b> <c:if test="${ customerDTO.appStatus != 80 }">
												<c:set var="newStatus" value="80"></c:set>
												<c:set var="link" value="BLOCK_APPLICATION"></c:set>
											</c:if> <c:if test="${ customerDTO.appStatus == 80 }">
												<c:set var="newStatus" value="40"></c:set>
												<c:set var="link" value="UNBLOCK_APPLICATION"></c:set>
											</c:if> <%-- <a
											href="javascript:submitForm('viewAgentForm','changeApplicationStatus.htm?customerId=${customerDTO.customerId}&status=${newStatus}')"
											id="change_app_status"
											onclick="return changeApplicationStatus(${ customerDTO.appStatus });">
												<spring:message code="${ link }" text="Change Status"></spring:message>
										</a> --%> <a
											<%-- href="javascript:submitForm('viewAgentForm','changeApplicationStatus.htm?customerId=${customerDTO.customerId}&status=${newStatus}')" --%>
											id="change_app_status"
											style="cursor: pointer;"
											onclick="return changeBlockApplication(${customerDTO.customerId},${newStatus},${ customerDTO.appStatus },'viewAgentForm');">
												<spring:message code="${ link }" text="Change Status"></spring:message>
										</a>
										</b> | 
								<%-- 	<b> <c:set var="newStatus" value="40"></c:set> <c:set var="link"
												value="DEACTIVATE_CUSTOMER"></c:set> <a
											href="javascript:submitForm('viewAgentForm','changeCustomerStatus.htm?customerId=${customerDTO.customerId}&status=${newStatus}')"
											id="change_customer_status"
											onclick="return changeCustomerStatus(${ customerDTO.customerStatus });">
												<spring:message code="${ link }" text="Change Status"></spring:message>
										</a>
										<a
											href="javascript:submitForm('viewAgentForm','changeCustomerStatus.htm?customerId=${customerDTO.customerId}&status=${newStatus}')"
											id="change_customer_status"
											onclick="return changeAgentStatus(${customerDTO.customerId},${newStatus},${ customerDTO.appStatus });">
												<spring:message code="${ link }" text="Change Status"></spring:message>
										</a>
										</b> | --%>

									</c:when>


									<%-- 	<c:when test="${ customerDTO.customerStatus == 40 }">
										<b> <c:set var="newStatus" value="20"></c:set> <c:set
												var="link" value="ACTIVATE_CUSTOMER"></c:set> <a
											href="javascript:submitForm('viewAgentForm','changeCustomerStatus.htm?customerId=${customerDTO.customerId}&status=${newStatus}')"
											id="change_customer_status"
											onclick="return changeCustomerStatus(${ customerDTO.customerStatus });">
												<spring:message code="${ link }" text="Change Status"></spring:message>
										</a>
										<a
											href="javascript:submitForm('viewAgentForm','changeCustomerStatus.htm?customerId=${customerDTO.customerId}&status=${newStatus}')"
											id="change_customer_status"
											onclick="return changeAgentStatus(${customerDTO.customerId},${newStatus},${ customerDTO.appStatus });">
												<spring:message code="${ link }" text="Change Status"></spring:message>
										</a>
										</b> |

									</c:when> --%>

								</c:choose>
							</authz:authorize>
							<authz:authorize ifAnyGranted="ROLE_ccexec">
								<c:choose>
									<c:when
										test="${ customerDTO.customerStatus == 10 ||customerDTO.customerStatus == 15}">
										<b> <c:if test="${ customerDTO.appStatus != 80 }">
												<c:set var="newStatus" value="80"></c:set>
												<c:set var="link" value="BLOCK_APPLICATION"></c:set>
											</c:if> <c:if test="${ customerDTO.appStatus == 80 }">
												<c:set var="newStatus" value="40"></c:set>
												<c:set var="link" value="UNBLOCK_APPLICATION"></c:set>
											</c:if> <%-- <a
											href="javascript:submitForm('viewAgentForm','changeCustomerStatus.htm?customerId=${customerDTO.customerId}&status=${newStatus}')"
											id="change_app_status"
											onclick="return changeApplicationStatus(${ customerDTO.appStatus });">
												<spring:message code="${ link }" text="Change Status"></spring:message>
										</a> --%> <a
											<%-- href="javascript:submitForm('viewAgentForm','changeApplicationStatus.htm?customerId=${customerDTO.customerId}&status=${newStatus}')" --%>
											id="change_app_status"
											style="cursor: pointer;"
											onclick="return changeBlockApplication(${customerDTO.customerId},${newStatus},${ customerDTO.appStatus },'viewAgentForm');">
												<spring:message code="${ link }" text="Change Status"></spring:message>
										</a>
										</b> | 
			<%-- 	<b> <c:if test="${ customerDTO.customerStatus != 40 }">
												<c:set var="newStatus" value="40"></c:set>
												<c:set var="link" value="DEACTIVATE_CUSTOMER"></c:set>
											</c:if> <c:if test="${ customerDTO.customerStatus == 40 }">
												<c:set var="newStatus" value="20"></c:set>
												<c:set var="link" value="ACTIVATE_CUSTOMER"></c:set>
											</c:if> <a
											href="javascript:submitForm('viewAgentForm','changeCustomerStatus.htm?customerId=${customerDTO.customerId}&status=${newStatus}')"
											id="change_customer_status"
											onclick="return changeCustomerStatus(${ customerDTO.customerStatus });">
												<spring:message code="${ link }" text="Change Status"></spring:message>
										</a>
										
										<a
											href="javascript:submitForm('viewAgentForm','changeCustomerStatus.htm?customerId=${customerDTO.customerId}&status=${newStatus}')"
											id="change_customer_status"
											onclick="return changeAgentStatus(${customerDTO.customerId},${newStatus},${ customerDTO.appStatus });">
												<spring:message code="${ link }" text="Change Status"></spring:message>
										</a>
										
										</b> | --%>

									</c:when>

									<c:when test="${ customerDTO.customerStatus == 20 }">


										<b> <c:if test="${ customerDTO.appStatus != 80 }">
												<c:set var="newStatus" value="80"></c:set>
												<c:set var="link" value="BLOCK_APPLICATION"></c:set>
											</c:if> <c:if test="${ customerDTO.appStatus == 80 }">
												<c:set var="newStatus" value="40"></c:set>
												<c:set var="link" value="UNBLOCK_APPLICATION"></c:set>
											</c:if> <%-- <a
											href="javascript:submitForm('viewAgentForm','changeCustomerStatus.htm?customerId=${customerDTO.customerId}&status=${newStatus}')"
											id="change_app_status"
											onclick="return changeApplicationStatus(${ customerDTO.appStatus });">
												<spring:message code="${ link }" text="Change Status"></spring:message>
										</a> --%> <a
											<%-- href="javascript:submitForm('viewAgentForm','changeApplicationStatus.htm?customerId=${customerDTO.customerId}&status=${newStatus}')" --%>
											id="change_app_status"
											style="cursor: pointer;"
											onclick="return changeBlockApplication(${customerDTO.customerId},${newStatus},${ customerDTO.appStatus },'viewAgentForm');">
												<spring:message code="${ link }" text="Change Status"></spring:message>
										</a>
										</b> | 
			<%-- 	<b> <c:set var="newStatus" value="40"></c:set> <c:set var="link"
												value="DEACTIVATE_CUSTOMER"></c:set> <a
											href="javascript:submitForm('viewAgentForm','changeCustomerStatus.htm?customerId=${customerDTO.customerId}&status=${newStatus}')"
											id="change_customer_status"
											onclick="return changeCustomerStatus(${ customerDTO.customerStatus });">
												<spring:message code="${ link }" text="Change Status"></spring:message>
										</a>
										</b> | --%>

									</c:when>


									<%-- 		<c:when test="${ customerDTO.customerStatus == 40 }">
										<b> <c:set var="newStatus" value="20"></c:set> <c:set
												var="link" value="ACTIVATE_CUSTOMER"></c:set> <a
											href="javascript:submitForm('viewAgentForm','changeCustomerStatus.htm?customerId=${customerDTO.customerId}&status=${newStatus}')"
											id="change_customer_status"
											onclick="return changeCustomerStatus(${ customerDTO.customerStatus });">
												<spring:message code="${ link }" text="Change Status"></spring:message>
										</a>
										
										<a
											href="javascript:submitForm('viewAgentForm','changeCustomerStatus.htm?customerId=${customerDTO.customerId}&status=${newStatus}')"
											id="change_customer_status"
											onclick="return changeAgentStatus(${customerDTO.customerId},${newStatus},${ customerDTO.appStatus });">
												<spring:message code="${ link }" text="Change Status"></spring:message>
										</a>
										</b>|
				
			</c:when> --%>

								</c:choose>
							</authz:authorize>

							<%-- <b><a href="#" id="smsSubscription"
								onclick="smsSubscription('${customerDTO.customerId}')"><spring:message
										code="LABEL_SMS_SUBSCRIPTION" text="SMS Subscription"></spring:message></a></b>
							| <b><a href="#" id="scSubscription"
								onclick="scSubscription('${customerDTO.customerId}')"><spring:message
										code="LABEL_SERVICE_CHARGE_SUBSCRIPTION"
										text="SC Subscription"></spring:message></a></b> --%>

						</div>
					</div>
					<table>
						<tr height="10px">
							<td></td>
						</tr>
					</table>
					<div id="data" tabindex="-1"
						style="color: #ba0101; font-weight: bold; font-size: 12px;"
						align="center">
						<table align="center" border="0">
							<tr height="20px">
								<%-- <td colspan="2"
									style="color: #ba0101; font-weight: bold; font-size: 12px;"
									align="center"><spring:message code="${message}" text="" />
								</td> --%>
							</tr>
						</table>
					</div>
					<input type="hidden" value="${customerDTO.type}" id="custType"
						name="custType" />
					<div class="modal fade" id="myModal1" role="dialog">
						<div class="modal-dialog modal-sm">

							<!-- Modal content-->
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close" onclick="closeModelBlock()"
										data-dismiss="modal">&times;</button>
									<h4 class="modal-title">Reason for Blocking?</h4>
								</div>
								<div class="modal-body">
									<input class="form-control" style="width: 100%;"
										name="blockComment" id="blockComment" type="text" />
								</div>
								<div class="modal-footer">
									<!-- <button type="button" class="btn btn-default" data-dismiss="modal">Close</button> -->
									<button type="button" class="btn btn-default"
										onclick="BlockCustSubmit('viewAgentForm')">Submit</button>
								</div>
							</div>

						</div>
					</div>
		<div class="modal fade" id="myModalDeActivate" role="dialog">
		    <div class="modal-dialog modal-sm">
		      <div class="modal-content">
		        <div class="modal-header">
		          <button type="button" class="close" onclick="closeModelDeActivate()" data-dismiss="modal">&times; </button>
		          <h4 class="modal-title">Reason for DeActivate ?</h4>
		        </div>
		        <div class="modal-body">
		          <input class="form-control" style="width: 100%;" name="deActivateComment" id="deActivateComment" type="text"/>
		        </div>
		        <div class="modal-footer">
		      <!--     <button type="button" class="btn btn-default" data-dismiss="modal">Close</button> -->
		          <button type="button" class="btn btn-default" onclick="deActivateCustSubmit('viewAgentForm')">Submit</button>
		        </div>
		      </div>
		      
		    </div>
 		 </div>
				</form:form>
			</div>
		</div>
	</div>
	<script>
       //@start Vineeth Date:16/10/2018 purpose:cross site Scripting -->
		function editCustomer(url,operatorId){
			document.getElementById('custiId').value=operatorId;
			submitlink(url,'viewAgentForm');
		}
       
       </script>
</body>
</html>