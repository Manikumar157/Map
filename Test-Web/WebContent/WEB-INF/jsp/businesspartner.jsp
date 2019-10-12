<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>

<html>
<head>
<!-- Loading Calendar JavaScript files -->
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/zapatec.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/customer.js"></script>

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
		 
		 "name":"<spring:message code="VALID_NAME" text="Please enter the Name "/>",
		 "nameLength":"<spring:message code="VALID_NAME_LENGTH" text="Name should be minimum 2 characters"/>",
		 "NameAlphabet":"<spring:message code="ERROR_MESSAGE_LAST_NAME_ALPHABET" text=" Name should contain only alphabets."></spring:message>",
		 "namePatt":"<spring:message code="ERROR_MESSAGE_CONTACT_NAME_ALPHABET" text=" Contact Name should contain only alphabets."></spring:message>",
		 "nameWhiteSpac":"<spring:message code="ERROR_MESSAGE_NAME_TWO_WHITE_SPACES" text="should not contain two consecutive white spaces."></spring:message>",
		 "spclName":"<spring:message code="ERROR_MESSAGE_NAME_SPECIAL_CHAR" text=" Name should not contain two consecutive white spaces."></spring:message>",	
		 "contactnamePatt":"<spring:message code="ERROR_MESSAGE_CONTACTNAME_ALPHABET" text=" Name should contain only alphabets."></spring:message>",
		 "contactnameLength":"<spring:message code="VALID_CONTACTNAME_LENGTH" text=" Contact Name should be minimum 2 characters."/>",
		 "contactnameWhiteSpac":"<spring:message code="ERROR_MESSAGE_CONTACT_NAME_TWO_WHITE_SPACES" text="Contact Name should not contain two consecutive white spaces."></spring:message>",
		 "code":"<spring:message code="VALID_CODE" text="Please enter the Code "/>",
		 "codePatt":"<spring:message code="ERROR_MESSAGE_CODE_PATT" text="Code should contain only numbers"></spring:message>",
		 "codeMinLen":"<spring:message code="ERROR_MESSAGE_CODE_MINLEN" text="Code should be in 6 digits"></spring:message>",
		 "codeMaxLen":"<spring:message code="ERROR_MESSAGE_CODE_MAXLEN" text="Code should not more than be 6 digits"></spring:message>",
		 "codeAllZeros":"<spring:message code="ERROR_MESSAGE_CODE_ALL_ZEROS" text="Code should not contain all zeros"></spring:message>",
		 "contactPerson":"<spring:message code="VALID_CONTACT_PERSON" text="Please enter the Contact Person "/>",
		 "contactNumber":"<spring:message code="VALID_CONTACT_NUMBER" text="Please enter the Contact Number "/>",
		 "orgContactNumber":"<spring:message code="VALID_ORG_CONTACT_NUMBER" text="Please enter the Orgnaisation Number "/>",
		 "numberAllZeros":"<spring:message code="ERROR_MESSAGE_CONTACT_NUMBER_ALL_ZEROS" text="Contact Number should not contain all zeros"></spring:message>",
		 "orgnumberAllZeros":"<spring:message code="ERROR_MESSAGE_ORG_CONTACT_NUMBER_ALL_ZEROS" text="Orgnisation Number should not contain all zeros"></spring:message>",
		 "numberPatt":"<spring:message code="ERROR_MESSAGE_CONTACT_NUMBER_PATT" text="Contact Number should contain only numbers"></spring:message>",
		 "contactNumberLength":"<spring:message code="VALID_CONTACT_NUMBER_LENGTH" text="Please Enter the valid Contact Number."/>",
		 "emailId":"<spring:message code="VALID_EMAILID" text="Please enter the EmailID "/>",
		 "orgemailId":"<spring:message code="VALID_ORG_EMAILID" text="Please enter the EmailID "/>",
		 "orgvalidEmailID":"<spring:message code="ERROR_MESSAGE_ORG_VALID_EMAIL_ID" text="Please enter valid Email ID."></spring:message>",
		 "orgemailIdMinLen":"<spring:message code="ERROR_MESSAGE_ORG_EMAIL_MINLEN" text="Email ID should be of minimum 11 characters"></spring:message>",
		 "validEmailID":"<spring:message code="ERROR_MESSAGE_VALID_EMAIL_ID" text="Please enter valid Email ID."></spring:message>",
		 "emailIdMinLen":"<spring:message code="ERROR_MESSAGE_EMAIL_MINLEN" text="Email ID should be of minimum 11 characters"></spring:message>",
		 //"commissionPercent":"<spring:message code="VALID_COMMISSION_PERCENT" text="Please enter the commission Percent "/>",
		 "commissionPercentLen":"<spring:message code="VALID_COMMISSION_PERCENT_LENGTH" text="Commission Percent can contains only 5 Numbers. "/>",
		 "validCommission":"<spring:message code="ERROR_VALID_COMMISSION" text="Commision Percent should contain only Number."/>",
		 //"validCommissionPerc":"<spring:message code="ERROR_VALID_COMMISSION" text="Commision Percent should contain only Number."/>",
		 "partnerType":"<spring:message code="VALID_PARTNER_TYPE" text="Please select the Partner Type."/>",
		 "address":"<spring:message code="VALID_BUSINESS_PARTNER" text="Please enter the Adress."/>",
		 "orgaddress":"<spring:message code="VALID_ORG_ADD_BUSINESS_PARTNER" text="Please enter the Orgnization Adress."/>",
		 "addressLength":"<spring:message code="VALID_ORG_ADDRESS_LENGTH" text="Address should contains minimum 4 Digits."/>",
		 "orgaddressLength":"<spring:message code="VALID_ORG_ADDRESS_LENGTH" text="Address should contains minimum 4 Digits."/>",
		 "validIssueDays":"<spring:message code="VALID_ISSUE_DAY" text="Please  enter  a valid  days in  Issue Date "/>",
		 "addressAlphaNum":"<spring:message code="ERROR_MESSAGE_ALPHANUMERIC" text="Address should contains AlphaNumeric and Special Characters $_."/>",	 			 
		 "businessEntityLimit":"<spring:message code="VALID_BUSINESS_ENTITY" text="Please enter the Business Entity."/>",
		 "kycid":"<spring:message code="VALID_KYC_ID" text="Please enter KYC-ID Number."/>",
		 "kyctype":"<spring:message code="VALID_KYC_TYPE" text="Please select the Kyc Type."/>",
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
			 var address=document.getElementById("address").value.trim();
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
             /* <!--  Author name <vinod joshi>, Date<7/5/2018>, purpose of change < bcz of this address not validating properly > ,
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
		    
          /*    if(address.charAt(0) == " " || address.charAt(address.length-1) == " "){
				 alert('<spring:message code="LABEL.ADDRESS.BLANK" text="Please remove the white space from address"/>'); 
				 return false; 	 
		     }else if(address.length > 180){
			     alert('<spring:message code="LABEL.ADDRESS.LENGTH" text="Address should not exceed more than 180 characters"/>');        
			     return false;  	 
			} 
		      */
		  
		    	 document.customerRegistrationForm.action = 'saveCustomer.htm';
				 document.customerRegistrationForm.submit();
			 
			 }
		
		/*Hide 
		function textCounter(field,cntfield,maxlimit) {
			 if (field.value.length > maxlimit) // if too long...trim it!
			 field.value = field.value.substring(0, maxlimit);
			 // otherwise, update 'characters left' counter
			 else
			 cntfield.value = maxlimit - field.value.length;
		 } */
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

