<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>

<head>
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/css/calendar-system.css" />
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/zapatec.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/<spring:message code="CALENDAR_LANG"/>.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/bankManagement.js"></script>	
<script type="text/javascript" src="<%=request.getContextPath()%>/js/clearingHouseManagement.js"></script>

<script type="text/javascript"> 
$(document).ready(function() {
		
		$("#country").change(function() {		
			$country = document.getElementById("countryId").value;		
			$.post("getOperators.htm", {
				country : $country
			}, function(data) {				
				document.getElementById("operators").innerHTML="";
				document.getElementById("operators").innerHTML = data;
			});
		});			
		$("#operators").change(function() {				
			$operator = document.getElementById("operatorName").value;			
			$.post("getDenominations.htm", {
				operator : $operator
			}, function(data) {
				document.getElementById("denominations").innerHTML="";
				document.getElementById("denominations").innerHTML = data;
			});
		});	
		$("#operators").change(function() {
			document.getElementById("operatorId").value = document.getElementById("operatorName").value ;
		});	
		$("#denominations").change(function() {
			document.getElementById("denominationId").value = document.getElementById("denomination").value ;
		});					
	});

  function check(){	
		Zapatec.Calendar.setup({
        firstDay          : 1,
        timeFormat        : "12",
        electric          : false,
        inputField        : "fromDate",
        button            : "trigger",
        ifFormat          : "%d/%m/%Y",
        daFormat          : "%Y/%m/%d",
        timeInterval          : 01
      });
	 
  	}

 	function check1(){	
		Zapatec.Calendar.setup({
        firstDay          : 1,
        timeFormat        : "12",
        electric          : false,
        inputField        : "toDate",
        button            : "trigger1",
        ifFormat          : "%d/%m/%Y",
        daFormat          : "%Y/%m/%d",
        timeInterval          : 01
      });
	 
  	}
 	
	function compareDate(from){
 		
		var dt2  = parseInt(from.substring(0,2),10);
		var mon2 = parseInt(from.substring(3,5),10)-1;
	    var yr2  = parseInt(from.substring(6,10),10);
	  
	    	var toDate = new Date(yr2, mon2, dt2); 	    
		    var currentDate = new Date();
		    var month = currentDate.getMonth();
		    var day = currentDate.getDate();
		    var year = currentDate.getFullYear();
		    var cdate=new Date(year,month,day);
		    var compDate=cdate-toDate;		    
			if(compDate>= 0)
				return true;
			else
				return false;

	}

 	
  	function searchReport(id){  	
  		var country=document.getElementById("countryId").value;
  		var operator=document.getElementById("operatorName").value;  	  		
  		var fromDate=document.getElementById("fromDate").value;
  	    var toDate=document.getElementById("toDate").value;    	 
  	    if(country==""){
  	    alert("<spring:message code='LABEL.COUNTRY' text='Please select Country Name'/>");
  	  	return false;	
  	    }else if(country!=0 && operator==""){
  	  	alert("<spring:message code='LABEL.OPERATOR' text='Please select Operator Name'/>");
  	  	return false;	
  	  	}else if(country!=0 && operator=='select'){
  	  	alert("<spring:message code='LABEL.OPERATOR' text='Please select Operator Name'/>");
  	    return false;	
  	  	}else if(fromDate==""){
  		alert("<spring:message code='LABEL.FROMDATE' text='Please enter From Date'/>");
  		return false;
  		} else if (!compareDate(fromDate)){
  	    alert("<spring:message code='LABEL.FROMDATE_TODAY' text='Please enter From Date less than Todays date'/>");
  	    return false;
  	    } else if (toDate==""){
  		alert("<spring:message code='LABEL.TODATE' text='Please enter To Date'/>");
  		return false;
  		} else if (!compareDate(toDate)){
  	    alert("<spring:message code='LABEL.TODATE_TODAY' text='Please enter To Date Less than Todays date'/>");
  	    return false;
  	    } else if(fromDate == toDate){
  		alert("<spring:message code='SAME_DATES' text='From Date and To Date are similar'/>");
  		return false;
  		}else if(!compareFromDate(fromDate,toDate)){
  		alert("<spring:message code='COMPARE_DATES' text='From Date should not be greater than To Date'/>");
  		return false;
  		}  	    
  	    if(id=='barReport'){
  	    document.topupform.action="barReportDataValidation.htm";
  	    document.topupform.submit();  
  	    }else
  	    document.topupform.action="pieReportDataValidation.htm";
 	    document.topupform.submit();	
  	 }  

