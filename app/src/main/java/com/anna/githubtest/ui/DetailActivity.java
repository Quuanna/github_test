package com.anna.githubtest.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

    public static final String INTENT_EXTRA_KEY = "INTENT_EXTRA_KEY";
    private ActivityDetailBinding binding;
    private DetailViewModel viewModel;
    private String loginId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getIntent() != null) {
            loginId = getIntent().getStringExtra(INTENT_EXTRA_KEY);
        }

        initViewModel();
        viewDataObserve();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this,
                ViewModelProvider.Factory.from(DetailViewModel.initializer)).get(DetailViewModel.class);
        if (loginId != null) {
            viewModel.callApiGetUserDetail(loginId);
        }
    }

    private void viewDataObserve() {
        viewModel.getUiState().observe(this, uiState -> {
            if (uiState instanceof UiState.Loading) {
                showProgress(true);
            } else if (uiState instanceof UiState.Error) {
                String message = ((UiState.Error) uiState).getMessage();
                showDialogMsg(message);
            } else if (uiState instanceof UiState.Success) {
                showProgress(false);
            }
        });

        viewModel.getDetailInfo().observe(this, detailInfo -> {
            setupView(detailInfo);
            if (loginId != null) {
                viewModel.callApiPublicRepos(loginId);
            }
        });

        viewModel.getPublicRepos().observe(this, this::setupRecyclerView);
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

    private void showDialogMsg(String errorMessage) {
        if (errorMessage != null) {
            new AlertDialog.Builder(this)
                    .setMessage(errorMessage)
                    .setPositiveButton(R.string.button_confirm, (dialog, i) -> {
                        finish();
                        dialog.dismiss();
                    }).show();
        }
    }
}
