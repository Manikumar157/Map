<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="utf-8">
<!--   <meta content="IE=edge" http-equiv="X-UA-Compatible"> -->
<meta content="width=device-width, initial-scale=1" name="viewport">
<meta content="" name="description">
<meta content="" name="author">
<link href="ico/favicon.ico" rel="shortcut icon">
<title><spring:message code="LABEL_TITLE" text="CIFMobile" /></title>
<link rel="shortcut icon" href="images/favicon.gif" />
<jsp:include page="common.jsp"></jsp:include>
<style>
@-moz-document url-prefix() {
    select {
        width: 180px;
    }
    #status {
        width: 180px;
    }
    #countryId {
        width: 180px;
    }
    #billerName {
        width: 180px;
    }
    #adjustedAmount {
        width: 180px;
    }
    #adjustedFee {
        width: 180px;
    }
    #operatorName {
        width: 180px;
    }
    #comission {
        width: 180px;
    }
}
@media screen and (-webkit-min-device-pixel-ratio:0) {
    select {
        width: 161px;
    }
    .navbar-right {
    	width: 330px;
    }
    textarea {
        width: 161px;
    }
    #currencyId {
        width: 161px;
    }
    #status {
        width: 161px;
    }
    #countryId {
        width: 161px;
    }
    #billerName {
        width: 161px;
    }
    #adjustedAmount {
        width: 161px;
    }
    #adjustedFee {
        width: 161px;
    }
    #fromDate {
        width: 161px;
    }
    #toDate {
        width: 161px;
    }
}

@media screen and (min-width:0\0) {
   select {
        width: 181px;
    }
    #status {
        width: 181px;
    }
    #countryId {
        width: 181px;
    }
    #billerName {
        width: 181px;
    }
    #adjustedAmount {
        width: 181px;
    }
    #adjustedFee {
        width: 181px;
    }
    #operatorName {
        width: 181px;
    }
    #comission {
        width: 181px;
    }
}

textarea{
border-color:#e6e6e6;border-radius:3px;
}
select{
border-color:#e6e6e6;height:30px;border-radius:3px;
}
input[type="radio"]{
   margin-right:5px;
}
</style>
</head>
<body role="document">

	<div id="preloader">
		<div id="status">&nbsp;</div>
	</div>
	<!-- TOPNAV -->
	<tiles:insertAttribute name="header" />

	<!-- SIDE MENU -->

	<div class="wrap-sidebar-content">

		<tiles:insertAttribute name="menu" />

	</div>
	<!-- END OF SIDE MENU -->

	<!--Body-->
	<div class="wrap-fluid" id="paper-bg">

		<div class="row">
			<tiles:insertAttribute name="body" />

		</div>
	</div>
	<tiles:insertAttribute name="footer" />
	</div>


	<jsp:include page="commonScript.jsp"></jsp:include>
</body>

</html>