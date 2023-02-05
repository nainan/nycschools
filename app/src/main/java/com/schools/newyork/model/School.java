package com.schools.newyork.model;

public class School {
    String dbn;
    String school_name;

    public School(String dbn, String schoolName) {
        this.dbn = dbn;
        this.school_name = schoolName;
    }

    public String getDbn() {
        return dbn;
    }

    public String getSchoolName() {
        return school_name;
    }
}
