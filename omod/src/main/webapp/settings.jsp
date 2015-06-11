<%@ include file="/WEB-INF/template/include.jsp"%>
<openmrs:require privilege="Manage the PDT System" otherwise="/login.htm" redirect="/module/patientdatatransfer/settings.form" />
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/localHeader.jsp"%>

<h2>
	<spring:message code="patientdatatransfer.settings" />
</h2>



<br/><br/>

<a href="${pageContext.request.contextPath}/module/patientdatatransfer/newclinic.form"><spring:message code="patientdatatransfer.settings.addClinic"/></a>

<br/><br/>

<div class="patientdatatransfer">
<form id="clinicsForm" method="post">
<b class="boxHeader"><spring:message code="patientdatatransfer.settings.clinics" /></b>
	<div class="box">
		<div id="clinicsDiv" align="center">
			<table cellpadding="0" cellspacing="0" width="100%" id="clinicsTable">
				<tr>
					<td align="left"><strong><spring:message code="patientdatatransfer.settings.clinicName" /></strong></td>
					<td align="left"><strong><spring:message code="patientdatatransfer.settings.clinicIPAddress" /></strong></td>
					<td align="left"><strong><spring:message code="patientdatatransfer.settings.clinicPort" /></strong></td>
					<td align="left"><strong><spring:message code="patientdatatransfer.settings.clinicUrlPrefix" /></strong></td>
					<td align="left"><strong><spring:message code="patientdatatransfer.settings.deleteClinic" /></strong></td>
				</tr>
				<c:forEach var="clinic" items="${clinicsList}" varStatus="varStatus">				
				<c:choose>
					<c:when test="${(varStatus.count) % 2 == 0}">
					<tr bgcolor="#E6E6E6">
					</c:when>
					<c:otherwise>
					<tr>
					</c:otherwise>
				</c:choose>
					<td align="left">
						${clinic.name}
					</td>
					<td align="left">
 						${clinic.ipAddress}
					</td>
					<td align="left">
						${clinic.port}
					</td>
					<td align="left">
 						${clinic.urlPrefix}
					</td>
					<td align="left">
						<input type=checkbox name=clinicsToRemoveId value=${clinic.id} />
					</td>
				</tr>
				</c:forEach>
			</table>	
		</div>
		<input type="submit" value="<spring:message code="patientdatatransfer.settings.submitChanges"/>" onclick="return confirm('<spring:message code="patientdatatransfer.settings.submitChanges.warning"/>')" name="action" />
		<input type="reset" value="<spring:message code="patientdatatransfer.settings.undoChanges"/>" name="unaction" />
	</div>
</form>
</div>
<br/><br/>



<%@ include file="/WEB-INF/template/footer.jsp"%>