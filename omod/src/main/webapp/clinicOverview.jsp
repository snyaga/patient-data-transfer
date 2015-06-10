<%-- from WEB-INF/template/include.jsp, essentially "include"ing it by copy-paste --%>
<%@ taglib prefix="c" uri="/WEB-INF/taglibs/c-rt.tld" %>
<%@ taglib prefix="openmrs" uri="/WEB-INF/taglibs/openmrs.tld" %>
<%@ taglib prefix="openmrs_tag" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="page" uri="/WEB-INF/taglibs/page.tld" %>
<%@ taglib prefix="request" uri="/WEB-INF/taglibs/request.tld" %>
<%@ taglib prefix="response" uri="/WEB-INF/taglibs/response.tld" %>
<%@ taglib prefix="spring" uri="/WEB-INF/taglibs/spring.tld" %>
<%@ taglib prefix="fn" uri="/WEB-INF/taglibs/fn.tld" %>
<%@ taglib prefix="form" uri="/WEB-INF/taglibs/spring-form.tld" %>

<%-- from WEB-INF/template/header.jsp, essentially "include"ing it by copy-paste --%>
<%-- this was done so I can add details to HEAD section --%>
<%@ page errorPage="/errorhandler.jsp" %>
<%@ page import="org.openmrs.web.WebConstants" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page import="org.openmrs.web.WebConstants" %>
<%
	pageContext.setAttribute("msg", session.getAttribute(WebConstants.OPENMRS_MSG_ATTR));
	pageContext.setAttribute("msgArgs", session.getAttribute(WebConstants.OPENMRS_MSG_ARGS));
	pageContext.setAttribute("err", session.getAttribute(WebConstants.OPENMRS_ERROR_ATTR));
	pageContext.setAttribute("errArgs", session.getAttribute(WebConstants.OPENMRS_ERROR_ARGS));
	session.removeAttribute(WebConstants.OPENMRS_MSG_ATTR);
	session.removeAttribute(WebConstants.OPENMRS_MSG_ARGS);
	session.removeAttribute(WebConstants.OPENMRS_ERROR_ATTR);
	session.removeAttribute(WebConstants.OPENMRS_ERROR_ARGS);
%>

<openmrs:require privilege="Manage the PDT System" otherwise="/login.htm" redirect="/module/patientdatatransfer/clinicOverview.form"/>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<openmrs:htmlInclude file="/openmrs.js" />
		<openmrs:htmlInclude file="/scripts/openmrsmessages.js" appendLocale="true" />
		<openmrs:htmlInclude file="/openmrs.css" />
		<link href="<openmrs:contextPath/><spring:theme code='stylesheet' />" type="text/css" rel="stylesheet" />
		<openmrs:htmlInclude file="/style.css" />
		<openmrs:htmlInclude file="/dwr/engine.js" />
		<openmrs:htmlInclude file="/dwr/interface/DWRAlertService.js" />
		<c:if test="${empty DO_NOT_INCLUDE_JQUERY}">
			<openmrs:htmlInclude file="/scripts/jquery/jquery.min.js" />
			<openmrs:htmlInclude file="/scripts/jquery-ui/js/jquery-ui.custom.min.js" />
            <openmrs:htmlInclude file="/scripts/jquery-ui/js/jquery-ui-timepicker-addon.js" />
			<openmrs:htmlInclude file="/scripts/jquery-ui/js/jquery-ui-datepicker-i18n.js" />
			<openmrs:htmlInclude file="/scripts/jquery-ui/js/jquery-ui-timepicker-i18n.js" />
			<link href="<openmrs:contextPath/>/scripts/jquery-ui/css/<spring:theme code='jqueryui.theme.name' />/jquery-ui.custom.css" type="text/css" rel="stylesheet" />
		</c:if>
		<link rel="shortcut icon" type="image/ico" href="<openmrs:contextPath/><spring:theme code='favicon' />">
		<link rel="icon" type="image/png" href="<openmrs:contextPath/><spring:theme code='favicon.png' />">

		<c:choose>
			<c:when test="${!empty pageTitle}">
				<title>${pageTitle}</title>
			</c:when>
			<c:otherwise>
				<title><spring:message code="openmrs.title"/></title>
			</c:otherwise>
		</c:choose>


		<script type="text/javascript">
			<c:if test="${empty DO_NOT_INCLUDE_JQUERY}">
				var $j = jQuery.noConflict();
			</c:if>
			/* variable used in js to know the context path */
			var openmrsContextPath = '${pageContext.request.contextPath}';
			var dwrLoadingMessage = '<spring:message code="general.loading" />';
			var jsDateFormat = '<openmrs:datePattern localize="false"/>';
			var jsTimeFormat = '<openmrs:timePattern format="jquery" localize="false"/>';
			var jsLocale = '<%= org.openmrs.api.context.Context.getLocale() %>';
			
			/* prevents users getting false dwr errors msgs when leaving pages */
			var pageIsExiting = false;
			if (jQuery)
			    jQuery(window).bind('beforeunload', function () { pageIsExiting = true; } );
			
			var handler = function(msg, ex) {
				if (!pageIsExiting) {
					var div = document.getElementById("openmrs_dwr_error");
					div.style.display = ""; // show the error div
					var msgDiv = document.getElementById("openmrs_dwr_error_msg");
					msgDiv.innerHTML = '<spring:message code="error.dwr"/>' + " <b>" + msg + "</b>";
				}
				
			};
			dwr.engine.setErrorHandler(handler);
			dwr.engine.setWarningHandler(handler);
		</script>

		<openmrs:extensionPoint pointId="org.openmrs.headerFullIncludeExt" type="html" requiredClass="org.openmrs.module.web.extension.HeaderIncludeExt">
			<c:forEach var="file" items="${extension.headerFiles}">
				<openmrs:htmlInclude file="${file}" />
			</c:forEach>
		</openmrs:extensionPoint>


