function viewTransactions(customerId, pageNumber) {

	//$csrfToken = $("#csrfToken").val();
	$.ajax({
		type : "POST",
		url : "viewTransactions.htm",
		data : "customerId=" + customerId + "&pageNumber=" + pageNumber,
		//csrfToken : $csrfToken,
		success : function(msg) {
			window.scrollTo(0, document.body.scrollHeight);
			$("#data").html("");
			$("#data").html(msg);
			//setTokenValFrmAjaxResp();
		}
	});
}

function viewRequests(customerId, pageNumber) {
	$.ajax({
		type : "POST",
		url : "viewRequests.htm",
		data : "customerId=" + customerId + "&pageNumber=" + pageNumber,
		success : function(msg) {
			document.getElementById("data").innerHTML = "";
			document.getElementById("data").innerHTML = msg;
		}
	});
}

function viewSMS(mobileNumber, pageNumber) {
	$.ajax({
		type : "POST",
		url : "viewSMSLog.htm",
		data : "mobno=" + mobileNumber + "&pageNumber=" + pageNumber,
		success : function(msg) {
			document.getElementById("data").innerHTML = "";
			document.getElementById("data").innerHTML = msg;
		}
	});
}

function changeSmsLogStatus(msgId, mobileNumber) {
	if (confirm(Alertmsg.ALERT_RESCHEDULE_SMS)) {
		$csrfToken = $("#csrfToken").val();
		$.ajax({
			type : "POST",
			url : "changeSmsLogStatus.htm",
			//data: "msgId=" + msgId + "&mobno=" + mobileNumber,
			data : {
				msgId : msgId,
				mobno : mobileNumber,
				csrfToken : $csrfToken
			},
			success : function(data) {

				document.getElementById("data").innerHTML = "";
				document.getElementById("data").innerHTML = data;
				//Sending the updated csrf token
				var newTokenVal = document.getElementById('csrfToken2').value;
				if (newTokenVal != null) {
					$('input[name=csrfToken]').val(newTokenVal);
				}

			}
		});

		/* var newToken=document.getElementById('csrfToken2');
		if(newToken!=null){
			$('input[name=csrfToken]').val(newToken);
		}*/
	}
}

function updateCSRFToken(tokenData) {
	$('input[name=csrfToken]').val($($(tokenData)[0]).find("input").val());
}

function reinitiateRequest(requestId) {
	//alert("requestId - " + requestId );
	if (confirm(Alertmsg.ALERT_REINIT_REQUEST)) {
		window.open("reinitiateRequest.htm?requestId=" + requestId, "mywindow",
				"menubar=0,toolbar=0,resizable=1,width=350,height=350");
	}
}

function openNewWindow(url) {
	window.open(url, "mywindow",
			"menubar=0,toolbar=0,resizable=1,width=450,height=450");
}

/* @Added by vinod joshi purpuse:- Cross site Scripting  */
function changeAgentStatus(custId, currentStatus, app) {
	var msg = "";

	if (currentStatus != 40) {
		msg = Alertmsg.ALERT_ACTIVATE_CUSTOMER;
	} else {
		msg = Alertmsg.ALERT_DEACTIVATE_CUSTOMER;
	}
	if (confirm(msg, custId, currentStatus, appStatus)) {
		var customerId = document.getElementById('customerId').value = custId;
		var appStatus = document.getElementById('appStatus').value = currentStatus;
		var url = "changeAgentStatus.htm";
		submitlink(url, "viewAgentForm");

	}

}

/* @Added by vinod joshi purpuse:- Cross site Scripting  */
function changeAgentApplicationStatus(custId, currentStatus, app) {
	var msg = "";

	if (currentStatus != 80) {
		msg = Alertmsg.ALERT_UNBLOCK_APP;
	} else {
		msg = Alertmsg.ALERT_BLOCK_APP;
	}
	if (confirm(msg, custId, currentStatus, appStatus)) {
		var customerId = document.getElementById('customerId').value = custId;
		var appStatus = document.getElementById('appStatus').value = currentStatus;
		var url = "changeAgentApplicationStatus.htm";
		submitlink(url, "viewAgentForm");

	}

}

/* @Added by vinod joshi purpuse:- Cross site Scripting  */
function changeApplicationStatus(custId, currentStatus, app) {
	var msg = "";

	if (currentStatus != 80) {
		msg = Alertmsg.ALERT_UNBLOCK_APP;
	} else {
		msg = Alertmsg.ALERT_BLOCK_APP;
	}
	if (confirm(msg, custId, currentStatus, appStatus)) {
		var customerId = document.getElementById('customerId').value = custId;
		var appStatus = document.getElementById('appStatus').value = currentStatus;
		var url = "changeApplicationStatus.htm";
		submitlink(url, "viewCustomerForm");

	}

	/*if(confirm(msg)){
		$.post("changeApplicationStatus.htm", {
			customerId : customerId,
			status : currentStatus
		}, function(data) {
			//alert(data);
			document.getElementById("data").innerHTML="";
			document.getElementById("data").innerHTML = data;
		});
	}*/

}

