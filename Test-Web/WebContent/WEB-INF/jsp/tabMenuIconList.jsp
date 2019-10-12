<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:if test="${list.size() > 0 }">
	<c:set var="totalSize" value="${list.size() }"/>
	<c:forEach  items="${list}" var="ct"  varStatus="status">
		<c:if test="${status.index%3 == 0}">
			<c:set var="counter" value="0" />
			<div class="col-md-12 col-xs-12 col-lg-12 col-sm-12 padding-0">
		</c:if>
		<div class="col-md-4 col-xs-12 col-lg-4 col-sm-6">
		<%-- 	<input type="radio" name="tab-menu-icon" class="${ entity }" value="${ct[id]}"/>  --%>
			<input type="hidden" value="${ct[value]}" id="txt-hidden" />
			<c:if test="${ct[blob] != null && ct[blob] != undefined}">
				<img width="60px" height="60px" class="selectimage ${ entity }" id="selected-image${ct[id]}" src= "data:image/png;base64,${ct[blob]}"  value="${ct[id]}"/>
			</c:if>
		</div>
		<c:if test="${counter == 2 || status.index == totalSize}">
			</div>
		</c:if>
		<c:set var="counter" value="${counter+1}" />
	</c:forEach>
	<%-- <% for(int i = 0; i < 10; i++) { %>
        <c:if test="<%= i%3 == 0 %>">
			<c:set var="counter" value="0" />
			<div class="col-md-12 col-xs-12 col-lg-12 col-sm-12 padding-0">
		</c:if>
		<div class="col-md-4 col-xs-12 col-lg-4 col-sm-6">
			<input type="radio" name="tab-menu-icon" class="tab-menu-icon" value="<%= i %>"/> test <%= i %>
		</div>
		<c:if test="${counter == 2}">
			</div>
		</c:if>
		<c:set var="counter" value="${counter+1}" />
    <% } %> --%>
</c:if>
<c:if test="${list.size() == 0 }">
	${ emptyMessage }
</c:if>