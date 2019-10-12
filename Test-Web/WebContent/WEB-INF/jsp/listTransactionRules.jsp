<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>

<html>
	<head>
		<title><spring:message code="LABEL_TITLE" text="EOT Mobile"></spring:message></title>
		<style type="text/css">
		<!--
		.style1 {
		color: #FFFFFF;
		font-weight: bold;
		}
		-->
		</style>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
		<script type="text/javascript">
		
		 $(document).ready(function(){

			  applyChosen();

		  });
		 
		function addRule(){
			
			var ruleLevel = document.getElementsByName("ruleLevel") ;
			var bankId = document.searchTRRuleForm.bankId.value ;
			
			var selectedRule ;
			
			if(ruleLevel[0].checked){
				selectedRule = 1;
			}if(ruleLevel[1].checked){
				selectedRule = 2;
			}
			
			document.searchTRRuleForm.action="showTransactionRuleForm.htm";
		    document.searchTRRuleForm.submit();
			
		}
		
		function TransDetail(url,trRuleId,ruleLevel){
			document.getElementById('trRuleId').value=trRuleId;
			document.getElementById('ruleLevel1').value=ruleLevel;
			submitlink(url,'searchTRRuleForm');
		}
		
		function showForm(url,ruleLevel){
			document.getElementById('ruleLevel1').value=ruleLevel;
			submitlink(url,'searchTRRuleForm');
		}
		
		$(document).ready(function() {
			
			var ruleLevel = document.getElementById("ruleLevel") ;
			
			if( ruleLevel.value == 1 || ruleLevel.value == 4 ){
				$("#bankgroups").hide();
				$("#banks").hide();
				$("#profiles").hide();
			}else if( ruleLevel.value == 2 ){
				$("#bankgroups").show();
				$("#banks").hide();
				$("#profiles").hide();
			}else if( ruleLevel.value == 3 ){
				$("#bankgroups").hide();
				$("#banks").show();
				$("#profiles").show();
				applyChosen();
			}
			
			$("#ruleLevels").change(function() {
				var ruleLevel = document.getElementById("ruleLevel") ;
				//alert(ruleLevel.value);
				if( ruleLevel.value == 1 || ruleLevel.value == 4 ){
					$("#bankgroups").hide();
					$("#banks").hide();
					$("#profiles").hide();
				}else if( ruleLevel.value == 2 ){
					$("#bankgroups").show();
					$("#banks").hide();
					$("#profiles").hide();
				}else if( ruleLevel.value == 3 ){
					$("#bankgroups").hide();
					$("#banks").show();
					$("#profiles").show();
					applyChosen();
				}
				
			});
			
			
		$(document).change(function() {
			$("#bankGroupName").change(function() {		
				$bankGroupName = document.getElementById("bankGroupName").value;
				$.post("getBanks.htm", {
					bankGroupId : $bankGroupName
				}, function(data) {
					document.getElementById("bank").innerHTML="";
					document.getElementById("bank").innerHTML = data;
					applyChosen();
					/* <!--  Author name <vinod joshi>, Date<7/5/2018>, purpose of change < Token problem, logging out page> ,
					//Start--> */
					setTokenValFrmAjaxResp();
					/* End */
				});
			});

			$("#bank").change(function() {
				document.getElementById("bankId").value= document.getElementById("bankName").value;;
				$bankName = document.getElementById("bankName").value;
				$.post("getCustomerProfiles.htm", {
					bankId : $bankName
				}, function(data) {
					document.getElementById("profiles").innerHTML="";
					document.getElementById("profiles").innerHTML = data;
					applyChosen();
					/* <!--  Author name <vinod joshi>, Date<7/6/2018>, purpose of change < Token problem, logging out page> ,
					//Start--> */
					setTokenValFrmAjaxResp();
					/* End */
					
				});
			});

			$("#profiles").change(function() {
				document.getElementById("profileId").value = document.getElementById("profileName").value ;
				applyChosen();
			});
						
		});
		/*Author name <vinod joshi>, Date<6/25/2018>, purpose of change <Searching criteria is not working > ,*/
		/*Start*/
		/* $(document).change(function() { */
			$("#bankName").change(function() {
				var bankName =  document.getElementById("bankId").value= document.getElementById("bankName").value;
				 $bankName = bankName
				 //bankId : $bankName
					$csrfToken = $("#csrfToken").val();
				$.post("getCustomerProfiles.htm", {
					bankId : $bankName,
					csrfToken : $csrfToken
				}, function(data) {
					document.getElementById("profiles").innerHTML="";
					document.getElementById("profiles").innerHTML = data;
					document.getElementById("profileId").value = "";
					applyChosen();
					/* <!--  Author name <vinod joshi>, Date<7/6/2018>, purpose of change < Token problem, logging out page> ,
					//Start--> */
					setTokenValFrmAjaxResp();
					/* End */
				});
			});
		/* }); */
			$("#profiles").change(function() {
				document.getElementById("profileId").value = document.getElementById("profileName").value ;
			});
						
		});
		/*End*/

		$(document).ready(function() {	
			$("#profileName").change(function() {
				document.getElementById("profileId").value = document.getElementById("profileName").value ;
			});
			$("#bankName").change(function() {
				document.getElementById("bankId").value = document.getElementById("bankName").value ;
			});			
		});
		
		function validate(){
			
			var ruleLevel = document.getElementById("ruleLevel").value;
			var bankGroup=document.getElementById("bankGroupName").value;
			var bankName = document.getElementById("bankName").value;
			var profileName = document.getElementById("profileId").value;
			
			 if(ruleLevel==2 && bankGroup ==""){
			     alert('<spring:message code="VALID_EMPTY_BANK_GROUP" text="Please select the Bank Group"/>' );
			     return false;
			 }else if(ruleLevel==3 && bankName ==""){
			     alert('<spring:message code="VALID_EMPTY_BANK_NAME" text="Please select the Bank Name"/>');
			     return false;
			 }else if(ruleLevel==3 && profileName ==""){
				 alert('<spring:message code="VALID_EMPTY_PROFILE" text="Please select the Profile" />');
			     return false;
			 }else{
				 document.searchTRRuleForm.action="searchTransactionRules.htm";
				 document.searchTRRuleForm.submit();
			 }  

		}
		
		function txnRulesExcel(){

			submitlink("exportToXlsForTxnRules.htm","searchTRRuleForm"); 
			 for(var i=0;i<150000;i++);{
			 document.body.style.cursor = 'default';
			 canSubmit = true; 
			 $.unblockUI();
			 }
		}
		function txnRulesPDF(){
			 
			 submitlink("exportToPdfForTxnRules.htm","searchTRRuleForm"); 
			 for(var i=0;i<150000;i++);{
			 document.body.style.cursor = '';
			 canSubmit = true; 
			 }
		}
		</script>
	</head>
	
	<body>
	
	<div class="col-lg-12">
