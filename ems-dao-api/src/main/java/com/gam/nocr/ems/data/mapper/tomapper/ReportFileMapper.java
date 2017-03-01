package com.gam.nocr.ems.data.mapper.tomapper;

import java.util.ArrayList;
import java.util.List;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.ReportFileTO;
import com.gam.nocr.ems.data.domain.vol.ReportFileVTO;
import com.gam.nocr.ems.data.enums.ReportFileType;

/**
 * Created by IntelliJ IDEA.
 * User: jalilian
 * Date: 5/12/13
 * Time: 2:18 PM
 */
public class ReportFileMapper {

    private ReportFileMapper() {
    }

    public static List<ReportFileTO> convert(List<ReportFileVTO> reportFileVTOs) throws BaseException {
        List<ReportFileTO> reportFileTOs = new ArrayList<ReportFileTO>();
        for (ReportFileVTO reportFileVTO : reportFileVTOs) {
            ReportFileTO reportFileTO = new ReportFileTO();
            reportFileTO.setCaption(reportFileVTO.getCaption());
            reportFileTO.setType(ReportFileType.valueOf(reportFileVTO.getType()));
            reportFileTO.setData(reportFileVTO.getData());
            reportFileTOs.add(reportFileTO);
        }
        return reportFileTOs;
    }
}
