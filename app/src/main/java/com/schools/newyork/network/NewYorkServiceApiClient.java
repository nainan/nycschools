package com.schools.newyork.network;

import androidx.lifecycle.MutableLiveData;

import com.google.common.flogger.FluentLogger;
import com.schools.newyork.model.School;
import com.schools.newyork.model.SchoolSATScores;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewYorkServiceApiClient {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private final NewYorkServiceInterface newYorkServiceInterface;

    @Inject
    public NewYorkServiceApiClient(NewYorkServiceInterface newYorkServiceInterface) {
        this.newYorkServiceInterface = newYorkServiceInterface;
    }

    public void getSchools(MutableLiveData<List<School>> liveData) {
        Call<List<School>> call = newYorkServiceInterface.listSchools();
        call.enqueue(new Callback<List<School>>() {
            @Override
            public void onResponse(Call<List<School>> call, Response<List<School>> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(response.body());
                    logger.atInfo().log("Fetched schools list from server.");
                } else {
                    liveData.postValue(null);
                    logger.atWarning().log("Failure received while fetching schools list");
                }
            }

            @Override
            public void onFailure(Call<List<School>> call, Throwable t) {
                liveData.postValue(null);
                logger.atSevere().withCause(t).log("Failure connecting to server");
            }
        });
    }

    public void getSchoolSATScores(MutableLiveData<SchoolSATScores> liveData, String dbn) {
        Call<List<SchoolSATScores>> call = newYorkServiceInterface.getSATScores(dbn);
        call.enqueue(new Callback<List<SchoolSATScores>>() {
            @Override
            public void onResponse(
                    Call<List<SchoolSATScores>> call,
                    Response<List<SchoolSATScores>> response) {
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        liveData.postValue(response.body().get(0));
                        logger.atInfo().log("Fetched SAT scores for school: "
                                + response.body().get(0).getSchoolName());
                    } else {
                        liveData.postValue(null);
                        logger.atWarning().log("Empty SAT score list from server");
                    }
                } else {
                    liveData.postValue(null);
                    logger.atWarning().log("Failure received while fetching SAT scores");
                }

            }

            @Override
            public void onFailure(Call<List<SchoolSATScores>> call, Throwable t) {
                liveData.postValue(null);
                logger.atSevere().withCause(t).log("Failure connecting to server");
            }
        });
    }
}