<div class="box">
	<div class="box-header">
		<h3 class="box-title">
			<span><spring:message code="LABEL_TR_RULE" text="Transaction Rules"/></span>
		</h3>
	</div><br/>
	<form:form name="searchTRRuleForm" id="searchTRRuleForm" action="searchTransactionRules.htm" method="post" commandName="trSearchDTO"> 
	<jsp:include page="csrf_token.jsp"/> 
	<form:hidden path="pageNumber" value="1"/>
	<form:hidden path="bankId"/>
	<form:hidden path="profileId"/>
	<form:hidden path="ruleLevel"/>
	<input id="trRuleId" type="hidden" name="trRuleId" />
	<input id="ruleLevel1" type="hidden" name="ruleLevel1" value=""/>
	<div class="col-md-3 col-md-offset-10">
	<authz:authorize ifAnyGranted="ROLE_admin,ROLE_bankadmin">
	<authz:authorize ifAnyGranted="ROLE_addTransacationRulesAdminActivityAdmin">
	<%-- <a href="javascript:showForm('showTransactionRuleForm.htm','1')" ><strong><spring:message code="LABEL_ADD_RULE" text="Add Rule"></spring:message></strong></a> &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; --%>
		 <input type="button" class="btn btn-primary"
							value="<spring:message code="LABEL_ADD_RULE" text="Add Clearing House"/>"
					onclick="javascript:showForm('showTransactionRuleForm.htm','1')" />
	</authz:authorize>	
		
	</authz:authorize>
	<authz:authorize ifAnyGranted="ROLE_bankgroupadmin">
	<authz:authorize ifAnyGranted="ROLE_addTransacationRulesAdminActivityAdmin">
		<%-- <a href="javascript:showForm('showTransactionRuleForm.htm','2')" ><strong><spring:message code="LABEL_ADD_RULE" text="Add Rule"></spring:message></strong></a> --%>
		 <input type="button" class="btn btn-primary"
							value="<spring:message code="LABEL_ADD_RULE" text="Add Clearing House"/>"
					onclick="javascript:showForm('showTransactionRuleForm.htm','2')" />
	</authz:authorize>
	</authz:authorize>
	<authz:authorize ifAnyGranted="ROLE_parameter">	
	<authz:authorize ifAnyGranted="ROLE_addTransacationRulesAdminActivityAdmin">
		<%-- <a href="javascript:showForm('showTransactionRuleForm.htm','3')"><strong><spring:message code="LABEL_ADD_RULE" text="Add Rule"></spring:message></strong></a> --%>
		<input type="button" class="btn btn-primary"
							value="<spring:message code="LABEL_ADD_RULE" text="Add Clearing House"/>"
					onclick="javascript:showForm('showTransactionRuleForm.htm','3')" />
	</authz:authorize>
	</authz:authorize>
	</div>
	<div class="box-body">
		<form class="form-inline" role="form">
			<div class="row">
			<!-- <div class="col-md-4"></div> -->
				<div class="col-sm-4" id="ruleLevels">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_RULE_TYPE" text="Rule Type"/></label> 
					<form:select path="ruleLevel" id="ruleLevel" cssClass="dropdown_small chosen-select">
     					<form:option value="1"><spring:message code="LABEL_GLOBAL" text="Global"/></form:option>
     					<form:option value="4" ><spring:message code="LABEL_INTER_BANK" text="Inter Bank"/></form:option>
  	<c:if test="${bankGroupList!=null}"><form:option value="2" ><spring:message code="LABEL_BANK_GROUPS" text="Bank Groups"/></form:option></c:if>
     					<form:option value="3" ><spring:message code="LABEL_PROFILES" text="Profiles"/></form:option>
     					</form:select>
				</div>
				<%-- <div class="col-md-4" id="bankgroups">
				<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_BANKGROUP" text="Bank Group"/></label>
       			<form:select path="bankGroupId" id="bankGroupName" cssClass="dropdown_small chosen-select">
						<option value=""><spring:message code="LABEL_WUSER_SELECT" text="--Please Select--"></spring:message></option>
						<form:options items="${bankGroupList}"	itemValue="bankGroupId" itemLabel="bankGroupName"></form:options>
				</form:select>
				</div>
				<div class="col-md-4" id="banks">
				<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_BANK" text="Bank"/></label>
				<form:select path="bankId"	id="bankName" cssClass="dropdown_small chosen-select">
					<option value=""><spring:message code="LABEL_WUSER_SELECT" text="--Please Select--"></spring:message></option>
					<form:options items="${bankList}" itemValue="bankId" itemLabel="bankName"></form:options>
				</form:select>
			</div> --%>
			<div class="col-md-4" id="profiles">
  				<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_PROFILES" text="Profiles"></spring:message></label>
  				<form:select path="profileId" id="profileName" cssClass="dropdown_small chosen-select">
  					<option value=""><spring:message code="LABEL_WUSER_SELECT" text="--Please Select--"></spring:message></option>
					<form:options items="${profileList}" itemValue="profileId" itemLabel="profileName"></form:options>
				</form:select>
  			</div>
			</div>
			<div class="box-footer">
			<input type="button" class="btn btn-primary pull-right" id="submitButton" value="<spring:message code="LABEL_SEARCH" text="Submit"/>" onclick="validate();" style="margin-right: 60px;"/>
				<br/><br/>
			</div>
		</form>
