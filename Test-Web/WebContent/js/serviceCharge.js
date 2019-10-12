   
	/* calender -------------- */

	function check(){	
		Zapatec.Calendar.setup({
        firstDay          : 1,
        timeFormat        : "12",
        electric          : false,
        inputField        : "applicableFrom",
        button            : "trigger",
        ifFormat          : "%d/%m/%Y",
        daFormat          : "%Y/%m/%d",
        timeInterval          : 01
      });
	 
  	}

 	function check1(){	
		Zapatec.Calendar.setup({
        firstDay          : 1,
        timeFormat        : "12",
        electric          : false,
        inputField        : "applicableTo",
        button            : "trigger1",
        ifFormat          : "%d/%m/%Y",
        daFormat          : "%Y/%m/%d",
        timeInterval          : 01
      });
	 
  	}
 	/* end -------------- */
 	
 	
 	
 	/* Service Charge Rule Dynamic Table data create/update/delete  -------------- */
 	
  	 var minTxnValue = new Array(),maxTxnValue=new Array(),scPercentage=new Array(),
  	 sChargeFixed=new Array(),discountLimit=new Array(),minSCharge=new Array(),maxSCharge=new Array();
  	 
     var l=0;
     function addTxnDetails(id) {
    	 
    	 	var a = document.getElementById("minTxn").value;
            var b = document.getElementById("maxTxn").value;
            var c = document.getElementById("scPerc").value;
            var d = document.getElementById("sChargeFixed").value;
            var e = document.getElementById("dLimit").value;
            var f = document.getElementById("minSCharge").value;
            var g = document.getElementById("maxSCharge").value;     
            var txnid=document.getElementById("transactions").value;   
            /*if(findType([10,15,20,30,35,70,75,98,37,40,39],txnid)!=undefined){
            a="0",b="0",c="0",e="0",f="0",g=d;
            }*/
            	           
            var editStatus=(id==undefined)?0:1;
            if(txnValidate(a,b,c,d,e,f,g,-1)){            

            minTxnValue[l]=a; maxTxnValue[l]=b; scPercentage[l]=c;sChargeFixed[l]=d;discountLimit[l]=e; minSCharge[l]=f; maxSCharge[l]=g; l++; 
            
            addTxnRows(a,b,c,d,e,f,g,editStatus,-1);

        	clearTxnFields();
            
            }
            if(sChargeFixed.length!=0)
    		document.getElementById("transactions").disabled=true;
    		        	  
        }
     
     	function txnValidate(a,b,c,d,e,f,g,rid){
     	
     		var txnid=document.getElementById("transactions").value; 
     		
     	//Author_Name<Abu kalam Azad>Date<25/06/2018> once click at Add button transaction type alert should display, if any transaction not selected, start..
     		if(txnid==""){
                alert("Please select Transaction Type");           
                return false;
     		}
     		
     		//End...
     		if(findType([10,15,20,30,35,70,75,98,37,40,39],txnid)==undefined){
     		
     		if(a==""||b==""||c==""||d==""||e==""||f==""||g==""){
            	alert(AlertMsg.empty);
            	return false;
            }
            if(isNaN(a) || a.charAt(0) == " " || a.charAt(a.length-1) == " "|| a.indexOf(".")!=-1 || a.length>15 || a<0){ 
            	alert(AlertMsg.minTxn);
    			return false;
            }
            if(isNaN(b) || b=="0" || b.charAt(0) == " " || b.charAt(b.length-1) == " " || b.indexOf(".")!=-1|| b.length>15 || b<0){
            	alert(AlertMsg.maxTxn);
    			return false;
            }          
            if(parseInt(b)<=parseInt(a)){
        		alert(AlertMsg.validMaxtxn);
          		return false;
          	}           
            if(minTxnValue.length!=0){
            	  for(var k=0;k<minTxnValue.length;k++){
                      if(rid!=k){
                         if(a>=parseInt(minTxnValue[k])&&a<=parseInt(maxTxnValue[k]) || b>=parseInt(minTxnValue[k])&&b<=parseInt(maxTxnValue[k])){
                      	alert(AlertMsg.rangeTxn);
          				return false;
          			 
                         }
                      }                     
                }            
            }   
            if(isNaN(c) || c.charAt(0) == " " || c.charAt(c.length-1) == " " || c.length>6 || c<0 || parseFloat(c)>100){
            	alert(AlertMsg.perc);
    			return false;
            }
            if(isNaN(d) || d.charAt(0) == " " || d.charAt(d.length-1) == " " || d.indexOf(".")!=-1 || d.length> 15|| d<0){
            	alert(AlertMsg.fixed);
    			return false;
            }
            if(parseInt(d)>parseInt(b)){
        		alert(AlertMsg.fixedGreater);
          		return false;
          	}   
            if(isNaN(e) || e.charAt(0) == " " || e.charAt(e.length-1) == " " || e.indexOf(".")!=-1|| e.length>15 || e<0){
            	alert(AlertMsg.dlimit);
    			return false;
            }
            if(parseInt(e)>parseInt(b)){
        		alert(AlertMsg.dlimitGreater);
          		return false;
          	}      
            if(isNaN(f) || f.charAt(0) == " " || f.charAt(f.length-1) == " " || f.indexOf(".")!=-1 || f.length>15){
            	alert(AlertMsg.minSC);
    			return false;
            }
            if(isNaN(g) || g.charAt(0) == " " || g.charAt(g.length-1) == " " || g.indexOf(".")!=-1 || g.length>15 || g<0){
            	alert(AlertMsg.maxSC);
    			return false;
            }   
            if(parseInt(g)<parseInt(f)){
        		alert(AlertMsg.validmaxScv);
          		return false;
          	}
            /*if(g <= f){
            	alert(AlertMsg.validmaxScv);
    			return false; 	
            }*/
            	
            if(parseInt(g)>parseInt(b)){
        		alert(AlertMsg.maxSCLimit);
          		return false;
          	}      
     		}else{
     			 if(d=="" || d.indexOf(".")!=-1){
     				alert(AlertMsg.fixed);
         			return false;
     			 }
     			 if(isNaN(d) || d.charAt(0) == " " || d.charAt(d.length-1) == " "|| d.length>15 || d<0){
                 	alert(AlertMsg.fixed);
         			return false;
                 }
     			 if(minTxnValue.length!=0){
     			 for(var k=0;k<minTxnValue.length;k++){
                          if(rid!=k){
                             if(d==parseInt(sChargeFixed[k])){
                          	 alert(AlertMsg.rangeTxn);
              				 return false;
              				 }
                          }
                    }
     			 }
     			
     			if(parseInt(g)<parseInt(f)){
            		alert(AlertMsg.validmaxScv);
              		return false;
              	}
     		}
            return true;
     	}
        
        function addTxnRows(a, b, c, d, e, f, g,editStatus,rindex) { 
                   
        	var table=document.getElementById("tblGrid");       
        	var lastRow =(rindex==-1)?table.rows.length:rindex;	             
            
            var row=table.insertRow(lastRow);
            if(lastRow%2!=0)
            row.setAttribute("bgColor","#F9F9F9");  
            
            var td2 = row.insertCell(0);
            td2.setAttribute("width", "17%");
            td2.setAttribute("height", "25px");
            td2.setAttribute("align","center");
            td2.innerHTML= a;  

            var td3 = row.insertCell(1);
            td3.setAttribute("width", "18%");
            td3.setAttribute("align","center");
            td3.innerHTML = b;            

            var td4 = row.insertCell(2);
            td4.setAttribute("width", "12%");    
            td4.setAttribute("align","center");        
            td4.innerHTML = c;

            var td5 = row.insertCell(3);
            td5.setAttribute("width", "10%");
            td5.setAttribute("align","center");
            td5.innerHTML = d;

            var td6 = row.insertCell(4);
            td6.setAttribute("width", "16%");
            td6.setAttribute("align","center");
            td6.innerHTML = e;

            var td7 = row.insertCell(5);
            td7.setAttribute("width", "8%");
            td7.setAttribute("align","center");
            td7.innerHTML = f;

            var td8 = row.insertCell(6);
            td8.setAttribute("width", "9%");
            td8.setAttribute("align","center");
            td8.innerHTML = g;    
            
            var td9 = row.insertCell(7);
            td9.setAttribute("width", "15%");
            td9.setAttribute("align","center");
            var p="<a onclick='deleteTxnRowss();' style='cursor:pointer'>"+AlertMsg.delet+"</a>";
            var q="<a onclick='txnEditDays();' style='cursor:pointer'>"+AlertMsg.edit+"</a>";
            td9.innerHTML =(editStatus==1)?q+" | "+p:p; 
            
        }
    	
    	function deleteTxnRowss() { 
    		if(document.getElementById("sChargeFixed").value!="")
   			 return false;

    		if(confirm(AlertMsg.deleteAlert)){
    		
    		 var table = document.getElementById("tblGrid");
    		 var rows = document.getElementById('tblGrid').getElementsByTagName('tbody')[0].getElementsByTagName('tr');
    		 for (var s=0; s < rows.length; s++) {
    		 rows[s].onclick = function() {
    		 var i=this.rowIndex;   
    		 if(minTxnValue[0] != "Min. Txn Value"){
    			 i=i-1;
    			 table.deleteRow(i+1);	
    		 }else{
                    table.deleteRow(i);	
                    }			
                    minTxnValue.splice(i,1);
                    maxTxnValue.splice(i,1);
                    scPercentage.splice(i,1);
                    sChargeFixed.splice(i,1);
                    discountLimit.splice(i,1);
                    minSCharge.splice(i,1);	
                    maxSCharge.splice(i,1);	
                    l--;
                }

            }
    		 clearTxnFields();
    		 if(sChargeFixed.length==1)
    	    		document.getElementById("transactions").disabled=false;
    		 }
    	}
    	
    /*	 bug fixed for service charge rule value while adding ou updating. By Ajinkya, date : 26 july 2016*/
    	
    	function txnEditDays(){		
   		 var rows = document.getElementById('tblGrid').getElementsByTagName('tbody')[0].getElementsByTagName('tr');
   		 document.getElementById('txnEditDiv').style.display = 'block';
   		 document.getElementById('txnAddDiv').style.display = 'none';  	 	
   		 
   		 for (var i = 1; i <= rows.length; i++) {
   		 rows[i].onclick = function() {
   			 var id=this.rowIndex;           	 
   			 document.getElementById("tindex").value=id;    		 
   			 document.getElementById("minTxn").value=minTxnValue[id];
   			 document.getElementById("maxTxn").value=maxTxnValue[id];
   			 document.getElementById("scPerc").value=scPercentage[id];
   			 document.getElementById("sChargeFixed").value=sChargeFixed[id];
   			 document.getElementById("dLimit").value=discountLimit[id];
   			 document.getElementById("minSCharge").value=minSCharge[id];
   			 document.getElementById("maxSCharge").value=maxSCharge[id];
          		}
   	 	}  
   	 	
   		}
    	
    	function updateTxnRows(){		
  		  var table = document.getElementById("tblGrid");
  		  var i = document.getElementById("tindex").value
  		  var a = document.getElementById("minTxn").value;
            var b = document.getElementById("maxTxn").value;
            var c = document.getElementById("scPerc").value;
            var d = document.getElementById("sChargeFixed").value;
            var e = document.getElementById("dLimit").value;
            var f = document.getElementById("minSCharge").value;
            
            var txnid=document.getElementById("transactions").value;   	
            var g;
            g = document.getElementById("maxSCharge").value; 
            
     		/*if(findType([30,35,10,15,20,98,70,75,37,40,39],txnid)==undefined){            
            g = document.getElementById("maxSCharge").value;  }else{
            g = document.getElementById("sChargeFixed").value;  	
            }*/
           
            if(txnValidate(a,b,c,d,e,f,g,i)){       
          	  table.deleteRow(i);
  		  		minTxnValue[i]=a;
  		  		maxTxnValue[i]=b;
  		  		scPercentage[i]=c;	
  		  		sChargeFixed[i]=d;
  		  		discountLimit[i]=e;
  		  		minSCharge[i]=f;	
  		  		maxSCharge[i]=g;    		  	
  		 
  		  addTxnRows(a,b,c,d,e,f,g,1,i);
  		  clearTxnFields();
  		  document.getElementById('txnEditDiv').style.display = 'none';
   		  document.getElementById('txnAddDiv').style.display = 'block';  	
            }
  		}

        
    	function clearTxnFields(){
    		
    		document.getElementById("minTxn").value="";
    		document.getElementById("maxTxn").value="";
    		document.getElementById("scPerc").value="";
    		document.getElementById("sChargeFixed").value="";
    		document.getElementById("dLimit").value="";
    		document.getElementById("minSCharge").value="";
    		document.getElementById("maxSCharge").value="";

  			}
  
  	/* End  -------------- */
  	
  	/* Applicable Days Dynamic Table data create/update/delete  -------------- */
  

	var day = new Array(),from=new Array(),to=new Array();
	var z=0;
	function addDays(id) {
        var combo=document.getElementById("day");        
        var dayid = combo.value;
        var a = combo.options[combo.selectedIndex].firstChild.nodeValue ;
        var b = document.getElementById("fhours").value;
        var c = document.getElementById("thours").value; 
        var editStatus=(id==undefined)?0:1;
        if(dayValidate(dayid,b,c,-1)){ 
        
        day[z]=dayid; from[z]=b; to[z]=c;z++;       
        
        addDayRow(a,b,c,editStatus,-1);

    	clearDayField();
        
        }
    	  
    }
	
	function dayValidate(a,b,c,rid){
		
			if(a==""||b==""||c==""){
			alert(AlertMsg.empty);
			return false;
	        } 
			if(isNaN(b)|| b.charAt(0) == " " || b.charAt(b.length-1) == " " || b.indexOf(".")!=-1 || b<0){
			alert(AlertMsg.fhours);
		    return false;
			}
			if(isNaN(c)||c.charAt(0) == " " || c.charAt(c.length-1) == " " || c.indexOf(".")!=-1 || c<0){
			alert(AlertMsg.thours);
			return false;
			}
	        if(!ValidateTime(b,0)){
	        alert(AlertMsg.fhours);
	        return false;
	        }
	        if(!ValidateTime(c,0)){
	        alert(AlertMsg.thours);
	        return false;
	        }
	      	if(parseInt(c)<=parseInt(b)){
		 	alert(AlertMsg.hours);
		 	return false;
	     	} 
	     	if(day.length!=0){
	                for(var k=0;k<day.length;k++){
	                   //alert("a,a :: "+a+minTxnValue[k]+"b,b:: "+b+maxTxnValue[k]);
	                	if(rid!=k){
	                    if(parseInt(a)==parseInt(day[k]) && (parseInt(b)>parseInt(from[k])&&parseInt(b)<parseInt(to[k]) || parseInt(c)>parseInt(from[k])&&parseInt(c)<parseInt(to[k]))){
	                	alert(AlertMsg.dayRange);
	                	clearDayField();
	    				return false;	    			  
	                    }
	                    if(parseInt(a)==parseInt(day[k]) && ((parseInt(b)==parseInt(from[k]))||(parseInt(c)==parseInt(to[k])))){
		                	alert(AlertMsg.dayRange);
		                	clearDayField();
		    				return false;	    			  
		                }
	                    if(parseInt(a)==parseInt(day[k]) && parseInt(b)==0){
	                    	if(parseInt(c)>parseInt(from[k])){
	                    		alert(AlertMsg.dayRange);
	                    		clearDayField();
			    				return false;	    
	                    	}
	                    }
	                    if((parseInt(a)==8 || parseInt(day[k])==8) && (parseInt(b)>parseInt(from[k])&&parseInt(b)<parseInt(to[k]) || parseInt(c)>parseInt(from[k])&&parseInt(c)<parseInt(to[k]))){
	                    	alert(AlertMsg.dayRange);
	                    	clearDayField();
		    				return false;	                    	
	                    }
	                    if((parseInt(a)==8 || parseInt(day[k])==8) && ((parseInt(b)==from[k])||(parseInt(c)==to[k]))){
		                	alert(AlertMsg.dayRange);
		                	clearDayField();
		    				return false;	    			  
		                }
	                    if((parseInt(a)==8 || parseInt(day[k])==8)&& parseInt(b)==0){
	                    	if(parseInt(c)>parseInt(from[k])){
	                    		alert(AlertMsg.dayRange);
	                    		clearDayField();
			    				return false;	    
	                    	}
	                    }

	                	}
	                }
	        } 
	     	
	     	return true;
	}
	
	
	function addDayRow(a, b, c,editStatus,rindex) {      
		  
		var table1=document.getElementById("tblGrid1");   		      
		var lastRow =(rindex==-1)?table1.rows.length:rindex;	     
        var row=table1.insertRow(lastRow);
        if(lastRow%2!=0)
        row.setAttribute("bgColor","#F9F9F9");  
             
        
        var td2=row.insertCell(0);
        td2.setAttribute("height", "25px");
        td2.setAttribute("width", "16%");
        td2.setAttribute("align","center");
        td2.innerHTML=a;        
         
        var td3=row.insertCell(1);
        td3.setAttribute("width", "35%");
        td3.setAttribute("align","center");
        td3.innerHTML= b;  
        
        var td4=row.insertCell(2);
        td4.setAttribute("width", "35%");
        td4.setAttribute("align","center");
        td4.innerHTML= c;  
        
        var td5 = row.insertCell(3);
        td5.setAttribute("width", "14%");
        td5.setAttribute("align","center");
        var p="<a onclick='deleteDayRow();' style='cursor:pointer'>"+AlertMsg.delet+"</a>";
        var q="<a onclick='dayEdit();' style='cursor:pointer'>"+AlertMsg.edit+"</a>";
        td5.innerHTML =(editStatus==1)?q+" | "+p:p; 
       
    }

	function dayEdit(){		
		 var rows = document.getElementById('tblGrid1').getElementsByTagName('tbody')[0].getElementsByTagName('tr');
		 for (var i = 0; i < rows.length; i++) {
		 rows[i].onclick = function() {
		 var id=this.rowIndex;
		 document.getElementById("dindex").value=id;
		 document.getElementById("day").selectedIndex = day[id];  		 
		 document.getElementById("fhours").value=from[id];
		 document.getElementById("thours").value=to[id];       

      		}
	 		}
		 document.getElementById('dayEditDiv').style.display = 'block';
  		 document.getElementById('dayAddDiv').style.display = 'none';  		
  		
		}
	
	function updateDayRow(){		
		  var table = document.getElementById("tblGrid1");
		  var i = document.getElementById("dindex").value;
		  var combo=document.getElementById("day");        
	      var dayid = combo.value;
	      var a = combo.options[combo.selectedIndex].firstChild.nodeValue ;
	      var b = document.getElementById("fhours").value;
	      var c = document.getElementById("thours").value;     
	      if(dayValidate(dayid,b,c,i)){ 
		  table.deleteRow(i);
					day[i]=dayid;
					from[i]=b;
					to[i]=c;					
		 
		  addDayRow(a,b,c,1,i);
		  clearDayField();
		  document.getElementById('dayEditDiv').style.display = 'none';
   		  document.getElementById('dayAddDiv').style.display = 'block';  	
	      }
		}
	
	function deleteDayRow() {
		if(document.getElementById("day").value!=""){
			 return false;}
		
		if(confirm(AlertMsg.deleteAlert)){
		 
		 var table = document.getElementById("tblGrid1");
		 var rows = document.getElementById('tblGrid1').getElementsByTagName('tbody')[0].getElementsByTagName('tr');
		 for (var s = 0; s < rows.length; s++) {
			 rows[s].onclick = function() {
				 var j=this.rowIndex;    
				 table.deleteRow(j);	
				 day.splice(j,1);
				 from.splice(j,1);
				 to.splice(j,1); 
				 z--;
              
            }

        }
		 clearDayField();
		}
	}

	
	function clearDayField(){
		
  		document.getElementById("day").value="";
  		document.getElementById("fhours").value="";
  		document.getElementById("thours").value=""; 	 	
	}    
	
	/* End  -------------- */
	
	/* writeSCRuleValues  -------------- */
	
		
	function writeSCRuleValues(){
		 if(minTxnValue.length==0)		
		 return false;
		 var i;
		 if(minTxnValue[0] == "Min. Txn Value"){
			 i=1;
		 }else{
			 i=0;
		 }
		 var t1="";var t2="";var t3=""; var t4="";var t5="";var t6="";var t7="";
	     for (i ; i <= minTxnValue.length; i++) {			 
			// alert("minTxn :: "+minTxnValue[i]+" max :: "+maxTxnValue[i]+" % :: "+scPercentage[i]+" fixed :: "+sChargeFixed[i]+" dlimit :: "+discountLimit[i]+" minsc :: "+minSCharge[i]+" maxsc :: "+maxSCharge[i]);
			 if(minTxnValue[i]!=undefined){
			 t1=(i!=(minTxnValue.length-1))?t1+minTxnValue[i]+",":t1+minTxnValue[i];
			 t2=(i!=(minTxnValue.length-1))?t2+maxTxnValue[i]+",":t2+maxTxnValue[i];
			 t3=(i!=(minTxnValue.length-1))?t3+scPercentage[i]+",":t3+scPercentage[i];
			 t4=(i!=(minTxnValue.length-1))?t4+sChargeFixed[i]+",":t4+sChargeFixed[i];
			 t5=(i!=(minTxnValue.length-1))?t5+discountLimit[i]+",":t5+discountLimit[i];
			 t6=(i!=(minTxnValue.length-1))?t6+minSCharge[i]+",":t6+minSCharge[i];	
			 t7=(i!=(minTxnValue.length-1))?t7+maxSCharge[i]+",":t7+maxSCharge[i];	
			 }
	      }
	     document.getElementById("minTxnValue").value=t1;
         document.getElementById("maxTxnValue").value=t2;
         document.getElementById("scPercentage").value=t3;
         document.getElementById("scFixed").value=t4;
         document.getElementById("discountLimit").value=t5;
         document.getElementById("minSC").value=t6;
         document.getElementById("maxSC").value=t7;		
		
	}
	
	/* End  -------------- */
	
	
	/* writeDayValues  -------------- */
	
	function writeDayValues(){
		 if(day.length==0)
		 return false;
		 var i;
		 if(day[0]!=undefined && day[0]!="")
			 i=0;
		 else
			 i=1;
		 var d1="";var d2="";var d3="";
	     for (i; i <= day.length+1; i++) {
			
			 if(day[i]!=undefined && day[i]!=""){
			 d1=(i!=(day.length-1))?d1+day[i]+",":d1+day[i];
			 d2=(i!=(day.length-1))?d2+from[i]+",":d2+from[i];
			 d3=(i!=(day.length-1))?d3+to[i]+",":d3+to[i];	
			 }
	      }
			document.getElementById('days').value=d1;
	        document.getElementById('fromHours').value=d2;
	        document.getElementById('toHours').value=d3;	
		
	}
	/* End  -------------- */
	
	/* Transaction list : selection  -------------- */
	
	
	function findType(arr, obj) {
	    for(var i=0; i<arr.length; i++) {
	        if (arr[i] == obj) return true;
	    }
	}
	
	function unSelectTxn(){
		var len=document.serviceChargeForm.transactions.length;
		for (var e = 0; e < len; e++) {
			document.serviceChargeForm.transactions[e].selected=false; }
	}

	function changeStatus(flag){
		  document.getElementById("minTxn").disabled=flag;
          document.getElementById("maxTxn").disabled=flag;
          document.getElementById("scPerc").disabled=flag;
          document.getElementById("sChargeFixed").disabled=flag;
          document.getElementById("dLimit").disabled=flag;
          document.getElementById("minSCharge").disabled=flag;
          document.getElementById("maxSCharge").disabled=flag;   
	}
	
	function viewHideSCTextField(){		
		var len=document.serviceChargeForm.transactions.length;
		var val="";var status=false;var f=0;var imposedStatus=false;var sourceTypeStatus=false;
		for (var e = 0; e < len; e++) {
			if (document.serviceChargeForm.transactions[e].selected) {	
				val=parseInt(document.serviceChargeForm.transactions[e].value);		
				if(f==0){
					if(findType([10,15,20,70,75,37,40,39],val)!=undefined){							
						status=1;
					}else if(findType([30,35,98],val)!=undefined){							
						status=2;
					}else{
						status=3;
					}
				}
				//alert("status : "+status);
				if(status==1){
					if(findType([10,15,20,70,75,37,40,39],val)==undefined){
						alert(AlertMsg.txnType);
						unSelectTxn();
						changeStatus(true);
						return false;
					}
				}else if(status==2){
					
					if(findType([30,35,98,82,95,90,83,80,55,99,84,100,115,116,126,128,137,138,140,141,143,144,146],val)==undefined){
						alert(AlertMsg.txnType);
						unSelectTxn();
						changeStatus(true);
						return false;
					}
			
				}
				else{
					if(findType([82,95,90,83,80,55,99,84,100,115,116,126,128,133,137,138,140,141,143,144,146],val)==undefined){
						alert(AlertMsg.txnType);
						unSelectTxn();
						changeStatus(true);
						return false;
					}
				}
				if(val=="80" || val=="90" || val=="128" || val=="133" || val=="146")
					imposedStatus=true;
				if(val=="30" || val=="35"|| val=="55"|| val=="80"||val=="82"||val=="83"|| val=="90" || val=="84"|| val=="95"|| val=="99")
					sourceTypeStatus=true;

				f++;
			}
		}
		if(status==3){
			changeStatus(false);
		}//else{
			//changeStatus(true);
		//	document.getElementById("sChargeFixed").disabled=false;
		//}
		imposedStatus==true?document.getElementById('divImpose').style.display = 'block': document.getElementById('divImpose').style.display = 'none'; 
		//sourceTypeStatus==true?document.getElementById('divImpose1').style.display = 'block': document.getElementById('divImpose1').style.display = 'none';

	}

	
	
	/* End  -------------- */
	
	
	function trimString (str) {
	    str = str.replace(/^\s+/, '');
	    for (var i = str.length - 1; i >= 0; i--) {
	        if (/\S/.test(str.charAt(i))) {
	            str = str.substring(0, i + 1);
	            break;
	        }
	    }
	    return str;
	}	
	
	
	function setSCRuleArraysByTableData(){	
		var table = document.getElementById("tblGrid");  
		var value;
		for(var i = 0; i < table.rows.length; i++){
	    for(var j=0; j<7; j++) {
	    value = table.rows[i].cells[j].innerHTML;	  
	    switch(j){
	            case 0:     
	            	minTxnValue[l]=trimString(value);
	            /*	minTxnValue.sort();*/
	            	break;
	            case 1:                   
	            	maxTxnValue[l]=trimString(value);break;	
	            case 2:     
	            	scPercentage[l]=trimString(value);break;
	            case 3:                   
	            	sChargeFixed[l]=trimString(value);break;	
	            case 4:     
	            	discountLimit[l]=trimString(value);break;
	            case 5:                   
	            	minSCharge[l]=trimString(value);break;
	            case 6:                   
	            	maxSCharge[l]=trimString(value);break;	
	            }
	        }
	        l++;	 
	    	}		 
		
		 if(sChargeFixed.length!=0)
	    	 document.getElementById("transactions").disabled=true;
		     viewHideSCTextField();
		     if( document.getElementById("minTxnValue")!=null)
		  	 document.getElementById("minTxnValue").value="";
		  	 
		  	if( document.getElementById("maxTxnValue")!=null)
	         document.getElementById("maxTxnValue").value="";
	         
	         if( document.getElementById("scPercentage")!=null)
	         document.getElementById("scPercentage").value="";
	         
	         if( document.getElementById("scFixed")!=null)
	         document.getElementById("scFixed").value="";
	         
	         if( document.getElementById("discountLimit")!=null)
	         document.getElementById("discountLimit").value="";
	         
	         if( document.getElementById("minSC")!=null)
	         document.getElementById("minSC").value="";
	         
	         if( document.getElementById("maxSC")!=null)
	         document.getElementById("maxSC").value="";	   
	   }
	
	function setDayArraysByTableData(){	
		var table = document.getElementById("tblGrid1");  
		var value;
		for(var i = 0; i < table.rows.length; i++){
	    for(var j=0; j<3; j++) {
	    value = table.rows[i].cells[j].innerHTML;
	   
	    switch(j){
	            case 0:     
	            	day[z]=trimString(value.substring(value.indexOf ('>')+1,value.lastIndexOf('<')));break;
	            case 1:                   
	            	from[z]=trimString(value);break;	
	            case 2:                   
	            	to[z]=trimString(value);break;	
	            }
	        }
	        z++;	 
	    }		    
		document.getElementById('days').value="";
        document.getElementById('fromHours').value="";
        document.getElementById('toHours').value="";	   
	}
	
	

	function ValidateTime(time, formatType) {
		  var segments;      // Break up of the time into hours and minutes
		  var hour;          // The value of the entered hour
		  var minute;        // The value of the entered minute
		    
		  //time = time.replace(".", ":");
		    
		  if (formatType == 1) {                                          /* Validating standard time */
		    segments = time.split(":");		    
		    if (segments.length == 2) {
		      segments[1] = segments[1].substring(0,2);
		      hour = segments[0];                                          // Test the hour
		      if ((hour > 12) || (hour < 1))  
		        return false;
		            
		      minute = segments[1];                                        // Test the minute
		      if (( minute <= 59) && (minute >= 0)) 
		        return true;
		    }		      
		  }
		  else { 
			  
			segments = time.split(".");
			    
		    if (segments.length == 2 || segments.length == 1) {
		      hour = segments[0];  
		      hour=parseInt(hour);  
		     
		      if(isNaN(hour))
			  	return false;
			  		                                     // Test the hour
		      if ((hour > 24) || (hour <= -1)) 
		      return false;
				      
		      if(segments.length ==2){  
		      	minute = segments[1];  
		      	if(isNaN(parseInt(minute)))
			      	return false;			      
		      	if (!(( minute <= 59) && (minute >= 0))) 
		        	return false;		        		    	
		      	if(minute.length==3)
			    	return false;	 
			                                       // Test the minute
		        }
		      return true;
		    }
		    
		  }
		  return false;
		}  	

	function compareDate(fromdt,todt)
	{
		
	//	alert(fromdt+"-"+todt);
		
	    var dt1  = parseInt(fromdt.substring(0,2),10);
	    var mon1 = parseInt(fromdt.substring(3,5),10)-1;
	    var yr1  = parseInt(fromdt.substring(6,10),10);
	    var dt2  = parseInt(todt.substring(0,2),10);
	    var mon2 = parseInt(todt.substring(3,5),10)-1;
	    var yr2  = parseInt(todt.substring(6,10),10);
	    var fromDate = new Date(yr1, mon1, dt1);
	    var toDate = new Date(yr2, mon2, dt2); 
	    
	//    alert(fromDate+"-"+toDate);
	    
		if( fromDate <= toDate )
			return true;
		else
			return false;
		
	}
	
	function getCurrentDate(){
		var date = new Date();
		var d  = date.getDate();
		var day = (d < 10) ? '0' + d : d;
		var m = date.getMonth() + 1;
		var month = (m < 10) ? '0' + m : m;
		var yy = date.getYear();
		var year = (yy < 1000) ? yy + 1900 : yy;
		var cdate=day + "-" + month + "-" + year;
     return cdate;
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
	/*function dtvalid(fromDate,toDate)  
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
	
	alert(AlertMsg.applicableFromDateFormat);  
	return false;  
	} 
	if (tcpos1==-1 || tcpos2==-1){
		alert(AlertMsg.applicableToDateFromat);  
		return false;  
	} 

	if(fdaystr.length>2){
		
		alert(AlertMsg.applicablevalidFromDay);
		return false;
	}
	if(tdaystr.length>2){
		
		alert(AlertMsg.applicablevalidToDay);
		return false;
	}
	if(fmonthstr.length>2){
		
		alert(AlertMsg.applicablevalidFromMonth);
		return false;
	}
	if(tmonthstr.length>2){
		
		alert(AlertMsg.applicablevalidToMonth);
		return false;
	}
	if (fdaystr.length<1 || fpday<1 || fpday>31 || (fpmonth==2 && fpday>februarycheck(fpyear))   || fpday > monthdays[fpmonth]){  
		
		alert(AlertMsg.applicablevalidfDay);  
		return false ; 
		} 
	if (tdaystr.length<1 || tpday<1 || tpday>31 || (tpmonth==2 && tpday>februarycheck(tpyear))   || tpday > monthdays[tpmonth]){  
		
		alert(AlertMsg.applicablevalidTodays);  
		return false ; 
		} 


	if (fmonthstr.length<1 || fpmonth<1 || fpmonth>12){  
		
	alert(AlertMsg.applicablevalidFMonth);  
	return false;  
	}

	if (tmonthstr.length<1 || tpmonth<1 || tpmonth>12){  
		
		alert(AlertMsg.applicablevalidToMonth);  
		return false;  
		}

	if (yearstr.length != 4 || pyear==0 || pyear<dminyear || pyear>dmaxyear){  
	alert("Input a valid 4 digit year between "+dminyear+" and "+dmaxyear);  
	return false;  
	}  
	if (fromDate.indexOf(chsep,fcpos2+1)!=-1 || checkinteger(getcharacters(fromDate, chsep))==false){  
		
	alert(AlertMsg.applicablevalidFromDay) ; 
	return false;  
	}
	if (toDate.indexOf(chsep,tcpos2+1)!=-1 || checkinteger(getcharacters(toDate, chsep))==false){
		
		alert(AlertMsg.applicablevalidToDay) ; 
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
	*/
	
	
	
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
	//var DateOfBirth=new String("12/912/2012");
		var monthdays = finaldays(12);  
	/*var fChar1 = fromDate.charAt(2);
	var fChar2 = fromDate.charAt(5);*/
	var chsep= "/" ;
	var fcpos1=fromDate.indexOf(chsep); 
	var fcpos2=fromDate.indexOf(chsep,fcpos1+1);  
	
	if (fcpos1==-1 || fcpos2==-1 ){  
		
	alert(AlertMsg.applicableFromDateFormat);  
	return false;  
	} 

	if (fromDate.length<10 || fromDate.length>10){  

		alert(AlertMsg.applicableFromDateFormat);  
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
	
	/*var tChar1 = toDate.charAt(2);
	var tChar2 = toDate.charAt(5);*/
	
	var tcpos1=toDate.indexOf(chsep); 
	var tcpos2=toDate.indexOf(chsep,tcpos1+1);  
	

	if (fromDate.length<10 || fromDate.length>10){  

		alert(AlertMsg.applicableFromDateFormat);  
		return false;  
	} 
	/*if ( Char1 =='/' && Char2 == '/' )
	{
	 alert ('Please check foramt');
	return false;
	}*/

	var tday;
	var tmonth;
	var tyear;

	tday = toDate.substring(0,2);
	tmonth = toDate.substring(3,5);
	tyear = toDate.substring(6,10);


	if(!validDay(day))
	{
	alert(AlertMsg.applicablevalidFromDay);

	return false;
	}

	if(!validMonth(month)){	
	alert(AlertMsg.applicablevalidFromMonth);

	return false;
	}

	if ((month==2 && day>februarycheck(year))   || day > monthdays[month]){  
		
		alert(AlertMsg.applicablevalidFromDay);  
		return false ; 
		} 
if (tcpos1==-1 || tcpos2==-1 ){  
		
		alert(AlertMsg.applicableToDateFromat);  
		return false;  
		} 
	if (toDate.length<10 || toDate.length>10){  

		alert(AlertMsg.applicableToDateFromat);  
		return false;  
	} 
	
	if(!validDay(tday))
	{
	alert(AlertMsg.applicablevalidToDay);

	return false;
	}

	if(!validMonth(tmonth)){	
	alert(AlertMsg.applicablevalidToMonth);

	return false;
	}
	 
	if ((tmonth==2 && tday>februarycheck(tyear))   || tday > monthdays[tmonth]){  
		
		alert(AlertMsg.applicablevalidToDay);  
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
	