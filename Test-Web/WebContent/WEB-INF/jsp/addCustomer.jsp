<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>

<html>
	<head>
		<!-- Loading Calendar JavaScript files -->
	<%-- <%-- 	<script type="text/javascript" src="<%=request.getContextPath()%>/js/zapatec.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
	    <script type="text/javascript"	src="<%=request.getContextPath()%>/js/<spring:message code="CALENDAR_LANG"/>.js"></script> 
		
	<link href="../../css/style.css" rel="stylesheet" type="text/css" /> --%>
	
	<script type="text/javascript">

	function validate() {
		var docF = document.addCustomerReqForm;
		if (docF.mobileNumber.value == "") {
			alert("<spring:message code='MOBILENUM_EMPTY' text=' Please enter valid Mobile Number'/>");			
			return false;
		}
		if (docF.mobileNumber.value.search(/[^0-9\-()+]/g) != -1 ){
			alert("<spring:message code='MOBILENUM_EMPTY' text=' Please enter valid Mobile Number'/>");			
			return false;

		}	
			document.addCustomerReqForm.method = "post";
			document.addCustomerReqForm.action = "searchMobileNo.htm";
			document.addCustomerReqForm.submit();
		}
	
	function cancelForm(){
		document.getElementById("mobileNumber").value="";
		document.addCustomerReqForm.action="searchCustomer.htm";
		document.addCustomerReqForm.submit();
	}

</script>
	
	
	</head>
	<body>
		<table width="1003" border="0" align="center" cellpadding="0" cellspacing="0">
		  	<tr>
		    	<td><jsp:include page="top.jsp"/></td>
		  	</tr>
		  	<tr> 	
			    <td>
				    <table width="1003" border="0" cellspacing="0" cellpadding="0">
					    <tr>
						    <td width="159" valign="top">
						    	<table width="100%" border="0" cellspacing="0" cellpadding="0">
						    		<tr><td><jsp:include page="left.jsp"/></td></tr>
						    	</table>
						    </td>
						    <td width="844" align="left" valign="top">
						        <table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
							        <tr>
							        	<td><span class="headding" style="font-size: 15px;font-weight: bold;"><spring:message code="LABEL_ADD_CUSTOMER" text="View Customers"></spring:message></span></td>
							        </tr>
							         <tr><td>&nbsp</td></tr>
							        <tr>
							        	<td align="center" height="30px"> <div style="color: #ba0101;font-weight: bold;font-size: 12px;"><spring:message code="${message}" text=""/></div></td>
							        </tr>
							        <tr>
							        	<td>
							        		<form id="addCustomerReqForm" name="addCustomerReqForm" method="post"  action="searchMobileNo.htm">
									        	<table  width="40%" border="0" style="-moz-border-radius: 5px; -webkit-border-radius: 5px; border: 1px solid #a6a6a7"
														align="center" cellpadding="8" cellspacing="8" valign="top">
									        		<tr>
										        		<td><span style="color:#FF0000;">&nbsp;</span><spring:message code="LABEL_MOBILE_NO" text="Mobile Number"></spring:message></td>
														<td><input name="mobileNumber" id="mobileNumber" size="20" type="text"/></td>	
													</tr>
													<tr>
													<td align="right" colspan="2"><input type="button"
														value="<spring:message code="LABEL_SUBMIT" text="Submit"/>"
														onclick="validate()" /> <input type="button"
														value="<spring:message code="LABEL_CANCEL" text="Cancel 1" />"
														onclick="cancelForm();" /></td>
												</tr>
									               
									        	</table>
								        	</form>
							        	</td>
							        </tr>
							       
							        <tr>
							        	<td>
							        		
										 </td>
							 		</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
	  		<tr>
	    		<td><jsp:include page="footer.jsp"/></td>
	  		</tr>
		</table>
	</body>
</html>
