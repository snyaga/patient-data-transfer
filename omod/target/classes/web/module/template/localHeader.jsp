<spring:htmlEscape defaultHtmlEscape="true" />
<ul id="menu">
	<li class="first"><a
		href="${pageContext.request.contextPath}/admin"><spring:message
				code="admin.title.short" /></a></li>

	<li
		<c:if test='<%= request.getRequestURI().contains("/manage") %>'>class="active"</c:if>>
		<a
		href="${pageContext.request.contextPath}/module/patientdatatransfer/manage.form"><spring:message
				code="patientdatatransfer.manage" /></a>
	</li>
	
	<!-- Add further links here -->
	<li
			<c:if test='<%= request.getRequestURI().contains("/basicInfo") %>'>class="active"</c:if>>
		<a
				href="${pageContext.request.contextPath}/module/patientdatatransfer/basicInfo.form"><spring:message
				code="patientdatatransfer.basicInfo" /></a>
	</li>

	<li
			<c:if test='<%= request.getRequestURI().contains("/exportInfo") %>'>class="active"</c:if>>
		<a
				href="${pageContext.request.contextPath}/module/patientdatatransfer/exportInfo.form"><spring:message
				code="patientdatatransfer.exportInfo" /></a>
	</li>

	<li>
			<c:if test='<%= request.getRequestURI().contains("/summary") %>'>class="active"</c:if>>
		<a
				href="${pageContext.request.contextPath}/module/patientdatatransfer/summary.form"><spring:message
				code="patientdatatransfer.summary" /></a>
	</li>
</ul>
<h2>
	<spring:message code="patientdatatransfer.title" />
</h2>
