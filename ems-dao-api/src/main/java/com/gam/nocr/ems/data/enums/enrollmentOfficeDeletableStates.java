package com.gam.nocr.ems.data.enums;

public enum enrollmentOfficeDeletableStates {
    NO_REQUEST, REGULAR_REQUESTS, CRITICAL_REQUESTS, NOCR_OFFICE;

    @Override
    public String toString() {
        switch (this) {
            case NO_REQUEST:
                return "NO_REQUEST";
            case REGULAR_REQUESTS:
                return "REGULAR_REQUESTS";
            case CRITICAL_REQUESTS:
                return "CRITICAL_REQUESTS";
            case NOCR_OFFICE:
                return "NOCR_OFFICE";
        }
        return null;
    }
}