</script>
<title><spring:message code="LABEL_TITLE" text="EOT MOBILE" /></title>
</head>
<body onload="check(),check1()">
	<table width="1003" border="0" align="center" cellpadding="0" cellspacing="0">
		<tr>
			<td><jsp:include page="top.jsp" /></td>
		</tr>
		<tr>
			<td>
				<table>
					<tr>
						<td width="159" valign="top">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td><jsp:include page="left.jsp" /></td>
								</tr>
							</table>
						</td>
						<td width="844" align="left" valign="top">
							<table width="98%" border="0" height="200px" align="left" cellpadding="0" cellspacing="0">								
								<tr height="20px">
								<td align="left" class="headding" width="20%"
										style="font-size: 15px; font-weight: bold;"><spring:message code="TITLE_REPORT_TOPUP" text="Topup Reports"/>
									</td>						
								</tr>								
								<tr height="20px">
			                    	<td align="center" colspan="2">
			                    	     <div style="color: #ba0101; font-weight: bold; font-size: 12px;">
			                    	        <spring:message code="${message}" text="" />			                    	        
			                    	     </div>
			                    	</td>
			                	</tr>
								<form:form name="topupform" method="post" commandName="operatorDTO">
									<tr style="height:60px">
										<td valign="top" colspan="6">
											<table style="-moz-border-radius: 5px; -webkit-border-radius: 5px; border: 1px solid #a6a6a7" align="center" width="100%" cellpadding="4" cellspacing="4">												
																					
												<tr>	
												<form:hidden path="operatorId" />
												<form:hidden path="denominationId" />												
													<td><spring:message code="LABEL_COUNTRY" text="Country"/><font color="red">*</font></td>
											        <td><div id="country">										
											         <select class="dropdown" id="countryId" name="countryId" >
											         <option value=""><spring:message code="LABEL_SELECT" text="--Please Select--" /></option>
										             <option value="0"><spring:message 	code="LABEL_ALL" text="Select All" />
											         <c:set var="lang" value="${language}"></c:set>
														<c:forEach items="${countryList}" var="country">
														<c:forEach items="${country.countryNames}" var="cNames">
																	<c:if test="${cNames.comp_id.languageCode==lang }">
																	 <option value="<c:out value="${country.countryId}"/>"  <c:if test="${country.countryId eq operatorDTO.countryId}" >selected=true</c:if> > 
																	<c:out 	value="${cNames.countryName}"/>	
																	</option>
																	</c:if>										
														</c:forEach>
														</c:forEach>														
													</select>
													</div>											
									               <font color="red"><form:errors path="countryId" /></font>
									               </td>									               
													<td><spring:message code="LABEL_OPERATOR" text="Operator:" /><font color="red">*</font></td>
											      <td><div id="operators">
											         <form:select path="operatorId" cssClass="dropdown" id="operatorName">
												       <form:option value=""><spring:message code="LABEL_SELECT" text="--Please Select--" /></form:option>												       
												       <form:options items="${list}" itemLabel="operatorName" itemValue="operatorId"></form:options>
											         </form:select></div> <FONT color="red"><form:errors path="operatorId" /></FONT>
											      </td>												      											
												</tr>
												<tr>
												<td><spring:message code="LABEL_DENOMINATION" text="Denomination:" /><font color="red">*</font></td>
											      <td><div id="denominations">
											         <form:select path="denominationId" cssClass="dropdown" id="denomination">
												       <form:option value=""><spring:message code="LABEL_SELECT" text="--Please Select--" /></form:option>
												       <form:options items="${list}" itemLabel="denomination" itemValue="denominationId"></form:options>
											         </form:select></div> <FONT color="red"><form:errors path="denominationId" /></FONT>
											      </td>		
												  <td><spring:message code="LABEL_FROMDATE" text="From" /><font color="red">*</font></td>																		
													<td>
													<form:input path="fromDate" readonly="true"></form:input><img style="" src="<%=request.getContextPath()%>/images/calendar.gif"
														id="trigger" alt="Calendar" title="Calendar" align="top" onClick="check()"> 
													 </img> 
														<font color="red">
													<form:errors path="fromDate" /></font></td>											
																										
												  </tr>	
												  <tr>
												  <td><spring:message code="LABEL_TODATE" text="To" /><font color="red">*</font></td>
														<td>
														<form:input path="toDate" readonly="true"></form:input><img style="" src="<%=request.getContextPath()%>/images/calendar.gif"
															id="trigger1" alt="Calendar" title="Calendar" align="top" onClick="check1()"></img>
															<font color="red">
															<form:errors path="toDate" /></font>
													</td>	
												  </tr>										
												 <tr>
												 	<td colspan="4" align="right">
												 	<input type="button" value="BarReport" id="barReport"  onclick="searchReport(this.id);" />
												 	<input type="button" value="PieReport" id="pieReport"  onclick="searchReport(this.id);" />
													</td>	
												 </tr>
											</table>											
										</td>                                    
									</tr>								
								</form:form>
								<tr>
								<c:if test="${message eq null && imgHide ne 'imgHide'}">
								<td align="center" colspan="2">
			                    	     <div style="font-weight: bold; font-size: 20px;">
			                    	        <spring:message code="${message}" text="TOPUP SUMMARY" />			                    	        
			                    	     </div>
			                    	</td>
								</c:if>
								</tr>								
							</table>	
						 <c:if test="${barReport ne 'barReport'}">										
							<c:if test="${message eq null && imgHide ne 'imgHide'}">
								<img id='img1' src="<c:out value="${imgUrl}"/>?fromDate=<c:out value="${fromDate}"/>&toDate=<c:out value="${toDate}"/>&chartType=<c:out value="${chartType}"/>&countryId=<c:out value="${countryId}"/>&operatorId=<c:out value="${operatorId}"/>&denominationId=<c:out value="${denominationId}"/>&imgType=imgCount" width="400" height="400"/>	
								<img id='img2' src="<c:out value="${imgUrl}"/>?fromDate=<c:out value="${fromDate}"/>&toDate=<c:out value="${toDate}"/>&chartType=<c:out value="${chartType}"/>&countryId=<c:out value="${countryId}"/>&operatorId=<c:out value="${operatorId}"/>&denominationId=<c:out value="${denominationId}"/>&imgType=imgValue" width="400" height="400"/>	
							</c:if>
						</c:if>
						<c:if test="${barReport eq 'barReport'}">										
							<c:if test="${message eq null && imgHide ne 'imgHide'}">
								<img id='img1' src="<c:out value="${imgUrl}"/>?fromDate=<c:out value="${fromDate}"/>&toDate=<c:out value="${toDate}"/>&chartType=<c:out value="${chartType}"/>&countryId=<c:out value="${countryId}"/>&operatorId=<c:out value="${operatorId}"/>&denominationId=<c:out value="${denominationId}"/>&imgType=imgCount" width="800" height="400"/>	
							</c:if>
						</c:if>
							
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
