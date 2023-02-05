package com.schools.newyork.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.schools.newyork.model.School;
import com.schools.newyork.network.NewYorkServiceApiClient;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for Schools list
 */
@HiltViewModel
public class SchoolListViewModel extends ViewModel {
    private NewYorkServiceApiClient newYorkServiceApiClient;
    private MutableLiveData<List<School>> schoolListLiveData = new MutableLiveData<>();

    @Inject
    public SchoolListViewModel(NewYorkServiceApiClient newYorkServiceApiClient) {
        this.newYorkServiceApiClient = newYorkServiceApiClient;
    }

    public MutableLiveData<List<School>> getSchoolListLiveData() {
        return schoolListLiveData;
    }

    public void getSchoolList() {
        newYorkServiceApiClient.getSchools(schoolListLiveData);
    }
}
