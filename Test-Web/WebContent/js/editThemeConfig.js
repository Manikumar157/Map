function setTextColor(picker) {
		document.getElementsByTagName('body')[0].style.color = '#' + picker.toString()
	}

function previewThemeConfig() {
	//alert("preview Theme");
}

function updateThemeConfig() {
	
	if(isValidate()){
		document.themeConfigForm.method = "post";
		document.themeConfigForm.action = "updateThemeConfig.htm";
		document.themeConfigForm.submit();
	}
}

function isValidate() {
	
	if(document.getElementById("bankId").value==""){
		alert("Please select bank.");
		document.themeConfigForm.bankId.focus();
		return false;
	}
	
//	if(document.getElementById("logoImg").value==""){
//		 if(document.getElementById("previousLogo") == undefined) {
//			alert("Please select logo.");
//			document.themeConfigForm.logoImg.focus();
//			return false;
//		 }
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
