function txnEdit(objRef){	
	if($("#subscriptionId").val() != ""){
		alert("Complete current action");
		return false;
	}

	var id = $(objRef).parent().parent().attr("id").replace("smsTxnDetails_", "");
	
	$("#subscriptionId").val($("#smsTxnDetails_"+id+" td.subscriptionType div").html().trim());    		 
	$("#costPerPackage").val($("#smsTxnDetails_"+id+" td.costPerPackage").html().trim());  
	$("#noOfTxn").val($("#smsTxnDetails_"+id+" td.noOfTxn").html().trim());  
	
	$("#subsTableGrid tr#smsTxnDetails_"+id).remove();	
	$("#subsTableGrid tr[id^=smsTxnDetails_]").each(function(i){
		$(this).attr("id", "smsTxnDetails_"+i)
	});
	
	$("#txnEditDiv").show();
	$("#txnAddDiv").hide(); 
}

function addTxnDetailss() {
	var e = $("#subscriptionId").val();	
	if(e==""){
		alert(AlertMsg.subscriptionTypeSelect);
		return false;
	}

	var f = $("#costPerPackage").val() ;
	var g = $("#noOfTxn").val() ;

	if(f==""){
		alert(AlertMsg.costPerTransaction);
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
		alert(AlertMsg.numberOfTransaction);
		return false;
	}
	if(f==0){
		alert(AlertMsg.numberOfTransaction);
		return false;
	}
	if(!g.match(/^[1-9][0-9]*$/)){
	    alert(AlertMsg.numberOfTransaction);
	    return false;
	}
	 
	if(validateSubType(e)) {
		addTxnRowss(e,f,g);
		clearTxnField();
	 }
}

function addTxnRowss(e, f, g) {
	var curRowRef = $("#subsTableGrid tr").length;
	
	var subRow = 
			"<tr id='smsTxnDetails_"+curRowRef+"'>" +
				"<td class='subscriptionType' width='25%' align='center' height='25px'>"+$("#subscriptionId option[value="+e+"]").text()+
					"<div style='display: none; '>"+e+"</div>" +
				"</td>" +
				"<td class='costPerPackage' width='35%' align='center'>"+f+"</td>" +
				"<td class='noOfTxn' width='25%' align='center'>"+g+"</td>" +
				"<td width='25%' align='center'>" +
					"<a onclick=\"txnEdit(this);\" style='cursor:pointer'>"+AlertMsg.edit+"</a>"+
					"|"+
					"<a onclick=\"deleteTxnRow(this);\" style='cursor:pointer'> "+AlertMsg.delet+"</a>"+
				"</td>" +
			"</tr>";
	console.log(subRow);
	$("#subsTableGrid").append(subRow);
}

function deleteTxnRow(objRef) {
	if(document.getElementById("subscriptionId").value!=""){
		alert("Complete current action");
		return false;
	} 
	
	if(confirm(AlertMsg.deleteAlert)){	
		var id = $(objRef).parent().parent().attr("id").replace("smsTxnDetails_", "");
		
		$("#subsTableGrid tr#smsTxnDetails_"+id).remove();
		$("#subsTableGrid tr[id^=smsTxnDetails_]").each(function(i){
			$(this).attr("id", "smsTxnDetails_"+i)
		});
	}
}

function clearTxnField(){	
	$("#costPerPackage").val("");
	$("#noOfTxn").val("");           
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
	$("#subsTableGrid tr td.subscriptionType div").each(function(){
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
	
	$("#subsTableGrid tr[id^=smsTxnDetails_]").each(function(){
		t5.push($(this).find("td.subscriptionType div").html());
		t6.push($(this).find("td.costPerPackage").html());
		t7.push($(this).find("td.noOfTxn").html());
	});
	 
	
	$("#subscription").val(t5.join(","));
	$("#costPerPackage").val(t6.join(","));
	$("#noOfTxn").val(t7.join(","));
	
	$('#txnAddDiv').hide(); 
	$('#subscriptionId').hide(); 
	$('#costPerPackage').hide(); 
	$('#noOfTxn').hide(); 
}