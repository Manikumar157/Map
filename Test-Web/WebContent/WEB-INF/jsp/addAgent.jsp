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
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/agent.js"></script>
<script type="text/javascript"  src="<%=request.getContextPath()%>/js/customer.js"></script>
<script>
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
/* 	$(document).ready(function() {
		$("input").datepicker({
		    changeMonth: true, 
		    changeYear: true, 
		    dateFormat: 'yy-mm-dd',
		    minDate: 0,
		    onSelect: function(dateText) {
		        $sD = new Date(dateText);		     
		    }
		});
		});
	 */
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
		"placeOfBirthSpace":"<spring:message code="VALIDATION_PLACE_OF_BIRTH_SPACE" text="Please remove unwanted spaces in Place Of Birth"></spring:message>",
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
		"commissionEmpty": "Please enter Commission",
		"commissionPatt": "Please enter valid Commission values",
		"agentCode":"Please enter Merchant Code",
		"nameWhiteSpac":"<spring:message code="ERROR_MESSAGE_NAME_TWO_WHITE_SPACES" text="should not contain two consecutive white spaces."></spring:message>"		 
		};
		function fileValidate(action){
		  var dob=document.AgentRegistrationForm.dob.value;
			 var filetypeSignature = document.AgentRegistrationForm.signature.value.substring(document.AgentRegistrationForm.signature.value.lastIndexOf("."));
			 var filetypeIdProof = document.AgentRegistrationForm.idProof.value.substring(document.AgentRegistrationForm.idProof.value.lastIndexOf("."));
			 var firstName=document.getElementById("firstName").value;
			 var placeOfBirth=document.getElementById("placeOfBirth").value;
			 var lastName=document.getElementById("lastName").value;
			 var mobileNumber=document.getElementById("mobileNumber").value;
			 var pattern='^\[a-zA-ZÀ-ÿ\'-. ]{1,30}$';
			 var IdNumberPattern='^\[0-9A-Za-z ]*$';
			 var agentCodePattern='^\[1-9][0-9]*$';
			 var firstNamePattern='^\[a-zA-ZÀ-ÿ]*$';
			 var placeOfBirthPattern='^\[a-zA-ZÀ-ÿ ,()]*$';
			 var addressAlphaNumeric ='/^[A-Za-z0-9 ,\/#&-.]{5,100}$/';
			 var mobilepattern=''; 
			 var professionPattern = '^\[a-zA-ZÀ-ÿ\' ]{1,30}$';
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
			 //var bankCustomerId = document.getElementById("bankCustomerId").value ; 
		//	 var answer = document.getElementById("answer").value;
			 var emailAddress = document.getElementById("emailAddress").value;
			 var pothers = document.getElementById("other").value;
		//	 var commission = document.getElementById("commission").value;
		//	 var doublePatt = '^-?\\d*\\.\\d+$';
		//	 var doublePatt = '/(\d+(\.\d+)?)/';
			 var doublePatt = '^(?=.)([+-]?([0-9]*)(\.([0-9]+))?)$';
			 var idType = document.getElementById("idType").value;
			 var profilePhoto=document.getElementById("profilePhoto").value ;
			 var addressProof=document.getElementById("addressProof").value ;
			 var filetypeprofilePhoto = document.AgentRegistrationForm.signature.value.substring(document.AgentRegistrationForm.profilePhoto.value.lastIndexOf("."));
			 var filetypeaddressProof = document.AgentRegistrationForm.idProof.value.substring(document.AgentRegistrationForm.addressProof.value.lastIndexOf("."));
			 var signatureimge=document.getElementById("signature").files[0]; 
			 var idProofimge=document.getElementById("idProof").files[0];
			 var profilePhotoimge=document.getElementById("profilePhoto").files[0];
			 var addressProofimge=document.getElementById("addressProof").files[0];
			 var type = document.getElementById("custType").value;
			 
			 if(document.getElementById('countryId').value!=''){
				 mobilepattern='^\[0-9]{'+mobileNumLen+'}$'; 
			 }
			
			 if(type==2) {
				 var agentCode = document.getElementById("agentCode").value;
				 if(agentCode==""){
					 alert(Alertmsg.agentCode);
			         document.AgentRegistrationForm.agentCode.focus();
			         return false;
				 }
				 else if(agentCode.length != 6){
					 alert('Please enter valid Merchant Code of length 6 characters');
			         document.AgentRegistrationForm.agentCode.focus();
			         return false;
				 }
				 else if(agentCode.search(agentCodePattern)==-1){
	                 alert('Please enter valid Merchant Code in numeric digits');
	                 document.AgentRegistrationForm.agentCode.focus();
	                 return false;
		    	 }
		        
			 }
			 if(document.getElementById("title").value=="") {
		         alert(Alertmsg.title);
		         document.AgentRegistrationForm.title.focus();
		         return false;
			 }else if(document.getElementById("firstName").value=="") {
		         alert(Alertmsg.firstName);
		         document.AgentRegistrationForm.firstName.focus();
		         return false;
			 }else if(firstName.charAt(0) == " " || firstName.charAt(firstName.length-1) == " "){
                 alert(Alertmsg.firstNameSpace);
                 document.AgentRegistrationForm.firstName.focus();
                 return false;
             }else if(firstName.search(firstNamePattern)==-1){
         		alert( Alertmsg.firstnamevalid) ;
        		return false;
        	}else if(document.getElementById("lastName").value=="") {
            	  alert('<spring:message code="VALIDATION_LAST_NAME" text="Please enter Last name"/>');
	              document.AgentRegistrationForm.lastName.focus();
	              return false;
			 }else if(lastName.search(firstNamePattern)==-1){
	         		alert( Alertmsg.lastName);
	        		return false;
	        	}else if(document.getElementById("mobileNumber").value=="") {
				 /* alert(document.getElementById("mobileNumber").value); */
		         alert(Alertmsg.mobileNumber);
		         document.AgentRegistrationForm.mobileNumber.focus();
		         return false;
			 }else if(mobileNumber.charAt(0) == " " || mobileNumber.charAt(mobileNumber.length-1) == " "){
                 alert(Alertmsg.mobileNumberSpace);
                 document.AgentRegistrationForm.mobileNumber.focus();
                 return false;
             }else if(document.getElementById('countryId').value!='' && mobileNumber.search(mobilepattern)==-1){
                 alert(Alertmsg.mobileNumberCharacter+" of length "+mobileNumLen);
                 document.AgentRegistrationForm.mobileNumber.focus();
                 return false;
             }else if(!checkAllZero(document.getElementById("mobileNumber").value)) {
		         alert(Alertmsg.mobileNumberCharacter);
		         document.AgentRegistrationForm.mobileNumber.focus();
		         return false;
			 }else if(document.getElementById("dob").value=="") {
		         alert(Alertmsg.dob);
		         document.AgentRegistrationForm.dob.focus();
		         return false;
			 }else if(!checkDateOfBirth(dob)){
			     	return false;  
			   }else if(!compareDod(dob)){
	             alert(Alertmsg.dobValid);
                 return false;
                }else if(placeOfBirth.charAt(0) == " " || placeOfBirth.charAt(placeOfBirth.length-1) == " "){
                    alert(Alertmsg.placeOfBirthSpace);
                    document.AgentRegistrationForm.placeOfBirth.focus();
                    return false;
                }else if(document.AgentRegistrationForm.placeOfBirth.value.indexOf("  ") != -1){
            		alert("Place Of Birth "+Alertmsg.nameWhiteSpac);
            		document.AgentRegistrationForm.placeOfBirth.focus();
            		return false;
            	}
			   else if(placeOfBirth.search(placeOfBirthPattern)==-1){
                	alert("Please enter valid place of birth");
                	 document.AgentRegistrationForm.placeOfBirth.focus();
                	 return false;
                }else if(document.getElementById("language").value=="") {
		         alert(Alertmsg.language);
		         document.AgentRegistrationForm.language.focus();
		         return false;
			 }/* else if(document.getElementById("type").value=="") {
		         alert(Alertmsg.type);
		         document.AgentRegistrationForm.type.focus();
		         return false;
			 } */else if(document.getElementById("customerProfileId").value=="") {
		         alert(Alertmsg.customerProfileId);
		         document.AgentRegistrationForm.customerProfileId.focus();
		         return false;
			 }/* else if(document.getElementById("questionId").value=="" || document.getElementById("questionId").value==" ") {
		         alert(Alertmsg.questionId);
		         document.AgentRegistrationForm.questionId.focus();
		         return false;
			 }else if(document.getElementById("questionId").value=="select") {
				 alert(Alertmsg.questionId);
		         return false;
			 }else if(answer=="") {
		         alert(Alertmsg.answer);
		         document.AgentRegistrationForm.answer.focus();
		         return false;
			 }else if(answer.charAt(0) ==" " || answer.charAt(idNumber.length-1) == " "){
                 alert('<spring:message code="ANSWER_SPACE" text="Please remove white space in answer"/>');
		         document.AgentRegistrationForm.answer.focus();
                 return false;
				 
			 } */else if (checkEmail(emailAddress) == false) {
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
		         document.AgentRegistrationForm.address.focus();
		         return false;
			 }
             /* <!--  Author name <vinod joshi>, Date<7/5/2018>, purpose of change < bcz of this address not validating properly> ,
 			//Start--> */
             /* else if(address.search(firstNamePattern)==-1){
	         		alert("Please enter valid address");
	        		return false;
	        	} */else if(document.getElementById("quarterId").value=="" || document.getElementById("quarterId").value==" ") {
		         alert(Alertmsg.quarterId);
		         //document.AgentRegistrationForm.quarterId.focus();
		         return false;
		     }else if(document.getElementById("quarterId").value=="select") {
		         alert(Alertmsg.quarterId);
		         //document.AgentRegistrationForm.quarterId.focus();
		         return false;
		     }/* else if(bankCustomerId !=""){
		    	 if(bankCustomerId.search(IdNumberPattern)==-1){
	                 alert('<spring:message code="VALIDATION_BANK_ID_NUMBER" text="Please enter valid Bank Customer ID"/>');
	                 document.AgentRegistrationForm.idNumber.focus();
	                 return false;
		    	 }
		    	 if(bankCustomerId.charAt(0) ==" " || bankCustomerId.charAt(bankCustomerId.length-1) == " "){
	                 alert('<spring:message code="VALIDATION_BANK_ID_NUMBER" text="Please enter valid Bank Customer ID"/>');
	                 document.AgentRegistrationForm.bankCustomerId.focus();
	                 return false;
		    	 }
		    	 if(!checkAllZero(bankCustomerId)){
		    		 alert('<spring:message code="VALIDATION_BANK_ID_NUMBER" text="Please enter valid Bank Customer ID"/>');
	                 document.AgentRegistrationForm.bankCustomerId.focus();
	                 return false;
		    	 }  
		     }*/
		    /*  
		     else if(commission == "" || commission == " "){
		    	 alert(Alertmsg.commissionEmpty);
		    	 return false;
		     } 
		     else if(commission == "" && commission == " " && commission >100 ){
		    	 alert("Commission Value Cannot be more than 100%");
		    	 return false;
		    	 }
		     else if(commission!="" && commission.length > 0){
		    	 if(commission.search(doublePatt) == -1){
		    		 alert(Alertmsg.commissionPatt);
		    		 return flase;
		    	 }
		     } */
             
		      if(idNumber !=""){
		    	 if(document.getElementById("idType").value==null || document.getElementById("idType").value=="" ){
		    		 alert('Please First Select the ID Type');
		    		 return false;
		    	 }
	            
		    	 if(idNumber.search(IdNumberPattern)==-1){
	                 alert('<spring:message code="VALIDATION_ID_NUMBER" text="Please enter valid id number"/>');
	                 document.AgentRegistrationForm.idNumber.focus();
	                 return false;
		    	 }
		    	 if(idNumber.charAt(0) ==" " || idNumber.charAt(idNumber.length-1) == " "){
	                 alert('<spring:message code="VALIDATION_SPACE_ID_NUMBER" text="Please remove white space in id number"/>');
	                 document.AgentRegistrationForm.idNumber.focus();
	                 return false;
		    	 }
		    	 if(!checkAllZero(idNumber)){
		    		 
		    		 alert('<spring:message code="VALIDATION_ID_NUMBER" text="Please enter valid id number"/>');
	                 document.AgentRegistrationForm.idNumber.focus();
	                 return false;
		    	 } 
		     }
             if(placeOfIssue !=""){
            	     if(document.getElementById("idType").value==null || document.getElementById("idType").value=="" ){
    	    		     alert('Please First Select the ID Type');
    	    		     return false;
    	         	 }
			    	 if(placeOfIssue.search(firstNamePattern)==-1){
		                 alert( '<spring:message code="VALIDATION_PLACEOFISSSUE" text="Please enter valid Name in place of issue"/>');
		                 document.AgentRegistrationForm.placeOfIssue.focus();
		                 return false;
			    		 }
			    	 if(placeOfIssue.charAt(0) ==" " || placeOfIssue.charAt(placeOfIssue.length-1) == " "){
		                 alert('<spring:message code="VALIDATION_SPACE_PLACE_ISSUE" text="Please remove white space in place of issue"/>');
		                 document.AgentRegistrationForm.placeOfIssue.focus();
		                 return false;
			    	 }
			    	 if(!checkAllZero(placeOfIssue)){
			    		 alert( '<spring:message code="VALIDATION_PLACEOFISSSUE" text="Please enter valid Name in place of issue"/>');
		                 document.AgentRegistrationForm.placeOfIssue.focus();
		                 return false;
			    	 } 
			   } 
             if(expiryDate!=""){
            	 if(document.getElementById("idType").value==null || document.getElementById("idType").value=="" ){
		    		 alert('Please First Select the ID Type');
		    		 return false;
		    	 }
			 		 if(!expiryDateValid(expiryDate)){
				     	return false;  
				   }
		     } if(issueDate !=""){
		    	 if(document.getElementById("idType").value==null || document.getElementById("idType").value=="" ){
		    		 alert('Please First Select the ID Type');
		    		 return false;
		    	 }
		 		 if(!issueDateValid(issueDate)){
		 			 
				     	return false;  
				   }
		     } if(expiryDate!="" && issueDate !=""){		    	
		 		 if(!compareExpiryIssueDate(issueDate,expiryDate)){
		 			 alert('<spring:message code="VALIDATION_ISSUE_DATE" text="Please enter issue date less than expiry date"/>');
				     	return false;  
				   }
		     } 
		   
			  if(signature!=""){
					 /* if(action=='ADD' && (filetypeSignature.toLowerCase() != ".jpg")){
					     alert(Alertmsg.signatureLower);
					     document.AgentRegistrationForm.signature.focus();
					     return false;
					 } */
					 if(signatureimge.size > 3145728) {  // validation according to file size
						    alert('Others Image File Size should be less then 3MB.');
						    return false;
					}
					 
					 
			}
			  
			  if(idProof !="") {
				
				 /* if(action=='ADD' && (filetypeIdProof.toLowerCase() != ".jpg")){
					 alert(Alertmsg.idProofLower);
					 document.AgentRegistrationForm.idProof.focus();
					 return false;
				 }
				  */
				 if(idProofimge.size > 3145728) {  // validation according to file size
					    alert('ID Proof Image File Size should be less then 3MB.');
					    return false;
				}
		     }
			
			
			if(profilePhoto !="") {
					/* 
					 if(action=='ADD' && (filetypeprofilePhoto.toLowerCase() != ".jpg")){
						 alert(Alertmsg.idProofLower);
						 document.AgentRegistrationForm.idProof.focus();
						 return false;
					 }
					  */
					 if(profilePhotoimge.size > 3145728) {  // validation according to file size
						    alert('Profile Photo Image File Size should be less then 3MB.');
						    return false;
					}
					 
			     }
		     
		     if(addressProof !="") {
				/* 
				 if(action=='ADD' && (filetypeaddressProof.toLowerCase() != ".jpg")){
					 alert(Alertmsg.idProofLower);
					 document.AgentRegistrationForm.idProof.focus();
					 return false;
				 } */
				 
				 if(addressProofimge.size > 3145728) {  // validation according to file size
					    alert('Address Proof Image File Size should be less then 3MB.');
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
			 if (document.getElementById("profession").value == 'Others') {
					if (pothers.charAt(0) == " "
						|| pothers.charAt(pothers.length - 1) == " ") {
					alert('<spring:message code="VALIDATION_VALID_PROFESSION_SPACE" text="Please remove unwanted space in Profession"/>');
					//document.customerRegistrationForm.others.value=pothers;
					document.getElementById("others").value = "";
					pothers = null;
					enterProfession();
					return false;
				}

				if (pothers.search(professionPattern) == -1) {
					alert('<spring:message code="VALIDATION_VALID_PROFESSION" text="Please enter valid Others Profession"/>');
					//document.customerRegistrationForm.others.value=pothers;
					document.getElementById("others").value = "";
					pothers = null;
					enterProfession();
					return false;
				}
				if (pothers != null && pothers != "") {
					document.AgentRegistrationForm.other.value = pothers;
				}
				}
            /*  if(address.charAt(0) == " " || address.charAt(address.length-1) == " "){
				 alert('<spring:message code="LABEL.ADDRESS.BLANK" text="Please remove the white space from address"/>'); 
				 return false; 	 
		     }else if(address.length > 100){
			     alert('<spring:message code="LABEL.ADDRESS.LENGTH" text="Address should not exceed more than 100 characters"/>');        
			     return false;  	 
			} */ 
		     
		    
		    	 document.AgentRegistrationForm.action = 'saveAgent.htm';
				 document.AgentRegistrationForm.submit();
			 
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
				 document.getElementById('divImpose').style.display = 'block';
				/*  var pothers=prompt('<spring:message code="VALIDATION_PROFESSION" text="Please enter your profession"/>' );
				 if(pothers.charAt(0) == " " || pothers.charAt(pothers.length-1) == " " ){
					  alert( '<spring:message code="VALIDATION_VALID_PROFESSION_SPACE" text="Please remove unwanted space in profession"/>' );
					  //document.AgentRegistrationForm.others.value=pothers;
					  document.getElementById("others").value="";
					  pothers=null;
					 enterProfession();
				 } 
				 
				 if(pothers.search(professionPattern) ==-1 ){
					  alert( '<spring:message code="VALIDATION_VALID_PROFESSION" text="Please enter valid profession"/>' );
					  //document.AgentRegistrationForm.others.value=pothers;
					  document.getElementById("others").value="";
					  pothers=null;
					 enterProfession();
				 } 
					if (pothers!=null && pothers!="")
					  {						
					    document.AgentRegistrationForm.others.value=pothers;
						
					  } 
 */

		}else{
			document.getElementById('divImpose').style.display = 'none';
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
					 document.AgentRegistrationForm.IDTypeothers.value=iothers;
					
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

		
		if (document.AgentRegistrationForm.dob.value != "") {
			t1 = document.AgentRegistrationForm.dob.value;

			if (getAge(t1) < 18) {
				alert(Alertmsg.age18 + " 18 years.");
				document.AgentRegistrationForm.dob.value = "";
				
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
	
	

	document.AgentRegistrationForm.reset();
	document.AgentRegistrationForm.title.value="";
//	document.AgentRegistrationForm.language.value="";
//	document.AgentRegistrationForm.customerProfileId.value="";
//	document.AgentRegistrationForm.questionId.value="";
//	document.AgentRegistrationForm.type.value="";
	document.AgentRegistrationForm.cityId.value="";
	document.AgentRegistrationForm.quarterId.value="";
	document.AgentRegistrationForm.profession.value="";
	document.AgentRegistrationForm.idType.value="";
	document.AgentRegistrationForm.customerProfileId.value="";
//	document.AgentRegistrationForm.language.value="";
//	document.AgentRegistrationForm.country.value="";
	
	
}
		
</script>
</head>
<body>
<div class="row">
<div class="col-lg-12">
<div class="box">
	<div class="box-header">
		<h3 class="box-title">
		<label>
			<c:choose>
						<c:when test="${customerDTO.type==3}"> <spring:message code="LABEL_AGENT_SOLE_MERCHANT" text="Agent Sole Merchant"/>
						 </c:when> 
						 <c:when test="${customerDTO.type==1}"> <spring:message code="LABEL_AGENT" text="Agent"/>
						 </c:when> 
						 <c:when test="${customerDTO.type==2}"> <spring:message code="LABEL_SOLE_MERCHANT" text="Sole-Merchant"/>
						 </c:when> 
					</c:choose></label>
			<label><spring:message code="TITLE_REGISTRATION" text="Registration"></spring:message></label>
		</h3>
	</div>
	<div class="col-sm-5 col-sm-offset-4">
		<div class="col-sm-12" style="color: #ba0101; margin:auto; font-weight: bold; font-size: 12px;">
			<spring:message code="${message}" text="" />
		</div>	
	</div>
	<form:form method="POST" class="form-inline" commandName="customerDTO" name="AgentRegistrationForm" id="AgentRegistrationForm" enctype="multipart/form-data">
	<jsp:include page="csrf_token.jsp"/>
	<div class="col-sm-6 col-sm-offset-9" >
		<a href="javascript:submitForm('AgentRegistrationForm','searchAgentPage.htm')">
		<strong>	
			<c:choose>
						<c:when test="${customerDTO.type==3}"><strong style="margin-left:30px;"><spring:message code="LINK_SEARCH_AGENT_SOLE_MERCHANTS" text="Search Agent Sole Merchants"/></strong>
						 </c:when> 
						 <c:when test="${customerDTO.type==1}"> <strong style="margin-left:50px;"><spring:message code="LINK_SEARCH_AGENTS" text="Search Agents"/></strong>
						 </c:when> 
						 <c:when test="${customerDTO.type==2}"><strong style="margin-left:30px;"> <spring:message code="LINK_SEARCH_SOLE_MERCHANT" text="Search Merchants"/></strong>
						 </c:when> 
					</c:choose>
		</strong></a>
	</div>
	<!-- <div class="box-body"> -->
			<form:hidden path="city" />
			<form:hidden path="quarter" />
			<form:hidden path="question" />
			<form:hidden path="countryId" value="1" id="countryId"/>
			<%-- <form:hidden path="others" /> --%>
			<form:hidden path="IDTypeothers" />
			<form:hidden path="language" id="language" value="en_US"/>
			<input type="hidden" name="newProfile" id="newProfile" value="" />
			<input type="hidden" name="newSignature" id="newSignature" value="" />
			<input type="hidden" name="newIDproof" id="newIDproof" value="" />
			<input type="hidden" name="newAddressProof" id="newAddressProof" value="" />
			<div id="mobileNum" style="visibility:hidden"></div>
			<c:if test="${customerDTO.type==1 || customerDTO.type==2}">	 
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_BUSNIESS_NAME" text="Business Name:"></spring:message></label>
					<form:input path="businessName" maxlength="100" cssClass="form-control" ></form:input>
					<font color="RED"><form:errors path="businessName"></form:errors></font> 
				</div>	
				<c:if test="${customerDTO.type==2}">	 
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_MERCHANT_CODE" text="Merchant Code"></spring:message><font color="red">*</font></label> </label>
					<form:input path="agentCode" maxlength="6" minlength="6" cssClass="form-control" ></form:input>
					<font color="RED"><form:errors path="agentCode"></form:errors></font> 
				</div>	
				</c:if>	
			</div>
			</c:if>	
			<div class="row">

				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_CUSTOMER_TITLE" text="Title:"></spring:message><font color="red">*</font></label> 
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
					<form:option value="Prof">
						<spring:message code="LABEL_USER_PROF" text="Prof"></spring:message>
					</form:option>
					 <form:option value="Dr">
						<spring:message code="LABEL_USER_Dr" text="Dr"></spring:message>
					</form:option>
				</form:select> <font color="RED"><form:errors path="title"></form:errors></font>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_FIRST_NAME" text="First Name:" /><font color="red">*</font></label> 
					<form:input path="firstName" maxlength="30" cssClass="form-control" ></form:input> 
					<font color="RED"><form:errors path="firstName"></form:errors></font>
					<%-- <form:hidden path="customerId" /> --%>
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
					<form:input path="mobileNumber" maxlength="${mobileNumLength}" cssClass="form-control" ></form:input>
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
					<label class="col-sm-4"><spring:message code="LABEL_LANGUAGE" text="Language" /><!-- <font color="red">*</font> -->
					</label> 
					<label class="col-sm-4"><spring:message
									code="LABEL_DEFAULT_LANGUAGE" text="English" /></label>
				
					<%--  commenting to make a default language as English,
									by vineeth on 12-11-2018
					
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
					</form:select> <font color="RED"><form:errors path="language"></form:errors></font> --%>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_PROFILE" text="Profile" /><font color="red">*</font></label> 
					<form:select path="customerProfileId"  multiple="false">
						<form:option value="">
							<spring:message code="LABEL_WUSER_SELECT"
								text="--Please Select--"></spring:message>
						</form:option>
						<form:options items="${masterData.customerProfileList}" itemLabel="profileName" itemValue="profileId"/>
					</form:select> <font color="RED"><form:errors path="customerProfileId"></form:errors></font>
				</div>			
			</div>
			<%-- <div class="row">
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
			</div> --%>
			<div class="row">
				<%-- <div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_ACCTYPE" text="type" /></label>
					 <label class="col-sm-4" id="type">
					 <c:if test="${customerDTO.type == 1 }">
						 <spring:message code="LABEL_AGENT" text="Agent"></spring:message></c:if>								
					 <c:if test="${customerDTO.type == 2 }">
						<spring:message code="LABEL_SOLE_MERCHANT" text="Merchant"></spring:message></c:if>
					 <c:if test="${customerDTO.type == 3 }">
					 	<spring:message code="LABEL_AGENT_SOLE_MERCHANT" text="Agent Sole Merchant"></spring:message></c:if>
					 </label>					
				</div> --%>
				<%-- <label class="col-sm-4"><<spring:message code="LABEL_ACCTYPE" text="Type:" /><font color="red">*</font></label> 
					<form:radiobutton path="type" value="0" selected="true" /> <spring:message code="LABEL_CUSTOMER" text="No" /> 
					<form:radiobutton path="type" value="2" /> <spring:message code="LABEL_SOLE_MERCHANT" text="No" /> 
					<font color="red"><form:errors path="type" /></font>--%>
				 
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_EMAILID" text="Email Id:" /></label>
					<form:input path="emailAddress" maxlength="32" cssClass="form-control" ></form:input> 
					<font color="RED"><form:errors path="emailAddress"></form:errors></font>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_COUNTRY" text="Country :" /></label><!--  <font color="red">*</font> -->
					 <spring:message code="LABEL_COUNTRY_SOUTH_SUDAN" text="SouthSudan"/></div>
				</div>			
			
			<div class="row">
				
					<%-- commenting to make a default country as SouthSudan,
							by vineeth on 12-11-2018
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
			       </select> <font color="RED"><form:errors path="countryId" cssClass="" /></font> --%>
				
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
				
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_ADDRESS" text="Address:" /><font color="red">*</font></label> 
					<form:textarea path="address" cssClass="form-control" maxlength="100" onKeyDown="textCounter(document.AgentRegistrationForm.address,180,180)"  onKeyUp="textCounter(document.AgentRegistrationForm.address,180,180)" style="width: 180px; height: 100px;"></form:textarea> 
					<font color="RED"><form:errors path="address"></form:errors></font>
				</div>
				
					
			</div>
			<div class="row">
				
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_QUARTER" text="Quarter:"/><font color="red">*</font></label> 
					<div id="quarters"><form:select path="quarterId" id="quarterId">
						<option value=""><spring:message
							code="LABEL_WUSER_SELECT" text="--Please Select--"></spring:message></option>
						<form:options items="${quarter}" itemValue="quarterId" itemLabel="quarter"></form:options>
					</form:select></div>
					<font color="RED"><form:errors path="quarterId"></form:errors></font><br />
				</div>
				
				<%-- <div class="col-md-6">
				<label class="col-sm-4"><spring:message	code="LABEL_COMMISSION" text="Commission(%)"></spring:message><!-- <font color="red">*</font> --></label>
				<form:input path="commission" maxlength="20" cssClass="form-control"></form:input>
					<!-- path="commission" -->
				</div> --%>
				
			</div>
			<div class="row">
			<%-- <div class="col-md-6">
				<label class="col-sm-4"><spring:message	code="LABEL_BANK_CUSTOMER_ID" text="Bank Customer ID"></spring:message></label>
				<form:input path="bankCustomerId" maxlength="20" cssClass="form-control"></form:input>
			</div> --%>
			
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_PROFESSION" text="Profession" /></label> 
					<form:select path="profession" multiple="false" onchange="selectProfession1();">
						<form:option value="">
							<spring:message code="LABEL_WUSER_SELECT"
								text="--Please Select--"></spring:message>
						</form:option>
						<form:option value="Nurse">
							<spring:message code="LABEL_NURSE" text="Nurse"></spring:message>
						</form:option>
						<form:option value="Doctor">
							<spring:message code="LABEL_DOCTOR" text="Doctor"></spring:message>
						</form:option> 
						<form:option value="Pharmacist">
							<spring:message code="LABEL_PHARMACIST" text="Pharmacist"></spring:message>
						</form:option>
						<form:option value="Employee">
							<spring:message code="LABEL_EMPLOYEE" text="Employee"></spring:message>
						</form:option>
						<form:option value="Farmer">
							<spring:message code="LABEL_FARMER" text="Farmer"></spring:message>
						</form:option>
						<%-- <form:option value="Gendarme">
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
						<form:option value="Military">
							<spring:message code="LABEL_MILITARY" text="Military"></spring:message>
						</form:option>--%>
						<form:option value="Officer">
							<spring:message code="LABEL_OFFICER" text="Officer"></spring:message>
						</form:option>
						<%-- <form:option value="Pharmacist">
							<spring:message code="LABEL_PHARMACIST" text="Pharmacist"></spring:message>
						</form:option>
						<form:option value="Policeman">
							<spring:message code="LABEL_POLICE" text="Police"></spring:message>
						</form:option> --%>
						<form:option value="Retired">
							<spring:message code="LABEL_RETIRED" text="Retired"></spring:message>
						</form:option>
						<%-- <form:option value="Merchant">
							<spring:message code="LABEL_SOLE_MERCHANT" text="Merchant"></spring:message>
						</form:option> --%>
						<form:option value="Student">
							<spring:message code="LABEL_STUDENT" text="Student"></spring:message>
						</form:option>
					<%-- 	<form:option value="Student(University)">
							<spring:message code="LABEL_STUDENT_UNIVERSITY" text="Student(University)"></spring:message>
						</form:option> --%>
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
						
						<%-- <form:option value="Consular Card">
							<spring:message code="LABEL_CONSULAR_CARD" text="Consular Card"></spring:message>
						</form:option> --%>
						<form:option value="Driving License">
							<spring:message code="LABEL_DRIVING_LISENCE" text="Driving License"></spring:message>
						</form:option>
						<%-- <form:option value="Identity Proof">
							<spring:message code="LABEL_IDENTITY_PROOF" text="Identity Proof"></spring:message>
						</form:option> --%>
						<form:option value="Company Registration Certificate">
							<spring:message code="LABEL_COMPANY_REG_CERTIFICATE" text="Company Registration Certificate"></spring:message>
						</form:option>
						<form:option value="Payam Letter">
							<spring:message code="LABEL_PAYAM_LETTER" text="Payam Letter"></spring:message>										
						</form:option>
						<form:option value="National Identity">
							<spring:message code="LABEL_NATIONAL_IDENTITY" text="National Identity"></spring:message>
						</form:option>
						<form:option value="PassPort">
							<spring:message code="LABEL_PASSPORT" text="PassPort"></spring:message>
						</form:option>
						<form:option value="IDs of Office Administrators and primary assistance">
							<spring:message code="LABEL_IDS_OFFICE_ADMINISTRATORS_AND_PRIMARY_ASSISTANCE" text="IDs of Office Administrators and primary assistance"></spring:message>
						</form:option>
						<form:option value="Completed Agent Application form">
							<spring:message code="LABEL_COMPLETED_AGENT_APPLICATION_FORM" text="Completed Agent Application form"></spring:message>
						</form:option>
						<form:option value="Memorandum & Articles of Association">
							<spring:message code="LABEL_MEMORANDOM_ARTICLES" text="Memorandum & Articles of Association"></spring:message>
						</form:option>
						<form:option value="TIN Certificate">
							<spring:message code="LABEL_TIN_CERTIFICATE" text="TIN Certificate"></spring:message>
						</form:option>
						
						<form:option value="License/ Business Permit">
							<spring:message code="LABEL_LICENCE_BUSINESS_PERMIT" text="License/ Business Permit"></spring:message>
						</form:option>
						<form:option value="Certificate of Incorporation">
							<spring:message code="LABEL_CERTIFICATE_INCPRPORATION" text="Certificate of Incorporation"></spring:message>
						</form:option>
						<form:option value="Tax Identification Number (TIN)">
							<spring:message code="LABEL_IDENTIFICATION_NUMBER" text="Tax Identification Number (TIN)"></spring:message>
						</form:option>
						<form:option value="Police clearance certificate">
							<spring:message code="LABEL_POLICE_CLEARANCE_CERTIFICATE" text="Police clearance certificate"></spring:message>
						</form:option>
						
						<form:option value="Others">
							<spring:message code="LABEL_OTHERS" text="Others"></spring:message>
						</form:option>
					</form:select>
				</div>
			</div>
					<div class="row" id="divImpose" style="display:none;">
	                  <div class="col-sm-6">
	                  <label class="col-sm-4"><spring:message code="LABEL_OTHERS_PROFESSION" text="Others Profession" /><font color="red">*</font></label>	                    
	                  <form:input path="others" maxlength="50" id="other"
							cssClass="form-control"></form:input>
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
					<label class="col-sm-4"><spring:message code="LABEL_ISSUE_DATE" text="Issue Date" /></label> 
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
					<label class="col-sm-4"><spring:message code="LABEL_SIGNATURE" text="Others:" /> <c:if test="${(customerDTO.customerId eq null) }"></c:if></label> 
					<form:input path="signature" type="file" id="signature" accept=".jpeg,.jpg,.png" onchange="loadImageFile('signature');"/>
					<span style="font-size: 11px;color: red;">(3MB max size allowed)</span>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_ID_PROOF" text="ID Proof:"/> <c:if test="${(customerDTO.customerId eq null) }"></c:if></label> 
					<form:input path="idProof" type="file" id="idProof" accept=".jpeg,.jpg,.png" onchange="loadImageFile('idProof');"/>
					<span style="font-size: 11px;color: red;">(3MB max size allowed)</span>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_PROFILE_PHOTO" text="Profile Photo:" /> <c:if test="${(customerDTO.customerId eq null) }"></c:if></label> 
					<form:input path="profilePhoto" type="file" id="profilePhoto" accept=".jpeg,.jpg,.png" onchange="loadImageFile('profilePhoto');"/>
					<span style="font-size: 11px;color: red;">(3MB max size allowed)</span>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_ADDRESS_PROOF" text="Address Proof:"/> <c:if test="${(customerDTO.customerId eq null) }"></c:if></label> 
					<form:input path="addressProof" type="file" id="addressProof" accept=".jpeg,.jpg,.png" onchange="loadImageFile('addressProof');"/>
					<span style="font-size: 11px;color: red;">(3MB max size allowed)</span>
				</div>
			</div>
			</c:if>
			<c:if test="${customerDTO.isIDProofRequired == 1 }">
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_SIGNATURE" text="Signature:" /> <c:if test="${(customerDTO.customerId eq null) }"></c:if></label> 
					<form:input path="signature" type="file" id="signature" accept=".jpeg,.jpg,.png" onchange="loadImageFile('signature');"/>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_ID_PROOF" text="ID Proof:"/> <c:if test="${(customerDTO.customerId eq null) }"></c:if></label> 
					<form:input path="idProof" type="file" id="idProof" accept=".jpeg,.jpg,.png" onchange="loadImageFile('idProof');"/>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_PROFILE_PHOTO" text="Profile Photo:" /> <c:if test="${(customerDTO.customerId eq null) }"></c:if></label> 
					<form:input path="profilePhoto" type="file" id="profilePhoto" accept=".jpeg,.jpg,.png" onchange="loadImageFile('profilePhoto');"/>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_ADDRESS_PROOF" text="Address Proof:"/> <c:if test="${(customerDTO.customerId eq null) }"></c:if></label> 
					<form:input path="addressProof" type="file" id="addressProof" accept=".jpeg,.jpg,.png" onchange="loadImageFile('addressProof');"/>
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
				 <input type="button" class="btn btn-primary" value="<spring:message code="LABEL_RESET" text="reset"/>" onclick="clearForm();"></input>
				</c:if>
				<c:if test="${(customerDTO.customerId ne null) }">
				 <input type="button" class="btn btn-primary" value="<spring:message code="LABEL_CANCEL" text="cancel"/>" onclick="cancelForm1();"></input>
				</c:if>
			</div>	<br /> <br />
				<form:hidden path="isIDProofRequired" value="${customerDTO.isIDProofRequired}" id="isIDProofRequired"/>				
				<form:hidden path="type" value="${customerDTO.type}" id="type"/>	
			<br/>
			<input type="hidden" value="${customerDTO.type}" id="custType" name="custType"/>
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
