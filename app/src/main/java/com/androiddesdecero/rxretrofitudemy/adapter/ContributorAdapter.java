package com.androiddesdecero.rxretrofitudemy.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androiddesdecero.rxretrofitudemy.R;
import com.androiddesdecero.rxretrofitudemy.model.Contributor;

import java.util.List;

public class ContributorAdapter extends RecyclerView.Adapter<ContributorAdapter.ContributorViewHolder> {

    private List<Contributor> contributors;

    public ContributorAdapter(List<Contributor> contributors) {
        this.contributors = contributors;
    }

    @NonNull
    @Override
    public ContributorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_contributor, viewGroup, false);
        return new ContributorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContributorViewHolder contributorViewHolder, int i) {
        Contributor contributor = contributors.get(i);
        contributorViewHolder.tvName.setText(contributor.getLogin());
        contributorViewHolder.tvNumber.setText(contributor.getContributions()+"");
    }

    @Override
    public int getItemCount() {
        return contributors.size();
    }

    public void setData(List<Contributor> contributors){
        this.contributors = contributors;
        notifyDataSetChanged();
    }


    public class ContributorViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;
        private TextView tvNumber;

        public ContributorViewHolder (@NonNull View itemView){
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvNumber = itemView.findViewById(R.id.tvNumber);
        }
    }
}
