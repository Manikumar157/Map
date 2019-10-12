function checkAll(){
		var len=document.getElementsByName('manageCustomerCheck').length;
		if(document.getElementById('checkAll_id').checked==true){
			for(var i=0;i<len;i++){
				if(!document.getElementsByName('manageCustomerCheck')[i].disabled){
					document.getElementsByName('manageCustomerCheck')[i].checked=true;
				}
			}
		}else{
			for(var i=0;i<len;i++){
				document.getElementsByName('manageCustomerCheck')[i].checked=false;
			}
		}
	}
var checkedIds=new Array(0);
var conf="";
function apprOrRejectCust(type, formName) {
	var displayRejectDialog=true;
	var len=document.getElementsByName('manageCustomerCheck').length;
	var count=0;
	var canSubmit=true;

	
	for(var i=0;i<len;i++){
		if(document.getElementsByName('manageCustomerCheck')[i].checked==true){
			count++;
			var custStatus =(document.getElementsByName('manageCustomerCheck')[i].id).split("#")[1];
			if(custStatus=="11"){
				alert("Please select pending or rejected status Customers for Approve/Reject.");
				return false;
			}
			checkedIds[i]=document.getElementsByName('manageCustomerCheck')[i].value;
		}
	}
	if(canSubmit == true && count == 0 && type == 'Approve'){
		alert(Alertmsg.customerApprove);
		return false;
	}else if(canSubmit == true && count == 0 && type == 'Reject'){
		alert(Alertmsg.customerReject);
		return false;
	}
	
	
	
	if(canSubmit == true && type == 'Reject'){
		//conf = confirm(Alertmsg.custRejectConfirm);
		
		if (displayRejectDialog && $('#rejectComment').val() == '') {
			$('#myModal').modal({backdrop: 'static', keyboard: false});
			displayRejectDialog=false;
			document.getElementById('rejectComment').value='';
			return;
		}else {
			conf=true;
		}
		 
		
	}else if(canSubmit == true && type == 'Approve'){
		conf = confirm(Alertmsg.custApproveConfirm);
		if (conf){
			apprOrRejectCustSubmit(canSubmit,type, formName)
		}
	}
}
function apprOrRejectCustSubmit(canSubmit,type, formName) {
	if (canSubmit == true){
		document.getElementById('manageCustomer').value = checkedIds;
		document.getElementById('kycStatus').value = type;
		
		if (type == 'Reject') {
			
			if ($('#rejectComment').val() != '' && $('#rejectComment').val() != undefined) {
				
				if(confirm(Alertmsg.custRejectConfirm))
				{
					submitlink("apprOrRejectCust.htm",formName);
				}
				document.getElementById('rejectComment').value='';
				
			} else   {
				alert("Please comment for rejection.");
				$('#rejectComment').focus();
			}
			
		}else if (conf) {
			 
			submitlink("apprOrRejectCust.htm",formName);
		}
		
	}/*else if(canSubmit == true && conf == true && type == 'Approve'){
		document.customerForm.manageCustomer.value=checkedIds;
		document.getElementById('type').value = type;
		submitlink("apprOrRejectCust.htm","customerForm");
	}*/
	


	
}
function closeModel(){
	document.getElementById('rejectComment').value='';
}

function check(){
	Zapatec.Calendar.setup({
		firstDay          : 1,
		timeFormat        : "12",
		electric          : false,
		inputField        : "agreementToField0",
		button            : "trigger",
		ifFormat          : "%d/%m/%Y",
		daFormat          : "%Y/%m/%d",
		timeInterval      : 01
	});   

}
function check1(){

	Zapatec.Calendar.setup({
		firstDay          : 1,
		timeFormat        : "12",
		electric          : false,
		inputField        : "agreementFromField0",
		button            : "trigger1",
		ifFormat          : "%d/%m/%Y",
		daFormat          : "%Y/%m/%d",
		timeInterval      : 01
	});

}



function fileValidate(){

	var filetypeBranchFile = document.bankManageMentForm.branchFile.value.substring(document.bankManageMentForm.branchFile.value.lastIndexOf("."));

	if(document.bankManageMentForm.branchFile.value=="" || document.bankManageMentForm.branchFile.value==" ") {
		alert(Alertmsg.branchValid);
		document.bankManageMentForm.branchFile.focus();
	}else{
		document.bankManageMentForm.submit();
	}

}