</div>
</form:form>
</div>
	<div class="box">
		<div class="box-body table-responsive">
		
			<div class="exprt" style="margin-top: 0px; margin-bottom: 4px; height: 30px;">
                	<%-- <span><spring:message code="LABEL_EXPORT_AS" text="Export as"/> </span> --%>
					<span style="float:right; margin-right: 5px;">
						<a href="#" onclick="javascript:txnRulesExcel();" style="text-decoration:none;margin-left: -16px;"><img src="<%=request.getContextPath()%>/images/excel.jpg" />
	    					<%--<span><spring:message code="LABEL_EXCEL" text="Excel"/></span> --%>
						</a>
					</span>
					
					<span style="margin-right: 30px; float:right">
						<a href="#" style="text-decoration:none;margin-left: 10px;" onclick="javascript:txnRulesPDF();">
						<img src="<%=request.getContextPath()%>/images/pdf.jpg" />
							<%-- <span><spring:message code="LABEL_PDF" text="PDF"/></span> --%>
						</a>
					</span>					
   		    </div>
		
			<table id="example1" class="table table-bordered table-striped" style="text-align:center;">
				<thead>
					<tr>
						<th><spring:message code="LABEL_RULE_LEVEL" text="Rule Level" /></th>
						<th><spring:message code="LABEL_TXNTYPE" text="TxnType" /></th>
						<th><spring:message code="LABEL_TH_MAXVAL_LIMIT" text="MaxValLimit" /></th>
						<th><spring:message code="LABEL_CALCULATED_ON" text="Calculated On" /></th>
						<th><spring:message code="LABEL_APPROVAL_LIMIT" text="Approval Limit" /></th>
						<th><spring:message code="LABEL_ACTION" text="Action" /></th>
					</tr>
				</thead>
				<tbody>
	              <c:forEach items="${page.results}" var="trrules">
	              <c:set var="j" value="${ j+1 }"></c:set>
				  <tr <c:if test="${ j%2 == 0 }"> </c:if>>
				 	<td>
						<c:if test="${ trrules.ruleLevel == 1  }"><spring:message code="LABEL_GLOBAL" text="Global"/></c:if>
						<c:if test="${ trrules.ruleLevel == 2  }"><spring:message code="LABEL_BANK_GROUP_ADMIN" text="Bank Group Admin"/></c:if>
						<c:if test="${ trrules.ruleLevel == 3  }"><spring:message code="LABEL_CUSTOMER_PROFILE" text="Customer Profile"/></c:if>
						<c:if test="${ trrules.ruleLevel == 4  }"><spring:message code="LABEL_INTER_BANK" text="Inter Bank"/></c:if>
					</td>
					<td style="text-align:left;">
					<c:set var="lang" value="${language}"></c:set>
					<c:forEach items="${trrules.transactionRuleTxns}" var="txnType">
						<c:forEach items="${txnType.transactionType.transactionTypesDescs}" var="transactionTypesDescs">
						 <c:if test="${transactionTypesDescs.comp_id.locale==lang}">
								<c:out 	value="${transactionTypesDescs.description}"/>	-
								</c:if>		 
						
						</c:forEach>
						
					<c:if test="${txnType.sourceType==1}">
	                     <c:set var="source"><spring:message code="LABEL_SOURCE_WALLET" text="Wallet" /></c:set>
                        </c:if>
                        <c:if test="${txnType.sourceType==2}">
	                     <c:set var="source"><spring:message code="LABEL_SOURCE_CARD" text="Card" /></c:set>
                        </c:if>
                         <c:if test="${txnType.sourceType==3}">
	                     <c:set var="source"><spring:message code="LABEL_SOURCE_BANKACC" text="Bank Account" /></c:set>
                        </c:if>
                         <c:if test="${txnType.sourceType==0}">
	                     	<c:set var="source"><spring:message code="LABEL_SOURCE_OTHERS" text="Others" /></c:set>
                        </c:if>
                        <c:if test="${txnType.sourceType==4}">
	                     	<c:set var="source"><spring:message code="" text="FI" /></c:set>
                        </c:if>
                        <c:out value="${source}"/>	<br/>
					</c:forEach>
				   </td>													   
				   													 	
					<td style="text-align:right;">  <c:out value="${trrules.maxValueLimit}" />  
					<%-- <fmt:formatNumber value = "${trrules.maxValueLimit}" type = "number"/> --%></td>			
					<td>
					<c:choose> <c:when test="${trrules.ruleType==0}"> <spring:message code="LABEL_DEBIT" text="Debit"/> </c:when>
					<c:otherwise><spring:message code="LABEL_CREDIT" text="Credit"/></c:otherwise></c:choose>
					</td>
					<td style="text-align:right;">
						<c:choose> <c:when test="${trrules.approvalLimit ne null }"> 						
       					 <%-- <fmt:formatNumber value = "${trrules.approvalLimit}" type = "number"/>  --%>
						  <c:out value="${trrules.approvalLimit}"/> 
						 </c:when>
					<c:otherwise><spring:message code="LABEL_TXN_AMT_NA" text="N/A"/></c:otherwise></c:choose>
											
													
					</td>			
					
					<authz:authorize ifAnyGranted="ROLE_admin">
					<authz:authorize ifAnyGranted="ROLE_editTransacationRulesAdminActivityAdmin">
				 	<c:choose>
			        	<c:when test="${ trrules.ruleLevel==1 or trrules.ruleLevel==4}">
							<%-- <td align="center" ><a href="javascript:submitForm('searchTRRuleForm','editTransactionRule.htm?trRuleId=${trrules.transactionRuleId}&ruleLevel=${trrules.ruleLevel}')"/><spring:message code="LABEL_EDIT" text="Edit:"/></td> --%>	
					 		<td align="center" ><a href="javascript:TransDetail('editTransactionRule.htm','<c:out value="${trrules.transactionRuleId}"/>','<c:out value="${trrules.ruleLevel}"/>')"/><spring:message code="LABEL_EDIT" text="Edit:"/></td>
					 	</c:when>
					 	<c:otherwise> 
					 	<%-- <td><a href="javascript:submitForm('searchTRRuleForm','viewTransactionRule.htm?trRuleId=<c:out value="${trrules.transactionRuleId}"/>')"/><spring:message code="LABEL_VIEW" text="View"/></td> --%>
					 	<td><a href="javascript:TransDetail('viewTransactionRule.htm','<c:out value="${trrules.transactionRuleId}"/>')"/><spring:message code="LABEL_VIEW" text="View"/></td>
					 	</c:otherwise>
				 	</c:choose>
			 		</authz:authorize>
			 		</authz:authorize>
			 		
			 		<authz:authorize ifAnyGranted="ROLE_bankgroupadmin">
			 		<authz:authorize ifAnyGranted="ROLE_editTransacationRulesAdminActivityAdmin">
				 	<c:choose>
			        	<c:when test="${ trrules.ruleLevel==2}">
							<%-- <td><a href="javascript:submitForm('searchTRRuleForm','editTransactionRule.htm?trRuleId=${trrules.transactionRuleId}&ruleLevel=${trrules.ruleLevel}')"/><spring:message code="LABEL_EDIT" text="Edit:"/></td> --%>	
							<td align="center" ><a href="javascript:TransDetail('editTransactionRule.htm','<c:out value="${trrules.transactionRuleId}"/>','<c:out value="${trrules.ruleLevel}"/>')"/><spring:message code="LABEL_EDIT" text="Edit:"/></td>
					 	</c:when>
					 	<c:otherwise>
					 	<%-- <td><a href="javascript:submitForm('searchTRRuleForm','viewTransactionRule.htm?trRuleId=<c:out value="${trrules.transactionRuleId}"/>')"/><spring:message code="LABEL_VIEW" text="View"/></td> --%>
					 	<td><a href="javascript:TransDetail('viewTransactionRule.htm','<c:out value="${trrules.transactionRuleId}"/>')"/><spring:message code="LABEL_VIEW" text="View"/></td>
					 	</c:otherwise>
				 	</c:choose>
			 		</authz:authorize>
			 		</authz:authorize>
			 		<authz:authorize ifAnyGranted="ROLE_bankadmin">
			 		<authz:authorize ifAnyGranted="ROLE_editTransacationRulesAdminActivityAdmin">
				 	<%-- <c:choose> --%>
			        	<%-- <c:when test="${ trrules.ruleLevel==2}"> --%>
							<%-- <td><a href="javascript:submitForm('searchTRRuleForm','editTransactionRule.htm?trRuleId=${trrules.transactionRuleId}&ruleLevel=${trrules.ruleLevel}')"/><spring:message code="LABEL_EDIT" text="Edit:"/></td> --%>	
							<td align="center" ><a href="javascript:TransDetail('editTransactionRule.htm','<c:out value="${trrules.transactionRuleId}"/>','<c:out value="${trrules.ruleLevel}"/>')"/><spring:message code="LABEL_EDIT" text="Edit:"/></td>
					 	<%-- </c:when> --%>
					 <%-- 	<c:otherwise> --%>
					 	<%-- <td><a href="javascript:submitForm('searchTRRuleForm','viewTransactionRule.htm?trRuleId=<c:out value="${trrules.transactionRuleId}"/>')"/><spring:message code="LABEL_VIEW" text="View"/></td> --%>
					 	<%-- <td><a href="javascript:TransDetail('viewTransactionRule.htm','<c:out value="${trrules.transactionRuleId}"/>')"/><spring:message code="LABEL_VIEW" text="View"/></td> --%>
					 <%-- 	</c:otherwise> --%>
				 	<%-- </c:choose> --%>
			 		</authz:authorize>
			 		</authz:authorize>
			 		
			 		<authz:authorize ifAnyGranted="ROLE_parameter">
			 		<authz:authorize ifAnyGranted="ROLE_editTransacationRulesAdminActivityAdmin">
				 	<c:choose>
			        	<c:when test="${ trrules.ruleLevel==3}">
							<%-- <td align="center" ><a href="javascript:submitForm('searchTRRuleForm','editTransactionRule.htm?trRuleId=${trrules.transactionRuleId}&ruleLevel=${trrules.ruleLevel}')"/><spring:message code="LABEL_EDIT" text="Edit:"/></td>	 --%>
					 		<td align="center" ><a href="javascript:TransDetail('editTransactionRule.htm','<c:out value="${trrules.transactionRuleId}"/>','<c:out value="${trrules.ruleLevel}"/>')"/><spring:message code="LABEL_EDIT" text="Edit:"/></td>
					 	</c:when>
					 	<c:otherwise>
					 	<%-- <td><a href="javascript:submitForm('searchTRRuleForm','viewTransactionRule.htm?trRuleId=<c:out value="${trrules.transactionRuleId}"/>')"/><spring:message code="LABEL_VIEW" text="View"/></td> --%>
					 	<td><a href="javascript:TransDetail('viewTransactionRule.htm','<c:out value="${trrules.transactionRuleId}"/>')"/><spring:message code="LABEL_VIEW" text="View"/></td>
					 	</c:otherwise>
				 	</c:choose>
			 		</authz:authorize>
			 		</authz:authorize>
			 		
			 		<authz:authorize ifNotGranted="ROLE_parameter,ROLE_bankadmin,ROLE_bankgroupadmin,ROLE_admin">
			 		<td>
			 		
			 			<spring:message code="LABEL_TXN_AMT_NA" text="N/A"/>
			 		</td>
			 		</authz:authorize>
				
				  </tr>
				  </c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>	
</body>
</html>
