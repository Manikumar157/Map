<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="authz"
	uri="http://www.springframework.org/security/tags"%>
<html>
<head>
<script  src="https://code.jquery.com/jquery-3.1.1.js"></script>
<script type="text/javascript"  src="<%=request.getContextPath()%>/js/highcharts.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/exporting.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/highcharts-3d.js"></script>

</head>
<style>
.highcharts-credits{
display:none;
}
txnCountVsTxnType, #sliders {
    min-width: 310px; 
    max-width: 800px;
    margin: 0 auto;
}
txnCountVsTxnType {
    height: 400px; 
}
txnTypeVsTxnAmount, #sliders {
    min-width: 310px; 
    max-width: 800px;
    margin: 0 auto;
}
txnTypeVsTxnAmount {
    height: 400px; 
}
#sliderDiv{
    background: #fff;
    width: 1000px;
    margin-left: 35px;
}
</style>
<body>

<div id="txnCountVsTxnType" style="min-width: 300px; max-width: 1000px; height: 400px; margin: 0 auto"></div>
<div class= "row" id ="sliderDiv">
<div id="sliders">
    <table>
        <tr>
        	<td>Change Top View Angle</td>
        	<td><input id="alpha" type="range" min="0" max="45" value="15"/> <span id="alpha-value" class="value"></span></td>
        </tr>
        <tr>
        	<td>Change Front View Angle</td>
        	<td><input id="beta" type="range" min="-45" max="45" value="15"/> <span id="beta-value" class="value"></span></td>
        </tr>
        <tr>
        	<td>Change Side View Angle</td>
        	<td><input id="depth" type="range" min="20" max="100" value="50"/> <span id="depth-value" class="value"></span></td>
        </tr>
    </table>
</div>
<br/>
<div id="txnTypeVsTxnAmount" style="min-width: 300px; height: 400px; max-width: 1000px; margin: 0 auto"></div>
</div>



<script>
	var pageSummData =  ${pageSummData};
</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dashBoardData.js"></script>
</body>
