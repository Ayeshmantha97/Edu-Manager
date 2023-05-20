package com.developersstack.edumanagment.controller;

import com.developersstack.edumanagment.db.Database;
import com.developersstack.edumanagment.db.DbConnection;
import com.developersstack.edumanagment.model.Student;
import com.developersstack.edumanagment.model.Teacher;
import com.developersstack.edumanagment.view.tm.StudentTm;
import com.developersstack.edumanagment.view.tm.TeacherTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeacherFormController {
    public AnchorPane contaxt;
    public TextField txtID;
    public TextField txtFullname;
    public TextField txtContact;
    public TextField txtAddress;
    public TableView tblTeacher;
    public TableColumn colID;
    public TableColumn colFullname;
    public TableColumn colContact;
    public TableColumn colAddress;
    public TableColumn colOption;
    public Button btn;
    public TextField txtSearch;

    String searchText = "";

    public void initialize(){

        setTeacherID();
        setTeacherTable(searchText);

        colID.setCellValueFactory(new PropertyValueFactory("teacherID"));
        colFullname.setCellValueFactory(new PropertyValueFactory("fullName"));
        colContact.setCellValueFactory(new PropertyValueFactory("contact"));
        colAddress.setCellValueFactory(new PropertyValueFactory("address"));
        colOption.setCellValueFactory(new PropertyValueFactory("btn"));

        tblTeacher.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->{
                    if(null != newValue){
                        setData((TeacherTm) newValue);
                    }
                }
        );

        txtSearch.textProperty().addListener((observable, oldValue, newValue) ->
        {
            searchText = newValue;
            setTeacherTable(searchText);
        });
    }

    private void setData(TeacherTm tm) {
        txtID.setText(tm.getTeacherID());
        txtFullname.setText(tm.getFullName());
        txtContact.setText(tm.getContact());
        txtAddress.setText(tm.getAddress());
        btn.setText("Update");

    }

    private void setTeacherID() {
       try {
           String lastID = lastTeacher();
           String [] split = lastID.split("-");
           String lastIntegerAsString = split[1];
           int lastIntegerAsInteger = Integer.parseInt(lastIntegerAsString);
           lastIntegerAsInteger++;
           String generatedTeacherID = "T-"+lastIntegerAsInteger;
           txtID.setText(generatedTeacherID);
       }catch (SQLException | ClassNotFoundException e){
           e.printStackTrace();
       }
    }

    public void saveOnAction(ActionEvent actionEvent) {
        Teacher teacher = new Teacher(
                txtID.getText(),
                txtFullname.getText(),
                txtContact.getText(),
                txtAddress.getText()
        );
        if(btn.getText().equalsIgnoreCase("Save")){
            try{
                    if(saveTeacher(teacher)){
                        clear();
                        setTeacherTable(searchText);
                        setTeacherID();
                        new Alert(Alert.AlertType.INFORMATION,"Teacher saved!").show();
                    }new Alert(Alert.AlertType.ERROR,"Try again");
                }catch (SQLException | ClassNotFoundException e){
                    new Alert(Alert.AlertType.ERROR,e.toString());
                }

            }else {
                try {
                    if(updateTeacher(teacher)){
                        clear();
                        setTeacherTable(searchText);
                        setTeacherID();
                        btn.setText("Save");
                    }
                }catch (SQLException | ClassNotFoundException et){
                    et.printStackTrace();
                }

            }
    }

    private void setTeacherTable(String searchText) {
        ObservableList<TeacherTm> oblist = FXCollections.observableArrayList();
        try {
            for (Teacher t:searchTeachers(searchText)
            ) {

                if (t.getFullName().contains(searchText)) {
                    Button btn = new Button("Delete");
                    TeacherTm tm = new TeacherTm(
                            t.getTeacherID(),
                            t.getFullName(),
                            t.getContact(),
                            t.getAddress(),
                            btn
                    );
                    btn.setOnAction(e -> {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure", ButtonType.YES, ButtonType.NO);
                        Optional<ButtonType> buttonType = alert.showAndWait();
                        if (buttonType.get().equals(ButtonType.YES)) {
                            try {
                                if(deleteTeacher(t.getTeacherID())){
                                    new Alert(Alert.AlertType.INFORMATION, "Teacher removed").show();
                                    setTeacherID();
                                    setTeacherTable(searchText);
                                    clear();
                                }else {
                                    new Alert(Alert.AlertType.WARNING, "try again").show();
                                }
                            } catch (ClassNotFoundException | SQLException ex) {
                                throw new RuntimeException(ex);
                            }


                        }
                    });

                    oblist.add(tm);
                }
            }
            tblTeacher.setItems(oblist);

        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }

    }

    private void clear(){
        txtFullname.clear();
        txtContact.clear();
        txtAddress.clear();
    }


    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashboardForm");
    }

    private void setUI(String location) throws IOException {
        Stage stage = (Stage) contaxt.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();
    }

    public void newTeacherOnAction(ActionEvent actionEvent) {
        clear();
        btn.setText("Save");
        setTeacherTable(searchText);
        setTeacherID();
    }
    private boolean saveTeacher(Teacher teacher) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "INSERT INTO teacher VALUES (?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,teacher.getTeacherID());
        preparedStatement.setString(2,teacher.getFullName());
        preparedStatement.setString(3,teacher.getContact());
        preparedStatement.setString(4,teacher.getAddress());
        return preparedStatement.executeUpdate()>0;
    }
    private boolean updateTeacher(Teacher teacher) throws ClassNotFoundException, SQLException {

        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "UPDATE teacher SET full_name=?,contact=?,address=? WHERE teacher_id=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(4,teacher.getTeacherID());
        statement.setString(2,teacher.getContact());
        statement.setObject(1,teacher.getFullName());
        statement.setString(3,teacher.getAddress());
        return statement.executeUpdate()>0;

    }
    private String lastTeacher() throws ClassNotFoundException, SQLException {

        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT teacher_id FROM teacher ORDER BY CAST(SUBSTRING(teacher_id,3) AS UNSIGNED ) DESC LIMIT 1;";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()){
            return resultSet.getString("teacher_id");

        }
        return "T-0";
    }
    private List<Teacher> searchTeachers(String searchText) throws SQLException, ClassNotFoundException {
        searchText= "%"+searchText+"%";
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT * FROM teacher WHERE full_name LIKE ? OR address LIKE ?;";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,searchText);
        statement.setString(2,searchText);
        ResultSet resultSet = statement.executeQuery();
        List<Teacher> list = new ArrayList<>();
        while (resultSet.next()){
            list.add(
                    new Teacher(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4)
                    )
            );
        }
        return list;
    }
    private boolean deleteTeacher(String id) throws ClassNotFoundException, SQLException {

        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "DELETE FROM teacher WHERE teacher_id=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,id);
        return statement.executeUpdate()>0;
    }
}
