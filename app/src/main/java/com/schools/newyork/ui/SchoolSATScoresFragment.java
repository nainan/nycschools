package com.schools.newyork.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.common.flogger.FluentLogger;
import com.schools.newyork.R;
import com.schools.newyork.model.SchoolSATScores;
import com.schools.newyork.viewmodel.SchoolSATScoresViewModel;

/**
 * Fragment that displays the SAT Scores of
 * an individual school.
 */
public class SchoolSATScoresFragment extends Fragment {

    @VisibleForTesting
    static final String SAT_TEST_TAKERS_STRING = "Number of SAT test takers: ";
    @VisibleForTesting
    static final String CRITICAL_READING_SCORE_STRING = "Critical Reading Average SAT Score: ";
    @VisibleForTesting
    static final String MATH_SCORE_STRING = "Math Average SAT Score: ";
    @VisibleForTesting
    static final String WRITING_SCORE_STRING = "Writing AVG SAT Score: ";
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();
    private String dbn;

    private TextView schoolNameTextView;
    private TextView numberOfTestTakersTextView;
    private TextView criticalReadingAvgScoreTextView;
    private TextView mathAvgScoreTextView;
    private TextView writingAvgScoreTextView;
    private TextView noDataAvailableTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_school,
                container,
                /* attachToRoot= */ false);
        initializeView(view);
        return view;
    }

    private void initializeView(View view) {
        schoolNameTextView = view.findViewById(R.id.school_name);
        numberOfTestTakersTextView = view.findViewById(R.id.num_sat_test_takers);
        criticalReadingAvgScoreTextView = view.findViewById(R.id.sat_critical_reading_avg_score);
        mathAvgScoreTextView = view.findViewById(R.id.sat_math_avg_score);
        writingAvgScoreTextView = view.findViewById(R.id.sat_writing_avg_score);
        noDataAvailableTextView = view.findViewById(R.id.data_status);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.dbn = getArguments().getString("dbn");
        resetView();
        updateSchoolsViewModel();

    }

    private void resetView() {
        schoolNameTextView.setText("");
        numberOfTestTakersTextView.setText("");
        criticalReadingAvgScoreTextView.setText("");
        mathAvgScoreTextView.setText("");
        writingAvgScoreTextView.setText("");
        noDataAvailableTextView.setVisibility(View.GONE);
    }

    private void updateSchoolsViewModel() {
        SchoolSATScoresViewModel viewModel =
                new ViewModelProvider(
                        /* owner= */ requireActivity()).get(SchoolSATScoresViewModel.class);
        viewModel
                .getSchoolSATScoresLiveData()
                .observe(/* owner= */requireActivity(), new Observer<SchoolSATScores>() {
                    @Override
                    public void onChanged(SchoolSATScores school) {
                        if (school != null && school.getDbn().equals(dbn)) {
                            schoolNameTextView.setText(school.getSchoolName());
                            numberOfTestTakersTextView.setText(
                                    SAT_TEST_TAKERS_STRING + school.getNumOfSatTestTakers());
                            criticalReadingAvgScoreTextView.setText(
                                    CRITICAL_READING_SCORE_STRING + school.getSatCriticalReadingAvgScore());
                            mathAvgScoreTextView.setText(
                                    MATH_SCORE_STRING + school.getSatMathAvgScore());
                            writingAvgScoreTextView.setText(
                                    WRITING_SCORE_STRING + school.getSatWritingAvgScore());
                            noDataAvailableTextView.setVisibility(View.GONE);
                        } else {
                            noDataAvailableTextView.setVisibility(View.VISIBLE);
                            logger.atWarning().log("Error fetching data");
                        }
                    }
                });
        viewModel.getSchoolSATScores(dbn);
    }
}