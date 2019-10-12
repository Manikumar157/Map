function validate(action){
	var countryId=document.getElementById("countryId").value;
	
	if(countryId!=null){
		var mobileNumLen=parseInt(document.getElementById("mobileNum").innerHTML);

	}


	var firstNamePattern='^\[a-zA-ZÀ-ÿ]*$';
	var mobilepattern='^\[0-9]{10,12}$';
	var emailID=document.webUserManageMentForm.email.value;
	var firstName=document.getElementById("firstName").value;
	var middleName=document.getElementById("middleName").value;
	var lastName=document.getElementById("lastName").value;
	var mobileNumber=document.getElementById("mobileNumber").value;	
	var accessid =  document.getElementById("accessmodediv").value ;


	var numPattern='^[0-9]*$';
	if(document.getElementById('countryId').value!=''){
		mobilepattern='^\[0-9]{'+mobileNumLen+'}$'; 
	}

	if(firstName==""){
		alert( Alertmsg.firstname) ;
		return false;
	}
	if(firstName.charAt(0) == " " || firstName.charAt(firstName.length-1) == " "){
		alert( Alertmsg.firstNameSpace) ;
		return false;
	}
	if(firstName.search(firstNamePattern)==-1){
		alert( Alertmsg.firstnamevalid) ;
		return false;
	}
	if(middleName.charAt(0) == " " || middleName.charAt(middleName.length-1) == " "){
		alert( Alertmsg.middleName) ;
		return false;
	}
	if(middleName.search(firstNamePattern)==-1){
		alert( Alertmsg.middleName) ;
		return false;
	}
	if(lastName.charAt(0) == " " || lastName.charAt(lastName.length-1) == " "){
		alert( Alertmsg.lastName) ;
		return false;
	}
	if(lastName.search(firstNamePattern)==-1){
		alert( Alertmsg.lastName) ;
		return false;	
	}
	if(firstName.length<2){
		alert( Alertmsg.firstNameLength) ;
		return false;
	}
	
	if (emailID==""){
		alert( Alertmsg.emailnotempty) ;
		return false;
	}
	if (checkEmail(emailID)==false){
		alert( Alertmsg.emailvalid) ;
		return false;
	}
	if ( countryId== "" ) {
		alert(Alertmsg.countryName); 
		return false;
	}
	if(mobileNumber.charAt(0) == " " || mobileNumber.charAt(mobileNumber.length-1) == " "){
		alert( Alertmsg.mobileNumberSpace) ;
		return false;
	}
	if(document.webUserManageMentForm.mobileNumber.value.search(mobilepattern)==-1){
		alert(Alertmsg.mobileNumLength +" "+ mobileNumLen);
		return false;
	}
	
	if(document.webUserManageMentForm.mobileNumber.value==""){
		alert( Alertmsg.mobilenumber) ;
		return false;
	}
	if(document.webUserManageMentForm.mobileNumber.value.indexOf("0000000")!=-1){
		alert( Alertmsg.mobilenumbervalid) ;
		return false;
	}
	if(document.webUserManageMentForm.mobileNumber.value.search(mobilepattern)==-1){
		alert( Alertmsg.mobilenumberdigits) ;
		return false;
	}
	var a = 0, rdbtn=document.getElementsByName("status")
	for(var i=0;i<rdbtn.length;i++) {
	if(rdbtn.item(i).checked == false) {
	a++;
	}
	}
	if(a == rdbtn.length) {
	alert("Please enter Status");

	return false;
	}
	
	if(document.webUserManageMentForm.roleId.value==""){
		alert( Alertmsg.userrole) ;
		return false;
	}
	
	if(document.getElementById("roleId").value==28){
		if(document.getElementById("bulkPaymentPartnerId").value==""){
			alert('Please Select the BulkPayment Partners');
			return false;
		}}
	
	/*@Vinod joshi Added the code  @Start*/
	if(document.getElementById("roleId").value==24 || document.getElementById("roleId").value==25 || document.getElementById("roleId").value==26){
		if(document.getElementById("businessPartnerId").value==""){
			alert('Please Select the Principle Agent');
			return false;
		}}
		
		
		/*if(document.getElementById("accessMode").value==""){
			alert('Please select the Access Mode');
			return false;
		}*/
		var a = 0, rdbtn=document.getElementsByName("accessMode")
		if(document.getElementById("roleId").value==24 || document.getElementById("roleId").value==28
				 || document.getElementById("roleId").value==3 || document.getElementById("roleId").value==10
				 || document.getElementById("roleId").value==16 || document.getElementById("roleId").value==22 || document.getElementById("roleId").value==27
				 || document.getElementById("roleId").value==29|| document.getElementById("roleId").value==30 || document.getElementById("roleId").value==2){
			rdbtn.item(0).checked = true;
		}
		for(var i=0;i<rdbtn.length;i++) {
		if(rdbtn.item(i).checked == false) {
		a++;
		}
		}
		if(a == rdbtn.length) {
		alert("Please enter select the access mode");

		return false;
		}

	
	
	if(document.getElementById("roleId").value==3 || document.getElementById("roleId").value==8|| document.getElementById("roleId").value==12 || 
		document.getElementById("roleId").value==13 ||document.getElementById("roleId").value==14 || document.getElementById("roleId").value==2
		|| document.getElementById("roleId").value==9|| document.getElementById("roleId").value==10|| document.getElementById("roleId").value==11
		|| document.getElementById("roleId").value==22|| document.getElementById("roleId").value==21|| document.getElementById("roleId").value==16){
		if(document.getElementById("bankId").value==""){
			alert(Alertmsg.selectBank);
			return false;
		}
		if(document.getElementById("branchId").value=="" || document.getElementById("branchId").value=="select"){
			alert(Alertmsg.selectBranch);
			return false;
		}

	}/*else if(document.getElementById("roleId").value==2){
			if(document.getElementById("bank").value==""){
				alert(Alertmsg.selectBank);
				return false;
			}

		}*/else if(document.getElementById("roleId").value==7){
			if(document.getElementById("groupId").value==""){
				alert(Alertmsg.selectBankGroup);
				return false;
			}			
		}
	/*@End*/
	if(action=='LABEL_UPDATE'){

		document.webUserManageMentForm.action="updateUser.htm";
		document.webUserManageMentForm.submit();
	}else{

		document.webUserManageMentForm.action="saveUser.htm"; 
		document.webUserManageMentForm.submit();
	}
	return true;

}
function checkEmail() {
	var email = document.getElementById('email');
	var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	if (email.value!="" && !filter.test(email.value)) {
		return false;
	}else{
		return true;
	}
}

function cancelForm(){
	document.webUserManageMentForm.action="showWebUserForm.htm";
	document.webUserManageMentForm.submit();
}

function confirmReset(url,form){
	if(confirm(Alertmsg.confirmResetPassword)){
	submitlink(url,form);	
		
	}else{
		return false;
	}
}
