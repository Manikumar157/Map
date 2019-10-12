<!-- Main jQuery Plugins -->
<link rel="stylesheet" href="css/jquery-ui.css" />
<link rel="stylesheet" href="css/jquery-ui-smoothness.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/ui/datatables/jquery-1.12.4.js"></script>
<!-- <script src="http://code.jquery.com/jquery-1.10.2.js"></script> -->
<script src="js/ui/jquery-ui.js"></script>
<script type='text/javascript' src="js/ui/jquery.js"></script>
<!-- <script src="js/ui/jquery-1.10.2.js"></script> -->
<script type='text/javascript' src='js/ui/jquery-ui.js'></script>
<script type='text/javascript' src='js/ui/bootstrap.js'></script>
<script type='text/javascript' src='js/ui/date.js'></script>
<script type='text/javascript'
	src='js/ui/slimscroll/jquery.slimscroll.js'></script>
<script type='text/javascript' src='js/ui/jquery.nicescroll.min.js'></script>
<script type='text/javascript' src='js/ui/sliding-menu.js'></script>
<script type='text/javascript'
	src='js/ui/scriptbreaker-multiple-accordion-1.js'></script>
<script type='text/javascript' src='js/ui/tip/jquery.tooltipster.min.js'></script>
<script type='text/javascript' src='js/ui/vegas/jquery.vegas.js'></script>
<script type='text/javascript' src='js/ui/image-background.js'></script>
<script type="text/javascript" src="js/ui/jquery.tabSlideOut.v1.3.js"></script>
<script type='text/javascript' src='js/chosen.jquery.js'></script>

<script type="text/javascript" src="js/ui/skycons/skycons.js"></script>
<!-- <script src="js/ui/datatables/jquery.dataTables.js"
	type="text/javascript"></script>
<script src="js/ui/datatables/dataTables.bootstrap.js"
	type="text/javascript"></script> -->
<script type="text/javascript" src="<%=request.getContextPath()%>/js/ui/datatables/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/ui/datatables/dataTables.select.min.js"></script>

<script src="js/ui/footable/js/footable.js?v=2-0-1"
	type="text/javascript"></script>
<script src="js/ui/footable/js/footable.sort.js?v=2-0-1"
	type="text/javascript"></script>
<script src="js/ui/footable/js/footable.filter.js?v=2-0-1"
	type="text/javascript"></script>
<script src="js/ui/footable/js/footable.paginate.js?v=2-0-1"
	type="text/javascript"></script>
<script src="js/ui/footable/js/footable.paginate.js?v=2-0-1"
	type="text/javascript"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/common.js"></script>
	 <%-- <script type="text/javascript"  src="<%=request.getContextPath()%>/js/tooltipster.bundle.js"></script> --%>
<script type="text/javascript" src="js/ui/app.js"></script>
<script type="text/javascript">

var currentLocation =window.location.href.substr(window.location.href.lastIndexOf("/")+1);
var currentId = currentLocation.substr(0,currentLocation.lastIndexOf(".htm"));
//comment below to fix ui issues in theme config (clicking on merchant, sole merchant, customer option in Theme Config)
//$("#"+currentId).css({"background":"#286b2a", "color":" rgb(243, 213, 6)!important;", "font-weight":"900"});
var menuId = $("#"+currentId).parent().parent().attr('id');
$('#'+menuId).show();
if(currentId=='saveCurrency'||currentId=='showCurrencies' ||currentId=='editCurrency'
	||currentId=='showExchangeRateForm' ||currentId=='addExchangeRate' ||currentId=='showCurrencyConverterForm'
		||currentId=='addCurrencyConverter'){ 
	$("#showCurrencies").css({
		"background" : "#286b2a"
});
$('#list').show();
}
 if(currentId == 'editCountry' || currentId == 'viewCities'|| currentId == 'saveCountries'
	|| currentId == 'editCity' || currentId == 'viewQuarters' || currentId == 'saveCities'
	|| currentId == 'editQuarter'|| currentId == 'showCountries' || currentId == 'saveQuarters') { 
$("#showCountries").css({
	"background" : "#286b2a"
});
$('#list').show();
}
 if(currentId == 'editLocateUS' || currentId == 'addLocateUS'
		|| currentId == 'showLocateUsForm') {
	$("#showLocateUSForm").css({
		"background" : "#286b2a"
});
	$('#list').show();
}
if (currentId == 'addClearanceHouseForm'
	|| currentId == 'editClearanceHouse'
	|| currentId == 'viewClearingHousePool'
	|| currentId == 'searchClearingHouse'
		|| currentId == 'showClearanceHouseForm'
	|| currentId == 'generateSettlementReport') {
$("#showClearanceHouseForm").css({
	"background" : "#286b2a"
});
$('#list').show();
}

