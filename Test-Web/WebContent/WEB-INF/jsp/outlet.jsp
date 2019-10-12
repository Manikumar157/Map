<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="authz"
	uri="http://www.springframework.org/security/tags"%>
<html>
<head>

<%--  <script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.5.1.min.js"></script>  --%>
	<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-ui-1.8.14.custom.min.js"></script>

<style>
.pull-right {
    float: right!important;
    margin-right: 164px;
}
/* by:rajlaxmi for:changing position of cancel bttn */
.cancelbttn {
    float: right!important;
    margin-right: -139px;
}
</style>

<!-- Loading language definition file -->


<script type="text/javascript">
$(document).ready(
		function() {
			$("#countryId").change(function() {
				/*  @BugId-505 by:rajlaxmi for:dropdown reset issue */
				$("#cityId option:gt(0)").remove();
				$("#quarterId option:gt(0)").remove();
				updateChosen();
				
				$country = document.getElementById("countryId").value;
				if($country.length<1)
					$country=0;
				$csrfToken = $("#csrfToken").val();

				$.post("getCityList.htm", {
					country : $country,
					csrfToken : $csrfToken
				}, function(data) {
					$country = document.getElementById("countryId").value;
					
					document.getElementById("cities").innerHTML = data;
			
					setTokenValFrmAjaxResp();
					applyChosen();
				});
			});
			
			$("#cities").change(function() {
				$city = document.getElementById("cityId").value;
				if($city.length<1)
					$city=0;
				$csrfToken = $("#csrfToken").val();
				$.post("getQuartersList.htm", {
					city : $city,
					csrfToken : $csrfToken
				}, function(data) {
					document.getElementById("quarters").innerHTML = "";
					document.getElementById("quarters").innerHTML = data;
					setTokenValFrmAjaxResp();
					applyChosen();
					
				});
			});

			
		});

