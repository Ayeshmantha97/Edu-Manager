package com.developersstack.edumanagment.util.security;

import org.mindrot.BCrypt;


public class PasswordManage {
    public String encode(String rowPassword){
        return BCrypt.hashpw(rowPassword,BCrypt.gensalt(10));
    }

    public boolean checkPassword(String rowPassword,String hashPassword){
        return BCrypt.checkpw(rowPassword,hashPassword);
    }
}
