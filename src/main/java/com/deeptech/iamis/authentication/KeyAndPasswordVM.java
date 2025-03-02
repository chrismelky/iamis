package com.deeptech.iamis.authentication;

import lombok.*;

/**
 * View Model object for storing the user's key and password.
 */
@Data
public class KeyAndPasswordVM {

    private String key;

    private String newPassword;

}
