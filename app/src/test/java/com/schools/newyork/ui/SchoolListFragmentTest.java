package com.schools.newyork.ui;

import static com.schools.newyork.testutil.TestSchoolResponseUtil.TEST_SCHOOL_NAME_1;
import static com.schools.newyork.testutil.TestSchoolResponseUtil.TEST_SCHOOL_NAME_2;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;

import com.schools.newyork.R;
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
public class SchoolListFragmentTest {

    @Rule
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Inject
    MockWebServer mockWebServer;

    private SchoolListFragment schoolListFragment;
    private FragmentManager fragmentManager;

    @Before
    public void init() {
        hiltRule.inject();
    }

    @Test
    public void onResume_withSuccessfulServerResponse_updatesRecyclerView()
            throws InterruptedException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(TestSchoolResponseUtil.schoolResponse));

        setupFragment();
        Thread.sleep(1000);
        ShadowLooper.idleMainLooper();

        RecyclerView recyclerView = schoolListFragment.getView().findViewById(R.id.recycler_view);
        recyclerView.measure(
                View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED
        );
        recyclerView.layout(0, 0, 1000, 1000);
        assertNotNull(schoolListFragment);
        assertNotNull(recyclerView);
        assertNotNull(recyclerView.findViewHolderForAdapterPosition(0));
        assertNotNull(recyclerView.findViewHolderForAdapterPosition(1));
        assertEquals(
                ((TextView) recyclerView.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.row_text_view)).getText(),
                TEST_SCHOOL_NAME_1);
        assertEquals(((TextView) recyclerView.findViewHolderForAdapterPosition(1).itemView.findViewById(R.id.row_text_view)).getText(),
                TEST_SCHOOL_NAME_2);
    }

    @Test
    public void onResume_withBadResponse_doesNotUpdateRecyclerView() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_BAD_GATEWAY));

        setupFragment();
        Thread.sleep(1000);
        ShadowLooper.idleMainLooper();

        assertNotNull(schoolListFragment);

        RecyclerView recyclerView = schoolListFragment.getView().findViewById(R.id.recycler_view);
        recyclerView.measure(
                View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED
        );
        recyclerView.layout(0, 0, 1000, 1000);
        assertNotNull(schoolListFragment);
        assertNotNull(recyclerView);
        assertNull(recyclerView.findViewHolderForAdapterPosition(0));
        assertNull(recyclerView.findViewHolderForAdapterPosition(1));
    }

    @Test
    public void onItemClick_afterSuccessfulSetup_opensSchoolSATScoreFragment()
            throws InterruptedException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(TestSchoolResponseUtil.schoolResponse));
        setupFragment();
        Thread.sleep(1000);
        ShadowLooper.idleMainLooper();
        RecyclerView recyclerView = schoolListFragment.getView().findViewById(R.id.recycler_view);
        recyclerView.measure(
                View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED
        );
        recyclerView.layout(0, 0, 1000, 1000);

        recyclerView.findViewHolderForAdapterPosition(0).itemView.performClick();
        ShadowLooper.idleMainLooper();

        assertEquals(
                fragmentManager.findFragmentById(R.id.fragment_school_containter_view).getClass(),
                SchoolSATScoresFragment.class);
    }


    private void setupFragment() {
        ApplicationProvider.getApplicationContext()
                .setTheme(androidx.appcompat.R.style.Theme_AppCompat);
        MainActivity activity =
                Robolectric.buildActivity(MainActivity.class)
                        .create()
                        .start()
                        .resume()
                        .get();

        ShadowLooper.idleMainLooper();

        fragmentManager = activity.getSupportFragmentManager();
        schoolListFragment =
                (SchoolListFragment) fragmentManager.findFragmentById(
                        R.id.fragment_school_containter_view);
    }
}
