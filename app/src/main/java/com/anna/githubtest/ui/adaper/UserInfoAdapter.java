package com.anna.githubtest.ui.adaper;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.anna.githubtest.R;
import com.anna.githubtest.data.ListUsers;
import com.anna.githubtest.databinding.ListItemUserViewBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserInfoAdapter extends ListAdapter<ListUsers, UserInfoAdapter.ItemViewHolder> implements Filterable {

    private ValueFilter valueFilter;
    private List<ListUsers> defaultFilterList = new ArrayList<>();
    private List<ListUsers> listUserItems = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public UserInfoAdapter(@NonNull DiffUtil.ItemCallback<ListUsers> diffCallback) {
        super(diffCallback);
    }

    public void addOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setData(List<ListUsers> list) {
        listUserItems = list;
        defaultFilterList = list;
        submitList(list);
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemUserViewBinding binding = ListItemUserViewBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ListUsers user = listUserItems.get(position);
        holder.bindData(user);
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ListItemUserViewBinding binding;

        public ItemViewHolder(ListItemUserViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindData(ListUsers user) {
            Glide.with(itemView.getContext())
                    .load(user.getImageUrl())
                    .transform(new RoundedCorners(100))
                    .into(binding.imagePhoto);

            binding.tvId.setText(String.valueOf(user.getId()));
            binding.tvUserName.setText(user.getLoginID());
            binding.tvSiteAdmin.setText(
                    itemView.getContext().getString(R.string.site_admin, String.valueOf(user.isSiteAdmin())));

            itemView.setOnClickListener(v -> onItemClickListener.openDetailPage(user.getLoginID()));
        }
    }

    public class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();

            if (TextUtils.isEmpty(charSequence)) {
                filterResults.count = defaultFilterList.size();
                filterResults.values = defaultFilterList;

            } else {
                List<ListUsers> filterList = defaultFilterList.stream()
                        .filter(keyword -> keyword.getLoginID().toLowerCase().contains(charSequence)
                                || keyword.getLoginID().toUpperCase().contains(charSequence))
                        .collect(Collectors.toList());

                filterResults.count = filterList.size();
                filterResults.values = filterList;
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if (filterResults.values instanceof ArrayList) {
                List<ListUsers> resultValue = (List<ListUsers>) filterResults.values;
                listUserItems = resultValue;
                submitList(resultValue);
            }
        }
    }

    public static class DiffUtilCallback extends DiffUtil.ItemCallback<ListUsers> {

        @Override
        public boolean areItemsTheSame(@NonNull ListUsers oldItem, @NonNull ListUsers newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ListUsers oldItem, @NonNull ListUsers newItem) {
            return oldItem.getLoginID().equals(newItem.getLoginID());
        }
    }

    public interface OnItemClickListener {
        void openDetailPage(String LoginID);
    }
}
