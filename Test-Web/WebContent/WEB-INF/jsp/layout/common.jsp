

<!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="css/bootstrap.css">
    <!-- Bootstrap theme -->
    <!--  <link rel="stylesheet" href="css/bootstrap-theme.min.css"> -->

    <!-- Custom styles for this template -->
    <link rel="stylesheet" href="css/jquery-ui.css" />
    <%@include file="theme.jsp" %>
    <!-- <link rel="stylesheet" href="css/theme.css"> -->
    <link rel="stylesheet" href="css/dashboard.css">
    <!-- <link rel="stylesheet" href="css/style_v.1.0.css"> -->
    <%@include file="commonStyle.jsp" %>
    <link rel="stylesheet" href="css/dripicon.css">
    <link rel="stylesheet" href="css/typicons.css" />
    <link rel="stylesheet" href="css/font-awesome.css" /> 
   <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.6.3/css/all.css" integrity="sha384-UHRtZLI+pbxtHCWp1t77Bi1L4ZtiqrqD80Kn4Z8NTSRyMA2Fd33n5dQ8lWUE00s/" crossorigin="anonymous">
    <link rel="stylesheet" href="css/responsive.css">
    <link rel="stylesheet" href="js/ui/tip/tooltipster.css"> 
    <link rel="stylesheet" type="text/css" href="js/ui/vegas/jquery.vegas.css" />
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">
    <link href="js/ui/datatables/dataTables.bootstrap.css" rel="stylesheet" type="text/css" />
    <link href="<%=request.getContextPath()%>/css/serverside-datatable/jquery.dataTables.min.css" rel="stylesheet" type="text/css" />
	<link href="js/ui/footable/css/footable.core.css?v=2-0-1" rel="stylesheet" type="text/css" />
    <link href="js/ui/footable/css/footable.standalone.css" rel="stylesheet" type="text/css" />
    <link href="js/ui/footable/css/footable-demos.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="js/ui/datepicker/datepicker.css">
  	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/tooltipster.bundle.css" />
     <!-- pace loader -->
    <script src="js/ui/pace/pace.js"></script>
    
    <link href="js/ui/pace/themes/orange/pace-theme-flash.css" rel="stylesheet" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.min.css"></link>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap-theme.min.css"></link>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
    <style>
    .activeItem{
  	 	background-color:#999; /*make some difference for the active item here */
	}
	img.ui-datepicker-trigger {
    margin-left: 5px;
	}
	
	.ui-datepicker-header.ui-widget-header.ui-helper-clearfix.ui-corner-all {
    background: none;
    border: none;
}

.ui-state-highlight, .ui-widget-content .ui-state-highlight, .ui-widget-header .ui-state-highlight {
    border: 1px solid #3c8dbc;
    background: #3c8dbc;
    color: #363636;
}
.ui-state-default, .ui-widget-content .ui-state-default, .ui-widget-header .ui-state-default {
    border: none;
    background: none;
    font-weight: normal;
    color: #555555;
}
th.sorting {
    text-align: center;
}
th.sorting_asc {
    text-align: center;
}

ul.nav.navbar-nav.navbar-right {
    margin-top: -17px;
}
a.dropdown-toggle.text-white {
    margin-left: 82px;
}
th.sorting_desc {
    text-align: center;
}
th.sorting_asc {
    text-align: center;
}
.table_border{
	border: 1px solid #ddd;
	border-radius: 15px;
}
    </style>
    <link rel="stylesheet" href="css/chosen.css" />