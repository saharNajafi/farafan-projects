package com.gam.nocr.ems.data.domain.vol;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.util.EmsUtil;
import flexjson.JSON;

import java.util.List;

/**
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class PersonVTO extends ExtEntityTO {

    private String firstName;
    private String lastName;
    private String nid;
    private String userName;
    private String password;
    private String cPassword;
    private String lastLogin;
    private String birthCertNum;
    private String birthCertSeries;
    private String fatherName;
    private String mobilePhone;
    private String phone;
    private String email;
    private String lStatus;
    private String requestStatusString;

    private String departmentId;
    private String departmentName;

    private String roles;
    private List<RoleVTO> roleList;
    private String permissions;
    private List<PermissionVTO> permissionList;
    private String admin;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @JSON(include = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JSON(include = false)
    public String getcPassword() {
        return cPassword;
    }

    public void setcPassword(String cPassword) {
        this.cPassword = cPassword;
    }

    @JSON(include = false)
    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    @JSON(include = false)
    public String getBirthCertNum() {
        return birthCertNum;
    }

    public void setBirthCertNum(String birthCertNum) {
        this.birthCertNum = birthCertNum;
    }

    @JSON(include = false)
    public String getBirthCertSeries() {
        return birthCertSeries;
    }

    public void setBirthCertSeries(String birthCertSeries) {
        this.birthCertSeries = birthCertSeries;
    }

    @JSON(include = false)
    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    @JSON(include = false)
    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    @JSON(include = false)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getlStatus() {
        return lStatus;
    }

    public void setlStatus(String lStatus) {
        this.lStatus = lStatus;
    }

    @JSON(include = false)
    public String getRequestStatusString() {
        return requestStatusString;
    }

    public void setRequestStatusString(String requestStatusString) {
        this.requestStatusString = requestStatusString;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    @JSON(include = false)
    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @JSON(include = false)
    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public List<RoleVTO> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<RoleVTO> roleList) {
        this.roleList = roleList;
    }

    @JSON(include = false)
    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public List<PermissionVTO> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<PermissionVTO> permissionList) {
        this.permissionList = permissionList;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String toString() {
        return EmsUtil.toJSON(this);
    }
}
