<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<p>Hello ${user.systemId}!</p>

<form method="POST" action="<c:url value="/module/patientdatatransfer/manage" />">
Patient ID:
<input type="text" name="patientId"/><br/>

    <input type="submit" value="Search"/>
</form>

<%@ include file="/WEB-INF/template/footer.jsp"%>