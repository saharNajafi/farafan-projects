<%@ page import="com.gam.commons.core.BaseLog" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.util.jar.Attributes" %>
<%@ page import="java.util.jar.Manifest" %>
<%--
  Created by IntelliJ IDEA.
  User: saadat
  Date: 5/23/12
  Time: 8:02 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<title>سامانه پشتيبانی خدمات</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="app/locale/GlobalVars.js"></script>
<script type="text/javascript" src="app/locale/Util.js"></script>
<script type="text/javascript">

    var EMS_VERSION =
            '<%
                    org.slf4j.Logger logger = BaseLog.getLogger("index.jsp");
                    try{
                        ServletContext servletContext = getServletConfig().getServletContext();
                        InputStream manifestStream = servletContext.getResourceAsStream("/META-INF/MANIFEST.MF");
                        Manifest manifest = new Manifest(manifestStream);
                        Attributes attributes = manifest.getMainAttributes();
                        String impVersion = attributes.getValue("Specification-Version");
                        if (impVersion != null && impVersion.trim().length() > 0)
                            out.print(impVersion);
                        else
                            out.print("1.0.0");
                    } catch(Throwable t){
                        logger.error(t.getMessage(), t);
                        // exception
                        out.print("1.0.0");
                    }
            %>';
            
            //test(EMS_VERSION);
            //cardRequestTimeOut = 120000;
</script>
<style type="text/css">


/*        .x-panel-body-default {
           background: none repeat scroll 0 0 white;
            border-color: #99BCE8;
            border-style: solid;
            border-width: 0px;    !important;
            border:0px;            !important;
            color: black;
        }*/

/*#officeGrid-actionColumn{*/
#officegrid-actionColumn {
    width: 200px;
}

.ltrStyle {
    direction: ltr;
!important;
    text-align: left;
!important;
}

.wrap .x-grid-cell-inner {
    white-space: normal;
!important;
}

.x-grid-centerColumn-hidden {
    display: none;
}

.x-grid-centerColumn-icon {
    cursor: pointer;
    margin-right: 45%;
    margin-left: 50%;

}

.x-grid-center-icon {
    cursor: pointer;

}

.textfieldset {
    margin-left: 25px;
    width: 350px;
}

.line-through {
    text-decoration: line-through;
}

/* style rows on mouseover */
/*		.x-grid-row-over .x-grid-cell-inner {
            font-weight: bold;

        }*/

.cTextAlign {
    text-align: right;
    background: red;
}

