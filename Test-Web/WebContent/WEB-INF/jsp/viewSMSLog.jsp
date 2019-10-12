<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<style>
tr th {
	text-align: center;
}
</style>
<jsp:include page="/WEB-INF/jsp/layout/common.jsp"/>

<div class="box-body table-responsive">
		<div id="csrfToken2Div">
	<%@include file="csrf_token2.jsp"%>
</div>
        <c:set value="${(page.currentPage-1)*10}" var="j"></c:set> 
        
       <table id="example2" class="table table-bordered table-hover" style="text-align:center;">
						<thead>
							<tr>
				<td colspan="9" style="color: #ba0101;font-weight: bold;font-size: 12px;" align="center">
					<spring:message code="${message}" text=""/>
				</td>
			  </tr>
             <thead>     
             	<tr>
                <th align="center"><spring:message code="LABEL_SLNO" text="Sl. No:"/></th>          
                <th align="center" ><spring:message code="LABEL_MOB_NO" text="Mob No:"/></th>
                <th align="center"><spring:message code="LABEL_MSG_TYPE" text="Msg Type:"/></th>
                <th align="center"><spring:message code="LABEL_ENCODE" text="Encode:"/></th>
                <th align="center"><spring:message code="LABEL_SCH_DATE" text="Sch Date:"/></th>    
                <th align="center"><spring:message code="LABEL_SENT_DATE" text="Sent Date:"/></th>   
                <th align="center"><spring:message code="LABEL_STATUS" text="Status:"/></th>    
                <th align="center"><spring:message code="LABEL_ACTION" text="Action"/></th>    
              </tr>
              </thead>
              <tbody>
              <c:forEach items="${page.results}" var="smsLog">
 			  <tr <c:if test="${j%2==0}"> bgcolor="white" </c:if> <c:if test="${j%2!=0}"> bgcolor="#d2d3f1" </c:if>>
			  
			 	<c:set var="j" value="${j + 1}" scope="page"/>    
			 	<td align="center" height="23px"><c:out value="${j}" /></td>
				<td align="center"><c:out value="${smsLog.mobileNumber}" /></td>
				<td align="center">				
				<c:choose> 
				<c:when test="${smsLog.messageType==1}"> <spring:message code="LABEL_MSGTYPE_NEWTXNPIN" text="NewTxnPin"/>  </c:when> 
				<c:when test="${smsLog.messageType==2}"> <spring:message code="LABEL_MSGTYPE_NEWLOGINPIN" text="NewLoginPin"/></c:when>
				<c:when test="${smsLog.messageType==3}"> <spring:message code="LABEL_MSGTYPE_INITXNPINLOGINPIN" text="InitialTxnPinLoginPin"/> </c:when>
				<c:when test="${smsLog.messageType==4}"> <spring:message code="LABEL_MSGTYPE_APPLINK" text="AppLink"/> </c:when>
				<c:when test="${smsLog.messageType==5}"> <spring:message code="LABEL_MSGTYPE_WEBUSERPSW" text="WebUsernamePassword"/> </c:when>
				<c:when test="${smsLog.messageType==6}"> <spring:message code="LABEL_MSGTYPE_WEBOTP" text="OTP"/> </c:when>
				<c:when test="${smsLog.messageType==7}"> <spring:message code="LABEL_MSGTYPE_ALERT_CREDIT" text="Credit"/> </c:when>
				<c:when test="${smsLog.messageType==8}"> <spring:message code="LABEL_MSGTYPE_ALERT_DEBIT" text="Debit"/> </c:when>
				<c:when test="${smsLog.messageType==9}"> <spring:message code="LABEL_MSGTYPE_ALERT_WEEKLY" text="Weekly"/> </c:when>
	            <c:when test="${smsLog.messageType==10}"> <spring:message code="LABEL_SALE_OTP" text="Sale OTP"/> </c:when>
				<c:when test="${smsLog.messageType==11}"> <spring:message code="LABEL_MSGTYPE_WEB_RESET_PASSWORD" text="Web User Reset Password"/> </c:when>
				<c:when test="${smsLog.messageType==12}"> <spring:message code="LABEL_MSGTYPE_ADD_CARD" text="Add Card"/> </c:when>
				<c:when test="${smsLog.messageType==13}"> <spring:message code="LABEL_MSGTYPE_SMSCASH" text="SMS Cash"/> </c:when>
				<c:when test="${smsLog.messageType==15}"> <spring:message code="LABEL_RESET_PIN" text="Reset PIN"/> </c:when>
				</c:choose>					
				</td>
				<td align="center">				
				<c:choose> 
				<c:when test="${smsLog.encoding==1}"> <spring:message code="LABEL_ENCODE_TEXT" text="Text:"/>  </c:when> 
				<c:when test="${smsLog.encoding==2}"> <spring:message code="LABEL_ENCODE_BINARY" text="Binary:"/>  </c:when>
				<c:when test="${smsLog.encoding==3}"> <spring:message code="LABEL_ENCODE_WAP" text="Wap:"/>  </c:when>			
				</c:choose>				
				</td>				
				<td align="center"><fmt:formatDate pattern="dd-MM-yyyy HH:mm:ss" value="${smsLog.scheduledDate}" /></td>
				<td align="center"><fmt:formatDate pattern="dd-MM-yyyy HH:mm:ss" value="${smsLog.sentDate}" /></td>
				<td align="center">				
				<c:choose> 
				<c:when test="${smsLog.status==0}"> <spring:message code="LABEL_STATUS_SCHEDULED" text="Scheduled"/>  </c:when> 
				<c:when test="${smsLog.status==1}"> <spring:message code="LABEL_STATUS_SENT" text="Sent"/>  </c:when>
				<c:when test="${smsLog.status==2}"> <spring:message code="LABEL_STATUS_FAILED" text="Failed"/>  </c:when>			
				</c:choose>				
				</td>	
				<td align="center"><c:if test="${smsLog.status!=0}"><a href="#" onclick="changeSmsLogStatus('<c:out value="${ smsLog.messageId}"/>','${smsLog.mobileNumber}')" ><spring:message code="LABEL_RESEND" text="Resend"></spring:message></a></c:if></td>	
				
			  </tr>
				
			</c:forEach>
			</tbody>
	    
       </table>
       </div>
        
        <jsp:include page="/WEB-INF/jsp/layout/commonScript.jsp"/> 
