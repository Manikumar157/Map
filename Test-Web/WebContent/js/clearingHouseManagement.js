function cancelForm(){
        document.clearingPool.action="showClearanceHouseForm.htm";
        document.clearingPool.submit();
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
 

 function settlementDateValidate(fromDate)
 {
 
 var monthdays = finaldays(12);  
 var Char1 = fromDate.charAt(2);
 var Char2 = fromDate.charAt(5);
 var chsep= "/" ;
 var fcpos1=fromDate.indexOf(chsep); 
 var fcpos2=fromDate.indexOf(chsep,fcpos1+1);  
 if (fcpos1==-1 || fcpos2==-1 ){  
 	
 alert(Alertmsg.settlementDateFormat);  
 return false;  
 } 

 if (fromDate.length<10 || fromDate.length>10){  

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

 day = fromDate.substring(0,2);
 month = fromDate.substring(3,5);
 year = fromDate.substring(6,10);


 if(!validDay(day))
 {
 alert(Alertmsg.validSettlementDay);

 return false;
 }

 if(!validMonth(month)){	
 alert(Alertmsg.validSettlementMonth);

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
 function validatesettlementDate(fromDate)  
 {  
  
 if (settlementDateValidate(fromDate)==false)  
 {  

return false;  
 }  
 return true ; 
 }  
 
 function checkEmailID(emailID) {

		var atStr = "@";
		var dotStr = ".";
		var undeScoreStr = "_";
		var iChars = "!#$%^&*()+=-[]\\\';,/{}|\":<>?~`";
	
		if (emailID.indexOf(atStr) == -1) {
			return false;
		} else {
			var em = emailID.substring(emailID.indexOf(atStr) + 1);
			var em2 = emailID.substring(0, emailID.indexOf(atStr));
			var strPos = emailID.indexOf(atStr);

			if (emailID.indexOf(atStr) == 0) {
				return false;
			} else if (emailID.indexOf(dotStr) == -1) {
				return false;
			} else if (emailID.indexOf(dotStr) == 0) {
				return false;
			} else if (emailID.indexOf(undeScoreStr) == 0) {
				return false;
			}
			if (emailID.substring(strPos - 1, strPos)==(undeScoreStr)
					|| emailID.substring(strPos + 1, strPos + 2)==(
							undeScoreStr)) {
				return false;
			} else if (emailID.indexOf(atStr, (strPos + 1)) != -1) {
				return false;
			} else if (emailID.substring(strPos - 1, strPos)==(dotStr)
					|| emailID.substring(strPos + 1, strPos + 2)==(dotStr)) {
				return false;
			} else if (emailID.indexOf(" ") != -1) {
				return false;
			} else if (checkEmailFisrtPart(em2)) {
				return false;
			} else if (checkEmailSecondPart(em)) {
				return false;
			} else {
				for (var i = 0; i < emailID.length; i++) {
					if (iChars.indexOf(emailID.charAt(i)) != -1) {
						return false;
					}
				}
			}
		}
		return true;
	}

	function checkEmailFisrtPart(em2) {
		var dotStr = ".";
		var undeScoreStr = "_";

		if (checkEmailNumPresAtFstPos(em2)) {
			return true;
		}

		if (em2.indexOf(undeScoreStr) != -1) {
			if (em2.indexOf(undeScoreStr, (em2.indexOf(undeScoreStr) + 1)) == (em2
					.indexOf(undeScoreStr) + 1)) {
				return true;
			}
		}

		if (em2.indexOf(dotStr) != -1) {
			if (em2.indexOf(dotStr, (em2.indexOf(dotStr) + 1)) == (em2
					.indexOf(dotStr) + 1)) {
				return true;
			}
		}

		return false;
	}

	function checkEmailSecondPart(em) {
		var dotStr = ".";
		var undeScoreStr = "_";
		var emDotPos = em.indexOf(dotStr);
		
		if(emDotPos==-1){
			return true;
		}
		
		if (checkEmailNumPresAfter(em)) {
			return true;
		}

		if(checkNumOfDots(em) > 2){
			return true;
		}
		
		if(em.substring(em.indexOf(dotStr)+1,em.indexOf(dotStr)+2)==dotStr){
			return true;
		}
		if(em.substring(em.lastIndexOf(dotStr)+1,em.lastIndexOf(dotStr)+2)==dotStr){
			return true;
		}
		if((em.substring(em.lastIndexOf(dotStr)+1,em.length)).length<2 || 
		(em.substring(em.lastIndexOf(dotStr)+1,em.length)).length>3){
			return true;
		}
		
		if (em.indexOf(undeScoreStr) != -1) {
			return true;
		}

		return false;
	}

	function checkEmailNumPresAtFstPos(str) {
		var iChars = "0123456789";
		for (var i = 0; i < iChars.length; i++) {
			if (str.indexOf(iChars.charAt(i)) == 0) {
				return true;
			}
		}
		return false;
	}

	function checkEmailNumPresAfter(str) {
		var iChars = "0123456789";
		for (var i = 0; i < str.length; i++) {
			if (iChars.indexOf(str.charAt(i)) != -1) {
				return true;
			}
		}
		return false;
	}
	
	function checkNumOfDots(em) {
		var countDots = 0;
		var dotChar = '.';
		var emCharArray = em.split('');
		for (var i = 0; i < emCharArray.length; i++) {
			if (emCharArray[i] == dotChar) {
				countDots++;
			}
		}
		return countDots;
	}
	
	
