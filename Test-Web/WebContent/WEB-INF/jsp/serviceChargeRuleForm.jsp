<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<style type="text/css">
.inner-addon {
    position: relative;
}
th {
   text-align: center !important;
}
/*  #paper-bg {
min-height: 1210px;
} */
#paper-bg {
   min-height: 1011px;
}
.col-md-12.container {
   margin-top: 15px;
}
.pull-right.view_size {
   margin-top: 4px;
}
.first_box {
   box-shadow: 0px 0px 8px 1px rgba(46, 102, 5, 0.43);
   border-radius: 10px;
}
.second_box {
   box-shadow: 0px 0px 8px 1px rgba(46, 102, 5, 0.43);
   border-radius: 10px;
}
.third_box{
box-shadow: 0px 0px 8px 1px rgba(46, 102, 5, 0.43);
   border-radius: 10px;
}

/* style icon */
.inner-addon .glyphicon {
  position: absolute;
  padding: 10px;
  pointer-events: none;

}
.chosen-container chosen-container-single chosen-container-single-nosearch
{
width:150px !important;
}

/* align icon */
.right-addon .glyphicon  { right:  15px;}


/* add padding  */
.right-addon input  { padding-right:  30px; }
img.ui-datepicker-trigger {
    margin-left: 177px;
    margin-top: -55px;
}

</style>

<!-- Loading Calendar JavaScript files -->
<script type="text/javascript"    src="<%=request.getContextPath()%>/js/zapatec.js"></script>
<script type="text/javascript"    src="<%=request.getContextPath()%>/js/<spring:message code="CALENDAR_LANG"/>.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript"    src="<%=request.getContextPath()%>/js/serviceCharge.js"></script>   
<script type="text/javascript"    src="<%=request.getContextPath()%>/js/subscriptionCharge.js"></script>   
<script type="text/javascript">



$(document).ready(function() {
//	setDayArraysByTableData();
    $("#ruleLevels").change(function() {
        var ruleLevel = document.getElementById("ruleLevelCustomerProfile").checked ;
        if( ruleLevel ){
            $("#profiles").show();
        }else{
            $("#profiles").hide();
        }
       
    });
   
});

//@start @Vinod Joshi Date:17/10/2018 purpose:cross site Scripting -->
function viewDetails(url,PageNumber){
	document.getElementById('pageNumber').value=PageNumber;
	submitlink(url,'serviceChargeForm');
}
//@end

    var AlertMsg={           
        "empty":"<spring:message code='ALERT_SC_ALLFIELD' text='Please Fill all values.'/>",
        "minTxn":"<spring:message code='ALERT_SC_MINTXN' text='Please give valid Minimum Transaction amount.'/>",
        "maxTxn":"<spring:message code='ALERT_SC_MAXTXN' text='Please give valid Maximum Transaction amount.'/>",
        "rangeTxn":"<spring:message code='ALERT_SC_TXNRANGE' text='Transaction range already exist.'/>",
        "perc":"<spring:message code='ALERT_SC_PERC' text='Please give valid Percentage.'/>",
        "fixed":"<spring:message code='ALERT_SC_SCFIXED' text='Please give valid Service Charge Fixed.'/>",
        "dlimit":"<spring:message code='ALERT_SC_DLIMIT' text='Please give valid Discount Limit.'/>",
        "minSC":"<spring:message code='ALERT_SC_MINSC' text='Please give valid Minimum Service Charge.'/>",
        "maxSC":"<spring:message code='ALERT_SC_MAXSC' text='Please give valid Maximum Service Charge.'/>",       
        "edit":"<spring:message code='LABEL_EDIT' text='Edit'/>",
        "delet":"<spring:message code='LABEL_DELETE' text='Delete'/>",       
        "fhours":"<spring:message code='ALERT_SC_FHOURS' text='Please give valid FromHours.'/>",
        "thours":"<spring:message code='ALERT_SC_THOURS' text='Please give valid ToHours.'/>",
        "hours":"<spring:message code='ALERT_SC_HOURS' text='ToHours must be greater than FromHours.'/>",
        "dayRange":"<spring:message code='ALERT_SC_DAYRANGE' text='day range already exist.'/>",
        "scfixedEmpty":"<spring:message code='ALERT_SC_PERC_EMPTY' text='Please enter Service Charge Percentage.'/>",
        "validMaxtxn":"<spring:message code='ALERT_SC_MAXTXNGREATER' text='Max transaction value must be greater than minimum transaction value.'/>",
        "validmaxScv":"<spring:message code='ALERT_SC_MAXSCV_INVALID' text='Maximum Service Charge Value must be greater than Minimum Service Charge value.'/>",
        "maxSCLimit":"<spring:message code='ALERT_MAX_SC_LIMIT' text='Maximum Service Charge Value should not be greater than Maximum Transaction value.'/>",
        "fixedGreater":"<spring:message code='ALERT_SC_FIXED_GREATER' text='Service Charge fixed should not be greater than Maximum Transaction value.'/>",
        "dlimitGreater":"<spring:message code='ALERT_DL_GREATER' text='Discount Limit should not be greater than Maximum Transaction value.'/>",
        "deleteAlert":"<spring:message code='ALERT_DELETE_ROW' text='Are you sure want to delete?'/>",
        "txnType":"<spring:message code='ALERT_SC_TRANSACTIONS_TYPE' text='Transaction type is different,please select same type.'/>",
         "applicableFromDateFormat":"<spring:message code='VALID_APPLICABLE_FROM_DATE_FORMAT' text='The applicable from date format must be : dd/mm/yyyy'/>",
         "applicableToDateFromat":"<spring:message code="VALID_APPLICABLE_TO_DATE_FORMAT" text="The applicable to date format must be : dd/mm/yyyy"/>",
         "applicablevalidFromDay":"<spring:message code="VALID_APPLICABLE_DAY" text="Please enter valid applicable from date"/>",
         "applicablevalidToDay":"<spring:message code="VALID_APPLICABLE_TO_DAY" text="Please  enter  a valid applicable to date"/>",
         "applicablevalidFromMonth":"<spring:message code="VALID_APPLICABLE_FROM_MONTH" text="Please  enter  a valid  month in applicable from date"/>",
         "applicablevalidToMonth":"<spring:message code="VALID_APPLICABLE_TO_MONTH" text="Please  enter  a valid  month in applicable to date"/>",
         "applicablevalidfDay":"<spring:message code="VALID_APPLICABLE_FROM_DAY" text="Please  enter  a valid  days in applicable from date"/>",
         "applicablevalidTodays":"<spring:message code="VALID_APPLICABLE_TO_DAYS" text="Please  enter  a valid  days in applicable to date"/>",
         "applicablevalidFMonth":"<spring:message code="VALID_APPLICABLE_FROM_MONTH" text="Please  enter  a valid  month in applicable from date"/>",
         "applicablevalidToMonth":"<spring:message code="VALID_APPLICABLE_TO_MONTH" text="Please  enter  a valid  month in applicable to date"/>",
         "edit":"<spring:message code='LABEL_EDIT' text='Edit'/>",
 		"delet":"<spring:message code='LABEL_DELETE' text='Delete'/>",
 		"deleteAlert":"<spring:message code='ALERT_DELETE_ROW' text='Are you sure want to delete?'/>",
 		"subscriptionTypeSelect":"<spring:message code='SUBSCRIPTION_TYPE_SELECT' text='Please select subscription type'/>",
 		"costPerTransaction":"<spring:message code='ALERT_COST_PER_TRANSACTION' text='Please add cost per transaction'/>",
 		"costPerPackageValid":"<spring:message code='ALERT_COST_PER_VALID_TRANSACTION' text='Please add valid cost for transaction'/>",
 		"differentSubscriptionType":"<spring:message code='DIFFERENT_SUBSCRIPTION_TYPE' text='Please select different subscription type'/>",
 		"numberOfTransaction":"<spring:message code='ALERT_NUMBER_OF_TRANSACTION' text='Please add valid number of transactions'/>",
 		"SMSRuleExist":"<spring:message code='ERROR_10012' text='SMS package name already exist, please try with another name'/>"
    };
