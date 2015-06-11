<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<openmrs:require privilege="Create New PDT Requests" otherwise="/login.htm" redirect="/module/patientdatatransfer/newRequest.form" />
<%@ include file="template/localHeader.jsp"%>


<openmrs:htmlInclude file="/scripts/calendar/calendar.js" />
<div class="box" id="newRequestFormBox"  style="background-color: transparent; ">
	<h2 class="boxHeader" style="text-align:center; font-size:20px; background-color: ">
		<c:choose>
			<c:when test="${not empty pdr}">
				<spring:message code="patientdatatransfer.editRequest" />
			</c:when>
			<c:otherwise>
				<spring:message code="patientdatatransfer.newRequest" />
			</c:otherwise>
		</c:choose>
	</h2>
<form method="POST" enctype="multipart/form-data" style="padding-left: 25%; padding-right: 25%;">
	<table style="font-size:16px; ">
		<c:choose>
		<c:when test="${not empty pdr}">
			<input type="hidden" name="id" value="${pdr.id}" />
		</c:when>
		</c:choose>
		<tr>
			<td><spring:message code="patientdatatransfer.request.patientIdentifier" /> (<spring:message code="patientdatatransfer.newRequest.optional"/>)</td>
			<td>
				<c:choose>
				<c:when test="${not empty pdr}">
				<input type="text" name="patientIdentifier" value="${pdr.patientIdentifier}"/>
				</c:when>
				<c:otherwise>
				<input type="text" name="patientIdentifier"/>
				</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td><spring:message code="patientdatatransfer.request.patientGivenName" /></td>
			<td>
				<c:choose>
				<c:when test="${not empty pdr}">
				<input type="text" name="patientGivenName" value="${pdr.givenName}"/>
				</c:when>
				<c:otherwise>
				<input type="text" name="patientGivenName"/>
				</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td><spring:message code="patientdatatransfer.request.patientMiddleName" /> (<spring:message code="patientdatatransfer.newRequest.optional"/>)</td>
			<td>
				<c:choose>
				<c:when test="${not empty pdr}">
				<input type="text" name="patientMiddleName" value="${pdr.middleName}"/>
				</c:when>
				<c:otherwise>
				<input type="text" name="patientMiddleName"/>
				</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td><spring:message code="patientdatatransfer.request.patientFamilyName" /></td>
			<td>
				<c:choose>
				<c:when test="${not empty pdr}">
				<input type="text" name="patientFamilyName" value="${pdr.familyName}"/>
				</c:when>
				<c:otherwise>
				<input type="text" name="patientFamilyName"/>
				</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td><spring:message code="patientdatatransfer.request.patientBday" /> (DD/MM/YYYY)</td>
			<td>
				<c:choose>
				<c:when test="${not empty pdr}">
				<input type="text" name="birthDate" id="birthdate" size="11" value="<fmt:formatDate value="${pdr.birthDate}" pattern="dd/MM/yyyy"/>" onfocus="showCalendar(this,60)" onChange="clearError('birthdate')"/>
				</c:when>
				<c:otherwise>
				<input type="text" name="birthDate" id="birthdate" size="11" value="" onfocus="showCalendar(this,60)" onChange="clearError('birthdate')"/>
				</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td><spring:message code="patientdatatransfer.request.patientGender" /></td>
			<td><select name="gender">
				<c:choose>
				<c:when test="${not empty pdr and pdr.gender == 'M'}">
				<option value="M" selected="selected">Male</option>
				<option value="F">Female</option>
				</c:when>
				<c:when test="${not empty pdr and pdr.gender == 'F'}">
				<option value="M">Male</option>
				<option value="F" selected="selected">Female</option>
				</c:when>
				<c:otherwise>
				<option value="M">Male</option>
				<option value="F">Female</option>
				</c:otherwise>
				</c:choose>
			</select></td>
		</tr>
		<tr>
			<td><spring:message code="patientdatatransfer.request.patientPhoneNumber" /> (<spring:message code="patientdatatransfer.newRequest.optional"/>)</td>
			<td>
				<c:choose>
				<c:when test="${not empty pdr}">
				<input type="text" name="phoneNumber" value="${pdr.phoneNumber}"/>
				</c:when>
				<c:otherwise>
				<input type="text" name="phoneNumber"/>
				</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td><spring:message code="patientdatatransfer.request.originalClinic" /></td>
			<td><select name="originalClinic">
					<c:forEach var="clinic" items="${clinics}">
						<c:choose>
						<c:when test="${not empty pdr and pdr.originalClinic == clinic.name}">
						<option value="${ clinic.name }" selected="selected">${ clinic.name }</option>
						</c:when>
						<c:otherwise>
						<option value="${ clinic.name }">${ clinic.name }</option>
						</c:otherwise>
						</c:choose>
					</c:forEach>
				</select></td>
		</tr>
		<tr>
			<td><spring:message code="patientdatatransfer.request.consentForm" /> (<spring:message code="patientdatatransfer.newRequest.optional"/>)</td>
			<td>
				<c:choose>
				<c:when test="${not empty pdr and not empty pdr.consentForm}">
				<strong>You currently have a <a href="${consentFormBaseURL}/${pdr.consentForm }">consent form</a> uploaded. Leave the below field blank to keep the same one.</strong>
				<br/>
				<input type="file" name="consentFile"/>
				</c:when>
				<c:otherwise>
				<input type="file" name="consentFile"/>
				</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td colspan="2"><input type="checkbox" name="agreedToConsent" value="false"/><spring:message code="patientdatatransfer.newRequest.agreeText" /></td>
		</tr>
		<tr>
			<td colspan="2"><input type="submit" value="<spring:message code="patientdatatransfer.newRequest.submit"/>"/></td>
		</tr>
	</table>
</form>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>