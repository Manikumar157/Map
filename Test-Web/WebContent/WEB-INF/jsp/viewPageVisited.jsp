<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="authz"
	uri="http://www.springframework.org/security/tags"%>
<html>

<head>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/bankManagement.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/clearingHouseManagement.js"></script>

<script type="text/javascript">
	
</script>
</head>
<body onload="check(),check1()">
	<div class="col-lg-12">
		<div class="box">
			<div class="box-header">
				<h3 class="box-title">
					<span><spring:message code="TITLE_ACCESS_LOG"
							text="View Access Logs" /></span>
				</h3>
			</div>
			<br />
			<div class="pull-right">
				<a href="javascript:submitForm('accessLogForm','showAccessLog.htm')"><strong><spring:message
							code="TITLE_VIEWACCESS" text="View Access" /></strong></a> &nbsp; &nbsp;
				&nbsp;
			</div>
			<br />
			<br />
			<form:form name="accessLogForm" id="accessLogForm" method="post"
				commandName="accessLogDTO" action="searchModule.htm">
				<jsp:include page="csrf_token.jsp"/>
				<div class="box-body table-responsive">
					<table id="example2" class="table table-bordered table-hover"
						style="text-align: center;">
						<thead>
							<tr>
								<th><spring:message code="LABEL_PAGEMODULE"
										text="Page/Module" /></th>
								<th><spring:message code="LABEL_VISITEDTIME"
										text="Visited Time" /></th>
								<th><spring:message code="LABEL_USERID" text="UserId" /></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${page.results}" var="pageLog">
								<tr>
									<td><c:out value="${pageLog.pageVisited}" /></td>
									<td><fmt:formatDate pattern="dd/MM/yyyy HH:mm:ss"
											value="${pageLog.timeVisit}" /></td>
									<td><c:out value="${pageLog.sessionLog.userName}" /></td>

								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</form:form>
		</div>

	</div>
</body>

</html>
