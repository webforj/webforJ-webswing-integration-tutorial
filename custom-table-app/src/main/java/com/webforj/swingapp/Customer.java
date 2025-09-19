package com.webforj.swingapp;

public class Customer {
  private int id;
  private String name;
  private String company;
  private String email;

  public Customer(int id, String name, String company, String email) {
    this.id = id;
    this.name = name;
    this.company = company;
    this.email = email;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getCompany() {
    return company;
  }

  public String getEmail() {
    return email;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}