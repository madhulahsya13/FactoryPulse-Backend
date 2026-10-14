package com.factorypulse.backend.dto;

/**
 * Public registration body. There is deliberately NO role field:
 * every public registration becomes FACTORY_OWNER on the server.
 */
public class RegisterRequest {

    private String name;
    private String email;
    private String password;
    private String factoryName;

    public RegisterRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }
}