function validate()
    {  
	
	  var ruleLevel = document.getElementsByName('ruleLevel');
      var ruleLevelValue = false;

      for(var i=0; i<ruleLevel.length;i++){
          if(ruleLevel[i].checked == true){
        	  ruleLevelValue = true;    
           }
       }
       if(!ruleLevelValue){
        alert("Please Choose the Rule Type ");
        return false;
      }
        var docF=document.serviceChargeForm;    
        var ruleName=docF.ruleName.value;
        var ruleValue = $("#serviceChargeRuleId").val();
        if(ruleName ==""){
            alert("<spring:message code='NotEmpty.serviceChargeDTO.ruleName' text='Please enter Rule name'/>");
            return false;
        }else if(ruleName.charAt(0) == " " || ruleName.charAt(ruleName.length-1) == " "){
            alert("<spring:message code='ALERT_SC_RULENAME_INVALID' text='Please enter valid Rule name'/>");
            return false;
        }else if(ruleName.length>32){
            alert("<spring:message code='ALERT_SC_RULENAME_INVALID_LENGTH' text='Service Charge Rule Name Should not Exceed More Than 32 Characters'/>");
            return false;
        }else if(docF.applicableFrom.value==""){
            alert("<spring:message code='NotNull.serviceChargeDTO.applicableFrom' text='Please select Applicable from date'/>");
            return false;
        }else if(!validdate(docF.applicableFrom.value,docF.applicableTo.value)){
             return false; 
            
        }else if(!compareDate(getCurrentDate(),docF.applicableFrom.value)){
            alert("<spring:message code='ALERT_INVALID_CURRENT_DATE' text='Applicable from date cannot be less than current date.'/>");
            return false;
        }else if(docF.applicableTo.value==""){
            alert("<spring:message code='NotNull.serviceChargeDTO.applicableTo' text=' Please select Applicable to date'/>");           
            return false;
        }else if(!compareDate(docF.applicableFrom.value,docF.applicableTo.value)){
            alert("<spring:message code='ALERT_SC_DATECOMPARE' text='Applicable to date must be greater than Applicable from date'/>");           
            return false;
        }else if(docF.timeZone.value==""){
            alert("<spring:message code='ALERT_SC_TIMEZONE' text='Please select Time zone'/>");           
            return false;
        }else if(docF.transactions.value==""){
            alert("<spring:message code='NotNull.serviceChargeDTO.transactions' text='Please select Transaction'/>");           
            return false;
        }/* Naqui: Since source type is removed no need for this validation
        else if(!alertSourceType(docF.transactions.value)){           
                alert("<spring:message code='ALERT_SC_SOURCE_TYPE' text='Please select Source Type.'/>");           
                return false;
        } */else{
            writeSCRuleValues();
            writeDayValues(); 
        if(minTxnValue.length==undefined || minTxnValue.length==0){
            alert("<spring:message code='ALERT_SC_RULE_VALUE' text='scRulevalue:'/>");           
            return false;
        }
        if(ruleValue != ""){
        	 if(minTxnValue.length==undefined || minTxnValue.length==1){
                 alert("<spring:message code='ALERT_SC_RULE_VALUE' text='scRulevalue:'/>");           
                 return false;
        	 }
        }
        if(day[0]==undefined || day[0]==""){
	        if(day.length==undefined|| day.length==1 || day.length==0){
	            alert("<spring:message code='ALERT_SC_APPLICABLEDAY' text='applicableDay:'/>");  
	            return false;
	        }
        }else{
        	if(day.length==undefined|| day.length==0){
                alert("<spring:message code='ALERT_SC_APPLICABLEDAY' text='applicableDay:'/>");  
                return false;
            }
        }
        if($("#subscriptionId").val()!=""){
    	alert("<spring:message code='ALERT_COMPLETE_CURRENT_ACTION' text='Please complete current action'/>");
    	 return false;
    	}
        writeSMSRuleValues();
    document.getElementById("transactions").disabled=false;            
    document.serviceChargeForm.method="post";
    document.serviceChargeForm.action="saveServiceChargeRule.htm";
    document.serviceChargeForm.submit();
    }
    }
   
    function alertSourceType(val){
        var docF=document.serviceChargeForm;
        	 if((findType([55,80,83,90,35,82,30,84],val)!=undefined)){           
                 if(docF.sourceType[0].checked==false&docF.sourceType[1].checked==false&docF.sourceType[2].checked==false&docF.sourceType[3].checked==false){
                     return false;
                 }else
                     return true;
             }else
             return true;
    }
   
    function cancelForm(){
        /* document.serviceChargeForm.action="searchServiceChargeRules.htm?ruleLevel=1";
        document.serviceChargeForm.submit(); */
        var rule = 1;
    	var ruleLevel = document.getElementById('ruleLevel').value=rule;
        url = "searchServiceChargeRules.htm";
    	submitlink(url,'serviceChargeForm');
    }
   
    function readValuesFromTxtBox(){
       
        var minTxn = (document.getElementById("minTxnValue").value).split(",");
        var maxTxn = (document.getElementById("maxTxnValue").value).split(",");
        var scPerc = (document.getElementById("scPercentage").value).split(",");
        var scFixed = (document.getElementById("scFixed").value).split(",");
        var dlimit = (document.getElementById("discountLimit").value).split(",");
        var minSc = (document.getElementById("minSC").value).split(",");
        var maxSc = (document.getElementById("maxSC").value).split(",");    
      
        if(minTxn.length!=0){
        for(var i=0;i<minTxn.length;i++){   
        if(minTxn[i]!=""){
        var a = minTxn[i];var b=maxTxn[i];var c=scPerc[i];var d=scFixed[i];    var e = dlimit[i];var f=minSc[i];var g=maxSc[i];
           
        minTxnValue[l]=a; maxTxnValue[l]=b; scPercentage[l]=c;sChargeFixed[l]=d;discountLimit[l]=e; minSCharge[l]=f; maxSCharge[l]=g; l++;

        addTxnRow(a,b,c,d,e,f,g,1,-1);
        }
        }       
        }
        if(sChargeFixed.length!=0)
        document.getElementById("transactions").disabled=true;
        viewHideSCTextField();
        clearTxnField();
       
        var combo=document.getElementById("day");
        var days= (document.getElementById("days").value).split(",");
        var fh = (document.getElementById("fromHours").value).split(",");
        var th = (document.getElementById("toHours").value).split(",");
        if(days.length!=0){
        for(var i=0;i<days.length;i++){   
       
        var dayText = combo.options[days[i]].firstChild.nodeValue ;
       
        var a=days[i];var b=fh[i];var c=th[i];
       
        day[z]=a; from[z]=b; to[z]=c;z++;      
       
        addDayRow(dayText,b,c,1,-1);
       
            }
        }
        clearDayField();
       
        }
  
   
      function scrollUp(id){
    	  if(id==1){
	       $("#serviceChargeRuleDiv").fadeToggle(1);
	       $("#serviceChargeRuleDiv").fadeToggle(2000);
    	  window.scrollTo(0, 0);}
    	  if(id==2){
   	       $("#application_for_days").fadeToggle(1);
   	    $("#application_for_days").fadeToggle(2000);
       	  window.scrollTo(0, 0);
       	  }
    	  if(id==3){
   	       $("#subscriptionChargeDiv").fadeToggle(1);
   	    $("#subscriptionChargeDiv").fadeToggle(2000);
       	  window.scrollTo(0, 0);
       	  }
      }