if (currentId == 'editBankGroup'|| currentId == 'showBankGroup'
		|| currentId == 'saveBankGroup') {
$("#showBankGroup").css({
	"background" : "#286b2a"
});
$('#list1').show();
}

if (currentId == 'addBankForm' || currentId == 'searchBank'
	|| currentId == 'editBankForm' || currentId == 'showBankManagementForm'
	|| currentId == 'editBranchDetails' || currentId == 'AddBranchForm'
	|| currentId == 'searchBranch' || currentId == 'saveBank') {
$("#showBankManagementForm").css({
	"background" : "#286b2a"
});
$('#list1').show();
}
if (currentId == 'showBillersForm' || currentId == 'searchBillerPage' || currentId == 'saveBillersForm'
	|| currentId == 'addCard'|| currentId == 'searchBiller'|| currentId == 'editBiller') { 
$("#searchBiller").css({
	"background" : "#286b2a"
});
$('#list3').show();
}
if (currentId == 'addOperators' || currentId == 'searchOperators'
	|| currentId == 'editOperator' || currentId == 'viewDenominations'
	|| currentId == 'uploadVoucherForm' || currentId == 'showOperators'
	|| currentId == 'addOperatorCard' 	|| currentId == 'saveOperator'
	|| currentId == 'saveDenomination' 	|| currentId == 'viewVouchers'
	|| currentId == 'uploadVouchersFile' 	|| currentId == 'showReport'
	|| currentId == 'searchBarReport' 	|| currentId == 'barReportDataValidation'
	|| currentId == 'searchPieReport' 	|| currentId == 'pieReportDataValidation'
	|| currentId == 'getOperators' 	|| currentId == 'addOperatorCard'
	|| currentId == 'saveOperatorCard' || currentId == 'editDenomination') { 
$("#showOperators").css({
	"background" : "#286b2a"
});
$('#list3').show();
}

if (currentId == 'showHelpDeskConfig'|| currentId == 'editHelpDesk'|| currentId == 'saveHelpDesk'){
		
	$("#showHelpDesk").css({
		"background" : "#286b2a"
	});
	$('#list').show();
	}


if (currentId == 'editStakeHolderForm'|| currentId == 'showStakeHolderForm'){
$("#showStakeHolderForm").css({
	"background" : "#286b2a"
});
$('#list').show();
}

if (currentId == 'showUserForm' || currentId == 'searchWebUser' || currentId == 'editUser'
	|| currentId == 'viewUser' || currentId == 'resetUserPassword'|| currentId == 'showWebUserForm') {
$("#showWebUserForm").css({
	"background" : "#286b2a"
});
$('#list6').show();
}

if (currentId == 'editStakeHolderForm'){
$("#showStakeHolderForm").css({
	"background" : "#286b2a"
});
$('#list').show();
}

/* if (currentId == 'showUserForm' || currentId == 'searchWebUser' || currentId == 'editUser'
	|| currentId == 'viewUser') {
$("#showWebUserForm").css({
	"background" : "#286b2a"
});
$('#list5').show();
} 
*/
if (currentId == 'saveInterBankFee' || currentId == 'interBankForm') {
	$("#interBankForm").css({
		"background" : "#286b2a"
	});
	$('#list4').show();
	}
