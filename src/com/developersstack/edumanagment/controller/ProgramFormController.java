package com.developersstack.edumanagment.controller;

import com.developersstack.edumanagment.db.Database;
import com.developersstack.edumanagment.db.DbConnection;
import com.developersstack.edumanagment.model.Program;
import com.developersstack.edumanagment.model.Student;
import com.developersstack.edumanagment.model.Teacher;
import com.developersstack.edumanagment.model.Tech;
import com.developersstack.edumanagment.view.tm.ProgramTm;
import com.developersstack.edumanagment.view.tm.TechAddTm;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ProgramFormController {
    public AnchorPane context;
    public TextField txtID;
    public TextField txtName;
    public TextField txtCost;
    public TextField txtTechnology;
    public TableView tblTechnology;
    public TableColumn colTID;
    public TableColumn colTTechnology;
    public TableColumn colRemove;
    public ComboBox cmbTeacher;
    public TextField txtSearch;
    public Button btn1;
    public TableView<ProgramTm> tblProgram;
    public TableColumn colCode;
    public TableColumn colName;
    public TableColumn colTeacher;
    public TableColumn colTechnology;
    public TableColumn colCost;
    public TableColumn colOption;

    String searchText = "";
    ObservableList<TechAddTm> techList = FXCollections.observableArrayList();




    public void initialize(){
        setProgramData(searchText);
        setProgramID();
        setTeachers();

        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        colName.setCellValueFactory(new PropertyValueFactory<>("programName"));
        colTeacher.setCellValueFactory(new PropertyValueFactory<>("teacher"));
        colTechnology.setCellValueFactory(new PropertyValueFactory<>("technology"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("option"));

        colTID.setCellValueFactory(new PropertyValueFactory<>("code"));
        colTTechnology.setCellValueFactory(new PropertyValueFactory<>("name"));
        colRemove.setCellValueFactory(new PropertyValueFactory<>("btn"));

        txtSearch.textProperty().addListener((observable, oldValue, newValue) ->
                {searchText = newValue;
                if(newValue != null){
                    setProgramData(searchText);
                }
                });

        tblProgram.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setData(newValue);
            }
        });

    }

    private void setData(ProgramTm prog) throws RuntimeException {
        txtID.setText(prog.getCode());
        txtName.setText(prog.getProgramName());
        txtCost.setText(String.valueOf(prog.getCost()));
        cmbTeacher.setPromptText(prog.getTeacher());
        btn1.setText("Update");
        try {
            String tech = findTechnology(prog.getProgramName());
            String updatedString = tech.replace(Character.toString('['), "");
            String realUpdatedString = updatedString.replace(Character.toString(']'), "");
            String[] stringArray = realUpdatedString.split(",\\s*");
            ObservableList oblist2 = FXCollections.observableArrayList();
            for (int i = 0; i < stringArray.length; i++) {
                Button btn = new Button("Delete");
                TechAddTm techAddTm = new TechAddTm(
                        i+1,stringArray[i],btn
                );
                btn.setOnAction(event -> {
                    Alert  alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure?",ButtonType.YES,ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if(buttonType.get().equals(ButtonType.YES)){
                        techList.remove(techAddTm);
                    }

                });
                techList.add(techAddTm);

            }
            tblTechnology.setItems(techList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void setTeachers() {
        try {
            ObservableList<String> oblist = FXCollections.observableArrayList(findAllTeachers());
            cmbTeacher.setItems(oblist);
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }

    }

    private void setProgramID() {
        try {

            String lastID = lastProgram();
            String split[] = lastID.split("-");
            String lastIDIntegerAsAString = split[1];
            int lastIDIntegerAsAInteger = Integer.parseInt(lastIDIntegerAsAString);
            lastIDIntegerAsAInteger++;
            String generatedStudentID = "P-"+lastIDIntegerAsAInteger;
            txtID.setText(generatedStudentID);

        }catch (SQLException |ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm");
    }

    public void newProgramOnAction(ActionEvent actionEvent) {
        clear();
        setProgramID();
        btn1.setText("Save");
        cmbTeacher.setPromptText("Teacher");
    }

    public void saveOnAction(ActionEvent actionEvent) {


        String[] techArray = new String[techList.size()];
        int position = 0;
        for (TechAddTm tec : techList
        ) {
            techArray[position] = tec.getName();
            position++;
        }
        Program program = new Program(
                txtID.getText(),
                txtName.getText(),
                Double.parseDouble(txtCost.getText()),
                cmbTeacher.getValue().toString(),
                Arrays.toString(techArray)
        );

        if (btn1.getText().equals("Save")) {


            try {
                if (saveProgram(program)) {
                    setProgramData(searchText);
                    clear();
                    setProgramID();
                    new Alert(Alert.AlertType.INFORMATION,"Program Saved").show();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            try {
                if(updateProgram(program)){
                    setProgramData(searchText);
                    clear();
                    setProgramID();
                    btn1.setText("Save");
                    cmbTeacher.setPromptText("Teacher");
                    new Alert(Alert.AlertType.INFORMATION,"Program Updated").show();
                }else {
                    new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                }
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private void clear(){
        txtName.clear();
        txtCost.clear();
        cmbTeacher.getSelectionModel().clearSelection();
        techList.clear();
    }

    private void setProgramData(String searchText) {

        ObservableList<ProgramTm> oblist = FXCollections.observableArrayList();
        try {
            for (Program pr:searchPrograms(searchText)
                 ) {
                    Button tech = new Button("See more");
                    Button remove = new Button("Delete");

                    ProgramTm programTm = new ProgramTm(
                            pr.getProgramCode(),
                            pr.getProgramName(),
                            pr.getTechnology(),
                            tech,
                            remove,
                            pr.getCost()
                    );


                    remove.setOnAction(event -> {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
                        Optional<ButtonType> buttonType = alert.showAndWait();
                        if (buttonType.get().equals(ButtonType.YES)) {
                            try {
                                if(deleteProgram(pr.getProgramCode())){
                                    setProgramData(searchText);
                                    setProgramID();
                                }
                            } catch (ClassNotFoundException | SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                    oblist.add(programTm);
                }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        tblProgram.setItems(oblist);

    }


    private void setUi(String location) throws IOException {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();
    }
    public void techAddOnAction(ActionEvent actionEvent) {
        if(!isExists(txtTechnology.getText().trim())){
            Button btn = new Button("Delete");
            TechAddTm techAddTm = new TechAddTm(
                    techList.size()+1,txtTechnology.getText(),btn
            );
            techList.add(techAddTm);
            txtTechnology.clear();


            btn.setOnAction(event -> {
                Alert  alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure?",ButtonType.YES,ButtonType.NO);
                Optional<ButtonType> buttonType = alert.showAndWait();
                if(buttonType.get().equals(ButtonType.YES)){
                    techList.remove(techAddTm);
                }

            });
            tblTechnology.setItems(techList);
        }else {
            txtTechnology.selectAll();
            new Alert(Alert.AlertType.WARNING,"Already Exists").show();
        }
    }
    
    private boolean isExists(String technology){
        for (TechAddTm tech:techList
             ) {
            if(tech.getName().equals(technology)){
                return true;
            }
            
        }
        return false;
    }
    private String lastProgram() throws ClassNotFoundException, SQLException {

        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT program_code FROM program ORDER BY CAST(SUBSTRING(program_code,3) AS UNSIGNED ) DESC LIMIT 1;";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()){
            return resultSet.getString("program_code");

        }
        return "P-0";
    }

    private List<String> findAllTeachers() throws SQLException, ClassNotFoundException {
        List<String> tlist = new ArrayList<>();
        String sql = "SELECT * FROM teacher";
        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            tlist.add(resultSet.getString(1));
        }
        return tlist;

    }
    private boolean saveProgram(Program program) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "INSERT INTO program VALUES (?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,program.getProgramCode());
        statement.setString(2,program.getProgramName());
        statement.setDouble(3,program.getCost());
        statement.setString(4,program.getTechnology());
        statement.setString(5,program.getTeacherID());
        return statement.executeUpdate()>0;
    }

    private List<Program> searchPrograms(String searchText) throws SQLException, ClassNotFoundException {
        searchText= "%"+searchText+"%";
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT * FROM program WHERE name LIKE ?;";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,searchText);
        ResultSet resultSet = statement.executeQuery();
        List<Program> list = new ArrayList<>();
        while (resultSet.next()){
            list.add(
                    new Program(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getDouble(3),
                            resultSet.getString(4),
                            resultSet.getString(5)
                    )
            );
        }
        return list;
    }
    private boolean deleteProgram(String id) throws ClassNotFoundException, SQLException {

        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "DELETE FROM program WHERE program_code=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,id);
        return statement.executeUpdate()>0;
    }

    private String findTechnology(String name) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT * FROM program WHERE name=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,name);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            return resultSet.getString(4);
        }
        return null;
    }
    private boolean updateProgram(Program program) throws ClassNotFoundException, SQLException {

        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "UPDATE program SET name=?,cost=?,technology_array=?,teacher_teacher_id= ? WHERE program_code=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,program.getProgramName());
        statement.setDouble(2,program.getCost());
        statement.setString(3,program.getTechnology());
        statement.setString(4,program.getTeacherID());
        statement.setString(5,program.getProgramCode());
        return statement.executeUpdate()>0;

    }
}
