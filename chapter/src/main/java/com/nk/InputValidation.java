package com.nk;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class InputValidation {


    public static boolean isValidEmail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
    public static boolean isValidPassword(String password)
    {
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

        Pattern pat = Pattern.compile(passwordRegex );
        if (password == null)
            return false;
        return pat.matcher(password).matches();
    }
    public static boolean isValidUsername(String username)
    {
        String passwordRegex = "^[a-z0-9_-]{3,15}$";

        Pattern pat = Pattern.compile(passwordRegex );
        if (username == null)
            return false;
        return pat.matcher(username).matches();
    }


}
