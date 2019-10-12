<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="authz"
	uri="http://www.springframework.org/security/tags"%>
	
	
<style>
ul#nav-tabs-wrapper>li.active>a {
    color: #fff;
    background-color:#3e4e68;
}

.nav>li>a:hover, .nav>li>a:focus, .nav .open > a, .nav .open > a:hover, .nav .open > a:focus {
    background-color: #3e4e68!important;
    color: #fff!important;
}
ul.header_menu>li {
    display: block;
    font-size: 11px;
    color: #666677;
    font-weight: bold;
    margin-top: 5px;
    margin-bottom: -7px;
}

li.col-sm-2 {
    text-align: center;
}
ul.left_side>li {
    display: block;
    font-size: 16px;
    color: #000;
}
.content_field.col-sm-10 {
    border-bottom: 1px solid rgba(215, 213, 213, 0.33);
	width: 100%;
}
ul.resize>li {
    display: flex;
    margin-top: 1px;
}
ul.left_side {
    border: 1px solid #30314f;
    box-shadow: 0px 0px 3px 0px;
	margin-right: 23px;
    background: #d2d3f1;
}
ul.left_side>li {
    margin-top: 15px;
    margin-bottom: 10px;
}
.col-lg-12.table_box {
    margin-top: -4px;
    padding: 13px;
}

ul.left_side>li {
    margin-top: 15px;
    margin-bottom: 10px;
}

ul.resize>li {
    display: flex;
    margin-top: 4px;
}
.button_1, .button_2, .button_3, .button_4{
	display:none;
}
li.col-sm-2 {
    text-align: center;
}

.left_right_content.col-lg-12 {
    box-shadow: 0px 0px 5px 0px #d2d3f1;
    padding: 6px;
    margin-top: 10px;
    height: 239px;
}
.col-lg-12.content_text {
    box-shadow: 0px 0px 5px 0px green;
    padding: 6px;
    margin-top: 10px;
}
.content_field_right{
	
	width: 100%;
} 
.content_field_right1{
	display:none;
	width: 100%;
}
.scrolling_text.col-sm-10.col-lg-10 {
    width: 83%;
    height: 215px;
  
}
ul.left_side>li {
    margin-left: -40px;
    border-bottom:1px dotted rgba(224, 220, 220, 0.99);
    text-align: center;
}
.col-sm-2.textbox1_size {
    margin-left: -20px;
}
span.no_align1 {
    text-align: center;
    font-size: 14px;
    margin-left: 46px;
}
span.no_align2 {
    text-align: center;
    margin-left: 70px;
    font-size: 14px;
}
ul.resize {
    margin-left: 54px;
    margin-top: -10px;
}

/* Hover tooltips */
.field-tip {
    position:absolute;
    cursor:help;
    width:0px;
}
.field-tip .tip-content {
    position: absolute;
    top: -22px;
    right: 9999px;
    width: 142px;
    height: 77px;
    font-size: 12px;
    margin-right: -274px;
    padding: 6px;
    color: #666677;
    border-radius: 16px;
    background: #3E4E68;
    -moz-box-shadow: 2px 2px 5px #aaa;
    box-shadow: 2px 2px 5px #aaa;
    opacity: 0;
    -webkit-transition: opacity 250ms ease-out;
    -moz-transition: opacity 250ms ease-out;
    -ms-transition: opacity 250ms ease-out;
    -o-transition: opacity 250ms ease-out;
    transition: opacity 250ms ease-out;
}
        /* <http://css-tricks.com/snippets/css/css-triangle/> */
        .field-tip .tip-content:before {
            content:' '; /* Must have content to display */
            position:absolute;
            top:29%;
            left:-16px; /* 2 x border width */
            width:0;
            height:0;
            margin-top:-8px; /* - border width */
            border:8px solid transparent;
            border-right-color:#3E4E68;
        }
        .field-tip:hover .tip-content {
            right:-20px;
            opacity:1;
        }
.checkbox-inline, .radio-inline {
	font-size: 14px;
}

 .nav>li>a {
    position: relative;
    display: block;
    padding: 8px 10px;
}

li.col-sm-2.sub_bar {
    margin-right: 25px;
    margin-left: -25px;
}

ul#nav-tabs-wrapper>li>a {
    color: #666677;
    
}
ul#nav-tabs-wrapper>li::selection>a {
    color: #fff;
    background-color:#3e4e68;
}


