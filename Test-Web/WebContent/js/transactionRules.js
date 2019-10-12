

/* Transaction Rule Dynamic Table data create/update/delete  -------------- */

var cvlimit=new Array(),ntimes=new Array(),allper=new Array(),allperunit=new Array();

var l=0;
function addTxnDetails(id) {
	var maxVal=document.getElementById("maxValueLimit").value;
	if(maxVal==""){
		alert(AlertMsg.maxVal);
		return false;
	}
		
	var e = document.getElementById("cvlimit").value;
	var f = document.getElementById("ntimes").value;
	var g = document.getElementById("allper").value;       
	var combo = document.getElementById("allperunit") ;
	var allperunitVal = combo.options[combo.selectedIndex].firstChild.nodeValue ;
	var h = combo.value;      
	var editStatus=(id==undefined)?0:1;
	
	if(txnValidate(e,f,g,h,-1,maxVal)){            

		cvlimit[l]=e; ntimes[l]=f; allper[l]=g;allperunit[l]=h; l++; 

		addTxnRow(e,f,g,allperunitVal,editStatus,-1);

		clearTxnField();

	}
	cvlimit.length!=0?document.getElementById('maxValueLimit').disabled=true:document.getElementById('maxValueLimit').disabled=false;

}
//@start Vinod joshi  Date:16/10/2018 purpose:cross site Scripting -->
function viewTxRules(url,PageNumber){
	document.getElementById('pageNumber').value=PageNumber;
	submitlink(url,'transactionRuleForm');
}

function txnValidate(e,f,g,h,rid,maxVal){

	if(e==""||f==""||g=="" || h=="" ){
		alert(AlertMsg.empty);
		return false;
	}	
	if(isNaN(e)|| e.charAt(0) == " " || e.charAt(e.length-1) == " "|| e.indexOf(".")!=-1 || e<=0){
		alert(AlertMsg.maxCum);
		return false;
	}
	if(isNaN(maxVal) || maxVal.charAt(0) == " " || maxVal.charAt(maxVal.length-1) == " " || maxVal.indexOf(".")!=-1 || maxVal<=0){
		alert(AlertMsg.wrongMaxVal);
		return false;
	}
	if(parseInt(e)<parseInt(maxVal)){
		alert(AlertMsg.maxCumVal);
		return false;
	}
	if(isNaN(f)|| f.charAt(0) == " " || f.charAt(f.length-1) == " " || f.indexOf(".")!=-1 || f<=0){
		alert(AlertMsg.maxNum);
		return false;
	}
	if(isNaN(g)|| g.charAt(0) == " " || g.charAt(g.length-1) == " " || g.indexOf(".")!=-1 || g<=0){
		alert(AlertMsg.allowedPer);
		return false;
	} 
	if(h=="0"){
		alert(AlertMsg.allPerUnit);
		return false;
	}  
	if(allperunit.length!=0){
  	  for(var k=0;k<allperunit.length;k++){
            if(rid!=k){
               if(h==allperunit[k]){
            	   alert(AlertMsg.trRuleExist);
            	   return false;			 
               	}
            }
            }                     
      }            
	
	if(allperunit.length!=0){
	  	  for(var k=0;k<allperunit.length;k++){
	            if(rid!=k){
	               
	               //-------------
	               if(h==1){
	            	   
	            	   if(allperunit[k]==2){

	        			   if(parseInt(e)>=parseInt(cvlimit[k])){
	        				   alert(AlertMsg.cumValexceedweek);
	        				   return false;
	        			   }
	        			   if(parseInt(f)>=parseInt(ntimes[k])){
	        				   alert(AlertMsg.nooftimeexceedweek);
	        				   return false;
	        			   }

	            	   }
	            	   if(allperunit[k]==3){
	            		   
	            		   if(parseInt(e)>=parseInt(cvlimit[k])){
	        				   alert(AlertMsg.cumValexceedmonth);
	        				   return false;
	        			   }
	        			   if(parseInt(f)>=parseInt(ntimes[k])){
	        				   alert(AlertMsg.nooftimeexceedmonth);
	        				   return false;
	        			   }

	            		   
	            	   }
	            	   
	               }else if(h==2){
	            	   
	            	   if(allperunit[k]==1){

	        			   if(parseInt(e)<=parseInt(cvlimit[k])){
	        				   alert(AlertMsg.cumValgreaterday);
	        				   return false;
	        			   }
	        			   if(parseInt(f)<=parseInt(ntimes[k])){
	        				   alert(AlertMsg.nooftimegreaterday);
	        				   return false;
	        			   }

	            	   }
	            	   if(allperunit[k]==3){
	            		   
	            		   if(parseInt(e)>=parseInt(cvlimit[k])){
	            			   alert(AlertMsg.cumValexceedmonth);
	        				   return false;
	        			   }
	        			   if(parseInt(f)>=parseInt(ntimes[k])){
	        				   alert(AlertMsg.nooftimeexceedmonth);
	        				   return false;
	        			   }
	            	   }
	            	   
	               }else{
	            	   
	            	   if(allperunit[k]==1){

	        			   if(parseInt(e)<=parseInt(cvlimit[k])){
	        				   alert(AlertMsg.cumValgreaterday);
	        				   return false;
	        			   }
	        			   if(parseInt(f)<=parseInt(ntimes[k])){
	        				   alert(AlertMsg.nooftimegreaterday);
	        				   return false;
	        			   }

	            	   }
	            	   if(allperunit[k]==2){
	            		   
	            		   if(parseInt(e)<=parseInt(cvlimit[k])){
	        				   alert(AlertMsg.cumValgreaterweek);
	        				   return false;
	        			   }
	        			   if(parseInt(f)<=parseInt(ntimes[k])){
	        				   alert(AlertMsg.nooftimegreaterweek);
	        				   return false;
	        			   }

	            		   
	            	   }
	            	   
	               }
	               //-------------
	            
	            }
	            }                     
	      }            

	
	
	return true;
}

