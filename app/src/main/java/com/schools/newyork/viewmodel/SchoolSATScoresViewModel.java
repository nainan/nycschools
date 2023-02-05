package com.schools.newyork.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.schools.newyork.model.SchoolSATScores;
import com.schools.newyork.network.NewYorkServiceApiClient;
import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for School's SAT scores
 */
@HiltViewModel
public class SchoolSATScoresViewModel extends ViewModel {

    private NewYorkServiceApiClient newYorkServiceApiClient;
    private MutableLiveData<SchoolSATScores> schoolSATScoresLiveData = new MutableLiveData<>();

    @Inject
    public SchoolSATScoresViewModel(NewYorkServiceApiClient newYorkServiceApiClient) {
        this.newYorkServiceApiClient = newYorkServiceApiClient;
    }

    public MutableLiveData<SchoolSATScores> getSchoolSATScoresLiveData() {
        return schoolSATScoresLiveData;
    }

    public void getSchoolSATScores(String dbn) {
        newYorkServiceApiClient.getSchoolSATScores(schoolSATScoresLiveData, dbn);
    }

}
