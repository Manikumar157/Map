var conf="";
function updateKycStatus(formName, type, customerId) {
	var displayRejectDialog=true;
	var checkedIds=new Array(0);
	
	if(type == 'Reject'){
		
		if (displayRejectDialog ) {
			$('#myModal').modal('show');
			displayRejectDialog=false;
			 return;
		}else{
			conf=true;
		}
		 
		
	}else if( type == 'Approve'){
		if(confirm(Alertmsg.custApproveConfirm)){
			apprOrRejectCustSubmit(formName, type,customerId);
		}
	}
}
	function apprOrRejectCustSubmit(formName, type,customerId) {

		document.getElementById('manageCustomer').value = customerId;
		document.getElementById('kycStatus').value = type;
		
		if (type == 'Reject') {

			if ($('#rejectComment').val() != '' && $('#rejectComment').val() != undefined) {
				
				if(confirm(Alertmsg.custRejectConfirm))
				submitlink("apprOrRejectCust.htm",formName);
			} else   {
				alert("Please comment for rejection.");
				$('#rejectComment').focus();
			}

		
		}else {
			 
			submitlink("apprOrRejectCust.htm",formName);
		}
		
	}/*else if(canSubmit == true && conf == true && type == 'Approve'){
		document.customerForm.manageCustomer.value=checkedIds;
		document.getElementById('type').value = type;
		submitlink("apprOrRejectCust.htm","customerForm");
	}*/
	



$(document).ready(function() {
	
/*	commenting to make a default country as SouthSudan,
	by vineeth on 12-11-2018
	
	$("#country").change(function() {
		document.getElementById("countryId").value = document.getElementById("country").value ;
		$country = document.getElementById("country").value;
		$csrfToken = $("#csrfToken").val();
		
		$.post("getCityList.htm", {
			country : $country,
			csrfToken : $csrfToken
		}, function(data) {
			document.getElementById("cities").innerHTML="";
			document.getElementById("cities").innerHTML = data;
			setTokenValFrmAjaxResp();
			applyChosen();
		});
	});*/

	$("#cities").change(function() {
		document.getElementById("city").value = document.getElementById("cityId").value ;
		$city = document.getElementById("cityId").value;
		$csrfToken = $("#csrfToken").val();
		$.post("getQuartersList.htm", {
			city : $city,
			csrfToken : $csrfToken
		}, function(data) {
			document.getElementById("quarters").innerHTML="";
			document.getElementById("quarters").innerHTML = data;
			setTokenValFrmAjaxResp();
			applyChosen();
		});
	});

	$("#quarters").change(function() {
		document.getElementById("quarter").value = document.getElementById("quarterId").value ;
	});
	
/*	commenting to make a default country as SouthSudan,
	by vineeth on 12-11-2018, and there is no Security quesions and answers and also mobilenumber length is static
	
	$("#country").change(function() {
		$country = document.getElementById("countryId").value;
		$.post("mobileNumLenthRequest.htm", {
			country : $country
		}, function(data) {
			document.getElementById("mobileNum").innerHTML="";
			document.getElementById("mobileNum").innerHTML = data.match(/\d/g).join("");
			
		});
	});	
	
	$("#language").change(function() {
		
		$language = document.getElementById("language").value;
		$csrfToken = $("#csrfToken").val();
		
		$.post("getQustionList.htm", {
			language : $language,
			csrfToken : $csrfToken
		}, function(data) {
			document.getElementById("questions").innerHTML="";
			document.getElementById("questions").innerHTML = data;
			setTokenValFrmAjaxResp();
			applyChosen();
		});
	});	
	$("#questions").change(function() {
		 document.getElementById("question").value= document.getElementById("questionId").value;
		
	}); */	
});




/*function clearForm(){
	
	 alert("yes");
	
	 document.AgentRegistrationForm.title.value=="";
	 
	var city=document.getElementById("cityId");
	
	var quarter=document.getElementById("quarterId");
	var question=document.getElementById("questionId");
		  var foundDefault = false;
		  for(var i = 0; i < city.length; i++) {
		    if(city.options[i].defaultSelected) {
		    	city.selectedIndex = i;
		      foundDefault = true;
		    }
		    if(!foundDefault) {
		    	city.selectedIndex = 0;
		    }
		  }
		  for(var i = 0; i < quarter.length; i++) {
			    if(quarter.options[i].defaultSelected) {
			    	quarter.selectedIndex = i;
			      foundDefault = true;
			    }
			    if(!foundDefault) {
			    	quarter.selectedIndex = 0;
			    }
			  }
		  for(var i = 0; i < question.length; i++) {
			    if(question.options[i].defaultSelected) {
			    	question.selectedIndex = i;
			      foundDefault = true;
			    }
			    if(!foundDefault) {
			    	quarter.selectedIndex = 0;
			    }
			  }
		  
		  elements = document.getElementsById("title").value;
	
		 
		  for(i=0; i < elements.length ; i++){
		   elements[i].selectedIndex= 0;
		  }

		  
	//document.getElementById("AgentRegistrationForm").reset();
	document.AgentRegistrationForm.reset();
	
}*/
function cancelForm1(){
	/*var customerId=document.getElementById("customerId").value;
	document.AgentRegistrationForm.action="viewAgent.htm?customerId="+customerId;
	document.AgentRegistrationForm.submit();*/
	
	var customerId=document.getElementById("customerId").value;
	var url = "viewAgent.htm";
	submitlink(url,'AgentRegistrationForm');
	
}
function check(){
		Zapatec.Calendar.setup({
        firstDay          : 1,
        timeFormat        : "12",
        electric          : false,
        inputField        : "dob",
        button            : "trigger",
        ifFormat          : "%d/%m/%Y",
        daFormat          : "%Y/%m/%d",
        timeInterval      : 01
      });				 
}

