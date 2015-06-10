<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="/WEB-INF/template/include.jsp"%>
<openmrs:require privilege="Manage the PDT System" otherwise="/login.htm" redirect="/module/patientdatatransfer/recentTransactions.form"/>


<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<h2>
	<spring:message code="patientdatatransfer.recentTransactions" />
</h2>
<div class="patientdatatransfer">
<b class="boxHeader"><spring:message code="patientdatatransfer.recentTransactions.incomingTitle" /></b>
<div class="box">
	<form class="recentTransactionForm" method="post">
		<div class="recentTransactionsList" align="center">
			<table cellpadding="0" cellspacing="0" width="100%" class="recentTransactionsTable">
				<tr>
					<td align="left"><strong><spring:message code="patientdatatransfer.request.dateTimeTransferred"/></strong></td>
					<td align="left"><strong><spring:message code="patientdatatransfer.request.originalClinic"/></strong></td>
					<td align="left"><strong><spring:message code="patientdatatransfer.request.requestedBy"/></strong></td>
					<td align="left"><strong><spring:message code="patientdatatransfer.request.patientName"/></strong></td>
					<td align="left"><strong><spring:message code="patientdatatransfer.request.patientBday"/></strong></td>
				</tr>
				<c:forEach var="recentIncoming" items="${incomingRecentTransactions}" varStatus="varStatus">				
					<c:choose>
					<c:when test="${recentIncoming.status > 9}">
					<tr bgcolor="#FFA07A">
					</c:when>
					<c:when test="${(varStatus.count) % 2 == 0}">
					<tr bgcolor="#E6E6E6">
					</c:when>
					<c:otherwise>
					<tr>
					</c:otherwise>
					</c:choose>
						<td valign="top" align="left">
							<fmt:formatDate value="${recentIncoming.dateRequested}" pattern="MM-dd-yyyy hh:mm"/>
						</td>
						<td valign="top" align="left">
							${ recentIncoming.originalClinic }
						</td>
						<td valign="top" align="left">
							${ recentIncoming.requesterName }
						</td>
						<td valign="top" align="left">
							${recentIncoming.givenName} ${recentIncoming.middleName } ${recentIncoming.familyName}
						</td>
						<td valign="top" align="left">
							<fmt:formatDate value="${recentIncoming.birthDate}" pattern="MM-dd-yyyy"/>							
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</form>
</div>
</div>
<br />
<br />

<!-- This is where the second table begins -->
<div class="patientdatatransfer">
<b class="boxHeader"><spring:message code="patientdatatransfer.recentTransactions.outgoingTitle" /></b>
<div class="box">
	<form class="recentTransactionForm" method="post">
		<div class="recentTransactionsList" align="center">
			<table cellpadding="0" cellspacing="0" width="100%" class="recentTransactionsTable">
				<tr>
					<td align="left"><strong><spring:message code="patientdatatransfer.request.dateTimeTransferred"/></strong></td>
					<td align="left"><strong><spring:message code="patientdatatransfer.request.originalClinic"/></strong></td>
					<td align="left"><strong><spring:message code="patientdatatransfer.request.requestedBy"/></strong></td>
					<td align="left"><strong><spring:message code="patientdatatransfer.request.patientName"/></strong></td>
					<td align="left"><strong><spring:message code="patientdatatransfer.request.patientBday"/></strong></td>
				</tr>
				<c:forEach var="recentOutgoing" items="${outgoingRecentTransactions}" varStatus="varStatus">
					<c:choose>
					<c:when test="${(varStatus.count) % 2 == 0}">
					<tr bgcolor="#E6E6E6">
					</c:when>
					<c:otherwise>
					<tr>
					</c:otherwise>
					</c:choose>				
						<td valign="top" align="left">
							<fmt:formatDate value="${recentOutgoing.dateRequested}" pattern="MM-dd-yyyy hh:mm"/>
							<%--${recentOutgoing.dateTransferred}--%>
						</td>						
						<td valign="top" align="left">
							${ recentOutgoing.requestingClinic }
						</td>
						<td valign="top" align="left">
							${ recentOutgoing.requesterName }
						</td>
						<td valign="top" align="left">
							${recentOutgoing.givenName} ${recentOutgoing.middleName } ${recentOutgoing.familyName}
						</td>
						<td valign="top" align="left">
							<fmt:formatDate value="${recentOutgoing.birthDate}" pattern="MM-dd-yyyy"/>							
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</form>
</div>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>