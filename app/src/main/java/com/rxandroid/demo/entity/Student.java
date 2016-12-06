package com.rxandroid.demo.entity;

import java.util.List;

/**
 * @author 张全
 */

public class Student {
    private List<Course> courses;
    private String name;
    public List<Course> getCourse() {
        return courses;
    }

    public void setCourse(List<Course> courses) {
        this.courses = courses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
