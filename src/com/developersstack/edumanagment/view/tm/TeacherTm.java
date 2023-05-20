package com.developersstack.edumanagment.view.tm;

import javafx.scene.control.Button;

public class TeacherTm {
    private String teacherID;
    private String fullName;
    private String contact;
    private String address;
    private Button btn;

    public TeacherTm() {
    }

    public TeacherTm(String teacherID, String fullName, String contact, String address, Button btn) {
        this.teacherID = teacherID;
        this.fullName = fullName;
        this.contact = contact;
        this.address = address;
        this.btn = btn;
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

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    @Override
    public String toString() {
        return "TeacherTm{" +
                "teacherID='" + teacherID + '\'' +
                ", fullName='" + fullName + '\'' +
                ", contact='" + contact + '\'' +
                ", address='" + address + '\'' +
                ", btn=" + btn +
                '}';
    }
}
