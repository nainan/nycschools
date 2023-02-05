package com.schools.newyork.network;

import static com.schools.newyork.testutil.TestSchoolResponseUtil.TEST_CRITICAL_READING_AVG_1;
import static com.schools.newyork.testutil.TestSchoolResponseUtil.TEST_DBN_1;
import static com.schools.newyork.testutil.TestSchoolResponseUtil.TEST_DBN_2;
import static com.schools.newyork.testutil.TestSchoolResponseUtil.TEST_MATH_AVG_1;
import static com.schools.newyork.testutil.TestSchoolResponseUtil.TEST_NUM_SAT_TESTERS_1;
import static com.schools.newyork.testutil.TestSchoolResponseUtil.TEST_SCHOOL_NAME_1;
import static com.schools.newyork.testutil.TestSchoolResponseUtil.TEST_SCHOOL_NAME_2;
import static com.schools.newyork.testutil.TestSchoolResponseUtil.TEST_WRITING_AVG_1;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import androidx.lifecycle.MutableLiveData;

import com.schools.newyork.model.School;
import com.schools.newyork.model.SchoolSATScores;
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
public class NewYorkServiceApiClientTest {

    private static final int SERVER_TIMEOUT = 1000;
    @Rule
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);
    @Inject
    MockWebServer mockWebServer;
    @Inject
    NewYorkServiceApiClient newYorkServiceApiClient;

    @Before
    public void init() {
        hiltRule.inject();
    }

    @Test
    public void getSchools_updatesLiveData_successfully() throws InterruptedException {
        MutableLiveData<List<School>> liveData = new MutableLiveData<>();
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(TestSchoolResponseUtil.schoolResponse));

        newYorkServiceApiClient.getSchools(liveData);
        Thread.sleep(SERVER_TIMEOUT);
        ShadowLooper.idleMainLooper();

        assertNotNull(liveData.getValue());
        assertEquals(liveData.getValue().size(), 2);
        assertEquals(liveData.getValue().get(0).getDbn(), TEST_DBN_1);
        assertEquals(liveData.getValue().get(0).getSchoolName(), TEST_SCHOOL_NAME_1);
        assertEquals(liveData.getValue().get(1).getDbn(), TEST_DBN_2);
        assertEquals(liveData.getValue().get(1).getSchoolName(), TEST_SCHOOL_NAME_2);
    }

    @Test
    public void getSchools_failureResponse_updatesLiveDataToNull() throws InterruptedException {
        MutableLiveData<List<School>> liveData = new MutableLiveData<>();
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_BAD_GATEWAY));

        newYorkServiceApiClient.getSchools(liveData);
        Thread.sleep(SERVER_TIMEOUT);
        ShadowLooper.idleMainLooper();

        assertNull(liveData.getValue());
    }

    @Test
    public void getSchoolSATScores_updatesLiveData_successfully() throws InterruptedException {
        MutableLiveData<SchoolSATScores> liveData = new MutableLiveData<>();
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(TestSchoolResponseUtil.schoolSATScoreResponse));

        newYorkServiceApiClient.getSchoolSATScores(liveData, TEST_DBN_1);
        Thread.sleep(SERVER_TIMEOUT);
        ShadowLooper.idleMainLooper();

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
    public void getSchoolSATScores_failureResponse_updatesLiveDataToNull()
            throws InterruptedException {
        MutableLiveData<SchoolSATScores> liveData = new MutableLiveData<>();
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_BAD_GATEWAY));

        newYorkServiceApiClient.getSchoolSATScores(liveData, TEST_DBN_1);
        Thread.sleep(SERVER_TIMEOUT);
        ShadowLooper.idleMainLooper();

        assertNull(liveData.getValue());
    }
}
