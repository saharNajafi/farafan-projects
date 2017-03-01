package com.gam.nocr.ems.web.listreader.processor;

import com.gam.commons.listreader.ListReaderException;
import com.gam.commons.listreader.processor.BaseVLPListProcessor;
import com.gam.nocr.ems.config.EMSValueListProvider;
import gampooya.tools.vlp.ValueListProvider;

/**
 * Base class for all processors in EMS project. It overrides the
 * {@link com.gam.commons.listreader.processor.BaseVLPListProcessor#getValueListProvider()} returning project's
 * {@link gampooya.tools.vlp.ValueListProvider} implementation
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class EMSVLPListProcessor extends BaseVLPListProcessor {

    public EMSVLPListProcessor() {
        super();
    }

    /**
     * Returns an instance of project's {@link gampooya.tools.vlp.ValueListProvider} implementation
     *
     * @return an instance of project's {@link gampooya.tools.vlp.ValueListProvider} implementation
     * @throws ListReaderException
     */
    @Override
    public ValueListProvider getValueListProvider() throws ListReaderException {
        return EMSValueListProvider.getProvider();
    }
}
