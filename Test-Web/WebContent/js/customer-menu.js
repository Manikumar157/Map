$(document).ready(function() {
	
	$("body").on("change", "#bankId", function(){
		$bankId = $(this).val();
//		$csrfToken = $("#csrfToken").val();
		
		$.post("getCustomerProfilesByBank.htm", {
			bankId : $bankId
		}, function(data) {
			document.getElementById("profile-list-holder").innerHTML="";
			document.getElementById("profile-list-holder").innerHTML = data;
			//setTokenValFrmAjaxResp();
			applyChosen();
		});
	});
	
	$("body").on("change", ".tab-list-radio", function() {
		$tab = $(this).val();
		$.post("loadGridMenu.htm", {
			tabId: $tab,
			appId: 0,
		}, function (data) {
			document.getElementById("menu-list-holder").innerHTML="";
			document.getElementById("menu-list-holder").innerHTML = data;
			$("#menu-icon-list-holder").html(Alertmsg.emptyIcon);
			//setTokenValFrmAjaxResp();
			applyChosen();
		});
	});
	
	$("body").on("change", ".tab-menu", function(event) {
		if ($selectedMenu != "" && $selectedMenu != null && $selectedMenu != undefined && $selectedMenu != $(this).val()) {
			if ($("#tab-menu" + $selectedMenu).attr("selectedicon") == undefined) {
				alert("Please select icon for " + $('label[for=tab-menu' + $selectedMenu +']').text() + " before you continue.");
				$(this).prop("checked", false);
				event.preventDefault();
			    return false;
			}
		}
		if($(this).prop("checked") == true){
			$functionalCode = $(this).attr("functionalCode");
			$selectedMenu = $(this).val();
			$.post("loadMenuIcons.htm", {
				functionalCode: $functionalCode,
			}, function (data) {
				document.getElementById("menu-icon-list-holder").innerHTML="";
				document.getElementById("menu-icon-list-holder").innerHTML = data;
				$("#icons-for").html('for <b>' + $('label[for=tab-menu' + $selectedMenu +']').text() + '</b>');
				//setTokenValFrmAjaxResp();
				applyChosen();
			});
        }
        else if($(this).prop("checked") == false){
			$menu = $(this).val();
			$icon = $(this).attr("selectedIcon");
			var index = $selectedMenuIcon.indexOf($menu + '~' + $icon);
			$selectedMenu = null;
			if (index > -1) {
				$selectedMenuIcon.splice(index, 1);
			}
			$("#menu-icon-list-holder").text(Alertmsg.emptyIcon);
			$("#selected-menu-icon").val($selectedMenuIcon.toString());
			$("#icons-for").html('');
        }
	});
	
	$("body").on("change", ".tab-menu-icon", function() {
		$icon = $(".tab-menu-icon").val();
		var index = $selectedMenuIcon.indexOf($selectedMenu + '~' + $selectedIcon);
		var name=$('label[for=tab-menu' + $selectedMenu +']').text();
		console.log(name);
		var image = $("#selected-image"+ $(this).val()).attr("src");
		console.log(image);
		var imageElement = '<div class="imageBox"><img src="'+image+'"></div>';
		$selectedIcon = $(this).val();
		if (index > -1) {
			$selectedMenuIcon[index] = $selectedMenu + '~' + $(this).val();
		} else {
			$selectedMenuIcon.push($selectedMenu + '~' + $(this).val());
		}
		
		$("#tab-menu" + $selectedMenu).attr("selectedIcon", $(this).val());
		$("#selected-menu-icon").val($selectedMenuIcon.toString());
		$(".mobileSection").append("<div class='mobileMenu col-lg-4'>"+name+"<br/>"+imageElement+"</div>");
	});

});

function submitMobileMenuConfig() {
	var validationSuccess = true;
	if(document.customerMenuConfigForm.bankId.value == ""){
		alert(Alertmsg.emptyBankName);
		event.preventDefault();
		return false;
	} 
	
//	$(".tab-list-radio").each(function(){
//		if ($(this).prop("checked") == false) {
//			validationSuccess = false;
//		}
//	});
	if ($(".tab-list-radio:checked").length == 0) {
		alert(Alertmsg.emptyTab);
		event.preventDefault();
		return false;
	}
	var menuSelectedCount = 0;
	var menuIconCount = 0;
	$(".tab-menu").each(function(){
		if ($(this).prop("checked") == true) {
			menuSelectedCount++;
		}
		if ($(this).attr("selectedicon") != undefined) {
			menuIconCount++;
		}
	});
	if (menuSelectedCount == 0) {
		alert(Alertmsg.emptyMenu);
		event.preventDefault();
		return false;
	}
	if (menuIconCount == 0) {
		alert(Alertmsg.emptyMenuIcon);
		event.preventDefault();
		return false;
	}
	if (menuSelectedCount != menuIconCount) {
		alert("Please select the icon for the respected menu.");
		event.preventDefault();
		return false;
	}
	

	submitForm("customerMenuConfigForm", "saveDynamicMenuConfiguration.htm");
}