</script>
<style type="text/css">

input[disabled] {
background-color: #dcdcdc;
border: #3532ff 1px solid;
color: #000000;
cursor: default;
}

</style>
<style>
td{
text-align:center;
}
/*  #paper-bg {
min-height: 1210px;
} */
#paper-bg {
    min-height: 1011px;
}
.col-md-12.container {
    margin-top: 15px;
}
.pull-right.view_size {
    margin-top: 4px;
}
.btn-toolbar {
    margin-left: -5px;
    margin-top: 23px;
}
</style>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>

	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"/>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css"/>
<style>
 #accordion .panel-heading { padding: 0;}
#accordion .panel-title > a {
	display: block;
	padding: 0.4em 0.6em;
    outline: none;
    font-weight:bold;
    text-decoration: none;
}

#accordion .panel-title > a.accordion-toggle::before, #accordion a[data-toggle="collapse"]::before  {

    content:"\2212";
    float: left;
    font-family: 'Glyphicons Halflings';
	margin-right :1em;
}
#accordion .panel-title > a.accordion-toggle.collapsed::before, #accordion a.collapsed[data-toggle="collapse"]::before  {
    content:"\002B";
}
.extend{
    width: 40.00%;
}
</style>

 <form:form name="serviceChargeForm" id="serviceChargeForm" action="saveServiceChargeRule.htm" method="post" commandName="serviceChargeDTO" autocomplete="off">
