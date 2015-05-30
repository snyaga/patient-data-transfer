<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />

    <!-- Stylesheet -->

    <openmrs: htmlInclude file="/moduleResources/patientdatatransfer/resources/assets/style.css"/>

    <!-- Scripts -->

    <openmrs: htmlInclude file="/moduleResources/patientdatatransfer/resources/assets/jquery.js"/>

    <openmrs: htmlInclude file="/moduleResources/patientdatatransfer/resources/assets/identifier.js"/>

  <!--  <link rel="stylesheet" type="text/css" href="style.css" />
    <script language="javascript" type="text/javascript"
            src="assets/jquery.js"></script>
    <script type="text/javascript" src="assets/identifier.js">
    </script>
   -->
</head>

<body>
<div id="wrapper">
    <div id="header"></div>

        <br>
        <br>
    </div>

    <div id="bodycolumn">
        <blockquote>
            <h1>Create Data Import Request</h1>
        </blockquote>

        <p>Hello ${user.systemId}! Please Enter the required basic Information</p>
        <br>
        <br>
        <form>
            <div id="in">
                <p><label>Gender</label> <select id="gender" >
                    <option value=m>Male</option>
                    <option value=f>Female</option>
                </select></p>
                <p><label>County</label> <select id="county">
                    <option value=nrb>Nairobi</option>
                    <option value=kbu>Kiambu</option>
                    <option value=mga>Murang'a</option>
                </select></p>
                <p><label>Date of Birth</label> <input type="date" id="dob"</p>

                <p><label>Select Unique ID</label><select name="patientuid" id="patientuid" onchange="CheckPatient(this.value);">
                    <option value="" disabled selected>Select an ID type</option>
                    <option value="ID">National ID</option>
                    <option value="clinicNo">Patient Clinic Number</option>
                </select><br />
                <div id="ID" style="display: none;"><strong>Enter National ID: </strong><input type="text" name="ID" /><br /></div>
                <div id="clinicNo" style="display: none;"><strong>Enter Patient Clinic Number: <strong><input type="text" name="clinicNo" /><br /></div>
                </p>

                <p><input type="button" id="btn" value="Import Data" size="15" /><br />
                </p>
            </div>

        </form>
        <div class="lb"></div>
        <div id="result"></div>



</div>
</div>
</body>
</html>

<%@ include file="/WEB-INF/template/footer.jsp"%>