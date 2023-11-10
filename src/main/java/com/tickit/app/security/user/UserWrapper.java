package com.tickit.app.security.user;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserWrapper {
    List<User> users;

    public UserWrapper(List<User> users) {
        this.users = users;
    }
}