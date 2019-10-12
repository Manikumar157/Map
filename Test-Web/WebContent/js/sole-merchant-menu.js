function getRequestParam(){
	var url_string = window.location.href;
	var url = new URL(url_string);
	var appId = url.searchParams.get("appType");
	if (appId == null || appId == "") {
		/* AUTHOR: Murari DATE: 02 AUG 2018 - Purpose - bug 5794 start---*/
		/*var appId = url.searchParams.get("appId");*/
		var appId = $('#appId').val();
		/*end*/
	}
	return appId;
}
$(document).ready(function() {
	if ($("#selected-menu-icon").val() != "") {
		$selectedDetails = JSON.parse($("#selected-menu-icon").val());
		/* AUTHOR: Vineeth DATE: 18 JUL 2018 - SELECTED VALUE WAS NOT FETCHING, SO CHANGED SOME CODE TO MAKE IT ACCESS*/
		$selectedTabId = $(".tab-list-radio:checked").val();
		$checkExistMenu = $selectedDetails.find((menu) => menu.tabId == $selectedTabId);
		if ($checkExistMenu != undefined && $selectedDetails != "") {
			$tabMenuIconList = $checkExistMenu.tabMenuIconList;
			setEachMenuAndDisplayPreview();
		} else {
			$tabMenuIconList = [];
		}
	}
	
	$(".edit-page-icons").each(function(){
		var name=$('label[for=tab-menu' + $(this).attr("menuId") +']').text()
		var image = $(this).attr("src");
		var imageElement = '<div class="imageBox"><img src="'+image+'"></div>';
		$(".mobileSection").append("<div class='mobileMenu col-lg-4'>"+name+"<br/>"+imageElement+"</div>")
		$(".icon").append("<div class='mobileMenu col-lg-4'>"+name+"<br/>"+imageElement+"</div>")
	});
	
	$("body").on("change", "#bankId", function(){
		$bankId = $(this).val();
		resetTabsAndMenus();
		$.post("getCustomerProfilesByBank.htm", {
			bankId : $bankId
		}, function(data) {
			document.getElementById("profile-list-holder").innerHTML="";
			document.getElementById("profile-list-holder").innerHTML = data;
			//setTokenValFrmAjaxResp();
			applyChosen();
		});
	});
	$("body").on("change", "#profileId", function(){
		resetTabsAndMenus();
	});
	
	$("body").on("change", ".tab-list-radio", function() {
		
		var $checkMenuExistance = $tabMenuIconList.find((menu) => menu.menuId == $selectedMenu);
		if ($checkMenuExistance != undefined && $checkMenuExistance.iconId == undefined) {
			var index = $tabMenuIconList.indexOf($checkMenuExistance);
			if (index !== -1) {
				$tabMenuIconList.splice(index, 1);
			}
		}
		
		
		/* On change of tab */
		if ($selectedDetails.length > 0) {
			var $checkSelectedTab = $selectedDetails.find((tab) => tab.tabId == $selectedTabId);
			if ($checkSelectedTab == undefined || $checkSelectedTab == "") {
				$selectedDetails.push({"tabId": $selectedTabId, "tabMenuIconList": $tabMenuIconList});
				resetTrackingVariable();
			} else {
				var index = $selectedDetails.indexOf($checkSelectedTab);
				if (index !== -1) {
					$checkSelectedTab.tabMenuIconList = $tabMenuIconList;
					$selectedDetails[index] = $checkSelectedTab;
					resetTrackingVariable();
				}
			}
		} else {
			$selectedDetails.push({"tabId": $(this).val(), "tabMenuIconList": $tabMenuIconList});
			resetTrackingVariable();
		}
		
		
		$selectedTabId = $(this).val();
		var $checkSelectedTab = $selectedDetails.find((tab) => tab.tabId == $selectedTabId);
		if ($checkSelectedTab != undefined && $checkSelectedTab != "") {
			$selectedTabId = $checkSelectedTab.tabId;
			$tabMenuIconList = $checkSelectedTab.tabMenuIconList;
		}
		var checkBankExist = checkBankProfileExistance($selectedTabId);
		

	});
	
	$("body").on("change", ".tab-menu", function(event) {
		if ($selectedMenu != "" && $selectedMenu != null && $selectedMenu != undefined && $selectedMenu != $(this).val()) {
			var $checkMenuExistance = $tabMenuIconList.find((menu) => menu.menuId == $selectedMenu);
			/* AUTHOR: Vineeth DATE: 18 JULY 2018 */
			if ($checkMenuExistance != undefined && $checkMenuExistance.iconId == undefined) {
				alert("Please select icon for " + $('label[for=tab-menu' + $selectedMenu +']').text() + " before you continue.");
				$(this).prop("checked", false);
				event.preventDefault();
			    return false;
			}
		}
		if($(this).prop("checked") == true){
			$selectedMenu = $(this).val();
			$selectedMenuText = $('label[for=tab-menu' + $selectedMenu +']').text();
			var $checkMenuIconList = $tabMenuIconList.find((menu) => menu.menuId == $selectedMenu);
			if ($checkMenuIconList == undefined || $checkMenuIconList == "") {
				$tabMenuIconList.push({
					"menuId": $selectedMenu,
					"iconId": undefined,
					"menuName": $selectedMenuText,
					"iconImage": undefined,
					"dbId": undefined,
					"status": "10"
				});
				$functionalCode = $(this).attr("functionalCode");
				getIconList($functionalCode, $selectedMenu);
			} else {
				$functionalCode = $(this).attr("functionalCode");
				getIconList($functionalCode, $selectedMenu, $checkMenuIconList.iconId);
				var index = $tabMenuIconList.indexOf($checkMenuIconList);
				if (index !== -1) {
					$checkMenuIconList.menuId = $selectedMenu;
					$checkMenuIconList.menuName = $selectedMenuText;
					$checkMenuIconList.status = "10";
					$tabMenuIconList[index] = $checkMenuIconList;
				}
			}

		} else if($(this).prop("checked") == false){ 
			$uncheckedMenu = $(this).val();
			var $checkMenuIconList = $tabMenuIconList.find((menu) => menu.menuId == $uncheckedMenu);
			var index = $tabMenuIconList.indexOf($checkMenuIconList);
			if ($checkMenuIconList.dbId == undefined) {
				if (index !== -1) {
					$tabMenuIconList.splice(index, 1);
				}
				createPreviewImage();
			} else {
				$checkMenuIconList.status = "20";
				createPreviewImage();
			}
			$("#menu-icon-list-holder").text(Alertmsg.emptyIcon);
		}
		// change by vineeth, written by nupur, added to maintain a background for selected icon in the tab menu.
		$.each($tabMenuIconList,function(){
			const iconImage = this.iconId;
			setTimeout(function() {
				$(".tab-menu-icon").each(function(){
					if($(this).attr('value') == iconImage){
						$(this).css("background-color","grey");
					}
				});
			},1000);
			
		});
		//change over
	});
	// vineeth changes
	$("body").on("click", ".tab-menu-icon", function() {
		var $selectedIcon = $(this).attr('value');
		var $checkMenuIconList = $tabMenuIconList.find((menu) => menu.menuId == $selectedMenu);
		if ($checkMenuIconList == undefined || $checkMenuIconList == "") {
		$tabMenuIconList.push({
		"menuId": $selectedMenu,
		"iconId": $selectedIcon,
		"menuName": $('label[for=tab-menu' + $selectedMenu +']').text(),
		"iconImage": $("#selected-image"+ $(this).attr('value')).attr("src"),
		"dbId": undefined
		});
		createPreviewImage();
		} else {
		var index = $tabMenuIconList.indexOf($checkMenuIconList);
		if (index !== -1) {
		$checkMenuIconList.iconId = $selectedIcon;
		$checkMenuIconList.iconImage = $("#selected-image"+ $(this).attr('value')).attr("src");
		$tabMenuIconList[index] = $checkMenuIconList;
		}
		createPreviewImage();
		// vineeth change
		$(".tab-menu-icon").css("background-color", "");
		$(this).css("background-color", "grey");
		// vineeth change over
		}

	});

});

