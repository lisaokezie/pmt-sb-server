package com.tickit.app.security.user;

import lombok.Getter;
import lombok.Setter;

/**
 * Request to change a user's password
 */
@Getter
@Setter
public class PasswordChangeRequest {
    private String oldPassword;
    private String newPassword;
}