function changeCustomerStatus(custId, currentStatus, app) {
	var msg = "";
	$csrfToken = $("#csrfToken").val();
	if (currentStatus != 40) {
		msg = Alertmsg.ALERT_ACTIVATE_CUSTOMER;
	} else {
		msg = Alertmsg.ALERT_DEACTIVATE_CUSTOMER;
	}
	if (confirm(msg, custId, currentStatus, appStatus)) {
		var customerId = document.getElementById('customerId').value = custId;
		var appStatus = document.getElementById('appStatus').value = currentStatus;
		var url = "changeCustomerStatus.htm";
		var newTokenVal = document.getElementById('csrfToken2').value;
		if (newTokenVal != null) {
			$('input[name=csrfToken]').val(newTokenVal);
		}
		submitlink(url, "viewCustomerForm");

	}
}

/*function changeMerchantStatus(custId,currentStatus,app)
 {
 var msg = "" ;
 $csrfToken = $("#csrfToken").val();
 if(currentStatus!=40){
 msg = Alertmsg.ALERT_ACTIVATE_CUSTOMER ;
 } else {
 msg = Alertmsg.ALERT_DEACTIVATE_CUSTOMER ;
 }
 if(confirm(msg,custId,currentStatus,appStatus)){
 var customerId=document.getElementById('customerId').value=custId;
 var appStatus = document.getElementById('appStatus').value=currentStatus;
 var url = "changeMerchantStatus.htm";
 var newTokenVal=document.getElementById('csrfToken2').value;
 if (newTokenVal!=null) {
 $('input[name=csrfToken]').val(newTokenVal);
 }
 submitlink(url,"viewAgentForm");

 }
 }*/

function resetPIN(customerId) {
	if (confirm(Alertmsg.ALERT_RESET_CUST_PIN)) {
		//alert("#reset_pin");
		$.post("resetLoginPin.htm", {
			customerId : customerId
		}, function(data) {
			//alert(data);
			document.getElementById("data").innerHTML = "";
			document.getElementById("data").innerHTML = data;
		});
	}
}

function resetTxnPIN(customerId) {
	if (confirm(Alertmsg.ALERT_RESET_CUST_TXN_PIN)) {
		//alert("#reset_pin");
		$.post("resetTxnPin.htm", {
			customerId : customerId
		}, function(data) {
			//alert(data);
			document.getElementById("data").innerHTML = "";
			document.getElementById("data").innerHTML = data;
		});
	}
}

function reinstallApp(customerId) {
	if (confirm(Alertmsg.ALERT_REINSTALL_APP)) {
		//alert("#reset_pin");
		$.post("reinstallApplication.htm", {
			customerId : customerId
		}, function(data) {
			//alert(data);
			document.getElementById("data").innerHTML = "";
			document.getElementById("data").innerHTML = data;
		});
	}
}
function smsSubscription(customerId) {
	$.post("suscribedSMS.htm", {
		customerId : customerId
	}, function(data) {
		document.getElementById("data").innerHTML = "";
		document.getElementById("data").innerHTML = data;
	});
}
function smsPackageDetails(packageId, customerId) {
	$('li[class^=pkg]').removeClass("active");
	//$('li[class^=pkg]').css({'background-color' : '#d2d3f1'});

	$.post("smsPackageDetails.htm", {
		packageId : packageId,
		customerId : customerId
	}, function(data) {
		document.getElementById("descData").innerHTML = "";
		document.getElementById("descData").innerHTML = data;
		$('li.pkg' + packageId).addClass("active");
	});
}
function subscribePackage(packageId, subscriptionType, noOfSms, customerId) {
	//var customerId = getParameter('customerId');
	$.post("subscribeSMSPackage.htm", {
		customerId : customerId,
		packageId : packageId,
		subscriptionType : subscriptionType,
		noOfSms : noOfSms
	}, function(data) {
		document.getElementById("descData").innerHTML = "";
		document.getElementById("descData").innerHTML = data;
	});
}
$(document).ready(function() {
	var selectIndex = new Array('1', '2', '3', '4');
	$('#data').delegate('input[name=selectRdoPackage]', 'change', function(e) {
		var sel = $('input[name=selectRdoPackage]:checked').val();
		for (var i = 0; i < selectIndex.length; i++) {
			if (selectIndex[i] != sel) {
				$('.button_' + selectIndex[i]).hide();
			}
		}
		$('.button_' + sel).show();
	});

});
function getParameter(name) {
	if (name = (new RegExp('[?&]' + encodeURIComponent(name) + '=([^&]*)'))
			.exec(location.search))
		return decodeURIComponent(name[1]);
}

function onSubmit() {
	var customerId = getParameter('customerId');
	var url = "getCurrentSubcription.htm?customerId=" + customerId;
	window.location.href = url;

}

