package com.schools.newyork.ui;

import static com.schools.newyork.testutil.TestSchoolResponseUtil.TEST_CRITICAL_READING_AVG_1;
import static com.schools.newyork.testutil.TestSchoolResponseUtil.TEST_DBN_1;
import static com.schools.newyork.testutil.TestSchoolResponseUtil.TEST_MATH_AVG_1;
import static com.schools.newyork.testutil.TestSchoolResponseUtil.TEST_NUM_SAT_TESTERS_1;
import static com.schools.newyork.testutil.TestSchoolResponseUtil.TEST_SCHOOL_NAME_1;
import static com.schools.newyork.testutil.TestSchoolResponseUtil.TEST_WRITING_AVG_1;
import static com.schools.newyork.ui.SchoolSATScoresFragment.CRITICAL_READING_SCORE_STRING;
import static com.schools.newyork.ui.SchoolSATScoresFragment.MATH_SCORE_STRING;
import static com.schools.newyork.ui.SchoolSATScoresFragment.SAT_TEST_TAKERS_STRING;
import static com.schools.newyork.ui.SchoolSATScoresFragment.WRITING_SCORE_STRING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.test.core.app.ApplicationProvider;

import com.schools.newyork.R;
import com.schools.newyork.network.NewYorkServiceApiClient;
import com.schools.newyork.testutil.TestSchoolResponseUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
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
public class SchoolSATScoresFragmentTest {

    @Rule
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Inject
    MockWebServer mockWebServer;

    private SchoolSATScoresFragment schoolSATScoresFragment;

    @Before
    public void init() {
        hiltRule.inject();
    }

    @Test
    public void onResume_withSuccessfulServerSATResponse_updatesTextViews()
            throws InterruptedException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(TestSchoolResponseUtil.schoolResponse));
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(TestSchoolResponseUtil.schoolSATScoreResponse));

        setupFragment();
        Thread.sleep(1000);
        ShadowLooper.idleMainLooper();

        assertNotNull(schoolSATScoresFragment);
        assertEquals(TEST_SCHOOL_NAME_1,
                getTextFromTextView(R.id.school_name));
        assertEquals(SAT_TEST_TAKERS_STRING + TEST_NUM_SAT_TESTERS_1,
                getTextFromTextView(R.id.num_sat_test_takers));
        assertEquals(
                CRITICAL_READING_SCORE_STRING + TEST_CRITICAL_READING_AVG_1,
                getTextFromTextView(R.id.sat_critical_reading_avg_score));
        assertEquals(MATH_SCORE_STRING + TEST_MATH_AVG_1,
                getTextFromTextView(R.id.sat_math_avg_score));
        assertEquals(WRITING_SCORE_STRING + TEST_WRITING_AVG_1,
                getTextFromTextView(R.id.sat_writing_avg_score));
    }

    @Test
    public void onResume_withBadResponse_resetsTextViews() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(TestSchoolResponseUtil.schoolResponse));
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_BAD_GATEWAY));

        setupFragment();
        Thread.sleep(1000);
        ShadowLooper.idleMainLooper();

        assertNotNull(schoolSATScoresFragment);
        assertTrue(getTextFromTextView(R.id.school_name).isEmpty());
        assertTrue(getTextFromTextView(R.id.num_sat_test_takers).isEmpty());
        assertTrue(getTextFromTextView(R.id.sat_critical_reading_avg_score).isEmpty());
        assertTrue(getTextFromTextView(R.id.sat_math_avg_score).isEmpty());
        assertTrue(getTextFromTextView(R.id.sat_writing_avg_score).isEmpty());
    }

    private void setupFragment() {
        ApplicationProvider.getApplicationContext().setTheme(androidx.appcompat.R.style.Theme_AppCompat);
        MainActivity activity = Robolectric.buildActivity(MainActivity.class)
                .create()
                .start()
                .resume()
                .get();

        Bundle fragmentBundle = new Bundle();
        fragmentBundle.putString("dbn", TEST_DBN_1);
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_school_containter_view,
                SchoolSATScoresFragment.class,
                fragmentBundle);
        fragmentTransaction.commit();

        ShadowLooper.idleMainLooper();

        schoolSATScoresFragment =
                (SchoolSATScoresFragment) fragmentManager.findFragmentById(
                        R.id.fragment_school_containter_view);
    }

    private String getTextFromTextView(int id) {
        return ((TextView) schoolSATScoresFragment.getView().findViewById(id)).getText().toString();
    }
}