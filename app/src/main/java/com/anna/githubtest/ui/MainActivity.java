package com.anna.githubtest.ui;

import static com.anna.githubtest.ui.DetailActivity.getActivityIntent;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.anna.githubtest.ui.adaper.UserInfoAdapter;
import com.anna.githubtest.databinding.ActivityMainBinding;
import com.anna.githubtest.ui.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainViewModel mainViewModel;
    private UserInfoAdapter userInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
        initViewModel();
        viewObserve();
    }

    private void initView() {
        userInfoAdapter = new UserInfoAdapter(new UserInfoAdapter.DiffUtilCallback());
        userInfoAdapter.addOnItemClickListener(onItemClickListener());
        binding.recyclerView.setAdapter(userInfoAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        binding.searchView.setOnQueryTextListener(onQueryTextListener());
    }

    private void initViewModel() {
        mainViewModel = new ViewModelProvider(this,
                ViewModelProvider.Factory.from(MainViewModel.initializer)).get(MainViewModel.class);
        mainViewModel.callApiGetListUsers();
    }

    private void viewObserve() {
        mainViewModel.getUiState().observe(this, uiState -> {
            switch (uiState) {
                case ERROR:
                    showProgress(false);
                    Toast.makeText(this, "Network Request failed", Toast.LENGTH_LONG).show();
                    break;
                case LOADING:
                    showProgress(true);
                    break;
                case SUCCESS:
                    showProgress(false);

                    break;
            }
        });

        mainViewModel.getUserBasicList().observe(this, userList -> userInfoAdapter.setData(userList));
    }

    private UserInfoAdapter.OnItemClickListener onItemClickListener() {
        return loginID -> startActivity(getActivityIntent(this, loginID));
    }

    private SearchView.OnQueryTextListener onQueryTextListener() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                userInfoAdapter.getFilter().filter(newText);
                return false;
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
}