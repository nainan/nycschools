package com.schools.newyork.network;

import com.schools.newyork.model.School;
import com.schools.newyork.model.SchoolSATScores;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewYorkServiceInterface {

    @GET("s3k6-pzi2.json")
    Call<List<School>> listSchools();

    @GET("f9bf-2cp4.json")
    Call<List<SchoolSATScores>> getSATScores(@Query("dbn") String dbn);

}