<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>

<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
<title><spring:message code="LABEL_TITLE" text="EOT Mobile"/></title>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
-->
</style></head>
<body>
<table width="1004" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td width="993" align="center" class="copy_rgt"><spring:message code="LABEL_VERSION_COPYRIGHT" text="v"/> <td>${version}</td><spring:message code="LABEL_COPYRIGHT" text="Copyright"/> &copy; <spring:message code="LABEL_COPYRIGHT_STRING" text="2019 All Rights Reserved Trinity Technologies"/></td>
    <td width="2" align="center">&nbsp;</td>
  </tr>
</table>
</body>
</html>