function scSubscription(customerId) {
	$.post("subscribedSC.htm", {
		customerId : customerId
	}, function(data) {
		document.getElementById("data").innerHTML = "";
		document.getElementById("data").innerHTML = data;
	});
}
function scPackageDetails(packageId, customerId) {
	$('li[class^=pkg]').css({
		'background-color' : '#d2d3f1'
	});
	$('li.pkg' + packageId).css({
		'background-color' : '#3E4E68',
		'outline' : 'medium none !important'
	});

	$.post("scPackageDetails.htm", {
		packageId : packageId,
		customerId : customerId
	}, function(data) {
		document.getElementById("descData").innerHTML = "";
		document.getElementById("descData").innerHTML = data;
	});
}

$(document).ready(function() {
	var selectIndex = new Array('1', '2', '3', '4');
	$('#data').delegate('input[name=selectRdoPackage]', 'change', function(e) {
		var sel = $('input[name=selectRdoPackage]:checked').val();
		for (var i = 0; i < selectIndex.length; i++) {
			if (selectIndex[i] != sel) {
				$('.button_' + selectIndex[i]).hide();
			}
		}
		$('.button_' + sel).show();
	});

});
function scSubscriptionDetails(serviceChargeRuleId, subscriptionType,
		numberOfTxn, customerId) {
	//	var customerId = getParameter('customerId');
	$.post("subscribeSC.htm", {
		customerId : customerId,
		serviceChargeRuleId : serviceChargeRuleId,
		subscriptionType : subscriptionType,
		numberOfTxn : numberOfTxn
	}, function(data) {
		document.getElementById("descData").innerHTML = "";
		document.getElementById("descData").innerHTML = data;
	});
}

function currentSC() {
	var customerId = getParameter('customerId');
	var url = "getSCSubscribedList.htm?customerId=" + customerId;
	window.location.href = url;
}
function enableTooltipss() {
	$('.enableTooltip').tooltipster();
};

var conf = "";
var custId = "";
var currentStatus = "";
var app = "";
function changeBlockApplication(custId1, currentStatus1, app1,form) {
	var canSubmit = "";
	custId = custId1;
	currentStatus = currentStatus1;
	app = app1;

	if (currentStatus == 80) {

		if ($('#blockComment').val() == '') {
			$('#myModal1').modal({
				backdrop : 'static',
				keyboard : false
			});
			document.getElementById('blockComment').value = '';
			return;
		} else {
			conf = true;
		}
	} else {
		conf = confirm(Alertmsg.ALERT_UNBLOCK_APP);
		if (conf) {
			BlockCustSubmit(form)
		}
	}
}

function BlockCustSubmit(form) {

	if (currentStatus == 80) {

		if ($('#blockComment').val() != ''
				&& $('#blockComment').val() != undefined) {

			if (confirm(Alertmsg.ALERT_BLOCK_APP)) {
				var customerId = document.getElementById('customerId').value = custId;
				var appStatus = document.getElementById('appStatus').value = currentStatus;
				var url = "changeApplicationStatus.htm";
				submitlink(url, form);
			}
		//	document.getElementById('rejectComment').value = '';

		} else {
			alert("Please comment for Blocking...");
			$('#blockComment').focus();
		}

	} else if (conf) {
		var customerId = document.getElementById('customerId').value = custId;
		var appStatus = document.getElementById('appStatus').value = currentStatus;
		var url = "changeApplicationStatus.htm";
		submitlink(url,form);
	}
}

function changeDeActivateApplication(custId1, currentStatus1, app1,form) {
	var canSubmit = "";
	custId = custId1;
	currentStatus = currentStatus1;
	app = app1;

	if (currentStatus == 40) {

		if ($('#deActivateComment').val() == '') {
			$('#myModalDeActivate').modal({
				backdrop : 'static',
				keyboard : false
			});
			document.getElementById('deActivateComment').value = '';
			return;
		} else {
			conf = true;
		}
	} else {
		conf = confirm(Alertmsg.ALERT_ACTIVATE_CUSTOMER);
		if (conf) {
			deActivateCustSubmit(form)
		}
	}
}

function deActivateCustSubmit(form) {

	if (currentStatus == 40) {

		if ($('#deActivateComment').val() != ''
				&& $('#deActivateComment').val() != undefined) {

			if (confirm(Alertmsg.ALERT_DEACTIVATE_CUSTOMER)) {
				var customerId = document.getElementById('customerId').value = custId;
				var appStatus = document.getElementById('appStatus').value = currentStatus;
				var url = "changeCustomerStatus.htm";
				submitlink(url, form);
			}
		//	document.getElementById('rejectComment').value = '';

		} else {
			alert("Please comment for DeActivate...");
			$('#deActivateComment').focus();
		}

	} else if (conf) {
		var customerId = document.getElementById('customerId').value = custId;
		var appStatus = document.getElementById('appStatus').value = currentStatus;
		var url = "changeCustomerStatus.htm";
		submitlink(url,form);
	}
}
function closeModel(){
	document.getElementById('rejectComment').value='';
}
function closeModelBlock(){
	document.getElementById('blockComment').value='';
}
function closeModelDeActivate(){
	document.getElementById('deActivateComment').value='';
}