function addTxnRow(e, f, g,h,editStatus,rindex) { 
	//alert("edit statusl :: "+editStatus);

	var table=document.getElementById("tblGrid");       
	var lastRow =(rindex==-1)?table.rows.length:rindex;	         

	var row=table.insertRow(lastRow);	
	if(lastRow%2!=0)
		row.setAttribute("bgColor","#d2d3f1");            

	var td6 = row.insertCell(0);
	td6.setAttribute("width", "10%");
	td6.setAttribute("align","center");
	td6.setAttribute("height","25px");
	td6.innerHTML = e;

	var td7 = row.insertCell(1);
	td7.setAttribute("width", "15%");
	td7.setAttribute("align","center");
	td7.innerHTML = f;

	var td8 = row.insertCell(2);
	td8.setAttribute("width", "10%");
	td8.setAttribute("align","center");
	td8.innerHTML = g; 

	var td9 = row.insertCell(3);
	td9.setAttribute("width", "10%");
	td9.setAttribute("align","center");
	td9.innerHTML = h;  

	var td10 = row.insertCell(4);
	td10.setAttribute("width", "10%");
	td10.setAttribute("align","center");
	var p="<a onclick='txnEdit();' style='cursor:pointer'>"+AlertMsg.edit+"</a>";
	var q="<a onclick='deleteTxnRow();' style='cursor:pointer'>"+AlertMsg.delet+"</a>";	
	td10.innerHTML =(editStatus==1)?p+" | "+q:q; 

	}

	function txnEdit(){		
		 var rows = document.getElementById('tblGrid').getElementsByTagName('tbody')[0].getElementsByTagName('tr');
		 for (var i = 0; i < rows.length; i++) {
		 rows[i].onclick = function() {
			 var id=this.rowIndex;           	 
			 document.getElementById("tindex").value=id;    		 
			 document.getElementById("cvlimit").value=cvlimit[id];
			 document.getElementById("ntimes").value=ntimes[id];
			 document.getElementById("allper").value=allper[id];       
			 document.getElementById("allperunit").selectedIndex=allperunit[id];
     		}
	 	}  
		 document.getElementById('txnEditDiv').style.display = 'block';
		 document.getElementById('txnAddDiv').style.display = 'none'; 
		 //document.getElementbyId('tblGrid').disabled = true;
		 cvlimit.length!=1?document.getElementById('maxValueLimit').disabled=true:document.getElementById('maxValueLimit').disabled=false;
		 disableTable();
	 	
		}
	function disableTable() {
		var rows = document.getElementById('tblGrid').getElementsByTagName('tbody')[0].getElementsByTagName('tr');
		   for (var i = 0; i < rows.length; i++) {
		     rows[i].onClick= null;
		   } 
		  }

	
	function updateTxnRow(){
		var maxVal=document.getElementById("maxValueLimit").value;
		if(maxVal==""){
			alert(AlertMsg.maxVal);
			return false;
		}
		  var table = document.getElementById("tblGrid");
		  var i = document.getElementById("tindex").value;
		  var e = document.getElementById("cvlimit").value;
		  var f = document.getElementById("ntimes").value;
		  var g = document.getElementById("allper").value;       
		  var combo = document.getElementById("allperunit") ;
		  var allperunitVal = combo.options[combo.selectedIndex].firstChild.nodeValue ;
		  var h = combo.value;
		  
       if(txnValidate(e,f,g,h,i,maxVal)){       
     	  table.deleteRow(i);
     	  cvlimit[i]=e;
     	  ntimes[i]=f; 
     	  allper[i]=g;
     	  allperunit[i]=h;	
		 
		  addTxnRow(e,f,g,allperunitVal,1,i);
		  clearTxnField();
		  document.getElementById('txnEditDiv').style.display = 'none';
		  document.getElementById('txnAddDiv').style.display = 'block';  	
       }
		}


