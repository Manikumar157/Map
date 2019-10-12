<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
	<script src="<%=request.getContextPath()%>/js/bootstrap.min.js"></script>
	
<%-- <link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.min.css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap-theme.min.css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/font-awesome.min.css"></link> --%>
<style>

.well {
    min-height: 20px;
    padding: 4px;
    height: 225px;
    overflow: scroll;
    width: 131px;
    margin-left: -9px;
}
.nav-pills>li.active>a, .nav-pills>li.active>a:focus, .nav-pills>li.active>a:hover {
    color: #fff;
    background-color: #30314f;
}
 .nav>li>a {
    position: relative;
    display: block;
    padding: 8px 10px;
}
.nav>li>a:focus, .nav>li>a:hover {
    text-decoration: none;
    background-color: #337ab7;
    color: #fff;
}
ul#nav-tabs-wrapper>li>a {
    color: #666677;
}
.nav-tabs>li>a {
    margin-right: 0px;
}
ul#nav-tabs-wrapper>li {
    background-color: #d2d3f1;
    border-radius: 5px;
}
.nav-pills>li.active>a, .nav-pills>li.active>a:focus, .nav-pills>li.active>a:hover {
	color: #fff;
	background-color: #337ab7;
}
ul#nav-tabs-wrapper>li:focus {
    background-color: #337ab7;
    border-radius: 5px;
}
</style>

<div style="color: #ba0101; font-weight: bold; font-size: 12px; text-align: center">
	<spring:message code="${message}" text="" />
</div>
<form><jsp:include page="csrf_token.jsp"/>
</form>
<div class="scrolling_text col-sm-10 col-lg-10">
<c:forEach items="${SCSubscriptionDTO }" var="packageDetails">
		<div class="packageFeatureSet">
	
				<div class="content_field_right col-sm-10 col-lg-10">
					<div class="content_field right_content1 col-sm-10 col-lg-10">
						<div class="col-sm-3 textbox1_size alignment_size">
							<label class="radio-inline"> 
							<input type="radio" name="selectRdoPackage"
								id="selectRdoPackage<c:out value="${packageDetails.subscriptionType}"></c:out>"
								class="selectPackage<c:out value="${packageDetails.subscriptionType}"></c:out>"
								value="<c:out value="${packageDetails.subscriptionType}"></c:out>">
								<c:if test="${packageDetails.subscriptionType == 1}"><spring:message code="LABEL_DAILY" text="Daily" />
										
									</c:if> <c:if test="${packageDetails.subscriptionType == 2}"><spring:message code="LABEL_WEEKLY" text="Weekly" />
										
									</c:if> <c:if test="${packageDetails.subscriptionType == 3}"><spring:message code="LABEL_MONTHLY" text="Monthly" />
										
									</c:if> <c:if test="${packageDetails.subscriptionType == 4}">
										<spring:message code="LABEL_YEARLY" text="Yearly" />
									</c:if>
							</label>
						</div>
						<div class="col-sm-3 alignment_size">
							<span class="no_align1">
								<c:out value="${packageDetails.costPerSubscription}"></c:out>
							</span>
						</div>
						<div class="col-sm-2 alignment_size">
							<span class="no_align2">
								<c:out value="${packageDetails.numberOfTxn}"></c:out>
							</span>
						</div>
						 <div class="col-sm-2 textbox2_size alignment_size">
							<span class="field-tip">
								<ul class="resize">
									<li><input type="button"
										class="btn btn-Success button_<c:out value="${packageDetails.subscriptionType}"></c:out>"
										value="Buy" 
										onclick="scSubscriptionDetails('<c:out value="${packageDetails.serviceChargeRule.serviceChargeRuleId}"/>' , '<c:out value='${packageDetails.subscriptionType}'/>', '<c:out value='${packageDetails.numberOfTxn}'/>','<c:out value='${customerId}'/>');" /></li>
								</ul> 
						
							</span>
						</div> 
					</div>
				</div>
				</div>
			</c:forEach>
		

	
</div>