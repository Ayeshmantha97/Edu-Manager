package com.developersstack.edumanagment.model;

public class Program {
    private String programCode;
    private String programName;
    private double cost;
    private String teacherID;
    private String technology;

    public Program() {
    }

    public Program(String programCode, String programName, double cost, String teacherID, String technology) {
        this.programCode = programCode;
        this.programName = programName;
        this.cost = cost;
        this.teacherID = teacherID;
        this.technology = technology;
    }

    public String getProgramCode() {
        return programCode;
    }

    public void setProgramCode(String programCode) {
        this.programCode = programCode;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }
}
