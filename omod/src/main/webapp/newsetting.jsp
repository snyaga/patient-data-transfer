<%@ include file="/WEB-INF/template/include.jsp"%>
<openmrs:require privilege="Manage the PDT System" otherwise="/login.htm" redirect="/module/patientdatatransfer/settings.form" />
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/localHeader.jsp"%>

<h2>
	<spring:message code="patientdatatransfer.settings" />
</h2>


<div class="patientdatatransfer">
<b class="boxHeader"><spring:message code="patientdatatransfer.settings.addSetting"/></b>
<div class="box">
	<form id="newSettingForm" method="post">
		<div id="newSettingDiv" align="center">
			<table cellpadding="6" cellspacing="5" width="100%" id="newSettingTable">
				<tr>
					<td></td>
					<td><spring:message code="patientdatatransfer.settings.settingName"/></td>
					<td><input type="text" size="32" maxlength="256" name="settingName"></td>
				</tr>
				<tr>
					<td></td>
					<td valign="top"><spring:message code="patientdatatransfer.settings.settingValue"/></td>
					<td>
						<textarea name="settingValue" rows="5" cols="32"></textarea>
					</td>						
				</tr>
			</table>
			<br/>
		</div>
		<input type="submit" value="<spring:message code="patientdatatransfer.settings.submitChanges"/>" name="action">
		<input type="reset" value="<spring:message code="patientdatatransfer.settings.undoChanges"/>" 		name="unaction">
	</form>
	<hr/>
	<a href="${pageContext.request.contextPath}/module/patientdatatransfer/newclinic.form"><spring:message code="patientdatatransfer.settings.addClinic"/></a>
	<a href="${pageContext.request.contextPath}/module/patientdatatransfer/settings.form"><spring:message code="patientdatatransfer.settings.title"/></a>
</div>
</div>
<%@ include file="/WEB-INF/template/footer.jsp"%>