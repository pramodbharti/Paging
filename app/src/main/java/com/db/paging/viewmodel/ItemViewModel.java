package com.db.paging.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.db.paging.model.Item;
import com.db.paging.paging.ItemDataSource;
import com.db.paging.paging.ItemDataSourceFactory;
import com.db.paging.util.NetworkState;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ItemViewModel extends ViewModel {

    private Executor executor;
    private LiveData<NetworkState> networkStateLiveData;
    private LiveData<PagedList<Item>> pagedListLiveData;

    public ItemViewModel() {
        init();
    }


    private void init() {
        executor = Executors.newFixedThreadPool(5);
        ItemDataSourceFactory itemDataSourceFactory = new ItemDataSourceFactory();
        networkStateLiveData = Transformations.switchMap(itemDataSourceFactory
                        .getItemDataSourceMutableLiveData(),
                ItemDataSource::getNewtworkState);

        PagedList.Config pagedListConfig = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(10)
                .setPageSize(20).build();

        pagedListLiveData =  (new LivePagedListBuilder(itemDataSourceFactory,pagedListConfig))
                .setFetchExecutor(executor)
                .build();
    }


    public LiveData<NetworkState> getNetworkState(){
        return networkStateLiveData;
    }

    public LiveData<PagedList<Item>>  getPagedListLiveData(){
        return pagedListLiveData;
    }
}