function submitMobileMenuConfig(formId, formActionUrl) {
	
	if ($selectedDetails.length > 0) {
		var $checkSelectedTab = $selectedDetails.find((tab) => tab.tabId == $selectedTabId);
		if ($checkSelectedTab == undefined || $checkSelectedTab == "") {
			$selectedDetails.push({"tabId": $selectedTabId, "tabMenuIconList": $tabMenuIconList});
		} else {
			var index = $selectedDetails.indexOf($checkSelectedTab);
			if (index !== -1) {
				$checkSelectedTab.tabMenuIconList = $tabMenuIconList;
				$selectedDetails[index] = $checkSelectedTab;
			}
		}
	} else {
		$selectedDetails.push({"tabId": $selectedTabId, "tabMenuIconList": $tabMenuIconList});
	}
	
	
	
	var validationSuccess = true;
	if ($("#editBankId").length > 0) { 
		if ($("#editBankId").val() == "") {
			alert(Alertmsg.emptyBankName);
			event.preventDefault();
			return false;	
		}
	} else {
		if(document.customerMenuConfigForm.bankId.value == ""){
			alert(Alertmsg.emptyBankName);
			event.preventDefault();
			return false;
		} 
	}

	if ($(".tab-list-radio:checked").length == 0) {
		alert(Alertmsg.emptyTab);
		event.preventDefault();
		return false;
	}
	

	var menuSelectedCount = 0;
	var menuIconCount = 0;
	var jsonForSaveData = [];
	var $jsonMenuListForSave = [];
	for (let validateTab of $selectedDetails) {
		for (let validateMenu of validateTab.tabMenuIconList) {
			if (validateMenu.menuId != undefined) {
				menuSelectedCount++;
			}
			if (validateMenu.iconId != undefined) {
				menuIconCount++;
			}
	//	}
		// vineeth changes
      //		$jsonMenuListForSave = validateTab.tabMenuIconList.filter((menu) => {menu.iconImage="";return menu;});
      //		if ($jsonMenuListForSave.length > 0) {
      //			jsonForSaveData.push({"tabId": validateTab.tabId, "tabMenuIconList": $jsonMenuListForSave});
	// changes over
		}
	}


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
	
	/*AUTHOR: Pavan DATE: 28 JUN 2018 - Bug Fix: Icon getting empty when error is coming bug fixed*/

	for (let validateTab of $selectedDetails) {
		$jsonMenuListForSave = validateTab.tabMenuIconList.filter((menu) => {menu.iconImage="";return menu;});
		if ($jsonMenuListForSave.length > 0) {
			jsonForSaveData.push({"tabId": validateTab.tabId, "tabMenuIconList": $jsonMenuListForSave});
		}
	}
	$("#selected-menu-icon").val(JSON.stringify(jsonForSaveData));
	submitForm(formId, formActionUrl);
}


