
package com.gam.nocr.ems.biz.service.external.client.emks.lds;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.gam.nocr.ems.biz.service.external.client.emks.lds package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetNIDCardKeysAndPINsResponseGetNIDCardKeysAndPINsResult_QNAME = new QName("http://tempuri.org/", "GetNIDCardKeysAndPINsResult");
    private final static QName _AnyURI_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "anyURI");
    private final static QName _Char_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "char");
    private final static QName _UnsignedByte_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedByte");
    private final static QName _DateTime_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "dateTime");
    private final static QName _AnyType_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "anyType");
    private final static QName _UnsignedInt_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedInt");
    private final static QName _Int_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "int");
    private final static QName _QName_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "QName");
    private final static QName _UnsignedShort_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedShort");
    private final static QName _Float_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "float");
    private final static QName _Decimal_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "decimal");
    private final static QName _Long_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "long");
    private final static QName _Double_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "double");
    private final static QName _Short_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "short");
    private final static QName _Guid_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "guid");
    private final static QName _Base64Binary_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "base64Binary");
    private final static QName _Duration_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "duration");
    private final static QName _Byte_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "byte");
    private final static QName _Exception_QNAME = new QName("http://schemas.datacontract.org/2004/07/System", "Exception");
    private final static QName _CardKeysAndPINs_QNAME = new QName("http://schemas.datacontract.org/2004/07/EMKS_WCFService", "CardKeysAndPINs");
    private final static QName _String_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "string");
    private final static QName _EMKSException_QNAME = new QName("http://schemas.datacontract.org/2004/07/EMKS_WCFService", "EMKSException");
    private final static QName _UnsignedLong_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedLong");
    private final static QName _CardMoCKeys_QNAME = new QName("http://schemas.datacontract.org/2004/07/EMKS_WCFService", "CardMoCKeys");
    private final static QName _Boolean_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "boolean");
    private final static QName _EMKSExceptionErrorCode_QNAME = new QName("http://schemas.datacontract.org/2004/07/EMKS_WCFService", "ErrorCode");
    private final static QName _EMKSExceptionInner_QNAME = new QName("http://schemas.datacontract.org/2004/07/EMKS_WCFService", "Inner");
    private final static QName _CardKeysAndPINsSM_QNAME = new QName("http://schemas.datacontract.org/2004/07/EMKS_WCFService", "SM");
    private final static QName _CardKeysAndPINsID_QNAME = new QName("http://schemas.datacontract.org/2004/07/EMKS_WCFService", "ID");
    private final static QName _CardKeysAndPINsASDMAC_QNAME = new QName("http://schemas.datacontract.org/2004/07/EMKS_WCFService", "ASD_MAC");
    private final static QName _CardKeysAndPINsASDENC_QNAME = new QName("http://schemas.datacontract.org/2004/07/EMKS_WCFService", "ASD_ENC");
    private final static QName _CardKeysAndPINsSign_QNAME = new QName("http://schemas.datacontract.org/2004/07/EMKS_WCFService", "Sign");
    private final static QName _CardKeysAndPINsNMoC_QNAME = new QName("http://schemas.datacontract.org/2004/07/EMKS_WCFService", "NMoC");
    private final static QName _CardKeysAndPINsASD_QNAME = new QName("http://schemas.datacontract.org/2004/07/EMKS_WCFService", "ASD");
    private final static QName _GetNIDCardMoCKeysCardInfo_QNAME = new QName("http://tempuri.org/", "CardInfo");
    private final static QName _CardMoCKeysMoCENC_QNAME = new QName("http://schemas.datacontract.org/2004/07/EMKS_WCFService", "MoC_ENC");
    private final static QName _CardMoCKeysMoCMAC_QNAME = new QName("http://schemas.datacontract.org/2004/07/EMKS_WCFService", "MoC_MAC");
    private final static QName _GetSignatureRndData_QNAME = new QName("http://tempuri.org/", "RndData");
    private final static QName _GetSignatureResponseGetSignatureResult_QNAME = new QName("http://tempuri.org/", "GetSignatureResult");
    private final static QName _GetNIDCardMoCKeysResponseGetNIDCardMoCKeysResult_QNAME = new QName("http://tempuri.org/", "GetNIDCardMoCKeysResult");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.gam.nocr.ems.biz.service.external.client.emks.lds
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetNIDCardKeysAndPINsResponse }
     * 
     */
    public GetNIDCardKeysAndPINsResponse createGetNIDCardKeysAndPINsResponse() {
        return new GetNIDCardKeysAndPINsResponse();
    }

    /**
     * Create an instance of {@link Exception }
     * 
     */
    public Exception createException() {
        return new Exception();
    }

    /**
     * Create an instance of {@link EMKSException }
     * 
     */
    public EMKSException createEMKSException() {
        return new EMKSException();
    }

    /**
     * Create an instance of {@link CardKeysAndPINs }
     * 
     */
    public CardKeysAndPINs createCardKeysAndPINs() {
        return new CardKeysAndPINs();
    }

    /**
     * Create an instance of {@link GetNIDCardMoCKeys }
     * 
     */
    public GetNIDCardMoCKeys createGetNIDCardMoCKeys() {
        return new GetNIDCardMoCKeys();
    }

    /**
     * Create an instance of {@link CardMoCKeys }
     * 
     */
    public CardMoCKeys createCardMoCKeys() {
        return new CardMoCKeys();
    }

    /**
     * Create an instance of {@link GetSignature }
     * 
     */
    public GetSignature createGetSignature() {
        return new GetSignature();
    }

    /**
     * Create an instance of {@link GetNIDCardKeysAndPINs }
     * 
     */
    public GetNIDCardKeysAndPINs createGetNIDCardKeysAndPINs() {
        return new GetNIDCardKeysAndPINs();
    }

    /**
     * Create an instance of {@link GetSignatureResponse }
     * 
     */
    public GetSignatureResponse createGetSignatureResponse() {
        return new GetSignatureResponse();
    }

    /**
     * Create an instance of {@link GetNIDCardMoCKeysResponse }
     * 
     */
    public GetNIDCardMoCKeysResponse createGetNIDCardMoCKeysResponse() {
        return new GetNIDCardMoCKeysResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CardKeysAndPINs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetNIDCardKeysAndPINsResult", scope = GetNIDCardKeysAndPINsResponse.class)
    public JAXBElement<CardKeysAndPINs> createGetNIDCardKeysAndPINsResponseGetNIDCardKeysAndPINsResult(CardKeysAndPINs value) {
        return new JAXBElement<CardKeysAndPINs>(_GetNIDCardKeysAndPINsResponseGetNIDCardKeysAndPINsResult_QNAME, CardKeysAndPINs.class, GetNIDCardKeysAndPINsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "anyURI")
    public JAXBElement<String> createAnyURI(String value) {
        return new JAXBElement<String>(_AnyURI_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "char")
    public JAXBElement<Integer> createChar(Integer value) {
        return new JAXBElement<Integer>(_Char_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedByte")
    public JAXBElement<Short> createUnsignedByte(Short value) {
        return new JAXBElement<Short>(_UnsignedByte_QNAME, Short.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "dateTime")
    public JAXBElement<XMLGregorianCalendar> createDateTime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_DateTime_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "anyType")
    public JAXBElement<Object> createAnyType(Object value) {
        return new JAXBElement<Object>(_AnyType_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedInt")
    public JAXBElement<Long> createUnsignedInt(Long value) {
        return new JAXBElement<Long>(_UnsignedInt_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "int")
    public JAXBElement<Integer> createInt(Integer value) {
        return new JAXBElement<Integer>(_Int_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "QName")
    public JAXBElement<QName> createQName(QName value) {
        return new JAXBElement<QName>(_QName_QNAME, QName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedShort")
    public JAXBElement<Integer> createUnsignedShort(Integer value) {
        return new JAXBElement<Integer>(_UnsignedShort_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "float")
    public JAXBElement<Float> createFloat(Float value) {
        return new JAXBElement<Float>(_Float_QNAME, Float.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "decimal")
    public JAXBElement<BigDecimal> createDecimal(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_Decimal_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "long")
    public JAXBElement<Long> createLong(Long value) {
        return new JAXBElement<Long>(_Long_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "double")
    public JAXBElement<Double> createDouble(Double value) {
        return new JAXBElement<Double>(_Double_QNAME, Double.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "short")
    public JAXBElement<Short> createShort(Short value) {
        return new JAXBElement<Short>(_Short_QNAME, Short.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "guid")
    public JAXBElement<String> createGuid(String value) {
        return new JAXBElement<String>(_Guid_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "base64Binary")
    public JAXBElement<byte[]> createBase64Binary(byte[] value) {
        return new JAXBElement<byte[]>(_Base64Binary_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Duration }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "duration")
    public JAXBElement<Duration> createDuration(Duration value) {
        return new JAXBElement<Duration>(_Duration_QNAME, Duration.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Byte }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "byte")
    public JAXBElement<Byte> createByte(Byte value) {
        return new JAXBElement<Byte>(_Byte_QNAME, Byte.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Exception }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/System", name = "Exception")
    public JAXBElement<Exception> createException(Exception value) {
        return new JAXBElement<Exception>(_Exception_QNAME, Exception.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CardKeysAndPINs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EMKS_WCFService", name = "CardKeysAndPINs")
    public JAXBElement<CardKeysAndPINs> createCardKeysAndPINs(CardKeysAndPINs value) {
        return new JAXBElement<CardKeysAndPINs>(_CardKeysAndPINs_QNAME, CardKeysAndPINs.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "string")
    public JAXBElement<String> createString(String value) {
        return new JAXBElement<String>(_String_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EMKSException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EMKS_WCFService", name = "EMKSException")
    public JAXBElement<EMKSException> createEMKSException(EMKSException value) {
        return new JAXBElement<EMKSException>(_EMKSException_QNAME, EMKSException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedLong")
    public JAXBElement<BigInteger> createUnsignedLong(BigInteger value) {
        return new JAXBElement<BigInteger>(_UnsignedLong_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CardMoCKeys }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EMKS_WCFService", name = "CardMoCKeys")
    public JAXBElement<CardMoCKeys> createCardMoCKeys(CardMoCKeys value) {
        return new JAXBElement<CardMoCKeys>(_CardMoCKeys_QNAME, CardMoCKeys.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "boolean")
    public JAXBElement<Boolean> createBoolean(Boolean value) {
        return new JAXBElement<Boolean>(_Boolean_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EMKS_WCFService", name = "ErrorCode", scope = EMKSException.class)
    public JAXBElement<String> createEMKSExceptionErrorCode(String value) {
        return new JAXBElement<String>(_EMKSExceptionErrorCode_QNAME, String.class, EMKSException.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Exception }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EMKS_WCFService", name = "Inner", scope = EMKSException.class)
    public JAXBElement<Exception> createEMKSExceptionInner(Exception value) {
        return new JAXBElement<Exception>(_EMKSExceptionInner_QNAME, Exception.class, EMKSException.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EMKS_WCFService", name = "SM", scope = CardKeysAndPINs.class)
    public JAXBElement<String> createCardKeysAndPINsSM(String value) {
        return new JAXBElement<String>(_CardKeysAndPINsSM_QNAME, String.class, CardKeysAndPINs.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EMKS_WCFService", name = "ID", scope = CardKeysAndPINs.class)
    public JAXBElement<String> createCardKeysAndPINsID(String value) {
        return new JAXBElement<String>(_CardKeysAndPINsID_QNAME, String.class, CardKeysAndPINs.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EMKS_WCFService", name = "ASD_MAC", scope = CardKeysAndPINs.class)
    public JAXBElement<String> createCardKeysAndPINsASDMAC(String value) {
        return new JAXBElement<String>(_CardKeysAndPINsASDMAC_QNAME, String.class, CardKeysAndPINs.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EMKS_WCFService", name = "ASD_ENC", scope = CardKeysAndPINs.class)
    public JAXBElement<String> createCardKeysAndPINsASDENC(String value) {
        return new JAXBElement<String>(_CardKeysAndPINsASDENC_QNAME, String.class, CardKeysAndPINs.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EMKS_WCFService", name = "Sign", scope = CardKeysAndPINs.class)
    public JAXBElement<String> createCardKeysAndPINsSign(String value) {
        return new JAXBElement<String>(_CardKeysAndPINsSign_QNAME, String.class, CardKeysAndPINs.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EMKS_WCFService", name = "NMoC", scope = CardKeysAndPINs.class)
    public JAXBElement<String> createCardKeysAndPINsNMoC(String value) {
        return new JAXBElement<String>(_CardKeysAndPINsNMoC_QNAME, String.class, CardKeysAndPINs.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EMKS_WCFService", name = "ASD", scope = CardKeysAndPINs.class)
    public JAXBElement<String> createCardKeysAndPINsASD(String value) {
        return new JAXBElement<String>(_CardKeysAndPINsASD_QNAME, String.class, CardKeysAndPINs.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "CardInfo", scope = GetNIDCardMoCKeys.class)
    public JAXBElement<String> createGetNIDCardMoCKeysCardInfo(String value) {
        return new JAXBElement<String>(_GetNIDCardMoCKeysCardInfo_QNAME, String.class, GetNIDCardMoCKeys.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EMKS_WCFService", name = "MoC_ENC", scope = CardMoCKeys.class)
    public JAXBElement<String> createCardMoCKeysMoCENC(String value) {
        return new JAXBElement<String>(_CardMoCKeysMoCENC_QNAME, String.class, CardMoCKeys.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/EMKS_WCFService", name = "MoC_MAC", scope = CardMoCKeys.class)
    public JAXBElement<String> createCardMoCKeysMoCMAC(String value) {
        return new JAXBElement<String>(_CardMoCKeysMoCMAC_QNAME, String.class, CardMoCKeys.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "RndData", scope = GetSignature.class)
    public JAXBElement<String> createGetSignatureRndData(String value) {
        return new JAXBElement<String>(_GetSignatureRndData_QNAME, String.class, GetSignature.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "CardInfo", scope = GetNIDCardKeysAndPINs.class)
    public JAXBElement<String> createGetNIDCardKeysAndPINsCardInfo(String value) {
        return new JAXBElement<String>(_GetNIDCardMoCKeysCardInfo_QNAME, String.class, GetNIDCardKeysAndPINs.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetSignatureResult", scope = GetSignatureResponse.class)
    public JAXBElement<String> createGetSignatureResponseGetSignatureResult(String value) {
        return new JAXBElement<String>(_GetSignatureResponseGetSignatureResult_QNAME, String.class, GetSignatureResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CardMoCKeys }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetNIDCardMoCKeysResult", scope = GetNIDCardMoCKeysResponse.class)
    public JAXBElement<CardMoCKeys> createGetNIDCardMoCKeysResponseGetNIDCardMoCKeysResult(CardMoCKeys value) {
        return new JAXBElement<CardMoCKeys>(_GetNIDCardMoCKeysResponseGetNIDCardMoCKeysResult_QNAME, CardMoCKeys.class, GetNIDCardMoCKeysResponse.class, value);
    }

}
