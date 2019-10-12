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
<!-- Loading Calendar JavaScript files -->
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/zapatec.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/customer.js"></script>
<script>
	$(document).ready(function() {
		$('#signature').change(function() {
			readURL(this, '#signaturePreview');
		});
	});
	$(document).ready(function() {
		$('#idProof').change(function() {
			readURL(this, '#idProofPreview');
		});
	});
	
	function readURL(input, previewId) {

		  if (input.files && input.files[0]) {
		    var reader = new FileReader();

		    reader.onload = function(e) {
		      $(previewId).attr('src', e.target.result);
		    }

		    reader.readAsDataURL(input.files[0]);
		  }
		} 
</script>
<script type="text/javascript">
		var mobileNumlen="0";
		var Alertmsg={
		"title":"<spring:message code="NotNull.customerDTO.title" text="Please select Title"></spring:message>",
		"firstName":"<spring:message code="NotEmpty.customerDTO.firstName" text="Please enter First Name"></spring:message>",
		"mobileNumber":"<spring:message code="NotNull.customerDTO.mobileNumber" text="Please enter Mobile Number"></spring:message>",
		"dob":"<spring:message code="NotNull.customerDTO.dob" text="Please select Date of Birth"></spring:message>",
		"dobValid":"<spring:message code="VALID_DOB" text="Please enter Date of Birth Should  be less than todays Date"></spring:message>",
		"language":"<spring:message code="NotEmpty.customerDTO.language" text="Please select Language"></spring:message>",
		"type":"<spring:message code="NotEmpty.customerDTO.type" text="Please select customer type"></spring:message>",
		"customerProfileId":"<spring:message code="NotEmpty.customerDTO.customerProfileId" text="Please select customer profile"></spring:message>",
		"questionId":"<spring:message code="NotNull.customerDTO.questionId" text="Please select Question"></spring:message>",
		"answer":"<spring:message code="NotEmpty.customerDTO.answer" text="Please enter Your Answer"></spring:message>",
		"countryId":"<spring:message code="NotNull.customerDTO.countryId" text="Please select Country Name"></spring:message>",
		"cityId":"<spring:message code="NotNull.customerDTO.cityId" text="Please select State"></spring:message>",
		"address":"<spring:message code="NotEmpty.customerDTO.address" text="Please enter your Address"></spring:message>",
		"quarterId":"<spring:message code="NotNull.customerDTO.quarterId" text="Please select City/County Name"></spring:message>",
		"signature":"<spring:message code="VALIDATION_SIGNATURE" text="Please select Signature"></spring:message>",
		"idProof":"<spring:message code="VALIDATION_ID_PROOF" text="Please select ID Proof"></spring:message>",
		"signatureLower":"<spring:message code="VALIDATION_SIGNATURE_JPG" text="Please select a jpg file for Signature"></spring:message>",
		"idProofLower":"<spring:message code="VALIDATION_ID_PROOF_JPG" text="Please select a jpg file for Id Proof"></spring:message>",
		"firstNameSpace":"<spring:message code="VALIDATION_FIRSTNAME_SPACE" text="Please remove unwanted spaces in First Name"></spring:message>",
		"mobileNumberSpace":"<spring:message code="VALIDATION_MOBILE_SPACE" text="Please remove unwanted spaces in Mobile Number"></spring:message>",
		"firstNameCharcter":"<spring:message code="VALIDATION_FIRSTNAME_CHARCTERS" text="Please enter only charcters in First Name"/>",
		"mobileNumberCharacter":"<spring:message code="VALIDATION_MOBILE_DIGITS" text="Please enter valid Mobile Number"/>",
		"settlementDateFormat":"<spring:message code="VALID_CUSTOMER_DOB_DATE_FORMAT" text="Date of birth date format must be : dd/mm/yyyy"/>",
		 "validDay":"<spring:message code="VALID_CUSTOMER_DAY" text="Please enter valid  Date of birth"/>",
		 "validMonth":"<spring:message code="VALID_CUSTOMER_MONTH" text="Please  enter  a valid  month in Date of birth"/>",
		 "validSettlementDay":"<spring:message code="VALID_CUSTOMER_DAY" text="Please  enter  a valid  days in  Date of birth "/>",
		 "expiryDateFormat":"<spring:message code="VALID_EXPIRY_DATE_FORMAT" text="Expiry date format must be : dd/mm/yyyy"/>",
		 "validExpiryDay":"<spring:message code="VALID_EXPIRY_DAY" text="Please enter valid  Expiry date"/>",
		 "validExpiryMonth":"<spring:message code="VALID_EXPIRY_MONTH" text="Please  enter  a valid  month in Expiry Date"/>",
		 "validExpiryDays":"<spring:message code="VALID_EXPIRY_DAY" text="Please  enter  a valid  days in  Expiry Date "/>",
		 "issueDateFormat":"<spring:message code="VALID_ISSUE_DATE_FORMAT" text="Issue date format must be : dd/mm/yyyy"/>",
		 "firstnamevalid":"<spring:message code="VALID_FIRSTNAME" text="Please enter only Characters in First Name "/>",
		 "validIssueDay":"<spring:message code="VALID_ISSUE_DAY" text="Please enter valid  Issue date"/>",
		 "validIssueMonth":"<spring:message code="VALID_ISSUE_MONTH" text="Please  enter  a valid  month in Issue Date"/>",
		 "lastName":"<spring:message code="VALID_LASTNAME" text="Please enter only Characters in Last Name "/>",
		 "validIssueDays":"<spring:message code="VALID_ISSUE_DAY" text="Please  enter  a valid  days in  Issue Date "/>",	 			 
			"age18":"Customer age should be minimum",	
		};
		function fileValidate(action){
		  var dob=document.customerRegistrationForm.dob.value;
			 var filetypeSignature = document.customerRegistrationForm.signature.value.substring(document.customerRegistrationForm.signature.value.lastIndexOf("."));
			 var filetypeIdProof = document.customerRegistrationForm.idProof.value.substring(document.customerRegistrationForm.idProof.value.lastIndexOf("."));
			 var firstName=document.getElementById("firstName").value;
			 var placeOfBirth=document.getElementById("placeOfBirth").value;
			 var lastName=document.getElementById("lastName").value;
			 var mobileNumber=document.getElementById("mobileNumber").value;
			 var pattern='^\[a-zA-ZÀ-ÿ\'-. ]{1,30}$';
			 var IdNumberPattern='^\[0-9A-Za-z ]*$';
			 var firstNamePattern='^\[a-zA-ZÀ-ÿ]*$';
			 var addressAlphaNumeric ='/^[A-Za-z0-9 ,\/#&-.]{5,100}$/';
			 var mobilepattern=''; 
			 var mobileNumLen=parseInt(document.getElementById('mobileNum').innerHTML.trim());
			 var address=document.getElementById("address").value;
			 var signature=document.getElementById("signature").value ;
			 var idProof=document.getElementById("idProof").value ;
			 var expiryDate=document.getElementById("expiryDate").value ;
			 var issueDate=document.getElementById("issueDate").value ;
			 var profession=document.getElementById("profession").value ;  
			 var placeOfIssue=document.getElementById("placeOfIssue").value ;  
			 var idNumber=document.getElementById("idNumber").value ;  
			 var idProofRequired = document.getElementById("isIDProofRequired").value ; 
			 var bankCustomerId = document.getElementById("bankCustomerId").value ; 
			 var answer = document.getElementById("answer").value;
			 var emailAddress = document.getElementById("emailAddress").value;

			 if(document.getElementById('countryId').value!=''){
				 mobilepattern='^\[0-9]{'+mobileNumLen+'}$'; 
			 }
			
             if(document.getElementById("title").value=="") {
		         alert(Alertmsg.title);
		         document.customerRegistrationForm.title.focus();
		         return false;
			 }else if(document.getElementById("firstName").value=="") {
		         alert(Alertmsg.firstName);
		         document.customerRegistrationForm.firstName.focus();
		         return false;
			 }else if(firstName.charAt(0) == " " || firstName.charAt(firstName.length-1) == " "){
                 alert(Alertmsg.firstNameSpace);
                 document.customerRegistrationForm.firstName.focus();
                 return false;
             }else if(firstName.search(firstNamePattern)==-1){
         		alert( Alertmsg.firstnamevalid) ;
        		return false;
        	}else if(document.getElementById("lastName").value=="") {
            	  alert('<spring:message code="VALIDATION_LAST_NAME" text="Please enter Last name"/>');
	              document.customerRegistrationForm.lastName.focus();
	              return false;
			 }else if(lastName.search(firstNamePattern)==-1){
	         		alert( Alertmsg.lastName);
	        		return false;
	        	}else if(document.getElementById("mobileNumber").value=="") {
				 /* alert(document.getElementById("mobileNumber").value); */
		         alert(Alertmsg.mobileNumber);
		         document.customerRegistrationForm.mobileNumber.focus();
		         return false;
			 }else if(mobileNumber.charAt(0) == " " || mobileNumber.charAt(mobileNumber.length-1) == " "){
                 alert(Alertmsg.mobileNumberSpace);
                 document.customerRegistrationForm.mobileNumber.focus();
                 return false;
             }else if(document.getElementById('countryId').value!='' && mobileNumber.search(mobilepattern)==-1){
                 alert(Alertmsg.mobileNumberCharacter+" of length "+mobileNumLen);
                 document.customerRegistrationForm.mobileNumber.focus();
                 return false;
             }else if(!checkAllZero(document.getElementById("mobileNumber").value)) {
		         alert(Alertmsg.mobileNumberCharacter);
		         document.customerRegistrationForm.mobileNumber.focus();
		         return false;
			 }else if(document.getElementById("dob").value=="") {
		         alert(Alertmsg.dob);
		         document.customerRegistrationForm.dob.focus();
		         return false;
			 }else if(!checkDateOfBirth(dob)){
			     	return false;  
			   }else if(!compareDod(dob)){
	             alert(Alertmsg.dobValid);
                 return false;
                }else if(placeOfBirth.search(firstNamePattern)==-1){
                	alert("Please enter valid place of birth");
                }else if(document.getElementById("language").value=="") {
		         alert(Alertmsg.language);
		         document.customerRegistrationForm.language.focus();
		         return false;
			 }else if(document.getElementById("type").value=="") {
		         alert(Alertmsg.type);
		         document.customerRegistrationForm.type.focus();
		         return false;
			 }else if(document.getElementById("customerProfileId").value=="") {
		         alert(Alertmsg.customerProfileId);
		         document.customerRegistrationForm.customerProfileId.focus();
		         return false;
			 }else if(document.getElementById("questionId").value=="" || document.getElementById("questionId").value==" ") {
		         alert(Alertmsg.questionId);
		         document.customerRegistrationForm.questionId.focus();
		         return false;
			 }else if(document.getElementById("questionId").value=="select") {
				 alert(Alertmsg.questionId);
		         return false;
			 }else if(answer=="") {
		         alert(Alertmsg.answer);
		         document.customerRegistrationForm.answer.focus();
		         return false;
			 }else if(answer.charAt(0) ==" " || answer.charAt(idNumber.length-1) == " "){
                 alert('<spring:message code="ANSWER_SPACE" text="Please remove white space in answer"/>');
		         document.customerRegistrationForm.answer.focus();
                 return false;
				 
			 }else if (checkEmail(emailAddress) == false) {
					alert('<spring:message code="VALIDATION_CLPOOL_EMAIL" text="Please enter valid Email ID  "/>');
					return false;
			 } else if(document.getElementById("countryId").value=="" || document.getElementById("countryId").value==" " ) {
		        alert(Alertmsg.countryId);
		         return false;
			 }else if(document.getElementById("cityId").value=="" || document.getElementById("cityId").valu==" ") {
		         alert(Alertmsg.cityId);
		         return false;
			 }else if(document.getElementById("cityId").value=="select") {
		         alert(Alertmsg.cityId);
		        
		         return false;
			 }else if(document.getElementById("address").value=="") {
		         alert(Alertmsg.address);
		         document.customerRegistrationForm.address.focus();
		         return false;
			 }
             /* <!--  Author name <vinod joshi>, Date<7/5/2018>, purpose of change < bcz of this address not validating properly> ,
 			//Start--> */
             /* else if(address.search(firstNamePattern)==-1){
	         		alert("Please enter valid address");
	        		return false;
	        	} */else if(document.getElementById("quarterId").value=="" || document.getElementById("quarterId").value==" ") {
		         alert(Alertmsg.quarterId);
		         //document.customerRegistrationForm.quarterId.focus();
		         return false;
		     }else if(document.getElementById("quarterId").value=="select") {
		         alert(Alertmsg.quarterId);
		         //document.customerRegistrationForm.quarterId.focus();
		         return false;
		     }else if(bankCustomerId !=""){
		    	 if(bankCustomerId.search(IdNumberPattern)==-1){
	                 alert('<spring:message code="VALIDATION_BANK_ID_NUMBER" text="Please enter valid Bank Customer ID"/>');
	                 document.customerRegistrationForm.idNumber.focus();
	                 return false;
		    	 }
		    	 if(bankCustomerId.charAt(0) ==" " || bankCustomerId.charAt(bankCustomerId.length-1) == " "){
	                 alert('<spring:message code="VALIDATION_BANK_ID_NUMBER" text="Please enter valid Bank Customer ID"/>');
	                 document.customerRegistrationForm.bankCustomerId.focus();
	                 return false;
		    	 }
		    	 if(!checkAllZero(bankCustomerId)){
		    		 alert('<spring:message code="VALIDATION_BANK_ID_NUMBER" text="Please enter valid Bank Customer ID"/>');
	                 document.customerRegistrationForm.bankCustomerId.focus();
	                 return false;
		    	 } 
		     }else if(idNumber !=""){
		    	 if(idNumber.search(IdNumberPattern)==-1){
	                 alert('<spring:message code="VALIDATION_ID_NUMBER" text="Please enter valid id number"/>');
	                 document.customerRegistrationForm.idNumber.focus();
	                 return false;
		    	 }
		    	 if(idNumber.charAt(0) ==" " || idNumber.charAt(idNumber.length-1) == " "){
	                 alert('<spring:message code="VALIDATION_SPACE_ID_NUMBER" text="Please remove white space in id number"/>');
	                 document.customerRegistrationForm.idNumber.focus();
	                 return false;
		    	 }
		    	 if(!checkAllZero(idNumber)){
		    		 
		    		 alert('<spring:message code="VALIDATION_ID_NUMBER" text="Please enter valid id number"/>');
	                 document.customerRegistrationForm.idNumber.focus();
	                 return false;
		    	 } 
		     }
             if(placeOfIssue !=""){
			    	 if(placeOfIssue.search(firstNamePattern)==-1){
		                 alert( '<spring:message code="VALIDATION_PLACEOFISSSUE" text="Please enter valid Name in place of issue"/>');
		                 document.customerRegistrationForm.placeOfIssue.focus();
		                 return false;
			    		 }
			    	 if(placeOfIssue.charAt(0) ==" " || placeOfIssue.charAt(placeOfIssue.length-1) == " "){
		                 alert('<spring:message code="VALIDATION_SPACE_PLACE_ISSUE" text="Please remove white space in place of issue"/>');
		                 document.customerRegistrationForm.placeOfIssue.focus();
		                 return false;
			    	 }
			    	 if(!checkAllZero(placeOfIssue)){
			    		 alert( '<spring:message code="VALIDATION_PLACEOFISSSUE" text="Please enter valid Name in place of issue"/>');
		                 document.customerRegistrationForm.placeOfIssue.focus();
		                 return false;
			    	 } 
			   } 
             if(expiryDate!=""){
				   
			 		 if(!expiryDateValid(expiryDate)){
				     	return false;  
				   }
		     } if(issueDate !=""){
		 		 if(!issueDateValid(issueDate)){
		 			 
				     	return false;  
				   }
		     } if(expiryDate!="" && issueDate !=""){
		    	
		 		 if(!compareExpiryIssueDate(issueDate,expiryDate)){
		 			 alert('<spring:message code="VALIDATION_ISSUE_DATE" text="Please enter issue date less than expiry date"/>');
				     	return false;  
				   }
		     } 
			 else if(signature!=""){
					 if(action=='ADD' && (filetypeSignature.toLowerCase() != ".jpg")){
					     alert(Alertmsg.signatureLower);
					     document.customerRegistrationForm.signature.focus();
					     return false;
					 }
			}else if(idProof !=""  ) {
					
					 if(action=='ADD' && (filetypeIdProof.toLowerCase() != ".jpg")){
						 alert(Alertmsg.idProofLower);
						 document.customerRegistrationForm.idProof.focus();
						 return false;
					 }
			     }
		     
		     if(idProofRequired == "1"){
		    	 
		    	 if(signature == ""){
		    		 alert('<spring:message code="VALIDATION_SIGNATURE" text="Please select signature"/>');
		    		 return false;
		    	 }else if(idProof == ""){
		    		 alert('<spring:message code="VALIDATION_ID_PROOF" text="Please select the id proof"/>');
		    		 return false;
		    	 }
		     }
		    
             if(address.charAt(0) == " " || address.charAt(address.length-1) == " "){
				 alert('<spring:message code="LABEL.ADDRESS.BLANK" text="Please remove the white space from address"/>'); 
				 return false; 	 
		     }else if(address.length > 180){
			     alert('<spring:message code="LABEL.ADDRESS.LENGTH" text="Address should not exceed more than 180 characters"/>');        
			     return false;  	 
			} 
		     
		     else{
		    	 document.customerRegistrationForm.action = 'saveCustomer.htm';
				 document.customerRegistrationForm.submit();
			 }
			 }
		
		function textCounter(field,cntfield,maxlimit) {
			 if (field.value.length > maxlimit) // if too long...trim it!
			 field.value = field.value.substring(0, maxlimit);
			 // otherwise, update 'characters left' counter
			 else
			 cntfield.value = maxlimit - field.value.length;
		 }
		function checkAllZero(value){
			
			var count=0;
			for (var i = 0; i < value.length; i++) {
			   
				if(value.charAt(i)==0){
					count++;
				}
			}
			if(count==value.length){
				return false;
			}else{
				return true;
			}

		}
		function selectProfession1(){
			
			 var professionPattern='^\[a-zA-ZÀ-ÿ\' ]{1,30}$';
			 if(document.getElementById("profession").value=='Others'){
				 //document.getElementById("others").value="";
				 
				 var pothers=prompt('<spring:message code="VALIDATION_PROFESSION" text="Please enter your profession"/>' );
				 if(pothers.charAt(0) == " " || pothers.charAt(pothers.length-1) == " " ){
					  alert( '<spring:message code="VALIDATION_VALID_PROFESSION_SPACE" text="Please remove unwanted space in profession"/>' );
					  //document.customerRegistrationForm.others.value=pothers;
					  document.getElementById("others").value="";
					  pothers=null;
					 enterProfession();
				 } 
				 
				 if(pothers.search(professionPattern) ==-1 ){
					  alert( '<spring:message code="VALIDATION_VALID_PROFESSION" text="Please enter valid profession"/>' );
					  //document.customerRegistrationForm.others.value=pothers;
					  document.getElementById("others").value="";
					  pothers=null;
					 enterProfession();
				 } 
					if (pothers!=null && pothers!="")
					  {						
					    document.customerRegistrationForm.others.value=pothers;
						
					  } 


		}
		}
		
		function enterProfession(){
			
			selectProfession1();
		}
		function selectIdtype(){
		 var IdNumberPattern='^\[a-zA-ZÀ-ÿ\' ]{1,30}$';
		 if(document.getElementById("idType").value=='Others'){
			 var iothers=prompt('<spring:message code="VALIDATION_ID_TYPE" text="Please enter your IDType"/>' );
			 
			 if(iothers.charAt(0) == " " || iothers.charAt(iothers.length-1) == " " ){
				  alert('<spring:message code="VALIDATION_VALID_ID_TYPE_SPACE" text="Please remove  unwanted white space  in IDType"/>' );
				 
				  iothers=null;
				  enterIDType();
				  
			 } 
			 if(iothers.search(IdNumberPattern) ==-1 ){
				  alert('<spring:message code="VALIDATION_VALID_ID_TYPE" text="Please enter valid IDType"/>' );
				 
				  iothers=null;
				  enterIDType();
				  
			 } 
			 if (iothers!=null && iothers!="")
				  {
					 document.customerRegistrationForm.IDTypeothers.value=iothers;
					
				  } 


	}
	}
		function checkEmail() {
			var email = document.getElementById('emailAddress');
			var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
			if (email.value != "" && !filter.test(email.value)) {
				return false;
			} else {
				return true;
			}
		}
		
		function checkAge(){

		
		if (document.customerRegistrationForm.dob.value != "") {
			t1 = document.customerRegistrationForm.dob.value;

			if (getAge(t1) < 18) {
				alert(Alertmsg.age18 + " 18 years.");
				document.customerRegistrationForm.dob.value = "";
				
				return false;
			} else {
				return true;
			}
		}
	}
		function getAge(dateString) {
		var today = new Date();
		var myDate = dateString.split("/");
		var convertDate = myDate[2] + "-" + myDate[1] + "-" + myDate[0];

		var birthDate = new Date(convertDate);

		var age = today.getFullYear() - birthDate.getFullYear();

		var m = today.getMonth() - birthDate.getMonth();
		if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
			age--;
		}

		return age;
	}