function getIconList($functionalCode, $selectedMenu, $iconCode=null) {
	$.post("loadMenuIcons.htm", {
		functionalCode: $functionalCode,
	}, function (data) {
		document.getElementById("menu-icon-list-holder").innerHTML="";
		document.getElementById("menu-icon-list-holder").innerHTML = data;
		$("#icons-for").html('for <b>' + $('label[for=tab-menu' + $selectedMenu +']').text() + '</b>');
		if ($iconCode != null) {
			$("input:radio[class='tab-menu-icon'][value=" + $iconCode + "]").prop('checked', true);
			createPreviewImage();
		}
		//setTokenValFrmAjaxResp();
		applyChosen();
	});
}

function showBankConfigurationView($bankId, $profileId, $bankName = null) {
	$.post("getConfigurationBybank.htm", {
		bankId: $bankId,
		profileId: $profileId,
		appId: getRequestParam()
	}, function (data) {
		document.getElementById("menu-preview").innerHTML="";
		document.getElementById("menu-preview").innerHTML = data;
		$("#myModal").modal('show');
		$("#modal-heading").text($bankName);
		constructPreview();
	});
}

function resetTrackingVariable() {
	$tabMenuIconList = [];
	$selectedTabId = null;
	$selectedMenu = null;
}

function setEachMenuAndDisplayPreview() {
	$(".tab-menu").each(function(){
		var $checkMenuExistance = $tabMenuIconList.find((menu) => menu.menuId == $(this).val());
		if ($checkMenuExistance != undefined && $checkMenuExistance != "") {
			$(this).prop("checked", true);
		}
	});
	createPreviewImage();
}

