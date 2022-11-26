package com.example.bottomnavbarwithnavgation.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.bottomnavbarwithnavgation.R;
import com.example.bottomnavbarwithnavgation.databinding.FragmentManageBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class Manage extends Fragment {
    FragmentManageBinding fragmentManageBinding;
    String categoryName;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentManageBinding =FragmentManageBinding.inflate(inflater,container,false);
        return fragmentManageBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("please Wait");
        progressDialog.setCancelable(false);
        fragmentManageBinding.addBtn.setOnClickListener(v -> {
            validData();
        });
    }

    private void validData() {
        categoryName = fragmentManageBinding.categoryNameEditText.getText().toString();
        if (TextUtils.isEmpty(categoryName)){
            fragmentManageBinding.categoryNameEditText.setError("enter category name");
            fragmentManageBinding.categoryNameEditText.requestFocus();
            Toast.makeText(getContext(), "please enter category name", Toast.LENGTH_SHORT).show();
        }else{
            uploadCategoryToFb();
        }
    }

    private void uploadCategoryToFb() {
        long timeStamp = System.currentTimeMillis();

        progressDialog.setMessage("Adding Category");
        progressDialog.show();
        Map<String,Object> map = new HashMap<>();
        map.put("id",timeStamp);
        map.put("category",categoryName);
        map.put("timestamp",timeStamp);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Categories");
        databaseReference.child(""+timeStamp)
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Added Successfully", Toast.LENGTH_SHORT).show();
                        fragmentManageBinding.categoryNameEditText.setText("");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "message "+e, Toast.LENGTH_SHORT).show();

                    }
                });


    }
}