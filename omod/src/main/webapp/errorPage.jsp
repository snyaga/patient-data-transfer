<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<h2>
	Error
</h2>

<p>
	<c:out value="${ errorMessage }" />
	
</p>

<%@ include file="/WEB-INF/template/footer.jsp"%>