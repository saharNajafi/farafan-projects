<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>ReportRequestTest</title>
</head>
<body bgcolor="#A3A3FF">
<form action="ReportRequestServlet" method="post">
    <table>
        <tr>
            <td>ReportName:</td>
            <td><input type="text" name="name" checked="checked"></td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>MasterReportPath:</td>
            <td><input type="text" name="path" size="70" checked="checked"></td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>SubReportPath1:</td>
            <td><input type="text" name="subReportPath1" size="70" checked="checked"></td>
            <td>Caption:</td>
            <td><input type="text" name="subReportCaption1" checked="checked"></td>
        </tr>
        <tr>
            <td>SubReportPath2:</td>
            <td><input type="text" name="subReportPath2" size="70" checked="checked"></td>
            <td>Caption:</td>
            <td><input type="text" name="subReportCaption2" checked="checked"></td>
        </tr>
        <tr>
            <td>OutputType:</td>
            <td>
                <select id="output" name="output">
                    <option value="PDF">PDF</option>
                    <option value="XLS">Excel</option>
                </select>
            </td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>Parameters:</td>
            <td><input type="text" name="params"></td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td><input type="submit"><input type="reset"></td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
    </table>
</form>

</body>
</html>
