function setTextColor(picker) {
		document.getElementsByTagName('body')[0].style.color = '#' + picker.toString()
	}

function previewThemeConfig() {
	//alert("preview Theme");
}

function saveThemeConfig() {
	
	if(isValidate()){
		/*document.webColorConfigForm.method = "post";
		document.webColorConfigForm.action = "saveThemeConfig.htm";
		document.webColorConfigForm.submit();*/
		
		submitForm("addWebColorConfigForm", "saveWebColorConfig.htm");
	}
}

function cancelWebColorConfig(formId, action) {
	
	var url=action;
	
	submitForm(formId, url);
}

function clearThemeConfig() {
	
}

function isValidate() {
	/*--@start Murari  Date:03/08/2018 purpose:bug 5733 */
	var filetype = $("#logoImg").val().substring( $("#logoImg").val().lastIndexOf("."));
	/*End*/
	
	if(document.getElementById("bankId").value==""){
		alert("Please select bank.");
		document.webColorConfigForm.bankId.focus();
		return false;
	}
	
	if(document.getElementById("logoImg").value==""){
		alert("Please select logo.");
		document.webColorConfigForm.logoImg.focus();
		return false;
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
$(document).ready(function(){
	
	//mobile view section
		$('input[type=text]').change(function(){
			var element = $(this).attr('actionElem');
			$(element).css('background-color', $(this).css('background-color'));
			$(element).css('background-image', "url()");
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
/*--@start Murari  Date:30/07/2018 purpose:cross site Scripting */
function webColorView(bankId) {
	document.getElementById('bankId').value=bankId;
	submitlink('viewWebColorConfigForm.htm','webColorConfigForm');
}
function webColorEdit(bankId) {
	document.getElementById('bankId').value=bankId;
	submitlink('editWebColorConfigForm.htm','webColorConfigForm');
}
/*End*/


