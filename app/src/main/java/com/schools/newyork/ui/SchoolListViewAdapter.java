package com.schools.newyork.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.schools.newyork.R;
import com.schools.newyork.model.School;

import java.util.List;

public class SchoolListViewAdapter
        extends RecyclerView.Adapter<SchoolListViewAdapter.MyViewHolder> {

    List<School> schools;
    OnItemClickedListener onItemClickedListener;

    public void setSchoolItems(List<School> schools, OnItemClickedListener onItemClickedListener) {
        this.schools = schools;
        this.onItemClickedListener = onItemClickedListener;
    }

    @Override
    public SchoolListViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.list_row_view, parent, /* attachToRoot= */ false);
        return new SchoolListViewAdapter.MyViewHolder(view, onItemClickedListener);
    }

    @Override
    public void onBindViewHolder(SchoolListViewAdapter.MyViewHolder holder, int position) {
        if (schools == null || position >= schools.size()) return;
        holder.schoolName.setText(schools.get(position).getSchoolName());
    }

    @Override
    public int getItemCount() {
        if (schools == null) return 0;
        return schools.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView schoolName;

        public MyViewHolder(View itemView, OnItemClickedListener onItemClickedListener) {
            super(itemView);
            schoolName = itemView.findViewById(R.id.row_text_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickedListener != null
                            && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        onItemClickedListener.onItemClicked(getAdapterPosition());
                    }
                }
            });
        }

    }
}
