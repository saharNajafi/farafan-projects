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
            loadRequestIds();
            loadBatchIds();
        }

        function loadRequestIds() {
            var xmlhttp = getXmlHttp();
            xmlhttp.onreadystatechange = function () {
                if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                    for (var i = 0; i < 10; i++) {
                        document.getElementById("reqId" + i).innerHTML = xmlhttp.responseText;
                    }
                }
            }
            xmlhttp.open("POST", "CMSServlet?t=" + Math.random(), true);
            xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
            xmlhttp.send("method=loadRequestIds");
        }

        function loadBatchIds() {
            var xmlhttp = getXmlHttp();
            xmlhttp.onreadystatechange = function () {
                if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                    for (var i = 0; i < 10; i++) {
                        document.getElementById("batId" + i).innerHTML = xmlhttp.responseText;
                    }
                }
            }
            xmlhttp.open("POST", "CMSServlet?t=" + Math.random(), true);
            xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
            xmlhttp.send("method=loadBatchIds");
        }

        function submitBatch() {
            // Build string to post
            var post = "&batId=" + document.getElementById("batId").value;
            var tmpReq, tmpCrd;
            for (var i = 0; i < 10; i++) {
                tmpReq = document.getElementById("reqId" + i).value;
                tmpCrd = document.getElementById("cardInfo" + i).value;
                if (!tmpReq || !tmpCrd || (tmpReq.trim() == "") || (tmpCrd.trim() == "")) {
                    break;
                }
                post += "&reqId" + i + "=" + tmpReq + "&cardInfo" + i + "=" + tmpCrd;
            }

            var xmlhttp = getXmlHttp();
            xmlhttp.onreadystatechange = function () {
                if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                    document.getElementById("result").value = xmlhttp.responseText;
                }
            }
            xmlhttp.open("POST", "CMSServlet?t=" + Math.random(), true);
            xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
            xmlhttp.send("method=submitBatch" + post);

            loadBatchIds();
        }

        function submitBox() {
            // Build string to post
            var post = "&boxId=" + document.getElementById("boxId").value;
            var tmpBat;
            for (var i = 0; i < 10; i++) {
                tmpBat = document.getElementById("batId" + i).value;
                if (!tmpBat || (tmpBat.trim() == "")) {
                    break;
                }
                post += "&batId" + i + "=" + tmpBat;
            }

            var xmlhttp = getXmlHttp();
            xmlhttp.onreadystatechange = function () {
                if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                    document.getElementById("result").value = xmlhttp.responseText;
                }
            }
            xmlhttp.open("POST", "CMSServlet?t=" + Math.random(), true);
            xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
            xmlhttp.send("method=submitBox" + post);
        }
    </script>
</head>
<body onload="loadData()">
<form>
    <fieldset>
        <legend>New Batch:</legend>
        Batch ID: <input id="batId" name="batId" type="text"/><br/>
        <small style="color: red">Format: crn,issuanceDate,shipmentDate,keysetversion (yyyy/mm/dd)</small>
        <br/>
        Card Info 0: <select id="reqId0" name="reqId0"></select> <input id="cardInfo0" name="cardInfo0" type="text"/>
        <br/>
        Card Info 1: <select id="reqId1" name="reqId1"></select> <input id="cardInfo1" name="cardInfo1" type="text"/>
        <br/>
        Card Info 2: <select id="reqId2" name="reqId2"></select> <input id="cardInfo2" name="cardInfo2" type="text"/>
        <br/>
        Card Info 3: <select id="reqId3" name="reqId3"></select> <input id="cardInfo3" name="cardInfo3" type="text"/>
        <br/>
        Card Info 4: <select id="reqId4" name="reqId4"></select> <input id="cardInfo4" name="cardInfo4" type="text"/>
        <br/>
        Card Info 5: <select id="reqId5" name="reqId5"></select> <input id="cardInfo5" name="cardInfo5" type="text"/>
        <br/>
        Card Info 6: <select id="reqId6" name="reqId6"></select> <input id="cardInfo6" name="cardInfo6" type="text"/>
        <br/>
        Card Info 7: <select id="reqId7" name="reqId7"></select> <input id="cardInfo7" name="cardInfo7" type="text"/>
        <br/>
        Card Info 8: <select id="reqId8" name="reqId8"></select> <input id="cardInfo8" name="cardInfo8" type="text"/>
        <br/>
        Card Info 9: <select id="reqId9" name="reqId9"></select> <input id="cardInfo9" name="cardInfo9" type="text"/>
        <br/>
        <button type="button" onclick="submitBatch()">ارسال</button>
        <br/><br/>
    </fieldset>

    <fieldset>
        <legend>New Box:</legend>
        Box ID: <input id="boxId" name="boxId" type="text"/><br/>
        Batch 0: <select id="batId0" name="batId0"></select> <br/>
        Batch 1: <select id="batId1" name="batId1"></select> <br/>
        Batch 2: <select id="batId2" name="batId2"></select> <br/>
        Batch 3: <select id="batId3" name="batId3"></select> <br/>
        Batch 4: <select id="batId4" name="batId4"></select> <br/>
        Batch 5: <select id="batId5" name="batId5"></select> <br/>
        Batch 6: <select id="batId6" name="batId6"></select> <br/>
        Batch 7: <select id="batId7" name="batId7"></select> <br/>
        Batch 8: <select id="batId8" name="batId8"></select> <br/>
        Batch 9: <select id="batId9" name="batId9"></select> <br/>
        <button type="button" onclick="submitBox()">ارسال</button>
        <br/><br/>
    </fieldset>

    <textarea id="result" rows="15" cols="100"></textarea>
</form>
</body>
</html>