package com.developersstack.edumanagment.controller;

import com.developersstack.edumanagment.db.Database;
import com.developersstack.edumanagment.db.DbConnection;
import com.developersstack.edumanagment.model.Student;
import com.developersstack.edumanagment.view.tm.StudentTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


public class StudentFormController {
    public AnchorPane context;
    public TextField txtID;
    public TextField txtFullness;
    public TextField txtAddress;
    public DatePicker txtDob;
    public TableView<StudentTm> tblStudent;
    public TableColumn colID;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colDob;
    public TableColumn colOption;
    public Button btn;
    public TextField txtSearch;

    String searchText = "";

    public void initialize(){
        setStudentID();
        setStudentTable(searchText);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) ->{
                    searchText = newValue;
                    setStudentTable(searchText);
                }
                );

        colID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colDob.setCellValueFactory(new PropertyValueFactory<>("dateofBirt"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        tblStudent.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->{
            if(null != newValue){
                setData(newValue);
            }

                }
                );
    }

    private void setData(StudentTm tm) {
        txtID.setText(tm.getID());
        txtFullness.setText(tm.getFullName());
        txtAddress.setText(tm.getAddress());
        txtDob.setValue(LocalDate.parse(tm.getDateofBirt(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        btn.setText("Update");

    }

    private void setStudentTable(String searchText) {
        ObservableList<StudentTm> oblist = FXCollections.observableArrayList();
        try {
            for (Student st:searchStudent(searchText)
            ) {
                StudentTm tm = null;
                if (st.getFullName().contains(searchText)) {
                    Button btn = new Button("Delete");
                    tm = new StudentTm(
                            st.getStudentID(),
                            st.getFullName(),
                            st.getAddress(),
                            new SimpleDateFormat("yyyy-MM-dd").format(st.getDob()),
                            btn
                    );
                    btn.setOnAction(e -> {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure", ButtonType.YES, ButtonType.NO);
                        Optional<ButtonType> buttonType = alert.showAndWait();
                        if (buttonType.get().equals(ButtonType.YES)) {

                            try {
                                if(deleteStudent(st.getStudentID())){
                                    new Alert(Alert.AlertType.INFORMATION, "Student removed").show();
                                    setStudentID();
                                    setStudentTable(searchText);
                                    clear();
                                }
                            } catch (ClassNotFoundException ex) {
                                throw new RuntimeException(ex);
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                            new Alert(Alert.AlertType.WARNING, "try again").show();


                        }
                    });
                    oblist.add(tm);

                }


            }
            tblStudent.setItems(oblist);
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }

    }
    private boolean deleteStudent(String id) throws ClassNotFoundException, SQLException {

        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "DELETE FROM student WHERE student_id=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,id);
        return statement.executeUpdate()>0;
    }
    private List<Student> searchStudent(String searchText) throws ClassNotFoundException, SQLException {
        searchText= "%"+searchText+"%";
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/lms3","root","1234");
        String sql = "SELECT * FROM student WHERE full_name LIKE ? OR address LIKE ?;";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,searchText);
        statement.setString(2,searchText);
        ResultSet resultSet = statement.executeQuery();
        List<Student>   list = new ArrayList<>();
        while (resultSet.next()){
           list.add(
                   new Student(
                           resultSet.getString(1),
                           resultSet.getString(2),
                           resultSet.getString(4),
                           resultSet.getDate(3)
                   )
           );
        }
        return list;
    }

    private void setStudentID() {
        try {

            String lastID = lastStudent();
            String split[] = lastID.split("-");
            String lastIDIntegerAsAString = split[1];
            int lastIDIntegerAsAInteger = Integer.parseInt(lastIDIntegerAsAString);
            lastIDIntegerAsAInteger++;
            String generatedStudentID = "S-"+lastIDIntegerAsAInteger;
            txtID.setText(generatedStudentID);
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    private String lastStudent() throws ClassNotFoundException, SQLException {

        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT student_id FROM student ORDER BY CAST(SUBSTRING(student_id,3) AS UNSIGNED ) DESC LIMIT 1;";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()){
            return resultSet.getString("student_id");

        }
        return "S-0";
    }

    public void saveOnAction(ActionEvent actionEvent) {
        Student student = new Student(
                txtID.getText(),
                txtFullness.getText(),
                txtAddress.getText(),
                Date.from(txtDob.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant())
        );
        if(btn.getText().equalsIgnoreCase("Save")){

            try{
                if(saveStudent(student)){
                    setStudentID();
                    clear();
                    setStudentTable(searchText);
                    new Alert(Alert.AlertType.INFORMATION,"Student saved!").show();
                }new Alert(Alert.AlertType.ERROR,"Try again");
            }catch (SQLException | ClassNotFoundException e){
                new Alert(Alert.AlertType.ERROR,e.toString());
            }

        }else {
            try {
                if(updateStudent(student)){
                    clear();
                    setStudentTable(searchText);
                    setStudentID();
                    btn.setText("Save");
                }
            }catch (SQLException | ClassNotFoundException et){
                et.printStackTrace();
            }
            }


    }

    private void clear(){
        txtDob.setValue(null);
        txtAddress.clear();
        txtFullness.clear();
    }

    public void newStudentOnAction(ActionEvent actionEvent) {
        clear();
        setStudentID();
        btn.setText("Save");
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashboardForm");
    }

    private void setUI(String location) throws IOException {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();

    }

    private boolean saveStudent(Student student) throws ClassNotFoundException, SQLException {

        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "INSERT INTO student VALUES(?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,student.getStudentID());
        statement.setString(2,student.getFullName());
        statement.setObject(3,student.getDob());
        statement.setString(4,student.getAddress());
        return statement.executeUpdate()>0;


    }
    private boolean updateStudent(Student student) throws ClassNotFoundException, SQLException {

        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "UPDATE student SET dob=?,full_name=?,address=? WHERE student_id=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(4,student.getStudentID());
        statement.setString(2,student.getFullName());
        statement.setObject(1,student.getDob());
        statement.setString(3,student.getAddress());
        return statement.executeUpdate()>0;


    }
}
