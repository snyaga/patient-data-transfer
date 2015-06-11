<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="/WEB-INF/template/include.jsp"%>
<openmrs:require privilege="Create New PDT Requests" otherwise="/login.htm" redirect="/module/patientdatatransfer/currentRequests.form" />
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>
<%@ page import ="org.openmrs.module.patientdatatransfer.api.PatientDataTransferService" %>

<h2>
	<spring:message code="patientdatatransfer.patientDataRequests" />
</h2>

<br/>

<a href="${pageContext.request.contextPath }/module/patientdatatransfer/newRequest.form"><spring:message code="patientdatatransfer.newRequest" /></a>

<br/><br/>

<c:forEach var="pdtRequest" items="${requests}" varStatus="varStatus">
	<c:if test="${varStatus.first}">
		<b class="boxHeader" style="background-color:#563d7c;"><spring:message code="patientdatatransfer.currentPatientDataRequests" /></b>
		<div class="box" id="requestListing">
			<table cellpadding="5" cellspacing="0">
				<tdead>
					<tr>
						<td><!--  column where Edit link will appear --></td>
						<td><strong><spring:message code="patientdatatransfer.request.dateTimeRequested" /></strong></td>
						<td><strong><spring:message code="patientdatatransfer.request.patientName" /></strong></td>
						<td><strong><spring:message code="patientdatatransfer.request.patientBday" /></strong></td>
						<td><strong><spring:message code="patientdatatransfer.request.originalClinic" /></strong></td>
						<td><strong><spring:message code="patientdatatransfer.request.status" /></strong></td>
						<td><strong><spring:message code="patientdatatransfer.request.approver" /></strong></td>
						<td><strong><spring:message code="patientdatatransfer.request.denyReason" /></strong></td>
					</tr>
				</thead>
				<tbody>
	</c:if>
				<c:choose>
					<c:when test="${pdtRequest.status > 9}">
					<tr bgcolor="#FFA07A"> <!--  light salmon -->
					</c:when>
					<c:when test="${pdtRequest.status == 5 || pdtRequest.status == 6}">
					<tr bgcolor="#90EE90"> <!--  light green -->
					</c:when>
					<c:when test="${(varStatus.count) % 2 == 0}">
					<tr bgcolor="#E6E6E6">
					</c:when>
					<c:otherwise>
					<tr>
					</c:otherwise>
				</c:choose>
					<td>
						<c:if test="${pdtRequest.status == 1 or pdtRequest.status == 11 or pdtRequest.status == 13}">
						<a href= "${pageContext.request.contextPath}/module/patientdatatransfer/newRequest.form?id=${pdtRequest.id}"><spring:message code="patientdatatransfer.currentRequests.edit" /></a>
						</c:if>
					</td>
					<td>
						<fmt:formatDate value="${pdtRequest.dateRequested}" pattern="MM-dd-yyyy hh:mm"/>
					</td>
					<td>${ pdtRequest.givenName } ${ pdtRequest.middleName } ${ pdtRequest.familyName }</td>
					<td>
						<fmt:formatDate value="${pdtRequest.birthDate}" pattern="MM-dd-yyyy"/>
					</td>
					<td>${ pdtRequest.originalClinic }</td>
					<td>${ pdtRequest.printStatus }</td>
					<c:choose>
					<c:when test="${pdtRequest.signedBy == 0}">
						<td>&lt; none yet &gt;</td>
					</c:when>
					<c:otherwise>
						<td>${ pdtRequest.signerName }</td>
					</c:otherwise>
					</c:choose>
					<c:choose>
					<c:when test="${pdtRequest.status == 11 }"> <!-- denied locally -->
						<td>${ pdtRequest.signerMessage }</td>
					</c:when>
					<c:when test="${pdtRequest.status == 13 }"> <!-- denied remotely -->
						<td>${ pdtRequest.remoteMessage }</td>
					</c:when>
					<c:otherwise>
						<td>n/a</td>
					</c:otherwise>
					</c:choose>
				</tr>
				
	<c:if test="${varStatus.last}">
			</tbody>
			</table>
		</div>
	</c:if>
	
</c:forEach>

<%@ include file="/WEB-INF/template/footer.jsp"%>