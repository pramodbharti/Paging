package com.db.paging.paging;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.db.paging.model.GithubResponse;
import com.db.paging.model.Item;
import com.db.paging.network.RetrofitClient;
import com.db.paging.util.NetworkState;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ItemDataSource extends PageKeyedDataSource<Integer, Item> {

    private static final String QUERY = "Android";
    public static final int FIRST_PAGE = 1;
    private static final int PER_PAGE = 10;

    private RetrofitClient retrofitClient;
    private MutableLiveData<NetworkState> networkState;
    private MutableLiveData<NetworkState> initialLoading;


    public ItemDataSource() {
        retrofitClient = RetrofitClient.getInstance();
        networkState = new MutableLiveData<NetworkState>();
        initialLoading = new MutableLiveData<>();
    }

    public MutableLiveData<NetworkState> getNewtworkState() {
        return networkState;
    }

    public MutableLiveData<NetworkState> getInitialLoading() {
        return initialLoading;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Item> callback) {
        initialLoading.postValue(NetworkState.LOADING);
        networkState.postValue(NetworkState.LOADING);

        retrofitClient.getApiService()
                .getSearchResults(QUERY,FIRST_PAGE,PER_PAGE)
                .enqueue(new Callback<GithubResponse>() {
                    @Override
                    public void onResponse(Call<GithubResponse> call, Response<GithubResponse> response) {
                        if(response.body()!=null){
                            callback.onResult(response.body().getItems(),null,FIRST_PAGE+1);
                        initialLoading.postValue(NetworkState.LOADED);
                        networkState.postValue(NetworkState.LOADED);
                        }else {
                            initialLoading.postValue(new NetworkState(NetworkState.Status.FAILED,response.message()));
                            networkState.postValue(new NetworkState(NetworkState.Status.FAILED,response.message()));
                        }
                    }

                    @Override
                    public void onFailure(Call<GithubResponse> call, Throwable t) {
                        String errorMessage = t.getMessage();
                        networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                    }
                });
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Item> callback) {

        networkState.postValue(NetworkState.LOADING);

        retrofitClient.getApiService()
                .getSearchResults(QUERY,params.key,params.requestedLoadSize)
                .enqueue(new Callback<GithubResponse>() {
                    @Override
                    public void onResponse(Call<GithubResponse> call, Response<GithubResponse> response) {
                        if(response.body()!=null){
                            Integer previousKey = (params.key>1)?params.key-1:null;
                            callback.onResult(response.body().getItems(),previousKey);
                        }else {
                            networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                        }
                    }

                    @Override
                    public void onFailure(Call<GithubResponse> call, Throwable t) {
                        String errorMessage = t.getMessage();
                        networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                    }
                });

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Item> callback) {

        Log.i(TAG, "Loading Rang " + params.key + " Count " + params.requestedLoadSize);

        networkState.postValue(NetworkState.LOADING);

        retrofitClient.getApiService()
                .getSearchResults(QUERY,params.key,params.requestedLoadSize)
                .enqueue(new Callback<GithubResponse>() {
                    @Override
                    public void onResponse(Call<GithubResponse> call, Response<GithubResponse> response) {
                        if(response.body()!=null){
                            Integer nextKey = (params.key.equals(response.body().getTotalCount())) ? null : params.key+1;
                            callback.onResult(response.body().getItems(),nextKey);
                        }else {
                            networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                        }
                    }

                    @Override
                    public void onFailure(Call<GithubResponse> call, Throwable t) {
                        String errorMessage = t.getMessage();
                        networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                    }

                });
    }
}
