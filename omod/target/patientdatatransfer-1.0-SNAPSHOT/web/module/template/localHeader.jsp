<spring:htmlEscape defaultHtmlEscape="true" />
<ul id="menu">
	<li class="first">
		<a href="${pageContext.request.contextPath}/admin"><spring:message
				code="admin.title.short" />
		</a>
	</li>
	<li <c:if test='<%= request.getRequestURI().contains("/home") %>'> active</c:if>">
	<a href="${pageContext.request.contextPath}/module/patientdatatransfer/home.form">
		<spring:message code="patientdatatransfer.home" />
	</a>
	</li>
	<li <c:if test='<%= request.getRequestURI().contains("/recentTransactions") %>'>class="active"</c:if>>
		<a href="${pageContext.request.contextPath }/module/patientdatatransfer/recentTransactions.form">
			<spring:message code="patientdatatransfer.recentTransactions" />
		</a>
	</li>
	<li <c:if test='<%= request.getRequestURI().contains("/currentRequests") %>'>class="active"</c:if>>
		<a href="${pageContext.request.contextPath }/module/patientdatatransfer/currentRequests.form">
			<spring:message code="patientdatatransfer.patientDataRequests" />
		</a>
	</li>
	<li <c:if test='<%= request.getRequestURI().contains("/outgoingRequests") %>'>class="active"</c:if>>
		<a href="${pageContext.request.contextPath }/module/patientdatatransfer/outgoingRequests.form">
			<spring:message code="patientdatatransfer.outgoingRequests" />
		</a>
	</li>
	<li <c:if test='<%= request.getRequestURI().contains("/incomingRequests") %>'>class="active"</c:if>>
		<a href="${pageContext.request.contextPath }/module/patientdatatransfer/incomingRequests.form">
			<spring:message code="patientdatatransfer.incomingRequests" />
		</a>
		<pdt:first />
	</li>
	<li <c:if test='<%= request.getRequestURI().contains("/settings") %>'> active</c:if>">
	<a href="${pageContext.request.contextPath}/module/patientdatatransfer/settings.form">
		<spring:message code="patientdatatransfer.settings" />
	</a>
	</li>
	<li <c:if test='<%= request.getRequestURI().contains("/clinicOverview") %>'> active</c:if>">
	<a href="${pageContext.request.contextPath}/module/patientdatatransfer/clinicOverview.form">
		<spring:message code="patientdatatransfer.clinicsOverview" />
	</a>
	</li>
</ul>
