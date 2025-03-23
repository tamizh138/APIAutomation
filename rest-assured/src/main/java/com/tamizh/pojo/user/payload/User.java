package com.tamizh.pojo.user.payload;

public class User {
    private String name;
    private String job;

    public User(String name, String job) {
        this.name = name;
        this.job = job;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setJob(String job) {
        this.job = job;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", job='" + job + '\'' +
                '}';
    }
}
