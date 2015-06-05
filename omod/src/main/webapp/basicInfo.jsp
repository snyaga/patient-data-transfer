<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>


<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />

    <!-- Stylesheet

    <%--<openmrs:htmlInclude file="/module/patientdatatransfer/resources/assets/style.css"/>--%>

    <!-- Scripts -->

    <%--<openmrs:htmlInclude file="/module/patientdatatransfer/resources/assets/jquery.js"/>--%>


    <%--<openmrs:htmlInclude file="/module/patientdatatransfer/resources/assets/identifier.js"/>--%>

    <!-- for some reason script not picked up. added it below. will fix later.
       -->

  <!--  <link rel="stylesheet" type="text/css" href="style.css" />
   -->

    <script language="javascript" type="text/javascript">
        function CheckPatient(val){
            var element=document.getElementById('ID');
            if(val=='ID')
                element.style.display='block';
            else
                element.style.display='none';

            var element=document.getElementById('clinicNo');
            if(val=='clinicNo')
                element.style.display='block';
            else
                element.style.display='none';
        }


    </script>

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
        <hr>
        <p>Hello ${user.systemId}! Please Enter the required basic Information</p>
        <br>
        <br>
        <form method="POST" action="<c:url value="/module/patientdatatransfer/importInfo" />">
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
                <P><label>Date of Birth</label> <input type="date" id="dob"/></p>

                <p><label>Select Unique ID </label><select name="patientuid" id="patientuid" onchange="CheckPatient(this.value);">
                    <option value="" disabled selected>Select an ID type</option>
                    <option value="ID">National ID</option>
                    <option value="clinicNo">Patient Clinic Number</option>
                </select><br />
                <div id="ID" style="display: none;"><strong>Enter National ID: </strong><input type="text" name="ID" /><br /></div>
                <div id="clinicNo" style="display: none;"><strong>Enter Patient Clinic Number: <strong><input type="text" name="clinicNo" /><br /></div>
                </p>
                <p><label>MFL Code: </label><select disabled id="mfl">
                    <option value="" disabled selected>Select previous clinic</option>
                    <option value=clinic1>Clinic code</option>
                    <option value=clinic2>Clinic code</option>
                    <option value=clinic3>Clinic code</option>
                    <option value=clinic4>Clinic code</option>
                </select>

                </p>

                <p><input type="submit" id="btn" value="Import Data" size="15" /><br />
                </p>
            </div>

        </form>
        <div class="lb"></div>
        <div id="result"></div>



</div>

</body>
</html>

<%@ include file="/WEB-INF/template/footer.jsp"%>