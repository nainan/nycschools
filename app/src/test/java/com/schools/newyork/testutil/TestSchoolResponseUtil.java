package com.schools.newyork.testutil;

public class TestSchoolResponseUtil {
    public static final String TEST_DBN_1 = "02M438";
    public static final String TEST_SCHOOL_NAME_1 = "International High School at Union Square";
    public static final String TEST_NUM_SAT_TESTERS_1 = "50";
    public static final String TEST_CRITICAL_READING_AVG_1 = "356";
    public static final String TEST_MATH_AVG_1 = "453";
    public static final String TEST_WRITING_AVG_1 = "390";

    public static final String TEST_DBN_2 = "02M260";
    public static final String TEST_SCHOOL_NAME_2 = "Clinton School Writers & Artists, M.S. 260";
    public static final String schoolResponse = "[{" +
            "\"dbn\": \"" + TEST_DBN_1 + "\"," +
            "\"school_name\": \"" + TEST_SCHOOL_NAME_1 + "\"" +
            "}," +
            "{" +
            "\"dbn\": \"" + TEST_DBN_2 + "\"," +
            "\"school_name\": \"" + TEST_SCHOOL_NAME_2 + "\"" +
            "}]";
    public static final String schoolSATScoreResponse = "[{" +
            "\"dbn\": \"" + TEST_DBN_1 + "\"," +
            "\"school_name\": \"" + TEST_SCHOOL_NAME_1 + "\"," +
            "\"num_of_sat_test_takers\": \"" + TEST_NUM_SAT_TESTERS_1 + "\"," +
            "\"sat_critical_reading_avg_score\": \"" + TEST_CRITICAL_READING_AVG_1 + "\"," +
            "\"sat_math_avg_score\": \"" + TEST_MATH_AVG_1 + "\"," +
            "\"sat_writing_avg_score\": \"" + TEST_WRITING_AVG_1 + "\"" +
            "}]";
}