if (currentId == 'showServiceChargeRuleForm' || currentId == 'viewServiceChargeRule'
	|| currentId == 'editServiceChargeRule' || currentId == 'searchServiceChargeRules'
	|| currentId == 'exportToPdfForSCR' || currentId == 'exportToXlsForSCR'
	|| currentId == 'listServiceChargeRules' || currentId == 'saveServiceChargeRule') { 
$("#searchServiceChargeRules").css({
	"background" : "#286b2a"
});
$('#list4').show();
}
if (currentId == 'showTransactionRuleForm'
	|| currentId == 'editTransactionRule'
	|| currentId == 'listTransactionRules'
	|| currentId == 'viewTransactionRule'
	|| currentId == 'searchTransactionRules'
	|| currentId == 'exportToPdfForTxnRules'
	|| currentId == 'getCustomerProfiles'
	|| currentId == 'exportToPdfForTxnRules'
	|| currentId == 'exportToXlsForTxnRules'
	|| currentId == 'saveTransactionRule') { 
$("#searchTransactionRules").css({
	"background" : "#286b2a"
});
$('#list4').show();
}
if(currentId == 'searchSmsAlertRules' || currentId == 'showSMSAlertForm' || currentId == 'getSmsAlertRule' || currentId == 'addSMSAlerts'){
	
	$("#showSmsAlertPackages").css({
		"background" : "#286b2a"
	});
	$('#list4').show();
}
/* if (currentId == 'showServiceChargeRuleForm'
	|| currentId == 'editServiceChargeRule'
	|| currentId == 'listServiceChargeRules' || currentId == 'saveServiceChargeRule') {
$("#searchServiceChargeRules").css({
	"background" : "#286b2a"
});
$('#list3').show();
}
if (currentId == 'showTransactionRuleForm'
	|| currentId == 'editTransactionRule'
	|| currentId == 'listTransactionRules'
	|| currentId == 'saveTransactionRule') {
$("#searchTransactionRules").css({
	"background" : "#286b2a"
});
$('#list3').show();
} 
if(currentId == 'searchSmsAlertRules' || currentId == 'showSMSAlertForm' || currentId == 'getSmsAlertRule' || currentId == 'addSMSAlerts'){
	
	$("#showSmsAlertPackages").css({
		"background" : "#286b2a"
	});
	$('#list4').show();
	}
*/
/*//@start Vinod joshi  Date:27/12/2018 purpose:Already Configured.
if (currentId == 'searchAuditLog') {
$("#searchAuditLogForm").css({
	"background" : "#286b2a"
});
$('#list6').show();
}

if (currentId == 'showAccessLog' || currentId == 'viewPageVisited') {
$("#showAccessLog").css({
	"background" : "#286b2a"
});
$('#list6').show();
}
if (currentId == 'getVersion'|| currentId == 'showVersionDetailsFrom' || currentId == 'getVersionDetails') {
$("#showVersionFrom").css({
	"background" : "#286b2a"
});
$('#list6').show();
} 
*/
if (currentId == 'selectTransactionForm') {
$("#selectTransactionForm").css({
	"background" : "#286b2a"
});
$('#list5').show();
}

if (currentId == 'uploadTransactionDetails' || currentId == 'selectTransactionSupportForm') {
$("#selectTransactionSupportForm").css({
	"background" : "#286b2a"
});
$('#list5').show();
}
if (currentId == 'txnReports' || currentId == 'accountLedgerReport') {
	$("#txnReports").css({
		"background" : "#286b2a"
	});
	$('#list5').show();
	}
	
if (currentId == 'accountTransfer'|| currentId == 'confAccountToAccount') {
	$("#selectAccountTransactionForm").css({
		"background" : "#286b2a"
	});
	$('#list5').show();
	}
	
if (currentId == 'cusAppCommReport'|| currentId == 'pdfCusRegReport' || currentId == 'xlsCusRegReport') {
	$("#cusAppCommReport").css({
		"background" : "#286b2a"
	});
	$('#list5').show();
	}