<jsp:include page="csrf_token.jsp"/>
<div id="shrink">
<div class="box box row col-lg-12">
<div class="box-header" >
        <h3 class="box-title" >
            <span><spring:message code="LABEL_SERVICE_CHARGE_RULE" text="Service Charge Rule"/></span>
        </h3>
   <br/>
    <div class="pull-right view_size">
       <%--  <b><a href="javascript:submitForm('serviceChargeForm','<c:out value="listServiceChargeRules.htm?pageNumber=1"/>')"><spring:message code="LINK_VIEWSCRULE" text="Rule" /> </a> &nbsp; &nbsp; &nbsp; &nbsp;</b> --%>
    	<b><a href="javascript:viewDetails('listServiceChargeRules.htm','1')"><spring:message code="LINK_VIEWSCRULE" text="Rule" /> </a> &nbsp; &nbsp; &nbsp; &nbsp;</b>
    </div></div> 
<div class=" col-md-12 container">
    <div class="row">
        <div class="col-md-12" id="main">
            <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
              <div class="panel panel-default">
                <div class="panel-heading" role="tab" id="headingOne" onclick="javascript:scrollUp(1);">
                  <h4 class="panel-title">
                    <a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                   <spring:message code="LABEL_SERVICE_CHARGE_RULES" text="Service Charge Rules"/>  
                    </a>
                  </h4>
                </div>
                <div id="collapseOne" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingOne">
                  <div class="panel-body" id ="serviceChargeRuleDiv">
                <div class="box first_box">
        <div class="col-md-5 col-md-offset-4">
            <div style="color: #ba0101; font-weight: bold; font-size: 12px;">
                <spring:message code="${message}" text="" />
            </div>
        </div>
      
    <div class="box-body">
    <div style="/*border:1px solid;*/border-radius:10px"><br />
            <div class="row">
            <authz:authorize ifAnyGranted="ROLE_admin,ROLE_bankgroupadmin,ROLE_bankadmin">
                <div class="col-sm-6">
                    <label class="col-sm-4" style="margin-top:4px;">
                        <spring:message code="LABEL_RULE_TYPE" text="Rule Type:" /><font color="red">*</font><br/><br/>
                        <spring:message code="LABEL_RULE_NAME" text="Rule Name" /><font color="red">*</font>
                    </label>
                    <div class="col-sm-5">
                    <form:radiobutton id="ruleLevel" path="ruleLevel" value="1" label="Global"></form:radiobutton>&nbsp; &nbsp;
                    <form:radiobutton id="ruleLevel" path="ruleLevel" value="4" label="InterBank"></form:radiobutton>
                    </div>
                    <div class="col-sm-5">
                    <form:input path="ruleName" cssClass="form-control"></form:input>
                        <font color="red"><form:errors path="ruleName" /></font>
                    </div>
                </div>
                </authz:authorize>
                <authz:authorize ifAnyGranted="ROLE_bankgroupadmin,ROLE_parameter,ROLE_bankadmin">
                    <form:input path="ruleLevel" type="hidden"/>
                    <input type="hidden" id="ruleLevel" name="ruleLevel" value="1">
                </authz:authorize>
                <authz:authorize ifAnyGranted="ROLE_parameter">                                                                   
                    <c:if test="${ serviceChargeDTO.ruleLevel == 1 or  serviceChargeDTO.ruleLevel == 2 }">
                        <c:set var="style" value="display:none"/>
                    </c:if>
                    <div class="col-sm-6" id="profiles" style="${style}">
                    <label class="col-sm-5" style="margin-top:4px;">
                    <spring:message code="LABEL_SELECT_PROFILE" text="Customer Profile" /><font color="red">*</font>
                    <form:select path="profileId" id="profileId" items="${customerProfileList }" itemLabel="profileName" itemValue="profileId" cssClass="dropdown"/>
                    </label>
                    </div></authz:authorize>
                <div class="col-sm-6">
                    <label class="col-sm-5"><spring:message code="LABEL_TRANSACTION_TYPE" text="Transaction Type" /><font color="red">*</font></label>
                    <select  id="transactions" class="col-sm-4 extend" name="transactions"  multiple="multiple" class="multiple" style="padding-left:0px;"  onchange="viewHideSCTextField();">
                    <option value="" disabled="disabled"><spring:message     code="LABEL_SELECT" text="--Please Select--" /></option>
                    <c:set var="lang" value="${language}"></c:set>
                    <c:forEach items="${masterData.transTypeList}" var="transTypeList" >
                   
                    <c:forEach items="${transTypeList.transactionTypesDescs}" var="transactionTypesDescs" varStatus="cnt">
                     <c:if test="${transactionTypesDescs.comp_id.locale==lang}">
                    <c:set value="f" var="sel"/>
                    <c:forEach items="${serviceChargeDTO.transactions}" var="arrVal">
                     <c:if test="${transTypeList.transactionType eq arrVal}"><c:set value="true" var="sel"/></c:if>
                    </c:forEach>
                   
                    <authz:authorize ifAnyGranted="ROLE_admin,ROLE_bankgroupadmin">   
                     <c:if test="${transTypeList.transactionType!=84 }">
                     <option value="<c:out value="${transTypeList.transactionType}"/>" <c:if test="${sel}"> selected=true</c:if> >
                    <c:out     value="${transactionTypesDescs.description}"/>   
                    </option>
                      </c:if>
                    </authz:authorize>
                   
                    <authz:authorize ifNotGranted="ROLE_admin,ROLE_bankgroupadmin">   
                    <c:if test="${transTypeList.transactionType!=84 }">
                     <option value="<c:out value="${transTypeList.transactionType}"/>" <c:if test="${sel}"> selected=true</c:if> >
                    <c:out     value="${transactionTypesDescs.description}"/>   
                    </option>
                    </c:if>
                    </authz:authorize>
                    </c:if>                                 
                </c:forEach>
                </c:forEach>
   
             </select>
        </div>
            </div>
            <div class="row">
               
            </div>
            <div class="row">
                <div class="col-sm-6">
                    <label class="col-sm-4" style="margin-top:4px;"><spring:message code="LABEL_APPLICABLE_FROM" text="Applicable From" /><font color="red">*</font></label>
                    <div class="col-sm-5">
                    <form:input path="applicableFrom"  cssClass="form-control datepicker " onClick="check()"></form:input>
                    <font color="red"> <form:errors path="applicableFrom" /></font>
                    </div>
                </div>
               <%--  <div class="col-sm-6"  id="divImpose1" style="display:none;">
                    <label class="col-sm-4" style="margin-top:4px;"><spring:message code="LABEL_SOURCE_TYPE" text="Source Type" /><font color="red">*</font></label>
                    <div class="col-sm-7">
                         <form:checkbox path="sourceType" value="1"/>&nbsp; &nbsp;<spring:message code="LABEL_SOURCE_WALLET" text="Wallet" /> &nbsp;
                         <form:checkbox path="sourceType" value="2" />&nbsp; &nbsp;<spring:message code="LABEL_SOURCE_CARD" text="Card" />  &nbsp;
                         <form:checkbox path="sourceType" value="4" />&nbsp; &nbsp;<spring:message code="LABEL_SOURCE_FI" text="FI" />  <br/>
                         <form:checkbox path="sourceType" value="3"/>&nbsp; &nbsp;<spring:message code="LABEL_SOURCE_BANKACC" text="Bank Account" /> &nbsp;
                         <form:checkbox path="sourceType" value="0"/>&nbsp; &nbsp;<spring:message code="LABEL_SOURCE_OTHERS" text="Others" />   
                    </div>
                </div> --%>
            </div>
            <div class="row">
                <div class="col-sm-6">
                    <label class="col-sm-4"><spring:message code="LABEL_APPLICABLE_TO" text="Applicable To:" /><font color="red">*</font></label>
                    <div class="col-sm-5">
                    <form:input path="applicableTo" cssClass="form-control datepicker" onClick="check1()"></form:input>
                    <font color="red"><form:errors path="applicableTo" /></font>
                    </div>
                </div>
                <div class="col-sm-6" id="divImpose" style="display:none;">
                    <label class="col-sm-4"><spring:message code="LABEL_IMPOSED_ON" text="Imposed On:" /><font color="red">*</font></label>
                    <div class="col-sm-6">
                    <form:radiobutton path="imposedOn" value="0" checked="true" />
                    <spring:message code="LABEL_CUSTOMER" text="customer" />&nbsp; &nbsp;
                    <form:radiobutton path="imposedOn" value="1" />
                    <spring:message code="LABEL_SELECT_BANK_OTHER_PARTY" text="Other Party"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-6">
                    <label class="col-sm-4" style="margin-top:4px;"><spring:message code="LABEL_TIME_ZONE" text="Time Zone:" /><font color="red">*</font></label>
                    <div class="col-sm-6">
                    <form:select path="timeZoneId" cssClass="dropdown chosen-select" id="timeZone" style="width:171px;">
                    <form:option value="" disabled="true">
                        <spring:message code="LABEL_SELECT" text="select:" />
                    </form:option>
                    <form:options items="${masterData.timeZoneList}"
                        itemLabel="timeZoneDesc" itemValue="timeZoneId" />
                </form:select>
            <font color="red"> <form:errors path="timeZoneId" /></font>
            </div>
                </div>
            </div>
    </div>
