package com.db.paging.paging;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.db.paging.databinding.NetworkStateBinding;
import com.db.paging.databinding.ViewListBinding;
import com.db.paging.model.Item;
import com.db.paging.util.NetworkState;

import java.util.List;

public class ItemListAdapter extends PagedListAdapter<Item, RecyclerView.ViewHolder> {

    private static final int TYPE_PROGRESS = 0;
    private static final int TYPE_ITEM = 1;

    private Context context;
    private NetworkState networkState;


    public ItemListAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder){
            ((ItemViewHolder) holder).bindItem(getItem(position));
        }else {
            ((NetworkStateViewHolder) holder).bindView(networkState);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if(viewType==TYPE_PROGRESS){
            NetworkStateBinding networkStateBinding = NetworkStateBinding.inflate(layoutInflater,parent,false);
            return new NetworkStateViewHolder(networkStateBinding);
        }else {
            ViewListBinding viewListBinding = ViewListBinding.inflate(layoutInflater,parent,false);
            return new ItemViewHolder(viewListBinding);
        }
    }

    private static DiffUtil.ItemCallback<Item> DIFF_CALLBACK = new DiffUtil.ItemCallback<Item>() {
        @Override
        public boolean areItemsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
            return oldItem.equals(newItem);
        }
    };

    public void setNetworkState(NetworkState networkState) {
        NetworkState previousState = this.networkState;
        boolean previousExtraRow = hasExtraRow();
        this.networkState = networkState;
        boolean newExtra = hasExtraRow();
        if (previousExtraRow != newExtra) {
            if (previousExtraRow) {
                notifyItemRemoved(getItemCount());
            } else {
                notifyItemInserted(getItemCount());
            }
        } else {
            notifyItemChanged(getItemCount() - 1);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return TYPE_PROGRESS;
        } else {
            return TYPE_ITEM;
        }
    }

    private boolean hasExtraRow() {
        return networkState != null && networkState != NetworkState.LOADED;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private ViewListBinding binding;

        public ItemViewHolder(ViewListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindItem(Item item) {
            binding.idValue.setText(item.getId().toString());
            binding.nameValue.setText(item.getName());
            binding.fullNameValue.setText(item.getFullName());
        }
    }


    public class NetworkStateViewHolder extends RecyclerView.ViewHolder {

        private NetworkStateBinding binding;

        public NetworkStateViewHolder(NetworkStateBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(NetworkState networkState) {
            if (networkState != null && networkState.getStatus() == NetworkState.Status.RUNNING) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }

            if (networkState != null && networkState.getStatus() == NetworkState.Status.FAILED) {
                binding.errorMsg.setVisibility(View.VISIBLE);
                binding.errorMsg.setText(networkState.getMsg());
            } else {
                binding.errorMsg.setVisibility(View.GONE);
            }
        }
    }
}
