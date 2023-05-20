package com.developersstack.edumanagment.view.tm;

import javafx.scene.control.Button;

public class ProgramTm {
    private String code;
    private String programName;
    private String teacher;
    private Button technology;
    private Button option;
    private double cost;

    public ProgramTm() {
    }

    public ProgramTm(String code, String programName, String teacher, Button technology, Button option, double cost) {
        this.code = code;
        this.programName = programName;
        this.teacher = teacher;
        this.technology = technology;
        this.option = option;
        this.cost = cost;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public Button getTechnology() {
        return technology;
    }

    public void setTechnology(Button technology) {
        this.technology = technology;
    }

    public Button getOption() {
        return option;
    }

    public void setOption(Button option) {
        this.option = option;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