<script type="text/javascript">
   //<![CDATA[

/*
 * Returns a function that waits for the specified XMLHttpRequest
 * to complete, then passes its XML response
 * to the given handler function.
 * req - The XMLHttpRequest whose state is changing
 * responseXmlHandler - Function to pass the XML response to
 */
function getCallbackFunction(req, processData) {

	// Return an anonymous function that listens to the 
	// XMLHttpRequest instance
	return function() {

		// If the request's status is "complete"
		if (req.readyState == 4) {
			if (req.status == 200) {
				// Pass the XML payload of the response to the 
				// handler function
				processData(req.responseXML);
			} else {
				// An HTTP problem has occurred
				alert("HTTP error: " + req.status);
			}
		}
	}
}

//]]>
</script>
	
<!-- GOOGLE MAPS -->
<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=AIzaSyDluKd2MvJo4Y8qzCC6bSAybqrTdrbgTUI"
  type="text/javascript"></script>
            
<script type="text/javascript">
//<![CDATA[
	
// stores all the GMarkers ( the markers on the map )    
var gmarkers = [];

// given the id of a clinic, find the GMarker object which corresponds to it
function getMarker(id) {
	for ( var i = 0; i <= gmarkers.length; i++) {
		var marker = gmarkers[i];
		if (marker.id == id) {
			return marker;
		}
	}
	return null;
}

