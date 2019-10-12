
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="csrfToken">
	<%@include file="csrf_token.jsp"%>
</div>
	<select id="businessPartnerId" class="dropdown chosen-select"
		name="businessPartnerId">
		<option value="" selected="selected" >
			<spring:message code="LABEL_WUSER_SELECT" text="--Please Select--"></spring:message>
		</option>
		<%-- <form:options items="${businessNameList}"
														itemLabel="Business" itemValue="businessNameList" /> --%>

		<c:forEach items="${businessNameList}" var="ct">
			<option value="${ct.name}"><c:out value="${ct.name}" /></option>
			<!-- style="font-weight:lighter;" -->
		</c:forEach>
		<%-- <font color="RED"><form:errors path="businessNameList"></form:errors></font> --%>
	</select>
	



<%-- <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page errorPage="errorPage.jsp" %>

<div id="comboCsrfToken">
<input type="hidden" name="combo_csrfToken" value="${sessionScope.csrfToken}" />
</div>

<select class="form-control" style="width:216px;" id="<c:out value="${id}" />" name="<c:out value="${id}" />">
	<option value="" ><spring:message code="LABEL_SELECT" text="-- Please Select --"/></option>
	<c:forEach items="${list}" var="var">
		<option value="<c:out value="${var[id]}" />" ><c:out value="${var[value]}" /></option>
	</c:forEach>
</select> --%>