function createPreviewImage() {
	$(".mobileSection").html("");
	for (let $imagePreview of $tabMenuIconList) {
		if ($imagePreview.status == "10" && $imagePreview.iconId != undefined && $imagePreview.iconImage != undefined) {
			var imageElement = '<div class="imageBox"><img src="'+$imagePreview.iconImage+'"></div>';
			$(".mobileSection").append("<div class='mobileMenu col-lg-4 text-center'>"+imageElement+"<div class='menu-text'>"+$imagePreview.menuName+"</div></div>");	
		}
	}
}

function constructPreview() { 
	var $previewData = JSON.parse($('#preview-selected-menu-icon').val());
	var i = 1;
	for (let $tabs of $previewData) {
		/* AUTHOR: Vineeth DATE: 18 JULY 2018 - MADE CHANGES TO CORRECT THE PREVIEW*/
		activeCheck = '';
		var active = $('.mobile-popup-preview ul li[class="active"] a').attr('href');
		var selectedTab = active.replace("#menuTab", "");
		var activeCheck = ($tabs.tabId==selectedTab) ? 'active' : '';
		var $tabElement = '<div role="tabpanel" class="tab-pane '+ activeCheck +'" id="menuTab'+$tabs.tabId+'">';
		for (let $menuList of $tabs.tabMenuIconList) {
			if ($menuList.status == "10") {
				var imageElement = '<div class="imageBox"><img src="'+$menuList.iconImage+'"></div>';
				$tabElement += "<div class='mobileMenu col-lg-4 text-center'>"+imageElement+"<div class='menu-text'>"+$menuList.menuName+"</div></div>";	
			}
		}
		$tabElement +="</div>";
		$("#preview-content-selected").append($tabElement);
		i++;
	}
}

function checkBankProfileExistance($selectedTabId) {
	if ($("#editBankId").length == 0) { 
		$bankId = $("#bankId").val();
		$profileId = $("#profileId").val();
		$appId = getRequestParam();
		$tabId = $selectedTabId;
		$.ajax({
	        url: "checkBankProfileExistance.htm",
	        type: 'POST',
	        async: false,
	        data: {bankId: $bankId, profileId: $profileId, appId: $appId, tabId: $tabId},
	        error: function(){
	           alert("error");
	        },
	        success: function(msg){ 
	        	var message = null;
	        	if (msg == "Success") {
	        		getMenuIconList($selectedTabId);
	        	} else {
		        	Object.keys(Alertmsg).map(function(key){if (key == msg) {message = Alertmsg[key];}});
		        	if (confirm(message) == true) {
		        		submitForm('customerMenuConfigForm', 'loadConfigurationBybank.htm?bankId='+$bankId+'&profileId='+$profileId+'&appId='+$appId);
		        	} else {
		        		resetTabsAndMenus();
		        	}
	        	}
	        }
	    });
	} else {
		getMenuIconList($selectedTabId);
	}
}

function cancelMenuConfig(formId, action, appType) {
	
	var url=action+"?appType="+appType;
	
	submitForm(formId, url);
}


function getMenuIconList($selectedTabId) {
	$appId = getRequestParam();
	$selectedMenu = null;
	$.post("loadGridMenu.htm", {
		tabId: $selectedTabId,
		appId: $appId,
	}, function (data) {
		document.getElementById("menu-list-holder").innerHTML="";
		document.getElementById("menu-list-holder").innerHTML = data;
		$("#menu-icon-list-holder").html(Alertmsg.emptyIcon);
		setEachMenuAndDisplayPreview();
	});
}

function resetTabsAndMenus() {
	$(".tab-list-radio").prop("checked", false);
	$("#menu-list-holder").html(Alertmsg.emptyTab);
	$("#menu-icon-list-holder").html(Alertmsg.emptyTab);
	$selectedDetails = [];
	resetTrackingVariable();
	createPreviewImage();
}
/*-- //@start Murari  Date:30/07/2018 purpose:cross site Scripting --*/
function merchantDetails(bankId, profileId, appId) {
	document.getElementById('bankId').value=bankId;
	document.getElementById('profileId').value=profileId;
	document.getElementById('appId').value=appId;
	submitlink('loadConfigurationBybank.htm','customerMenuConfigForm');
}
/*--  @End--*/