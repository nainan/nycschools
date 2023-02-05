package com.schools.newyork.modules;

import com.schools.newyork.network.NewYorkServiceInterface;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {

    private static final String CITY_OF_NEWYORK_URL = "https://data.cityofnewyork.us/resource/";

    @Singleton
    @Provides
    public NewYorkServiceInterface getNewYorkServiceInterface(Retrofit retrofit) {
        return retrofit.create(NewYorkServiceInterface.class);
    }

    @Singleton
    @Provides
    public Retrofit getNewYorkRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(CITY_OF_NEWYORK_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
