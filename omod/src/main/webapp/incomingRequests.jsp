<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>	
<%@ include file="/WEB-INF/template/include.jsp"%>
<openmrs:require privilege="Approve Inbound PDT Requests" otherwise="/login.htm" redirect="/module/patientdatatransfer/incomingRequests.form" />
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/localHeader.jsp"%>

<h2>
	<spring:message code="patientdatatransfer.incomingRequests" />
</h2>


<div class="patientdatatransfer">
<b class="boxHeader" style = background-color:#563d7c;><spring:message code="patientdatatransfer.incomingRequests" /></b>
<div class="box">
	<form id="incomingRequestsForm" method="post">
		<div id="incomingRequestsList" align="center">
			<table cellpadding="0" cellspacing="0" width="100%" id="incomingRequestsTable">
				<tr class="top">
					<td></td>
					<td align="left"><strong><spring:message code="patientdatatransfer.request.status"/></strong></td>
					<td align="left"><strong><spring:message code="patientdatatransfer.request.dateTimeRequested"/></strong></td>
					<td align="left"><strong><spring:message code="patientdatatransfer.request.patientIdentifier"/></strong></td>
					<td align="left"><strong><spring:message code="patientdatatransfer.request.patientName"/></strong></td>
					<td align="left"><strong><spring:message code="patientdatatransfer.request.patientBday"/></strong></td>
					<td align="left"><strong><spring:message code="patientdatatransfer.request.requestingClinic"/></strong></td>
					<td align="left"><strong><spring:message code="patientdatatransfer.request.requestedBy"/></strong></td>
					<td align="left"><strong><spring:message code="patientdatatransfer.request.consentForm"/></strong></td>
					<td align="left"><strong><spring:message code="patientdatatransfer.request.denyReason"/></strong></td>
				</tr>
				<c:forEach var="incomingRequest" items="${incomingRequestList}" varStatus="varStatus">
					<c:choose>
					<c:when test="${incomingRequest.status > 9 and incomingRequest.status <= 19}">
						<tr bgcolor="#FFA07A"> <!--  light salmon -->
					</c:when>
					<c:when test="${incomingRequest.status == 5 or incomingRequest.status == 7}">
						<tr bgcolor="#90EE90"> <!--  light green -->
					</c:when>
					<c:when test="${(varStatus.count) % 2 == 0}">
					<tr bgcolor="#E6E6E6">
					</c:when>
					<c:otherwise>
						<tr>
					</c:otherwise>
					</c:choose>
						
						<c:choose>
						<c:when test="${incomingRequest.status == 5 or incomingRequest.status == 7}">
							<td><!--  no checkbox for succeeded requests --></td>
						</c:when>
						<c:otherwise>
							<input type="hidden" name="incomingRequestIdHidden" value="${incomingRequest.id}"/>
							<td align="left"><input type="checkbox" name="incomingRequestId" value="${incomingRequest.id}"></td>
						</c:otherwise>
						</c:choose>						
						<td align="left">
							${incomingRequest.printStatus}
						</td>
						<td align="left">
							<fmt:formatDate value="${incomingRequest.dateRequested}" pattern="MM-dd-yyyy hh:mm"/>
						</td>
						<td align="left">
						<c:choose>
						<c:when test="${ not empty incomingRequest.patientIdentifier }">
							${ incomingRequest.patientIdentifier }
						</c:when>
						<c:otherwise>
							&lt; none &gt;
						</c:otherwise>
						</c:choose>
						</td>
						<td align="left">
							${incomingRequest.givenName} ${incomingRequest.middleName} ${incomingRequest.familyName} 
						</td>
						<td align="left"> 
							<fmt:formatDate value="${incomingRequest.birthDate}" pattern="MM-dd-yyyy"/>
						</td>
						<td align="left">
								${incomingRequest.requestingClinic}
						</td>
						<td align="left">
								${incomingRequest.requesterName}
						</td>
						<td align="left">
								<c:choose>
							<c:when test="${not empty incomingRequest.consentForm }">
								<a href="${incomingRequest.consentForm }"><img src="<%=request.getContextPath()%>/images/pdf.gif"/></a>
							</c:when>
							<c:otherwise>
								&lt; none &gt;
							</c:otherwise>
							</c:choose>								
						</td>
						<c:choose>
						<c:when test="${incomingRequest.status == 5 or incomingRequest.status == 7 }">
							<td><!--  no deny reason box for succeeded requests --></td>
						</c:when>
						<c:otherwise>
							<td align="left"><input type="text" name="denyReason" value=""/></td>
						</c:otherwise>
						</c:choose>
					</tr>
				</c:forEach>
				<tr style="padding: 15px;">
					<td colspan="6">
						<input type="submit" value="<spring:message code="patientdatatransfer.incomingRequests.approve"/>" onclick="return confirm('<spring:message code="patientdatatransfer.incomingRequests.approve.warning"/>')" name="action">
						<input type="submit" value="<spring:message code="patientdatatransfer.incomingRequests.deny"/>"    onclick="return confirm('<spring:message code="patientdatatransfer.incomingRequests.deny.warning"/>')" name="action">
					</td>
				</tr>
			</table>
		</div>
	</form>
</div>
</div>
<%@ include file="/WEB-INF/template/footer.jsp"%>