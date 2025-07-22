package com.naimche.pruebatecnica.dto;

import com.naimche.pruebatecnica.model.Address;

public class CreateUserDto {

    private String name;
    private String username;
    private String password;
    private Address address;

    //region Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
    //endregion


    public CreateUserDto(String name, String username, String password, Address address) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.address = address;
    }

    public CreateUserDto() {
    }
}