if (currentId == 'merchantReport' || currentId == 'exportToXlsMerchantCommisionReport'
		|| currentId == 'exportToPdfMerchantCommisionReport'|| currentId == 'searchTxnMerchantData') {
	$("#merchantAppReport").css({
		"background" : "#286b2a"
	});
	$('#list5').show();
	}
	
if (currentId == 'viewSmsDetails') {
	$("#showSMSLog").css({
		"background" : "#286b2a"
	});
	$('#list14').show();
	}
	
if (currentId == 'addSmsCampaign' ||  currentId == 'sendSmsCampaign') {
	$("#showSMSCampaign").css({
		"background" : "#286b2a"
	});
	$('#list14').show();
	}
/*
if (currentId == 'exportToXlsExternalTransactionSummaryDetails'
	|| currentId == 'exportToXlsExternalTransactionDetails') {
$("#settlementDetails").css({
	"background" : "#286b2a"
});
$('#list4').show();
}
 if (currentId == 'barChartDataValidation'
	|| currentId == 'pieChartDataValidation') {
$("#showCharts").css({
	"background" : "#286b2a"
});
$('#list4').show();
}

if (currentId == 'searchExternalTxns'
	|| currentId == 'exportToXlsExternalTransactionSummaryDetails'
	|| currentId == 'exportToXlsExternalTransactionDetails') {
$("#cancellation").css({
	"background" : "#286b2a"
});
$('#list4').show();
}
*/
if (currentId == 'loadPrivilegeDetails'
	|| currentId == 'assignPrivilegeDetails') {
$("#showPrivilegeDetails").css({
	"background" : "#286b2a"
});
$('#list6').show();
}


if (currentId == 'searchExternalTxns'
	|| currentId == 'exportToXlsExternalTransactionDetails'
	|| currentId == 'exportToXlsExternalTransactionSummaryDetails') {
$("#showExternalTransactions").css({
	"background" : "#286b2a"
});
$('#list4').show();
}

/* if (currentId == 'searchTxnSummary'
	|| currentId == 'exportToXLSForTransactionSummaryForBankTellerEOD'
	|| currentId == 'exportToXLSForTransactionSummary'
	|| currentId == 'exportToXLSForTransactionSummaryForTxnSummary'
	|| currentId == 'exportToXLSForTransactionSummaryPerBank') {
$("#showTxnSummary").css({
	"background" : "#286b2a"
});
$('#list4').show();
} */

/* if (currentId == 'viewCustomer' || currentId == 'showCustomerForm' || currentId == 'addBankAccount' || currentId == 'addCustomerCard')
{
$("#searchCustomer").css({
"background" : "#286b2a"
});
$('#list5').show();
} */

/* if (currentId == 'viewMerchant' || currentId == 'showMerchantForm' || currentId == 'editMerchant' || currentId == "searchOutlet" || currentId =="showOutletForm" || currentId =="searchTerminal" || currentId =="viewOutlet" || currentId =="addTerminal" || currentId =="editOutlet" || currentId == "showAllOutlets" )
{
$("#searchMerchant").css({
"background" : "#286b2a"
});
$('#list5').show();
} */

if (currentId == 'viewAdjustmentTransaction' || currentId == 'uploadReversalDetails'
	|| currentId == 'showAdjustmentForm' || currentId == 'viewAdjustmentTransaction'
		|| currentId == 'uploadReversalFile' 
		|| currentId == 'searchAdjustmentTransactions' || currentId == 'submitAdjustmentTxn')
{
	$("#showAdjustmentForm").css({ 
	"background" : "#286b2a"
});
	$('#list5').show();
	}
if (currentId == 'searchSenelecBill'|| currentId == 'getSenelecBills'|| currentId == 'senelecPdfReport'
	|| currentId == 'viewDetailsByPolicyNo') { 
$("#getSenelecBills").css({
	"background" : "#286b2a"
});
$('#list3').show();
}
if (currentId == 'showCustomerProfiles' || currentId == 'editCustomerProfiles' || currentId == 'saveCustomerProfile') { 
$("#showCustomerProfiles").css({
	"background" : "#286b2a"
});
$('#list2').show();
}
if (currentId == 'searchAuditLog' || currentId == 'searchAuditLogForm' || currentId == 'datasearchAuditLogForm' ) { 
$("#searchAuditLogForm").css({ 
	"background" : "#286b2a"
});
$('#list7').show();
}

