package com.anna.githubtest.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.anna.githubtest.R;
import com.anna.githubtest.data.DetailInfo;
import com.anna.githubtest.data.PublicRepos;
import com.anna.githubtest.databinding.ActivityDetailBinding;
import com.anna.githubtest.ui.adaper.DetailAdapter;
import com.anna.githubtest.ui.viewmodel.DetailViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private DetailViewModel detailViewModel;
    private String loginId;
    private static final String INTENT_EXTRA_KEY = "INTENT_EXTRA_KEY";

    public static Intent getActivityIntent(Context context, String loginId) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(INTENT_EXTRA_KEY, loginId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent() != null) {
            loginId = getIntent().getStringExtra(INTENT_EXTRA_KEY);
        }

        showProgress(true);
        initViewModel();
        viewObserve();
    }

    private void initViewModel() {
        detailViewModel = new ViewModelProvider(this,
                ViewModelProvider.Factory.from(DetailViewModel.initializer)).get(DetailViewModel.class);

        if (loginId != null) {
            detailViewModel.callApiGetUserDetail(loginId);
        }
    }

    private void viewObserve() {
        detailViewModel.getDetailInfo().observe(this, detailInfo -> {

            setupView(detailInfo);
            if (loginId != null) {
                detailViewModel.callApiPublicRepos(loginId);
            }
        });

        detailViewModel.getPublicRepos().observe(this, this::setupRecyclerView);
    }

    private void setupView(DetailInfo info) {
        checkFieldSetData(binding.tvUserName, info.getName());
        checkFieldSetData(binding.tvLoginId, info.getLogin());
        checkFieldSetData(binding.tvCompany, info.getCompany());
        checkFieldSetData(binding.tvLocation, info.getLocation());
        checkFieldSetData(binding.tvBlog, info.getBlog());

        if (info.getFollowers() != 0 && info.getFollowing() != 0) {
            binding.tvFollowers.setText(getString(R.string.text_followers_and_following,
                    String.valueOf(info.getFollowers()),
                    String.valueOf(info.getFollowing())
            ));
            binding.tvFollowers.setVisibility(View.VISIBLE);
        }

        Glide.with(this)
                .load(info.getImageUrl())
                .transform(new RoundedCorners(100))
                .into(binding.imagePhoto);
    }

    private void setupRecyclerView(List<PublicRepos> publicRepos) {
        showProgress(false);
        DetailAdapter detailAdapter = new DetailAdapter(publicRepos);
        binding.recyclerView.setAdapter(detailAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
    }


    private void checkFieldSetData(TextView textView, String param) {
        if (param != null && !param.isEmpty()) {
            textView.setText(param);
            textView.setVisibility(View.VISIBLE);
        }
    }

    private void showProgress(Boolean isShow) {
        if (isShow) {
            binding.contentLoadingProgressBar.show();
        } else {
            binding.contentLoadingProgressBar.hide();
        }

    }
}
