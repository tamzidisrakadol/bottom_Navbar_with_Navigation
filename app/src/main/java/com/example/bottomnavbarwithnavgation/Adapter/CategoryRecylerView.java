package com.example.bottomnavbarwithnavgation.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottomnavbarwithnavgation.databinding.CategoryItemLayoutBinding;
import com.example.bottomnavbarwithnavgation.model.CategoryModel;

import java.util.List;

public class CategoryRecylerView extends RecyclerView.Adapter<CategoryRecylerView.HolderCategory>{
    Context context;
    List<CategoryModel> categoryModelList;
    CategoryItemLayoutBinding categoryItemLayoutBinding;

    public CategoryRecylerView(Context context, List<CategoryModel> categoryModelList) {
        this.context = context;
        this.categoryModelList = categoryModelList;
    }

    @NonNull
    @Override
    public HolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        categoryItemLayoutBinding = CategoryItemLayoutBinding.inflate(LayoutInflater.from(context),parent,false);
        return new HolderCategory(categoryItemLayoutBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCategory holder, int position) {
        CategoryModel categoryModel = categoryModelList.get(position);
        holder.categoryTV.setText(categoryModel.getCategory());
    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    public class HolderCategory extends RecyclerView.ViewHolder{
        TextView categoryTV;
        public HolderCategory(@NonNull View itemView) {
            super(itemView);
            categoryTV = categoryItemLayoutBinding.categoryTV;
        }
    }
}