function validate() {
	var location = $("#location").val();
	var address = $("#address").val();
	var primaryContactName = $("#primaryContactName").val();
	var primaryContactAddress = $("#primaryContactAddress").val();
	var primaryContactPhone = $("#primaryContactPhone").val();
	var primaryContactMobile = $("#primaryContactMobile").val();
	var primaryeMailAddress = $("#primaryeMailAddress").val();
	var statuss = $("#active").val();
	var cityId = $("#cityId").val();
	var countryId = $("#countryId").val();
	var quarterId = $("#quarterId").val();
	var mcc = $("#mcc").val();
	var alternateContactMobile = $("#alternateContactMobile").val(); 
	var alternateContactName = $("#alternateContactName").val(); 
	var alternateContactAddress = $("#alternateContactAddress").val(); 
	var alternateeMailAddress = $("#alternateeMailAddress").val(); 
	var alternateContactPhone = $("#alternateContactPhone").val();
	var emailPattern = /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;
	/* bugId:505 by:rajlaxmi for:changing alert sequence */
	 if(location == ""  ){
			alert("<spring:message code='ALERT_ADD_LOCATION' text='Please add a location'/>");
			return false;
		} 
	 if(location.charAt(0) == " " || location.charAt(location.length-1) == " "){
         alert("<spring:message code='ALERT_VALIDATION_LOCATION_SPACE' text='Please remove unwanted spaces in Location'/>");
         document.showOutletForm.location.focus();
         return false;
         }
	 if(!location.match(/^[a-zA-Z0-9\s]+$/ ))
	 {		alert("<spring:message code='ALERT_ADD_LOCATION_VALIDATION' text='Add location should be alphanumeric value'/>");
		return false;
		} 
	 if(location.length>21)
	 {		alert("<spring:message code='ALERT_ADD_LOCATION_VALIDATION_LENGTH' text='Add location should not be greater than 20 characters'/>");
		return false;
		} 
	 
	 if(primaryContactName == ""){
			alert("<spring:message code='ALERT_INVALID_CONTACT_NAME' text='Primary name should not be null'/>");
			return false;
		}
	 
	 if(primaryContactName.charAt(0) == " " || primaryContactName.charAt(primaryContactName.length-1) == " "){
         alert("<spring:message code='ALERT_VALIDATION_PRIMARY_CONTACT_NAME_SPACE' text='Please remove unwanted spaces in Primary Contact Name'/>");
         document.showOutletForm.primaryContactName.focus();
         return false;
         }
				 
	 if(!primaryContactName.match(/^[a-zA-Z\s]+$/)){
			alert("<spring:message code='ALERT_PRIMARY_CONTACT_NAME_VALIDATION' text='Name should  have alphabets only'/>");
			return false;
		}
	 if(primaryContactMobile == ""){
			alert("<spring:message code='ALERT_VALIDATION_PRIMARY_MOBILE_NUMBER' text='Primary Mobile Number should not be null'/>");
			return false;
		}
	 if (primaryContactMobile.charAt(0) == " "
			|| primaryContactMobile.charAt(primaryContactMobile.length - 1) == " ") {
		alert("<spring:message code='ALERT_VALIDATION_PRIMARY_MOBILE_NO_SPACE' text='Please remove unwanted spaces in Primary Contact Number'/>");
		
		return false;
	}
	
	 if ( primaryContactMobile.length<10 || primaryContactMobile.length>15 || primaryContactMobile ==/[0-9]/) {
	
		alert("<spring:message code='ALERT_VALIDATION_PRIMARY_MOBILE_NUMBER_LENGHT' text='Please enter a valid Phone number ,number should have minimum 10 digits and mximum 14 digits'/>");
		return false;
	}
	   if (primaryeMailAddress == "") {
			alert('<spring:message code="ALERT_VALID_EMAIL_ID" text="Please enter  email id"/>');
			return false;
		}
	   if (!primaryeMailAddress.match(emailPattern)) {
			alert('<spring:message code="VALIDATION_CLPOOL_EMAIL" text="Please enter valid Email ID"/>');
			return false;
		}
	 if(primaryContactMobile.charAt(0) == " " || primaryContactMobile.charAt(primaryContactMobile.length-1) == " "){
         alert("<spring:message code='ALERT_VALIDATION_PRIMARY_MOBILE_NO_SPACE' text='Please remove unwanted spaces in Primary Contact Number'/>");
         document.showOutletForm.primaryContactMobile.focus();
         return false;
         }
	 if(primaryContactPhone == ""){
			alert("<spring:message code='ALERT_VALID_PHONE_NUMBER' text='Primary Phone Number should not be null'/>");
			return false;
		}
	
	   if ( primaryContactPhone.length<10 || primaryContactPhone.length>15 || primaryContactPhone ==/[0-9]/) {
			alert('<spring:message code="ALERT_VALID_PHONE_NUMBER_LENGTH" text="Please enter a valid Phone number ,number should have minimum 10 digits and maximum 14 digits"/>');
			return false;
		}
	   if(primaryContactPhone.charAt(0) == " " || primaryContactPhone.charAt(primaryContactPhone.length-1) == " "){
	         alert("<spring:message code='ALERT_VALIDATION_PRIMARY_PHONE_NO_SPACE' text='Please remove unwanted spaces in Primary Phone Number'/>");
	         document.showOutletForm.primaryContactPhone.focus();
	         return false;
	         }
	 	if (address.length > 200) {
			alert('<spring:message code="ALERT_ADDRESS_LENGTH" text="Address should not exceed more than 180 characters"/>');
			return false;
	 	}
	 	if(address.charAt(0) == " " || address.charAt(address.length-1) == " "){
	         alert("<spring:message code='ALERT_VALIDATION_ADDRESS_SPACE' text='Please remove unwanted spaces in Address'/>");
	         document.showOutletForm.address.focus();
	         return false;
	         }
		 if(alternateContactMobile != ""  ){
			  if(alternateContactMobile.length<10 || alternateContactMobile.length>15 || alternateContactMobile ==/[0-9]/)
				{alert("<spring:message code='ALERT_VALIDATION_ALTERNATE_CONTACT_MOBILE_LENGTH' text='Please enter a valid alternate mobile number ,number should have minimum 10 digits and maximum 14 digits'/>");
				return false;}
			
		 if(alternateContactMobile.charAt(0) == " " || alternateContactMobile.charAt(alternateContactMobile.length-1) == " "){
	         alert("<spring:message code='ALERT_VALIDATION_ALTERNATE_CONTACT_MOBILE_SPACE' text='Please remove unwanted spaces in Alternate Contact Mobile'/>");
	         document.showOutletForm.alternateContactMobile.focus();
	         return false;
	         } }
		  if (alternateContactName != "") {
				if (!alternateContactName.match(/^[a-zA-Z\s]+$/)) {
					alert("<spring:message code='ALERT_PRIMARY_CONTACT_NAME_CRITERIA' text='Alternate Contact Name should have alphabets only'/>");
					return false;
				}

				if (alternateContactName.charAt(0) == " "
						|| alternateContactName
								.charAt(alternateContactName.length - 1) == " ") {
					alert("<spring:message code='ALERT_VALIDATION_ALTERNATE_CONTACT_NAME_SPACE' text='Please remove unwanted spaces in Alternate Contact Name'/>");
					document.showOutletForm.alternateContactName.focus();
					return false;
				}
			}
		  if(alternateContactPhone != ""  ){
			  if(alternateContactPhone.length<10 || alternateContactPhone.length>15 || alternateContactPhone ==/[0-9]/)
				{alert("<spring:message code='ALERT_VALID_ALTERNATE_PHONE_NUMBER' text='Please enter a valid phone number ,number should have minimum 10 digits and maximum 14 digits'/>");
				return false;}
			  
			
		  if(alternateContactPhone.charAt(0) == " " || alternateContactPhone.charAt(alternateContactPhone.length-1) == " "){
		         alert("<spring:message code='ALERT_VALIDATION_ALTERNATE_CONTACT_PHONE_SPACE' text='Please remove unwanted spaces in Alternate Contact Phone'/>");
		         document.showOutletForm.alternateContactPhone.focus();
		         return false;
		         } }
		  
			
		  if(alternateeMailAddress != ""  ){
			  if(!alternateeMailAddress.match(emailPattern))
				{alert("<spring:message code='ALERT_VALIDATE_ALTERNATE_EMAIL_ID' text='Please enter valid Alternate Email ID'/>");
				return false;}
			}
		  if(statuss == ""){
				alert("<spring:message code='ALERT_STATUS' text='Please select a status'/>");
				return false;
			          } 
     	  if(countryId == ""){
		alert("<spring:message code='ALERT_COUNTRY' text='Please select a country'/>");
		return false;
	       } 
	     if(cityId == "" || cityId=="select"){
		alert("<spring:message code='ALERT_CITY' text='Please select a city'/>");
		return false;
	       }
		  if(mcc == ""){
				alert("<spring:message code='ALERT_MCC' text='Please select MCC'/>");
				return false;
			}
	   	if(quarterId == "" || quarterId=="select"){
		alert("<spring:message code='ALERT_QUARTER' text='Please select a quarter'/>");
		return false;
	     }
		
	 	if(address == ""){
		alert("<spring:message code='ALERT_ADDRESS' text='Please enter address'/>");
		return false;
	    }
	 	  if(address.charAt(0) == " " || address.charAt(address.length-1) == " "){
		         alert("<spring:message code='ALERT_VALIDATION_ADDRESS_SPACE' text='Please remove unwanted spaces in Address'/>");
		         document.showOutletForm.address.focus();
		         return false;
		         }
	 	if(primaryContactAddress == "" ){
			alert('<spring:message code="ALERT_INVALID_PRIMARY_CONTACT_ADDRESS" text="Please add primary address"/>');
			return false;
		}
	 	  if(primaryContactAddress.charAt(0) == " " || primaryContactAddress.charAt(primaryContactAddress.length-1) == " "){
		         alert("<spring:message code='ALERT_VALIDATION_PRIMARY_CONTACT_ADDRESS_SPACE' text='Please remove unwanted spaces in Primary Contact Address'/>");
		         document.showOutletForm.primaryContactAddress.focus();
		         return false;
		         }
		if (primaryContactAddress.length > 180) {
			alert('<spring:message code="ALERT_ADDRESS_LENGTH" text="Address should not exceed more than 180 characters"/>');
			return false;
	 	}
		  if(alternateContactAddress != ""  ){
			  if(alternateContactMobile.length>180)
				{alert("<spring:message code='ALERT_ADDRESS_LENGTH' text='Address should not exceed more than 180 characters'/>");
				return false;}
			}
		  if(alternateContactAddress.charAt(0) == " " || alternateContactAddress.charAt(alternateContactAddress.length-1) == " "){
		         alert("<spring:message code='ALERT_VALIDATION_ALTERNATE_CONTACT_ADDRESS_SPACE' text='Please remove unwanted spaces in Alternate Contact Address'/>");
		         document.showOutletForm.alternateContactAddress.focus();
		         return false;
		         }
	 
	
	document.showOutletForm.method = "post";
	document.showOutletForm.action = "saveOutlet.htm";
	document.showOutletForm.submit();
}
	
	
	function cancelForm(showOutlet) {
		document.showOutletForm.action = "showMechantOutlets.htm?merchantId="+showOutlet;
		document.showOutletForm.submit();
	}