var selectedCHouseNameArr=new Array(),priorityArr=new Array();
var z=0;var selectedcHouse=0;
function addCHouse(status,a,b) { 
	
	var selectedCHouse;var selectedCHouseName;var priority;	
	var combo = document.getElementById("chouses") ;
	if(status==1){
		selectedCHouse = a;
		var newIndex;
		for(var s=0; s < combo.options.length; s++)
		{
			if(combo.options[s].value == a)
				newIndex=s;
		}
		selectedCHouseName = combo.options[newIndex].firstChild.nodeValue ;
		priority=b;
	}else{

		selectedCHouse = combo.value;
		selectedCHouseName = combo.options[combo.selectedIndex].firstChild.nodeValue ;

		priority = document.getElementById("priority").value;
		var priorityInput='^[0-9]*$';	

		if(selectedCHouse=="0"){
			alert(Alertmsg.clearinghouse);
			return false;
		}
		if(priority == "" ){
			alert(Alertmsg.emptyPriority);
			return false;
		}else if(priority==0){
			alert(Alertmsg.zeroPriority);
			return false;
		}else if(document.getElementById("priority").value.search(priorityInput)== -1){
			alert(Alertmsg.priority);
			return false;
		}else if(priority.length>1 ){
			alert(Alertmsg.priorityLength);
			return false;
		}else{
			for(var i=0;i<z;i++){
				if(selectedCHouseNameArr[i]==document.getElementById("chouses" ).value){
					alert(Alertmsg.uniqueHouse);
					return false;
				}
				else if(priorityArr[i]== document.getElementById("priority").value){
					alert(Alertmsg.uniquePriority);
					return false;
				}
			}
		}		
	}

	priorityArr[priorityArr.length]=priority;
	selectedCHouseNameArr[selectedCHouseNameArr.length]=selectedCHouse;
	z++;
	
	var table=document.getElementById("tblGrid");       
	var lastRow = table.rows.length;         

	var row=table.insertRow(lastRow);

	var td1 = row.insertCell(0);
	td1.setAttribute("align","center");
	td1.setAttribute("width","70px;");
	td1.setAttribute("height","15px;");
	td1.innerHTML = lastRow+1;

	var td2 = row.insertCell(1);
	td2.setAttribute("align","center");
	td2.setAttribute("width","250px;");
	td2.innerHTML = selectedCHouseName;

	var td3 = row.insertCell(2);
	td3.setAttribute("align","center");
	td3.setAttribute("width","70px;");
	td3.innerHTML = priority; 

	var td4 = row.insertCell(3);
	td4.setAttribute("align","center");
	td4.setAttribute("width","70px;");
	td4.innerHTML = "<a onclick='deleteRow();' style='cursor:pointer'>"+"Delete"+"</a>"; 

	// document.getElementById("clearingHouses").value =  ((currVal1 == '' ) ? "" : currVal1 +",") + selectedCHouse ;
	// document.getElementById("clearingHousePriorities").value = ((currVal2 == "" ) ? "" : currVal2 +",")+ priority  ;
	document.getElementById("chouses").selectedIndex="0";
	document.getElementById("priority").value="";
	
	

}

function deleteRow() {	

	var table = document.getElementById("tblGrid");
	var rows = document.getElementById('tblGrid').getElementsByTagName('tbody')[0].getElementsByTagName('tr');
	for (var s = 0; s < rows.length; s++) {
		rows[s].onclick = function() {
			var i=this.rowIndex;    
			table.deleteRow(i);				
			selectedCHouseNameArr.splice(i,1);
			priorityArr.splice(i,1);        
			z--;
			if(selectedcHouse!=0)
			{selectedcHouse--;}
			
		};
	}  	
}

function writeValues(){	
	
	if(priorityArr.length==0)		
		return false;
	var t1="";var t2="";
	for (var i = 0; i < priorityArr.length; i++) {			 
		//alert("selectedCHouseName :: "+selectedCHouseNameArr[i]+" priority :: "+priorityArr[i]);
		if(priorityArr[i]!=undefined){
			t1=(i!=(priorityArr.length-1))?t1+selectedCHouseNameArr[i]+",":t1+selectedCHouseNameArr[i];
			t2=(i!=(priorityArr.length-1))?t2+priorityArr[i]+",":t2+priorityArr[i];			 
		}
	}
	document.getElementById("clearingHouses").value=t1;
	document.getElementById("clearingHousePriorities").value=t2;       

}

function Validate(){

	validateFields();			

}

