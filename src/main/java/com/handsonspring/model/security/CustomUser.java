package com.handsonspring.model.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUser extends User {

    private com.handsonspring.model.User user;

    public CustomUser(String username, String password, boolean enabled, boolean accountNonExpired,
                      boolean credentialsNonExpired,
                      boolean accountNonLocked,
                      Collection<? extends GrantedAuthority> authorities, com.handsonspring.model.User userEntity) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.user = userEntity;
    }

    public com.handsonspring.model.User getUser() {
        return user;
    }

    public void setUser(com.handsonspring.model.User user) {
        this.user = user;
    }
}