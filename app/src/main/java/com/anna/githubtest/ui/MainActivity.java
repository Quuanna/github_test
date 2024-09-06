package com.anna.githubtest.ui;

import static com.anna.githubtest.ui.DetailActivity.INTENT_EXTRA_KEY;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anna.githubtest.R;
import com.anna.githubtest.data.ListUsers;
import com.anna.githubtest.ui.adaper.ListUserAdapter;
import com.anna.githubtest.databinding.ActivityMainBinding;
import com.anna.githubtest.ui.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private ListUserAdapter listUserAdapter;
    private final List<ListUsers> listUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
        initViewModel();
        viewDataObserve();
    }

    private void initView() {
        listUserAdapter = new ListUserAdapter(new ListUserAdapter.DiffUtilCallback());
        listUserAdapter.addOnItemClickListener(onItemClickListener());
        binding.recyclerView.setAdapter(listUserAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        binding.recyclerView.addOnScrollListener(onScrollListener());
        binding.searchView.setOnQueryTextListener(onQueryTextListener());
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this,
                ViewModelProvider.Factory.from(MainViewModel.initializer)).get(MainViewModel.class);
        viewModel.callApiGetListUsers();
    }

    private void viewDataObserve() {
        uiStateObserve();
        userListObserve();
    }

    private void userListObserve() {
        viewModel.getUserList().observe(this, userList -> {
                    listUsers.addAll(userList);
                    List<ListUsers> newListUsers = listUsers.stream()
                            .distinct()
                            .sorted(Comparator.comparingInt(ListUsers::getId))
                            .collect(Collectors.toList());
                    listUserAdapter.setUpdateList(new ArrayList<>(newListUsers));
                }
        );
    }

    private void uiStateObserve() {
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
    }

    private ListUserAdapter.OnItemClickListener onItemClickListener() {
        return loginID -> {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(INTENT_EXTRA_KEY, loginID);
            startActivity(intent);
        };
    }

    private SearchView.OnQueryTextListener onQueryTextListener() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listUserAdapter.getFilter().filter(newText);
                return false;
            }
        };
    }

    private RecyclerView.OnScrollListener onScrollListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                    if (lastVisibleItemPosition >= listUserAdapter.getItemCount() - 1) {
                        viewModel.callApiNextPageGetListUsers();
                    }
                }
            }
        };
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
                    .setPositiveButton(R.string.button_confirm, (dialog, i) -> dialog.dismiss())
                    .show();
        }
    }
}