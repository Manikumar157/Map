function downloadFile(fileName){
	canSubmit = true;
	//$csrfToken = $("#csrfToken").val();
	$("#failedFileName").val(fileName);
	submitFailFiles("downloadFailedFiles.htm", "failedFileDownloadForm");
	document.body.style.cursor = '';
}

function submitFailFiles(url,formId){
		document.body.style.cursor = 'wait';
		
		if(document.getElementById(formId)!=null){
			document.getElementById(formId).method = "post";
			document.getElementById(formId).action = url;
			document.getElementById(formId).submit();
	}
		//setTokenValFrmAjaxResp();
		$("#failedFileName").val("");
}

function updateCSRFToken(tokenData){
	$('input[name=csrfToken]').val($($(tokenData)[0]).find("input").val());
}


function compareFromDate() {

	var toDate = document.failedFileDownloadForm.toDate.value;
	var fromDate = document.failedFileDownloadForm.fromDate.value;

	if (new Date(fromDate) > new Date()) {
		alert(Alertmsg.fromDateGreaterCurrentDate);
		document.failedFileDownloadForm.fromDate.value = "";
		return false;
	}
	if (toDate != "") {
		if (new Date(toDate) > new Date()) {
			alert(Alertmsg.toDateGreaterCurrentDate);
			document.failedFileDownloadForm.toDate.value = "";
			return false;
		}
		if (new Date(fromDate) > new Date(toDate)) {
			alert(Alertmsg.toDateFromDate);
			document.failedFileDownloadForm.toDate.value = "";
			return false;
		}
	} else
		return true;
}


function OnLoad() {
	//menuPgload();
	openCalanderFromDate();
	openCalanderToDate();
	
	noBack();
	
}
function openCalanderFromDate() {
	Zapatec.Calendar.setup({
		firstDay : 1,
		timeFormat : "12",
		electric : false,
		inputField : "fromDate",
		button : "trigger",
		ifFormat : "%Y-%m-%d",
		daFormat : "%Y/%m/%d",
		timeInterval : 01
	});
}

function openCalanderToDate() {
	Zapatec.Calendar.setup({
		firstDay : 1,
		timeFormat : "12",
		electric : false,
		inputField : "toDate",
		button : "trigger1",
		ifFormat : "%Y-%m-%d",
		daFormat : "%Y/%m/%d",
		timeInterval : 01
	});
}

function searchFailFileSubmit() {
	$("#searchPage").val("1");
	
	if (document.failedFileDownloadForm.fromDate.value != ""
			&& document.failedFileDownloadForm.toDate.value == "") {
		alert("Please select Registered To Date.");
		document.failedFileDownloadForm.fromDate.focus();
		return false;
	} else if (document.failedFileDownloadForm.fromDate.value == ""
			&& document.failedFileDownloadForm.toDate.value != "") {
		alert("Please select Registered From Date.");
		document.failedFileDownloadForm.fromDate.focus();
		return false;
	}
	submitlink("showFailedFiles.htm", "failedFileDownloadForm");
}


function clearForm() {
	
	document.failedFileDownloadForm.fromDate.value  = "";
	document.failedFileDownloadForm.toDate.value = "";
	document.failedFileDownloadForm.failedFileName.value = "";
}

