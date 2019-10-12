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
		<!-- <script type='text/javascript' src='js/jquery-1.5.1.min.js'></script> -->
<%-- 		<script type="text/javascript" src="<%=request.getContextPath()%>/js/customerServices.js"></script> --%>
		<script type="text/javascript">
			
		var Alertmsg={"ALERT_BLOCK_APP":"<spring:message code='ALERT_BLOCK_APP' text='Do you want to block the application &#63;'/>",
				"ALERT_UNBLOCK_APP":"<spring:message code='ALERT_UNBLOCK_APP' text='Do you want to unblock the application &#63;'/>",
				 "ALERT_ACTIVATE_CUSTOMER":"<spring:message code='ALERT_ACTIVATE_CUSTOMER' text='Do you want to activate the customer &#63;'/>",
				 "ALERT_DEACTIVATE_CUSTOMER":"<spring:message code='ALERT_DEACTIVATE_CUSTOMER' text='Do you want to de-activate the customer &#63;'/>",
				 "ALERT_RESET_CUST_PIN" : "<spring:message code='ALERT_RESET_CUST_PIN' text='Do you want to reset the customer PIN &#63;'/>",
				 "ALERT_RESCHEDULE_SMS" : "<spring:message code='ALERT_RESCHEDULE_SMS' text='Do you want to re-send the SMS to the customer &#63;'/>",
				 "ALERT_REINIT_REQUEST" : "<spring:message code='ALERT_REINIT_REQUEST' text='Do you want to re-initiate the request &#63;'/>",
				 "ALERT_REINSTALL_APP" : "<spring:message code='ALERT_REINSTALL_APP' text='Do you want to re-install the application &#63; (This will disable the existing application.)'/>"
				 };	
			 
			 setTimeout("showHideDiv()", 600000);// 10 minutes
			 
			 function showHideDiv() {
			        var link = document.getElementById('link');
			        link.style.display = "none";
			}
			 function customerDetail(url,operatorId){
					document.getElementById('id').value=operatorId;
					submitlink(url,'viewBusinessPartnerForm');
				}
			// Naqui: vineeth pdf for businessPartners
				function businessPartnersPDF(){
						
						 var url="exportToPDFBusinessPartners.htm";
						 submitlink(url,"viewBusinessPartnerForm"); 
						 for(var i=0;i<150000;i++);
						 document.body.style.cursor = 'default';
						 canSubmit = true; 
					}
				
				function businessPartnersExcel(){
					
					 var url="businessPartnerExcelReport.htm";
					 submitlink(url,"viewBusinessPartnerForm"); 
					 for(var i=0;i<150000;i++);
					 document.body.style.cursor = 'default';
					 canSubmit = true; 
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
		</style>
<style>
td{
text-align:center;
}	
	
</style>
	</head>
	<body>
		<form:form id="viewBusinessPartnerForm" name="viewBusinessPartnerForm" commandName="BusinessPartnerDTO"  method="post">
	<jsp:include page="csrf_token.jsp"/>
 <form:hidden path="id" /> 
<div class="row">	
<div class="col-lg-12">
<div class="box">

	<div class="box-header">
		<h3 class="box-title">
			
			<span><spring:message code="TITLE_BULKPAYMENT_PARTNER_DETAILS" text="Bulk-Payment Partner Details "> </spring:message></span>
			<%-- </c:if>
			<c:if test='${roleId ne 2}'>
			<span><spring:message code="TITLE_BUSINESS_PARTNER_DETAILS" text="Business Partner Details "> </spring:message></span>
			</c:if>--%>		
			</h3>
	</div>

	<%-- <div class="col-sm-5 col-sm-offset-9">
		<a href="javascript:submitForm('viewBusinessPartnerForm','searchCustomerPage.htm')"><strong><spring:message code="LINK_SEARCH_CUSTOMERS" text="Search Customers"> </spring:message></strong></a>  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
		<a href="javascript:submitForm('viewBusinessPartnerForm','businessPartner.htm')"><strong><spring:message code="LINK_SEARCH_BACK_BUSINESS_PARTNERS" text="Search BusinessPartners"> </spring:message></strong></a>  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
	</div> --%>
	<%-- <c:if test="${BusinessPartnerDTO.id ne null }"> --%>
		<c:if test='${fromDate eq null || fromDate eq ""}'>
		 <div class="col-sm-5 col-sm-offset-9">
			<a href="javascript:submitForm('viewBusinessPartnerForm','bulkpaymentpartnerPartner.htm')"><strong style="margin-left: 45px;">
						<spring:message code="LINK_SEARCH_BULKPAYMENT_PARTNER" text="Search Bulk-Payment Partner"></spring:message>
						
			<%-- </c:if>
			<c:if test='${roleId ne 2}'>
						<spring:message code="LINK_SEARCH_BUSINESS_PARTNERS" text="Search Business Partners"></spring:message>
			</c:if> --%>
			</strong></a>
		</div>
	</c:if>	
		<c:if test='${fromDate ne null && fromDate ne ""}'>
		 <div class="col-sm-5 col-sm-offset-8">	
			<a href="javascript:submitForm('viewBusinessPartnerForm','bulkpaymentpartnerPartner.htm')"><strong style="margin-left: 100px;"><spring:message code="LINK_SEARCH_BACK_BUSINESS_PARTNERS" text="Back to Search Business Partners"> </spring:message></strong></a>  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
		</div>
		</c:if>
<%-- 	</c:if> --%>
	<br/><br/>
	<div class="box-body">
			<div class="row">
				
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_ORGANIZATION_NAME" text="Organisation Name:"></spring:message></label> 
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${BusinessPartnerDTO.name} " /> </div>
				</div>
				
				<%-- <div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_BUSINESS_PARTNER_ID" text="BusinessPartner Id"/></label> 
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${BusinessPartnerDTO.id} " /></div>
				</div> --%>
				
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_ORGANIZATION_NUMBER" text="Organization Number"></spring:message></label> 
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${BusinessPartnerDTO.organizationNumber}" /> </div>
				</div>
				
			</div>
			
			<div class="row">
			
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_EMAILID" text="Email Id:"/></label>
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${BusinessPartnerDTO.organizationEmailId}" /></div> 
					
				</div>
	
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_BUSINESS_ENTITY" text="Business Entity"/></label> 					
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${BusinessPartnerDTO.businessEntityLimit}" /></div>
				</div>	
	
			</div>
			
			
			<div class="row">
				
				<%-- <div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_ADDRESS" text="Address"/></label> 
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${BusinessPartnerDTO.address}" /></div>
				</div> --%>
				
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_COMMISSION" text="Commision(%)"/></label> 
					<div class="col-sm-5"><c:out value="${BusinessPartnerDTO.commissionPercent}" /></div>
				</div>
				
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_PARTNER_TYPE" text="Partner Type"/></label> 
					<div class="col-sm-5" style="margin-top:4px;">
					<c:choose>						
						 <%-- <c:when test="${BusinessPartnerDTO.partnerType==1}"> <spring:message code="LABEL_BA_ADMIN" text="BA Admin"/>
						 </c:when> --%> 
						 <c:when test="${BusinessPartnerDTO.partnerType==1}"> <spring:message code="LABEL_BUSINESS_PARTNER_L1" text="Business Partner L1"/>
						 </c:when>
						  <c:when test="${BusinessPartnerDTO.partnerType==2}"> <spring:message code="LABEL_BUSINESS_PARTNER_L2" text="Business Partner L2"/>
						 </c:when>
						  <c:when test="${BusinessPartnerDTO.partnerType==3}"> <spring:message code="LABEL_BUSINESS_PARTNER_L3" text="Business Partner L3"/>
						 </c:when> 
					</c:choose>
					</div>
				</div>
				
			</div>
			
			 <div class="row">
				
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_ADDRESS" text="Address:"/></label> 
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${BusinessPartnerDTO.organizationAddress} " /></div>
				</div>
				
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_KYC_TYPE" text="Kyc Type"/></label> 
					<div class="col-sm-5" style="margin-top:4px;">
					<c:choose>						
						 <c:when test="${BusinessPartnerDTO.kycTypeId==1}"> <spring:message code="LABEL_KYC_PASSPORT" text="Passport"/>
						 </c:when>
						  <c:when test="${BusinessPartnerDTO.kycTypeId==2}"> <spring:message code="LABEL_KYC_AADHAAR" text="Aadhaar"/>
						 </c:when>
						  <c:when test="${BusinessPartnerDTO.kycTypeId==3}"> <spring:message code="LABEL_KYC_VOTERID" text="VoterID"/>
						 </c:when> 
						 <c:when test="${BusinessPartnerDTO.kycTypeId==4}"> <spring:message code="LABEL_KYC_PANCARD" text="Pan Card"/>
						 </c:when> 
					</c:choose>
					</div>
				</div>
				
			</div>
			 
			 <div class="row">
				
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_KYCID_NUMBER" text="KYC-ID Number"/></label> 					
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${BusinessPartnerDTO.kycIdNumber}" /></div>
				</div>	
				
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_KYC_IMAGE" text="KYC Image" /></label> 
					<div class="col-sm-5">
					<a onclick="return openNewWindow('<%=request.getContextPath()%>/getBPPhoto.htm?type=kycImage&id=<c:out value="${BusinessPartnerDTO.id}" />')">
							<img src="<%=request.getContextPath()%>/getBPPhoto.htm?type=kycImage&id=<c:out value="${BusinessPartnerDTO.id}" />" onerror="this.src='<%=request.getContextPath()%>/images/default_photo.png';" alt="<spring:message code="LABEL_KYC_IMG_NOT_FOUND" text="Kyc Image not found." />" width="50" height="50" />
					</a>
				</div> 
				
				</div>
				</div>
				
			<div class="row">

			<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_BUSINESS_PARTNER_CONTACT_PERSON" text=" Contact Person"></spring:message></label> 
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${BusinessPartnerDTO.contactPerson}" /> </div>
			</div>
			
			<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_DESIGNATION_NAME" text="Designation"/></label> 
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${BusinessPartnerDTO.designation} " /></div>
			</div> 

			</div>
			
			<div class="row">

			    <div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_BUSINESS_PARTNER_CONTACT_NUMBER" text="Contact Number"></spring:message></label> 
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${BusinessPartnerDTO.contactNumber}" /> </div>
			   </div>
				
			    <div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_EMAILID" text="Email Id:"/></label>
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${BusinessPartnerDTO.emailId}" /></div> 
			   </div>

		</div>
				
			<div class="row">
				
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_BUSINESS_PARTNER_CREATED_DATE" text="Created Date"/></label> 
					<div class="col-sm-5" style="margin-top:4px;"><fmt:formatDate pattern="dd-MM-yyyy" value="${BusinessPartnerDTO.createdDate}"/></div>
				</div>				

				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_BUSINESS_PARTNER_UPDATED_DATE" text="Updated Date"/></label> 
					<div class="col-sm-5" style="margin-top:4px;"><fmt:formatDate pattern="dd-MM-yyyy" value="${BusinessPartnerDTO.updatedDate}" /></div>
				</div>

			</div>		
				
			<%-- 	<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_PARTNER_STATUS" text="Status"/></label> 					
					<div class="col-sm-5" style="margin-top:4px;">
						<c:out value="${BusinessPartnerDTO.status}" />
						 <c:if test="${BusinessPartnerDTO.status == 1}">
                             <c:out value="Active" />
                          </c:if>
                           <c:if test="${BusinessPartnerDTO.status== 0}">
                                   <c:out value="Inactive" />
                            </c:if>        		
					</div>
				</div>				
				
			</div> --%>		
		
			</div></div> 
			<table><tr height="10px"><td></td></tr></table>
				<div id="data" tabindex="-1" style="color: #ba0101;font-weight: bold;font-size: 12px;" align="center">
					<table align="center" border="0">
						<tr height="20px" >
							<td colspan="2" style="color: #ba0101;font-weight: bold;font-size: 12px;" align="center">
								<spring:message code="${message}" text=""/>
							</td>
						</tr>
					</table>
				</div>		
	</div>
</div>

 <c:if test="${BusinessPartnerDTO.partnerType ne 3}">
		<div class="box">
				<div class="box-body table-responsive">
				<div class="exprt" style="margin-top: 0px; margin-bottom: 4px; height: 30px;">
                	<%-- <span><spring:message code="LABEL_EXPORT_AS" text="Export as"/> </span> --%>
					<span style="float:right; margin-right: 5px;">
						<a href="#" onclick="javascript:businessPartnersExcel();" style="text-decoration:none;"><img src="<%=request.getContextPath()%>/images/excel.jpg" />
							<%-- <span><spring:message code="LABEL_EXCEL" text="Excel"/></span> --%>
						</a>
					</span>
					
					<span style="margin-right: 30px; float:right">
					<a href="#" style="text-decoration:none;" onclick="javascript:businessPartnersPDF();"><img src="<%=request.getContextPath()%>/images/pdf.jpg" />
						<%-- <span><spring:message code="LABEL_PDF" text="PDF"/></span> --%>
					</a>
					</span>					
   			   </div>
					<table id="example1" class="table table-bordered table-striped" style="text-align:center;">
						<thead>
							<tr>
								<th><spring:message code="LABEL_BUSINESS_PARTNER_NAME" text="Business Partner Name"/></th>
								<th><spring:message code="LABEL_BUSINESS_PARTNER_TYPE" text="Business Partner Type"/></th>
								<th><spring:message code="LABEL_CONTACT_NO" text="Contact Number"/></th>
								<th><spring:message code="LABEL_CREATED_DATE" text="Created Date"/></th>
								<th><spring:message code="LABEL_ACTION_EDIT = Action" text="Action"/></th>
							</tr>
						</thead>
						<tbody>
							<c:set var="j" value="0"></c:set>
					            <c:forEach items="${page.results}" var="bpart">
					            <c:set var="j" value="${ j+1 }"></c:set>
					            <tr>
								    <td><c:out value="${bpart.name}" /></td>
								    
									<%-- <c:if test="${bpart.partnerType == 1}">
                                        <c:set var="status" ><spring:message code="LABEL_BA_ADMIN" text="BA Admin"/></c:set>
                                    </c:if> --%>
                                    <c:if test="${bpart.partnerType == 1}">
                                        <c:set var="status" ><spring:message code="LABEL_BUSINESS_PARTNER_L1" text="Business Partner L1"/></c:set>
                                    </c:if>
                                     <c:if test="${bpart.partnerType == 2}">
                                        <c:set var="status" ><spring:message code="LABEL_BUSINESS_PARTNER_L2" text="Business Partner L2"/></c:set>
                                    </c:if>
                                    <c:if test="${bpart.partnerType == 3}">
                                        <c:set var="status" ><spring:message code="LABEL_BUSINESS_PARTNER_L3" text="Business Partner L3"/></c:set>
                                    </c:if>
                                                <td><c:out value="${status}" /></td>	
                                      	<td><c:out value="${bpart.contactNumber}" /></td>
                                      	  <td><fmt:formatDate pattern="dd-MM-yyyy" value="${bpart.createdDate}" /></td> 	          
									<td>						
										<a href="javascript:customerDetail('viewBusinessPartner.htm','<c:out value="${bpart.id}"/>')"><spring:message code="LABEL_VIEW" text="View" /></a>
									</td>
								</tr>
								</c:forEach>
						</tbody>
					</table>
				</div>
	</div>	
	</c:if>
			<input type="hidden" name="nameV" id="nameV" value="${name}"/>
			<input type="hidden" name="contactNumberV" id="contactNumberV" value="${contactNumber}"/>
			<input type="hidden" name="fromDateV" id="fromDateV" value="${fromDate}"/>
			<input type="hidden" name="toDateV" id="toDateV" value="${toDate}"/>
			<input type="hidden" name="seniorPartnerV" id="seniorPartnerV" value="${seniorPartner}"/>

</form:form>
</body>
</html>