package com.developersstack.edumanagment.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class IntakeFormController {
    public AnchorPane contaxt;
    public TextField txtID;
    public TextField txtName;
    public DatePicker txtStartDate;
    public ComboBox cmbProgram;
    public TextField txtSearch;
    public Button btnSave;
    public TableView tblIntake;
    public TableColumn colID;
    public TableColumn colIntake;
    public TableColumn colStartDate;
    public TableColumn colProgram;
    public TableColumn colComplete;
    public TableColumn colOption;

    public void newIntakeOnAction(ActionEvent actionEvent) {
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm");
    }

    private void setUi(String location) throws IOException {
        Stage stage = (Stage) contaxt.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();
    }

    public void saveOnAction(ActionEvent actionEvent) {
    }
}
