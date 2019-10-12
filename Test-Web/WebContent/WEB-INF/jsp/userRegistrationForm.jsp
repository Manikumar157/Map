<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><spring:message code="LABEL_TITLE" text="EOT MOBILE" /></title>

<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/css/calendar-system.css" />

<!-- Loading Calendar JavaScript files -->
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/zapatec.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/calendar.js"></script>

<!-- Loading language definition file -->
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/<spring:message code="CALENDAR_LANG"/>.js"></script>

<script language="JavaScript">
function clearForm(){
	document.userRegistrationForm.reset();
}
function check(){
	//alert("hi") ;
		Zapatec.Calendar.setup({
        firstDay          : 1,
        timeFormat        : "12",
        electric          : false,
        inputField        : "dob",
        button            : "trigger",
        ifFormat          : "%d/%m/%Y",
        daFormat          : "%Y/%m/%d",
        timeInterval          : 01
      });
	 
  }

 function check1(){
	//alert("hi") ;
		Zapatec.Calendar.setup({
        firstDay          : 1,
        timeFormat        : "12",
        electric          : false,
        inputField        : "pob",
        button            : "trigger1",
        ifFormat          : "%d/%m/%Y",
        daFormat          : "%Y/%m/%d",
        timeInterval          : 01
      });
	 
  }


	function limitText(limitField, limitCount, limitNum) {
		if (limitField.value.length > limitNum) {
			limitField.value = limitField.value.substring(0, limitNum);
		} else {
			limitCount.value = limitNum - limitField.value.length;
		}
	    }
		
		function startTime()
		{
			var today=new Date()
			var h=today.getHours()
			var m=today.getMinutes()
			var s=today.getSeconds()
			// add a zero in front of numbers<10
			m=checkTime(m)
			s=checkTime(s)
			document.getElementById('txt').innerHTML=today
			t=setTimeout('startTime()',500)
		}

		function checkTime(i)
		{
		if (i<10) 
		{i="0" + i}
		return i
		}
	function openNewWindow(custId){
			
			day = new Date();
			id = day.getTime();
			
			//alert("the report type = "+del);
			var url = "policy.htm?type=photo&custId="+custId;
			//alert("the report type = "+url);
			eval("page" + id + " = window.open(url, '" + id + "','width=500,height=400');");
		
		
	}

	function openNewWindow2(custId,type){
			
			day = new Date();
			id = day.getTime();
			
			//alert("the report type = "+del);
			var url = "photo.htm?type="+type+"&custId="+custId;
			//alert("the report type = "+url);
			eval("page" + id + " = window.open(url, '" + id + "','width=600,height=620');");
		
	}

	function openNewWindow1(familyId,noOfDependent){
			
			day = new Date();
			id = day.getTime();
			
			//alert("the report type = "+del);
			var url = "formDetails.htm?type=form&familyId="+familyId+"&noOfDependent="+noOfDependent;
			
			alert("the report type = "+url);
			document.zerofiweb.method="post";
				document.zerofiweb.action=url;
				document.zerofiweb.submit();
				
			//eval("page" + id + " = window.open(url, '" + id + "','width=400,height=300');");
		
		}
		
	function isValidPassword()
	{
		var field=document.changePwdForm.newPassword;
		re=/^[0-9A-Za-z@!#$)(*&^%]{6,15}$/;
		return re.test(field.value);
	}
function fileValidate(){
if(document.userRegistrationForm.uip.value=="" || document.userRegistrationForm.uip.value==" ")
		{
			alert("<spring:message code='VALIDATION_ID_PROOF' text='please select Id proof'/>");
			document.userRegistrationForm.uip.focus();
			return false;
		}	
		var filetype1 = document.userRegistrationForm.uip.value.substring(document.userRegistrationForm.uip.value.lastIndexOf("."));
	 if(filetype1.toLowerCase() != ".jpg" ){
	    alert("<spring:message code='VALIDATION_PROOF_JPG' text='please enter jpg files' /> ");
	    document.userRegistrationForm.uip.focus();
		return false;
		}

}
 
 
</script>
</head>
<body onload="check(),check1()">
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
					<tr>
					<tr>
						<td align="left" class="headding" width="20%"
							style="font-size: 15px; font-weight: bold;"><spring:message
							code="TITLE_REGIST_SERVICE"
							text="Welcome to GIM Mobile User Registration Service"></spring:message>
						<br />
						<br />
						</td>
					</tr>
					<tr>
						<td style="color: #ba0101"><c:if
							test="${map.message ne null}">
							<spring:message code="${map.message}" />
						</c:if></td>
					</tr>

					<td valign="top">


					<div>

					<table border="0">

						<tr>
							<form:form name="userRegistrationForm"
								action="userRegistrationProcess.htm" id="userRegistrationForm"
								method="post" commandName="userDTO">
								<table width="760px" border="0" align="center" cellpadding="0"
									cellspacing="0"
									style="-moz-border-radius: 5px; -webkit-border-radius: 5px; border: 1px solid #a6a6a7">

									<tr>
										<td height="151" colspan="2" valign="top">
										<table border="0" width="100%" cellpadding="2" cellspacing="2">
											<tr>
												<td width="20%"><spring:message code="LABEL_USER_TITLE"
													text="Title:"></spring:message></td>
												<td><form:select path="title" name="title"
													class="dropdown" id="title">
													<option value="select" selected="selected"><spring:message
														code="LABEL_USER_SELECT" text="select"></spring:message></option>
													<option value="Mr"><spring:message
														code="LABEL_USER_MR" text="Mr."></spring:message></option>
													<option value="Mrs"><spring:message
														code="LABEL_USER_Mrs" text="Mrs."></spring:message></option>
													<option value="Dr"><spring:message
														code="LABEL_USER_Dr" text="Dr."></spring:message></option>
												</form:select> <FONT color="red"><form:errors path="title" /></FONT></td>
												<td><spring:message code="LABEL_FIRST_NAME"
													text="First Name:"></spring:message></td>
											<tr>
												<td><spring:message code="LABEL_MIDDLE_NAME"
													text="Middle Name:"></spring:message></td>
												<td><form:input path="middleName" id="middleName"
													name="middleName" /> <FONT color="red"><form:errors
													path="middleName" /></FONT></td>
												<td><spring:message code="LABEL_LAST_NAME"
													text="Last Name:" /></td>
												<td><form:input path="lastName" id="lastName"
													name="lastName" /> <FONT color="red"><form:errors
													path="lastName" /></FONT></td>

											</tr>

											<tr>
												<td><spring:message code="LABEL_USER_MOBILENO"
													text="Mobile No.:" /></td>
												<td><form:input path="mobile" id="mobile" name="mobile" />
												<FONT color="red"><form:errors path="mobile" /></FONT></td>
												<td><spring:message code="LABEL_GENDER" text="Gender:" /></td>
												<td><form:select path="gender" name="gender"
													class="dropdown" id="gender">
													<option value="select" selected="selected"><spring:message
														code="LABEL_USER_SELECT" text="select" /></option>
													<option value="male"><spring:message
														code="LABEL_USER_MALE" text="Male" /></option>
													<option value="female"><spring:message
														code="LABEL_USER_FEMALE" text="Female" /></option>
												</form:select> <FONT color="red"><form:errors path="gender" /></FONT></td>
											</tr>
											<tr>
												<td><spring:message code="LABEL_DOB"
													text="Date Of Birth:" /></td>
												<td colspan="0"><form:input path="dob" id="dob"
													name="dob" class="selectbox" /> <img style=""
													src="<%=request.getContextPath()%>/images/calendar.gif"
													id="trigger" alt="Calendar" title="Calendar" align="top"
													onClick="check()"> <FONT color="red"><form:errors
													path="dob" /></FONT></td>
												<td><spring:message code="LABEL_POB"
													text="Place Of Birth:" /></td>
												<td><form:input path="pob" id="pob" name="pob" /> <FONT
													color="red"><form:errors path="pob" /></FONT></td>
											</tr>
											<tr>

												<td><spring:message code="LABEL_COUNTRY"
													text="Country:" /></td>
												<td><form:select path="country" class="dropdown"
													id="country">
													<option><spring:message code="LABEL_USER_SELECT"
														text="--Please Select--" /></option>
													<form:options items="${countryList}" itemLabel="countryId" />

												</form:select> <FONT color="red"><form:errors path="country" /></FONT></td>
												<td><spring:message code="LABEL_CITY" text="City:" /></td>
												<td><form:select path="city" name="city"
													class="dropdown" id="city">
													<option><spring:message code="LABEL_USER_SELECT"
														text="--Please Select--" /></option>
													<form:options items="${cityList}" />

												</form:select> <FONT color="red"><form:errors path="city" /></FONT></td>
											</tr>
											<tr>
												<td><spring:message code="LABEL_ADDRESS"
													text="Address:" /></td>
												<td><form:input path="address" id="address"
													name="address" /> <FONT color="red"><form:errors
													path="address" /></FONT></td>
												<td><spring:message code="LABEL_QUARTER"
													text="Quarter:" /></td>
												<td><form:select path="quarter">
													<form:option value="0" label="Select" />
													<form:options items="${quarter}" />

												</form:select> <FONT color="red"><form:errors path="quarter" /></FONT></td>
											</tr>
											<tr>
												<td><spring:message code="LABEL_USER_ORGN"
													text="Organization:" /></td>
												<td><form:input path="organization" id="organization"
													name="organization" /> <FONT color="red"><form:errors
													path="organization" /></FONT></td>
												<td><spring:message code="LABEL_EMAILID"
													text="Email Id:" /></td>
												<td><form:input path="email" id="email" name="email" />
												<FONT color="red"><form:errors path="email" /></FONT></td>
											</tr>
											<tr>
												<td><spring:message code="LABEL_USER_USERNAME"
													text="User Name:" /></td>
												<td><form:input path="userName" id="userName"
													name="userName" /> <FONT color="red"><form:errors
													path="userName" /></FONT></td>
											</tr>
											<tr>
												<td><spring:message code="LABEL_USER_ROLE" text="Role:" /></td>
												<td><form:select path="role" name="role"
													class="dropdown" id="role">
													<form:option value="select" selected="selected">
														<spring:message code="LABEL_USER_SELECT" text="--Please Select--" />
													</form:option>
													<form:options items="${role}"></form:options>
												</form:select> <FONT color="red"><form:errors path="role" /></FONT></td>
												<td><spring:message code="LABEL_ID_PROOF"
													text="Id Proof:" /></td>
												<td><form:input path="uip" type="file" name="uip" /></td>
											</tr>



											<tr width="500px">
												<table width="100%">
													<tr align="left">
														<td style="padding-top: 8px; padding-bottom: 8px;"><input
															type="submit" value="<spring:message code="LABEL_SAVE" text="Save"/> " onClick="fileValidate()" /> <input
															type="button" value="<spring:message code="LABEL_RESET" text="Reset"/>" onClick="clearForm()" /></td>
													</tr>
												</table>
												</td>
											</tr>
										</table>
										</td>
									</tr>
								</table>
							</form:form>
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
