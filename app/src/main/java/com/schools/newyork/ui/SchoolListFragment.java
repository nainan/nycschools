package com.schools.newyork.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.flogger.FluentLogger;
import com.schools.newyork.R;
import com.schools.newyork.model.School;
import com.schools.newyork.viewmodel.SchoolListViewModel;

import java.util.List;

/**
 * Fragment that displays the List of schools in NewYork.
 */
public class SchoolListFragment extends Fragment {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private List<School> schools;

    private RecyclerView recyclerView;
    private SchoolListViewAdapter schoolListViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_school_list,
                container,
                /* attachToRoot= */ false);
        schoolListViewAdapter = new SchoolListViewAdapter();
        initializeView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateSchoolsViewModel();
    }

    private void initializeView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(schoolListViewAdapter);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(view.getContext(),
                        DividerItemDecoration.VERTICAL));
    }

    private void updateSchoolsViewModel() {
        SchoolListViewModel viewModel = new ViewModelProvider(
                /* owner= */ requireActivity()).get(SchoolListViewModel.class);
        viewModel.getSchoolListLiveData().observe(
                /* owner= */requireActivity(),
                new Observer<List<School>>() {
            @Override
            public void onChanged(List<School> schools) {
                if (schools != null) {
                    SchoolListFragment.this.schools = schools;
                    schoolListViewAdapter.setSchoolItems(
                            schools,
                            SchoolListFragment.this::onItemClicked);
                    schoolListViewAdapter.notifyDataSetChanged();

                } else {
                    logger.atSevere().log("Error fetching data");
                }
            }
        });
        viewModel.getSchoolList();
    }

    private void onItemClicked(int position) {
        Bundle fragmentBundle = new Bundle();
        fragmentBundle.putString("dbn", schools.get(position).getDbn());
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_school_containter_view,
                SchoolSATScoresFragment.class,
                fragmentBundle);
        fragmentTransaction.addToBackStack(/* name= */ "school list");
        fragmentTransaction.commit();
    }
}