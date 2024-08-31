package com.stargazing.suitcase.adapters;

import static com.stargazing.suitcase.utils.Utility.decode;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.stargazing.suitcase.database.entities.Item;
import com.stargazing.suitcase.R;
import com.stargazing.suitcase.databinding.ItemBinding;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> items;
    private final ItemClickListener listener;
    private final MyItemCallback myItemCallback;

    private static class MyItemCallback extends DiffUtil.ItemCallback<Item> {

        @Override
        public boolean areItemsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
            return oldItem.equals(newItem);
        }
    }

    public ItemAdapter(ItemClickListener listener) {
        this.items = new ArrayList<>();
        this.listener = listener;
        this.myItemCallback = new MyItemCallback();

    }

    public void updateListItems(List<Item> itemList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return items.size();
            }

            @Override
            public int getNewListSize() {
                return itemList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return myItemCallback.areItemsTheSame(items.get(oldItemPosition), itemList.get(newItemPosition));
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return myItemCallback.areContentsTheSame(items.get(oldItemPosition), itemList.get(newItemPosition));
            }
        });
        diffResult.dispatchUpdatesTo(this);
        this.items = itemList;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var binding = ItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        var item = items.get(position);
        holder.binding.tvDesc.setText(item.getDescription());
        holder.binding.tvQuantity.setText(String.valueOf(item.getQuantity()));
        holder.binding.tvPrice.setText(String.valueOf(item.getPrice()));
        if (item.isFinish())
            holder.binding.ivFinish.setImageResource(R.drawable.ic_checked);
        else
            holder.binding.ivFinish.setImageResource(R.drawable.ic_unchecked);

        if (item.getImage() != null) {
            holder.binding.ivItem.setImageBitmap(decode(item.getImage()));
        }

        holder.binding.cvItem.setOnClickListener(v -> {
            listener.onItemClicked(item);
        });
        holder.binding.ivFinish.setOnClickListener(v -> {
            item.setFinish(!item.isFinish());
            listener.onFinishItemClicked(item);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private ItemBinding binding;

        public ItemViewHolder(ItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
