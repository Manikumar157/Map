<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<body>
<% if(request.getParameter("key") != null ) { %>
<% if(request.getParameter("key").equals("pageNotFound")) { %>
<jsp:forward page="/pageNotFound.htm"></jsp:forward>
<% } }%>
</body>
</html>