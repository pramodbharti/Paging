package com.db.paging.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.db.paging.R;
import com.db.paging.databinding.ActivityMainBinding;
import com.db.paging.paging.ItemListAdapter;
import com.db.paging.viewmodel.ItemViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ItemListAdapter itemListAdapter;
    private ItemViewModel itemViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        itemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);

        binding.listItem.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        itemListAdapter=new ItemListAdapter(getApplicationContext());

        itemViewModel.getPagedListLiveData().observe(this,pagedList->
                itemListAdapter.submitList(pagedList));

        itemViewModel.getNetworkState().observe(this,networkState ->
                itemListAdapter.setNetworkState(networkState));

        binding.listItem.setAdapter(itemListAdapter);
    }
}
