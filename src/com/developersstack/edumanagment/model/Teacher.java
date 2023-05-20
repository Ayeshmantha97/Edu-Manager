package com.developersstack.edumanagment.model;

public class Teacher {
    private String teacherID;
    private String fullName;
    private String contact;
    private String address;

    public Teacher() {
    }

    public Teacher(String teacherID, String fullName, String contact, String address) {
        this.teacherID = teacherID;
        this.fullName = fullName;
        this.contact = contact;
        this.address = address;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
