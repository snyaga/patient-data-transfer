<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<h1>
	<spring:message code="patientdatatransfer.home" />
</h1>


 
 <img src="<%=request.getContextPath()%>/moduleResources/patientdatatransfer/hospital-building-green.png"/>
 <br/>
 <br/>
<h2>Patient History Management</h2>
<br/>
<hr/>
<div>
<p>Welcome to the Patient Data Transfer Module! This module provides the following services: </p>
<ul id="services">
 <li>Import Patient Data from another Clinic</li>
 <li>Export Patient Data to another Clinic</li>
 </ul>
 </div>
<%@ include file="/WEB-INF/template/footer.jsp"%>