package com.handsonspring.model;

import java.util.ArrayList;
import java.util.List;

public enum Role {
    ADMIN("Admin"),
    USER("User");

    private List<Role> subroles = new ArrayList<>();

    static {

        ADMIN.subroles.add(USER);
    }

    String name;
    private Role(String name)
    {
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public List<Role> getSubroles() {
        return subroles;
    }

    public static Role fromString(String name) {
        for (Role role : Role.values()) {
            if (role.name.equalsIgnoreCase(name)) {
                return role;
            }
        }
        throw new IllegalArgumentException("No constant with text " + name + " found");
    }
}