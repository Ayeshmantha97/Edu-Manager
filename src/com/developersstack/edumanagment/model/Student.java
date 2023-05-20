package com.developersstack.edumanagment.model;

import java.util.Date;

public class Student {
    private String studentID;
    private String fullName;
    private String address;
    private Date dob;

    public Student() {
    }

    public Student(String studentID, String fullName, String address, Date dob) {
        this.studentID = studentID;
        this.fullName = fullName;
        this.address = address;
        this.dob = dob;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentID='" + studentID + '\'' +
                ", fullName='" + fullName + '\'' +
                ", address='" + address + '\'' +
                ", dob=" + dob +
                '}';
    }
}
