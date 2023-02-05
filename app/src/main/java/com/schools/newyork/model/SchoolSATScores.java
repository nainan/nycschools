package com.schools.newyork.model;

public class SchoolSATScores {
    String dbn;
    String school_name;
    String num_of_sat_test_takers;
    String sat_critical_reading_avg_score;
    String sat_math_avg_score;
    String sat_writing_avg_score;

    public SchoolSATScores(
            String dbn, String school_name,
            String num_of_sat_test_takers,
            String sat_critical_reading_avg_score,
            String sat_math_avg_score,
            String sat_writing_avg_score) {
        this.dbn = dbn;
        this.school_name = school_name;
        this.num_of_sat_test_takers = num_of_sat_test_takers;
        this.sat_critical_reading_avg_score = sat_critical_reading_avg_score;
        this.sat_math_avg_score = sat_math_avg_score;
        this.sat_writing_avg_score = sat_writing_avg_score;
    }

    public String getDbn() {
        return dbn;
    }

    public String getSchoolName() {
        return school_name;
    }

    public String getNumOfSatTestTakers() {
        return num_of_sat_test_takers;
    }

    public String getSatCriticalReadingAvgScore() {
        return sat_critical_reading_avg_score;
    }

    public String getSatMathAvgScore() {
        return sat_math_avg_score;
    }

    public String getSatWritingAvgScore() {
        return sat_writing_avg_score;
    }
}
