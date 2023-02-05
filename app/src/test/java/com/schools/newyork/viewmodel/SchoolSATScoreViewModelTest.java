package com.schools.newyork.viewmodel;

import static com.schools.newyork.testutil.TestSchoolResponseUtil.TEST_CRITICAL_READING_AVG_1;
import static com.schools.newyork.testutil.TestSchoolResponseUtil.TEST_DBN_1;
import static com.schools.newyork.testutil.TestSchoolResponseUtil.TEST_MATH_AVG_1;
import static com.schools.newyork.testutil.TestSchoolResponseUtil.TEST_NUM_SAT_TESTERS_1;
import static com.schools.newyork.testutil.TestSchoolResponseUtil.TEST_SCHOOL_NAME_1;
import static com.schools.newyork.testutil.TestSchoolResponseUtil.TEST_WRITING_AVG_1;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import androidx.lifecycle.MutableLiveData;

import com.schools.newyork.model.SchoolSATScores;
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

import javax.inject.Inject;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import dagger.hilt.android.testing.HiltTestApplication;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@HiltAndroidTest
@Config(application = HiltTestApplication.class)
@RunWith(RobolectricTestRunner.class)
public class SchoolSATScoreViewModelTest {

    @Rule
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Inject
    MockWebServer mockWebServer;
    @Inject
    NewYorkServiceApiClient newYorkServiceApiClient;

    private SchoolSATScoresViewModel schoolSATScoresViewModel;

    @Before
    public void init() {
        hiltRule.inject();
        schoolSATScoresViewModel = new SchoolSATScoresViewModel(newYorkServiceApiClient);
    }

    @Test
    public void getSchoolSATScores_withSuccessResponse_updatesSchoolSATScoreLiveDataSuccessfully()
            throws InterruptedException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(TestSchoolResponseUtil.schoolSATScoreResponse));

        schoolSATScoresViewModel.getSchoolSATScores(TEST_DBN_1);
        Thread.sleep(1000);
        ShadowLooper.idleMainLooper();

        MutableLiveData<SchoolSATScores> liveData =
                schoolSATScoresViewModel.getSchoolSATScoresLiveData();
        assertNotNull(liveData.getValue());
        assertEquals(liveData.getValue().getDbn(), TEST_DBN_1);
        assertEquals(liveData.getValue().getSchoolName(), TEST_SCHOOL_NAME_1);
        assertEquals(liveData.getValue().getNumOfSatTestTakers(), TEST_NUM_SAT_TESTERS_1);
        assertEquals(
                liveData.getValue().getSatCriticalReadingAvgScore(),
                TEST_CRITICAL_READING_AVG_1);
        assertEquals(liveData.getValue().getSatMathAvgScore(), TEST_MATH_AVG_1);
        assertEquals(liveData.getValue().getSatWritingAvgScore(), TEST_WRITING_AVG_1);
    }

    @Test
    public void getSchoolList_withFailureResponse_updatesSchoolListLiveDataToNull()
            throws InterruptedException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_BAD_GATEWAY));

        schoolSATScoresViewModel.getSchoolSATScores(TEST_DBN_1);
        Thread.sleep(1000);
        ShadowLooper.idleMainLooper();

        MutableLiveData<SchoolSATScores> liveData =
                schoolSATScoresViewModel.getSchoolSATScoresLiveData();
        assertNull(liveData.getValue());
    }
}
