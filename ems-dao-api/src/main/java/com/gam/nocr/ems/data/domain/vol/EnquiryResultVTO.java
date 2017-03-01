package com.gam.nocr.ems.data.domain.vol;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public class EnquiryResultVTO {

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
    private int deathStatus;
    private String[] message;

    public EnquiryResultVTO(Long nId, byte[] firstName,
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
                            int deathStatus,
                            String[] message) {
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
        this.deathStatus = deathStatus;
        this.message = message;
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
        return birthCertificateSeri == null ? birthCertificateSeri  : birthCertificateSeri.clone();
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

    public int getDeathStatus() {
        return deathStatus;
    }

    public void setDeathStatus(int deathStatus) {
        this.deathStatus = deathStatus;
    }

    public String[] getMessage() {
        return message == null ? message : message.clone();
    }

    public void setMessage(String[] message) {
        this.message = message;
    }
}