function compareDate(fromdt,todt)
{
	var dt1  = parseInt(fromdt.substring(0,2),10);
	var mon1 = parseInt(fromdt.substring(3,5),10)-1;
	var yr1  = parseInt(fromdt.substring(6,10),10);
	var dt2  = parseInt(todt.substring(0,2),10);
	var mon2 = parseInt(todt.substring(3,5),10)-1;
	var yr2  = parseInt(todt.substring(6,10),10);
	var fromDate = new Date(yr1, mon1, dt1);
	var toDate = new Date(yr2, mon2, dt2); 

	if(toDate < fromDate)
		return true;
	else
		return false;

}

function compareFromDate(fromdt,todt)
{
	var fromdate=fromdt.split('/');
	var toDate=todt.split('/');
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
	var fromDate = new Date(yr1, mon1-1, dt1);
	var toDate = new Date(yr2, mon2-1, dt2);
	if( fromDate <= toDate )

		return true;
	else
		return false;

}
function currentDate(fromdt){
	var dt1  = parseInt(fromdt.substring(0,2),10);
	var mon1 = parseInt(fromdt.substring(3,5),10)-1;
	var yr1  = parseInt(fromdt.substring(6,10),10);
	var fromDate = new Date(yr1, mon1, dt1);
	var cdate=new Date();

	var month = cdate.getMonth();
	var day = cdate.getDate();
	var year = cdate.getFullYear();
	var ccdate=new Date(year,month,day);

	if(fromDate>=ccdate)
		return true;
	else
		return false;
}
/*function cancelForm(){
	document.bankManageMentForm.action="showBankManagementForm.htm";
	document.bankManageMentForm.submit();
}*/

function currentDate(fromdt){
	var dt1  = parseInt(fromdt.substring(0,2),10);
	var mon1 = parseInt(fromdt.substring(3,5),10)-1;
	var yr1  = parseInt(fromdt.substring(6,10),10);
	var fromDate = new Date(yr1, mon1, dt1);
	var cdate=new Date();

	var month = cdate.getMonth();
	var day = cdate.getDate();
	var year = cdate.getFullYear();
	var ccdate=new Date(year,month,day);

	if(fromDate>=ccdate)
		return true;
	else
		return false;
}