function expiryDate(){
	Zapatec.Calendar.setup({
    firstDay          : 1,
    timeFormat        : "12",
    electric          : false,
    inputField        : "expiryDate",
    button            : "trigger1",
    ifFormat          : "%d/%m/%Y",
    daFormat          : "%Y/%m/%d",
    timeInterval      : 01
  });	
	
}

function issueDate(){
		Zapatec.Calendar.setup({
        firstDay          : 1,
        timeFormat        : "12",
        electric          : false,
        inputField        : "issueDate",
        button            : "trigger2",
        ifFormat          : "%d/%m/%Y",
        daFormat          : "%Y/%m/%d",
        timeInterval      : 01
      });				 
}

function openNewWindow( url )
{
		window.open (url,"mywindow","menubar=0,toolbar=0,resizable=1,width=450,height=450");
}
function compareDod(dob){
	var dt1  = parseInt(dob.substring(0,2),10);
    var mon1 = parseInt(dob.substring(3,5),10)-1;
    var yr1  = parseInt(dob.substring(6,10),10);
      var dbirth = new Date(yr1, mon1, dt1);
      var cdate=new Date();
    
var month = cdate.getMonth();
var day = cdate.getDate();
var year = cdate.getFullYear();
var ccdate=new Date(year,month,day);

if(dbirth<ccdate)
      return true;
 else
 return false;
}
	

var dminyear = 1900;  
var dmaxyear = 2200;  
var chsep= "/" ; 
function checkinteger(str1){  
var x;  
for (x = 0; x < str1.length; x++){   
  
var cr = str1.charAt(x);  
if (((cr < "0") || (cr > "9")))   
return false;  
}  
return true;  
}  
function getcharacters(s, chsep1){  
var x;  
var Stringreturn = "";  
for (x = 0; x < s.length; x++){   
var cr = s.charAt(x);  
if (chsep.indexOf(cr) == -1)   
Stringreturn += cr;  
}  
return Stringreturn;  
}  
function februarycheck(cyear)  
{  
return (((cyear % 4 == 0) && ( (!(cyear % 100 == 0)) || (cyear % 400 == 0))) ? 29 : 28 );  
}  
function finaldays(nr) {  
for (var x = 1; x <= nr; x++) {  
this[x] = 31;  
if (x==4 || x==6 || x==9 || x==11)  
{  
this[x] = 30}; 
if (x==2)  
{  
this[x] = 29};  
}   
return this;  
}   




function expiryDateValid(expDate)  
{  
 
if (validateExpiryDate(expDate)==false)  
{  

return false;  
}  
return true ; 
}  

function compareExpiryIssueDate(issueDate,expiryDate){
	
		var fromdate=issueDate.split('/');
		var toDate=expiryDate.split('/');
		var strFromDate=fromdate[0];
		var strFromMonth=fromdate[1];
		var strToDate=toDate[0];
		var strToMonth=toDate[1];
		
		if(parseInt(fromdate[0])<10)
		{
		strFromDate=fromdate[0].replace('0','');
		}
		if(parseInt(fromdate[1])<10)
		{
		strFromMonth=fromdate[1].replace('0','');
		}
		if(parseInt(toDate[0])<10)
		{
			strToDate=toDate[0].replace('0','');
		}
		if(parseInt(toDate[1])<10)
		{
			strToMonth=toDate[1].replace('0','');
		}
		var mon1 = parseInt(strFromMonth);
		var dt1 = parseInt(strFromDate);
		var yr1 = parseInt(fromdate[2]);

		var mon2 = parseInt(strToMonth);
		var dt2 = parseInt(strToDate);
		var yr2 = parseInt(toDate[2]); 
		    var issueDT = new Date(yr1, mon1-1, dt1);
			var expiryDT = new Date(yr2, mon2-1, dt2);
		    if( expiryDT <= issueDT )
				
				return false;
			else
				return true;

	
}

function checkDateOfBirth(issueDate)  
{  
 
if (validateDateofBirth(issueDate)==false)  
{  

return false;  
}  
return true ; 
}  