if (currentId == 'showAccessLog' || currentId == 'viewPageVisited' || currentId == 'searchAccessPage' || currentId == 'viewVisitedPage'
	 || currentId == 'datashowAccessLog' ) {
$("#showAccessLog").css({
	"background" : "#286b2a"
});
$('#list7').show();
}
if (currentId == 'getVersion'|| currentId == 'showVersionDetailsFrom' || currentId == 'getVersionDetails'
	|| currentId == 'showVersionFrom' || currentId == 'saveVersion' || currentId == 'updateVersion'
		|| currentId == 'saveVersionDetails') {
$("#showVersionFrom").css({
	"background" : "#286b2a"
});
$('#list7').show();
}

if (currentId == 'searchAgent' || currentId == 'viewAgent' || currentId == 'searchAgentPage' || currentId == 'addAgentRequest'
	 || currentId == 'saveAgent' || currentId == 'editAgent' || currentId == 'exportToXLSForAgentAccountDetails'
	|| currentId == 'exportToXLSForAgentAccountDetails' || currentId == 'changeAgentStatus' || currentId == 'editAgentProfiles'
	|| currentId == 'saveAgentProfile' || currentId == 'saveAgentBankAccount' || currentId == 'editAgentBankAccount'
		 || currentId == 'exportToPdfForAgentDetails' || currentId == 'exportToXlsForAgentDetails' ||currentId == 'changeMerchantStatus') { 
$("#searchAgent").css({ 
	"background" : "#286b2a"
});
$('#list9').show();
}

if (currentId == 'businessPartner' || currentId == 'addBusinessPartner' || currentId == 'viewBusinessPartner'
	|| currentId == 'saveBusinessPartner' || currentId == 'showBusinessPartnerForm' || currentId == 'searchBusinessPartner'
		|| currentId == 'editBusinessPartner' || currentId == 'exportToPDFBusinessPartners' || currentId == 'businessPartnerExcelReport') {
	$("#businessPartner").css({
		"background" : "#286b2a"
	});
	$('#list10').show();
	}

if (currentId == 'bulkpaymentpartnerPartner') {
	$("#transactionTest").css({
		"background" : "#286b2a"
	});
	$('#list11').show();
	}
	
if (currentId == 'uploadTransactionDetails') {
	$("#transactionTest").css({
		"background" : "#286b2a"
	});
	$('#list12').show();
	}

/* if (currentId == 'uploadTransactionDetails') {
$("#selectTransactionSupportForm").css({
	"background" : "#286b2a"
});
$('#list4').show();
}

if (currentId == 'uploadTransactionDetails') {
$("#selectTransactionSupportForm").css({
	"background" : "#286b2a"
});
$('#list4').show();
} */

if (currentId == 'exportToXlsExternalTransactionSummaryDetails'|| currentId == 'settlementDetails'
	|| currentId == 'exportToXlsExternalTransactionDetails') {
$("#settlementDetails").css({
	"background" : "#286b2a"
});
$('#list5').show();
}
if (currentId == 'barChartDataValidation'
	|| currentId == 'pieChartDataValidation' || currentId == 'showCharts') {
$("#showCharts").css({
	"background" : "#286b2a"
});
$('#list5').show();
}

if (currentId == 'cancellation'
	|| currentId == 'exportToXlsExternalTransactionDetails') {
$("#cancellation").css({
	"background" : "#286b2a"
});
$('#list5').show();
}

if (currentId == 'loadPrivilegeDetails'
	|| currentId == 'assignPrivilegeDetails' || currentId == 'showPrivilegeDetails') {
$("#showPrivilegeDetails").css({
	"background" : "#286b2a"
});
$('#list6').show();
}