function deleteTxnRow() {
	if(document.getElementById("cvlimit").value!=""){
		return false;
	}
	if(confirm(AlertMsg.deleteAlert)){	
	var table = document.getElementById("tblGrid");
	var rows = document.getElementById('tblGrid').getElementsByTagName('tbody')[0].getElementsByTagName('tr');
	for (var s = 0; s < rows.length; s++) {
		rows[s].onclick = function() {
			var i=this.rowIndex;    
			table.deleteRow(i);	
			cvlimit.splice(i,1);
			ntimes.splice(i,1);	
			allper.splice(i,1);	   
			allperunit.splice(i,1);	   
			l--;
		}
	}  	
	clearTxnField();
	cvlimit.length!=1?document.getElementById('maxValueLimit').disabled=true:document.getElementById('maxValueLimit').disabled=false;
	}
}

function clearTxnField(){	
	document.getElementById("cvlimit").value="";
	document.getElementById("ntimes").value="";
	document.getElementById("allper").value="";           
	document.getElementById("allperunit").selectedIndex=0;  
}

/* End  -------------- */


/* writeTxnRuleValues  -------------- */


function writeTxnRuleValues(){

	if(cvlimit.length==0)		
		return false;
	var t1="";var t2="";var t3=""; var t4="";var t5="";var t6="";var t7="";var t8="";
	for (var i = 0; i < cvlimit.length; i++) {			 
		//alert(" cvlimit :: "+cvlimit[i]+" ntimes :: "+ntimes[i]+" allper :: "+allper[i] +"allperunit :: "+allperunit[i]);
		if(cvlimit[i]!=undefined){			
			t5=(i!=(cvlimit.length-1))?t5+cvlimit[i]+",":t5+cvlimit[i];
			t6=(i!=(cvlimit.length-1))?t6+ntimes[i]+",":t6+ntimes[i];	
			t7=(i!=(cvlimit.length-1))?t7+allper[i]+",":t7+allper[i];	
			t8=(i!=(cvlimit.length-1))?t8+allperunit[i]+",":t8+allperunit[i];
		}
	}	
	document.getElementById("maxCumValueLimit").value=t5;
	document.getElementById("maxNumTimes").value=t6;
	document.getElementById("allowedPer").value=t7;
	document.getElementById("allowedPerUnit").value=t8;

}

/* End  -------------- */


function setTRRuleArraysByTableData(){	
	var table = document.getElementById("tblGrid");  
	var value;
	for(var i = 0; i < table.rows.length; i++){
    for(var j=0; j<4; j++) {
    value = table.rows[i].cells[j].innerHTML;
   
    switch(j){
            case 0:     
            	cvlimit[l]=value;
            	//alert("cvlimit :: "+cvlimit[l]);
            	break;            	
            case 1:                   
            	ntimes[l]=value;
            	//alert("ntimes :: "+ntimes[l]);
            	break;	
            case 2:                   
            	allper[l]=value;
            	//alert("allper :: "+allper[l]);
            	break;	
            	
            case 3:            	
            	allperunit[l]=value.substring(value.indexOf ('>')+1,value.lastIndexOf('<'));
            	//alert("allperunit :: "+allperunit[l]);
            	break;
            }
        }
        l++;	 
    }	
	cvlimit.length!=0?document.getElementById('maxValueLimit').disabled=true:document.getElementById('maxValueLimit').disabled=false;
   
}