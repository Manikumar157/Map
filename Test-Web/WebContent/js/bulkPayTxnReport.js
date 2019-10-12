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

function bulkPayTxnReportExcel(){
		
		 submitlink("xlsForBulkPayTxn.htm","bulkPayTxnReportForm"); 
		 for(var i=0;i<150000;i++);
		 document.body.style.cursor = 'default';
		 canSubmit = true; 
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

	   var tableOfResults=null;
	    tableOfResults = $('#bulkPayTxnReportData').DataTable({
			"processing": true,
			"serverSide": true,
			"paging" : true,
			"searching": false,
			"lengthChange": false,
			"SearchTerm":"",
		
			  "ajax" : {
				"url" : 'searchBulkPayTxnReportData.htm',
				"type" : "POST",
				  "data": function (data) {
					 	var SearchParameters = {};
		                var info = (tableOfResults == null) ? { page: 0, length: 10 } : tableOfResults.page.info();
		                SearchParameters.SearchTerm = '';
		                SearchParameters.pageNumber = data.start/data.length + 1;
		                SearchParameters.Draw = data.draw;

						var additionalArgs = ["mobileNumber","fromDate","toDate"];
						var i='', k='';
						$.each(additionalArgs, function(i, k){
							if($("#"+k).val()){
								SearchParameters[k] = $("#"+k).val();
								//console.log(SearchParameters[k])
							}
						});
						
						    var tableHeaderIndex=data.order[0].column;
							var sortBy=data.order[0].dir;
							var tableHeadervalue=data.columns[tableHeaderIndex].data;
							
							//console.log(sortBy);
							//console.log(tableHeadervalue);
							SearchParameters.sortColumn=tableHeadervalue;
							SearchParameters.sortBy=sortBy;
							SearchParameters.index=data.start;
		                
		                return SearchParameters;
		            },
		        	"error":function(errorThrown){
		        		ShowDataTable(null);
			        }
			}, 
			"columns": [
				{ "data": "SerNo" },
	            { "data": "TransactionID" },
	            { "data": "Name" },
	            { "data": "MobileNumber" },
	            { "data": "TransactionDate" },
	            { "data": "amount" },
	            { "data": "SC" },
	            { "data": "Status" }
	        ],
			"columnDefs": [ {"ordering": true},
			{ "orderable": false, "targets": 0 }
],
	       
	        "order": [[ 1, 'desc' ]]
		});
	    
	});
