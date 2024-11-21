package org.example.noteapp.supportsClass;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {
    //хэширование пароля
    public static String hashPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
    //проверка пароля
    public static boolean checkPassword(String password, String hashed){
        return BCrypt.checkpw(password, hashed);
    }
}
