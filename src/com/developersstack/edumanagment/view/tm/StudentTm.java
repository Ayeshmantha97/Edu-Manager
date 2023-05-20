package com.developersstack.edumanagment.view.tm;

import javafx.scene.control.Button;

public class StudentTm {
    private String ID;
    private String fullName;
    private String address;
    private String dateofBirt;
    private Button btn;

    public StudentTm() {
    }

    public StudentTm(String ID, String fullName, String address, String dateofBirt, Button btn) {
        this.ID = ID;
        this.fullName = fullName;
        this.address = address;
        this.dateofBirt = dateofBirt;
        this.btn = btn;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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

    public String getDateofBirt() {
        return dateofBirt;
    }

    public void setDateofBirt(String dateofBirt) {
        this.dateofBirt = dateofBirt;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }
}