function enterIDType(){
			
	selectIdtype();
}	
function clearForm(){
	
	

	document.customerRegistrationForm.reset();
	document.customerRegistrationForm.title.value="";
	document.customerRegistrationForm.language.value="";
	document.customerRegistrationForm.customerProfileId.value="";
	document.customerRegistrationForm.questionId.value="";
//	document.customerRegistrationForm.type.value="";
	document.customerRegistrationForm.cityId.value="";
	document.customerRegistrationForm.quarterId.value="";
	document.customerRegistrationForm.profession.value="";
	document.customerRegistrationForm.idType.value="";
	document.customerRegistrationForm.customerProfileId.value="";
	document.customerRegistrationForm.language.value="";
	document.customerRegistrationForm.country.value="";
	
	
}
		
</script>
</head>
<body>
<div class="row">
<div class="col-lg-12">
<div class="box">
	<div class="box-header">
		<h3 class="box-title">
			<span><spring:message code="TITLE_BUSINESS_PARTNER" text="Business Partner"></spring:message></span>
		</h3>
	</div>
	<div class="col-sm-5 col-sm-offset-4">
		<div class="col-sm-12 col-sm-offset-5" style="color: #ba0101; font-weight: bold; font-size: 12px;">
			<spring:message code="${message}" text="" />
		</div>	
	</div>
	<form:form method="POST" class="form-inline" commandName="BusinessPartnerDTO" name="businessRegistrationForm" id="businessRegistrationForm" enctype="multipart/form-data">
	<jsp:include page="csrf_token.jsp"/>
		<form:hidden path="type" value="${customerDTO.type}" id="type"/>
	<div class="col-sm-6 col-sm-offset-10">
		<%-- <a href="javascript:submitForm('customerRegistrationForm','searchCustomerPage.htm?pageNumber=1')"><strong><spring:message code="LINK_SEARCH_CUSTOMERS" text="Search Customers"></spring:message></strong></a> --%>
		<a href="javascript:submitForm('BusinessPartnerRegistrationForm','businessPartner.htm')"><strong><spring:message code="LINK_SEARCH_BusinessPartners" text="Search Business Partners"></spring:message></strong></a>
	</div>
	<!-- <div class="box-body"> -->
			<div class="row">
			<%-- <form:hidden path="city" />
			<form:hidden path="quarter" />
			<form:hidden path="question" />
			<form:hidden path="countryId" />
			<form:hidden path="others" />
			<form:hidden path="IDTypeothers" /> --%>
			<div id="mobileNum" style="visibility:hidden"></div>
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_BUSINESS_PARTNER_TITLE" text="Title:"></spring:message><font color="red">*</font></label> 
					<form:select path="title">
					<form:option value="">
						<spring:message code="LABEL_WUSER_SELECT"
							text="--Please Select--"></spring:message>
					</form:option>
					<form:option value="Mr">
						<spring:message code="LABEL_USER_MR" text="Mr."></spring:message>
					</form:option>
					<form:option value="Ms">
						<spring:message code="LABEL_USER_MS" text="Ms."></spring:message>
					</form:option>
					<form:option value="Mrs">
						<spring:message code="LABEL_USER_Mrs" text="Mrs."></spring:message>
					</form:option>
					<form:option value="Dr">
						<spring:message code="LABEL_USER_Dr" text="Dr."></spring:message>
					</form:option>
					<form:option value="MADAME">
						<spring:message code="LABEL_USER_MADAME" text="MADAME."></spring:message>
					</form:option>
					<form:option value="MADAME et MADAME">
						<spring:message code="LABEL_USER_MADAME_MADAME" text="MADAME et MADAME."></spring:message>
					</form:option>
					<form:option value="MADAME ou MADAME">
						<spring:message code="LABEL_USER_MADAME_OU_MADAME" text="MADAME ou MADAME."></spring:message>
					</form:option>
					<form:option value="MADEMOISELLE">
						<spring:message code="LABEL_USER_MADEMOISELLE" text="MADEMOISELLE."></spring:message>
					</form:option>
					<form:option value="MONSEIGNEUR">
						<spring:message code="LABEL_USER_MONSEIGNEUR" text="MONSEIGNEUR."></spring:message>
					</form:option>
					<form:option value="MONSIEUR">
						<spring:message code="LABEL_USER_MONSIEUR" text="MONSIEUR."></spring:message>
					</form:option>
					<form:option value="MONSIEUR et MONSIEUR">
						<spring:message code="LABEL_USER_MONSIEUR_MONSIEUR" text="MONSIEUR et MONSIEUR."></spring:message>
					</form:option>
					<form:option value="MONSIEUR ou MONSIEUR">
						<spring:message code="LABEL_USER_MONSIEUR_OU_MONSIEUR" text="MONSIEUR ou MONSIEUR."></spring:message>
					</form:option>
					<form:option value="MONSIEUR et Mlle">
						<spring:message code="LABEL_USER_MONSIEUR_Mlle" text="MONSIEUR et Mlle."></spring:message>
					</form:option>																
					<form:option value="MONSIEUR ou Mle">
						<spring:message code="LABEL_USER_MONSIEUR_OU_Mle" text="MONSIEUR ou Mle."></spring:message>
					</form:option>
					<form:option value="MR & MME">
						<spring:message code="LABEL_USER_MR_MME" text="MR & MME."></spring:message>
					</form:option>
					<form:option value="MR OU MME">
						<spring:message code="LABEL_USER_MR OU MME" text="MR OU MME."></spring:message>
					</form:option>
					
				</form:select> <font color="RED"><form:errors path="title"></form:errors></font>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_FIRST_NAME" text="First Name:" /><font color="red">*</font></label> 
					<form:input path="firstName" maxlength="30" cssClass="form-control" ></form:input> 
					<font color="RED"><form:errors path="firstName"></form:errors></font>
					<form:hidden path="customerId" />
				</div>			
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_MIDDLE_NAME" text="Middle Name:"></spring:message></label> 
					<form:input path="middleName" maxlength="30" cssClass="form-control" ></form:input>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_LAST_NAME" text="Last Name:"></spring:message><font color="red">*</font></label>
					<form:input path="lastName" maxlength="30" cssClass="form-control" ></form:input> 
				</div>			
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_MOBILE_NO" text="MobileNo.:"></spring:message><font color="red">*</font></label>
					<form:input path="mobileNumber"  cssClass="form-control" ></form:input>
					<font color="RED"><form:errors path="mobileNumber"></form:errors></font> 
				</div>
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_GENDER" text="Gender:" /><font color="red">*</font></label> 
					<form:radiobutton path="gender" value="Male" selected="true" /> <spring:message code="LABEL_MALE" text="Male" /> &nbsp; &nbsp;
					<form:radiobutton path="gender" value="Female" /> <spring:message code="LABEL_FEMALE" text="Female" /> 
					<font color="red"><form:errors path="gender" /></font>
				</div>			
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_DOB" text="Date Of Birth:" /><font color="red">*</font></label> 
					<form:input path="dob"  cssClass="form-control datepicker" id="dob" onfocus="this.blur()" onchange="javascript:checkAge();"/>
				
				</div>
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_POB" text="Place Of Birth:" /></label> 
					<form:input path="placeOfBirth" cssClass="form-control" ></form:input>
				</div>			
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_LANGUAGE" text="Language" /><font color="red">*</font></label> 
					<form:select path="language"  id="language" multiple="false">
						<form:option value="">
							<spring:message code="LABEL_WUSER_SELECT"
								text="--Please Select--"></spring:message>
						</form:option>
						<form:option value="en_US">
							<spring:message code="LABEL_LANG_ENGLISH" text="English"></spring:message>
						</form:option>
						<form:option value="fr_FR">
							<spring:message code="LABEL_LANG_FRENCH" text="French"></spring:message>
						</form:option>
						<form:option value="pt_PT">
							<spring:message code="LABEL_LANG_PORTUGUESE" text="Português"></spring:message>
						</form:option>
					</form:select> <font color="RED"><form:errors path="language"></form:errors></font>
				</div>
				<%-- <c:if test="${role ne 23 && role ne 24 && role ne 25 && role ne 26 }"> --%>
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_CUSTOMER_PROFILE" text="Customer Profile" /><font color="red">*</font></label> 
					<form:select path="customerProfileId"  multiple="false">
						<form:option value="">
							<spring:message code="LABEL_WUSER_SELECT"
								text="--Please Select--"></spring:message>
						</form:option>
						<form:options items="${masterData.customerProfileList}" itemLabel="profileName" itemValue="profileId"/>
					</form:select> <font color="RED"><form:errors path="customerProfileId"></form:errors></font>
				</div>	
			<%-- 	</c:if>	 --%>	
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_SECURE_QUEST" text="Security Question:" /><font color="red">*</font></label> 
					<div id="questions">
						<form:select path="questionId"  id="questionId" multiple="false">
							<option value=""><spring:message
								code="LABEL_WUSER_SELECT" text="--Please Select--"></spring:message></option>
							<form:options items="${questionList}"
								itemLabel="question" itemValue="questionId" />
						</form:select> 
						<font color="RED"></font><form:errors path="questionId" cssClass="" /></font>
					</div>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_ANSWER" text="Answer:" /><font color="red">*</font></label> 
					<form:input path="answer" maxlength="30" cssClass="form-control" ></form:input> 
					<font color="RED"><form:errors path="answer" cssClass="" /></font>
				</div>			
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_ACCTYPE" text="type" /></label> 
					<label class="col-sm-4" id="type">
					 <c:if test="${customerDTO.type == 0 }">
						 <spring:message code="LABEL_CUSTOMER" text="Customer"></spring:message></c:if></label>
					<%-- <form:select path="type"  id="type" multiple="false">
	                <form:option value=""><spring:message code="LABEL_SELECT" text="---Please Select---"/></form:option>				                                         
                                      <form:option value="0"><spring:message code="LABEL_CUSTOMER" text="Customer"/></form:option>	
                                      <form:option value="1"><spring:message code="LABEL_MERCHANT" text="Merchant"/></form:option>
                                      <form:option value="2"><spring:message code="LABEL_SOLE_MERCHANT" text="Sole-Merchant"/></form:option>
	               </form:select> <font color="RED"><form:errors path="type"></form:errors></font> --%>
				</div>
				<%-- <label class="col-sm-4"><<spring:message code="LABEL_ACCTYPE" text="Type:" /><font color="red">*</font></label> 
					<form:radiobutton path="type" value="0" selected="true" /> <spring:message code="LABEL_CUSTOMER" text="No" /> 
					<form:radiobutton path="type" value="2" /> <spring:message code="LABEL_SOLE_MERCHANT" text="No" /> 
					<font color="red"><form:errors path="type" /></font>--%>
				 
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_EMAILID" text="Email Id:" /></label>
					<form:input path="emailAddress" maxlength="32" cssClass="form-control" ></form:input> 
					<font color="RED"><form:errors path="emailAddress"></form:errors></font>
				</div>
				</div>			
			
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_COUNTRY" text="Country :" /><font color="red">*</font></label> 
					<select  id="country" name="countryId">
                       <option value=""><spring:message code="LABEL_SELECT" text="--Please Select--" /></option>
	                     <c:set var="lang" value="${language}"></c:set>
				            <c:forEach items="${masterData.countryList}" var="country">
				            <c:forEach items="${country.countryNames}" var="cNames">'
							   <c:if test="${cNames.comp_id.languageCode==lang }">
							     <option value="<c:out value="${country.countryId}"/>"  <c:if test="${country.countryId eq customerDTO.countryId}" >selected=true</c:if> > 
							        <c:out value="${cNames.countryName}"/>	
						         </option>
							  </c:if>										
				            </c:forEach>
				            </c:forEach>														
			       </select> <font color="RED"><form:errors path="countryId" cssClass="" /></font>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_CITY" text="City:" /><font color="red">*</font></label> 
					<div id="cities"><form:select path="cityId" id="cityId">
						<option value=""><spring:message
							code="LABEL_WUSER_SELECT" text="--Please Select--"></spring:message></option>
						<form:options items="${City}" itemValue="cityId"
							itemLabel="city"></form:options>
					</form:select></div>
					<font color="RED"><form:errors path="cityId"></form:errors></font>
				</div>			
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_ADDRESS" text="Address:" /><font color="red">*</font></label> 
					<form:textarea path="address" cssClass="form-control" onKeyDown="textCounter(document.customerRegistrationForm.address,180,180)"  onKeyUp="textCounter(document.customerRegistrationForm.address,180,180)" style="width: 180px; height: 100px;"></form:textarea> 
					<font color="RED"><form:errors path="address"></form:errors></font>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_QUARTER" text="Quarter:"/><font color="red">*</font></label> 
					<div id="quarters"><form:select path="quarterId" id="quarterId">
						<option value=""><spring:message
							code="LABEL_WUSER_SELECT" text="--Please Select--"></spring:message></option>
						<form:options items="${quarter}" itemValue="quarterId" itemLabel="quarter"></form:options>
					</form:select></div>
					<font color="RED"><form:errors path="quarterId"></form:errors></font><br />
				</div>
			</div>
			<div class="row">
			<div class="col-md-6">
				<label class="col-sm-4"><spring:message	code="LABEL_BANK_CUSTOMER_ID" text="Bank Customer ID"></spring:message></label>
				<form:input path="bankCustomerId" maxlength="20" cssClass="form-control"></form:input>
			</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_PROFESSION" text="Profession" /></label> 
					<form:select path="profession" multiple="false" onchange="selectProfession1();">
						<form:option value="">
							<spring:message code="LABEL_WUSER_SELECT"
								text="--Please Select--"></spring:message>
						</form:option>
						<form:option value="Doctor">
							<spring:message code="LABEL_DOCTOR" text="Doctor"></spring:message>
						</form:option>
						<form:option value="Employee">
							<spring:message code="LABEL_EMPLOYEE" text="Employee"></spring:message>
						</form:option>
						<form:option value="Farmer">
							<spring:message code="LABEL_FARMER" text="Farmer"></spring:message>
						</form:option>
						<form:option value="Gendarme">
							<spring:message code="LABEL_GENDARME" text="Gendarme"></spring:message>
						</form:option>
						<form:option value="Health Staff Magistrate">
							<spring:message code="LABEL_HEALTH_STAFF_MAGISTRATE" text="Health Staff Magistrate"></spring:message>
						</form:option>
						<form:option value="Journalist">
							<spring:message code="LABEL_JOURNALIST" text="Journalist"></spring:message>
						</form:option>
						<form:option value="Lawer">
							<spring:message code="LABEL_LAWYER" text="Lawer"></spring:message>
						</form:option>
						<form:option value="Magistrate">
							<spring:message code="LABEL_MAGISTRATE" text="Magistrate"></spring:message>
						</form:option>
						<form:option value="Managing Director">
							<spring:message code="LABEL_MANAGING_DIRECTOR" text="Managing Director"></spring:message>
						</form:option>
						<form:option value="Merchant">
							<spring:message code="LABEL_MERCHANT" text="Merchant"></spring:message>
						</form:option>
						<form:option value="Military">
							<spring:message code="LABEL_MILITARY" text="Military"></spring:message>
						</form:option>
						<form:option value="Officer">
							<spring:message code="LABEL_OFFICER" text="Officer"></spring:message>
						</form:option>
						<form:option value="Pharmacist">
							<spring:message code="LABEL_PHARMACIST" text="Pharmacist"></spring:message>
						</form:option>
						<form:option value="Policeman">
							<spring:message code="LABEL_POLICE" text="Police"></spring:message>
						</form:option>
						<form:option value="Retired">
							<spring:message code="LABEL_RETIRED" text="Retired"></spring:message>
						</form:option>
						<form:option value="Merchant">
							<spring:message code="LABEL_SOLE_MERCHANT" text="Merchant"></spring:message>
						</form:option>
						<form:option value="Student">
							<spring:message code="LABEL_STUDENT" text="Student"></spring:message>
						</form:option>
						<form:option value="Student(University)">
							<spring:message code="LABEL_STUDENT_UNIVERSITY" text="Student(University)"></spring:message>
						</form:option>
						<form:option value="Teacher">
							<spring:message code="LABEL_TEACHER" text="Teacher"></spring:message>
						</form:option>
						<form:option value="Unemplyed">
							<spring:message code="LABEL_UNEMPLOYED" text="Unemplyed"></spring:message>
						</form:option>
						<form:option value="Worker">
							<spring:message code="LABEL_WORKER" text="Worker"></spring:message>
						</form:option>
						<form:option value="Others">
							<spring:message code="LABEL_OTHERS" text="Others"></spring:message>
						</form:option>
					</form:select>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_IDTYPE" text="IDType" /></label> 
					<form:select path="idType"  multiple="false" onchange="selectIdtype();">
						<form:option value="">
							<spring:message code="LABEL_WUSER_SELECT"
								text="--Please Select--"></spring:message>
						</form:option>
						<form:option value="PassPort">
							<spring:message code="LABEL_PASSPORT" text="PassPort"></spring:message>
						</form:option>
						<form:option value="National Identity">
							<spring:message code="LABEL_NATIONAL_IDENTITY" text="National Identity"></spring:message>
						</form:option>
						<form:option value="Driving License">
							<spring:message code="LABEL_DRIVING_LISENCE" text="Driving License"></spring:message>
						</form:option>
						<form:option value="Identity Proof">
							<spring:message code="LABEL_IDENTITY_PROOF" text="Identity Proof"></spring:message>
						</form:option>
						<form:option value="Consular Card">
							<spring:message code="LABEL_CONSULAR_CARD" text="Consular Card"></spring:message>
						</form:option>
						<form:option value="Others">
							<spring:message code="LABEL_OTHERS" text="Others"></spring:message>
						</form:option>
					</form:select>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_ID_NUMBER" text="IDNumber" /></label> 
					<form:input path="idNumber" maxlength="20" cssClass="form-control" ></form:input>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_PLACE_OF_ISSUE" text="Place Of Issue" /></label> 
					<form:input path="placeOfIssue" maxlength="30" cssClass="form-control" ></form:input>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_ISSUE_DATE" text="IssueDate" /></label> 
					<form:input path="issueDate" id="issueDate" maxlength="10" cssClass="form-control datepicker" ></form:input>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_EXPIRY_DATE" text="Expiry Date" /></label> 
					<form:input path="expiryDate" id="expiryDate" maxlength="10" cssClass="form-control datepicker" ></form:input>
				</div>
			</div>
			
			<c:if test="${customerDTO.isIDProofRequired == 0 }">
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_SIGNATURE" text="Signature:" /> <c:if test="${(customerDTO.customerId eq null) }"></c:if></label> 
					<form:input path="signature" type="file" id="signature"/>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_ID_PROOF" text="ID Proof:"/> <c:if test="${(customerDTO.customerId eq null) }"></c:if></label> 
					<form:input path="idProof" type="file" id="idProof" />
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_PROFILE_PHOTO" text="Profile Photo:" /> <c:if test="${(customerDTO.customerId eq null) }"></c:if></label> 
					<form:input path="profilePhoto" type="file" id="profilePhoto"/>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_ADDRESS_PROOF" text="Address Proof:"/> <c:if test="${(customerDTO.customerId eq null) }"></c:if></label> 
					<form:input path="addressProof" type="file" id="addressProof" />
				</div>
			</div>
			</c:if>
			<c:if test="${customerDTO.isIDProofRequired == 1 }">
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_SIGNATURE" text="Signature:" /> <c:if test="${(customerDTO.customerId eq null) }"></c:if></label> 
					<form:input path="signature" type="file" id="signature"/>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_ID_PROOF" text="ID Proof:"/> <c:if test="${(customerDTO.customerId eq null) }"></c:if></label> 
					<form:input path="idProof" type="file" id="idProof" />
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_PROFILE_PHOTO" text="Profile Photo:" /> <c:if test="${(customerDTO.customerId eq null) }"></c:if></label> 
					<form:input path="profilePhoto" type="file" id="profilePhoto"/>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_ADDRESS_PROOF" text="Address Proof:"/> <c:if test="${(customerDTO.customerId eq null) }"></c:if></label> 
					<form:input path="addressProof" type="file" id="addressProof" />
				</div>
			</div>
			</c:if>
			<c:if test="${customerDTO.isIDProofRequired == 3 }">
			<div class="row">
             <div style="visibility: hidden;" id="proofs">
				<form:input path="signature" type="file" id="signature" />
				<form:input path="idProof" type="file" id="idProof"/>
				<form:input path="profilePhoto" type="file" id="profilePhoto" />
				<form:input path="addressProof" type="file" id="addressProof"/>
			</div>
			</div>
			</c:if>
			<c:if test="${customerDTO.customerId ne null }">
			<div class="row">
				<div class="col-sm-6">
                    <label class="col-sm-4">Previous Others:</label> 
					<span><img id="signaturePreview" width="50px" height="50px" class="img_resize_for_entity" src="<%=request.getContextPath()%>/getPhoto.htm?type=signature&customerId=<c:out value="${customerDTO.customerId}"></c:out>"/></span>
                 </div>
                 <div class="col-sm-6">
                    <label class="col-sm-4">Previous ID Proof:</label> 
					<span><img id="idProofPreview" width="50px" height="50px" class="img_resize_for_entity" src="<%=request.getContextPath()%>/getPhoto.htm?type=idproof&customerId=<c:out value="${customerDTO.customerId}" />"/></span>
                 </div>
              </div>
			<div class="row">
				<div class="col-sm-6">
                    <label class="col-sm-4">Previous profile Photo:</label> 
					<span><img id="signaturePreview" width="50px" height="50px" class="img_resize_for_entity" src="<%=request.getContextPath()%>/getPhoto.htm?type=profilePhoto&customerId=<c:out value="${customerDTO.customerId}"></c:out>"/></span>
                 </div>
                 <div class="col-sm-6">
                    <label class="col-sm-4">Previous Address Proof:</label> 
					<span><img id="addressProof" width="50px" height="50px" class="img_resize_for_entity" src="<%=request.getContextPath()%>/getPhoto.htm?type=addressProof&customerId=<c:out value="${customerDTO.customerId}" />"/></span>
                 </div>
              </div>
             </c:if>
			
			<input type="hidden" name="selectCity" id="selectCity"></input>
			<input type="hidden" name="selectQuarter" id="selectQuarter"></input> 
			<c:choose>
				<c:when test="${(customerDTO.customerId eq null) }">
					<c:set var="buttonName" value="LABEL_ADD" scope="page" />
					<c:set var="action" value="ADD" scope="page" />
				</c:when>
				<c:otherwise>
					<c:set var="buttonName" value="LABEL_UPDATE" scope="page" />
					<c:set var="action" value="EDIT" scope="page" />
				</c:otherwise>
			</c:choose> 
			<div class="col-md-3 col-md-offset-10">
			<input type="button" id="submitButton" class="btn btn-primary" value="<spring:message code="${ buttonName }" text="${ buttonName }"/>" onclick="fileValidate('${action}');"></input>
				<c:if test="${(customerDTO.customerId eq null) }">
				 <input type="button" class="btn btn-default" value="<spring:message code="LABEL_RESET" text="reset"/>" onclick="clearForm();"></input>
				</c:if>
				<c:if test="${(customerDTO.customerId ne null) }">
				 <input type="button" class="btn btn-default" value="<spring:message code="LABEL_CANCEL" text="cancel"/>" onclick="cancelForm();"></input>
				</c:if>
			</div>	<br /> <br />
				<form:hidden path="isIDProofRequired" value="${customerDTO.isIDProofRequired}" id="isIDProofRequired"/>		
					
			
			<br/>
			
			</form:form>
			</div>
</div>
</div>

			<script type="text/javascript">
						var mobileNumberlen='${mobileNumLength}';
						document.getElementById('mobileNum').innerHTML=mobileNumberlen;
						window.onload=function(){
						check();
						issueDate();
						expiryDate();
						};
			</script>
</body>
</html>
