<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSP Page</title>
    <script type="text/javascript">
        function getXmlHttp() {
            var xmlhttp;
            if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
                xmlhttp = new XMLHttpRequest();
            } else {// code for IE6, IE5
                xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
            }
            return xmlhttp;
        }

        function loadData() {
            loadJobs();
//            setInterval("loadJobs()", 1000);
        }

        function loadJobs() {
            var xmlhttp = getXmlHttp();
            xmlhttp.onreadystatechange = function () {
                if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                    for (var i = 0; i < 10; i++) {
                        document.getElementById("jobsTable").innerHTML =
                                "<tr> " +
                                        "<th>Job Name</th> " +
                                        "<th>Job Description</th> " +
                                        "<th>Currently Executing</th> " +
                                        "<th>Pause</th> " +
                                        "<th>Resume</th> " +
                                        "<th>Run</th> " +
                                        "</tr> " + xmlhttp.responseText;
                    }
                }
            }
            xmlhttp.open("POST", "JobServlet?t=" + Math.random(), true);
            xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
            xmlhttp.send("method=loadJobs");
        }

        function pauseJob(keyName) {
            var xmlhttp = getXmlHttp();
            xmlhttp.onreadystatechange = function () {
                if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                    document.getElementById("result").value = xmlhttp.responseText + "\n" + document.getElementById("result").value;
                }
            }
            xmlhttp.open("POST", "JobServlet?t=" + Math.random(), true);
            xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
            xmlhttp.send("method=pauseJob&keyName=" + keyName);
            loadJobs();
        }

        function runJob(keyName) {
            var xmlhttp = getXmlHttp();
            xmlhttp.onreadystatechange = function () {
                if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                    document.getElementById("result").value = xmlhttp.responseText + "\n" + document.getElementById("result").value;
                }
            }
            xmlhttp.open("POST", "JobServlet?t=" + Math.random(), true);
            xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
            xmlhttp.send("method=runJob&keyName=" + keyName);
            loadJobs();
        }

        function resumeJob(keyName) {
            var xmlhttp = getXmlHttp();
            xmlhttp.onreadystatechange = function () {
                if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                    document.getElementById("result").value = xmlhttp.responseText + "\n" + document.getElementById("result").value;
                }
            }
            xmlhttp.open("POST", "JobServlet?t=" + Math.random(), true);
            xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
            xmlhttp.send("method=resumeJob&keyName=" + keyName);
            loadJobs();
        }
    </script>
</head>
<body onload="loadData()">
<form>
    <table id="jobsTable" border="1"></table>

    <textarea id="result" rows="15" cols="100"></textarea>
</form>
</body>
</html>