package com.mindfire.backend.constants;

public class ValidatorConstants {
    public static final String FIRST_NAME_NOT_EMPTY = "First name cannot be empty";
    public static final String FIELD_SIZE_FIRST_NAME = "First name must be between 3 and 20 characters";

    public static final String LAST_NAME_NOT_EMPTY = "Last name cannot be empty";
    public static final String FIELD_SIZE_LAST_NAME = "Last name must be between 3 and 20 characters";

    public static final String EMAIL_NOT_EMPTY = "Email cannot be empty";
    public static final String EMAIL_INVALID = "Invalid email format";


    public static final String PASSWORD_NOT_EMPTY = "Password cannot be empty";
    public static final String PASSWORD_SIZE = "Password must be at least 6 characters long";

    public static final String USER_EMAIL_NOT_FOUND = "User not exists by user email";
    public static final String USER_ID_NOT_FOUND = "User with id does not exists";

    public static final String INVALID_PAGE_SIZE = "Page size can't be negative or Zero";

    public static final String ROLE_NOT_FOUND = "Role not exists";
    public static final String TOKEN_NOT_EMPTY = "Token can not be empty";
}