// Entry-point for the Google Maps/Clinic overview code -- called from body onLoad.
function load() {
	if (GBrowserIsCompatible()) {
		var i = 0;

		// A function to create the marker and set up the event window
		function createClinicMarker(point, name, id, isRevoked) {
			// set up a red and green hospital icon to use for displaying clinics on the map
			var redIcon = new GIcon();
			redIcon.iconSize = new GSize(32, 37);
			redIcon.iconAnchor = new GPoint(16, 18);
			redIcon.infoWindowAnchor = new GPoint(16, 18);
			redIcon.image = "<%=request.getContextPath()%>/moduleResources/patientdatatransfer/hospital-building-red.png";
			var greenIcon = new GIcon();
			greenIcon.iconSize = new GSize(32, 37);
			greenIcon.iconAnchor = new GPoint(16, 18);
			greenIcon.infoWindowAnchor = new GPoint(16, 18);
			greenIcon.image = "<%=request.getContextPath()%>/moduleResources/patientdatatransfer/hospital-building-green.png";

			var marker;
			// Create the GMarker object, using the red or green icon
			// based on the isRevoked parameter to this function.
			if (isRevoked) {
				marker = new GMarker(point, redIcon);
			} else {
				marker = new GMarker(point, greenIcon);
			}
			marker.tooltip = '<div class="tooltip">' + name + '</div>';

			marker.id = id;
			GEvent.addListener(marker, "click",	function() {
				displayDetails(marker, "" + id);
			});
			
			// save the newly created GMarker to the main array, and increment the i counter
			gmarkers[i] = marker;
			i++;

			// Actually add the marker to the map
			map.addOverlay(marker);

			//  Setup the mouseover/mouseout listeners for tooltip display
			GEvent.addListener(marker, "mouseover",	function() {
				showTooltip(marker);
			});
			GEvent.addListener(marker, "mouseout", function() {
				tooltip.style.visibility = "hidden";
			});
		}

		/*
		 * The list of clinic markers, loaded only once.
		 * The load once is enforced by the below function.
		 */		
		var clinicMarkers = null;
		function displayClinics() {
			if (clinicMarkers == null) {
				loadClinics();
			} else {
				displayClinicsMarkers();
			}
		}

		/*
		 * Load and display markers for all the clinics via AJAX request
		 */
		function loadClinics() {
			var request = GXmlHttp.create();
			request.open("GET", "${clinicURL}/", true); // clinicURL populated by Spring Controller
			// This request header forces Spring to serialize to XML as opposed to JSON
			request.setRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			request.onreadystatechange = getCallbackFunction(request, processClinicData);
			request.send(null);
		}

		/*
		 * Process clinic XML data, store it in clinicMarkers, and display the markers
		 */
		function processClinicData(xmlDoc) {
			// obtain the array of clinics and save it for later use
			clinicMarkers = xmlDoc.documentElement.getElementsByTagName("org.openmrs.module.patientdatatransfer.PDTClinic");
			// display the clinics
			displayClinicsMarkers();
		}

		/*
		 * Create markers for clinics
		 */
		function displayClinicsMarkers() {
			// ensure we are starting from scratch
			map.clearOverlays();
			
			for ( var i = 0; i < clinicMarkers.length; i++) {
				// obtain the attribues of each marker
				var lat = parseFloat(clinicMarkers[i].getElementsByTagName("latitude")[0].firstChild.nodeValue);
				var lng = parseFloat(clinicMarkers[i].getElementsByTagName("longitude")[0].firstChild.nodeValue);
				var id = clinicMarkers[i].getElementsByTagName("id")[0].firstChild.nodeValue;
				var label = clinicMarkers[i].getElementsByTagName("name")[0].firstChild.nodeValue;
				var port = clinicMarkers[i].getElementsByTagName("port")[0].firstChild.nodeValue;
				var revoked = clinicMarkers[i].getElementsByTagName("isRevoked")[0].firstChild.nodeValue;
				revoked = (revoked == "true") ? true : false;
				
				createClinicMarker(new GLatLng(lat, lng), label, id, revoked);
			}
		}

		/*
		 * Open the text info window to display details for a given clinic marker
		 *
		 */
		function displayDetails(marker, id) {
			for ( var i = 0; i < clinicMarkers.length; i++) {
				var currentId = clinicMarkers[i].getElementsByTagName("id")[0].firstChild.nodeValue;
				// loop until we find the correct id
				if (currentId == id) {
					var name = clinicMarkers[i].getElementsByTagName("name")[0].firstChild.nodeValue;
					var ipAddress = clinicMarkers[i].getElementsByTagName("ipAddress")[0].firstChild.nodeValue;
					var port = clinicMarkers[i].getElementsByTagName("port")[0].firstChild.nodeValue;
					var urlPrefix = clinicMarkers[i].getElementsByTagName("urlPrefix")[0].firstChild.nodeValue;
					var revoked = clinicMarkers[i].getElementsByTagName("isRevoked")[0].firstChild.nodeValue;
					revoked = (revoked == "true") ? true : false;

					marker.showMapBlowup();
					var html = '<b>' + name + '</b><br/>'
							+ ipAddress
							+ ':' + port + '<br/>'
							+ '/' + urlPrefix + '/' + '<br/>';
					if (revoked) {
						html += 'Certificate <font color=\'red\'>REVOKED</font>';
					} else {
						html += 'Certificate <font color=\'green\'>VALID</font>';
					}
					marker.openInfoWindowHtml(html);
				}
			}
		}

		/*
		 * This function displays the tooltip
		 */
		function showTooltip(marker) {
			tooltip.innerHTML = marker.tooltip;
			var point = map.getCurrentMapType().getProjection().fromLatLngToPixel(map.fromDivPixelToLatLng(new GPoint(0, 0),true), map.getZoom());
			var offset = map.getCurrentMapType().getProjection().fromLatLngToPixel(marker.getPoint(), map.getZoom());
			var anchor = marker.getIcon().iconAnchor;
			var width = marker.getIcon().iconSize.width;
			var height = tooltip.clientHeight;
			var pos = new GControlPosition(G_ANCHOR_TOP_LEFT, new GSize(offset.x - point.x - anchor.x + width,
											offset.y - point.y - anchor.y - height));
			pos.apply(tooltip);
			tooltip.style.visibility = "visible";
		}

		/*
		 * This function is invoked when the mouse leaves an entry in the sidebar
		 * It hides the tooltip
		 */
		function mymouseout() {
			tooltip.style.visibility = "hidden";
		}

		// create the map
		var map = new GMap2(document.getElementById("map"));

		map.addControl(new GLargeMapControl());
		map.addControl(new GOverviewMapControl());
		map.enableDoubleClickZoom();
		map.setCenter(new GLatLng(-1.940278, 29.873889), 5); // center at Rwanda

		// set up marker mouseover tooltip div
		var tooltip = document.createElement("div");
		map.getPane(G_MAP_FLOAT_PANE).appendChild(tooltip);
		tooltip.style.visibility = "hidden";

		// display clinics after map is created
		displayClinics();
	}
	else {
		alert("Sorry, the Google Maps API is not compatible with this browser");
	}
}
		//]]>
	</script>
