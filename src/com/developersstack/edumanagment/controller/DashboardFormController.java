package com.developersstack.edumanagment.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.text.DateFormatter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static com.sun.deploy.util.PerfLogger.setTime;

public class DashboardFormController {
    public Label lblDate;
    public AnchorPane context;
    public Label lblTime;

    public void initialize(){
      setData();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0),e ->{
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            lblTime.setText(LocalTime.now().format(dateTimeFormatter));
        } ),
        new KeyFrame(Duration.seconds(1))
    );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void setData() {
        lblDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

    }

    public void studentOnAction(ActionEvent actionEvent) throws IOException {
        setUi("StudentForm");
    }

    public void signoutOnAction(ActionEvent actionEvent) throws IOException {
        setUi("LoginForm");
    }
    private void setUi(String location) throws IOException {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();
    }

    public void teacherOnAction(ActionEvent actionEvent) throws IOException {
        setUi("TeacherForm");
    }

    public void programOnAction(ActionEvent actionEvent) throws IOException {
        setUi("ProgramForm");
    }

    public void intakeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("IntakeForm");
    }

    public void registerOnAction(ActionEvent actionEvent) throws IOException {
        setUi("RegistrationForm");
    }
}
