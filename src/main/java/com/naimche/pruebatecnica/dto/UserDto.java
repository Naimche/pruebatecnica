package com.naimche.pruebatecnica.dto;

import com.naimche.pruebatecnica.entity.Address;

public class UserDto {

    private Long id;
    private String name;
    private String username;
    private Address address;


    //region Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
    //endregion

    public UserDto() {
    }

    public UserDto(Long id, String name, String username, Address address) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.address = address;
    }
}