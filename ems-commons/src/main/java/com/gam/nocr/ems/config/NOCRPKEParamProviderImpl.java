/**
 *
 */
package com.gam.nocr.ems.config;

import com.gam.commons.profile.ProfileException;
import com.gam.commons.profile.ProfileManager;
import com.gam.commons.security.SecurityException;
import com.gam.commons.security.pke.GamPKEParamProviderImpl;

/**
 * An implementation of {@link com.gam.commons.security.pke.GamPKEParamProviderImpl} that encapsulates all configuration
 * items that would be used in certificate validation process
 *
 * @author Sina Golesorkhi
 */
public class NOCRPKEParamProviderImpl extends GamPKEParamProviderImpl {

    public NOCRPKEParamProviderImpl(ProfileManager profileManager) throws SecurityException, ProfileException {
        super(null);
        super.setCheckCertificateValidity((String) profileManager.getProfile("nocr.ems.profile.security.pke.certificateValiodationCacheTime", true, null, null));
        super.setCheckCertificateValidityForEnvelopeVerification((String) profileManager.getProfile("nocr.ems.profile.security.pke.certificateValiodationCacheTime", true, null, null));
        super.setCRLRootCertificatesKeystorePath((String) profileManager.getProfile("nocr.ems.profile.security.pke.crl.certStore.key.path", true, null, null));
        super.setCRLCertificatesKeystorePass((String) profileManager.getProfile("nocr.ems.profile.security.pke.crl.certStore.key.pass", true, null, null));
        super.setOCSPRootCertificatesKeystorePath((String) profileManager.getProfile("nocr.ems.profile.security.pke.ocsp.certStore.key.path", true, null, null));
        super.setOCSPCertificatesKeystorePass((String) profileManager.getProfile("nocr.ems.profile.security.pke.ocsp.certStore.key.pass", true, null, null));
        super.setValidationMethod((String) profileManager.getProfile("nocr.ems.profile.security.pke.profile.validationMethod", true, null, null));
        super.setCertificateValidationCacheTime((String) profileManager.getProfile("nocr.ems.profile.security.pke.profile.certificateValiodationCacheTime", true, null, null));
        super.setCRLAddresses((String) profileManager.getProfile("nocr.ems.profile.security.pke.profile.crlAddresses", true, null, null));
        super.setUseCRLCache((String) profileManager.getProfile("nocr.ems.profile.security.pke.profile.useCRLCache", true, null, null));
        super.setCRLCacheTime((String) profileManager.getProfile("nocr.ems.profile.security.pke.profile.crlCacheTime", true, null, null));
        super.setSymmetricDecryptionTransformation((String) profileManager.getProfile("nocr.ems.profile.security.pke.profile.symmetricDecryptionTransformation", true, null, null));
        super.setSymmetricDecSecretKeyAlgorithm((String) profileManager.getProfile("nocr.ems.profile.security.pke.profile.symmetricDecSecretKeyAlgorithm", true, null, null));
        super.setOCSPResponderKeyStorePass((String) profileManager.getProfile("nocr.ems.profile.security.pke.profile.setOCSPResponderKeyStorePass", true, null, null));
        super.setOCSPResponderKeyStorePath((String) profileManager.getProfile("nocr.ems.profile.security.pke.profile.setOCSPResponderKeyStorePath", true, null, null));
    }
}
