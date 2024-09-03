package com.anna.githubtest.ui.adaper;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anna.githubtest.R;
import com.anna.githubtest.data.ListUsers;
import com.anna.githubtest.databinding.ListItemUserViewBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserInfoRVAdapter extends RecyclerView.Adapter<UserInfoRVAdapter.ItemViewHolder> implements Filterable {

    private ValueFilter valueFilter;
    private static final ArrayList<ListUsers> defaultKeyWords = new ArrayList<>();
    private static List<ListUsers> listUsersList;

    public UserInfoRVAdapter(List<ListUsers> listUsers) {
        listUsersList = listUsers;
        defaultKeyWords.addAll(listUsers);
    }

    public void setUserBasicList(List<ListUsers> list) {
        listUsersList.clear();
        listUsersList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    @Override
    public int getItemCount() {
        return listUsersList.size();
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
        ListUsers user = listUsersList.get(position);
        holder.bindData(user);
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {
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

            binding.tvUserName.setText(user.getUserName());
            binding.tvSiteAdmin.setText(
                    itemView.getContext().getString(R.string.site_admin, String.valueOf(user.getSiteAdmin())));
        }
    }


    class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();

            if (TextUtils.isEmpty(charSequence)) {
                filterResults.count = defaultKeyWords.size();
                filterResults.values = defaultKeyWords;

            } else {
                List<ListUsers> filterList = defaultKeyWords.stream()
                        .filter(keyword -> keyword.getUserName().toLowerCase().contains(charSequence)
                                || keyword.getUserName().toUpperCase().contains(charSequence))
                        .collect(Collectors.toList());

                filterResults.count = filterList.size();
                filterResults.values = filterList;
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if (filterResults.values instanceof ArrayList) {
                listUsersList = (List<ListUsers>) filterResults.values;
                notifyDataSetChanged();
            }
        }
    }
}
