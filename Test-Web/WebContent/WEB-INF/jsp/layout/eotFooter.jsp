<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<style>
.footer {
    position: relative;
    left: 0;
    bottom: 0;
    width: 100%;
    color: black;
    text-align: right;
}
</style>
<!-- FOOTER -->
        <!-- <div id="footer" > -->
            <!-- <div class="devider-footer-left"></div>
            <div class="time">
                <p id="spanDate"></p>
                <p id="clock"></p>
            </div> -->
            <div class="footer"><spring:message code="LABEL_VERSION_COPYRIGHT" text="v"/>${version}<spring:message code="LABEL_COPYRIGHT" text="Copyright"/> &copy; <spring:message code="LABEL_COPYRIGHT_STRING" text="2019 All Rights Reserved Trinity Technologies"/></div>
        <!-- </div> -->
<!-- / FOOTER -->