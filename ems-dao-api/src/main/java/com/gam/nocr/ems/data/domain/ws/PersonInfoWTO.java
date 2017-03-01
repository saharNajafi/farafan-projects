package com.gam.nocr.ems.data.domain.ws;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class PersonInfoWTO {

    private Long nId;
    private byte[] firstName;
    private byte[] lastName;
    private byte[] fatherName;
    private byte[] birthCertificateSeri;
    private int birthCertificateSerial;
    private int birthCertificateNumber;
    private int birthDate;
    private int gender;
    private int officeCode;
    private int bookNumber;
    private int bookRow;
    private int firstNameHasPrefix;
    private int firstNameHasPostfix;
    private int lastNameHasPrefix;
    private int lastNameHasPostfix;

    public PersonInfoWTO(Long nId, byte[] firstName,
                         byte[] lastName,
                         byte[] fatherName,
                         byte[] birthCertificateSeri,
                         int birthCertificateSerial,
                         int birthCertificateNumber,
                         int birthDate,
                         int gender,
                         int officeCode,
                         int bookNumber,
                         int bookRow,
                         int firstNameHasPrefix,
                         int firstNameHasPostfix,
                         int lastNameHasPrefix,
                         int lastNameHasPostfix) {
        this.nId = nId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fatherName = fatherName;
        this.birthCertificateSeri = birthCertificateSeri;
        this.birthCertificateSerial = birthCertificateSerial;
        this.birthCertificateNumber = birthCertificateNumber;
        this.birthDate = birthDate;
        this.gender = gender;
        this.officeCode = officeCode;
        this.bookNumber = bookNumber;
        this.bookRow = bookRow;
        this.firstNameHasPrefix = firstNameHasPrefix;
        this.firstNameHasPostfix = firstNameHasPostfix;
        this.lastNameHasPrefix = lastNameHasPrefix;
        this.lastNameHasPostfix = lastNameHasPostfix;
    }

    public Long getnId() {
        return nId;
    }

    public void setnId(Long nId) {
        this.nId = nId;
    }

    public byte[] getFirstName() {
        return firstName == null ? firstName : firstName.clone();
    }

    public void setFirstName(byte[] firstName) {
        this.firstName = firstName;
    }

    public byte[] getLastName() {
        return lastName == null ? lastName : lastName.clone();
    }

    public void setLastName(byte[] lastName) {
        this.lastName = lastName;
    }

    public byte[] getFatherName() {
        return fatherName == null ? fatherName : fatherName.clone();
    }

    public void setFatherName(byte[] fatherName) {
        this.fatherName = fatherName;
    }

    public byte[] getBirthCertificateSeri() {
        return birthCertificateSeri == null ? birthCertificateSeri : birthCertificateSeri.clone();
    }

    public void setBirthCertificateSeri(byte[] birthCertificateSeri) {
        this.birthCertificateSeri = birthCertificateSeri;
    }

    public int getBirthCertificateSerial() {
        return birthCertificateSerial;
    }

    public void setBirthCertificateSerial(int birthCertificateSerial) {
        this.birthCertificateSerial = birthCertificateSerial;
    }

    public int getBirthCertificateNumber() {
        return birthCertificateNumber;
    }

    public void setBirthCertificateNumber(int birthCertificateNumber) {
        this.birthCertificateNumber = birthCertificateNumber;
    }

    public int getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(int birthDate) {
        this.birthDate = birthDate;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(int officeCode) {
        this.officeCode = officeCode;
    }

    public int getBookNumber() {
        return bookNumber;
    }

    public void setBookNumber(int bookNumber) {
        this.bookNumber = bookNumber;
    }

    public int getBookRow() {
        return bookRow;
    }

    public void setBookRow(int bookRow) {
        this.bookRow = bookRow;
    }

    public int getFirstNameHasPrefix() {
        return firstNameHasPrefix;
    }

    public void setFirstNameHasPrefix(int firstNameHasPrefix) {
        this.firstNameHasPrefix = firstNameHasPrefix;
    }

    public int getFirstNameHasPostfix() {
        return firstNameHasPostfix;
    }

    public void setFirstNameHasPostfix(int firstNameHasPostfix) {
        this.firstNameHasPostfix = firstNameHasPostfix;
    }

    public int getLastNameHasPrefix() {
        return lastNameHasPrefix;
    }

    public void setLastNameHasPrefix(int lastNameHasPrefix) {
        this.lastNameHasPrefix = lastNameHasPrefix;
    }

    public int getLastNameHasPostfix() {
        return lastNameHasPostfix;
    }

    public void setLastNameHasPostfix(int lastNameHasPostfix) {
        this.lastNameHasPostfix = lastNameHasPostfix;
    }
}
