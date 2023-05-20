package com.developersstack.edumanagment.controller;

import com.developersstack.edumanagment.util.tool.VerificationCodeGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ForgotPasswordFormController {
    public AnchorPane context;
    public TextField txtEmail;

    public void baclkToLoginOnAction(ActionEvent actionEvent) throws IOException {
        setUi("LoginForm");
    }

    public void sendVerificationCodeOnAction(ActionEvent actionEvent) {
        try {
            int verificationCode = new VerificationCodeGenerator().getCode(5);

            String fromEmail = "miayeshmantha@gmail.com";
            String toEmail = txtEmail.getText();
            String host = "localhost";

            Properties properties = System.getProperties();
            properties.getProperty("mail.smtp.host", host);

            Session session = Session.getDefaultInstance(properties);

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(fromEmail));
            mimeMessage.setSubject("Verification Code");
            mimeMessage.setText("Verification Code "+verificationCode);
            mimeMessage.addRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(toEmail)});

            //Transport.send(mimeMessage);
            System.out.println("Completed!");

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/CodeVerificationForm.fxml"));
            Parent parent = fxmlLoader.load();
            CodeVerificationFormController controller = fxmlLoader.getController();
            controller.setUserData(verificationCode,txtEmail.getText());
            Stage stage = (Stage) context.getScene().getWindow();
            stage.setScene(new Scene(parent));


        }catch (MessagingException e){
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setUi(String location) throws IOException {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();
    }
}
