package com.db.paging.paging;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;


public class ItemDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<ItemDataSource> itemDataSourceMutableLiveData;
    private ItemDataSource itemDataSource;

    public ItemDataSourceFactory(){
        this.itemDataSourceMutableLiveData=new MutableLiveData<>();
    }

    @NonNull
    @Override
    public DataSource create() {
        itemDataSource = new ItemDataSource();
        itemDataSourceMutableLiveData.postValue(itemDataSource);

        return itemDataSource;
    }

    public MutableLiveData<ItemDataSource> getItemDataSourceMutableLiveData(){
        return itemDataSourceMutableLiveData;
    }
}
