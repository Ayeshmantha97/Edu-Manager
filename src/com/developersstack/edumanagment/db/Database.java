package com.developersstack.edumanagment.db;

import com.developersstack.edumanagment.model.Program;
import com.developersstack.edumanagment.model.Student;
import com.developersstack.edumanagment.model.Teacher;
import com.developersstack.edumanagment.model.User;
import com.developersstack.edumanagment.util.security.PasswordManage;

import java.util.ArrayList;

public class Database {
    public static ArrayList<User> userTable = new ArrayList<>();

    static {
        userTable.add(new User("Isuru","Ayeshmantha","i@gmail.com",new PasswordManage().encode("1234")));
    }
    public static ArrayList<Student> studentsTable = new ArrayList<>();

    public static ArrayList<Teacher> teachersTabel = new ArrayList<>();

    public static ArrayList<Program> programsTabel = new ArrayList<>();
}
