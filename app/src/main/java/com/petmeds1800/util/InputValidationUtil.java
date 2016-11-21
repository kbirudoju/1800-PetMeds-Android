package com.petmeds1800.util;

/**
 * Created by Digvijay on 9/14/2016.
 */
public class InputValidationUtil {

    public static final String passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z]).{8,}$";

    public static final String firstNamePattern = "[A-Z][a-zA-Z]*";

    public static final String lastNamePattern = "[a-zA-z]+([ '-][a-zA-Z]+)*";
}
