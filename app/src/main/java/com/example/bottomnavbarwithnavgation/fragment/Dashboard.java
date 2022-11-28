package com.example.bottomnavbarwithnavgation.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bottomnavbarwithnavgation.Adapter.CategoryRecylerView;
import com.example.bottomnavbarwithnavgation.R;
import com.example.bottomnavbarwithnavgation.databinding.FragmentDashboardBinding;
import com.example.bottomnavbarwithnavgation.model.CategoryModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Dashboard extends Fragment {
    FragmentDashboardBinding fragmentDashboardBinding;
    CategoryModel categoryModel;
    List<CategoryModel> categoryModelList;
    CategoryRecylerView categoryRecylerView;
    NavController navController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentDashboardBinding = FragmentDashboardBinding.inflate(inflater,container,false);
        return fragmentDashboardBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= Navigation.findNavController(view);
        categoryModelList = new ArrayList<>();
        loadCategories();
    }

    private void loadCategories() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Categories");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryModelList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    categoryModel = ds.getValue(CategoryModel.class);
                    categoryModelList.add(categoryModel);
                }
                categoryRecylerView = new CategoryRecylerView(getContext(),categoryModelList);
                fragmentDashboardBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
                fragmentDashboardBinding.recyclerView.setAdapter(categoryRecylerView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}