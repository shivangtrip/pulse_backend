package com.nk;
import java.util.regex.Pattern;


public class InputValidation {


    public static boolean isValidEmail(String email)

    {

        String emailRegex = "[\\w]+@[a-zA-Z]+\\.[a-zA-Z]+";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        return pat.matcher(email).matches();
    }

    public static boolean isValidPassword(String password)

    {
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

        Pattern pat = Pattern.compile(passwordRegex);
        if (password == null) {
            return false;
        }
        return pat.matcher(password).matches();
    }

    public static boolean isValidUsername(String username)

    {
        String usernameRegex = "^[a-z0-9_-]{6,14}$";

        Pattern pat = Pattern.compile(usernameRegex );
        if (username == null){
            return false;}
        return pat.matcher(username).matches();
    }


}
