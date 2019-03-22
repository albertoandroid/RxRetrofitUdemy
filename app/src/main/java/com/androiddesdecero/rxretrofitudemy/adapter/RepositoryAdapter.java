package com.androiddesdecero.rxretrofitudemy.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androiddesdecero.rxretrofitudemy.R;
import com.androiddesdecero.rxretrofitudemy.model.GitHubRepo;

import java.util.List;


public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder> {

    private List<GitHubRepo> repos;

    public RepositoryAdapter(List<GitHubRepo> repos){
        this.repos = repos;
    }

    @android.support.annotation.NonNull
    @Override
    public RepositoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_repo,viewGroup, false);
        return new RepositoryViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull RepositoryViewHolder repositoryViewHolder, int i) {
        GitHubRepo repo = repos.get(i);
        repositoryViewHolder.tvRepositorio.setText(repo.getName());
        repositoryViewHolder.tvLenguaje.setText(repo.getLanguage());
        repositoryViewHolder.tvStars.setText(repo.getStargazers_count()+"");

    }

    @Override
    public int getItemCount() {
        return repos.size();
    }

    public void setData(List<GitHubRepo> repos){
        this.repos = repos;
        notifyDataSetChanged();
    }


    public class RepositoryViewHolder extends RecyclerView.ViewHolder{
        private TextView tvRepositorio;
        private TextView tvLenguaje;
        private TextView tvStars;

        private RepositoryViewHolder(@NonNull View itemView){
            super(itemView);
            tvRepositorio = itemView.findViewById(R.id.tvRepositorio);
            tvLenguaje = itemView.findViewById(R.id.tvLenguaje);
            tvStars = itemView.findViewById(R.id.tvStars);
        }
    }
}
