<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<style>
.mobile-popup-preview .nav-tabs>li {
    float: left;
    margin-bottom: 0px;
    width: 33%;
    text-align: center;
	min-height: 66px;
}
.mobile-popup-preview .nav-tabs>li>a {
	color: black;
	padding: 15px 0px;
	display: flex;
	justify-content: center;
	align-items: center;
	min-height: 66px;
}
/* AUTHOR: Murari DATE: 02 JULY 2018 - Bug Fix: Popup scroll issue fixed*/
.mobile-popup-preview .tab-content {
	height: calc(100% - 68px);
	padding: 10px;
	overflow-y: auto;
}
.mobile-popup-preview .nav-tabs {
	position: sticky;
    top: 45px;
    background: white;
    z-index: 10;
}
.mobile-popup-preview .nav-tabs>li.active>a, .mobile-popup-preview .nav-tabs>li.active>a:hover, .mobile-popup-preview .nav-tabs>li.active>a:focus {
	color: black;
	border: 0px;
	border-bottom: 2px solid #2a6496;
	min-height: 66px;
}
.mobile-popup-preview .mobilemenu-padding {
	height: 100%;
}
.mobile-popup-preview .mobilemenu-padding {
	padding: 45px 19px !important;
}
.mobile-popup-preview .active a, .mobile-popup-preview .nav-tabs>li.active>a:focus {
	background: gainsboro !important;
}
</style>

<div id="csrfToken">
	<%@include file="csrf_token.jsp"%>
</div>
<div class="col-lg-12 mobile-popup-preview">
	<div class="mobileSection col-lg-12 padding-0">
		<div class='mobileMenu col-lg-12 padding-0 mobilemenu-padding'>
			<textarea id="preview-selected-menu-icon" style="display:none;">${dynamicMenuConfDTO.selectedMenuIcon}</textarea>
			<ul class="nav nav-tabs" role="tablist">
				<c:forEach items="${tabList}" var="ct" varStatus="status">
				<c:if test="${status.index == 0}">
					<c:set var="tabSelection" value="active"/>
				</c:if>
				<c:if test="${status.index != 0}">
					<c:set var="tabSelection" value=""/>
				</c:if>
				<c:if test="${selectedTabList.contains(ct[id])}">
					<li class="${tabSelection}" role="presentation"><a href="#menuTab${ct[id]}"
						 role="tab" data-toggle="tab">${ct[value1]}</a></li>
				</c:if>
				</c:forEach>
			</ul>
			<div class="tab-content" id="preview-content-selected">
            </div>
		</div>
	</div>
</div>