if (currentId == 'searchExternalTxns' || currentId == 'showExternalTransactions'
	|| currentId == 'exportToXlsExternalTransactionDetails'
	|| currentId == 'exportToXlsExternalTransactionSummaryDetails') {
$("#showExternalTransactions").css({
	"background" : "#286b2a"
});
$('#list5').show();
}

if (currentId == 'searchTxnSummary'|| currentId == 'showTxnSummary'
	|| currentId == 'exportToXLSForTransactionSummaryForBankTellerEOD'
	|| currentId == 'exportToXLSForTransactionSummary'|| currentId == 'getRejectedReason'
	|| currentId == 'exportToXLSForTransactionSummaryForTxnSummary'
	|| currentId == 'exportToXLSForTransactionSummaryPerBank') { 
$("#showTxnSummary").css({
	"background" : "#286b2a"
});
$('#list5').show();
}

/* if (currentId == 'showTxnBusinessPartner')
{
$("#showTxnBusinessPartner").css({
"background" : "#286b2a"
});
$('#list5').show();
} */
if (currentId == 'getTxnSummaryDashBoard')
{
$("#dashBoard").css({
"background" : "#286b2a"
});
$('#list13').show();
}
if (currentId == 'showPendingTransactions'|| currentId == 'exportToXLSForPendingTransactionSummary' )
{
$("#showPendingTransactions").css({ 
"background" : "#286b2a"
});
$('#list5').show();
}

if (currentId == 'viewCustomer' || currentId == 'showCustomerForm' || currentId == 'addBankAccount' || currentId == 'addCustomerCard'
	|| currentId == 'searchMobileNo' || currentId == 'createAccount' || currentId == 'saveCustomer' || currentId == 'editCustomer'	
	|| currentId == 'searchCustomer' || currentId == 'searchCustomerPage' || currentId == 'exportToXLSForCustomerDetails' 
	|| currentId == 'exportToXLSForCustomerAccountDetails' || currentId == 'resetLoginPin' || currentId == 'resetTxnPin'
	|| currentId == 'reinstallApplication' || currentId == 'changeApplicationStatus' 
	|| currentId == 'addBankAccount' || currentId == 'saveCustomerBankAccount' || currentId == 'editCustomerBankAccount'
	|| currentId == 'addCustomerCard' || currentId == 'saveCustomerCard' || currentId == 'editCustomerCard')
{
$("#searchCustomer").css({
"background" : "#286b2a"
});
$('#list6').show();
}
if (currentId == 'viewMerchant' || currentId == 'showMerchantForm' || currentId == 'editMerchant' || currentId == "searchOutlet" || currentId =="showOutletForm" || currentId =="searchTerminal" || currentId =="viewOutlet" || currentId =="addTerminal" || currentId =="editOutlet" || currentId == "showAllOutlets" )
{
$("#searchMerchant").css({
"background" : "#286b2a"
});
$('#list6').show();
}

if (currentId == 'viewAdjustmentTransaction' || currentId == 'uploadReversalDetails')
{
	$("#showAdjustmentForm").css({
	"background" : "#286b2a"
});
	$('#list5').show();
}