</script>
</head>
<body>
	<form:form name="showOutletForm" id="showOutletForm" commandName="outletDTO"
		class="form-inline">
		<jsp:include page="csrf_token.jsp"/>
		<div class="col-lg-12">
			<div class="box">
				<div class="box-header">
					<h3 class="box-title">
						<span><spring:message code="LABEL_OUTLET" text="Outlet" /></span>
					</h3>
				</div>
				<div class="col-sm-5 col-sm-offset-10">
				<!-- bugId:505 by:rajlaxmi for:changing url ref from searchOutlet.htm to showAllOutlets.htm -->
				<%-- <a href="javascript:submitForm('showOutletForm','showMechantOutlets.htm?merchantId=<c:out value="${outletDTO.merchantId}"/>')"><strong><spring:message code="LABLE_ VIEW_OUTLET" text="View outlet"> </spring:message></strong></a> --%>
				     <a href="javascript:submitForm('showOutletForm','showMechantOutlets.htm?merchantId=<c:out value="${outletDTO.merchantId}"/>')"><strong><spring:message code="VIEW_OUTLET" text="View outlet"> </spring:message></strong></a>
				</div>
				<br />
				<div class="col-md-3 col-md-offset-4" style="color: #ba0101; font-weight: bold; font-size: 12px;">
					<spring:message code="${message}" text="" />
				</div>
				<form:hidden path="merchantId" />
				<form:hidden path="outletId" />
				<div class="box-body">
					<div class="row">
						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_LOCATIOIN" text="Location"></spring:message><font
								color="red">*</font></label>
							<form:input path="location" id="location" cssClass="form-control" />
						</div>
						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_PRIMARY_CONTACT_NAME" text="Primary contact name"></spring:message><font
								color="red">*</font></label>
							<form:input path="primaryContactName" id="primaryContactName" cssClass="form-control" />
						</div>
					
					</div>
						<div class="row">
							<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_PRIMARY_CONTACT_MOBILE" text="Primary contact mobile"></spring:message><font
								color="red">*</font></label>
							<form:input path="primaryContactMobile"  id="primaryContactMobile" cssClass="form-control" />
						</div>
						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_PRIMARY_CONTACT_EMAIL_ADDRESS" text="Primary email address"></spring:message><font
								color="red">*</font></label>
							<form:input path="primaryeMailAddress" id ="primaryeMailAddress" cssClass="form-control" />
						</div>
					</div>
					
					<div class="row">
					<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_PRIMARY_CONTACT_PHONE" text="Primary contact phone"></spring:message><font
								color="red">*</font></label>
							<form:input path="primaryContactPhone" id="primaryContactPhone" cssClass="form-control" />
						</div>
						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_ALTERNATE_CONTACT_MOBILE" text="Alternate contact mobile"></spring:message></label>
							<form:input path="alternateContactMobile" id="alternateContactMobile" cssClass="form-control" />
						</div>
						
						</div>
						
						<div class="row">
						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_ALTERNATE_CONTACT_NAME" text="Alternate contact name"></spring:message></label>
							<form:input path="alternateContactName"  id="alternateContactName" cssClass="form-control" />
						</div>
						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_ALTERNATE_CONTACT_PHONE" text="Alternate contact phone"></spring:message></label>
							<form:input path="alternateContactPhone" id="alternateContactPhone" cssClass="form-control" />
						</div>
						</div>
						
						<div class="row">
						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_ALTERNATE_MAIL_ADDRESS" text="Alternate email address"></spring:message></label>
							<form:input path="alternateeMailAddress"  id="alternateeMailAddress" cssClass="form-control" />
						</div>
							
								<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_STATUS" text="Status"/> <font
								color="red">*</font></label>
					<form:select path="active" id="active" cssClass="dropdown chosen-select">
		                <form:option value="0"><spring:message code="LABEL_SELECT" text="---Please Select---"/></form:option>
		                <form:option value="10"><spring:message code="LABEL_ACTIVE" text="Active"/></form:option>
		                <form:option value="20"><spring:message code="LABEL_DEACTIVATE" text="Inactive"/></form:option>		
		           </form:select>
				</div>
						</div>
						
						
						<div class="row">
						<!-- bugId:505 by:rajlaxmi  for:selected value in dropdown while editing-->
							<div class="col-md-6">
						<label class="col-sm-4"><spring:message
								code="LABEL_COUNTRY" text="Country :" /><font color="red">*</font></label>
								<label class="col-sm-5"><spring:message code="LABEL_COUNTRY_SOUTH_SUDAN" text="SouthSudan" /></label>
								<form:hidden path="countryId" value="1"/>
					</div>
						<div class="col-sm-6">
						<label class="col-sm-4"><spring:message code="LABEL_CITY"
								text="City:" /><font color="red">*</font></label>
					<div id="cities">
								<form:select path="cityId" class="dropdown_big chosen-select" id="cityId">
									<option value="">
										<spring:message code="LABEL_WUSER_SELECT"
											text="--Please Select--"></spring:message>
									</option>
									<form:options items="${City}" itemValue="cityId"
										itemLabel="city"></form:options>
								</form:select>
							</div>
						<font color="RED"> <form:errors path="cityId"></form:errors></font>
					 </div>
						</div>
						<br/>
						
						<div class="row">
						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_MCC" text="MCC" /><font
								color="red">*</font></label>
							<form:select path="mcc" id="mcc" cssClass="dropdown chosen-select">
								<form:option value="">
									<spring:message code="LABEL_SELECT" text="--Please Select--" />
								</form:option>
								<form:options items="${mccList}"
									itemLabel="merchantType" itemValue="mcc"></form:options>
							</form:select>
							<FONT color="red"><form:errors path="mcc" /></FONT>
						</div>
						<div class="col-sm-6">
						<label class="col-sm-4"><spring:message
								code="LABEL_QUARTER" text="Quarter:" /><font color="red">*</font></label>
						<div id="quarters">
							<form:select path="quarterId" class="dropdown_big chosen-select" id="quarterId">
								<option value="">
									<spring:message code="LABEL_WUSER_SELECT"
										text="--Please Select--"></spring:message>
								</option>
								<form:options items="${quarter}" itemValue="quarterId"
									itemLabel="quarter"></form:options>
							</form:select>
						</div>
						<font color="RED"> <form:errors path="quarterId"></form:errors></font><br />
					</div>
						</div>
						
					<div class="row">
				
						<div class="col-sm-6">
						<label class="col-sm-4"><spring:message code="LABEL_ADDRESS" text="Address:" /><font color="red">*</font></label> 
						<form:textarea path="address" cssClass="text_area" onKeyDown="textCounter(document.customerRegistrationForm.address,180,180)"  onKeyUp="textCounter(document.customerRegistrationForm.address,180,180)" style="width: 180px; height: 100px;"></form:textarea> 
						<font color="RED"><form:errors path="address"></form:errors></font>
						</div>

						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_PRIMARY_CONTACT_ADDRESS" text="Primary contact address"></spring:message><font
								color="red">*</font></label>
							<form:textarea path="primaryContactAddress" id="primaryContactAddress" cssClass="textarea" style="width: 180px; height: 100px;"/>
						</div>
					</div>
					<br />

					<div class="row">
						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_ALTERNATE_CONTACT_ADDRESS" text="Alternate contact address"></spring:message>
							</label>
							<form:textarea path="alternateContactAddress"  id ="alternateContactAddress" cssClass="textarea" style="width: 180px; height: 100px;"/>
						</div><br/><br/></div>
						</div>
								</div>
				<div class="box-footer">

			<input type="button" id="submitButton"
				value="<spring:message code="LABEL_SUBMIT" text="Submit"/>"
				onclick="validate();" class="btn btn-primary pull-right"></input>
   <!--by:rajlaxmi for:adding cancel bttn  -->
				<input type="button" class="btn btn-default cancelbttn" 
				value="<spring:message code="LABEL_CANCEL" text="cancel"/>"
				 onclick="cancelForm(<c:out value="${outletDTO.merchantId}"/>);"></input>
				
		</div>
					<br/>
						
			
				
			</div>
		</form:form>
	
</body>

</html>