function registerbusinesspartner() {
	if(validate()){
		submitlink("saveBusinessPartner.htm","BusinessPartnerRegistrationForm");
	}
	
}
/* <!--  @Author name <vinod joshi>, Write all java Script Conditions ,
	  	 @Start--> */
function validate(){
	var mobileLength = mobileNumberlen;	
	var patternName ="^([a-zA-Z]{1,20}[']{0,}[a-zA-Z]{1,20})*$";
	var strAlphabet = '^\[a-zA-Z ]{0,30}$';
	var CodePattern = '^\[0-9]{0,4}$';
	var CodeNoALLZEROS = '^\[0]{0,4}$';
	var strEmailPattern = '^\([a-zA-Z0-9_.-])+@(([a-zA-Z0-9-])+.)+([a-zA-Z0-9]{2,4})+$';
	var alphaNumericAddress = '/^([ A-Za-z0-9./#&,-]{0,100})*$/';
	var address = '^\[,.#-/&]{2,}$';
	var alphaNumericCode='/^[a-z0-9]+$/i';
	var numericData1 = '^\[0-9]{0,100}$';
	var emailIdPattern = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
	var rgx = /^[0-9]*\.?[0-9]*$/;
	var alphanum1 = /^[\w\s]*[-._.#.\/]?[\w\s\-\/.# , \d]*$/;
	var test4 = /^.*[a-zA-Z].*$/;
	var spclChar = /^[ A-Za-z0-9_@./#&+-]*$/;
	var numbers =  /^[0-9]+$/;
	var alphanum = "^[a-zA-Z0-9 ]+$";
	//var commissionPercent = document.BusinessPartnerRegistrationForm.commissionPercent.value;
	var contactNumber = document.BusinessPartnerRegistrationForm.contactNumber.value;
	var name = document.BusinessPartnerRegistrationForm.name.value;
	var organizationNumber = document.BusinessPartnerRegistrationForm.organizationNumber.value;
	var businessEntityLimit = document.BusinessPartnerRegistrationForm.businessEntityLimit.value;
	var kycImage=document.getElementById("kycImage").value ;
	var orgnizationaddress = document.BusinessPartnerRegistrationForm.organizationAddress.value.trim();
	var kycID = document.BusinessPartnerRegistrationForm.kycIdNumber.value;
	var designation = document.BusinessPartnerRegistrationForm.designation.value;
	var imge=document.getElementById("kycImage").files[0]; 
		
	
	if($.trim(document.BusinessPartnerRegistrationForm.name.value) == ""){
		alert(Alertmsg.name);
		document.BusinessPartnerRegistrationForm.name.focus();
		return false;
	}
	else if(document.BusinessPartnerRegistrationForm.name.value != "" && document.BusinessPartnerRegistrationForm.name.value.length < 2){
		alert(Alertmsg.nameLength);
		document.BusinessPartnerRegistrationForm.name.focus();
		return false;
	}
	else if(document.BusinessPartnerRegistrationForm.name.value.indexOf("  ") != -1){
		alert("Organization Name "+Alertmsg.nameWhiteSpac);
		document.BusinessPartnerRegistrationForm.name.focus();
		return false;
	}

	else if($.trim(document.BusinessPartnerRegistrationForm.name.value) != "" && name.match(numbers) ){
		alert('Organization Name should not contains only numbers');
		document.BusinessPartnerRegistrationForm.name.focus();
		return false;
	}
	
	else if (!name.match(alphanum)){
	alert('Organization name should not contains special characters');
    return false;
	}  
	
	else if($.trim(document.BusinessPartnerRegistrationForm.organizationNumber.value) != "" && !organizationNumber.match(numbers) ){
		alert('Organization Number should contains only numbers');
		document.BusinessPartnerRegistrationForm.organizationNumber.focus();
		return false;
	}
	
	else if($.trim(document.BusinessPartnerRegistrationForm.organizationNumber.value) != "" && $.trim(document.BusinessPartnerRegistrationForm.organizationNumber.value).search(CodeNoALLZEROS) != -1){
		alert(Alertmsg.orgnumberAllZeros);
		document.BusinessPartnerRegistrationForm.organizationNumber.focus();
		return false;
	}
	
	else if($.trim(document.BusinessPartnerRegistrationForm.organizationAddress.value)==""){
		alert(Alertmsg.orgaddress);
		document.BusinessPartnerRegistrationForm.organizationAddress.focus();
		return false;
	}

	/*  else if (!orgnizationaddress.match(alphanum1)){
			alert('Address should not contains special characters otherthan . , - # /');
			document.BusinessPartnerRegistrationForm.organizationAddress.focus();
		    return false;
			}
	  else if (!orgnizationaddress.match(test4)){
			alert('Address should contain only Alphabets');
			document.BusinessPartnerRegistrationForm.organizationAddress.focus();
		    return false;
			}  
	else if($.trim(document.BusinessPartnerRegistrationForm.organizationAddress.value) != "" && orgnizationaddress.match(numbers) ){
		alert('Address should not contains only numbers');
		document.BusinessPartnerRegistrationForm.organizationAddress.focus();
		return false;
	} 
 
    else if($.trim(document.BusinessPartnerRegistrationForm.organizationAddress.value).length < 4){
		alert(Alertmsg.orgaddressLength);
		document.BusinessPartnerRegistrationForm.organizationAddress.focus();
		return false;
	}
    else if(document.BusinessPartnerRegistrationForm.organizationAddress.value.indexOf("  ") != -1){
		alert("Address "+Alertmsg.nameWhiteSpac);
		document.BusinessPartnerRegistrationForm.organizationAddress.focus();
		return false;
	}
    else if(orgnizationaddress.indexOf("//") != -1 || 
    		orgnizationaddress.indexOf(",,") != -1 ||
    		orgnizationaddress.indexOf("..") != -1 ||
    		orgnizationaddress.indexOf("##") != -1 ||
    		orgnizationaddress.indexOf("--") != -1){
		alert("Address should not contain consecutive / or , or . or - or #");
		document.BusinessPartnerRegistrationForm.organizationAddress.focus();
		return false;
	} */
    else if($.trim(document.BusinessPartnerRegistrationForm.kycTypeId.value) == ""){
		alert(Alertmsg.kyctype);
		document.BusinessPartnerRegistrationForm.kycTypeId.focus();
		return false;
	}
    else if($.trim(document.BusinessPartnerRegistrationForm.kycIdNumber.value) == ""){
		alert(Alertmsg.kycid);
		document.BusinessPartnerRegistrationForm.kycIdNumber.focus();
		return false;
	}
	
	else if(document.BusinessPartnerRegistrationForm.kycIdNumber.value.indexOf("  ") != -1){
		alert("Document Number "+Alertmsg.nameWhiteSpac);
		document.BusinessPartnerRegistrationForm.kycIdNumber.focus();
		return false;
	}

	else if (!kycID.match(alphanum)){
	//	alert('KYC-ID Number should not contains only Numbers and special characters');
		alert('Document Number should contains only Numbers and Alphabet characters');
        return false;
    } 
	
	 else if(kycImage!="" && imge.size > 3145728) { 
	    alert('Image File Size Should less then 3MB');
	    return false;
	}
	
	
	else if($.trim(document.BusinessPartnerRegistrationForm.organizationEmailId.value) == ""){
		alert(Alertmsg.orgemailId);
		document.BusinessPartnerRegistrationForm.organizationEmailId.focus();
		return false;
	}
	else if($.trim(document.BusinessPartnerRegistrationForm.organizationEmailId.value) != "" && $.trim(document.BusinessPartnerRegistrationForm.organizationEmailId.value).length < 11 ){
		alert(Alertmsg.orgemailIdMinLen);
		document.BusinessPartnerRegistrationForm.organizationEmailId.focus();
		return false;
	}
		
	else if(emailIdPattern.test(organizationEmailId.value) == false){
			alert(Alertmsg.orgvalidEmailID);
            return false;
        }
	
	
	/* else if($.trim(document.BusinessPartnerRegistrationForm.code.value) == ""){
		alert(Alertmsg.code);
		document.BusinessPartnerRegistrationForm.code.focus();
		return false;
	}
	else if($.trim(document.BusinessPartnerRegistrationForm.code.value).length < 6){
		alert(Alertmsg.codeMinLen);
		document.BusinessPartnerRegistrationForm.code.focus();
		return false;
	}
	else if($.trim(document.BusinessPartnerRegistrationForm.code.value).length > 6){
		alert(Alertmsg.codeMaxLen);
		document.BusinessPartnerRegistrationForm.code.focus();
		return false;
	}
	else if($.trim(document.BusinessPartnerRegistrationForm.code.value).search(CodeNoALLZEROS) != -1){
		alert(Alertmsg.codeAllZeros);
		document.BusinessPartnerRegistrationForm.code.focus();
		return false;
	}
	/* else if($.trim(document.BusinessPartnerRegistrationForm.code.value).search(alphaNumericCode) == -1){
		alert(Alertmsg.codePatt);
		document.BusinessPartnerRegistrationForm.code.focus();
		return false;
	} */ 
	else if($.trim(document.BusinessPartnerRegistrationForm.contactPerson.value) == ""){
		alert(Alertmsg.contactPerson);
		document.BusinessPartnerRegistrationForm.contactPerson.focus();
		return false;
	}
	else if(document.BusinessPartnerRegistrationForm.contactPerson.value.indexOf("  ") != -1){
		alert(Alertmsg.contactnameWhiteSpac);
		document.BusinessPartnerRegistrationForm.contactPerson.focus();
		return false;
	}
	else if(document.BusinessPartnerRegistrationForm.contactPerson.value != "" && document.BusinessPartnerRegistrationForm.contactPerson.value.length < 2){
		alert(Alertmsg.contactnameLength);
		document.BusinessPartnerRegistrationForm.contactPerson.focus();
		return false;
	}
	else if($.trim(document.BusinessPartnerRegistrationForm.contactPerson.value).search(strAlphabet) == -1){
		alert(Alertmsg.contactnamePatt);
		document.BusinessPartnerRegistrationForm.contactPerson.focus();
		return false;
	}
	
	else if(document.BusinessPartnerRegistrationForm.designation.value != "" && document.BusinessPartnerRegistrationForm.designation.value.length < 2){
		alert('Enter the Valid Designation values.');
		document.BusinessPartnerRegistrationForm.contactPerson.focus();
		return false;
	}
	
	 else if(document.BusinessPartnerRegistrationForm.designation.value != "" && designation.match(numbers)){
		alert('Designation should contains only Alphabets');
		document.BusinessPartnerRegistrationForm.contactPerson.focus();
		return false;
	} 
	
	else if($.trim(document.BusinessPartnerRegistrationForm.contactNumber.value) == ""){
		alert(Alertmsg.contactNumber);
		document.BusinessPartnerRegistrationForm.contactNumber.focus();
		return false;
	}
	else if($.trim(document.BusinessPartnerRegistrationForm.contactNumber.value) != "" && !contactNumber.match(numbers) ){
		alert('Contatct Number should contains only numbers');
		document.BusinessPartnerRegistrationForm.contactNumber.focus();
		return false;
	}

	else if($.trim(document.BusinessPartnerRegistrationForm.contactNumber.value).search(CodeNoALLZEROS) != -1){
		alert(Alertmsg.numberAllZeros);
		document.BusinessPartnerRegistrationForm.contactNumber.focus();
		return false;
	}
	
	
	/* if($.trim(document.BusinessPartnerRegistrationForm.contactNumber.value).search(CodePattern) == -1){
		alert(Alertmsg.numberPatt);
		document.BusinessPartnerRegistrationForm.contactNumber.focus();
		return false;
	} */
	
	else if(document.BusinessPartnerRegistrationForm.contactNumber.value != "" && document.BusinessPartnerRegistrationForm.contactNumber.value.length != mobileNumberlen){
		alert(Alertmsg.contactNumberLength+" of length "+mobileNumberlen);
		document.BusinessPartnerRegistrationForm.contactNumber.focus();
		return false;
	}
	/* else if(document.BusinessPartnerRegistrationForm.contactNumber.value != "" && document.BusinessPartnerRegistrationForm.contactNumber.value.length > mobileNumberlen){
		alert(Alertmsg.contactNumberLength+" of length "+mobileNumberlen);
		document.BusinessPartnerRegistrationForm.contactNumber.focus();
		return false;
	} */
	else if($.trim(document.BusinessPartnerRegistrationForm.emailId.value) == ""){
		alert(Alertmsg.emailId);
		document.BusinessPartnerRegistrationForm.emailId.focus();
		return false;
	}
	else if($.trim(document.BusinessPartnerRegistrationForm.emailId.value) != "" && $.trim(document.BusinessPartnerRegistrationForm.emailId.value).length < 11 ){
		alert(Alertmsg.emailIdMinLen);
		document.BusinessPartnerRegistrationForm.emailId.focus();
		return false;
	}
		
	/* else if(document.BusinessPartnerRegistrationForm.emailId.value != "" && !checkEmailID(document.BusinessPartnerRegistrationForm.emailId.value)){
			alert(Alertmsg.validEmailID);
			document.BusinessPartnerRegistrationForm.emailId.focus();
			return false;
		} */
	else if (emailIdPattern.test(emailId.value) == false){
			alert(Alertmsg.validEmailID);
            return false;
        }
	/* else if($.trim(document.BusinessPartnerRegistrationForm.commissionPercent.value) == ""){
		alert(Alertmsg.commissionPercent);
		document.BusinessPartnerRegistrationForm.commissionPercent.focus();
		return false;
	}
	else if($.trim(document.BusinessPartnerRegistrationForm.commissionPercent.value).length > 5){
		alert(Alertmsg.commissionPercentLen);
		document.BusinessPartnerRegistrationForm.commissionPercent.focus();
		return false;
	}
	
	else if(commissionPercent>100){
	alert('Commission Percent should be less than or equals To 100%');
	return false;
	}
	
	 else if (!commissionPercent.match(rgx)){
		alert(Alertmsg.validCommission);
        return false;
    }   
	else if($.trim(document.BusinessPartnerRegistrationForm.commissionPercent.value).search(CodePattern)==-1){
		alert(Alertmsg.validCommission);
		document.BusinessPartnerRegistrationForm.commissionPercent.focus();
		return false;
	}  */
	
	 else if($.trim(document.BusinessPartnerRegistrationForm.businessEntityLimit.value) == ""){
			alert(Alertmsg.businessEntityLimit);
			document.BusinessPartnerRegistrationForm.businessEntityLimit.focus();
			return false;
		}
		
		else if($.trim(document.BusinessPartnerRegistrationForm.businessEntityLimit.value) != "" && !businessEntityLimit.match(numbers) ){
			alert('business Entity should contains only numbers');
			document.BusinessPartnerRegistrationForm.businessEntityLimit.focus();
			return false;
		}
	
	/* else if($.trim(document.BusinessPartnerRegistrationForm.address.value)==""){
		alert(Alertmsg.address);
		document.BusinessPartnerRegistrationForm.address.focus();
		return false;
	}
	
	 else if (!document.BusinessPartnerRegistrationForm.address.value.match(alphanum1)){
			alert('Address should not contains special characters otherthan . , - # /');
			document.BusinessPartnerRegistrationForm.address.focus();
		    return false;
			}
	  else if (!document.BusinessPartnerRegistrationForm.address.value.match(test4)){
			alert('Address should contain Alphabets');
			document.BusinessPartnerRegistrationForm.address.focus();
		    return false;
			}  
	else if($.trim(document.BusinessPartnerRegistrationForm.address.value) != "" && document.BusinessPartnerRegistrationForm.address.value.match(numbers) ){
		alert('Address should not contains only numbers');
		document.BusinessPartnerRegistrationForm.address.focus();
		return false;
	} 

 	else if($.trim(document.BusinessPartnerRegistrationForm.address.value).length < 4){
		alert(Alertmsg.orgaddressLength);
		document.BusinessPartnerRegistrationForm.address.focus();
		return false;
	}
	 else if(document.BusinessPartnerRegistrationForm.address.value.indexOf("  ") != -1){
		alert("Address "+Alertmsg.nameWhiteSpac);
		document.BusinessPartnerRegistrationForm.address.focus();
		return false;
	}
	 else if(document.BusinessPartnerRegistrationForm.address.value.indexOf("//") != -1 || 
			 document.BusinessPartnerRegistrationForm.address.value.indexOf(",,") != -1 ||
			 document.BusinessPartnerRegistrationForm.address.value.indexOf("..") != -1 ||
			 document.BusinessPartnerRegistrationForm.address.value.indexOf("##") != -1 ||
			 document.BusinessPartnerRegistrationForm.address.value.indexOf("--") != -1){
		alert("Address should not contain consecutive / or , or . or - or #");
		document.BusinessPartnerRegistrationForm.address.focus();
		return false;
	} */

	/* else if($.trim(document.BusinessPartnerRegistrationForm.address.value.search(numericData1)!=-1)){
		alert(Alertmsg.addressAlphaNum);
		document.BusinessPartnerRegistrationForm.address.focus();
		return false;
	} */
	else if($.trim(document.BusinessPartnerRegistrationForm.partnerType.value) == ""){
		alert(Alertmsg.partnerType);
		document.BusinessPartnerRegistrationForm.partnerType.focus();
		return false;
	}
	
	
	
	
	
/* 	if($("#kycImage").val() == "")
	  {
	   alert("Attachment KYC Image is Required");
	   document.BusinessPartnerRegistrationForm.kycImage.focus();
	    return false;
	  }  */
	
	
	
	return true;
	
	
}

function cancelForm(){
	 clearForm();
	document.BusinessPartnerRegistrationForm.action="businessPartner.htm";
	document.BusinessPartnerRegistrationForm.submit();
}

function clearForm(){

	/* @Author name <vinod joshi>, Date<9/3/2018>, purpose of change <give the BusinessPartnerRegistray validation> */
//	document.BusinessPartnerRegistrationForm.reset();
	document.BusinessPartnerRegistrationForm.name.value="";
	document.BusinessPartnerRegistrationForm.organizationNumber.value="";
	document.BusinessPartnerRegistrationForm.organizationAddress.value="";
	document.BusinessPartnerRegistrationForm.organizationEmailId.value="";
//	document.BusinessPartnerRegistrationForm.partnerType.value="";
	/* document.BusinessPartnerRegistrationForm.code.value=""; */
	document.BusinessPartnerRegistrationForm.contactPerson.value="";
	document.BusinessPartnerRegistrationForm.contactNumber.value="";
	document.BusinessPartnerRegistrationForm.emailId.value="";
//	document.BusinessPartnerRegistrationForm.commissionPercent.value="";
	/* document.BusinessPartnerRegistrationForm.address.value=""; */
	document.BusinessPartnerRegistrationForm.kycTypeId.value="";
	document.BusinessPartnerRegistrationForm.kycIdNumber.value="";
	document.BusinessPartnerRegistrationForm.businessEntityLimit.value="";
//	document.BusinessPartnerRegistrationForm.kycImagePreview.value="";
	document.BusinessPartnerRegistrationForm.kycImage.value="";
	document.BusinessPartnerRegistrationForm.designation.value="";
	
	
	/*@End */
	
	
}
		
</script>
</head>
<body>
<div class="row">
<div class="col-lg-12">
<div class="box">
	<div class="box-header">
		<h3 class="box-title">			
			<c:if test='${roleId eq 2}'>
			<span><spring:message code="LABEL_PRINCIPAL_AGENT_SHOWFORM" text="Principal Agent"></spring:message></span>
			</c:if>
			<c:if test='${roleId ne 2}'>
			<span><spring:message code="LABEL_BUSINESSPARTNER_SHOWFORM" text="Super Agent"></spring:message></span>
			</c:if>
		</h3>
	</div>
	<div class="col-sm-5 col-sm-offset-4">
		<div class="col-sm-12" style="color: #ba0101; font-weight: bold; font-size: 12px; margin:auto;">
			<spring:message code="${message}" text="" />
		</div>	
	</div>
	<form:form method="POST" class="form-inline" commandName="BusinessPartnerDTO" name="BusinessPartnerRegistrationForm" id="BusinessPartnerRegistrationForm" enctype="multipart/form-data">
	<jsp:include page="csrf_token.jsp"/>
	<form:hidden path="id" /> 
	<form:hidden path="code" value="${BusinessPartnerDTO.code}"/> 
	<c:if test="${BusinessPartnerDTO.id eq null }">
		<div class="add_cus">					
			<a href="javascript:submitForm('BusinessPartnerRegistrationForm','businessPartner.htm')"><strong style="margin-left: 90px;">
			<c:if test='${roleId eq 2}'>
				<spring:message code="LINK_SEARCH_PRINCIPAL_AGENTS" text="Search Principal Agents"></spring:message>
			</c:if>
			<c:if test='${roleId ne 2}'>
				<spring:message code="LINK_SEARCH_BUSINESS_PARTNERS" text="Search Super Agents"></spring:message>
			</c:if>			
			</strong></a>
		</div>
	</c:if>		
	<c:if test="${BusinessPartnerDTO.id ne null }">
		<c:if test='${fromDate eq null || fromDate eq ""}'>
		<div class="add_cus">
			<a href="javascript:submitForm('BusinessPartnerRegistrationForm','businessPartner.htm')"><strong style="margin-left: 90px;">
			<c:if test='${roleId eq 2}'>
				<spring:message code="LINK_SEARCH_PRINCIPAL_AGENTS" text="Search Principal Agents"></spring:message>
			</c:if>
			<c:if test='${roleId ne 2}'>
				<spring:message code="LINK_SEARCH_BUSINESS_PARTNERS" text="Search Business Partners"></spring:message>
			</c:if>			
			</strong></a>
		</div>
	</c:if>	
		<c:if test='${fromDate ne null && fromDate ne ""}'>
		<div class="add_cus">	
			<a href="javascript:submitForm('BusinessPartnerRegistrationForm','businessPartner.htm')"><strong style="margin-left: 30px;">
			<spring:message code="LINK_SEARCH_BACK_BUSINESS_PARTNERS" text="Back to Search Business Partners"> </spring:message>
			</strong></a>  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
		</div>
		</c:if>
	</c:if> 
	<br /><br />
	 <div class="box-body">			
			<!-- @start vinod changes  -->
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_ORGANIZATION_NAME" text="Organization Name:"></spring:message><font color="red">*</font></label> 
					<form:input path="name" maxlength="30" cssClass="form-control" ></form:input>
					<font color="RED"><form:errors path="name"></form:errors></font>
				</div>
				
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_ORGANIZATION_NUMBER" text="Organization Number"></spring:message></label>
					<form:input path="organizationNumber" maxlength="12" cssClass="form-control" ></form:input> 
					<font color="RED"><form:errors path="organizationNumber"></form:errors></font>
				</div>	
				
			</div>
			
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_EMAILID" text="Email Id:" /><font color="red">*</font></label>
					<form:input path="organizationEmailId" maxlength="32" cssClass="form-control" ></form:input> 
					<font color="RED"><form:errors path="organizationEmailId"></form:errors></font>
				</div>
			
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_BUSINESS_ENTITY" text="Business Entity" /><font color="red">*</font></label>
					<form:input path="businessEntityLimit" maxlength="12" cssClass="form-control" ></form:input> 
					<font color="RED"><form:errors path="businessEntityLimit"></form:errors></font>
				</div>
			</div>
			
				<div class="row">
				<%-- <div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_COMMISSION" text="Commission(%)" /><font color="red">*</font></label>
					<form:input path="commissionPercent" maxlength="5" cssClass="form-control" ></form:input> 
					<font color="RED"><form:errors path="commissionPercent"></form:errors></font>
				</div> --%>
				
				
				<authz:authorize ifAnyGranted="ROLE_admin" >
				 <div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_PARTNER_TYPE" text="Partner Type" /></label> 					
					 <%--<form:select path="partnerType" id="partnerType" cssClass="dropdown chosen-select">
						<c:if test="${BusinessPartnerDTO eq null }">
						<form:option value=""><spring:message code="LABEL_SELECT" text="---Please Select---"/></form:option>
						<form:option value="1"><spring:message code="LABEL_BA_ADMIN" text="BA Admin"/></form:option>
						</c:if>
						<c:if test="${BusinessPartnerDTO ne null }">
						<form:option value="1"><spring:message code="LABEL_BA_ADMIN" text="BA Admin"/></form:option>
						</c:if>
						</form:select> <FONT color="red"><form:errors path="partnerType" /></FONT> 
						
						<!-- @vinod Uncomment when we want HardCoded BusinessPartner -->
						 <spring:message code="LABEL_BA_ADMIN" text="BA Admin"></spring:message>					
										
					</div> --%>
					
					<c:if test="${BusinessPartnerDTO.id eq null }">
					<label class="col-sm-5"><spring:message code="LABEL_BA_ADMIN" text="BA Admin"/></label>
					<form:hidden path="partnerType" value="1"/>
					</c:if>
					<c:if test="${BusinessPartnerDTO.id ne null }">
					<label class="col-sm-5"><spring:message code="LABEL_BA_ADMIN" text="BA Admin"/></label>
					<form:hidden path="partnerType" value="1"/>
					</c:if></div>
					
			</authz:authorize>
					
			<authz:authorize ifAnyGranted="ROLE_mGurush,ROLE_bankadmin,ROLE_bankteller">
					<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_PARTNER_TYPE" text="Partner Type" /><font color="red">*</font></label> 
					 <%-- <form:select path="partnerType" id="partnerType" cssClass="dropdown">
					<c:if test="${BusinessPartnerDTO.id eq null }">
						<form:option value=""><spring:message code="LABEL_SELECT" text="---Please Select---"/></form:option>
						<form:option value="2"><spring:message code="LABEL_BUSINESS_PARTNER_L1" text="Business Partner L1"/></form:option>
					</c:if>
					<c:if test="${BusinessPartnerDTO.id ne null }">
					<form:option value="2"><spring:message code="LABEL_BUSINESS_PARTNER_L1" text="Business Partner L1"/></form:option>
					</c:if>	
						</form:select> <FONT color="red"><form:errors path="partnerType" /></FONT> --%>
						
						<c:if test="${BusinessPartnerDTO.id eq null }">
					<label class="col-sm-5"><spring:message code="LABEL_BUSINESS_PARTNER_L1" text="Business Partner L1"/></label>
					<form:hidden path="partnerType" value="2"/>
					</c:if>
					<c:if test="${BusinessPartnerDTO.id ne null }">
					<label class="col-sm-5"><spring:message code="LABEL_BUSINESS_PARTNER_L1" text="Business Partner L1"/></label>
					<form:hidden path="partnerType" value="2"/>
					</c:if>
						
					</div> 
					</authz:authorize>

							<authz:authorize ifAnyGranted="ROLE_businesspartnerL1">
								<div class="col-sm-6">
									<label class="col-sm-5"><spring:message
											code="LABEL_PARTNER_TYPE" text="Partner Type" /></label>
									<c:if test="${BusinessPartnerDTO.id eq null }">
										<label class="col-sm-5"><spring:message
												code="LABEL_BUSINESS_PARTNER_L2" text="Business Partner L2" /></label>
										<form:hidden path="partnerType" value="3" />
									</c:if>
									<c:if test="${BusinessPartnerDTO.id ne null }">
										<label class="col-sm-5"><spring:message
												code="LABEL_BUSINESS_PARTNER_L2" text="Business Partner L2" /></label>
										<form:hidden path="partnerType" value="3" />
									</c:if>
								</div>

							</authz:authorize>

				<authz:authorize ifAnyGranted="ROLE_businesspartnerL2" >
					 <div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_PARTNER_TYPE" text="Partner Type" /></label> 
					<c:if test="${BusinessPartnerDTO.id eq null }">
					<label class="col-sm-5"><spring:message code="LABEL_BUSINESS_PARTNER_L3" text="Business Partner L3"/></label>
					<form:hidden path="partnerType" value="4"/>
					</c:if>
					<c:if test="${BusinessPartnerDTO.id ne null }">
					<label class="col-sm-5"><spring:message code="LABEL_BUSINESS_PARTNER_L3" text="Business Partner L3"/></label>
					<form:hidden path="partnerType" value="4"/>
					</c:if>	
			
			</div>
			</authz:authorize>
			</div>
			
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_ADDRESS" text="Address:" /><font color="red">*</font></label> 
					<form:textarea path="organizationAddress" cssClass="form-control" style="width: 180px; height: 100px;"></form:textarea> 
					<font color="RED"><form:errors path="organizationAddress"></form:errors></font>
				</div>
				
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_KYC_TYPE" text="Kyc Type" /><font color="red">*</font></label> 					
					 <form:select path="kycTypeId" id="kycTypeId" cssClass="dropdown">
						
						<form:option value=""><spring:message code="LABEL_SELECT" text="---Please Select---"/></form:option>
						  <c:if test="${BusinessPartnerDTO ne null }">
						<c:forEach items="${KycList}" var="ct">
										<form:option value="${ct.kycTypeId}">
											<c:out value="${ct.kycDescription}" />
										</form:option>
									</c:forEach>
					</c:if>	
						  <%-- <c:forEach items="${KycList}" var="kycType">
						  <option value="<c:out value="${kycType.kycDescription}"/>" > </option>
						 </c:forEach>  --%>
				   <c:if test="${BusinessPartnerDTO eq null }"> 
									<c:forEach items="${KycList}" var="ct">
										<form:option value="${ct.kycTypeId}">
											<c:out value="${ct.kycDescription}" />
										</form:option>
									</c:forEach>
				 </c:if>	 
								</form:select> <FONT color="red"><form:errors path="kycTypeId" /></FONT> 
						
							
				</div>
				
			</div>	
			
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_KYCID_NUMBER" text="KYC-ID Number" /><font color="red">*</font></label>
					<form:input path="kycIdNumber" maxlength="9" cssClass="form-control" ></form:input> 
					<font color="RED"><form:errors path="kycIdNumber"></form:errors></font>
				</div> 
				
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_KYC_IMAGE" text="KYC Image"/><!-- <font color="red">*</font> --></label> 
					<form:input path="kycImage" type="file" id="kycImage" accept=".jpeg,.jpg,.png"/>
					<span style="font-size: 11px;color: red;">(3MB max size allowed)</span>
				</div>
				</div>
				
				<div class="row">
				
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_CONTACT_PERSON" text="Contact Person"><font color="red">*</font></spring:message></label> 
					<form:input path="contactPerson" maxlength="30" cssClass="form-control" ></form:input>
				</div>
				
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_DESIGNATION_NAME" text="Position" /></label>
					<form:input path="designation" id="designation" maxlength="32" cssClass="form-control" ></form:input> 
					<font color="RED"><form:errors path="designation"></form:errors></font>
				</div>
				
			</div>
				
				
		<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_CONTACT_NUMBER" text="Contact Number"></spring:message><font color="red">*</font></label>
					<form:input path="contactNumber" maxlength="${mobileNumLength}" cssClass="form-control" ></form:input> 
				</div>
			
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_EMAILID" text="Email Id:" /><font color="red">*</font></label>
					<form:input path="emailId" maxlength="32" cssClass="form-control" ></form:input> 
					<font color="RED"><form:errors path="emailId"></form:errors></font>
				</div>
		</div>
				
				<div class="row">
				<c:if test="${BusinessPartnerDTO.id ne null }"> 
				<div class="col-sm-6">
				<label class="col-sm-4">Previous KYC Image:</label> 
						<a onclick="return openNewWindow('<%=request.getContextPath()%>/getBPPhoto.htm?type=kycImage&id=<c:out value="${BusinessPartnerDTO.id}" />')">
							<img id="kycImagePreview"
							src="<%=request.getContextPath()%>/getBPPhoto.htm?type=kycImage&id=<c:out value="${BusinessPartnerDTO.id}" />" onerror="this.src='<%=request.getContextPath()%>/images/default_photo.png';" alt="<spring:message code="LABEL_KYC_IMG_NOT_FOUND" text="Kyc Image not found." />" width="50" height="50" />
						</a>
					</div> 
					<form:hidden path="kycImage" id="kycImage"/>
					</c:if>
				</div>
				</div>
				
			
			<div class="box-footer" >

				<input type="button"  value="<spring:message code="LABEL_CANCEL" text="cancel"/>" onclick="cancelForm();"class="btn btn-primary pull-right" style="margin-right:10px;"></input>
			 	<input type="button"  value="<spring:message code="LABEL_RESET" text="reset"/>" onclick="clearForm();" class="btn btn-primary pull-right" style="margin-right:10px;"></input>
			
			 <c:if test="${BusinessPartnerDTO.id eq null }">
					<input type="button"   value="<spring:message code="LABEL_ADD" text="Add"/>" onclick="registerbusinesspartner();" class="btn btn-primary pull-right" style="margin-right:10px;"></input>
			</c:if>
			
			<c:if test="${BusinessPartnerDTO.id ne null }">
			 		<input type="button"   value="<spring:message code="LABEL_UPDATE" text="Add"/>" onclick="registerbusinesspartner();" class="btn btn-primary pull-right" style="margin-right:10px;"></input>
			</c:if> 
			
			
			<br /> <br />
			<br /> <br />
			</div>	</div>
				
			<input type="hidden" name="nameV" id="nameV" value="${name}"/>
			<input type="hidden" name="contactNumberV" id="contactNumberV" value="${contactNumber}"/>
			<input type="hidden" name="fromDateV" id="fromDateV" value="${fromDate}"/>
			<input type="hidden" name="toDateV" id="toDateV" value="${toDate}"/>
			</form:form>
			</div>
</div>
</div>

			 <script type="text/javascript">
						var mobileNumberlen='${mobileNumLength}';
					//	document.getElementById('mobileNum').innerHTML=mobileNumberlen;
						window.onload=function(){
						check();
						issueDate();
						expiryDate();
						};
			</script> 
			<script>
	$(document).ready(function() {
		$('#kycImage').change(function() {
			readURL(this, '#kycImagePreview');
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
</body>
</html>
