package com.developersstack.edumanagment.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class CodeVerificationFormController {
    public AnchorPane context;
    public TextField txtCode;
    public Label lblSelectedEmail;
    int code = 0;
    String selectedEmail = "";

    public void setUserData(int vrificationCode , String email){
        lblSelectedEmail.setText(email);
        selectedEmail = email;
        code = vrificationCode;
        System.out.println(code);

    }

    public void verifyOnAction(ActionEvent actionEvent) throws IOException {
        if(String.valueOf(code).equals(txtCode.getText())){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/RestPasswordForm.fxml"));
            Parent parent = fxmlLoader.load();
            RestPasswordFormController controller = fxmlLoader.getController();
            controller.setUserData(selectedEmail);
            Stage stage = (Stage) context.getScene().getWindow();
            stage.setScene(new Scene(parent));
        }else {
            new Alert(Alert.AlertType.ERROR,"Wrong Verification Code!!").show();
        }
    }

    public void changeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("ForgotPasswordForm");
    }

    private void setUi(String location) throws IOException {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/" + location + ".fxml"))));
        stage.centerOnScreen();
    }
    }
