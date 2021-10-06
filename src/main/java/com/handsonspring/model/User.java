package com.handsonspring.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User implements Identifiable {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    private String username;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String password;
    private String passwordConfirm;
    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPasswordAsString(String password) {
        if(!password.isEmpty()) {
            this.password = password;
        }
    }

    @Transient
    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }


    public Role getRole() {
        return role;
    }

    public String getRoleAsString() {
        return role.getName();
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setRoleAsString(String roleAsString) {
        this.role = Role.fromString(roleAsString);
    }

    public String toString() {
        return getUsername();
    }
}
