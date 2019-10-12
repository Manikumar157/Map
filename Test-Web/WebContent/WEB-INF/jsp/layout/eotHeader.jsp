<%@include file="../tag/commonTagLib.jsp"%>
<jsp:include page="common.jsp"></jsp:include>
<%
    String username=(String) session.getAttribute("username");
    if(username==null) username="";
%>
<style>
img.img_resize1 {
    width: 150px;
    height: 75px;
    margin-top: -24px;
    margin-left: 23px;
    position: absolute;
    left: -12px;
}
.text-white {
    color: #000000!important;
}
.dropdown-menu>li>a:hover, .dropdown-menu>li>a:focus {
     background-image: linear-gradient(to bottom,#030303 0,#242a2b 100%);
}
</style>
<%-- <form id="welcomeform" name="welcomeform">
<jsp:include page="../csrf_token.jsp"/> --%>
<!-- <div class="row" style="margin-top:-15px;margin-bottom:-10px;"> -->
        <!-- <div class="col-lg-3">
            <ul class="nav navbar-nav navbar-left list-unstyled list-inline text-amber date-list">
                <li><i class="fontello-th text-amber"></i>
                </li>
                <li id="Date"></li>
            </ul>
            <ul class="nav navbar-nav navbar-left list-unstyled list-inline text-amber date-list">
                <li><i class="fontello-stopwatch text-amber"></i>
                </li>
                <li id="hours"></li>
                <li class="point">:</li>
                <li id="min"></li>
                <li class="point">:</li>
                <li id="sec"></li>
            </ul>
        </div> -->
        <div class="col-lg-6">
        </div>
<form id="welcomeform" name="welcomeform">
	<jsp:include page="../csrf_token.jsp"/>
 <div class="col-lg-12 no-pad no-margin">
 <div class="col-lg-6"></div>
 <div class="col-lg-6">
           <ul class="nav navbar-nav navbar-right" style="will-change: 350px;margin-top:-20px !important;">
                <li>
                    <a data-toggle="dropdown" class="dropdown-toggle text-white" href="#">
                       <spring:message code="LABEL_WELCOME" text="Hi,"/> <%=username%>  <b class="caret"></b>
                    </a>
                    <ul style="margin:15px 15px 0 0;" role="menu" class="dropdown-setting dropdown-menu bg-amber">
                      <!--   <li>
                            <a href="#">
                                <span class="entypo-user"></span>&nbsp;&nbsp;My Profile</a>
                        </li> -->
                        <li>
                            <a href="javascript:submitForm('welcomeform','changePassword.htm')">
                                <span class="fas fa-key"></span>&nbsp;&nbsp;<spring:message code="LABEL_CHANGE_PASSWORD" text="Change Password"/></a>
                        </li>
						<li><a href="#" onclick="logout();"> <span class="fas fa-sign-out-alt"></span>&nbsp;&nbsp;<spring:message code="LABEL_LOGOUT" text="Logout "/> </a>
						</li>
                        
                    </ul>
                </li>
                 <!-- <li>
                    <a data-toggle="dropdown" class="dropdown-toggle text-white" href="#">
                        <i class="icon-gear"></i>&nbsp;Setting <b class="caret"></b>
                    </a>
                    <ul style="margin:25px 15px 0 0;" role="menu" class="theme-bg dropdown-setting dropdown-menu bg-amber">

                        <li>
                            <div id="button-bg" onclick="changeTheme('button-bg');"></div>
                        </li>
                        <li>
                            <div id="button-bg2" onclick="changeTheme('button-bg2');"></div>
                        </li>
                        <li>
                            <div id="button-bg3" onclick="changeTheme('button-bg3');"></div>
                        </li>
                        <li>
                            <div id="button-bg4" onclick="changeTheme('button-bg4');"></div>
                        </li>
                        <li>
                            <div id="button-bg5" onclick="changeTheme('button-bg5');"></div>
                        </li>
                        <li>
                            <div id="button-bg6" onclick="changeTheme('button-bg6');"></div>
                        </li>

                    </ul>
                </li> 
                    </ul>
                </li>-->

            </ul>
        </div>
        </div>
</form>
  <!--   </div> -->
    <!-- END OF TOPNAV -->
	<!-- Comtainer -->
    <div class="container-fluid" style="margin-top: -14px;">
        <!-- <div id="paper-top"> -->
        <div class="row">
                <div class="col-sm-3">

                    <a class="navbar-brand logo-text" style="padding: 0px 3px;" onclick="home();" href="#"><!-- WALLET -->
                          <%-- <img src="<%=request.getContextPath()%>/images/Easybank logo.png" alt="logo" class="img_resize1"/> --%>
                          <img src="<%=request.getContextPath()%>/images/m_gurush1.png" alt="logo" class="img_resize1"/>
                          <%-- <c:if test="${logo ne 'Core-Wallet' }">
                          <img class="img_resize1" src="data:image/png;base64,<%=session.getAttribute("logo") %>"/>
                          </c:if>
                          <c:if test="${logo eq 'Core-Wallet' }">
                          ${logo}
                          </c:if> --%>
                   <!--  <span style="height: 100px;">CIF Mobile</span>   -->
                   </a>
                    
                </div>
               
        </div>
        </div>