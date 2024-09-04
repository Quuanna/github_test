package com.anna.githubtest.ui.adaper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anna.githubtest.data.PublicRepos;
import com.anna.githubtest.databinding.ListItemReposViewBinding;

import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ItemViewHolder> {

    private final List<PublicRepos> publicReposList;

    public DetailAdapter(List<PublicRepos> publicReposList) {
        this.publicReposList = publicReposList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemReposViewBinding binding = ListItemReposViewBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        PublicRepos repos = publicReposList.get(position);
        holder.bindData(repos);
    }

    @Override
    public int getItemCount() {
        return publicReposList.size();
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ListItemReposViewBinding binding;

        public ItemViewHolder(ListItemReposViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindData(PublicRepos repos) {
            binding.tvRepoName.setText(repos.getName());
            binding.chipVisibility.setText(repos.getVisibility());
            if (repos.getDescription() != null) {
                binding.tvDescription.setText(repos.getDescription());
                binding.tvDescription.setVisibility(View.VISIBLE);
            }
        }
    }

}