</div>
            <div class="box-body table responsive">
            <table style="border:1px solid rgba(208, 204, 204, 0.74); width:100%;height:70px;">
            <tr>
                <th><spring:message code="LABEL_TH_MIN_TXNVALUE" text="minTxnValue:" /><font color="red">*</font></th>
                <th><spring:message code="LABEL_TH_MAX_TXNVALUE" text="maxTxnValue:" /><font color="red">*</font></th>
                <th><spring:message code="LABEL_TH_SC_PERCENTAGE" text="SCPercentage:" /><font color="red">*</font></th>
                <th><spring:message code="LABEL_TH_SC_FIXED" text="SCFixed:" /><font color="red">*</font></th>
                <th><spring:message code="LABEL_TH_DISCOUNT_LIMIT" text="DiscountLimit:" /><font color="red">*</font></th>
                <th><spring:message code="LABEL_TH_MIN_SC" text="MinSC:" /><font color="red">*</font></th>
                <th><spring:message code="LABEL_TH_MAX_SC" text="MaxSC:" /><font color="red">*</font></th>
                <th></th>
            </tr>   
           
            <tr >
                 <td><input name="minTxn" type="text" class="medium_text_feild" id="minTxn" size="7"/></td>
                <td><input name="maxTxn" type="text" class="medium_text_feild" id="maxTxn"  size="7"/></td>
                <td><input name="scPerc" type="text" class="small_text_feild" id="scPerc"  size="7"/></td>
                <td><input name="sChargeFixed" type="text" class="small_text_feild" id="sChargeFixed"  size="7"/></td>
                <td><input name="dLimit" type="text" class="small_text_feild" id="dLimit"  size="7"/></td>
                <td><input name="minSCharge" type="text" class="small_text_feild" id="minSCharge"  size="7"/></td>
                <td><input name="maxSCharge" type="text" class="small_text_feild" id="maxSCharge"  size="7"/></td>
                 <td>
                <div id="txnAddDiv">
                    <input type="button" id="addTxn" size="7" value="<spring:message code="BUTTON_ADD" text="add:"/>" onclick="addTxnDetails(<c:out value="${serviceChargeDTO.serviceChargeRuleId}"/>);" />
                </div>
                <div id="txnEditDiv" style="display: none;">
                    <input type="button" id="updateTxn"     value="<spring:message code="LABEL_UPDATE" text="Update:"/>" onclick="updateTxnRows()" />
                </div>
             </td>
            </tr>
            </table>
        <div style="height: 150px; overflow: scroll;border:1px solid rgba(208, 204, 204, 0.74);">
        <table style="width:100%;" cellspacing="0" cellpadding="0"  id="tblGrid">
        <tbody>
        <tr bgColor="#d2d3f1">
        <th width="8%" height="25px"><spring:message code="LABEL_TH_MIN_TXNVALUE" text="minTxnValue:" /></th>
        <th width="8%" height="25px"><spring:message code="LABEL_TH_MAX_TXNVALUE" text="maxTxnValue:" /></th>
        <th width="8%" height="25px"><spring:message code="LABEL_TH_SC_PERCENTAGE" text="SCPercentage:" /></th>
        <th width="8%" height="25px"><spring:message code="LABEL_TH_SC_FIXED" text="SCFixed:" /></th>
        <th width="8%" height="25px"><spring:message code="LABEL_TH_DISCOUNT_LIMIT" text="DiscountLimit:" /></th>
        <th width="8%" height="25px"><spring:message code="LABEL_TH_MIN_SC" text="MinSC:" /></th>
        <th width="8%" height="25px"><spring:message code="LABEL_TH_MAX_SC" text="MaxSC:" /></th>
        <th width="10%" height="25px"><spring:message code="LABEL_ACTION" text="action:" /></th>
        </tr>
       
        <c:if test="${(serviceChargeDTO.serviceChargeRuleId != null) }">
                <c:forEach items="${serviceChargeDTO.scRuleValue}"
                    var="scValue" varStatus="statusIndex">
                    <c:if test="${(statusIndex.index) % 2 == 0}">
                    <c:set var="bgcolor" value="#F5F5F5"></c:set>
                    </c:if>
                    <c:if test="${(statusIndex.index) % 2 != 0}">
                    <c:set var="bgcolor" value=""></c:set>
                    </c:if>
                    <tr bgColor="${ bgcolor}">
                    
                        <td align="center" width="17%" height="25px"><c:out
                                value="${scValue.minTxnValue}"></c:out>
                        </td>
                        <td align="center" width="18%"><c:out
                                value="${scValue.maxTxnValue}"></c:out>
                        </td>
                        <td align="center" width="12%"><c:out
                                value="${scValue.serviceChargePct}"></c:out>
                        </td>
                        <td align="center" width="10%"><c:out
                                value="${scValue.serviceChargeFxd}"></c:out>
                        </td>
                        <td align="center" width="16%"><c:out
                                value="${scValue.discountLimit}"></c:out>
                        </td>
                        <td align="center" width="8%"><c:out
                                value="${scValue.minServiceCharge}"></c:out>
                        </td>
                        <td align="center" width="9%"><c:out
                                value="${scValue.maxServiceCharge}"></c:out>
                        </td>
                        <td align="center" width="15%"><a
                            onclick="txnEditDays();" style="cursor: pointer"><spring:message
                                    code="LABEL_EDIT" text="Edit:" />
                        </a> | <a onclick="deleteTxnRowss();"
                            style="cursor: pointer"><spring:message
                                    code="LABEL_DELETE" text="delete:" />
                        </a></td>
                    </tr>
                </c:forEach>

            </c:if>
        </tbody>
        </table>
       
        </div>
        </div>
       
    </div>
                </div>
                </div>
              </div>
                   
              <div class="panel panel-default">
                <div class="panel-heading" role="tab" id="headingTwo" onclick="javascript:scrollUp(2);">
                  <h4 class="panel-title">
                    <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
                   <spring:message code="LABEL_APPLICATION_FOR_DAYS" text="Applicable for Days"/> 
                    </a>
                  </h4>
                </div>
            
                <div id="collapseTwo" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingTwo">
                  <div class="panel-body" id="application_for_days">
                  <div class="box-body table responsive second_box">
    <table style="border-collapse: collapse;border:1px solid rgba(208, 204, 204, 0.74); width:100%;height:70px;">
    <tr>
    <th style="text-align:center;" height="25px"><spring:message code="LABEL_SELECT_DAY" text="Select Day:" /><font color="red">*</font></th>
    <th style="text-align:left;color:grey" width="10%" height="25px">
    <select name="day" class="dropdown " id="day" width="150px !important;">
        <option value=""> <spring:message code="LABEL_SELECT" text="select:" /> </option>
        <option value="1"> <spring:message code="LABEL_SELECT_SUNDAY" text="sunday:" /> </option>
        <option value="2"> <spring:message code="LABEL_SELECT_MONDAY" text="monday:" /> </option>
        <option value="3"> <spring:message code="LABEL_SELECT_TUESDAY" text="tuesday:" /> </option>
        <option value="4"> <spring:message code="LABEL_SELECT_WEDNESDAY" text="wednesday:" /> </option>
        <option value="5"> <spring:message code="LABEL_SELECT_THURSDAY" text="thursday:" /> </option>
        <option value="6"> <spring:message code="LABEL_SELECT_FRIDAY" text="friday:" /> </option>
        <option value="7"> <spring:message code="LABEL_SELECT_SATURDAY" text="saturday:" /> </option>
        <option value="8"> <spring:message code="LABEL_SELECT_ALLDAY" text="allDays:" /> </option>
    </select>
    
    <FONT color="red"><form:errors path="days" /></FONT>
    </th>
    <th style="text-align:center;" height="25px"><spring:message code="LABEL_TH_FHOURS" text="FromHours:" /> (HH)<font color="red">*</font>&nbsp;&nbsp;</th>
    <th style="text-align:left;" height="25px"><input type="text" name="fhours" class="small_text_feild" id="fhours" maxlength="2" size="8"/>
    <FONT color="red"><form:errors path="fromHours" /></FONT></th>
    <th style="text-align:center;" height="25px"><spring:message code="LABEL_TH_TOHOURS" text="ToHours:" /> (HH)<font color="red">*</font>&nbsp;&nbsp; </th>
    <th style="text-align:left;" height="25px"><input type="text" name="thours" class="small_text_feild" id="thours" maxlength="2" size="8"/>
    <FONT color="red"><form:errors path="toHours" /></FONT></th>
    <th style="text-align:center;" height="25px">
    <div id="dayAddDiv">
        <input type="button" id="add"
            value="<spring:message code="BUTTON_ADD" text="add:"/>"
            onclick="addDays(<c:out value="${serviceChargeDTO.serviceChargeRuleId}"/>);" />
    </div>
    <div id="dayEditDiv" style="display: none;">
        <input type="button" id="update"
            value="<spring:message code="LABEL_UPDATE" text="Update:"/>"
            onclick="updateDayRow()" />
    </div></th>
    </tr>
    </table>
    <div style="height: 150px; overflow: scroll;border:1px solid rgba(208, 204, 204, 0.74);">
        <table style="width:100%;" cellspacing="0" cellpadding="0" id="tblGrid1">
        <tbody>
        <tr bgColor="#d2d3f1">
        <th width="15%" height="25px" style="text-align:center;"><spring:message code="LABEL_TH_DAYS" text="day:" /></th>
        <th width="15%" height="25px" style="text-align:center;"><spring:message code="LABEL_TH_FHOURS" text="FromHours:" /></th>
        <th width="15%" height="25px" style="text-align:center;"><spring:message code="LABEL_TH_TOHOURS" text="ToHours:" /> </th>
        <th width="15%" height="25px" style="text-align:center;"><spring:message code="LABEL_ACTION" text="action:" /></th>
        </tr>
        <c:if test="${(serviceChargeDTO.serviceChargeRuleId != null) }">
            <c:forEach items="${serviceChargeDTO.scDays}" var="sc" varStatus="statusIndex">
             <c:if test="${(statusIndex.index) % 2 == 0}">
                    <c:set var="bgcolor" value="#F5F5F5"></c:set>
                    </c:if>
                    <c:if test="${(statusIndex.index) % 2 != 0}">
                    <c:set var="bgcolor" value=""></c:set>
                    </c:if>
                 <tr bgColor="${ bgcolor}">
                    <td align="center" width="16%" height="25px">
                   
                    <c:choose>
                    <c:when test="${sc.day==1}"><spring:message code="LABEL_SELECT_SUNDAY" text="sunday:" /></c:when>
                    <c:when test="${sc.day==2}"><spring:message code="LABEL_SELECT_MONDAY" text="monday:" /></c:when>
                    <c:when test="${sc.day==3}"><spring:message code="LABEL_SELECT_TUESDAY" text="tuesday:" /> </c:when>
                    <c:when test="${sc.day==4}"><spring:message code="LABEL_SELECT_WEDNESDAY" text="wednesday:" /></c:when>
                    <c:when test="${sc.day==5}"><spring:message code="LABEL_SELECT_THURSDAY" text="thursday:" /></c:when>
                    <c:when test="${sc.day==6}"><spring:message code="LABEL_SELECT_FRIDAY" text="friday:" /></c:when>
                    <c:when test="${sc.day==7}"><spring:message code="LABEL_SELECT_SATURDAY" text="saturday:" /></c:when>                                                                               
                    </c:choose>
                    <div style="display: none">
                    <c:out value="${sc.day}"></c:out>
                    </div>
                   
                    </td>
                    <td align="center" width="35%"><c:out value="${sc.fromhh}"></c:out> </td>
                    <td align="center" width="35%"><c:out value="${sc.tohh}"></c:out> </td>
                    <td align="center" width="14%">
                        <a onclick="dayEdit();" style="cursor: pointer"> <spring:message code="LABEL_EDIT" text="Edit:" /> </a> |
                        <a onclick="deleteDayRow();" style="cursor: pointer"><spring:message code="LABEL_DELETE" text="delete:" /> </a>
                    </td>
                </tr>
               
            </c:forEach>
        </c:if>
        </tbody>
        </table>
    <input id="dindex" type="hidden" name="dindex" />
    <form:input path="days" type="hidden"></form:input>
    <form:input path="fromHours" type="hidden"></form:input>
    <form:input path="toHours" type="hidden"></form:input>
     <input id="pageNumber" type="hidden" name="pageNumber" />
    <c:if test="${(serviceChargeDTO.serviceChargeRuleId != null)&& message==null }">
    <script>setSCRuleArraysByTableData();
    setDayArraysByTableData();
    			check();check1();    </script>
    </c:if>
    <c:if test="${(serviceChargeDTO.serviceChargeRuleId == null) }">
    <script>check();check1();    </script>
    </c:if>
    <c:if test="${(message!=null) }">        
    <script>readValuesFromTxtBox();</script>
    </c:if>
    </div>
    
                  </div>
                </div>
              </div>
              </div>
              <div class="panel panel-default">
                <div class="panel-heading" role="tab" id="headingThree" onclick="javascript:scrollUp(3);">
                  <h4 class="panel-title">
                    <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseThree" aria-expanded="false" aria-controls="collapseThree">
                      Subscription Charge(Optional)
                    </a>
                  </h4>
                </div>
                <div id="collapseThree" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingThree">
                  <div class="panel-body" id="subscriptionChargeDiv">

    <div class="box-body table responsive third_box">
			 <div class="box-body table responsive">
			<table style="border:1px solid rgba(208, 204, 204, 0.74); width:100%;height:70px;">
			<tr>
				<th style="text-align:center;"><spring:message code="SUBSCRIPTION_TYPE" text="Subscription type"/><font color="red">*</font></th>
				<th style="text-align:center;" ><spring:message code="COST_FOR_TRANSACTION" text="Cost for Transaction"/><font color="red">*</font></th>
				<th style="text-align:center;"><spring:message code="NUMBER_OF_TRANSACTION" text="Number of Transaction"/><font color="red">*</font></th>
				<th></th>
			</tr>	
			
			<tr >
				<td style="text-align:center;">
				<select name="subscriptionType" class="dropdown" id="subscriptionId">
						<option value="" selected="true"><spring:message code="LABEL_SELECT" text="select:"/></option>
						<option value="1"><spring:message code="LABEL_SELECT_DAILY" text="Daily"/></option>
						<option value="2"><spring:message code="LABEL_SELECT_WEEKLY" text="Weekly"/></option>
						<option value="3"><spring:message code="LABEL_SELECT_MONTHLY" text="Monthly"/></option>
						<option value="4"><spring:message code="LABEL_SELECT_YEARLY" text="Yearly"/></option>
					</select>
				</td>
				<td style="text-align:center;"><input name="costPerPackage" id="costPerPackage" type="text" class="small_text_feild"  maxlength="10" autocomplete="off"/></td>
				<td style="text-align:center;"><input name="noOfTxn" id="noOfTxn" type="text" class="small_text_feild"  maxlength="6" autocomplete="off"/></td>
				<td style="text-align:center;"><div id="txnAddDiv"><input type="button" id="addTxn" value="<spring:message code="BUTTON_ADD" text="add:"/>" onclick="addTxnDetailss(<c:out value="${serviceChargeDTO.subscriptionChargeRuleId}"/>);"/></div>
	            <div id="txnEditDiv" style="display: none;"> <input type="button" id="updateTxn"  value="<spring:message code="LABEL_UPDATE" text="Update:"/>" onclick="updateTxnRow()" /></div>
			 </td>
			</tr>
			</table>
		
		<input name="subscription" id="subscription" type="hidden"/>
		<table style="width:100%;" cellspacing="0" cellpadding="0">
		<thead>
		<tr bgColor="#d2d3f1">
		<th width="8%" height="25px" style="text-align:center;"><spring:message code="SUBSCRIPTION_TYPE" text="Subscription type"/><font color="red">*</font></th>
		<th width="8%" height="25px" style="text-align:center;"><spring:message code="COST_FOR_TRANSACTION" text="Cost for transaction"/><font color="red">*</font></th>
		<th width="8%" height="25px" style="text-align:center;"><spring:message code="NUMBER_OF_TRANSACTIION" text="Number of transaction"/><font color="red">*</font></th>
		<th width="8%" height="25px" style="text-align:center;"><spring:message code="LABEL_ACTION" text="action:"/></th>
		</tr>
		</thead>
		<tr>
		<td colspan="8" align="center">
			<div style="height: 150px; overflow: scroll;border:1px solid rgba(208, 204, 204, 0.74);">
			<table id="subsTableGrid" width="100%" border="0" cellspacing="0"
				cellpadding="0">
				<tbody>
	  <%-- 	<c:if test="${(serviceChargeDTO.scSubscriptions.scsubscriptionId != null) }">   --%>
	       <c:forEach items="${serviceChargeDTO.scSubscriptions}" var="trs" varStatus="theCount">
	       <c:if test="${(theCount.index) % 2 == 0}">
                    <c:set var="bgcolor" value="#F5F5F5"></c:set>
                    </c:if>
                    <c:if test="${(theCount.index) % 2 != 0}">
                    <c:set var="bgcolor" value=""></c:set>
                    </c:if>
					<tr id="smsTxnDetails_${theCount.count-1}" bgColor="${ bgcolor}">
						<td align="center" width="8%" height="25px" class="subscriptionType"><c:choose> 
										<c:when test="${trs.subscriptionType==1}">Daily</c:when>
										<c:when test="${trs.subscriptionType==2}">Weekly</c:when>
										<c:when test="${trs.subscriptionType==3}">Monthly</c:when>
										<c:otherwise>Yearly</c:otherwise>
										</c:choose><div style="display: none"><c:out value="${trs.subscriptionType}"></c:out></div>
						</td>
						<td align="center" width="8%" class="costPerPackage"><c:out value="${trs.costPerSubscription}"></c:out>
						</td>
						<td align="center" width="8%" class="noOfTxn"><c:out value="${trs.numberOfTxn}"></c:out>
						</td>
						<td align="center" width="8%">
						<a onclick="txnEdit(this);" style="cursor:pointer"><spring:message code="LABEL_EDIT" text="Edit:"/></a> | 
						<a onclick="deleteTxnRow(this);" style="cursor:pointer"><spring:message code="LABEL_DELETE" text="delete:"/></a>
						</td>
					</tr>
					
				</c:forEach>

			<%-- </c:if>   --%>
		</tbody>
		</table></div></td></tr>
		</table>
		</div>
		</div>
    </div>                  </div>
                </div>
              </div>
         
            
              </div>
      <!-- //@Start, changed class purpose bug 5682, by Murari, dated : 20-07-2018 -->    
 <div class="col-md-3 col-md-offset-10">
      <!--  end -->
        <div class="btn-toolbar">
        <input type="button" class="btn-primary btn" value="<spring:message code="BUTTON_SUBMIT" text="submit"/>" onclick="validate();" />
        <input type="button"  class="btn-primary btn" value="<spring:message code="LABEL_CANCEL" text="Cancel"/>" onclick="cancelForm();" />
        </div>
    </div>
     </div>
</div>       
</div>
 <input id="tindex" type="hidden" name="tindex" value="" />
				   <form:input path="serviceChargeRuleId" type="hidden"></form:input>
				   <form:input path="minTxnValue" type="hidden"></form:input>
				   <form:input path="maxTxnValue" type="hidden"></form:input>
				   <form:input path="scPercentage" type="hidden"></form:input>
				   <form:input path="scFixed" type="hidden"></form:input>
				   <form:input path="discountLimit" type="hidden"></form:input>
				   <form:input path="minSC" type="hidden"></form:input>
				   <form:input path="maxSC" type="hidden"></form:input>
				   </div>
</form:form>