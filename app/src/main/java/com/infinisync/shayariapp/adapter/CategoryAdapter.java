package com.infinisync.shayariapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.infinisync.shayariapp.R;
import com.infinisync.shayariapp.databinding.ItemCategoryBinding;
import com.infinisync.shayariapp.model.CategoryModel;
import com.infinisync.shayariapp.view.ShayariActivity;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    Context context;
    ArrayList<CategoryModel> categories;

    public CategoryAdapter(Context context, ArrayList<CategoryModel> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryModel category = categories.get(position);
        holder.binding.categoryText.setText(category.getCategoryName());
        Glide.with(context)
                .load(category.getCategoryImage())
                .into(holder.binding.categoryImage);

        holder.binding.categoryImage.setBackgroundColor(Color.parseColor(category.getCategoryColor()));
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ShayariActivity.class);
            intent.putExtra("categoryId", category.getCategoryId());
            intent.putExtra("categoryName", category.getCategoryName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        ItemCategoryBinding binding;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCategoryBinding.bind(itemView);
        }
    }
}