if (currentId == 'showTxnBusinessPartner' 
	|| currentId == 'exportToPDFBusinessPartnersCommission'
	|| currentId == 'businessPartnerCommissionExcelReport')
{
	$("#businessPartnerCommission").css({
	"background" : "#286b2a"
});
	$('#list5').show();
}
		(function($) {
			"use strict";
			$("#example1").dataTable({
				"stripeClasses": ['red', 'green', 'yellow', '<spring:message code="datatable.css.row" text=""/>'],
				aaSorting: []
			     
			});
			
			$("#webUserTable").dataTable({
				"stripeClasses": ['red', 'green', 'yellow', '<spring:message code="datatable.css.row" text=""/>'],
				aaSorting: []
			     
			});
			
			$('#example2').dataTable({
				"bPaginate" : true,
				"bLengthChange" : false,
				"bFilter" : false,
				"bSort" : true,
				"bInfo" : true,
				"bAutoWidth" : false
			});
			

			$('#menuConfig').dataTable({
				"bPaginate" : true,
				"bLengthChange" : false,
				"bFilter" : false,
				"bSort" : true,
				"bInfo" : true,
				"bAutoWidth" : false
			});
		})(jQuery);

		(function($) {
			"use strict";
			$('.footable-res').footable();
		})(jQuery);

		(function($) {
			"use strict";
			$('#footable-res2').footable().bind(
					'footable_filtering',
					function(e) {
						var selected = $('.filter-status').find(':selected')
								.text();
						if (selected && selected.length > 0) {
							e.filter += (e.filter && e.filter.length > 0) ? ' '
									+ selected : selected;
							e.clear = !e.filter;
						}
					});

			$('.clear-filter').click(function(e) {
				e.preventDefault();
				$('.filter-status').val('');
				$('table.demo').trigger('footable_clear_filter');
			});

			$('.filter-status').change(function(e) {
				e.preventDefault();
				$('table.demo').trigger('footable_filter', {
					filter : $('#filter').val()
				});
			});

			$('.filter-api').click(function(e) {
				e.preventDefault();

				//get the footable filter object
				var footableFilter = $('table').data('footable-filter');

				alert('about to filter table by "tech"');
				//filter by 'tech'
				footableFilter.filter('tech');

				//clear the filter
				if (confirm('clear filter now?')) {
					footableFilter.clearFilter();
				}
			});
		})(jQuery);
		
  $( ".datepicker" ).datepicker({
	    	changeMonth: true,
	    	changeYear: true,
	    	buttonImage: 'images/calender-icon.png',
	    	buttonImageOnly: true,
	    	showOn: "both",//button
	    	yearRange: "-100:+100"
   });
  
   /*  $('.datepicker').daterangepicker({
       singleDatePicker: true,
       showDropdowns: true
   }, 
   function(start, end, label) {
       var years = moment().diff(start, 'years');
       alert("You are " + years + " years old.");
   }); */

  $(".menu").slimscroll({
      height: "200px",
      alwaysVisible: true,
      size: "5px"
  }).css("width", "100%");


  //tooltip menu sidebar setting

  $("[data-toggle='tooltip']").tooltip({
	  closedSign: '<b class="fa fa-caret-right"></b>',
      openedSign: '<b class="fa fa-caret-down"></b>'
  });

  $(".side-bar").accordionze({
      accordionze: true,
      speed: 300,
      closedSign: '<b class="fa fa-caret-right"></b>',
      openedSign: '<b class="fa fa-caret-down"></b>'
  });

  $('.tooltip-tip').tooltipster({
      position: 'top-left',
      animation: 'slide',
      theme: '.tooltipster-shadow',
      delay: 1,
      offsetY: '-50px',
      offsetX: '-60px',
      onlyOne: true
   //   restoration : previous

  });
  
  function changeTheme(imgId){
		var imgPath = '';
		var contextPath = '<%=request.getContextPath()%>';
		if(imgId=='button-bg')
			imgPath = contextPath+"/images/wg_blurred_backgrounds_11.jpg";
		if(imgId=='button-bg2')
			imgPath = contextPath+"/images/wg_blurred_backgrounds_5.jpg";
		if(imgId=='button-bg3')
			imgPath = contextPath+"/images/wg_blurred_backgrounds_9.jpg";
		if(imgId=='button-bg4')
			imgPath = contextPath+"/images/wg_blurred_backgrounds_19.jpg";
		if(imgId=='button-bg5')
			imgPath = contextPath+"/images/wg_blurred_backgrounds_3.jpg";
		if(imgId=='button-bg6')
			imgPath = contextPath+"/images/wg_blurred_backgrounds_6.jpg";
		 $("body").css({
	         "background": "url("+imgPath+")no-repeat center center fixed"
	     });
	}
  $(document).ready(function(){

	  applyChosen();

  });
  
  function applyChosen(){
	  $(".chosen-select").chosen({disable_search: true});
  }
  
  function updateChosen(){
	  $(".chosen-select").trigger("chosen:updated");
  }
</script>

<!-- TAB SLIDER -->