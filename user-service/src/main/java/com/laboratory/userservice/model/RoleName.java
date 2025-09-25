package com.laboratory.userservice.model;

public enum RoleName {
    ROLE_USER("Usuario básico del sistema"),
    ROLE_ADMIN("Administrador del sistema"),
    ROLE_TECHNICIAN("Técnico de laboratorio"),
    ROLE_VIEWER("Solo lectura"),
    ROLE_SUPERVISOR("Supervisor de laboratorio");

    private final String description;

    RoleName(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}