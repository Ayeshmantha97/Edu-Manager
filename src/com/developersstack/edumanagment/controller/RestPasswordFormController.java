package com.developersstack.edumanagment.controller;

import com.developersstack.edumanagment.db.Database;
import com.developersstack.edumanagment.model.User;
import com.developersstack.edumanagment.util.security.PasswordManage;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class RestPasswordFormController {
    public AnchorPane context;
    public TextField txtNewPassword;

    String mail = "";

    public void setUserData(String email) {
        mail = email;
    }



    public void changePasswordOnAction(ActionEvent actionEvent) throws IOException {
        Optional<User> selectedUser = Database.userTable.stream().filter(e -> e.getEmail().equals(mail)).findFirst();
        if(selectedUser.isPresent()){
            selectedUser.get().setPassword(new PasswordManage().encode(txtNewPassword.getText()));
            setUi("LoginForm");
        }else{
            new Alert(Alert.AlertType.INFORMATION,"Not Found").show();
        }

    }
    private void setUi(String location) throws IOException {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/" + location + ".fxml"))));
        stage.centerOnScreen();
    }
}
