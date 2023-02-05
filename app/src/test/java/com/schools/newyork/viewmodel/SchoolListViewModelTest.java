package com.schools.newyork.viewmodel;

import static com.schools.newyork.testutil.TestSchoolResponseUtil.TEST_DBN_1;
import static com.schools.newyork.testutil.TestSchoolResponseUtil.TEST_DBN_2;
import static com.schools.newyork.testutil.TestSchoolResponseUtil.TEST_SCHOOL_NAME_1;
import static com.schools.newyork.testutil.TestSchoolResponseUtil.TEST_SCHOOL_NAME_2;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import androidx.lifecycle.MutableLiveData;

import com.schools.newyork.model.School;
import com.schools.newyork.network.NewYorkServiceApiClient;
import com.schools.newyork.testutil.TestSchoolResponseUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLooper;

import java.net.HttpURLConnection;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import dagger.hilt.android.testing.HiltTestApplication;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@HiltAndroidTest
@Config(application = HiltTestApplication.class)
@RunWith(RobolectricTestRunner.class)
public class SchoolListViewModelTest {

    @Rule
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Inject
    MockWebServer mockWebServer;
    @Inject
    NewYorkServiceApiClient newYorkServiceApiClient;

    private SchoolListViewModel schoolListViewModel;

    @Before
    public void init() {
        hiltRule.inject();
        schoolListViewModel = new SchoolListViewModel(newYorkServiceApiClient);
    }

    @Test
    public void getSchoolList_withSuccessResponse_updatesSchoolListLiveDataSuccessfully()
            throws InterruptedException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(TestSchoolResponseUtil.schoolResponse));

        schoolListViewModel.getSchoolList();
        Thread.sleep(1000);
        ShadowLooper.idleMainLooper();

        MutableLiveData<List<School>> liveData = schoolListViewModel.getSchoolListLiveData();
        assertNotNull(liveData.getValue());
        assertEquals(liveData.getValue().size(), 2);
        assertEquals(liveData.getValue().get(0).getDbn(), TEST_DBN_1);
        assertEquals(liveData.getValue().get(0).getSchoolName(), TEST_SCHOOL_NAME_1);
        assertEquals(liveData.getValue().get(1).getDbn(), TEST_DBN_2);
        assertEquals(liveData.getValue().get(1).getSchoolName(), TEST_SCHOOL_NAME_2);
    }

    @Test
    public void getSchoolList_withFailureResponse_updatesSchoolListLiveDataToNull()
            throws InterruptedException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_BAD_GATEWAY));

        schoolListViewModel.getSchoolList();
        Thread.sleep(1000);
        ShadowLooper.idleMainLooper();

        MutableLiveData<List<School>> liveData = schoolListViewModel.getSchoolListLiveData();
        assertNull(liveData.getValue());
    }
}
