package com.gam.nocr.ems.config;

import com.gam.commons.core.BaseRuntimeExceptionCode;


/**
 * Collection of constant values representing dao layer error codes and their corresponding messages (to log in
 * console)
 *
 * @author: Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class DataExceptionCode extends BaseRuntimeExceptionCode {

    public static final String GLB_001_MSG = "The {} was not provided in the request";
    public static final String GLB_002_MSG = "The result value of the query could not be null.";
    public static final String GLB_003_MSG = "unable to perform action.";
    public static final String GLB_004_MSG = "The save operation could not be executed.";
    public static final String GLB_005_MSG = "The find operation could not be executed.";
    public static final String GLB_006_MSG = "The update operation could not be executed.";
    public static final String GLB_007_MSG = "The delete operation could not be executed.";
    public static final String GLB_008_MSG = "The parsing operation could not be done successfully.";
    public static final String GLB_009_MSG = "Cannot create dao with name {}";
    public static final String GLB_010_MSG = "The {} was not provided in the IMS sent XML";
    public static final String GLB_ERR_MSG = "\nException was happened in the middle of the process. The stack trace is : ";

    // com.gam.nocr.ems.data.mapper.xmlmapper.CardRequestCMSMapper
    public static final String CRC_001 = "EMS_D_CRC_001";
    public static final String CRC_002 = "EMS_D_CRC_002";
    public static final String CRC_003 = "EMS_D_CRC_003";
    public static final String CRC_004 = "EMS_D_CRC_004";
    public static final String CRC_005 = "EMS_D_CRC_005";
    public static final String CRC_006 = "EMS_D_CRC_006";
    public static final String CRC_007 = "EMS_D_CRC_007";
    public static final String CRC_008 = "EMS_D_CRC_008";
    public static final String CRC_009 = "EMS_D_CRC_009";

    // com.gam.nocr.ems.data.mapper.xmlmapper.PersonPKIMapper
    public static final String PPM_001 = "EMS_D_PPM_001";
    public static final String PPM_002 = "EMS_D_PPM_002";
    public static final String PPM_003 = "EMS_D_PPM_003";
    public static final String PPM_004 = "EMS_D_PPM_004";
    public static final String PPM_005 = "EMS_D_PPM_005";
    public static final String PPM_006 = "EMS_D_PPM_006";
    public static final String PPM_007 = "EMS_D_PPM_007";

    // com.gam.nocr.ems.data.mapper.xmlmapper.EnrollmentPKIMapper
    public static final String EPM_001 = "EMS_D_EPM_001";
    public static final String EPM_002 = "EMS_D_EPM_002";
    public static final String EPM_003 = "EMS_D_EPM_003";
    public static final String EPM_004 = "EMS_D_EPM_004";
    public static final String EPM_005 = "EMS_D_EPM_005";
    public static final String EPM_006 = "EMS_D_EPM_006";

    // com.gam.nocr.ems.data.mapper.xmlmapper.TokenPKIMapper
    public static final String TPM_001 = "EMS_D_TPM_001";
    public static final String TPM_002 = "EMS_D_TPM_002";
    public static final String TPM_003 = "EMS_D_TPM_003";
    public static final String TPM_004 = "EMS_D_TPM_004";
    public static final String TPM_005 = "EMS_D_TPM_005";
    public static final String TPM_006 = "EMS_D_TPM_006";
    public static final String TPM_007 = "EMS_D_TPM_007";
    public static final String TPM_008 = "EMS_D_TPM_008";
    public static final String TPM_009 = "EMS_D_TPM_009";
    public static final String TPM_010 = "EMS_D_TPM_010";
    public static final String TPM_011 = "EMS_D_TPM_011";
    public static final String TPM_012 = "EMS_D_TPM_012";
    public static final String TPM_013 = "EMS_D_TPM_013";
    public static final String TPM_014 = "EMS_D_TPM_014";
    public static final String TPM_015 = "EMS_D_TPM_015";
    public static final String TPM_016 = "EMS_D_TPM_016";
    public static final String TPM_017 = "EMS_D_TPM_017";
    public static final String TPM_018 = "EMS_D_TPM_018";
    public static final String TPM_019 = "EMS_D_TPM_019";
    public static final String TPM_020 = "EMS_D_TPM_020";
    public static final String TPM_021 = "EMS_D_TPM_021";
    public static final String TPM_022 = "EMS_D_TPM_022";
    public static final String TPM_023 = "EMS_D_TPM_023";
    public static final String TPM_024 = "EMS_D_TPM_024";
    public static final String TPM_025 = "EMS_D_TPM_025";
    public static final String TPM_026 = "EMS_D_TPM_026";
    public static final String TPM_027 = "EMS_D_TPM_027";

    // com.gam.nocr.ems.data.mapper.xmlmapper.IMSUpdateCitizenInfoMapper
    public static final String IUC_001 = "EMS_D_IUC_001";
    public static final String IUC_002 = "EMS_D_IUC_002";
    public static final String IUC_003 = "EMS_D_IUC_003";
    public static final String IUC_004 = "EMS_D_IUC_004";
    public static final String IUC_005 = "EMS_D_IUC_005";
    public static final String IUC_006 = "EMS_D_IUC_006";
    public static final String IUC_007 = "EMS_D_IUC_007";
    public static final String IUC_008 = "EMS_D_IUC_008";
    public static final String IUC_009 = "EMS_D_IUC_009";
    public static final String IUC_010 = "EMS_D_IUC_010";
    public static final String IUC_011 = "EMS_D_IUC_011";
    public static final String IUC_012 = "EMS_D_IUC_012";
    public static final String IUC_013 = "EMS_D_IUC_013";
    public static final String IUC_014 = "EMS_D_IUC_014";
    public static final String IUC_015 = "EMS_D_IUC_015";
    public static final String IUC_016 = "EMS_D_IUC_016";
    public static final String IUC_017 = "EMS_D_IUC_017";
    public static final String IUC_018 = "EMS_D_IUC_018";
    public static final String IUC_019 = "EMS_D_IUC_019";
    public static final String IUC_020 = "EMS_D_IUC_020";
    public static final String IUC_021 = "EMS_D_IUC_021";
    public static final String IUC_022 = "EMS_D_IUC_022";
    public static final String IUC_023 = "EMS_D_IUC_023";
    public static final String IUC_024 = "EMS_D_IUC_024";
    public static final String IUC_025 = "EMS_D_IUC_025";
    public static final String IUC_026 = "EMS_D_IUC_026";
    public static final String IUC_027 = "EMS_D_IUC_027";
    public static final String IUC_028 = "EMS_D_IUC_028";
    public static final String IUC_029 = "EMS_D_IUC_029";
    public static final String IUC_030 = "EMS_D_IUC_030";
    public static final String IUC_031 = "EMS_D_IUC_031";
    public static final String IUC_032 = "EMS_D_IUC_032";
    public static final String IUC_033 = "EMS_D_IUC_033";
    public static final String IUC_034 = "EMS_D_IUC_034";
    public static final String IUC_003_MSG = "The mapper '{}' could not do the mapping operation because the instance of type '{}' which was passed to this mapper was null.";
    public static final String IUC_004_MSG = "The mapper could not do the mapping operation because the field of {0} which was passed to this mapper was null.";

    // com.gam.nocr.ems.data.mapper.xmlmapper.IMSUpdateResultMapper
    public static final String IUR_001 = "EMS_D_IUR_001";
    public static final String IUR_002 = "EMS_D_IUR_002";
    public static final String IUR_003 = "EMS_D_IUR_003";
    public static final String IUR_004 = "EMS_D_IUR_004";
    public static final String IUR_005 = "EMS_D_IUR_005";
    public static final String IUR_006 = "EMS_D_IUR_006";
    public static final String IUR_007 = "EMS_D_IUR_007";
    public static final String IUR_008 = "EMS_D_IUR_008";
    public static final String IUR_009 = "EMS_D_IUR_009";
    public static final String IUR_010 = "EMS_D_IUR_010";
    public static final String IUR_011 = "EMS_D_IUR_011";
    public static final String IUR_012 = "EMS_D_IUR_012";
    public static final String IUR_013 = "EMS_D_IUR_013";
    public static final String IUR_014 = "EMS_D_IUR_014";
    public static final String IUR_015 = "EMS_D_IUR_015";
    public static final String IUR_016 = "EMS_D_IUR_016";
    public static final String IUR_017 = "EMS_D_IUR_017";
    public static final String IUR_018 = "EMS_D_IUR_018";
    public static final String IUR_019 = "EMS_D_IUR_019";

    // com.gam.nocr.ems.data.dao.impl.PersonDAOImpl
    public static final String PDI_001 = "EMS_D_PDI_001";
    public static final String PDI_002 = "EMS_D_PDI_002";
    public static final String PDI_003 = "EMS_D_PDI_003";
    public static final String PDI_004 = "EMS_D_PDI_004";
    public static final String PDI_005 = "EMS_D_PDI_005";
    public static final String PDI_006 = "EMS_D_PDI_006";
    public static final String PDI_007 = "EMS_D_PDI_007";
    public static final String PDI_008 = "EMS_D_PDI_008";
    public static final String PDI_009 = "EMS_D_PDI_009";
    public static final String PDI_010 = "EMS_D_PDI_010";
    public static final String PDI_011 = "EMS_D_PDI_011";
    public static final String PDI_012 = "EMS_D_PDI_012";
    public static final String PDI_013 = "EMS_D_PDI_013";
    public static final String PDI_014 = "EMS_D_PDI_014";
    public static final String PDI_015 = "EMS_D_PDI_015";
    public static final String PDI_016 = "EMS_D_PDI_016";
    public static final String PDI_017 = "EMS_D_PDI_017";
    public static final String PDI_018 = "EMS_D_PDI_018";
    public static final String PDI_019 = "EMS_D_PDI_019";
    public static final String PDI_020 = "EMS_D_PDI_020";
    public static final String PDI_021 = "EMS_D_PDI_021";
    public static final String PDI_022 = "EMS_D_PDI_022";
    public static final String PDI_023 = "EMS_D_PDI_023";
    public static final String PDI_024 = "EMS_D_PDI_024";
    public static final String PDI_025 = "EMS_D_PDI_025";
    public static final String PDI_026 = "EMS_D_PDI_026";
    public static final String PDI_027 = "EMS_D_PDI_027";
    public static final String PDI_028 = "EMS_D_PDI_028";
    public static final String PDI_029 = "EMS_D_PDI_029";
    public static final String PDI_030 = "EMS_D_PDI_030";
    public static final String PDI_031 = "EMS_D_PDI_031";
    public static final String PDI_032 = "EMS_D_PDI_032";
    public static final String PDI_033 = "EMS_D_PDI_033";
    public static final String PDI_034 = "EMS_D_PDI_034";
    public static final String PDI_035 = "EMS_D_PDI_035";
    public static final String PDI_036 = "EMS_D_PDI_036";
    public static final String PDI_037 = "EMS_D_PDI_037";
    public static final String PDI_038 = "EMS_D_PDI_038";
    public static final String PDI_039 = "EMS_D_PDI_039";
    public static final String PDI_040 = "EMS_D_PDI_039";

    public static final String PDI_001_MSG = "unable to change status";
    public static final String PDI_002_MSG = "violate unique constraint, national ID already exists";
    public static final String PDI_003_MSG = "violate unique constraint, person ID already exists";
    public static final String PDI_004_MSG = "violate unique constraint, user name already exists";
    public static final String PDI_005_MSG = "cannot save user in database";
    public static final String PDI_006_MSG = "violate foreign key constraint, this person already has token";
    public static final String PDI_007_MSG = "cannot find person";
    public static final String PDI_010_MSG = "violate foreign key constraint, the person already has profile";
    public static final String PDI_017_MSG = "violate foreign key constraint, the person already assigned as manager";
    public static final String PDI_025_MSG = "Integrity constraint violated, no enrollment office with this id exists";
    

    // com.gam.nocr.ems.data.dao.impl.SMSDAOImpl
    public static final String SDI_001 = "EMS_D_SDI_001";
    public static final String SDI_002 = "EMS_D_SDI_002";
    public static final String SDI_003 = "EMS_D_SDI_003";

    // com.gam.nocr.ems.data.dao.impl.EncryptedCardRequestDAOImpl
    public static final String ECI_001 = "EMS_D_ECI_001";
    
    // com.gam.nocr.ems.data.dao.impl.ImsEstelamImageDAOImpl
    public static final String IDI_001 = "EMS_D_IDI_001";
    public static final String IDI_002 = "EMS_D_IDI_002";
    public static final String IDI_003 = "EMS_D_IDI_003";
    public static final String IDI_001_MSG = "Unable to create ImsEstelamImage object";
    public static final String IDI_002_MSG = "Unable to execute find operation for ImsEstelamImage by nationalID and imageType";

    // com.gam.nocr.ems.data.dao.impl.DepartmentDAOImpl
    public static final String DDI_001 = "EMS_D_DDI_001";
    public static final String DDI_002 = "EMS_D_DDI_002";
    public static final String DDI_003 = "EMS_D_DDI_003";
    public static final String DDI_004 = "EMS_D_DDI_004";
    public static final String DDI_005 = "EMS_D_DDI_005";
    public static final String DDI_006 = "EMS_D_DDI_006";
    public static final String DDI_007 = "EMS_D_DDI_007";
    public static final String DDI_008 = "EMS_D_DDI_008";
    public static final String DDI_009 = "EMS_D_DDI_009";
    public static final String DDI_010 = "EMS_D_DDI_010";
    public static final String DDI_011 = "EMS_D_DDI_011";
    public static final String DDI_012 = "EMS_D_DDI_012";
    public static final String DDI_013 = "EMS_D_DDI_013";
    public static final String DDI_014 = "EMS_D_DDI_014";
    public static final String DDI_015 = "EMS_D_DDI_015";
    public static final String DDI_016 = "EMS_D_DDI_016";
    public static final String DDI_017 = "EMS_D_DDI_017";
    public static final String DDI_018 = "EMS_D_DDI_018";
    public static final String DDI_019 = "EMS_D_DDI_019";
    public static final String DDI_020 = "EMS_D_DDI_020";
    public static final String DDI_021 = "EMS_D_DDI_021";
    public static final String DDI_022 = "EMS_D_DDI_022";
    public static final String DDI_023 = "EMS_D_DDI_023";
    public static final String DDI_024 = "EMS_D_DDI_024";
    public static final String DDI_025 = "EMS_D_DDI_025";
    public static final String DDI_026 = "EMS_D_DDI_026";
    public static final String DDI_027 = "EMS_D_DDI_027";
    public static final String DDI_028 = "EMS_D_DDI_028";
    public static final String DDI_029 = "EMS_D_DDI_029";
    public static final String DDI_030 = "EMS_D_DDI_030";
    public static final String DDI_031 = "EMS_D_DDI_031";
    public static final String DDI_032 = "EMS_D_DDI_032";
    public static final String DDI_033 = "EMS_D_DDI_033";
    public static final String DDI_034 = "EMS_D_DDI_034";
    public static final String DDI_035 = "EMS_D_DDI_035";
    public static final String DDI_036 = "EMS_D_DDI_036";
    public static final String DDI_037 = "EMS_D_DDI_037";
    public static final String DDI_038 = "EMS_D_DDI_038";
    public static final String DDI_039 = "EMS_D_DDI_039";
    public static final String DDI_040 = "EMS_D_DDI_040";
    public static final String DDI_041 = "EMS_D_DDI_041";
    public static final String DDI_042 = "EMS_D_DDI_042";
    public static final String DDI_043 = "EMS_D_DDI_043";
    public static final String DDI_044 = "EMS_D_DDI_044";
    public static final String DDI_045 = "EMS_D_DDI_045";
    public static final String DDI_046 = "EMS_D_DDI_046";
    public static final String DDI_047 = "EMS_D_DDI_047";
    public static final String DDI_048 = "EMS_D_DDI_048";
    public static final String DDI_049 = "EMS_D_DDI_049";
    public static final String DDI_050 = "EMS_D_DDI_050";
    public static final String DDI_051 = "EMS_D_DDI_051";
    public static final String DDI_052 = "EMS_D_DDI_052";
    public static final String DDI_053 = "EMS_D_DDI_053";
    public static final String DDI_002_MSG = "foreign key constraint violation, it already has sub department";
    public static final String DDI_003_MSG = "unable to perform action";
    public static final String DDI_004_MSG = "violate unique constraint, department code already exists";
    public static final String DDI_005_MSG = "violate unique constraint, department DN already exists";
    public static final String DDI_006_MSG = "violate unique constraint, department name already exists";
    public static final String DDI_007_MSG = "unable to to update department";
    public static final String DDI_008_MSG = "unable to to find department";
    public static final String DDI_009_MSG = "foreign key constraint violation, a person is already assigned to this department";
    public static final String DDI_010_MSG = "foreign key constraint violation, a box already sent to this department";
    public static final String DDI_011_MSG = "foreign key constraint violation, a batch already sent to this department";
    public static final String DDI_012_MSG = "foreign key constraint violation, this profile is an enrollment office";
    public static final String DDI_013_MSG = "foreign key constraint violation, there is an infrastructure profile assigned to this department";
    public static final String DDI_014_MSG = "foreign key constraint violation, there is at least one card request sent from this office";
    public static final String DDI_015_MSG = "foreign key constraint violation, the office already has a token";
    public static final String DDI_016_MSG = "foreign key constraint violation, at least one workstation assigned to the office";
    public static final String DDI_018_MSG = "Problem when trying to load department with id {}, data is invalid";
    public static final String DDI_020_MSG = "No dispatch send types found for department with parent department id {}";
    public static final String DDI_021_MSG = "Integrity constraint violated, location province with id {} not found";
    public static final String DDI_022_MSG = "Problem when trying to load dispatch send type, data is invalid";
    public static final String DDI_023_MSG = "foreign key constraint violation, at least one reservation assigned to the office";
    public static final String DDI_024_MSG = "Problem when trying to load department with id {}, data is invalid";
    public static final String DDI_025_MSG = "Integrity constraint violated, department location province with id {} not found";
    public static final String DDI_026_MSG = "Integrity constraint violated, parent department with id {} not found";
    public static final String DDI_027_MSG = "Integrity constraint violated, department with id {} not found for enrollment office";
    public static final String DDI_028_MSG = "Integrity constraint violated, manager with id {} not found for enrollment office";
    public static final String DDI_029_MSG = "Integrity constraint violated, rating info with id {} not found for enrollment office";
    public static final String DDI_030_MSG = "Unable to create department";
    public static final String DDI_031_MSG = "Integrity constraint violated, department location province with id {} not found";
    public static final String DDI_032_MSG = "Integrity constraint violated, parent department with id {} not found";
    public static final String DDI_033_MSG = "Integrity constraint violated, department with id {} not found for enrollment office";
    public static final String DDI_034_MSG = "Integrity constraint violated, manager with id {} not found for enrollment office";
    public static final String DDI_035_MSG = "Integrity constraint violated, rating info with id {} not found for enrollment office";
    public static final String DDI_036_MSG = "Foreign key constraint violated, at least one box refers to this department";
    public static final String DDI_037_MSG = "Foreign key constraint violated, at least one batch refers to this department";
    public static final String DDI_038_MSG = "Foreign key constraint violated, at least one profile refers to this department";
    public static final String DDI_039_MSG = "Foreign key constraint violated, at least one person refers to this department";
    public static final String DDI_040_MSG = "Foreign key constraint violated, at least one enrollment office refers to this department";
    public static final String DDI_041_MSG = "Foreign key constraint violated, at least one workstation refers to this department";
    public static final String DDI_042_MSG = "Foreign key constraint violated, at least one network token refers to this department";
    public static final String DDI_043_MSG = "Foreign key constraint violated, at least one reservation refers to this department";
    public static final String DDI_044_MSG = "Foreign key constraint violated, at least one card request refers to this department";
    public static final String DDI_045_MSG = "Office cannot be deleted due to constraint violation";
    public static final String DDI_046_MSG = "Unable to remove departments";
    public static final String DDI_047_MSG = "Invalid input - department ids are not valid";
    public static final String DDI_048_MSG = "Integrity constraint violated, enrollment office type is not valid";
    public static final String DDI_049_MSG = "Unique constraint violated, a department already assigned to selected manager";
    public static final String DDI_052_MSG = "Foreign key constraint violated, this office is assigned as superior office";
    public static final String DDI_053_MSG = "Foreign key constraint violated, a card request originated from this office";

    // com.gam.nocr.ems.data.dao.impl.WorkstationDAOImpl
    public static final String WDI_001 = "EMS_D_WDI_001";
    public static final String WDI_002 = "EMS_D_WDI_002";
    public static final String WDI_003 = "EMS_D_WDI_003";
    public static final String WDI_004 = "EMS_D_WDI_004";
    public static final String WDI_005 = "EMS_D_WDI_005";
    public static final String WDI_006 = "EMS_D_WDI_006";
    public static final String WDI_007 = "EMS_D_WDI_007";
    public static final String WDI_008 = "EMS_D_WDI_008";
    public static final String WDI_009 = "EMS_D_WDI_009";
    public static final String WDI_010 = "EMS_D_WDI_010";
    public static final String WDI_011 = "EMS_D_WDI_011";
    public static final String WDI_012 = "EMS_D_WDI_012";
    public static final String WDI_014 = "EMS_D_WDI_014";
    public static final String WDI_015 = "EMS_D_WDI_015";
    public static final String WDI_013 = "EMS_D_WDI_013";
    public static final String WDI_002_MSG = "violate unique constraint, activation code already exists";
    public static final String WDI_003_MSG = "violate unique constraint, workstation code for this enrollment office already exists";
    public static final String WDI_004_MSG = "unable to to save workstation";
    public static final String WDI_006_MSG = "unable to to save workstationInfo";
    public static final String WDI_007_MSG = "unable to to save workstationPlugins";
    public static final String WDI_005_MSG = "unable to to find department";
    public static final String WDI_010_MSG = "workstation activation code is repetitive";

    // com.gam.nocr.ems.data.dao.impl.DispatchDAOImpl
    public static final String DSI_001 = "EMS_D_DSI_001";
    public static final String DSI_002 = "EMS_D_DSI_002";
    public static final String DSI_003 = "EMS_D_DSI_003";
    public static final String DSI_004 = "EMS_D_DSI_004";
    public static final String DSI_005 = "EMS_D_DSI_005";
    public static final String DSI_006 = "EMS_D_DSI_006";
    public static final String DSI_007 = "EMS_D_DSI_007";
    public static final String DSI_008 = "EMS_D_DSI_008";
    public static final String DSI_009 = "EMS_D_DSI_009";
    public static final String DSI_010 = "EMS_D_DSI_010";
    public static final String DSI_011 = "EMS_D_DSI_011";
    public static final String DSI_012 = "EMS_D_DSI_012";
    public static final String DSI_014 = "EMS_D_DSI_014";
    public static final String DSI_015 = "EMS_D_DSI_015";
    public static final String DSI_016 = "EMS_D_DSI_016";
    public static final String DSI_017 = "EMS_D_DSI_017";
    public static final String DSI_018 = "EMS_D_DSI_018";
    public static final String DSI_019 = "EMS_D_DSI_019";
    public static final String DSI_020 = "EMS_D_DSI_020";
    public static final String DSI_021 = "EMS_D_DSI_021";
    public static final String DSI_022 = "EMS_D_DSI_022";
    public static final String DSI_023 = "EMS_D_DSI_023";
    public static final String DSI_024 = "EMS_D_DSI_024";
    public static final String DSI_013 = "EMS_D_DSI_013";
    public static final String DSI_025 = "EMS_D_DSI_025";
    public static final String DSI_026 = "EMS_D_DSI_026";
    public static final String DSI_027 = "EMS_D_DSI_027";
    public static final String DSI_028 = "EMS_D_DSI_028";
    public static final String DSI_029 = "EMS_D_DSI_029";
    public static final String DSI_030 = "EMS_D_DSI_030";
    public static final String DSI_031 = "EMS_D_DSI_031";
    public static final String DSI_032 = "EMS_D_DSI_032";
    public static final String DSI_033 = "EMS_D_DSI_033";
    public static final String DSI_034 = "EMS_D_DSI_034";
    public static final String DSI_035 = "EMS_D_DSI_035";
    public static final String DSI_036 = "EMS_D_DSI_036";
    public static final String DSI_037 = "EMS_D_DSI_037";
    public static final String DSI_038 = "EMS_D_DSI_038";
    public static final String DSI_039 = "EMS_D_DSI_039";
    public static final String DSI_040 = "EMS_D_DSI_040";
    public static final String DSI_041 = "EMS_D_DSI_041";
    public static final String DSI_042 = "EMS_D_DSI_042";
    public static final String DSI_043 = "EMS_D_DSI_043";
    public static final String DSI_044 = "EMS_D_DSI_044";
    public static final String DSI_045 = "EMS_D_DSI_045";
    public static final String DSI_046 = "EMS_D_DSI_046";
    public static final String DSI_047 = "EMS_D_DSI_047";
    public static final String DSI_048 = "EMS_D_DSI_048";
    public static final String DSI_049 = "EMS_D_DSI_049";
    public static final String DSI_050 = "EMS_D_DSI_050";
    public static final String DSI_051 = "EMS_D_DSI_051";
    public static final String DSI_052 = "EMS_D_DSI_052";
    public static final String DSI_053 = "EMS_D_DSI_053";
    //By Adldoost for triggers
    public static final String DSI_054 = "EMS_D_DSI_054";
    public static final String DSI_055 = "EMS_D_DSI_055";
    public static final String DSI_056 = "EMS_D_DSI_056";
    public static final String DSI_057 = "EMS_D_DSI_057";
    public static final String DSI_058 = "EMS_D_DSI_058";
    public static final String DSI_059 = "EMS_D_DSI_059";
    public static final String DSI_060 = "EMS_D_DSI_060";
    public static final String DSI_061 = "EMS_D_DSI_061";
    public static final String DSI_062 = "EMS_D_DSI_062";
    public static final String DSI_063 = "EMS_D_DSI_063";
    public static final String DSI_064 = "EMS_D_DSI_064";
    public static final String DSI_065 = "EMS_D_DSI_065";
    public static final String DSI_066 = "EMS_D_DSI_066";
    public static final String DSI_067 = "EMS_D_DSI_067";
    public static final String DSI_068 = "EMS_D_DSI_068";
    public static final String DSI_069 = "EMS_D_DSI_069";
    public static final String DSI_070 = "EMS_D_DSI_070";
    public static final String DSI_071 = "EMS_D_DSI_071";
    public static final String DSI_072 = "EMS_D_DSI_072";
    public static final String DSI_073 = "EMS_D_DSI_073";
    public static final String DSI_074 = "EMS_D_DSI_074";
//    public static final String DSI_074 = "EMS_D_DSI_074";
    public static final String DSI_075 = "EMS_D_DSI_075";
    public static final String DSI_076 = "EMS_D_DSI_076";
    public static final String DSI_077 = "EMS_D_DSI_077";
    public static final String DSI_078 = "EMS_D_DSI_078";
    public static final String DSI_079 = "EMS_D_DSI_079";
    public static final String DSI_080 = "EMS_D_DSI_080";
    public static final String DSI_081 = "EMS_D_DSI_081";
    public static final String DSI_082 = "EMS_D_DSI_082";
    //Adldoost : messages for essential exceptions in triggers
    public static final String DSI_054_MSG = "Received cards count should not be larger than total count";
    public static final String DSI_056_MSG = "Received cards count should be larger than zero";
    public static final String DSI_063_MSG =  "Min date should not be null";
    public static final String DSI_070_MSG = "Can not find card request with specified cmsId";
    public static final String DSI_072_MSG = "Lost cards count should be larger than zero";
    public static final String DSI_073_MSG = "This situation should not be happened!";
    
    //
    public static final String DSI_001_MSG = "violate unique constraint, container id or container type or sender id already exists";
    public static final String DSI_002_MSG = "violate unique constraint, card serial number already exists";
    public static final String DSI_003_MSG = "unable to to update dispatchInfo";
    public static final String DSI_004_MSG = "unable to to update card";
    public static final String DSI_025_MSG = "violate unique constraint, Box CMS id already exists";
    public static final String DSI_026_MSG = "violate unique constraint, Batch CMS id already exists";
    public static final String DSI_027_MSG = "???";
    public static final String DSI_028_MSG = "unable to to save card container";
    public static final String DSI_032_MSG = "unable to to update batches";
    public static final String DSI_043_MSG = "The interval time for the received batches could not be found by using ProfileManager. So the default value was used instead.";
    public static final String DSI_044_MSG = "The interval time for the missed batches could not be found by using ProfileManager. So the default value was used instead.";
    public static final String DSI_045_MSG = "The interval time for the missed boxes could not be found by using ProfileManager. So the default value was used instead.";
    public static final String DSI_046_MSG = "The interval time for the missed cards could not be found by using ProfileManager. So the default value was used instead.";
    public static final String DSI_080_MSG = "unable to to update batch";
    public static final String DSI_081_MSG = "violate unique constraint, Batch CMS id already exists";
    public static final String DSI_082_MSG = "violate unique constraint, Postal Tracking Code already exists";


    // com.gam.nocr.ems.data.dao.impl.CardRequestDAOImpl
    public static final String CDI_001 = "EMS_D_CDI_001";
    public static final String CDI_002 = "EMS_D_CDI_002";
    public static final String CDI_003 = "EMS_D_CDI_003";
    public static final String CDI_004 = "EMS_D_CDI_004";
    public static final String CDI_005 = "EMS_D_CDI_005";
    public static final String CDI_006 = "EMS_D_CDI_006";
    public static final String CDI_007 = "EMS_D_CDI_007";
    public static final String CDI_008 = "EMS_D_CDI_008";
    public static final String CDI_009 = "EMS_D_CDI_009";
    public static final String CDI_010 = "EMS_D_CDI_010";
    public static final String CDI_011 = "EMS_D_CDI_011";
    public static final String CDI_012 = "EMS_D_CDI_012";
    public static final String CDI_013 = "EMS_D_CDI_013";
    public static final String CDI_014 = "EMS_D_CDI_014";
    public static final String CDI_015 = "EMS_D_CDI_015";
    public static final String CDI_016 = "EMS_D_CDI_016";
    public static final String CDI_017 = "EMS_D_CDI_017";
    public static final String CDI_018 = "EMS_D_CDI_018";
    public static final String CDI_019 = "EMS_D_CDI_019";
    public static final String CDI_020 = "EMS_D_CDI_020";
    public static final String CDI_021 = "EMS_D_CDI_021";
    public static final String CDI_022 = "EMS_D_CDI_022";
    public static final String CDI_023 = "EMS_D_CDI_023";
    public static final String CDI_024 = "EMS_D_CDI_024";
    public static final String CDI_025 = "EMS_D_CDI_025";
    public static final String CDI_026 = "EMS_D_CDI_026";
    public static final String CDI_027 = "EMS_D_CDI_027";
    public static final String CDI_028 = "EMS_D_CDI_028";
    public static final String CDI_029 = "EMS_D_CDI_029";
    public static final String CDI_030 = "EMS_D_CDI_030";
    public static final String CDI_031 = "EMS_D_CDI_031";
    public static final String CDI_032 = "EMS_D_CDI_032";
    public static final String CDI_033 = "EMS_D_CDI_033";
    public static final String CDI_034 = "EMS_D_CDI_034";
    public static final String CDI_035 = "EMS_D_CDI_035";
    public static final String CDI_036 = "EMS_D_CDI_036";
    public static final String CDI_037 = "EMS_D_CDI_037";
    public static final String CDI_038 = "EMS_D_CDI_038";
    public static final String CDI_039 = "EMS_D_CDI_039";
    public static final String CDI_040 = "EMS_D_CDI_040";
    public static final String CDI_041 = "EMS_D_CDI_041";
    public static final String CDI_042 = "EMS_D_CDI_042";
    public static final String CDI_043 = "EMS_D_CDI_043";
    public static final String CDI_044 = "EMS_D_CDI_044";
    public static final String CDI_045 = "EMS_D_CDI_045";
    public static final String CDI_046 = "EMS_D_CDI_046";
    public static final String CDI_047 = "EMS_D_CDI_047";
    public static final String CDI_048 = "EMS_D_CDI_048";
    public static final String CDI_049 = "EMS_D_CDI_049";
    public static final String CDI_050 = "EMS_D_CDI_050";
    public static final String CDI_051 = "EMS_D_CDI_051";
    public static final String CDI_052 = "EMS_D_CDI_052";
    public static final String CDI_053 = "EMS_D_CDI_053";
    public static final String CDI_054 = "EMS_D_CDI_054";
    public static final String CDI_055 = "EMS_D_CDI_055";
    public static final String CDI_056 = "EMS_D_CDI_056";
    public static final String CDI_057 = "EMS_D_CDI_057";
    public static final String CDI_058 = "EMS_D_CDI_058";
    public static final String CDI_059 = "EMS_D_CDI_059";
    public static final String CDI_060 = "EMS_D_CDI_060";
    public static final String CDI_061 = "EMS_D_CDI_061";
    public static final String CDI_062 = "EMS_D_CDI_062";
    public static final String CDI_063 = "EMS_D_CDI_063";
    public static final String CDI_064 = "EMS_D_CDI_064";
    public static final String CDI_065 = "EMS_D_CDI_065";
    public static final String CDI_066 = "EMS_D_CDI_066";
    public static final String CDI_067 = "EMS_D_CDI_067";
    public static final String CDI_068 = "EMS_D_CDI_068";
    public static final String CDI_069 = "EMS_D_CDI_069";
    public static final String CDI_070 = "EMS_D_CDI_070";
    public static final String CDI_071 = "EMS_D_CDI_071";
    public static final String CDI_072 = "EMS_D_CDI_072";
    public static final String CDI_073 = "EMS_D_CDI_073";
    public static final String CDI_074 = "EMS_D_CDI_074";
    public static final String CDI_075 = "EMS_D_CDI_075";
    public static final String CDI_076 = "EMS_D_CDI_076";
	public static final String CDI_077 = "EMS_D_CDI_077";
    public static final String CDI_078 = "EMS_D_CDI_078";
    public static final String CDI_079 = "EMS_D_CDI_079";
    public static final String CDI_080 = "EMS_D_CDI_080";
    public static final String CDI_081 = "EMS_D_CDI_081";
    public static final String CDI_082 = "EMS_D_CDI_082";
    public static final String CDI_083 = "EMS_D_CDI_083";
    public static final String CDI_084 = "EMS_D_CDI_084";
    public static final String CDI_085 = "EMS_D_CDI_085";
    public static final String CDI_086 = "EMS_D_CDI_086";
    public static final String CDI_087 = "EMS_D_CDI_087";
    public static final String CDI_088 = "EMS_D_CDI_088";
    public static final String CDI_100 = "EMS_D_CDI_100";
    public static final String CDI_089 = "EMS_D_CDI_089";
    public static final String CDI_090 = "EMS_D_CDI_090";
    public static final String CDI_091 = "EMS_D_CDI_091";
    public static final String CDI_092 = "EMS_D_CDI_092";
    public static final String CDI_093 = "EMS_D_CDI_093";
    public static final String CDI_094 = "EMS_D_CDI_094";
    public static final String CDI_095 = "EMS_D_CDI_095";
    public static final String CDI_096 = "EMS_D_CDI_096";
    public static final String CDI_097 = "EMS_D_CDI_097";
    public static final String CDI_098 = "EMS_D_CDI_098";
    public static final String CDI_099 = "EMS_D_CDI_099";

    public static final String CDI_001_MSG = "Unable to create Card Request";
    public static final String CDI_002_MSG = "Integrity constraint violated, card with id {} not found";
    public static final String CDI_003_MSG = "Integrity constraint violated, citizen with id {} not found";
    public static final String CDI_004_MSG = "Integrity constraint violated, enrollment office with id {} not found";
    public static final String CDI_006_MSG = "Could not execute find operation for card request id {}";
    public static final String CDI_007_MSG = "The required CardRequestTO instance with the given id could not be found.";
    public static final String CDI_009_MSG = "Foreign key constraint violated, at least one card request history refers to card request id {}";
    public static final String CDI_010_MSG = "Unable to delete card request for request id {}";
    public static final String CDI_015_MSG = "Unable to count card request with id ";
    public static final String CDI_016_MSG = "Foreign key constraint violated, at least one reservation refers to card request id {}";
    public static final String CDI_022_MSG = "Integrity constraint violated, card with id {} not found";
    public static final String CDI_023_MSG = "Integrity constraint violated, citizen with id {} not found";
    public static final String CDI_024_MSG = "Integrity constraint violated, enrollment office with id {} not found";
    public static final String CDI_025_MSG = "Unable to create Card Request";
    public static final String CDI_026_MSG = "The required count config value for the systemId '{}' which belongs to cardRequests query count, could not be fetched by profileManager. So The default value was used instead.";
    public static final String CDI_028_MSG = "The profile key '{}' is not valid.";
    public static final String CDI_029_MSG = "Exception was happened on getting the profileManager.";
    public static final String CDI_030_MSG = "The required hour based time interval config value for the systemId '{}' which belongs to cardRequests query count, could not be fetched by profileManager. So The default value was used instead.";
    public static final String CDI_036_MSG = "The format of the configured time interval is wrong. This config should be set such as these instances: '[upper than 1]d for day', '[1-24]h or [1-24] for hour', and '[1-59]m for minute'";
    public static final String CDI_042_MSG = "Integrity constraint violated, state transition is not permitted";
    public static final String CDI_052_MSG = "The required maxResult value could not be fetched by profileManager. So The default value was used instead.";
    public static final String CDI_058_MSG = "Integrity constraint violated, requested action transition is not permitted.";
    public static final String CDI_089_MSG = "Can not referesh entity manager";
    public static final String CDI_090_MSG = "Can not update deliver Office id in cardRequest for disableling office deliver feture";

    // com.gam.nocr.ems.data.dao.impl.DocTypeDAOImpl
    public static final String DCT_001 = "EMS_D_DCT_001";
    public static final String DCT_002 = "EMS_D_DCT_002";
    public static final String DCT_003 = "EMS_D_DCT_003";
    public static final String DCT_004 = "EMS_D_DCT_004";
    public static final String DCT_005 = "EMS_D_DCT_005";
    public static final String DCT_006 = "EMS_D_DCT_006";
    public static final String DCT_007 = "EMS_D_DCT_007";
    public static final String DCT_008 = "EMS_D_DCT_008";
    public static final String DCT_009 = "EMS_D_DCT_009";
    public static final String DCT_010 = "EMS_D_DCT_010";
    public static final String DCT_011 = "EMS_D_DCT_011";
    public static final String DCT_012 = "EMS_D_DCT_012";
    public static final String DCT_013 = "EMS_D_DCT_013";
    public static final String DCT_014 = "EMS_D_DCT_014";
    public static final String DCT_001_MSG = "Foreign key constraint violated, at least one document refers to doc type id {}";
    public static final String DCT_002_MSG = "Foreign key constraint violated, at least one service document refers to doc type id {}";
    public static final String DCT_003_MSG = "Unable to delete doc type id {}";
    public static final String DCT_004_MSG = "The operation to find all document types failed";
    public static final String DCT_005_MSG = "Document type or document type id cannot be null";
    public static final String DCT_006_MSG = "Foreign key constraint violated, at least one document refers to this doc type";
    public static final String DCT_007_MSG = "Foreign key constraint violated, at least one service document refers to this doc type";
    public static final String DCT_008_MSG = "Unable to delete doc types";
    public static final String DCT_010_MSG = "Unique constraint violation, document type with this name already exists";
    public static final String DCT_012_MSG = "Unique constraint violation, document type with this name already exists";
    public static final String DCT_014_MSG = "Document type ids not given for delete";   
    

    // com.gam.nocr.ems.data.dao.impl.LocationDAOImpl
    public static final String LDI_001 = "EMS_D_LDI_001";
    public static final String LDI_002 = "EMS_D_LDI_002";
    public static final String LDI_003 = "EMS_D_LDI_003";
    public static final String LDI_004 = "EMS_D_LDI_004";
    
    
    // com.gam.nocr.ems.data.dao.impl.CardRequestBlobsDAOImpl
    public static final String CRB_001 = "EMS_D_CRB_001";
    
    // com.gam.nocr.ems.data.dao.impl.ServiceDocumentTypeDAOImpl
    public static final String STI_001 = "EMS_D_STI_001";
    public static final String STI_002 = "EMS_D_STI_002";
    public static final String STI_003 = "EMS_D_STI_003";
    public static final String STI_004 = "EMS_D_STI_004";
    public static final String STI_005 = "EMS_D_STI_005";
    public static final String STI_006 = "EMS_D_STI_006";
    public static final String STI_007 = "EMS_D_STI_007";
    public static final String STI_001_MSG = "Unable to execute find operation for service document type by service id {}";
    public static final String STI_002_MSG = "Integrity constraint violated, document type id {} does not exist";
    public static final String STI_004_MSG = "No document type ids given to delete service document type";
    public static final String STI_006_MSG = "Cannot find service document type for document type, where document type is null";
    public static final String STI_007_MSG = "Cannot find service document type for document type id {}";

    // com.gam.nocr.ems.data.dao.impl.EnrollmentOfficeDAOImpl
    public static final String ENI_001 = "EMS_D_ENI_001";
    public static final String ENI_002 = "EMS_D_ENI_002";
    public static final String ENI_003 = "EMS_D_ENI_003";
    public static final String ENI_004 = "EMS_D_ENI_004";
    public static final String ENI_005 = "EMS_D_ENI_005";
    public static final String ENI_006 = "EMS_D_ENI_006";
    public static final String ENI_007 = "EMS_D_ENI_007";
    public static final String ENI_008 = "EMS_D_ENI_008";
    public static final String ENI_009 = "EMS_D_ENI_009";
    public static final String ENI_010 = "EMS_D_ENI_010";
    public static final String ENI_011 = "EMS_D_ENI_011";
    public static final String ENI_012 = "EMS_D_ENI_012";
    public static final String ENI_013 = "EMS_D_ENI_013";
    public static final String ENI_014 = "EMS_D_ENI_014";
    // com.gam.nocr.ems.data.dao.impl.Estelam2FailureLogDAOImpl
    public static final String EFI_001 = "EMS_D_EFI_001";


    // com.gam.nocr.ems.data.dao.impl.BlackListDAOImpl
    public static final String BLI_001 = "EMS_D_BLI_001";
    public static final String BLI_002 = "EMS_D_BLI_002";
    public static final String BLI_003 = "EMS_D_BLI_003";
    public static final String BLI_004 = "EMS_D_BLI_004";
    public static final String BLI_005 = "EMS_D_BLI_005";
    public static final String BLI_006 = "EMS_D_BLI_006";
    public static final String BLI_007 = "EMS_D_BLI_007";
    public static final String BLI_008 = "EMS_D_BLI_008";
    public static final String BLI_009 = "EMS_D_BLI_009";
    public static final String BLI_004_MSG = "Unable to find all black list items";
    public static final String BLI_005_MSG = "Unable to delete black list items";
    public static final String BLI_006_MSG = "Unique constraint violated, national id already exists";
    public static final String BLI_007_MSG = "Unique constraint violated, national id already exists";

    // com.gam.nocr.ems.data.dao.impl.RatingInfoDAOImpl
    public static final String RTI_001 = "EMS_D_RTI_001";
    public static final String RTI_002 = "EMS_D_RTI_002";
    public static final String RTI_003 = "EMS_D_RTI_003";
    public static final String RTI_004 = "EMS_D_RTI_004";
    public static final String RTI_005 = "EMS_D_RTI_005";
    public static final String RTI_006 = "EMS_D_RTI_006";
    public static final String RTI_007 = "EMS_D_RTI_007";
    public static final String RTI_008 = "EMS_D_RTI_008";
    public static final String RTI_009 = "EMS_D_RTI_009";
    public static final String RTI_010 = "EMS_D_RTI_010";
    public static final String RTI_011 = "EMS_D_RTI_011";
    public static final String RTI_012 = "EMS_D_RTI_012";
    public static final String RTI_013 = "EMS_D_RTI_013";
    public static final String RTI_014 = "EMS_D_RTI_014";
    public static final String RTI_015 = "EMS_D_RTI_015";
    public static final String RTI_001_MSG = "The operation to find all Rating Info TOs failed";
    public static final String RTI_002_MSG = "Could not delete all rating infos";
    public static final String RTI_003_MSG = "Rating info or rating info id is null";
    public static final String RTI_004_MSG = "Foreign key constraint violated, at least one enrollment office refers to rating info id {}";
    public static final String RTI_005_MSG = "Foreign key constraint violated, at least one enrollment office refers to rating info";
    public static final String RTI_006_MSG = "Could not delete all rating infos";
    public static final String RTI_007_MSG = "Unable to execute find operation";
    public static final String RTI_009_MSG = "Unique constraint violated, rating info class has already been used";
    public static final String RTI_010_MSG = "Unique constraint violated, rating info size has already been used";
    public static final String RTI_012_MSG = "Unique constraint violated, rating info class has already been used";
    public static final String RTI_013_MSG = "Unique constraint violated, rating info size has already been used";

    // com.gam.nocr.ems.data.dao.impl.CitizenDAOImpl
    public static final String CTI_001 = "EMS_D_CTI_001";
    public static final String CTI_002 = "EMS_D_CTI_002";
    public static final String CTI_003 = "EMS_D_CTI_003";
    public static final String CTI_004 = "EMS_D_CTI_004";
    public static final String CTI_005 = "EMS_D_CTI_005";
    public static final String CTI_006 = "EMS_D_CTI_006";
    public static final String CTI_007 = "EMS_D_CTI_007";
    public static final String CTI_008 = "EMS_D_CTI_008";
    public static final String CTI_009 = "EMS_D_CTI_009";
    public static final String CTI_010 = "EMS_D_CTI_010";
    public static final String CTI_011 = "EMS_D_CTI_011";
    public static final String CTI_012 = "EMS_D_CTI_012";
    public static final String CTI_013 = "EMS_D_CTI_013";
    public static final String CTI_014 = "EMS_D_CTI_014";
    public static final String CTI_015 = "EMS_D_CTI_015";
    public static final String CTI_016 = "EMS_D_CTI_016";
    public static final String CTI_017 = "EMS_D_CTI_017";
    public static final String CTI_018 = "EMS_D_CTI_018";
    public static final String CTI_019 = "EMS_D_CTI_019";
    public static final String CTI_020 = "EMS_D_CTI_020";
    public static final String CTI_021 = "EMS_D_CTI_021";
    public static final String CTI_022 = "EMS_D_CTI_022";
    public static final String CTI_001_MSG = "Unable to create citizen";
    public static final String CTI_002_MSG = "Unique constraint violated, citizen with this national ID already exists";
    public static final String CTI_003_MSG = "Integrity constraint violated, citizen with id {} not found";
    public static final String CTI_004_MSG = "Integrity constraint violated, birth province with id {} not found";
    public static final String CTI_005_MSG = "Integrity constraint violated, living city with id {} not found";
    public static final String CTI_006_MSG = "Integrity constraint violated, living province with id {} not found";
    public static final String CTI_007_MSG = "Integrity constraint violated, religion with id {} not found";
    public static final String CTI_008_MSG = "Integrity constraint violated, citizen info not found for spouse";
    public static final String CTI_009_MSG = "Integrity constraint violated, marital status id not found for spouse";
    public static final String CTI_010_MSG = "Integrity constraint violated, citizen info not found for child";
    public static final String CTI_011_MSG = "Integrity constraint violated, citizen info not found for biometric";
    public static final String CTI_012_MSG = "Integrity constraint violated, citizen info not found for document";
    public static final String CTI_013_MSG = "Integrity constraint violated, document type not found for document";
    public static final String CTI_015_MSG = "Could not execute find operation for citizen with national id '{}'";
    public static final String CTI_016_MSG = "Problem when trying to load citizen for request id {}, data is invalid";
    public static final String CTI_017_MSG = "Unable to execute find operation for citizen with request id {}";
    public static final String CTI_018_MSG = "Problem when trying to load citizen for request id {}, data is invalid";

    // com.gam.nocr.ems.data.dao.impl.CitizenInfoDAOImpl
    public static final String CZI_001 = "EMS_D_CZI_001";
    public static final String CZI_002 = "EMS_D_CZI_002";
    public static final String CZI_003 = "EMS_D_CZI_003";
    public static final String CZI_004 = "EMS_D_CZI_004";
    public static final String CZI_005 = "EMS_D_CZI_005";
    public static final String CZI_006 = "EMS_D_CZI_006";
    public static final String CZI_007 = "EMS_D_CZI_007";
    public static final String CZI_008 = "EMS_D_CZI_008";
    public static final String CZI_009 = "EMS_D_CZI_009";
    public static final String CZI_010 = "EMS_D_CZI_010";
    public static final String CZI_011 = "EMS_D_CZI_011";
    public static final String CZI_012 = "EMS_D_CZI_012";
    public static final String CZI_013 = "EMS_D_CZI_013";
    public static final String CZI_014 = "EMS_D_CZI_014";
    public static final String CZI_015 = "EMS_D_CZI_015";
    public static final String CZI_016 = "EMS_D_CZI_016";
    public static final String CZI_017 = "EMS_D_CZI_017";
    public static final String CZI_018 = "EMS_D_CZI_018";
    public static final String CZI_001_MSG = "Unable to create citizen info";
    public static final String CZI_002_MSG = "Integrity constraint violated, citizen with id {} not found";
    public static final String CZI_003_MSG = "Integrity constraint violated, birth province with id {} not found";
    public static final String CZI_004_MSG = "Integrity constraint violated, living city with id {} not found";
    public static final String CZI_005_MSG = "Integrity constraint violated, living province with id {} not found";
    public static final String CZI_006_MSG = "Integrity constraint violated, religion with id {} not found";
    public static final String CZI_008_MSG = "Integrity constraint violated, citizen info not found for spouse";
    public static final String CZI_009_MSG = "Integrity constraint violated, marital status id not found for spouse";
    public static final String CZI_010_MSG = "Integrity constraint violated, citizen info not found for child";
    public static final String CZI_011_MSG = "Integrity constraint violated, citizen info not found for biometric";
    public static final String CZI_012_MSG = "Integrity constraint violated, citizen info not found for document";
    public static final String CZI_013_MSG = "Integrity constraint violated, document type not found for document";
    public static final String CZI_015_MSG = "Unique constraint violated, spouse with this national ID already exists";
    public static final String CZI_016_MSG = "Unique constraint violated, child with this national ID already exists";

    // com.gam.nocr.ems.data.dao.CardRequestHistoryDAOImpl
    public static final String CRH_001 = "EMS_D_CRH_001";
    public static final String CRH_002 = "EMS_D_CRH_002";
    public static final String CRH_003 = "EMS_D_CRH_003";
    public static final String CRH_004 = "EMS_D_CRH_004";
    public static final String CRH_005 = "EMS_D_CRH_005";
    public static final String CRH_006 = "EMS_D_CRH_006";
    public static final String CRH_007 = "EMS_D_CRH_007";
    public static final String CRH_008 = "EMS_D_CRH_008";
    public static final String CRH_009 = "EMS_D_CRH_009";
    public static final String CRH_010 = "EMS_D_CRH_010";
    public static final String CRH_011 = "EMS_D_CRH_011";
    public static final String CRH_012 = "EMS_D_CRH_012";
    public static final String CRH_013 = "EMS_D_CRH_013";
    public static final String CRH_014 = "EMS_D_CRH_014";
    public static final String CRH_015 = "EMS_D_CRH_015";
    public static final String CRH_016 = "EMS_D_CRH_016";
    public static final String CRH_017 = "EMS_D_CRH_017";
    public static final String CRH_018 = "EMS_D_CRH_018";
    public static final String CRH_019 = "EMS_D_CRH_019";
    public static final String CRH_020 = "EMS_D_CRH_020";
    public static final String CRH_021 = "EMS_D_CRH_021";
    public static final String CRH_022 = "EMS_D_CRH_022";
    public static final String CRH_006_MSG = "Unable to execute find operation for card request history by system id {}";
    public static final String CRH_007_MSG = "System id cannot be null";
    public static final String CRH_009_MSG = "The required tryCounter value could not be fetched by profileManager. So The default value '{}' were used instead.";

    public static final String CRH_017_MSG = "Card request history primary key violated.";
    public static final String CRH_018_MSG = "Volatile foreign key constraint. There is no card request with specified id";
    public static final String CRH_019_MSG = "Integrity constraint violated, system id is not valid";
    public static final String CRH_020_MSG = "Integrity constraint violated, card request history action is not valid";

    // com.gam.nocr.ems.data.dao.PersonTokenDAOImpl
    public static final String PTI_001 = "EMS_D_PTI_001";
    public static final String PTI_002 = "EMS_D_PTI_002";
    public static final String PTI_003 = "EMS_D_PTI_003";
    public static final String PTI_004 = "EMS_D_PTI_004";
    public static final String PTI_005 = "EMS_D_PTI_005";
    public static final String PTI_006 = "EMS_D_PTI_006";
    public static final String PTI_007 = "EMS_D_PTI_007";
    public static final String PTI_008 = "EMS_D_PTI_008";
    public static final String PTI_009 = "EMS_D_PTI_009";
    public static final String PTI_010 = "EMS_D_PTI_010";
    public static final String PTI_011 = "EMS_D_PTI_011";
    public static final String PTI_012 = "EMS_D_PTI_012";
    public static final String PTI_013 = "EMS_D_PTI_013";
    public static final String PTI_014 = "EMS_D_PTI_014";
    public static final String PTI_015 = "EMS_D_PTI_015";
    public static final String PTI_016 = "EMS_D_PTI_016";
    public static final String PTI_017 = "EMS_D_PTI_017";
    public static final String PTI_018 = "EMS_D_PTI_018";
    public static final String PTI_019 = "EMS_D_PTI_019";
    public static final String PTI_020 = "EMS_D_PTI_020";
    public static final String PTI_021 = "EMS_D_PTI_021";
    public static final String PTI_022 = "EMS_D_PTI_022";
    public static final String PTI_023 = "EMS_D_PTI_023";
    public static final String PTI_024 = "EMS_D_PTI_024";
    public static final String PTI_025 = "EMS_D_PTI_025";
    public static final String PTI_026 = "EMS_D_PTI_026";
    public static final String PTI_027 = "EMS_D_PTI_027";
    public static final String PTI_028 = "EMS_D_PTI_028";
    public static final String PTI_029 = "EMS_D_PTI_029";
    public static final String PTI_030 = "EMS_D_PTI_030";
    public static final String PTI_031 = "EMS_D_PTI_031";
    public static final String PTI_032 = "EMS_D_PTI_032";
    public static final String PTI_033 = "EMS_D_PTI_033";
    public static final String PTI_034 = "EMS_D_PTI_034";
    public static final String PTI_035 = "EMS_D_PTI_035";
    public static final String PTI_036 = "EMS_D_PTI_036";
    public static final String PTI_037 = "EMS_D_PTI_037";
    public static final String PTI_005_MSG = "The required PersonTokenTo instance with the given id could not be found.";
    public static final String PTI_006_MSG = "The required list of type PersonTokenTo with the specified state could not be found.";
    public static final String PTI_007_MSG = "The required delivered PersonTokenTo instance with the specified personId and type could not be found.";
    public static final String PTI_011_MSG = "violate unique constraint, combination of person, token and state should be unique";
    public static final String PTI_020_MSG = "this person has more than one person token with delivered status and this type";
    public static final String PTI_023_MSG = "invalid request, ids are null";
    public static final String PTI_024_MSG = "invalid request, illegal change state from {} to approve renewall";
    public static final String PTI_026_MSG = "invalid request, ids are null";
    public static final String PTI_027_MSG = "invalid request, illegal change state from {} to ems reject";

    // com.gam.nocr.ems.data.dao.NetworkTokenDAOImpl
    public static final String NTI_001 = "EMS_D_NTI_001";
    public static final String NTI_002 = "EMS_D_NTI_002";
    public static final String NTI_003 = "EMS_D_NTI_003";
    public static final String NTI_004 = "EMS_D_NTI_004";
    public static final String NTI_005 = "EMS_D_NTI_005";
    public static final String NTI_006 = "EMS_D_NTI_006";
    public static final String NTI_007 = "EMS_D_NTI_007";
    public static final String NTI_008 = "EMS_D_NTI_008";
    public static final String NTI_009 = "EMS_D_NTI_009";
    public static final String NTI_010 = "EMS_D_NTI_010";
    public static final String NTI_011 = "EMS_D_NTI_011";
    public static final String NTI_012 = "EMS_D_NTI_012";
    public static final String NTI_013 = "EMS_D_NTI_013";
    public static final String NTI_014 = "EMS_D_NTI_014";
    public static final String NTI_015 = "EMS_D_NTI_015";
    public static final String NTI_016 = "EMS_D_NTI_016";
    public static final String NTI_001_MSG = "violate unique constraint, this enrollment office already has a token in this state";
    public static final String NTI_005_MSG = "The required NetworkTokenTo instance with the given id could not be found.";
    public static final String NTI_006_MSG = "The required list of type NetworkTokenTo with the specified state could not be found.";
    public static final String NTI_007_MSG = "The required delivered NetworkTokenTo instance with the specified enrollmentOfficeId could not be found.";

    // com.gam.nocr.ems.data.dao.CertificateDAOImpl
    public static final String CRT_001 = "EMS_D_CRT_001";
    public static final String CRT_002 = "EMS_D_CRT_002";
    public static final String CRT_003 = "EMS_D_CRT_003";
    public static final String CRT_004 = "EMS_D_CRT_004";
    public static final String CRT_005 = "EMS_D_CRT_005";
    public static final String CRT_006 = "EMS_D_CRT_006";
    public static final String CRT_007 = "EMS_D_CRT_007";
    public static final String CRT_005_MSG = "The required CertificateTo instance with the given id could not be found.";
    public static final String CRT_007_MSG = "The required CertificateTo instance with the given usage could not be found.";

    // com.gam.nocr.ems.data.dao.CardDAOImpl
    public static final String CAI_001 = "EMS_D_CAI_001";
    public static final String CAI_002 = "EMS_D_CAI_002";
    public static final String CAI_003 = "EMS_D_CAI_003";
    public static final String CAI_004 = "EMS_D_CAI_004";
    public static final String CAI_005 = "EMS_D_CAI_005";
    public static final String CAI_006 = "EMS_D_CAI_006";
    public static final String CAI_007 = "EMS_D_CAI_007";
    public static final String CAI_008 = "EMS_D_CAI_008";
    public static final String CAI_009 = "EMS_D_CAI_009";
    public static final String CAI_010 = "EMS_D_CAI_010";
    public static final String CAI_011 = "EMS_D_CAI_011";
    public static final String CAI_012 = "EMS_D_CAI_012";
    public static final String CAI_013 = "EMS_D_CAI_013";
    public static final String CAI_014 = "EMS_D_CAI_014";
    public static final String CAI_015 = "EMS_D_CAI_015";
    public static final String CAI_016 = "EMS_D_CAI_016";
    public static final String CAI_017 = "EMS_D_CAI_017";
    public static final String CAI_002_MSG = "violate unique constraint, Card serial number already exists";
    public static final String CAI_005_MSG = "unique constraint violated, card serial number already exists";

    // com.gam.nocr.ems.data.dao.impl.BatchDAOImpl
    public static final String BAI_001 = "EMS_D_BAI_001";
    public static final String BAI_001_MSG = "Unable to find all batches";

    // com.gam.nocr.ems.data.dao.impl.BiometricDAOImpl
    public static final String BDI_001 = "EMS_D_BDI_001";
    public static final String BDI_002 = "EMS_D_BDI_002";
    public static final String BDI_003 = "EMS_D_BDI_003";
    public static final String BDI_004 = "EMS_D_BDI_004";
    public static final String BDI_005 = "EMS_D_BDI_005";
    public static final String BDI_006 = "EMS_D_BDI_006";
    public static final String BDI_007 = "EMS_D_BDI_007";
    public static final String BDI_008 = "EMS_D_BDI_008";
    public static final String BDI_009 = "EMS_D_BDI_009";
    public static final String BDI_001_MSG = "Unable to create Biometric";
    public static final String BDI_002_MSG = "Integrity constraint violated, citizen info with id {} not found";
    public static final String BDI_003_MSG = "Unable to remove Biometric with request id {} and type {}";
    public static final String BDI_004_MSG = "Unable to execute find operation for biometric by request id and type";
    public static final String BDI_005_MSG = "Unable to remove finger biometric data which belong to the citizen with id {}.";
    public static final String BDI_006_MSG = "unique constraint violated. current biometric type already exists for this citizen. citizen id is : ";
    public static final String BDI_007_MSG = "Unable to remove face biometric data which belong to the citizen with id {}.";
    
    //com.gam.nocr.ems.data.dao.BiometricInfoDAOImpl
    public static final String BII_001 = "EMS_D_BII_001";
    public static final String BII_002 = "EMS_D_BII_002";
    public static final String BII_003 = "EMS_D_BII_003";
    public static final String BII_001_MSG = "Unable to remove Biometric Info with request id {}";
    public static final String BII_003_MSG = "Unable to find biometricInfo data with given nid.";


    // com.gam.nocr.ems.data.dao.DocumentDAOImpl
    public static final String DCI_001 = "EMS_D_DCI_001";
    public static final String DCI_002 = "EMS_D_DCI_002";
    public static final String DCI_003 = "EMS_D_DCI_003";
    public static final String DCI_004 = "EMS_D_DCI_004";
    public static final String DCI_005 = "EMS_D_DCI_005";
    public static final String DCI_006 = "EMS_D_DCI_006";
    public static final String DCI_007 = "EMS_D_DCI_007";
    public static final String DCI_001_MSG = "Unable to create document";
    public static final String DCI_002_MSG = "Unable to delete document for request id {}";
    public static final String DCI_003_MSG = "Integrity constraint violated, citizen info with id {} not found";
    public static final String DCI_004_MSG = "Integrity constraint violated, document type with id {} not found";
    public static final String DCI_005_MSG = "Unable to execute find operation for document by document type";
    public static final String DCI_006_MSG = "unique constraint violated. current document type already exists for this citizen. citizen id is : ";

    // com.gam.nocr.ems.data.dao.ReservationDAOImpl
    public static final String RSI_001 = "EMS_D_RSI_001";
    public static final String RSI_002 = "EMS_D_RSI_002";
    public static final String RSI_003 = "EMS_D_RSI_003";
    public static final String RSI_004 = "EMS_D_RSI_004";
    public static final String RSI_005 = "EMS_D_RSI_005";
    public static final String RSI_006 = "EMS_D_RSI_006";
    public static final String RSI_007 = "EMS_D_RSI_007";
    public static final String RSI_008 = "EMS_D_RSI_008";
    public static final String RSI_009 = "EMS_D_RSI_009";
    public static final String RSI_010 = "EMS_D_RSI_010";
    public static final String RSI_011 = "EMS_D_RSI_011";
    public static final String RSI_012 = "EMS_D_RSI_012";
    public static final String RSI_002_MSG = "Volatile unique constraint. The portal reservation with id '{}' already exists.";
    public static final String RSI_006_MSG = "Volatile foreign key constraint. The Enrollment office has been assigned to this record in advance.";
    public static final String RSI_007_MSG = "Volatile foreign key constraint. The card request has been assigned to this record in advance.";
    public static final String RSI_009_MSG = "Integrity constraint violated, Enrollment office with id {} not found";

    // com.gam.nocr.ems.data.dao.BusinessLogDAOImpl
    public static final String BZI_001 = "EMS_D_BZI_001";
    public static final String BZI_002 = "EMS_D_BZI_002";
    public static final String BZI_003 = "EMS_D_BZI_003";
    public static final String BZI_004 = "EMS_D_BZI_004";
    public static final String BZI_005 = "EMS_D_BZI_005";
    public static final String BZI_006 = "EMS_D_BZI_006";
    public static final String BZI_007 = "EMS_D_BZI_007";
    public static final String BZI_008 = "EMS_D_BZI_008";
    public static final String BZI_009 = "EMS_D_BZI_009";
    public static final String BZI_010 = "EMS_D_BZI_010";
    public static final String BZI_011 = "EMS_D_BZI_011";
    public static final String BZI_001_MSG = "BusinessLog primary key violated. check sequence nextval to solve the problem.";
    public static final String BZI_002_MSG = "BusinessLog could not be inserted.";
    public static final String BZI_003_MSG = "Cannot create thumbprint out of the digest.";
    public static final String BZI_004_MSG = "Cannot verify message digest for BusinessLog";
    public static final String BZI_005_MSG = "Error accessing profile manager";
    public static final String BZI_011_MSG = "Cannot find the requested certificate for businessLog record with id {}";

    // com.gam.nocr.ems.data.dao.ReportRequestDAOImpl
    public static final String RRI_001 = "EMS_D_RRI_001";
    public static final String RRI_002 = "EMS_D_RRI_002";
    public static final String RRI_003 = "EMS_D_RRI_003";
    public static final String RRI_004 = "EMS_D_RRI_004";
    public static final String RRI_005 = "EMS_D_RRI_005";
    public static final String RRI_006 = "EMS_D_RRI_006";
    public static final String RRI_007 = "EMS_D_RRI_007";
    public static final String RRI_008 = "EMS_D_RRI_008";
    public static final String RRI_009 = "EMS_D_RRI_009";
    public static final String RRI_010 = "EMS_D_RRI_010";

    // com.gam.nocr.ems.data.dao.ReportFileDAOImpl
    public static final String RPF_001 = "EMS_D_RPF_001";
    public static final String RPF_002 = "EMS_D_RPF_002";

    // com.gam.nocr.ems.data.dao.ReportDAOImpl
    public static final String RPT_001 = "EMS_D_RPT_001";

    // com.gam.nocr.ems.data.dao.ReligionDAOImpl
    public static final String RLG_001 = "EMS_D_RLG_001";
    
 // com.gam.nocr.ems.data.dao.HolidayDAOImpl
    public static final String HDI_001 = "EMS_D_HOL_001";
    public static final String HDI_001_MSG = "violate unique constraint, holidayDate already exists";
    
    // com.gam.nocr.ems.data.dao.impl.MessageDAOImpl
    public static final String MDI_001 = "EMS_D_MDI_001";
    public static final String MDI_002 = "EMS_D_MDI_002";
    public static final String MDI_001_MSG = "unable to to save message";
    public static final String MDI_002_MSG = "unable to get the count of message";
    
    // com.gam.nocr.ems.data.dao.impl.MessagePersonDAOImpl
    public static final String MPI_001 = "EMS_D_MPI_001";
    public static final String MPI_002 = "EMS_D_MPI_002";
    public static final String MPI_003 = "EMS_D_MPI_003";
    public static final String MPI_002_MSG = "unable to check the message has been inserted or not";
    public static final String MPI_003_MSG = "unable to change state of the message from unread to read";
    
    // com.gam.nocr.ems.data.dao.impl.MessageDestinationDAOImpl
    public static final String MDD_001 = "EMS_D_MDD_001";
    public static final String MDD_002 = "EMS_D_MDD_002";
    public static final String MDD_001_MSG = "Unable to change state of the message from unread to read";
    public static final String MDD_002_MSG = "Unable to find message for message id {}";
    
    // com.gam.nocr.ems.data.dao.impl.AboutDAOImpl
    public static final String ADI_001 = "EMS_D_ADI_001";
    public static final String ADI_002 = "EMS_D_ADI_002";
    public static final String ADI_002_MSG = "The operation to get aboutTo failed";
    
    // com.gam.nocr.ems.data.dao.impl.PhotoVipDAOImpl
    public static final String PHI_001 = "EMS_D_PHI_001";
    
    // com.gam.nocr.ems.data.dao.impl.NistHeaderDAOImpl
    public static final String NHD_001 = "EMS_D_NHD_001";
    public static final String NHD_002 = "EMS_D_NHD_002";
    public static final String NHD_001_MSG = "The operation to save nist header data failed";
    public static final String NHD_002_MSG = "The operation to find nist header data failed";

//	com.gam.nocr.ems.biz.service.internal.impl.WorkstationServiceImpl
    public static final String WST_002 = "EMS_WST_002";
    public static final String WST_002_MSG = "WorkStationId doesn't exist";

    
}