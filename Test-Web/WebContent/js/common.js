function submitForm(formName, url){
	var form = document.getElementById(formName);
	form.method = "post";
	form.action=url;
	form.submit();
}

//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting


function submitlink(url,formId){
	var canSubmit = true;
	//$.blockUI({ css: { backgroundColor: 'green', color: '#fff'} });
	if(canSubmit==true){
		canSubmit = false;
		var frm = document.getElementById(formId);
		
		document.body.style.cursor = 'wait';
		if(frm!=null){
			frm.method = "post";
			frm.action = url;
			frm.submit();
			$("body").css("cursor", "default");
			 //Naqui: Since loader was spinning continuously
			//$.unblockUI();
		}
		
	}
}

//@...end
function setTokenValFrmAjaxResp() {
	var newTokenVal = $('div[id=csrfToken] > input[name=csrfToken]').val();
	$('div[id=csrfToken]').remove();
	if (newTokenVal == 'undefined' || newTokenVal == '' || newTokenVal == null) {
		document.location.href = 'logout.htm';
	}
	$('input[name=csrfToken]').val(newTokenVal);
}

var prevItem = null;
function activateItem(t){
   if(prevItem != null){
      prevItem.className = prevItem.className.replace(/{\b}?activeItem/, "");
   }
   t.className += " activeItem";
   prevItem = t;
}

