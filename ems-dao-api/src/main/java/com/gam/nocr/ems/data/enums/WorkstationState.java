package com.gam.nocr.ems.data.enums;

/**
 * <p> TODO -- Explain this class </p>
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public enum WorkstationState {
    N, // NEW
    A, // APPROVED
    R; // REJECT

    public static Long toLong(WorkstationState state) {
        if (state == null) {
            return null;
        }

        switch (state) {
            case N:
                return 1L;
            case A:
                return 2L;
            case R:
                return 3L;
        }

        return null;
    }

    public static WorkstationState toState(Long state) {
        if (state == null) {
            return null;
        }

        switch (state.intValue()) {
            case 1:
                return N;
            case 2:
                return A;
            case 3:
                return R;
        }

        return null;
    }
}
