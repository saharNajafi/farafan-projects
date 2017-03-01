package com.gam.nocr.ems.data.mapper.tomapper;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.vol.UserVTO;
import com.gam.nocr.ems.data.domain.ws.UserWTO;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * <p> TODO -- Explain this class </p>
 *
 * @author <a href="mailto:saadat@gamelectronics.com.com">Alireza Saadat</a>
 */
public class UserMapper {

    public static UserVTO convert(UserWTO userWTO) throws BaseException {
        UserVTO userVTO = new UserVTO();

        if (EmsUtil.checkString(userWTO.getOldPassword()))
            userVTO.setOldPassword(userWTO.getOldPassword());
        if (EmsUtil.checkString(userWTO.getNewPassword()))
            userVTO.setNewPassword(userWTO.getNewPassword());
        if (EmsUtil.checkString(userWTO.getConfirmNewPassword()))
            userVTO.setConfirmNewPassword(userWTO.getConfirmNewPassword());

        return userVTO;
    }
}
