<%@ include file="/WEB-INF/template/include.jsp"%>
<openmrs:require privilege="Manage the PDT System" otherwise="/login.htm" redirect="/admin/patientdatatransfer/settings.form" />
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/localHeader.jsp"%>




<div class="patientdatatransfer" style="background-color: transparent;">
	<h2 style="text-align:center; font-size:20px;">
		<spring:message code="patientdatatransfer.settings" />
	</h2>
<b class="boxHeader" style="text-align:center; font-size:20px; background-color:#563d7c; "><spring:message code="patientdatatransfer.settings.addClinic"/></b>
<div class="box">
	<form id="newClinicForm" method="post" style="padding-left: 25%; padding-right: 25%;">
		<div id="newClinicDiv" align="center">

			<table  style="border: magenta" cellspacing="10" width="100%" id="newClinicTable">
				<tr>
					<td><spring:message code="patientdatatransfer.settings.clinicName"/></td>
					<td><input type="text" size="32" maxlength="256" name="clinicName"></td>
				</tr>
				<tr>
					<td><spring:message code="patientdatatransfer.settings.clinicDesc"/></td>
					<td><input type="text" size="32" maxlength="256" name="clinicDesc"></td>
				</tr>
				<tr>

					<td><spring:message code="patientdatatransfer.settings.clinicIPAddress"/></td>
					<td><input type="text" size="32" maxlength="256" name="clinicIpAddress"></td>
				</tr>
				<tr>

					<td><spring:message code="patientdatatransfer.settings.clinicPort"/></td>
					<td><input type="text" size="5" maxlength="5" name="clinicPort"></td>
				</tr>
				<tr>

					<td><spring:message code="patientdatatransfer.settings.clinicUrlPrefix"/></td>
					<td><input type="text" size="32" maxlength="256" name="clinicUrlPrefix"></td>
				</tr>
				<tr>

					<td><spring:message code="patientdatatransfer.settings.clinicLatitude"/></td>
					<td><input type="text"  size="32" maxlength="32" name="clinicLatitude"></td>
				</tr>
				<tr>

					<td><spring:message code="patientdatatransfer.settings.clinicLongitude"/></td>
					<td><input type="text"  size="32" maxlength="32" name="clinicLongitude"></td>
				</tr>
				
			</table>
		</div>
		<br/>
		<hr/>
		<br/>
		<div align="center">
		<input type="submit" value="<spring:message code="patientdatatransfer.settings.submitChanges"/>"  	name="action">
		<input type="reset" value="<spring:message code="patientdatatransfer.settings.undoChanges"/>" 		name="unaction">
	</div>
	</form>
	<br/>
	<hr/>
	<a href="${pageContext.request.contextPath}/module/patientdatatransfer/settings.form"><spring:message code="patientdatatransfer.settings.title"/></a>
</div>
</div>
<%@ include file="/WEB-INF/template/footer.jsp"%>