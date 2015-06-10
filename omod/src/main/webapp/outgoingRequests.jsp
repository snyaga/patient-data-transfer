<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="/WEB-INF/template/include.jsp"%>
<openmrs:require privilege="Approve Outbound PDT Requests" otherwise="/login.htm" redirect="/module/patientdatatransfer/outgoingRequests.form" />
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/localHeader.jsp"%>
<%@ page import="java.text.SimpleDateFormat" %>

<h2>
	<spring:message code="patientdatatransfer.outgoingRequests" />
</h2>
<%! SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy"); %>
<div class="patientdatatransfer">
<b class="boxHeader"><spring:message code="patientdatatransfer.outgoingRequests" /></b>
<div class="box">
	<form id="outgoingRequestsForm" method="post">
		<div id="outgoingRequestsList" align="center">
			<table cellpadding="0" cellspacing="0" width="100%" id="outgoingRequestsTable">
				<tr class="top">
					<td></td>
					<td align="left"><strong><spring:message code="patientdatatransfer.request.status"/></strong></td>
					<td align="left"><strong><spring:message code="patientdatatransfer.request.dateTimeRequested"/></strong></td>
					<td align="left"><strong><spring:message code="patientdatatransfer.request.patientIdentifier"/></strong></td>
					<td align="left"><strong><spring:message code="patientdatatransfer.request.patientName"/></strong></td>
					<td align="left"><strong><spring:message code="patientdatatransfer.request.patientBday"/></strong></td>
					<td align="left"><strong><spring:message code="patientdatatransfer.request.originalClinic"/></strong></td>
					<td align="left"><strong><spring:message code="patientdatatransfer.request.requestedBy"/></strong></td>
					<td align="left"><strong><spring:message code="patientdatatransfer.request.consentForm"/></strong></td>
					<td align="left"><strong><spring:message code="patientdatatransfer.request.denyReason"/></strong></td>
				</tr>
				<c:forEach var="outgoingRequest" items="${outgoingRequestList}" varStatus="varStatus">
					<c:choose>
					<c:when test="${outgoingRequest.status > 9  and outgoingRequest.status <= 19}">
					<tr bgcolor="#FFA07A">
					</c:when>
					<c:when test="${(varStatus.count) % 2 == 0}">
					<tr bgcolor="#E6E6E6">
					</c:when>
					<c:otherwise>
					<tr>
					</c:otherwise>
					</c:choose>
						<input type="hidden" name="outgoingRequestIdHidden" value="${outgoingRequest.id}"/>					
						<td align="left">
							<input type="checkbox" name="outgoingRequestId" value="${outgoingRequest.id}"/>
						</td>
						<td align="left">
						${outgoingRequest.printStatus}
						</td>						
						<td align="left">
							<fmt:formatDate value="${outgoingRequest.dateRequested}" pattern="MM-dd-yyyy hh:mm"/>
						</td>
						<td align="left">
						<c:choose>
						<c:when test="${ not empty outgoingRequest.patientIdentifier }">
							${ outgoingRequest.patientIdentifier }
						</c:when>
						<c:otherwise>
							&lt; none &gt;
						</c:otherwise>
						</c:choose>
						</td>
						<td align="left">
							<strong>${outgoingRequest.givenName} ${outgoingRequest.middleName} ${outgoingRequest.familyName}</strong>
						</td>
						<td align="left"> 
							<fmt:formatDate value="${outgoingRequest.birthDate}" pattern="MM-dd-yyyy"/>
						</td>
						<td align="left">
								${outgoingRequest.originalClinic}
						</td>
						<td align="left">
								${outgoingRequest.requesterName}
						</td>
						<td align="center">
							<c:choose>
							<c:when test="${not empty outgoingRequest.consentForm }">
								<a href="${consentFormBaseURL}/${outgoingRequest.consentForm }"><img src="<%=request.getContextPath()%>/images/pdf.gif"/></a>
							</c:when>
							<c:otherwise>
								&lt; none &gt;
							</c:otherwise>
							</c:choose>				
						</td>
						<td>
							<input type="text" name="denyReason" value="">
						</td>
					</tr>
				</c:forEach>
				<tr style="padding: 15px;">
					<td colspan="6">
						<input type="submit" value="<spring:message code="patientdatatransfer.outgoingRequests.approve"/>" onclick="return confirm('<spring:message code="patientdatatransfer.outgoingRequests.approve.warning"/>')" name="action">
						<input type="submit" value="<spring:message code="patientdatatransfer.outgoingRequests.deny"/>"    onclick="return confirm('<spring:message code="patientdatatransfer.outgoingRequests.deny.warning"/>')" name="action">
					</td>
				</tr>
			</table>
		</div>
	</form>
</div>
</div>
<%@ include file="/WEB-INF/template/footer.jsp"%>