function noBack() {
		window.history.forward();
	}
	noBack();
	window.onload = noBack;
	window.onpageshow = function(evt) {
		if (evt.persisted)
			noBack();
	}
	window.onunload = function() {
		void (0);
	}

	function logout() {
		var url = "logout.htm";	
		document.welcomeform.method="post";
		document.welcomeform.action=url;
		document.welcomeform.submit();	
	}
	
	function home(){
		//var url="home.htm";
		var url="txnSummary.htm";
		document.welcomeform.method = "post";
		document.welcomeform.action = url;
		document.welcomeform.submit();
	}
	
	function displaySubMenus(menu) {
		var lastMenu = "";
		if (lastMenu != "" && lastMenu != menu)
			document.getElementById(lastMenu).style.display = "none";
		lastMenu = menu;
		if (document.getElementById(menu).style.display == "")
			document.getElementById(menu).style.display = "none";
		else
			document.getElementById(menu).style.display = "";

	}
	
	document.oncontextmenu = function() {
		return false; 
	}

	
	/* var dminyear = 1900;  
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
	   function dtvalid(fromDate,toDate)  
	   {  
	   var monthdays = finaldays(12);  
	   var fcpos1=fromDate.indexOf(chsep); 
	   var fcpos2=fromDate.indexOf(chsep,fcpos1+1);  

	   var fdaystr=fromDate.substring(0,fcpos1);  
	   var fmonthstr=fromDate.substring(fcpos1+1,fcpos2);  
	   var fyearstr=fromDate.substring(fcpos2+1);  

	   var tcpos1=toDate.indexOf(chsep); 
	   var tcpos2=toDate.indexOf(chsep,tcpos1+1);  

	   var tdaystr=toDate.substring(0,tcpos1);  
	   var tmonthstr=toDate.substring(tcpos1+1,tcpos2);  
	   var tyearstr=toDate.substring(tcpos2+1);  

	   fstrYr=fyearstr;  
	   tstrYr=tyearstr;
	   if (fromDate.charAt(0)=="0" && fromDate.length>1) 
	   	strdatestrdate=fromDate.substring(1);  
	   if (fmonthstr.charAt(0)=="0" && fmonthstr.length>1) 
	   	fmonthstrmonthstr=fmonthstr.substring(0);  
	   if (toDate.charAt(0)=="0" && toDate.length>1) 
	   	tstrdatestrdate=toDate.substring(1);  
	   if (tmonthstr.charAt(0)=="0" && tmonthstr.length>1) 
	   	tmonthstrmonthstr=tmonthstr.substring(0); 

	   for (var i = 1; i <= 3; i++) {  
	   if (fstrYr.charAt(0)=="0" && fstrYr.length>1) fstrYrstrYr=fstrYr.substring(1);  
	   }  

	   for (var i = 1; i <= 3; i++) {  
	   	if (tstrYr.charAt(0)=="0" && tstrYr.length>1) tstrYrstrYr=tstrYr.substring(1);  
	   	}  
	   // The parseInt is used to get a numeric value from a string  
	   fpmonth=parseInt(fromDate.substring(3,5),10);  
	   fpday=parseInt(fromDate.substring(0,2),10); 
	   fpyear=parseInt(fstrYr) ; 

	   tpmonth=parseInt(toDate.substring(3,5),10);  
	   tpday=parseInt(toDate.substring(0,2),10); 
	   tpyear=parseInt(tstrYr) ; 


	   if (fcpos1==-1 || fcpos2==-1){  
	   	
	   alert(Alertmsg.fromDateFormat);  
	   return false;  
	   } 
	   if (tcpos1==-1 || tcpos2==-1){
	   	
	   	alert(Alertmsg.toDateFromat);  
	   	return false;  
	   } 

	   if(fdaystr.length>2){
	   	
	   	alert(Alertmsg.validFromDay);
	   	return false;
	   }
	   if(tdaystr.length>2){
	   	
	   	alert(Alertmsg.validToDay);
	   	return false;
	   }
	   if(fmonthstr.length>2){
	   	
	   	alert(Alertmsg.validFromMonth);
	   	return false;
	   }
	   if(tmonthstr.length>2){
	   	
	   	alert(Alertmsg.validToMonth);
	   	return false;
	   }
	   if (fdaystr.length<1 || fpday<1 || fpday>31 || (fpmonth==2 && fpday>februarycheck(fpyear))   || fpday > monthdays[fpmonth]){  
	   	
	   	alert(Alertmsg.validfDay);  
	   	return false ; 
	   	} 
	   if (tdaystr.length<1 || tpday<1 || tpday>31 || (tpmonth==2 && tpday>februarycheck(tpyear))   || tpday > monthdays[tpmonth]){  
	   	
	   	alert(Alertmsg.validTodays);  
	   	return false ; 
	   	} 


	   if (fmonthstr.length<1 || fpmonth<1 || fpmonth>12){  
	   	
	   alert(Alertmsg.validFromMonth);  
	   return false;  
	   }

	   if (tmonthstr.length<1 || tpmonth<1 || tpmonth>12){  
	   	
	   	alert(Alertmsg.validToMonth);  
	   	return false;  
	   	}

	   if (yearstr.length != 4 || pyear==0 || pyear<dminyear || pyear>dmaxyear){  
	   alert("Input a valid 4 digit year between "+dminyear+" and "+dmaxyear);  
	   return false;  
	   }  
	   if (fromDate.indexOf(chsep,fcpos2+1)!=-1 || checkinteger(getcharacters(fromDate, chsep))==false){  
	   	
	   alert(Alertmsg.validFromDay) ; 
	   return false;  
	   }
	   if (toDate.indexOf(chsep,tcpos2+1)!=-1 || checkinteger(getcharacters(toDate, chsep))==false){
	   	
	   	alert(Alertmsg.validToDay) ; 
	   	return false;  
	   	}  
	   return true;  
	   }  
	   function validdate(fromDate,toDate)  
	   {  
	    
	   if (dtvalid(fromDate,toDate)==false)  
	   {  

	   return false;  
	   }  
	   return true ; 
	   }  
	   
	 
	 //  var chsep= "/" ; 
	   
	   function fromDatevalid(fromDate)  
	   {  
	   var monthdays = finaldays(12);  
	   var fcpos1=fromDate.indexOf(chsep); 
	   var fcpos2=fromDate.indexOf(chsep,fcpos1+1);  

	   var fdaystr=fromDate.substring(0,fcpos1);  
	   var fmonthstr=fromDate.substring(fcpos1+1,fcpos2);  
	   var fyearstr=fromDate.substring(fcpos2+1);  



	   fstrYr=fyearstr;  
	   

	   for (var i = 1; i <= 3; i++) {  
	   if (fstrYr.charAt(0)=="0" && fstrYr.length>1) fstrYrstrYr=fstrYr.substring(1);  
	   }  

	    
	   // The parseInt is used to get a numeric value from a string  
	   fpmonth=parseInt(fromDate.substring(3,5),10);  
	   fpday=parseInt(fromDate.substring(0,2),10); 
	   fpyear=parseInt(fstrYr) ; 

	   


	   if (fcpos1==-1 || fcpos2==-1){  
	   	
	   alert(Alertmsg.fromDateFormat);  
	   return false;  
	   } 
	   

	   if(fdaystr.length>2){
	   	
	   	alert(Alertmsg.validFromDay);
	   	return false;
	   }
	   
	   if(fmonthstr.length>2){
	   	
	   	alert(Alertmsg.validFromMonth);
	   	return false;
	   }

	   if (fdaystr.length<1 || fpday<1 || fpday>31 || (fpmonth==2 && fpday>februarycheck(fpyear))   || fpday > monthdays[fpmonth]){  
	   	
	   	alert(Alertmsg.validFromDay);  
	   	return false ; 
	   	} 
	   


	   if (fmonthstr.length<1 || fpmonth<1 || fpmonth>12){  
	   	
	   alert(Alertmsg.validFromMonth);  
	   return false;  
	   }

	   

	   if (yearstr.length != 4 || pyear==0 || pyear<dminyear || pyear>dmaxyear){  
	   alert("Input a valid 4 digit year between "+dminyear+" and "+dmaxyear);  
	   return false;  
	   }  
	   if (fromDate.indexOf(chsep,fcpos2+1)!=-1 || checkinteger(getcharacters(fromDate, chsep))==false){  
	   	
	   alert(Alertmsg.validFromDay) ; 
	   return false;  
	   }
	   
	   return true;  
	   }  
	   function validFromDate(fromDate)  
	   {  
	    
	   if (fromDatevalid(fromDate)==false)  
	   {  

	   return false;  
	   }  
	   return true ; 
	   }  
	   
	   function toDatevalid(toDate)  
	   {  
	   var monthdays = finaldays(12);  
	   var fcpos1=toDate.indexOf(chsep); 
	   var fcpos2=toDate.indexOf(chsep,fcpos1+1);  

	   var fdaystr=toDate.substring(0,fcpos1);  
	   var fmonthstr=toDate.substring(fcpos1+1,fcpos2);  
	   var fyearstr=toDate.substring(fcpos2+1);  



	   fstrYr=fyearstr;  
	   

	   for (var i = 1; i <= 3; i++) {  
	   if (fstrYr.charAt(0)=="0" && fstrYr.length>1) fstrYrstrYr=fstrYr.substring(1);  
	   }  

	    
	   // The parseInt is used to get a numeric value from a string  
	   fpmonth=parseInt(toDate.substring(3,5),10);  
	   fpday=parseInt(toDate.substring(0,2),10); 
	   fpyear=parseInt(fstrYr) ; 

	   


	   if (fcpos1==-1 || fcpos2==-1){  
	   	
	   alert(Alertmsg.toDateFromat);  
	   return false;  
	   } 
	   

	   if(fdaystr.length>2){
	   	
	   	alert(Alertmsg.validToDay);
	   	return false;
	   }
	   
	   if(fmonthstr.length>2){
	   	
	   	alert(Alertmsg.validToMonth);
	   	return false;
	   }

	   if (fdaystr.length<1 || fpday<1 || fpday>31 || (fpmonth==2 && fpday>februarycheck(fpyear))   || fpday > monthdays[fpmonth]){  
	   	
	   	alert(Alertmsg.validToDay);  
	   	return false ; 
	   	} 
	   


	   if (fmonthstr.length<1 || fpmonth<1 || fpmonth>12){  
	   	
	   alert(Alertmsg.validToMonth);  
	   return false;  
	   }

	   

	   if (yearstr.length != 4 || pyear==0 || pyear<dminyear || pyear>dmaxyear){  
	   alert("Input a valid 4 digit year between "+dminyear+" and "+dmaxyear);  
	   return false;  
	   }  
	   if (toDate.indexOf(chsep,fcpos2+1)!=-1 || checkinteger(getcharacters(toDate, chsep))==false){  
	   	
	   alert(Alertmsg.validTodays) ; 
	   return false;  
	   }
	   
	   return true;  
	   }  
	   function validToDate(toDate)  
	   {  
	    
	   if (toDatevalid(toDate)==false)  
	   {  

	   return false;  
	   }  
	   return true ; 
	   }  */
	   
	   