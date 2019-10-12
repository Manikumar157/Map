function setTextColor(picker) {
		document.getElementsByTagName('body')[0].style.color = '#' + picker.toString()
	}

function previewThemeConfig() {
	//alert("preview Theme");
}

function saveThemeConfig() {
	
	if(isValidate()){
		/*document.themeConfigForm.method = "post";
		document.themeConfigForm.action = "saveThemeConfig.htm";
		document.themeConfigForm.submit();*/
		
		submitForm("themeConfigForm", "saveThemeConfig.htm");
	}
}


function cancelThemeConfig(formId, action, appType) {
	
	var url=action+"?appType="+appType;
	
	submitForm(formId, url);
}

function clearThemeConfig() {
	
}

function isValidate() {
	
	if(document.getElementById("bankId").value==""){
		alert("Please select bank.");
		document.themeConfigForm.bankId.focus();
		return false;
	}
	
//	if(document.getElementById("logoImg").value==""){
//		alert("Please select logo.");
//		document.themeConfigForm.logoImg.focus();
//		return false;
//	}
	
	return true;
	
}
$(document).ready(function(){
	
	//mobile view section
		$('input[type=text]').change(function(){
			var element = $(this).attr('actionElem');
			$(element).css('background-color', $(this).css('background-color'));
//			$(element).css('background-image', "url()");
		});
		$('input[type=radio]').change(function(){
			var element = $(this).attr('actionElem');
			$(element + "> p").css('color', '#'+$(this).val());
		})
		
	//web view section
		function readURL(input) {
			if (input.files && input.files[0]) {
				var reader = new FileReader();
				
				reader.onload = function (e) {
					$('#headerlogo').attr('src', e.target.result);
				}
				reader.readAsDataURL(input.files[0]);
			}
		}
		$("#logoImg").change(function(){
			readURL(this);
		});
	});
<!-- //@start Murari  Date:30/07/2018 purpose:cross site Scripting -->
function themeViewDetails(bankId, appType) {
	document.getElementById('bankId').value=bankId;
	document.getElementById('appType').value=appType;
	submitlink('viewThemeConfigForm.htm','themeConfigForm');
}
function themeEditDetails(bankId, appType) {
	document.getElementById('bankId').value=bankId;
	document.getElementById('appType').value=appType;
	submitlink('editThemeConfigForm.htm','themeConfigForm');
}
<!--  @End-->