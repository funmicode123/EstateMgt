package com.funmicode.data.model;

public enum UserRole {
    RESIDENT, ADMIN, SECURITY;

    public static UserRole fromString(String role) {
        try {
            return UserRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }
}