function validateDateofBirth(DateOfBirth)
{
//var DateOfBirth=new String("12/912/2012");
	var monthdays = finaldays(12);  
var Char1 = DateOfBirth.charAt(2);
var Char2 = DateOfBirth.charAt(5);
var chsep= "/" ;
var fcpos1=DateOfBirth.indexOf(chsep); 
var fcpos2=DateOfBirth.indexOf(chsep,fcpos1+1);  
if (fcpos1==-1 || fcpos2==-1 ){  
	
alert(Alertmsg.settlementDateFormat);  
return false;  
} 

if (DateOfBirth.length<10 || DateOfBirth.length>10){  

	alert(Alertmsg.settlementDateFormat);  
	return false;  
} 
/*if ( Char1 =='/' && Char2 == '/' )
{
 alert ('Please check foramt');
return false;
}*/

var day;
var month;
var year;

day = DateOfBirth.substring(0,2);
month = DateOfBirth.substring(3,5);
year = DateOfBirth.substring(6,10);


if(!validDay(day))
{
alert(Alertmsg.validDay);
DateOfBirth=null;
return false;
}

if(!validMonth(month)){	
alert(Alertmsg.validMonth);
DateOfBirth=null;
return false;
}

if ((month==2 && day>februarycheck(year))   || day > monthdays[month]){  
	
	alert(Alertmsg.validSettlementDay);  
	return false ; 
	} 
/*if(!validYear(year)){
		
alert('Invalid year in date of birth');
DateOfBirth=null;
return false;
}*/

} // end func

function IsNumeric(sText)
{
var ValidChars = "0123456789.";
var IsNumber=true;
var Char;

for (i = 0; i < sText.length && IsNumber == true; i++)
{
Char = sText.charAt(i);
if (ValidChars.indexOf(Char) == -1)
{
IsNumber = false;
}
}

return IsNumber;
} // end func


function validDay(day)
{
if ( IsNumeric(day) && day.length<3)
{
if( day >0 && day <32)
{
return true;
}
else
{
return false;
}
}
else
{
return false;
}

}// end func


function validMonth(month)
{
if ( IsNumeric(month) )
{
if( month >0 && month <13)
{
return true;
}
else
{
return false;
}
}
else
{
return false;
}
}// end func



function validYear(year)
{
var d = new Date();
var currentYear = d.getFullYear();

if( year.length!= 4) { return false; }

if ( IsNumeric(year) )
{
if( year >0 && year <=currentYear)
{
return true;
}
else
{
return false;
}

}
else
{
return false;
}

}// end func 

function issueDateValid(issueDate)  
{  
 
if (validateIssueDate(issueDate)==false)  
{  

return false;  
}  
return true ; 
}  

function validateIssueDate(issueDate)  
{ 
	
	var monthdays = finaldays(12);  
	var Char1 = issueDate.charAt(2);
	var Char2 = issueDate.charAt(5);
	var chsep= "/" ;
	var fcpos1=issueDate.indexOf(chsep); 
	var fcpos2=issueDate.indexOf(chsep,fcpos1+1);  
	if (fcpos1==-1 || fcpos2==-1 ){  
		
	alert(Alertmsg.issueDateFormat);  
	return false;  
	} 

	if (issueDate.length<10 || issueDate.length>10){  

		alert(Alertmsg.issueDateFormat);  
		return false;  
	} 
	

	var day;
	var month;
	var year;

	day = issueDate.substring(0,2);
	month = issueDate.substring(3,5);
	year = issueDate.substring(6,10);


	if(!validDay(day))
	{
	alert(Alertmsg.validIssueDays);
	return false;
	}

	if(!validMonth(month)){	
	alert(Alertmsg.validIssueMonth);
	return false;
	}

	if ((month==2 && day>februarycheck(year))   || day > monthdays[month]){  
		
		alert(Alertmsg.validIssueDays);  
		return false ; 
		} 
	
}

function validateExpiryDate(expDate){
	var monthdays = finaldays(12);  
	var Char1 = expDate.charAt(2);
	var Char2 = expDate.charAt(5);
	var chsep= "/" ;
	var fcpos1=expDate.indexOf(chsep); 
	var fcpos2=expDate.indexOf(chsep,fcpos1+1);  
	if (fcpos1==-1 || fcpos2==-1 ){  
		
	alert(Alertmsg.issueDateFormat);  
	return false;  
	} 

	if (expDate.length<10 || expDate.length>10){  

		alert(Alertmsg.expiryDateFormat);  
		return false;  
	} 
	

	var day;
	var month;
	var year;

	day = expDate.substring(0,2);
	month = expDate.substring(3,5);
	year = expDate.substring(6,10);


	if(!validDay(day))
	{
	alert(Alertmsg.validExpiryDays);
	return false;
	}

	if(!validMonth(month)){	
	alert(Alertmsg.validExpiryMonth);
	return false;
	}

	if ((month==2 && day>februarycheck(year))   || day > monthdays[month]){  
		
		alert(Alertmsg.validExpiryDays);  
		return false ; 
		} 
	
	
}