package com.developersstack.edumanagment.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class RegistrationFormController {
    public AnchorPane contaxt;
    public TextField txtRegistrationID;
    public TextField txtStudent;
    public ComboBox cmbProgram;
    public RadioButton rbnPaid;

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm");
    }

    public void regusterOnAction(ActionEvent actionEvent) {
    }
    private void setUi(String location) throws IOException {
        Stage stage = (Stage) contaxt.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();
    }
}