function checkSpace(bankCode)
{
	var count=0;
	for(var i=0;i<bankCode.length;i++){
		if(bankCode.charAt(i)==" "){
			count++;
		}
	}
	if(count>0){
		return false;
	}
	else{
		return true;
	}
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

function validdate(fromDate,toDate)  
{  

	if (dtvalid(fromDate,toDate)==false)  
	{  

		return false;  
	}  
	return true ; 
}  

function dtvalid(fromDate,toDate)
{
//	var DateOfBirth=new String("12/912/2012");
	var monthdays = finaldays(12);  
	var fChar1 = fromDate.charAt(2);
	var fChar2 = fromDate.charAt(5);
	var chsep= "/" ;
	var fcpos1=fromDate.indexOf(chsep); 
	var fcpos2=fromDate.indexOf(chsep,fcpos1+1);  

	if (fcpos1==-1 || fcpos2==-1 ){  

		alert(Alertmsg.fromDateFormat);  
		return false;  
	} 

	if (fromDate.length<10 || fromDate.length>10){  

		alert(Alertmsg.fromDateFormat);  
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

	day = fromDate.substring(0,2);
	month = fromDate.substring(3,5);
	year = fromDate.substring(6,10);

	var tChar1 = toDate.charAt(2);
	var tChar2 = toDate.charAt(5);
	var chsep= "/" ;
	var tcpos1=toDate.indexOf(chsep); 
	var tcpos2=toDate.indexOf(chsep,tcpos1+1);  
	if (tcpos1==-1 || tcpos2==-1 ){  
		alert(Alertmsg.toDateFromat);  
		return false;  
	} 

	if (fromDate.length<10 || fromDate.length>10){  

		alert(Alertmsg.fromDateFormat);  
		return false;  
	} 
	/*if ( Char1 =='/' && Char2 == '/' )
{
 alert ('Please check foramt');
return false;
}*/

	if(!validDay(day))
	{
		alert(Alertmsg.validFromDay);

		return false;
	}

	if(!validMonth(month)){	
		alert(Alertmsg.validFromMonth);

		return false;
	}

	if ((month==2 && day>februarycheck(year))   || day > monthdays[month]){  

		alert(Alertmsg.validFromDay);  
		return false ; 
	} 

	var tday;
	var tmonth;
	var tyear;

	tday = toDate.substring(0,2);
	tmonth = toDate.substring(3,5);
	tyear = toDate.substring(6,10);

	if (toDate.length<10 || toDate.length>10){  

		alert(Alertmsg.toDateFromat);  
		return false;  
	} 

	if(!validDay(tday))
	{
		alert(Alertmsg.validToDay);

		return false;
	}

	if(!validMonth(tmonth)){	
		alert(Alertmsg.validToMonth);

		return false;
	}

	if ((tmonth==2 && tday>februarycheck(tyear))   || tday > monthdays[tmonth]){  

		alert(Alertmsg.validToDay);  
		return false ; 
	} 


	/*if(!validYear(year)){

alert('Invalid year in date of birth');
DateOfBirth=null;
return false;
}*/

} // end func

function validDay(day)
{

	if (IsNumeric(day) && day.length<3)
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
function IsNumeric(sText)
{

	var ValidChars = "0123456789.";
	var IsNumber=true;
	var Char;

	for (var i = 0; i < sText.length && IsNumber == true; i++)
	{
		Char = sText.charAt(i);
		if (ValidChars.indexOf(Char) == -1)
		{
			IsNumber = false;
		}
	}

	return IsNumber;
} // end func

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

}

function LogAccess(url,sessionId){
	document.getElementById('sessionId').value=sessionId;
	submitlink(url,'accessLogForm');
}

$(document).ready(function() {
	

	var currentLocation =window.location.href.substr(window.location.href.lastIndexOf("/")+1);
	//salert("Hi "+currentLocation)
	 var tableOfResults=null;
	 var columnData=[];
	 var linkData={};
	 if(currentLocation =='showAccessLog.htm'){
		 columnData = [
          { "data": "UserId" },
          { "data": "LoginTime" },
          { "data": "LogoutTime" },
          { "data": "Action" }
      ]
		 linkData={
	        	"orderable": true,
	            "targets":  [3],
	            "render": function (data, type, row) {
	                /*return "<a href=\"javascript:LogAccess('accessLogForm','viewPageVisited.htm'?sessionId="+row.sessionId+"')\">View</a>";*/
	            	return "<a href=\"javascript:LogAccess('viewPageVisited.htm','"+row.sessionId+"')\">View</a>";
	            },
	        }
	 }
	 else if(currentLocation == 'searchAuditLogForm.htm'){
		 columnData = [

		               { "data": "updatedBy"},
		               { "data": "updatedDate" },
		               { "data": "entityName" },
		               { "data": "entityAttribute" }, 
		               { "data": "message" },
		               { "data": "newValue" },
		               { "data": "oldValue" }
		           ]
	 }
    tableOfResults = $('#tableData').DataTable({
		"processing": true,
		"serverSide": true,
		"paging" : true,
		"searching": false,
		"ordering": true,
		"lengthChange": false,
		"sDom":"tpr",
        "dom": 'difrtp',
		"SearchTerm":"",
	    /* "Page":2,
	    "Draw":10,
	    "EntityToSearch":"1", */
		  "ajax" : {
			"url" : 'data'+currentLocation,
			"type" : "POST",
			  "data": function (data) {
				 	var SearchParameters = {};
	                var info = (tableOfResults == null) ? { page: 0, length: 10 } : tableOfResults.page.info();
	                SearchParameters.SearchTerm = '';
	                SearchParameters.pageNumber = data.start/data.length + 1;
	                SearchParameters.Draw = data.draw;
	                SearchParameters.sortColumn = '';
	                var additionalArgs=[];
	               
	                if(currentLocation == 'showAccessLog.htm')
	                	additionalArgs = ["userId","fromDate","toDate"];
	                else if(currentLocation == 'searchAuditLogForm.htm')
	                	additionalArgs = ["module","userId","fromDate","toDate","length","direction"];
					var i='', k='';
					//var l='', m='';
					$.each(additionalArgs, function(i, k){
						if($("#"+k).val()){
							SearchParameters[k] = $("#"+k).val();
							console.log(SearchParameters[k])
						}
					}
					);
					
					 var tableHeaderIndex=data.order[0].column;
						var sortBy=data.order[0].dir;
						var tableHeadervalue=data.columns[tableHeaderIndex].data;
						
						//console.log(sortBy);
						//console.log(tableHeadervalue);
						SearchParameters.sortColumn=tableHeadervalue;
						SearchParameters.sortBy=sortBy;
	                return SearchParameters;
	            },
	        	"error":function(errorThrown){
	        		//ShowDataTable(null);
		        }
		}, 
		"columns":columnData,
		"columnDefs": [ {"ordering": true},
		                linkData

        ],
       
	});

});