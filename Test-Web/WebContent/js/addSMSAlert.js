function txnEdit(objRef){	
	if($("#subscriptionId").val() != ""){
		alert("Complete current action");
		return false;
	}

	var id = $(objRef).parent().parent().attr("id").replace("smsTxnDetails_", "");
	
	$("#subscriptionId").val($("#smsTxnDetails_"+id+" td.subscriptionType div").html().trim());    		 
	$("#costPerPackage").val($("#smsTxnDetails_"+id+" td.costPerPackage").html().trim());  
	$("#numberOfSMS").val($("#smsTxnDetails_"+id+" td.numberOfSMS").html().trim());  
	
	$("#tblGrid tr#smsTxnDetails_"+id).remove();	
	$("#tblGrid tr[id^=smsTxnDetails_]").each(function(i){
		$(this).attr("id", "smsTxnDetails_"+i)
	});
	
	$("#txnEditDiv").show();
	$("#txnAddDiv").hide(); 
}

function addTxnDetails() {
	var e = $("#subscriptionId").val();	
	if(e==""){
		alert(AlertMsg.subscriptionTypeSelect);
		return false;
	}

	var f = $("#costPerPackage").val() ;
	var g = $("#numberOfSMS").val() ;

	if(f==""){
		alert(AlertMsg.costPerPackage);
		return false;
	}
	if(f==0){
		alert(AlertMsg.costPerPackageValid);
		return false;
	}
	if(!f.match(/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/)) {
		alert(AlertMsg.costPerPackageValid);
		return false;
	}
	if(!f.match(/^\d{0,5}(\.\d{1,2})?$/gm)){
		alert(AlertMsg.costPerPackageValid);
		return false;
	}
	if(g==""){
		alert(AlertMsg.numberOfSMS);
		return false;
	}
	if(f==0){
		alert(AlertMsg.numberOfSMS);
		return false;
	}
	if(!g.match(/^[1-9][0-9]*$/)){
	    alert(AlertMsg.numberOfSMS);
	    return false;
	}
	 
	if(validateSubType(e)) {
		addTxnRow(e,f,g);
		clearTxnField();
	 }
}

function addTxnRow(e, f, g) {
	var curRowRef = $("#tblGrid tr").length;
	
	var newRow = 
			"<tr id='smsTxnDetails_"+curRowRef+"'>" +
				"<td class='subscriptionType' width='25%' align='center' height='25px'>"+$("#subscriptionId option[value="+e+"]").text()+
					"<div style='display: none; '>"+e+"</div>" +
				"</td>" +
				"<td class='costPerPackage' width='35%' align='center'>"+f+"</td>" +
				"<td class='numberOfSMS' width='25%' align='center'>"+g+"</td>" +
				"<td width='25%' align='center'>" +
					"<a onclick=\"txnEdit(this);\" style='cursor:pointer'>"+AlertMsg.edit+"</a>"+
					"|"+
					"<a onclick=\"deleteTxnRow(this);\" style='cursor:pointer'> "+AlertMsg.delet+"</a>"+
				"</td>" +
			"</tr>";
	$("#tblGrid").append(newRow);
}

function deleteTxnRow(objRef) {
	if(document.getElementById("subscriptionId").value!=""){
		alert("Complete current action");
		return false;
	} 
	
	if(confirm(AlertMsg.deleteAlert)){	
		var id = $(objRef).parent().parent().attr("id").replace("smsTxnDetails_", "");
		
		$("#tblGrid tr#smsTxnDetails_"+id).remove();
		$("#tblGrid tr[id^=smsTxnDetails_]").each(function(i){
			$(this).attr("id", "smsTxnDetails_"+i)
		});
	}
}

function clearTxnField(){	
	$("#costPerPackage").val("");
	$("#numberOfSMS").val("");           
	$("#subscriptionId").val("");  
}

function updateTxnRow(){
	addTxnDetails();
	if($("#subscriptionId").val() == ""){
		document.getElementById('txnEditDiv').style.display = 'none';
		document.getElementById('txnAddDiv').style.display = 'block';
	}
}
 
function validateSubType(e) {	
	var retFlag = true;
	$("#tblGrid tr td.subscriptionType div").each(function(){
		if($(this).html() == e) {
			alert(AlertMsg.differentSubscriptionType);
			retFlag = false;
		}
	});
	return retFlag;
}

function writeSMSRuleValues(){
	
	if($("#subscriptionId").val() != ""){
		alert("Complete current action");
		return false;
	}
	
	var t5=[];var t6=[];var t7=[];
	
	$("#tblGrid tr[id^=smsTxnDetails_]").each(function(){
		t5.push($(this).find("td.subscriptionType div").html());
		t6.push($(this).find("td.costPerPackage").html());
		t7.push($(this).find("td.numberOfSMS").html());
	});
	
	$("#subscription").val(t5.join(","));
	$("#costPerPackage").val(t6.join(","));
	$("#numberOfSMS").val(t7.join(","));
	
	$('#txnAddDiv').hide(); 
	$('#subscriptionId').hide(); 
	$('#costPerPackage').hide(); 
	$('#numberOfSMS').hide(); 
}