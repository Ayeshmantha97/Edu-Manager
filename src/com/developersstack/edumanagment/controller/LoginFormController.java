package com.developersstack.edumanagment.controller;

import com.developersstack.edumanagment.db.Database;
import com.developersstack.edumanagment.db.DbConnection;
import com.developersstack.edumanagment.model.User;
import com.developersstack.edumanagment.util.security.PasswordManage;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class LoginFormController {
    public AnchorPane context;
    public TextField txtEmail;
    public PasswordField txtPassword;

    public void createAccountOnAction(ActionEvent actionEvent) throws IOException {
        setUi("SignupForm");

    }

    public void frogetPasswordOnAction(ActionEvent actionEvent) throws IOException {
        setUi("ForgotPasswordForm");
    }

    public void loginOnAction(ActionEvent actionEvent) throws IOException {
        String email = txtEmail.getText().toLowerCase();
        String password = txtPassword.getText().trim();

        try{
            User selectedUser = login(email);

            if(null != selectedUser){
                if( new PasswordManage().checkPassword(password,selectedUser.getPassword())){
                    setUi("DashboardForm");
                }else{ new Alert(Alert.AlertType.ERROR,"Wrong Password").show();}
            }else {
                new Alert(Alert.AlertType.ERROR,"User not found").show();
            }

        }catch (SQLException | ClassNotFoundException e){
            new Alert(Alert.AlertType.WARNING,e.toString()).show();
        }


    }

    private void setUi(String location) throws IOException {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();
    }

    private User login(String email) throws ClassNotFoundException, SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM user WHERE email=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,email);
        ResultSet resultSet = statement.executeQuery();

        if(resultSet.next()){
            User user = new User(resultSet.getString("first_name"),resultSet.getString("last_name"),resultSet.getString("email"),resultSet.getString(4));
            System.out.println(user);
            return user;
        }
        return null;


    }
}
