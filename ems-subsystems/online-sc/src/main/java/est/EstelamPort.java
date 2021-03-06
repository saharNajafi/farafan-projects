
package est;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b14002
 * Generated source version: 2.2
 * 
 */
@WebService(name = "EstelamPort", targetNamespace = "http://est")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface EstelamPort {


    /**
     * 
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns est.ImageResult
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getImage", targetNamespace = "http://est", className = "est.GetImage")
    @ResponseWrapper(localName = "getImageResponse", targetNamespace = "http://est", className = "est.GetImageResponse")
    @Action(input = "http://est/EstelamPort/getImageRequest", output = "http://est/EstelamPort/getImageResponse")
    public ImageResult getImage(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        long arg2);

    /**
     * 
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns java.util.List<est.EstelamResult>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getEstelam", targetNamespace = "http://est", className = "est.GetEstelam")
    @ResponseWrapper(localName = "getEstelamResponse", targetNamespace = "http://est", className = "est.GetEstelamResponse")
    @Action(input = "http://est/EstelamPort/getEstelamRequest", output = "http://est/EstelamPort/getEstelamResponse")
    public List<EstelamResult> getEstelam(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        PersonInfo arg2);

    /**
     * 
     * @param arg4
     * @param arg3
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns java.util.List<est.EstelamResult>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getEstelam2", targetNamespace = "http://est", className = "est.GetEstelam2")
    @ResponseWrapper(localName = "getEstelam2Response", targetNamespace = "http://est", className = "est.GetEstelam2Response")
    @Action(input = "http://est/EstelamPort/getEstelam2Request", output = "http://est/EstelamPort/getEstelam2Response")
    public List<EstelamResult> getEstelam2(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        String arg2,
        @WebParam(name = "arg3", targetNamespace = "")
        String arg3,
        @WebParam(name = "arg4", targetNamespace = "")
        PersonInfo arg4);

    /**
     * 
     * @param arg5
     * @param arg4
     * @param arg3
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns java.util.List<est.EstelamResult6C>
     * @throws UnsupportedEncodingException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getEstelam8C", targetNamespace = "http://est", className = "est.GetEstelam8C")
    @ResponseWrapper(localName = "getEstelam8CResponse", targetNamespace = "http://est", className = "est.GetEstelam8CResponse")
    @Action(input = "http://est/EstelamPort/getEstelam8CRequest", output = "http://est/EstelamPort/getEstelam8CResponse", fault = {
        @FaultAction(className = UnsupportedEncodingException_Exception.class, value = "http://est/EstelamPort/getEstelam8C/Fault/UnsupportedEncodingException")
    })
    public List<EstelamResult6C> getEstelam8C(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        String arg2,
        @WebParam(name = "arg3", targetNamespace = "")
        String arg3,
        @WebParam(name = "arg4", targetNamespace = "")
        PersonInfo3F arg4,
        @WebParam(name = "arg5", targetNamespace = "")
        SenderInfo3F arg5)
        throws UnsupportedEncodingException_Exception
    ;

    /**
     * 
     * @param arg4
     * @param arg3
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns est.SenderResponseInfo
     * @throws Exception3F_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getEstelam8RI", targetNamespace = "http://est", className = "est.GetEstelam8RI")
    @ResponseWrapper(localName = "getEstelam8RIResponse", targetNamespace = "http://est", className = "est.GetEstelam8RIResponse")
    @Action(input = "http://est/EstelamPort/getEstelam8RIRequest", output = "http://est/EstelamPort/getEstelam8RIResponse", fault = {
        @FaultAction(className = Exception3F_Exception.class, value = "http://est/EstelamPort/getEstelam8RI/Fault/Exception3F")
    })
    public SenderResponseInfo getEstelam8RI(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        String arg2,
        @WebParam(name = "arg3", targetNamespace = "")
        String arg3,
        @WebParam(name = "arg4", targetNamespace = "")
        String arg4)
        throws Exception3F_Exception
    ;

    /**
     * 
     * @param arg4
     * @param arg3
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns est.EstelamResult7
     * @throws UnsupportedEncodingException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getEstelam7", targetNamespace = "http://est", className = "est.GetEstelam7")
    @ResponseWrapper(localName = "getEstelam7Response", targetNamespace = "http://est", className = "est.GetEstelam7Response")
    @Action(input = "http://est/EstelamPort/getEstelam7Request", output = "http://est/EstelamPort/getEstelam7Response", fault = {
        @FaultAction(className = UnsupportedEncodingException_Exception.class, value = "http://est/EstelamPort/getEstelam7/Fault/UnsupportedEncodingException")
    })
    public EstelamResult7 getEstelam7(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        String arg2,
        @WebParam(name = "arg3", targetNamespace = "")
        String arg3,
        @WebParam(name = "arg4", targetNamespace = "")
        PersonInfo3F arg4)
        throws UnsupportedEncodingException_Exception
    ;

    /**
     * 
     * @param arg4
     * @param arg3
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns java.util.List<est.EstelamResult7C>
     * @throws UnsupportedEncodingException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getEstelam7C", targetNamespace = "http://est", className = "est.GetEstelam7C")
    @ResponseWrapper(localName = "getEstelam7CResponse", targetNamespace = "http://est", className = "est.GetEstelam7CResponse")
    @Action(input = "http://est/EstelamPort/getEstelam7CRequest", output = "http://est/EstelamPort/getEstelam7CResponse", fault = {
        @FaultAction(className = UnsupportedEncodingException_Exception.class, value = "http://est/EstelamPort/getEstelam7C/Fault/UnsupportedEncodingException")
    })
    public List<EstelamResult7C> getEstelam7C(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        String arg2,
        @WebParam(name = "arg3", targetNamespace = "")
        String arg3,
        @WebParam(name = "arg4", targetNamespace = "")
        EstelamRequest arg4)
        throws UnsupportedEncodingException_Exception
    ;

    /**
     * 
     * @param arg4
     * @param arg3
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns est.EstelamResult9C
     * @throws UnsupportedEncodingException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getEstelam9C", targetNamespace = "http://est", className = "est.GetEstelam9C")
    @ResponseWrapper(localName = "getEstelam9CResponse", targetNamespace = "http://est", className = "est.GetEstelam9CResponse")
    @Action(input = "http://est/EstelamPort/getEstelam9CRequest", output = "http://est/EstelamPort/getEstelam9CResponse", fault = {
        @FaultAction(className = UnsupportedEncodingException_Exception.class, value = "http://est/EstelamPort/getEstelam9C/Fault/UnsupportedEncodingException")
    })
    public EstelamResult9C getEstelam9C(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        String arg2,
        @WebParam(name = "arg3", targetNamespace = "")
        String arg3,
        @WebParam(name = "arg4", targetNamespace = "")
        Estelam9CRequest arg4)
        throws UnsupportedEncodingException_Exception
    ;

    /**
     * 
     * @param arg5
     * @param arg4
     * @param arg3
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns est.EstelamResult10
     * @throws UnsupportedEncodingException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getEstelam10", targetNamespace = "http://est", className = "est.GetEstelam10")
    @ResponseWrapper(localName = "getEstelam10Response", targetNamespace = "http://est", className = "est.GetEstelam10Response")
    @Action(input = "http://est/EstelamPort/getEstelam10Request", output = "http://est/EstelamPort/getEstelam10Response", fault = {
        @FaultAction(className = UnsupportedEncodingException_Exception.class, value = "http://est/EstelamPort/getEstelam10/Fault/UnsupportedEncodingException")
    })
    public EstelamResult10 getEstelam10(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        String arg2,
        @WebParam(name = "arg3", targetNamespace = "")
        String arg3,
        @WebParam(name = "arg4", targetNamespace = "")
        String arg4,
        @WebParam(name = "arg5", targetNamespace = "")
        Estelam10PersonInfo arg5)
        throws UnsupportedEncodingException_Exception
    ;

    /**
     * 
     * @param arg4
     * @param arg3
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns est.EstelamResult11
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getEstelam11", targetNamespace = "http://est", className = "est.GetEstelam11")
    @ResponseWrapper(localName = "getEstelam11Response", targetNamespace = "http://est", className = "est.GetEstelam11Response")
    @Action(input = "http://est/EstelamPort/getEstelam11Request", output = "http://est/EstelamPort/getEstelam11Response")
    public EstelamResult11 getEstelam11(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        String arg2,
        @WebParam(name = "arg3", targetNamespace = "")
        String arg3,
        @WebParam(name = "arg4", targetNamespace = "")
        Estelam11Request arg4);

    /**
     * 
     * @param arg4
     * @param arg3
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns est.EstelamRIResponse
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getEstelamRI", targetNamespace = "http://est", className = "est.GetEstelamRI")
    @ResponseWrapper(localName = "getEstelamRIResponse", targetNamespace = "http://est", className = "est.GetEstelamRIResponse")
    @Action(input = "http://est/EstelamPort/getEstelamRIRequest", output = "http://est/EstelamPort/getEstelamRIResponse")
    public EstelamRIResponse getEstelamRI(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        String arg2,
        @WebParam(name = "arg3", targetNamespace = "")
        String arg3,
        @WebParam(name = "arg4", targetNamespace = "")
        String arg4);

    /**
     * 
     * @param arg4
     * @param arg3
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns java.util.List<est.EstelamResult>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getEstelam2SC", targetNamespace = "http://est", className = "est.GetEstelam2SC")
    @ResponseWrapper(localName = "getEstelam2SCResponse", targetNamespace = "http://est", className = "est.GetEstelam2SCResponse")
    @Action(input = "http://est/EstelamPort/getEstelam2SCRequest", output = "http://est/EstelamPort/getEstelam2SCResponse")
    public List<EstelamResult> getEstelam2SC(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        String arg2,
        @WebParam(name = "arg3", targetNamespace = "")
        String arg3,
        @WebParam(name = "arg4", targetNamespace = "")
        PersonInfo arg4);

    /**
     * 
     * @param arg4
     * @param arg3
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns est.ImageResult
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getImage2", targetNamespace = "http://est", className = "est.GetImage2")
    @ResponseWrapper(localName = "getImage2Response", targetNamespace = "http://est", className = "est.GetImage2Response")
    @Action(input = "http://est/EstelamPort/getImage2Request", output = "http://est/EstelamPort/getImage2Response")
    public ImageResult getImage2(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        String arg2,
        @WebParam(name = "arg3", targetNamespace = "")
        String arg3,
        @WebParam(name = "arg4", targetNamespace = "")
        long arg4);

    /**
     * 
     * @param arg5
     * @param arg4
     * @param arg3
     * @param arg2
     * @param arg1
     * @param arg0
     * @param arg6
     * @return
     *     returns est.ImageResult
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getImage3", targetNamespace = "http://est", className = "est.GetImage3")
    @ResponseWrapper(localName = "getImage3Response", targetNamespace = "http://est", className = "est.GetImage3Response")
    @Action(input = "http://est/EstelamPort/getImage3Request", output = "http://est/EstelamPort/getImage3Response")
    public ImageResult getImage3(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        String arg2,
        @WebParam(name = "arg3", targetNamespace = "")
        String arg3,
        @WebParam(name = "arg4", targetNamespace = "")
        long arg4,
        @WebParam(name = "arg5", targetNamespace = "")
        int arg5,
        @WebParam(name = "arg6", targetNamespace = "")
        int arg6);

    /**
     * 
     * @param arg5
     * @param arg4
     * @param arg3
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns est.ImageResult
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getImageSmart", targetNamespace = "http://est", className = "est.GetImageSmart")
    @ResponseWrapper(localName = "getImageSmartResponse", targetNamespace = "http://est", className = "est.GetImageSmartResponse")
    @Action(input = "http://est/EstelamPort/getImageSmartRequest", output = "http://est/EstelamPort/getImageSmartResponse")
    public ImageResult getImageSmart(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        String arg2,
        @WebParam(name = "arg3", targetNamespace = "")
        String arg3,
        @WebParam(name = "arg4", targetNamespace = "")
        long arg4,
        @WebParam(name = "arg5", targetNamespace = "")
        String arg5);

    /**
     * 
     * @param arg5
     * @param arg4
     * @param arg3
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns est.ImageResult
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getImage4", targetNamespace = "http://est", className = "est.GetImage4")
    @ResponseWrapper(localName = "getImage4Response", targetNamespace = "http://est", className = "est.GetImage4Response")
    @Action(input = "http://est/EstelamPort/getImage4Request", output = "http://est/EstelamPort/getImage4Response")
    public ImageResult getImage4(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        String arg2,
        @WebParam(name = "arg3", targetNamespace = "")
        String arg3,
        @WebParam(name = "arg4", targetNamespace = "")
        long arg4,
        @WebParam(name = "arg5", targetNamespace = "")
        long arg5);

    /**
     * 
     * @param arg4
     * @param arg3
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns java.util.List<est.EstelamResult3>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getEstelam3", targetNamespace = "http://est", className = "est.GetEstelam3")
    @ResponseWrapper(localName = "getEstelam3Response", targetNamespace = "http://est", className = "est.GetEstelam3Response")
    @Action(input = "http://est/EstelamPort/getEstelam3Request", output = "http://est/EstelamPort/getEstelam3Response")
    public List<EstelamResult3> getEstelam3(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        String arg2,
        @WebParam(name = "arg3", targetNamespace = "")
        String arg3,
        @WebParam(name = "arg4", targetNamespace = "")
        PersonInfo arg4);

    /**
     * 
     * @param arg4
     * @param arg3
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns java.util.List<est.EstelamResult4>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getEstelam4", targetNamespace = "http://est", className = "est.GetEstelam4")
    @ResponseWrapper(localName = "getEstelam4Response", targetNamespace = "http://est", className = "est.GetEstelam4Response")
    @Action(input = "http://est/EstelamPort/getEstelam4Request", output = "http://est/EstelamPort/getEstelam4Response")
    public List<EstelamResult4> getEstelam4(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        String arg2,
        @WebParam(name = "arg3", targetNamespace = "")
        String arg3,
        @WebParam(name = "arg4", targetNamespace = "")
        PersonInfo arg4);

    /**
     * 
     * @param arg4
     * @param arg3
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns est.EstelamResultC
     * @throws Exception3F_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getEstelam3SC", targetNamespace = "http://est", className = "est.GetEstelam3SC")
    @ResponseWrapper(localName = "getEstelam3SCResponse", targetNamespace = "http://est", className = "est.GetEstelam3SCResponse")
    @Action(input = "http://est/EstelamPort/getEstelam3SCRequest", output = "http://est/EstelamPort/getEstelam3SCResponse", fault = {
        @FaultAction(className = Exception3F_Exception.class, value = "http://est/EstelamPort/getEstelam3SC/Fault/Exception3F")
    })
    public EstelamResultC getEstelam3SC(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        String arg2,
        @WebParam(name = "arg3", targetNamespace = "")
        String arg3,
        @WebParam(name = "arg4", targetNamespace = "")
        PersonInfo arg4)
        throws Exception3F_Exception
    ;

    /**
     * 
     * @param arg5
     * @param arg4
     * @param arg3
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns est.EstelamResultC
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getEstelam3C", targetNamespace = "http://est", className = "est.GetEstelam3C")
    @ResponseWrapper(localName = "getEstelam3CResponse", targetNamespace = "http://est", className = "est.GetEstelam3CResponse")
    @Action(input = "http://est/EstelamPort/getEstelam3CRequest", output = "http://est/EstelamPort/getEstelam3CResponse")
    public EstelamResultC getEstelam3C(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        String arg2,
        @WebParam(name = "arg3", targetNamespace = "")
        String arg3,
        @WebParam(name = "arg4", targetNamespace = "")
        long arg4,
        @WebParam(name = "arg5", targetNamespace = "")
        String arg5);

    /**
     * 
     * @param arg5
     * @param arg4
     * @param arg3
     * @param arg2
     * @param arg1
     * @param arg0
     * @param arg6
     * @return
     *     returns est.EstelamResultC
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getImage5", targetNamespace = "http://est", className = "est.GetImage5")
    @ResponseWrapper(localName = "getImage5Response", targetNamespace = "http://est", className = "est.GetImage5Response")
    @Action(input = "http://est/EstelamPort/getImage5Request", output = "http://est/EstelamPort/getImage5Response")
    public EstelamResultC getImage5(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        String arg2,
        @WebParam(name = "arg3", targetNamespace = "")
        String arg3,
        @WebParam(name = "arg4", targetNamespace = "")
        long arg4,
        @WebParam(name = "arg5", targetNamespace = "")
        String arg5,
        @WebParam(name = "arg6", targetNamespace = "")
        int arg6);

    /**
     * 
     * @param arg4
     * @param arg3
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns est.EstelamResult5
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getEstelam5", targetNamespace = "http://est", className = "est.GetEstelam5")
    @ResponseWrapper(localName = "getEstelam5Response", targetNamespace = "http://est", className = "est.GetEstelam5Response")
    @Action(input = "http://est/EstelamPort/getEstelam5Request", output = "http://est/EstelamPort/getEstelam5Response")
    public EstelamResult5 getEstelam5(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        String arg2,
        @WebParam(name = "arg3", targetNamespace = "")
        String arg3,
        @WebParam(name = "arg4", targetNamespace = "")
        long arg4);

    /**
     * 
     * @param arg4
     * @param arg3
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns java.util.List<est.EstelamResult6C>
     * @throws UnsupportedEncodingException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getEstelam6C", targetNamespace = "http://est", className = "est.GetEstelam6C")
    @ResponseWrapper(localName = "getEstelam6CResponse", targetNamespace = "http://est", className = "est.GetEstelam6CResponse")
    @Action(input = "http://est/EstelamPort/getEstelam6CRequest", output = "http://est/EstelamPort/getEstelam6CResponse", fault = {
        @FaultAction(className = UnsupportedEncodingException_Exception.class, value = "http://est/EstelamPort/getEstelam6C/Fault/UnsupportedEncodingException")
    })
    public List<EstelamResult6C> getEstelam6C(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        String arg2,
        @WebParam(name = "arg3", targetNamespace = "")
        String arg3,
        @WebParam(name = "arg4", targetNamespace = "")
        PersonInfo3F arg4)
        throws UnsupportedEncodingException_Exception
    ;

    /**
     * 
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getVersion", targetNamespace = "http://est", className = "est.GetVersion")
    @ResponseWrapper(localName = "getVersionResponse", targetNamespace = "http://est", className = "est.GetVersionResponse")
    @Action(input = "http://est/EstelamPort/getVersionRequest", output = "http://est/EstelamPort/getVersionResponse")
    public String getVersion();

}
