<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!-- // vineeth change -->

<style>
.switch {
       display: inline-block;
    width: 40px;
    height: 17px;
    position: absolute;
    top: 4px;
    right: 0px;
}

.switch input {display:none;}

.slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ccc;
  -webkit-transition: .4s;
  transition: .4s;
}

.slider:before {
    position: absolute;
    content: "";
    height: 10px;
    width: 10px;
    left: 2px;
    bottom: 4px;
    background-color: white;
    -webkit-transition: .4s;
    transition: .4s;
}

input:checked + .slider {
  background-color: #2196F3;
}

input:focus + .slider {
  box-shadow: 0 0 1px #2196F3;
}

input:checked + .slider:before {
  -webkit-transform: translateX(26px);
  -ms-transform: translateX(26px);
  transform: translateX(26px);
}

/* Rounded sliders */
.slider.round {
  border-radius: 34px;
}

.slider.round:before {
  border-radius: 50%;
}
</style>


<!-- // change vineeth -->
<jsp:include page="csrf_token.jsp" />
<c:if test="${list.size() > 0 }">
	<c:set var="totalSize" value="${list.size() }"/>
	<c:forEach  items="${list}" var="ct"  varStatus="status">
		<c:if test="${status.index%2 == 0}">
			<c:set var="counter" value="0" />
			<div class="col-md-12 col-xs-12 col-lg-12 col-sm-12 padding-0">
		</c:if>
		<div class="col-md-6 col-xs-12 col-lg-6 col-sm-6">
		<%-- 	<input type="checkbox" class="${ entity }" id="${ entity }${ct[id]}" value="${ct[id]}" functionalCode="${ct[functionalCode]}"/> --%> 
			<label for="${ entity }${ct[id]}">${ct[value]}</label>
			
			<!-- // change vineeth -->
				<label for="${ entity }${ct[id]}">${ct[value1]}</label>
							<label class="switch">
							<input type="checkbox" class="${ entity }"
								id="${ entity }${ct[id]}" value="${ct[id]}"
								functionalCode="${ct[functionalCode]}" ><span class="slider round"></span>
							</label>
			<!-- // change vineeth over -->
			
		</div>
		<c:if test="${counter == 1 || status.index == totalSize}">
			</div>
		</c:if>
		<c:set var="counter" value="${counter+1}" />
	</c:forEach>
</c:if>
<c:if test="${list.size() == 0 }">
	${ emptyMessage }
</c:if>