</head>

<body  onload="load()" onunload="GUnload()">
	<div id="pageBody">
        
		<div id="userBar">
			<openmrs:authentication>
				<c:if test="${authenticatedUser != null}">
					<span id="userLoggedInAs" class="firstChild">
						<spring:message code="header.logged.in"/> ${authenticatedUser.personName}
					</span>
					<span id="userLogout">
						<a href='${pageContext.request.contextPath}/logout'><spring:message code="header.logout" /></a>
					</span>
					<span>
						<a href="${pageContext.request.contextPath}/options.form"><spring:message code="Navigation.options"/></a>
					</span>
				</c:if>
				<c:if test="${authenticatedUser == null}">
					<span id="userLoggedOut" class="firstChild">
						<spring:message code="header.logged.out"/>
					</span>
					<span id="userLogIn">
						<a href='${pageContext.request.contextPath}/login.htm'><spring:message code="header.login"/></a>
					</span>
				</c:if>
			</openmrs:authentication>

			<span id="userHelp">
				<a href='<%= request.getContextPath() %>/help.htm'><spring:message code="header.help"/></a>
			</span>
			<openmrs:extensionPoint pointId="org.openmrs.headerFull.userBar" type="html">
				<openmrs:hasPrivilege privilege="${extension.requiredPrivilege}">
					<span>
						<a href="${extension.url}"><spring:message code="${extension.label}"/></a>
					</span>
					<c:if test="${extension.portletUrl != null}">
						<openmrs:portlet url="${extension.portletUrl}" moduleId="${extension.moduleId}" id="${extension.portletUrl}" />
					</c:if>
				</openmrs:hasPrivilege>
			</openmrs:extensionPoint>
		</div>

		<%@ include file="/WEB-INF/template/banner.jsp" %>

		<%-- This is where the My Patients popup used to be. I'm leaving this placeholder here
			as a reminder of where to put back an extension point when I've figured out what it should
			look like. -DJ
		<div id="popupTray">
		</div>
		--%>

		<div id="content">

			<openmrs:forEachAlert>
				<c:if test="${varStatus.first}"><div id="alertOuterBox"><div id="alertInnerBox"></c:if>
					<div class="alert">
						<a href="#markRead" onClick="return markAlertRead(this, '${alert.alertId}')" HIDEFOCUS class="markAlertRead">
							<img src="${pageContext.request.contextPath}/images/markRead.gif" alt='<spring:message code="Alert.mark"/>' title='<spring:message code="Alert.mark"/>'/> <span class="markAlertText"><spring:message code="Alert.markAsRead"/></span>
						</a>
						${alert.text} ${alert.dateToExpire} <c:if test="${alert.satisfiedByAny}"><i class="smallMessage">(<spring:message code="Alert.mark.satisfiedByAny"/>)</i></c:if>
					</div>
				<c:if test="${varStatus.last}">
					</div>
					<div id="alertBar">
						<img src="${pageContext.request.contextPath}/images/alert.gif" align="center" alt='<spring:message code="Alert.unreadAlert"/>' title='<spring:message code="Alert.unreadAlert"/>'/>
						<c:if test="${varStatus.count == 1}"><spring:message code="Alert.unreadAlert"/></c:if>
						<c:if test="${varStatus.count != 1}"><spring:message code="Alert.unreadAlerts" arguments="${varStatus.count}" /></c:if>
					</div>
					</div>
				</c:if>
			</openmrs:forEachAlert>

			<c:if test="${msg != null}">
				<div id="openmrs_msg"><spring:message code="${msg}" text="${msg}" arguments="${msgArgs}" /></div>
			</c:if>
			<c:if test="${err != null}">
				<div id="openmrs_error"><spring:message code="${err}" text="${err}" arguments="${errArgs}"/></div>
			</c:if>
			<div id="openmrs_dwr_error" style="display:none" class="error">
				<div id="openmrs_dwr_error_msg"></div>
				<div id="openmrs_dwr_error_close" class="smallMessage">
					<i><spring:message code="error.dwr.stacktrace"/></i> 
					<a href="#" onclick="this.parentNode.parentNode.style.display='none'"><spring:message code="error.dwr.hide"/></a>
				</div>
			</div>
			


<%@ include file="template/localHeader.jsp"%>

<h2>
	<spring:message code="patientdatatransfer.clinicsOverview" />
</h2>

<div id="map" style="width: 600px; height: 450px"></div>
<noscript><b>JavaScript must be enabled in order for you to use Google Maps.</b> 
  However, it seems JavaScript is either disabled or not supported by your browser. 
  To view Google Maps, enable JavaScript by changing your browser options, and then 
  try again.
</noscript>

<%@ include file="/WEB-INF/template/footer.jsp"%>