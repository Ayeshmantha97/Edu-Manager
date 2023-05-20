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

public class SignupFormController {
    public AnchorPane context;
    public TextField txtFirstName;
    public PasswordField txtPassword;
    public TextField txtEmail;
    public TextField txtLastName;

    public void signupOnAction(ActionEvent actionEvent) throws IOException {
        String email = txtEmail.getText().toLowerCase();
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String password = new PasswordManage().encode(txtPassword.getText().trim());

        Database.userTable.add(new User(firstName,lastName,email,password));
        User user=new  User(firstName,lastName,email,password);

        try{
            boolean isSaved = signup(user);

            if(isSaved){
                new Alert(Alert.AlertType.INFORMATION,"Welcome!").show();
                setUi("LoginForm");
            }else {
                new Alert(Alert.AlertType.WARNING,"Try again").show();
            }
        }catch (SQLException | ClassNotFoundException e ){
            new Alert(Alert.AlertType.ERROR,e.toString()).show();
        }


    }

    public void alredyHaveAnAccountOnAction(ActionEvent actionEvent) throws IOException {
        setUi("LoginForm");
    }
    private void setUi(String location) throws IOException {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();
    }
    private boolean signup(User user) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver"); //load Driver
        Connection connection = DbConnection.getInstance().getConnection();
        //write a sql
        //language=SQL
        String sql = "INSERT INTO user VALUES(?,?,?,?) ";
        //statement
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,user.getEmail());
        statement.setString(2,user.getFirstName());
        statement.setString(3,user.getLastName());
        statement.setString(4,user.getPassword());
        //set sql into the statement and execute
        return statement.executeUpdate()>0;

    }
}
