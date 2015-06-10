<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<h2>
	<spring:message code="patientdatatransfer.home" />
</h2>

<%--
<h3>Database Tasks</h3>
<p><u>DEV functions:</u><br/>
	<a href="${pageContext.request.contextPath}/module/patientdatatransfer/home.form?func=addRole">Create default roles</a>
</p>
 --%>
 
 <img src="<%=request.getContextPath()%>/moduleResources/patientdatatransfer/hospital-building-green.png"/>
 <br/>Welcome to the Patient Data Transfer Module!
 
<%@ include file="/WEB-INF/template/footer.jsp"%>