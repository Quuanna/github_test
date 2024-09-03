package com.anna.githubtest.ui;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.anna.githubtest.ui.adaper.UserInfoRVAdapter;
import com.anna.githubtest.databinding.ActivityMainBinding;
import com.anna.githubtest.ui.viewmodel.MainViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MainViewModel mainViewModel;
    private UserInfoRVAdapter userInfoRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView(binding);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewObserve();
    }

    private void viewObserve() {
        mainViewModel.getUserBasicList().observe(this, userList -> {
            userInfoRVAdapter.setUserBasicList(userList);
        });
    }

    private void initView(ActivityMainBinding binding) {
        userInfoRVAdapter = new UserInfoRVAdapter(new ArrayList<>());
        binding.recyclerView.setAdapter(userInfoRVAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        binding.searchView.setOnQueryTextListener(onQueryTextListener());
    }

    private SearchView.OnQueryTextListener onQueryTextListener() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mainViewModel.callApiGetListUsers();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                userInfoRVAdapter.getFilter().filter(newText);
                return false;
            }
        };
    }
}
