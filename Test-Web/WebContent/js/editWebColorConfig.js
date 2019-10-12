function setTextColor(picker) {
		document.getElementsByTagName('body')[0].style.color = '#' + picker.toString()
	}

function previewThemeConfig() {
	//alert("preview Theme");
}

function updateThemeConfig() {
	
	if(isValidate()){
		
		document.editWebColorConfigForm.action = "updateWebColorConfig.htm";
		document.editWebColorConfigForm.submit();
	}
}

function isValidate() {
	
	/*--@start Murari  Date:03/08/2018 purpose:bug 5733 */
	var filetype = $("#logoImg").val().substring( $("#logoImg").val().lastIndexOf("."));
	/*End*/
	if(document.getElementById("bankId").value==""){
		alert("Please select bank.");
		document.themeConfigForm.bankId.focus();
		return false;
	}
	
	if(document.getElementById("logoImg").value==""){
		 if(document.getElementById("previousLogo") == undefined) {
			alert("Please select logo.");
			document.themeConfigForm.logoImg.focus();
			return false;
		 }
	}
	
	/*--@start Murari  Date:03/08/2018 purpose:bug 5733 */
	if(filetype.toLowerCase() != ".jpg" && filetype.toLowerCase() != ".jpeg" && filetype.toLowerCase() != ".png"){
	    alert("Please select the logo file format like .png or .jpeg");
	    $("#logoImg").focus();
		return false;
	}
	/*End*/
	
	return true;
	
}