.nav-tabs>li>a {
    margin-right: 0px;
}
ul#nav-tabs-wrapper>li {
    background-color: #d2d3f1;
    border-radius: 5px;
}

.text_align{
	  float: right;
    font-size: 11px;
    font-weight: 600;
    margin-right: 32px;
}

.content_field {
    border-bottom: 1px solid rgba(215, 213, 213, 0.33);
    width: 100%;
    padding-bottom: 0px;
}
.menu_list.co-lg-12 {
    border-style: solid;
    background-color: #d2d3f1;
    height: 30px;
}
.content_field_right {
    width: 100%;
    margin-top: -9px;
}
.well {
    min-height: 20px;
    padding: 4px;
    height: 225px;
    overflow: scroll;
    width: 131px;
    margin-left: -9px;
}
.col-lg-12.table_box {
    margin-top: -4px;
    padding: 13px;
    width: 83%;
    margin-left: 81px;
}
li.col-sm-3.sub_bar1 {
    margin-left: 1px;
}
li.col-sm-3.sub_bar2 {
    margin-left: -16px;
}
li.col-sm-2.sub_bar3 {
    margin-left: -43px;
}
li.col-sm-2.sub_bar4 {
    margin-left: 9px;
}
.alignment_size {
    margin-top: 19px;
    margin-bottom: 7px;
}
.tooltip_templates {
 display: none; 
}
</style>
<body>
<form:form commandName="smsAlertDTO" method="post" action="getCurrentSubcription.htm" name="processSmsSubscriptionForm" id="processSmsSubscriptionForm">
   <jsp:include page="csrf_token.jsp"/>
   <c:if test="${currentSubscription.customerSmsRuleDetailId ne null}">
  <div class="text_align">
   <a href="#" onclick="onSubmit();"><spring:message code="LABEL_CURRENT_SUBSCRIPTION" text="Current Subscription" /></a>
   </div>
   </c:if>
   
    <div class="container-fluid">
		<div class="col-lg-12 table_box">
			<div class="menu_list co-lg-12">
			<div class="row">
				<ul class="header_menu">
					<li class="col-sm-2 sub_bar"><spring:message code="LABEL_SUBSCRIPTION" text="Subscription" /></li>
					<li class="col-sm-3 sub_bar1"><spring:message code="SUBSCRIPTION_TYPE" text="Subscription type" /></li>
					<li class="col-sm-3 sub_bar2"><spring:message code="LABEL_SUBSCRIPTION_COST" text="Subscription Cost" /></li>
					<li class="col-sm-2 sub_bar3"><spring:message code="LABEL_NUMBER_OF_SMS" text="No. of SMS" /></li>
					<li class="col-sm-2"><spring:message code="LABEL_ACTION" text="Action" /></li>
				</ul>
			</div>
			</div>
				<div class="left_right_content col-lg-12">
					<div class="col-sm-2">
						<ul id="nav-tabs-wrapper" class="nav nav-tabs nav-pills nav-stacked well">
						<c:forEach items="${SmsPackageList}" var="packageList">
						<!-- date:05/08/16 by:rajlaxmi for:showing only subscribed sms packages details -->
						<li class="pkg<c:out value="${packageList.smsalertrule.smsAlertRuleId } " /> enableTooltip" data-toggle="tab" data-tooltip-content="#pkgDiscription<c:out value="${packageList.smsalertrule.smsAlertRuleId }"/>" onmouseover="enableTooltipss();" onclick="smsPackageDetails('<c:out value="${packageList.smsalertrule.smsAlertRuleId}"/>','<c:out value="${customerId}"/>')">
						<a ><c:out value="${packageList.smsalertrule.smsAlertRuleName }"></c:out></a>
										<div class="tooltip_templates">
										<span id="pkgDiscription<c:out value="${packageList.smsalertrule.smsAlertRuleId }"/>" style="color:#0d0d0d;">
										<c:forEach var="packageFeature" items="${packageList.smsalertrule.smsalertrulestxns }">
											<c:out value="${packageFeature.transactionType.description}"></c:out><br />
										</c:forEach>
									</span>
								</div>
							</li><br/>

							</c:forEach>
						</ul>
					</div>
					<!------------------------------------------->
					<div id="descData"></div>
					<!----------------------------------------------------->
				</div>
				        
		</div>
	</div>
	</form:form>
</body>