.girdAction-userActive-icon {
    background: url('resources/themes/images/user/Active.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
    margin-right: 45%;
    margin-left: 50%;

}

.girdAction-userInActive-icon {
    background: url('resources/themes/images/user/InActive.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
    margin-right: 45%;
    margin-left: 50%;

}

.girdAction-cacheRefresh-icon {
    background: url('resources/themes/images/default/shared/cacheRefresh.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.girdAction-verified-icon {
    background: url('resources/themes/images/default/shared/verified.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.girdAction-verify-icon {
    background: url('resources/themes/images/default/shared/verify.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.girdAction-noVerified-icon {
    background: url('resources/themes/images/default/shared/notVerified.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.girdAction-exportExcel-icon {
    background: url('/wr/ext/add-ons/images/excel.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.girdAction-damaged-icon {
    background: url('resources/themes/images/TokenIcon/damaged.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.girdAction-duplicate-icon {
    background: url('resources/themes/images/TokenIcon/duplicate.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.girdAction-deleteToken-icon {
    background: url('resources/themes/images/TokenIcon/deleteToken.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.girdAction-tahvil-icon {
    background: url('resources/themes/images/TokenIcon/tahvil.gif') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.girdAction-issueSSLToken-icon {
    background: url('resources/themes/images/TokenIcon/new.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.girdAction-UserList-icon {
    background: url('resources/themes/images/user/UserList.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.girdAction-OfficeCapacity-edit-icon {
    background: url('resources/themes/images/default/shared/edit.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.girdAction-LivePhotoActive-icon{
    background: url('resources/themes/images/officeSetting/camera01.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}
.girdAction-LivePhotoDeactive-icon{
    background: url('resources/themes/images/officeSetting/camera02.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}
.girdAction-FourfourtwoActive-icon{
    background: url('resources/themes/images/officeSetting/fingerprint01.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}
.girdAction-FourfourtwoDeactive-icon{
    background: url('resources/themes/images/officeSetting/fingerprint02.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}
.girdAction-DisablityModeActive-icon{
    background: url('resources/themes/images/officeSetting/picture01.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}
.girdAction-DisablityModeDeactive-icon{
    background: url('resources/themes/images/officeSetting/picture02.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}
.girdAction-ReIssueRequestActive-icon{
    background: url('resources/themes/images/officeSetting/reissuerequest01.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}
.girdAction-OfficeCapacity-icon{
    background: url('resources/themes/images/default/mainMenu/OfficeSummary.gif') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}
.girdAction-OfficeSetting-icon{
    background: url('resources/themes/images/officeSetting/officesetting.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}
.girdAction-ReIssueRequestDeactive-icon{
    background: url('resources/themes/images/officeSetting/reissuerequest02.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}
 .girdAction-AmputationAnnouncmentActive-icon { 
     background: url('resources/themes/images/officeSetting/amputationAnnouncment01.png') no-repeat right; 
     width: 13px; 
     height: 16px; 
     cursor: pointer; 
 } 
 .girdAction-AmputationAnnouncmentDeactive-icon { 
     background: url('resources/themes/images/officeSetting/amputationAnnouncment02.png') no-repeat right; 
     width: 13px; 
     height: 16px; 
     cursor: pointer; 
}
 .girdAction-ElderyModeActive-icon { 
     background: url('resources/themes/images/officeSetting/ElderyMode01.png') no-repeat right; 
     width: 13px;
     height: 16px; 
     cursor: pointer; 
 } 
 .girdAction-ElderyModeDeactive-icon { 
     background: url('resources/themes/images/officeSetting/ElderyMode02.png') no-repeat right; 
     width: 13px; 
     height: 16px; 
     cursor: pointer; 
}
 .girdAction-UseScannerUIActive-icon { 
     background: url('resources/themes/images/officeSetting/scannerUI01.png') no-repeat right; 
     width: 16px;
     height: 16px; 
     cursor: pointer; 
 } 
 .girdAction-UseScannerUIDeactive-icon { 
     background: url('resources/themes/images/officeSetting/scannerUI02.png') no-repeat right; 
     width: 16px; 
     height: 16px; 
     cursor: pointer; 
}
 .girdAction-AllowEditBackgroundActive-icon { 
     background: url('resources/themes/images/officeSetting/editBackground01.png') no-repeat right; 
     width: 16px;
     height: 16px; 
     cursor: pointer; 
 } 
 .girdAction-AllowEditBackgroundDeactive-icon { 
     background: url('resources/themes/images/officeSetting/editBackground02.png') no-repeat right; 
     width: 16px; 
     height: 16px; 
     cursor: pointer; 
}
 .girdAction-ReplacePinActive-icon { 
     background: url('resources/themes/images/officeSetting/ReplacePin01.png') no-repeat right; 
     width: 16px;
     height: 16px; 
     cursor: pointer; 
 } 
 .girdAction-ReplacePinDeactive-icon { 
     background: url('resources/themes/images/officeSetting/ReplacePin02.png') no-repeat right; 
     width: 16px; 
     height: 16px; 
     cursor: pointer; 
}
.girdAction-itemReceived-icon {
    background: url('resources/themes/images/dispath/itemReceived.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.girdAction-itemNotReceived-icon {
    background: url('resources/themes/images/dispath/itemNotReceived.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.girdAction-backToInitialState-icon {
    background: url('resources/themes/images/dispath/backToInitialState.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.girdAction-itemLost-icon {
    background: url('resources/themes/images/dispath/itemLost.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.girdAction-itemFound-icon {
    background: url('resources/themes/images/dispath/itemFound.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.girdAction-undoSend-icon {
    background: url('resources/themes/images/dispath/undoSend.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.girdAction-itemSend-icon {
    background: url('resources/themes/images/dispath/itemSend.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.girdAction-setBoxDetail-icon {
    background: url('resources/themes/images/dispath/setBoxDetail.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.girdAction-pause-icon {
    background: url('resources/themes/images/default/shared/pause.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.girdAction-resume-icon {
    background: url('resources/themes/images/default/shared/resume.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.girdAction-playRTL-icon {
    background: url('resources/themes/images/default/shared/play-rtl.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.girdAction-interrupt-icon {
    background: url('resources/themes/images/default/shared/forbidden.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.girdAction-forbidden-icon {
    background: url('resources/themes/images/default/shared/forbidden.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.Msg-Warning-icon {
    background: url('resources/themes/images/windows/dialogWarning.png') no-repeat right;
    width: 25px;
    height: 25px;

}

.grid-boxInto-action-icon {
    background: url('resources/themes/images/dispath/box_into.png') no-repeat right;
    width: 22px;
    height: 18px;
    cursor: pointer;

}

.grid-boxOut-action-icon {
    background: url('resources/themes/images/dispath/box_out.png') no-repeat right;
    width: 22px;
    height: 18px;
    cursor: pointer;
}

.grid-boxFind-action-icon {
    background: url('resources/themes/images/dispath/box_find.png') no-repeat right;
    width: 22px;
    height: 18px;
    cursor: pointer;
}

.grid-boxLose-action-icon {
    background: url('resources/themes/images/dispath/box_lose.png') no-repeat right;
    width: 22px;
    height: 18px;
    cursor: pointer;
}

.grid-deleteToken-action-icon {
    background: url(resources/themes/images/TokenIcon/deleteToken.png) no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.grid-tahvil-action-icon {
    background: url(resources/themes/images/TokenIcon/tahvil.gif) no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.grid-tamdid-action-icon {
    background: url(resources/themes/images/TokenIcon/tamdid.png) no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.grid-ActiveUser-action-icon {
    background: url(resources/themes/images/user/Active.png) no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
    margin-right: 25px;
}

.grid-InActiveUser-action-icon {
    background: url(resources/themes/images/user/InActive.png) no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
    margin-right: 25px;
}

.grid-NewToken-action-icon {
    background: url(resources/themes/images/TokenIcon/new.png) no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.grid-Checked-action-icon {
    background: url(resources/themes/images/default/shared/check.png) no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.grid-UnChecked-action-icon {
    background: url(resources/themes/images/default/shared/forbidden.png) no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.grid-Delete-action-icon {
    background: url(resources/themes/images/default/shared/forbidden.png) no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.windows-Cancel-icon {
    background: url(resources/themes/images/windows/Cancel.png) no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.windows-Save-icon {
    background: url(resources/themes/images/windows/save.png) no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}
.windows-Edit-icon {
    background: url(resources/themes/images/windows/edit.png) no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}
.grid-repeal-request-action-icon {
    background: url(resources/themes/images/default/shared/forbidden.png) no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.grid-reject-repeal-request-action-icon {
    background: url(resources/themes/images/default/shared/delete.png) no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.grid-undo-repeal-request-action-icon {
    background: url('resources/themes/images/dispath/undoSend.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.grid-undo-person-token-action-icon {
    background: url('resources/themes/images/default/shared/delete.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.grid-activate-report-icon {
    background: url('resources/themes/images/reports/activate-report.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}
.girdAction-infoabout-icon {
    background: url('resources/themes/images/default/tbar/iconInfo.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}
.grid-deactivate-report-icon {
    background: url('resources/themes/images/reports/deactivate-report.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.grid-run-report-icon {
    background: url('resources/themes/images/reports/run.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

/**
 * Stylesheet class for action items that should be hidden on UI
 */
.grid-action-hidden {
    display: none;
}

.grid-undo-network-token-action-icon {
    background: url('resources/themes/images/default/shared/delete.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}
.girdAction-losttaeed-icon {
    background: url('resources/themes/images/TokenIcon/taeedlost.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}
.grid-change-priority {
    background: url('resources/themes/images/default/toolbar/setting.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.grid-print-registration-receipt {
    background: url('resources/themes/images/default/toolbar/print.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}
.report-exportExcel-icon {
    background: url('/wr/ext/add-ons/images/excel.png') no-repeat left;
    width: 16px;
    height: 16px;
}

.report-exportExcel-disable-icon {
    background: url('resources/themes/images/default/shared/excel_disabled.png') no-repeat left;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.report-exportPDF-icon {
    background: url('resources/themes/images/default/shared/pdf.png') no-repeat left;
    width: 16px;
    height: 16px;
}

.report-exportPDF-disable-icon {
    background: url('resources/themes/images/default/shared/pdf_disabled.png') no-repeat left;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.grid-delete-icon {
    background: url('resources/themes/images/default/shared/delete.png') no-repeat left;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.report-download-icon {
    background: url('resources/themes/images/default/shared/download.png') no-repeat left;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.ems-display-field {
    font-weight: bold;
    color: black;
}

.instructions-read-only .x-form-item-body textarea
{
border-top-style : hidden !important;
border-bottom-style : hidden !important;
border-left-style: hidden !important;
border-right-style: hidden !important;
background-image: none !important;
}

.girdAction-tahvil-ict-icon {
    background: url('resources/themes/images/TokenIcon/token-tahvil.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}
.girdAction-reject-ict-icon {
    background: url('resources/themes/images/TokenIcon/token-delete.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}
.girdAction-taeid-ict-icon {
    background: url('resources/themes/images/TokenIcon/token-taeid.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}
.girdAction-taeid-ict-icon {
    background: url('resources/themes/images/TokenIcon/token-taeid.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}
.girdAction-faalsazi-ict-icon {
    background: url('resources/themes/images/TokenIcon/token-faalsazi.png') no-repeat right;
    width: 16px;
    height: 16px;
    cursor: pointer;
}

.girdAction-AllowAmputatedFingerActive-icon {
    background: url('resources/themes/images/officeSetting/AllowAmputatedFingerStatusForElderly.png') no-repeat right;
    width: 13px;
    height: 16px;
    cursor: pointer;
}
.girdAction-AllowAmputatedFingerInactive-icon {
    background: url('resources/themes/images/officeSetting/AllowAmputatedFingerStatusForElderly_inactive.png') no-repeat right;
    width: 13px;
    height: 16px;
    cursor: pointer;
}
    .girdAction-AllowChangeFingerActive-icon {
        background: url('resources/themes/images/officeSetting/AllowChangeFingerStatusDuringCaptureForElderly.png') no-repeat right;
        width: 13px;
        height: 16px;
        cursor: pointer;
    }
.girdAction-AllowChangeFingerInactive-icon {
    background: url('resources/themes/images/officeSetting/AllowChangeFingerStatusDuringCaptureForElderly_inactive.png') no-repeat right;
    width: 13px;
    height: 16px;
    cursor: pointer;
}
</style>

<link href="/wr/ext/4.1-beta-1/rtl/resources/css/ext-all.css" rel="stylesheet" type="text/css"/>
<link href="/wr/gam/4.x/rtl/resources/css/gam-all.css" rel="stylesheet" type="text/css"/>
<link href="resources/css/ems-all.css" rel="stylesheet" type="text/css"/>

<script src="/wr/ext/4.1-beta-1/rtl/ext-all-debug.js" type="text/javascript"></script>
<script src="/wr/gam/4.x/rtl/locale/Persian.js" type="text/javascript"></script>
<!-- <script src="/wr/gam/4.x/rtl/gam-all-debug.js" type="text/javascript"></script> -->
<!-- <script src="/wr/gam/4.x/rtl/ict-all-debug.js" type="text/javascript"></script> -->

<script src="app/locale/ict-all-debug.js" type="text/javascript"></script>

<script src="app/locale/Persian.js" type="text/javascript"></script>
<script src="app/locale/ErrCodeFa.js" type="text/javascript"></script>
<script src="app/locale/Tools.js" type="text/javascript"></script>
<script src="app/locale/Validation.js" type="text/javascript"></script>
<script src="app/locale/ObjectName.js" type="text/javascript"></script>
<script src="app/app.js" type="text/javascript"></script>
<%--<script type="text/javascript ">alert('okyy');</script>--%>

</head>
<body>

</body>
</html>